package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Hands
import com.mgs.rbsnov.domain.Player
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

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
}
