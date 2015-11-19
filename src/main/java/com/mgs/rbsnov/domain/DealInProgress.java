package com.mgs.rbsnov.domain;

import com.google.common.base.Objects;

import java.util.List;
import java.util.Optional;

public class DealInProgress {
    private final Player startingPlayer;
    private final Optional<Card> leadingCard;
    private final List<Card> followingCards;
    private final Optional<Player> waitingForPlayer;


    public DealInProgress(Player startingPlayer, Optional<Card> leadingCard, List<Card> followingCards, Optional<Player> waitingForPlayer) {
        this.startingPlayer = startingPlayer;
        this.leadingCard = leadingCard;
        this.followingCards = followingCards;
        this.waitingForPlayer = waitingForPlayer;
    }

    public List<Card> getFollowingCards() {
        return followingCards;
    }

    public Optional<Player> getWaitingForPlayer() {
        return waitingForPlayer;
    }

    public Optional<Card> getLeadingCard() {
        return leadingCard;
    }

    public boolean hasStarted() {
        return leadingCard.isPresent();
    }

    public boolean isCompleted() {
        return !waitingForPlayer.isPresent();
    }

    public Player getStartingPlayer() {
        return startingPlayer;
    }

    public int getCardSize() {
        return ! leadingCard.isPresent() ? 0: followingCards.size() + 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DealInProgress)) return false;
        DealInProgress that = (DealInProgress) o;
        return startingPlayer == that.startingPlayer &&
                Objects.equal(leadingCard, that.leadingCard) &&
                Objects.equal(followingCards, that.followingCards) &&
                Objects.equal(waitingForPlayer, that.waitingForPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(startingPlayer, leadingCard, followingCards, waitingForPlayer);
    }

    @Override
    public String toString() {
        return "DealInProgress{" +
                "startingPlayer=" + startingPlayer +
                ", leadingCard=" + leadingCard +
                ", followingCards=" + followingCards +
                ", waitingForPlayer=" + waitingForPlayer +
                '}';
    }
}
