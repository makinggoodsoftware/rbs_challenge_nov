package com.mgs.rbsnov.logic;

import com.mgs.rbsnov.domain.Card;
import com.mgs.rbsnov.domain.Player;

import java.util.*;

public class CardsDealer {
    private final CardsShuffler cardsShuffler;

    public CardsDealer(CardsShuffler cardsShuffler) {
        this.cardsShuffler = cardsShuffler;
    }

    public Map<Player, Set<Card>> deal(List<Player> toPlayers, Set<Card> fromCards) {
        SetRotator setRotator = newSetRotator(toPlayers);
        cardsShuffler.shuffle(fromCards).forEach(setRotator::accept);
        return setRotator.getSets();
    }

    public SetRotator newSetRotator(List<Player> forPlayers) {
        Map<Player, Set<Card>> toRotate = new HashMap<>();
        for (Player forPlayer : forPlayers) {
            toRotate.put(forPlayer, new HashSet<>());
        }

        return new SetRotator(toRotate, forPlayers);
    }

    public class SetRotator {
        private final Map<Player, Set<Card>> toRotate;
        private final List<Player> allPlayers;
        private int currentIndex = 0;
        private Player currentPlayer;

        public SetRotator(Map<Player, Set<Card>> toRotate, List<Player> allPlayers) {
            this.toRotate = toRotate;
            this.allPlayers = allPlayers;
            this.currentPlayer = allPlayers.get(currentIndex);
        }


        public void accept(Card toAccept) {
            getCurrentPlayer().add(toAccept);
            moveNext();
        }

        private void moveNext() {
            if (this.currentIndex + 1 >= toRotate.size()){
                this.currentIndex = 0;
            } else {
                this.currentIndex++;
            }
            this.currentPlayer = allPlayers.get(currentIndex);
        }

        public Set<Card> getCurrentPlayer() {
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
