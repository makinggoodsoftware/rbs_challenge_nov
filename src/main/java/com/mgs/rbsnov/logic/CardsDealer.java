package com.mgs.rbsnov.logic;

import com.google.common.collect.ImmutableList;
import com.mgs.rbsnov.domain.CantSatisfyRequirements;
import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.Player;
import com.mgs.rbsnov.domain.Suit;
import com.mgs.rbsnov.utils.ClosureValue;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class CardsDealer {
    private final Logger LOG = Logger.getLogger(CardsDealer.class);
    private final CardsShuffler cardsShuffler;
    private final CardsSetBuilder cardsSetBuilder;

    public CardsDealer(CardsShuffler cardsShuffler, CardsSetBuilder cardsSetBuilder) {
        this.cardsShuffler = cardsShuffler;
        this.cardsSetBuilder = cardsSetBuilder;
    }

    public Map<Player, Set<Card>> deal(List<Player> toPlayers, Set<Card> fromCards, Map<Player, Set<Card>> knownCards, Map<Player, Set<Suit>> missingSuits) throws CantSatisfyRequirements {
        Set<Card> toDeal = new HashSet<>();
        toDeal.addAll(fromCards);
        ClosureValue<Map<Player, Set<Card>>> initialSetup = new ClosureValue<>(new HashMap<>());
        knownCards.entrySet().stream().forEach(knownCardEntry -> {
            Player player = knownCardEntry.getKey();
            initialSetup.get().put(player, new HashSet<>());
            knownCardEntry.getValue().stream().
                    filter(toDeal::contains)
                    .forEach(knowCardInPlay -> {
                        initialSetup.get().get(player).add(knowCardInPlay);
                        toDeal.remove(knowCardInPlay);
                    });
        });
        return doRestrictions(toPlayers, missingSuits, toDeal, initialSetup.get(), cardsToDeal(toPlayers, fromCards.size()));
    }

    private Map<Player, Integer> cardsToDeal(List<Player> toPlayers, int size) {
        int baseNumber = size / toPlayers.size();
        int remainder = size % toPlayers.size();

        Map<Player, Integer> numberOfCardsToDeal = new HashMap<>();
        for (int i =0; i < toPlayers.size(); i++){
            Integer countOfCards = i < remainder ? baseNumber + 1 : baseNumber;
            numberOfCardsToDeal.put(toPlayers.get(i), countOfCards);
        }
        return numberOfCardsToDeal;
    }

    private Map<Player, Set<Card>> doRestrictions(List<Player> toPlayers, Map<Player, Set<Suit>> missingSuits, Set<Card> toDeal, Map<Player, Set<Card>> initialSetup, Map<Player, Integer> numberOfCardsToHave) throws CantSatisfyRequirements {
        Map<Player, Set<Card>> cards = new HashMap<>();
        List<Map.Entry<Player, Set<Suit>>> missingSuitsSorted = missingSuits.entrySet().stream().sorted(byNumberOfRestrictions()).collect(Collectors.toList());
        List<Player> remainingPlayers = new ArrayList<>();
        remainingPlayers.addAll(toPlayers);

        List<Requirement> requirements = new ArrayList<>();
        for (Map.Entry<Player, Set<Suit>> missingSuitEntry : missingSuitsSorted) {
            Player thisPlayer = missingSuitEntry.getKey();
            if (! toPlayers.contains(thisPlayer)) continue;

            Set<Card> allCards = getCards(toDeal, initialSetup, numberOfCardsToHave, thisPlayer, missingSuitEntry.getValue(), requirements);
            cards.put(thisPlayer, allCards);
            toDeal.removeAll(allCards);

            remainingPlayers.remove(thisPlayer);
        }
        for (Player remainingPlayer : remainingPlayers) {
            Set<Card> allCards = getCards(toDeal, initialSetup, numberOfCardsToHave, remainingPlayer, new HashSet<>(), requirements);
            cards.put(remainingPlayer, allCards);
            toDeal.removeAll(allCards);
        }

        if (toDeal.size() > 0 && requirements.size() == 0) throw new IllegalStateException();

        for (Requirement requirement : requirements) {
            Player playerRequiringCard = requirement.getPlayer();
            Player nextPlayer = playerRequiringCard;
            for (int i=0; i<requirement.getNumberOfCardsMissing(); i++){
                Card toGive = toDeal.iterator().next();
                toDeal.remove(toGive);

                Set<Card> candidates = new HashSet<>();
                Set<Card> allNextPlayerCards;
                int count = 0;
                do{
                    nextPlayer = nextPlayer.nextClockwise();
                    if (! toPlayers.contains(nextPlayer)) continue;

                    Set<Suit> nexPlayerMissingSuits = missingSuits.get(nextPlayer);
                    if (nexPlayerMissingSuits != null && ! nexPlayerMissingSuits.contains(toGive.getSuit())) {
                        allNextPlayerCards = cards.get(nextPlayer);
                        candidates = cardsSetBuilder.newSet(allNextPlayerCards).removeSuits(requirement.getMissingSuits()).build();
                    }
                    count ++;
                    if (count > 5) {
                        throw new CantSatisfyRequirements();
                    }
                }while (candidates.size() == 0);

                Card toTake = candidates.iterator().next();
                cards.get(playerRequiringCard).add(toTake);
                cards.get(nextPlayer).remove(toTake);
                cards.get(nextPlayer).add(toGive);
            }
        }

        return cards;
    }

    private Set<Card> getCards(Set<Card> toDeal, Map<Player, Set<Card>> initialSetup, Map<Player, Integer> numberOfCardsToHave, Player thisPlayer, Set<Suit> missingSuits, List<Requirement> requirements) {
        Set<Card> initialCards = initialSetup.getOrDefault(thisPlayer, new HashSet<>());
        Integer cardsToDeal = numberOfCardsToHave.get(thisPlayer);

        Set<Card> possibleCards = cardsSetBuilder.newSet(toDeal).removeSuits(missingSuits).build();
        ImmutableList<Card> shuffledPossibleCards = cardsShuffler.shuffle(possibleCards);

        if (initialCards == null || cardsToDeal == null){
            throw new IllegalStateException();
        }
        Integer remainingCards = cardsToDeal - initialCards.size();

        Set<Card> thisCards = new HashSet<>();
        thisCards.addAll(initialCards);
        if (remainingCards > shuffledPossibleCards.size()){
            thisCards.addAll(shuffledPossibleCards);
            requirements.add(new Requirement(thisPlayer, missingSuits, remainingCards - shuffledPossibleCards.size()));
        } else {
            thisCards.addAll(shuffledPossibleCards.subList(0, remainingCards));
        }

        return combine(thisCards, initialCards);
    }

    private Set<Card> combine(Set<Card> thisCards, Set<Card> initialCards) {
        Set<Card> combined = new HashSet<>();
        combined.addAll(thisCards);
        combined.addAll(initialCards);
        return combined;
    }

    private Comparator<Map.Entry<Player, Set<Suit>>> byNumberOfRestrictions() {
        return (left, right) -> new Integer(right.getValue().size()).compareTo(left.getValue().size());
    }

    private class Requirement {
        private final Player player;
        private final Set<Suit> missingSuits;
        private final int numberOfCardsMissing;

        public Requirement(Player player, Set<Suit> missingSuits, int numberOfCardsMissing) {
            this.player = player;
            this.missingSuits = missingSuits;
            this.numberOfCardsMissing = numberOfCardsMissing;
        }

        public Player getPlayer() {
            return player;
        }

        public Set<Suit> getMissingSuits() {
            return missingSuits;
        }

        public int getNumberOfCardsMissing() {
            return numberOfCardsMissing;
        }
    }
}
