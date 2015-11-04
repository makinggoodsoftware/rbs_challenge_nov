package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.DealInProgress;
import com.mgs.rbsnov.domain.Player;

import java.util.*;

import static java.util.Collections.singletonList;

public class DealInProgressFactory {
    public DealInProgress newJustStartedDeal (Player startingPlayer){
        return new DealInProgress(
                startingPlayer,
                Optional.empty(),
                new ArrayList<>(),
                Optional.of(startingPlayer.nextClockwise())
        );
    }

    public DealInProgress oneCardDeal (Player startingPlayer, Card firstCard){
        return new DealInProgress(
                startingPlayer,
                Optional.of(firstCard),
                new ArrayList<>(),
                Optional.of(startingPlayer.nextClockwise())
        );
    }

    public DealInProgress twoCardsDeal (Player startingPlayer, Card firstCard, Card secondCard){
        return new DealInProgress(
                startingPlayer,
                Optional.of(firstCard),
                singletonList(secondCard),
                Optional.of(startingPlayer.nextClockwise().nextClockwise())
        );
    }

    public DealInProgress threeCardsDeal (Player startingPlayer, Card firstCard, Card secondCard, Card thirdCard){
        return new DealInProgress(
                startingPlayer,
                Optional.of(firstCard),
                Arrays.asList(secondCard, thirdCard),
                Optional.of(startingPlayer.nextClockwise().nextClockwise())
        );
    }

    public DealInProgress fourCardsDeal (Player startingPlayer, Card firstCard, Card secondCard, Card thirdCard, Card fourthCard){
        return new DealInProgress(
                startingPlayer,
                Optional.of(firstCard),
                Arrays.asList(secondCard, thirdCard, fourthCard),
                Optional.empty()
        );
    }

    public DealInProgress next (DealInProgress dealInProgress, Card card){
        if (dealInProgress.isCompleted()) throw new IllegalStateException();


        Optional<Card> newLeadingCard;
        List<Card> newFollowingCards = new ArrayList<>();
        newFollowingCards.addAll(dealInProgress.getFollowingCards());
        if (dealInProgress.getLeadingCard().isPresent()) {
            newLeadingCard = dealInProgress.getLeadingCard();
            newFollowingCards.add(card);
        }
        else {
            newLeadingCard = Optional.of(card);
        }

        Optional<Player> newWaitingForPlayer = newFollowingCards.size() == 3 ? Optional.empty() : Optional.of(dealInProgress.getWaitingForPlayer().get().nextClockwise());
        return new DealInProgress(dealInProgress.getStartingPlayer(), newLeadingCard, newFollowingCards, newWaitingForPlayer);
    }
}
