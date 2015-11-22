package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Player
import com.mgs.rbsnov.domain.Suit
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration(classes = Config)
class CardsDealerSpecification extends Specification {
    @Resource
    CardsDealer cardsDealer
    @Resource
    CardsSetBuilder cardsSetBuilder

    def "should remember who has initially discarded cards" (){
        when:
        Map<Player, Set<Card>> cards = cardsDealer.deal(
                Player.all(Player.SOUTH),
                cardsSetBuilder.allCards().findAll{it != Card.TWO_OF_CLUBS && it != Card.ACE_OF_CLUBS},
                [
                        (Player.EAST): [Card.ACE_OF_SPADES, Card.ACE_OF_DIAMONDS, Card.ACE_OF_CLUBS] as Set
                ],
                new HashMap<Player, Set<Suit>>()
        )

        then:
        cards[Player.NORTH].size() == 12
        cards[Player.EAST].size() == 12
        cards[Player.SOUTH].size() == 13
        cards[Player.WEST].size() == 13

        cards[Player.EAST].contains(Card.ACE_OF_SPADES)
        cards[Player.EAST].contains(Card.ACE_OF_DIAMONDS)
        ! cards[Player.EAST].contains(Card.ACE_OF_CLUBS)
    }


    def "should respect missing suits" (){
        when:
        Map<Player, Set<Card>> cards = cardsDealer.deal(
                Player.all(Player.SOUTH),
                cardsSetBuilder.allCards().findAll{it != Card.TWO_OF_CLUBS},
                new HashMap<Player, Set<Card>>(),
                [
                        (Player.EAST): [Suit.CLUBS, Suit.HEARTS, Suit.DIAMONDS] as Set
                ]
        )

        then:
        cards[Player.NORTH].size() == 13
        cards[Player.EAST].size() == 12
        cards[Player.SOUTH].size() == 13
        cards[Player.WEST].size() == 13

        cards[Player.EAST].findAll{it.suit == Suit.SPADES}.size() == 12
    }

    def "should respect missing suits II" (){
        when:
        Map<Player, Set<Card>> cards = cardsDealer.deal(
                Player.all(Player.SOUTH),
                cardsSetBuilder.allCards().findAll{it != Card.TWO_OF_CLUBS},
                new HashMap<Player, Set<Card>>(),
                [
                        (Player.EAST): [Suit.CLUBS, Suit.HEARTS, Suit.DIAMONDS] as Set,
                        (Player.SOUTH): [Suit.DIAMONDS] as Set
                ]
        )

        then:
        cards[Player.NORTH].size() == 13
        cards[Player.EAST].size() == 12
        cards[Player.SOUTH].size() == 13
        cards[Player.WEST].size() == 13

        cards[Player.EAST].findAll{it.suit == Suit.SPADES}.size() == 12
        cards[Player.EAST].findAll{it.suit == Suit.DIAMONDS}.size() == 0
    }

    def "should combine missing suits and known cards" (){
        when:
        Map<Player, Set<Card>> cards = cardsDealer.deal(
                Player.all(Player.SOUTH),
                cardsSetBuilder.allCards().findAll{it != Card.TWO_OF_CLUBS},
                [
                        (Player.SOUTH): [Card.ACE_OF_HEARTS, Card.ACE_OF_CLUBS] as Set
                ],
                [
                        (Player.EAST): [Suit.CLUBS, Suit.HEARTS, Suit.DIAMONDS] as Set,
                        (Player.SOUTH): [Suit.DIAMONDS] as Set
                ]
        )

        then:
        cards[Player.NORTH].size() == 13
        cards[Player.EAST].size() == 12
        cards[Player.SOUTH].size() == 13
        cards[Player.WEST].size() == 13

        cards[Player.EAST].findAll{it.suit == Suit.SPADES}.size() == 12
        cards[Player.EAST].findAll{it.suit == Suit.DIAMONDS}.size() == 0
        cards[Player.SOUTH].contains(Card.ACE_OF_HEARTS)
        cards[Player.SOUTH].contains(Card.ACE_OF_CLUBS)
    }

    def "all have restrictions combine missing suits and known cards" (){
        when:
        Map<Player, Set<Card>> cards = cardsDealer.deal(
                Player.all(Player.SOUTH),
                cardsSetBuilder.allCards().findAll{it != Card.TWO_OF_CLUBS},
                [
                        (Player.SOUTH): [Card.ACE_OF_SPADES, Card.ACE_OF_CLUBS] as Set
                ],
                [
                        (Player.NORTH): [Suit.CLUBS, Suit.SPADES] as Set,
                        (Player.EAST): [Suit.CLUBS, Suit.HEARTS] as Set,
                        (Player.SOUTH): [Suit.DIAMONDS] as Set,
                        (Player.WEST): [Suit.DIAMONDS] as Set
                ]
        )

        then:
        cards[Player.NORTH].size() == 13
        cards[Player.EAST].size() == 12
        cards[Player.SOUTH].size() == 13
        cards[Player.WEST].size() == 13

        cards[Player.NORTH].findAll{it.suit == Suit.CLUBS}.size() == 0
        cards[Player.NORTH].findAll{it.suit == Suit.SPADES}.size() == 0

        cards[Player.EAST].findAll{it.suit == Suit.CLUBS}.size() == 0
        cards[Player.EAST].findAll{it.suit == Suit.HEARTS}.size() == 0

        cards[Player.SOUTH].findAll{it.suit == Suit.DIAMONDS}.size() == 0

        cards[Player.SOUTH].contains(Card.ACE_OF_SPADES)
        cards[Player.SOUTH].contains(Card.ACE_OF_CLUBS)
    }
}
