package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.DealInProgress;
import com.mgs.rbsnov.domain.Suit;

import java.util.*;
import java.util.stream.Collectors;

public class CardsFilter {
    private final CardScorer cardScorer;

    public CardsFilter(CardScorer cardScorer) {
        this.cardScorer = cardScorer;
    }

    public Set<Card> bestCards(DealInProgress dealInProgress, Set<Card> playableCards) {
        Map<Suit, List<Card>> allPlayableCardsBySuit = playableCards.stream().collect(Collectors.groupingBy(Card::getSuit));
        return dealInProgress.getLeadingCard().isPresent() ?
                followingDeal(dealInProgress, allPlayableCardsBySuit) :
                startingDeal (allPlayableCardsBySuit);
    }

    private Set<Card> startingDeal(Map<Suit, List<Card>> playableCards) {
        return null;
    }

    private Set<Card> followingDeal(DealInProgress dealInProgress, Map<Suit, List<Card>> playableCardsBySuit) {
        if (playableCardsBySuit.containsKey(dealInProgress.getLeadingCard().get().getSuit())){
            return followingSuit(dealInProgress, playableCardsBySuit);
        }
        return discarding (dealInProgress, playableCardsBySuit);
    }

    private Set<Card> discarding(DealInProgress dealInProgress, Map<Suit, List<Card>> playableCardsBySuit) {
        Set<Card> bestCards = new HashSet<>();
        if (playableCardsBySuit.containsKey(Suit.SPADES) && playableCardsBySuit.get(Suit.SPADES).contains(Card.QUEEN_OF_SPADES)){
            bestCards.add(Card.QUEEN_OF_SPADES);
            return bestCards;
        }

        Map<Suit, List<Card>> positivePointCardsBySuit = positivePointCards(playableCardsBySuit);
        Map<Suit, Card> mostValueableCardsBySuit = mostValuableCards(positivePointCardsBySuit);
        return asSet(mostValueableCardsBySuit.values());
    }

    private Map<Suit, List<Card>> positivePointCards(Map<Suit, List<Card>> cardsByAllSuits) {
        Map<Suit,List<Card>> nonNegativePointCards = new HashMap<>();
        for (Map.Entry<Suit, List<Card>> cardsBySuit : cardsByAllSuits.entrySet()) {
            ArrayList<Card> positivePointCards = new ArrayList<>();
            nonNegativePointCards.put(cardsBySuit.getKey(), positivePointCards);
            positivePointCards.addAll(cardsBySuit.getValue().stream().filter(card -> cardScorer.score(card) >= 0).collect(Collectors.toList()));
        }
        return nonNegativePointCards;
    }

    private Set<Card> followingSuit(DealInProgress dealInProgress, Map<Suit, List<Card>> playableCards) {
        return null;
    }

    private Set<Card> asSet(Collection<Card> values) {
        Set<Card> set = new HashSet<>();
        set.addAll(values);
        return set;
    }

    private Map<Suit, Card> mostValuableCards(Map<Suit, List<Card>> allPlayableCardsBySuit) {
        Map<Suit, Card> filtered = new HashMap<>();
        for (Map.Entry<Suit, List<Card>> playableCardsBySuit : allPlayableCardsBySuit.entrySet()) {
            Suit thisSuit = playableCardsBySuit.getKey();
            List<Card> thisSuitCards = playableCardsBySuit.getValue();
            if (thisSuitCards.size() > 0){
                filtered.put(thisSuit, highestValue(thisSuitCards));
            }
        }
        return filtered;
    }

    private Card highestValue(List<Card> cards) {
        Collections.sort(cards, (left, right) -> right.getNumeration().higherThan(left.getNumeration()) ? 1: -1);
        return cards.get(0);
    }
}
