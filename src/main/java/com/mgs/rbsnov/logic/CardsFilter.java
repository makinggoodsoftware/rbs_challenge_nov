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
        Set<Card> startingDeal = new HashSet<>();
        for (Map.Entry<Suit, List<Card>> suitListEntry : playableCards.entrySet()) {
            Suit suit = suitListEntry.getKey();
            if (suit == Suit.HEARTS){
                processHearts(startingDeal, suitListEntry.getValue());
            } else {
                startingDeal.addAll(suitListEntry.getValue());
            }
        }

        return startingDeal;
    }

    private void processHearts(Set<Card> startingDeal, List<Card> hearts) {
        Card lowestHeart = sortAsc(hearts).get(hearts.size() - 1);
        startingDeal.add(lowestHeart);
    }

    private Set<Card> followingDeal(DealInProgress dealInProgress, Map<Suit, List<Card>> playableCardsBySuit) {
        Suit leadingSuit = dealInProgress.getLeadingCard().get().getSuit();
        if (playableCardsBySuit.containsKey(leadingSuit)){
            return followingSuit(dealInProgress, playableCardsBySuit.get(leadingSuit));
        }
        return discarding (dealInProgress, playableCardsBySuit);
    }

    private Set<Card> followingSuit(DealInProgress dealInProgress, List<Card> playableCards) {
        Card leadingCard = dealInProgress.getLeadingCard().get();
        List<Card> sortedSuitCards = sortAsc(playableCards);
        if (cantKill(leadingCard, sortedSuitCards)) {
            return singletonSet(sortedSuitCards.get(0));
        }
        return canKillFollowingSuit(leadingCard, sortedSuitCards);
    }

    private Set<Card> canKillFollowingSuit(Card leadingCard, List<Card> sortedSuitCards) {
        if (leadingCard.getSuit() == Suit.HEARTS) {
            return highestNonKillingOrLowest (leadingCard, sortedSuitCards);
        }
        Set<Card> maxJustBeforeAndAfter = new HashSet<>();
        Card maxValue = sortedSuitCards.get(0);
        maxJustBeforeAndAfter.add(maxValue);
        Card thisCard;
        Card previousCard = maxValue;
        for (int i=1; i<sortedSuitCards.size(); i++) {
            thisCard = sortedSuitCards.get(i);
            if (! thisCard.getNumeration().higherThan(leadingCard.getNumeration())){
                maxJustBeforeAndAfter.add(previousCard);
                maxJustBeforeAndAfter.add(thisCard);
                return maxJustBeforeAndAfter;
            }
            previousCard = thisCard;
        }
        maxJustBeforeAndAfter.add(sortedSuitCards.get(sortedSuitCards.size()-1));
        return maxJustBeforeAndAfter;
    }

    private Set<Card> highestNonKillingOrLowest(Card leadingCard, List<Card> sortedSuitCards) {
        Card lowestCard = sortedSuitCards.get(sortedSuitCards.size()-1);
        Card thisCard = lowestCard;
        Card nextCard;
        for (int i = sortedSuitCards.size()-2; i>=0; i--){
            nextCard = sortedSuitCards.get(i);
            if (nextCard.getNumeration().higherThan(leadingCard.getNumeration())){
                return singletonSet(thisCard);
            }
            thisCard = nextCard;
        }
        return singletonSet(lowestCard);
    }

    private boolean cantKill(Card leadingCard, List<Card> sortedSuitCards) {
        return ! sortedSuitCards.get(0).getNumeration().higherThan(leadingCard.getNumeration());
    }

    private Set<Card> singletonSet(Card card) {
        Set<Card> singletonSet = new HashSet<>();
        singletonSet.add(card);
        return singletonSet;
    }

    private Set<Card> all(Map<Suit, List<Card>> playableCards) {
        Set<Card> playableCardsAsSet = new HashSet<>();
        playableCards.values().forEach(playableCardsAsSet::addAll);
        return playableCardsAsSet;
    }

    private Set<Card> discarding(DealInProgress dealInProgress, Map<Suit, List<Card>> playableCardsBySuit) {
        Set<Card> bestCards = new HashSet<>();
        if (playableCardsBySuit.containsKey(Suit.SPADES) && playableCardsBySuit.get(Suit.SPADES).contains(Card.QUEEN_OF_SPADES)){
            bestCards.add(Card.QUEEN_OF_SPADES);
            return bestCards;
        }

        Map<Suit, List<Card>> positivePointCardsBySuit = positivePointCards(playableCardsBySuit);
        if (positivePointCardsBySuit.size() == 0) {
            return lowestValue (playableCardsBySuit);
        }
        Map<Suit, Card> mostValuableCardsBySuit = mostValuableCards(positivePointCardsBySuit);
        return asSet(mostValuableCardsBySuit.values());
    }

    private Map<Suit, List<Card>> positivePointCards(Map<Suit, List<Card>> cardsByAllSuits) {
        Map<Suit,List<Card>> nonNegativePointCards = new HashMap<>();
        for (Map.Entry<Suit, List<Card>> cardsBySuit : cardsByAllSuits.entrySet()) {
            List<Card> allPositive = cardsBySuit.getValue().stream().filter(card -> cardScorer.score(card) >= 0).collect(Collectors.toList());
            if (allPositive.size() > 0){
                ArrayList<Card> positivePointCards = new ArrayList<>();
                nonNegativePointCards.put(cardsBySuit.getKey(), positivePointCards);
                positivePointCards.addAll(allPositive);
            }
        }
        return nonNegativePointCards;
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

    private Set<Card> lowestValue(Map<Suit, List<Card>> playableCardsBySuit) {
        for (List<Card> cards : playableCardsBySuit.values()) {
            if (cards.size() > 0) {
                Collections.sort(cards, (left, right) -> right.getNumeration().higherThan(left.getNumeration()) ? -1: 0);
                Card lowestValue = cards.get(0);
                Set<Card> lowestValueAsSet = new HashSet<>();
                lowestValueAsSet.add(lowestValue);
                return lowestValueAsSet;
            }
        }
        throw new IllegalStateException();
    }

    private Card highestValue(List<Card> cards) {
        List<Card> sortedCards = sortAsc(cards);
        return sortedCards.get(0);
    }

    private List<Card> sortAsc(List<Card> cards) {
        List<Card> sortedCards = new ArrayList<>();
        sortedCards.addAll(cards);
        Collections.sort(sortedCards, (left, right) -> right.getNumeration().higherThan(left.getNumeration()) ? 1: -1);
        return sortedCards;
    }
}
