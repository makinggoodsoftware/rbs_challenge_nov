package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Deal
import com.mgs.rbsnov.domain.Hands
import com.mgs.rbsnov.domain.Player
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

import static com.mgs.rbsnov.domain.Card.JACK_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.JACK_OF_DIAMONDS
import static com.mgs.rbsnov.domain.Card.JACK_OF_HEARTS
import static com.mgs.rbsnov.domain.Card.JACK_OF_SPADES
import static com.mgs.rbsnov.domain.Card.QUEEN_OF_CLUBS
import static com.mgs.rbsnov.domain.Card.QUEEN_OF_DIAMONDS
import static com.mgs.rbsnov.domain.Card.QUEEN_OF_HEARTS
import static com.mgs.rbsnov.domain.Card.QUEEN_OF_SPADES

@ContextConfiguration(classes = Config)
class HandsFactorySpecification extends Specification {
    @Resource
    HandsFactory handsFactory

    def "should deal starting with the specified user and then going clockwise" (){
        when:
        Hands hands = handsFactory.from(
                [
                        [Card.ACE_OF_SPADES] as Set,
                        [Card.ACE_OF_DIAMONDS] as Set,
                        [Card.ACE_OF_CLUBS] as Set,
                        [Card.ACE_OF_HEARTS] as Set
                ],
                Player.NORTH
        )

        then:
        hands.getNorthHand() == [Card.ACE_OF_SPADES] as Set
        hands.getEastHand() == [Card.ACE_OF_DIAMONDS] as Set
        hands.getSouthHand() == [Card.ACE_OF_CLUBS] as Set
        hands.getWestHand() == [Card.ACE_OF_HEARTS] as Set
    }

    def "shuffle and deal all cards to the 4 players" (){
        when:
        Hands hands = handsFactory.fromAllCardsShuffled(Player.SOUTH)

        then:
        hands.eastHand.size() == 13
        hands.northHand.size() == 13
        hands.westHand.size() == 13
        hands.southHand.size() == 13

        when:
        hands.eastHand.removeAll(hands.northHand)

        then:
        hands.eastHand.size() == 13

        when:
        hands.northHand.removeAll(hands.westHand)

        then:
        hands.northHand.size() == 13

        when:
        hands.westHand.removeAll(hands.southHand)

        then:
        hands.westHand.size() == 13

        when:
        hands.southHand.removeAll(hands.eastHand)

        then:
        hands.southHand.size() == 13
    }

    def "should reduce hands using deal" (){
        when:
        Hands hands = handsFactory.reduce(
                new Hands(
                        [JACK_OF_CLUBS, QUEEN_OF_CLUBS] as Set,
                        [JACK_OF_DIAMONDS, QUEEN_OF_DIAMONDS] as Set,
                        [JACK_OF_HEARTS, QUEEN_OF_HEARTS] as Set,
                        [JACK_OF_SPADES, QUEEN_OF_SPADES] as Set,
                ),
                new Deal(
                        JACK_OF_SPADES,
                        JACK_OF_CLUBS,
                        JACK_OF_DIAMONDS,
                        JACK_OF_HEARTS,
                ),
                Player.EAST
        )

        then:
        hands == new Hands (
                [QUEEN_OF_CLUBS] as Set,
                [QUEEN_OF_DIAMONDS] as Set,
                [QUEEN_OF_HEARTS] as Set,
                [QUEEN_OF_SPADES] as Set
        )
    }
}
