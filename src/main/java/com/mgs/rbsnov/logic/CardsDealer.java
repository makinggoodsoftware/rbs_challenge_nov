package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.Player;
import com.mgs.rbsnov.utils.ClosureValue;

import java.util.*;

public class CardsDealer {
    private final CardsShuffler cardsShuffler;

    public CardsDealer(CardsShuffler cardsShuffler) {
        this.cardsShuffler = cardsShuffler;
    }

    public Map<Player, Set<Card>> deal(List<Player> toPlayers, Set<Card> fromCards, Map<Player, Set<Card>> knownCards) {
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
        SetRotator setRotator = newSetRotator(toPlayers, initialSetup.get());
        cardsShuffler.shuffle(toDeal).forEach(setRotator::accept);
        return setRotator.getSets();
    }

    SetRotator newSetRotator(List<Player> forPlayers, Map<Player, Set<Card>> initialSetup) {
        Map<Player, Set<Card>> toRotate = new HashMap<>();
        for (Player forPlayer : forPlayers) {
            toRotate.put(forPlayer, new HashSet<>());
        }

        for (Map.Entry<Player, Set<Card>> initialSetupEntry : initialSetup.entrySet()) {
            Player player = initialSetupEntry.getKey();
            Set<Card> initialCards = initialSetupEntry.getValue();

            toRotate.get(player).addAll(initialCards);
        }
        return new SetRotator(toRotate, forPlayers);
    }

    class SetRotator {
        private final Map<Player, Set<Card>> toRotate;
        private final List<Player> allPlayers;
        private int currentIndex = 0;
        private int currentDeal = 0;
        private Player currentPlayer;

        public SetRotator(Map<Player, Set<Card>> toRotate, List<Player> allPlayers) {
            this.toRotate = toRotate;
            this.allPlayers = allPlayers;
            this.currentPlayer = allPlayers.get(currentIndex);
        }


        public void accept(Card toAccept) {
            Set<Card> currentCards = getCurrentPlayerCards();
            if (currentCards.size() > currentDeal){
                moveNext();
                accept(toAccept);
            } else {
                currentCards.add(toAccept);
                moveNext();
            }
        }

        private void moveNext() {
            if (this.currentIndex + 1 >= toRotate.size()){
                this.currentIndex = 0;
                this.currentDeal ++;
            } else {
                this.currentIndex++;
            }
            this.currentPlayer = allPlayers.get(currentIndex);
        }

        public Set<Card> getCurrentPlayerCards() {
            return toRotate.get(currentPlayer);
        }

        public Set<Card> get(int index) {
            return toRotate.get(index);
        }

        public Map<Player, Set<Card>> getSets() {
            return toRotate;
        }
    }

}
