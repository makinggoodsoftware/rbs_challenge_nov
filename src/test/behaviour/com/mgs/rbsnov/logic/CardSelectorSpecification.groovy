package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Player
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource


@ContextConfiguration(classes = Config)
class CardSelectorSpecification extends Specification{
    @Resource
    CardSelector cardSelector
    @Resource
    DealInProgressFactory dealInProgressFactory

    def "should select the best card" (){
        when:
        Card bestCard = cardSelector.bestCard(dealInProgressFactory.newJustStartedDeal(Player.SOUTH),
                [
                        Card.ACE_OF_HEARTS,
                        Card.EIGHT_OF_HEARTS,
                        Card.FOUR_OF_HEARTS,
                        Card.ACE_OF_DIAMONDS,
                        Card.FIVE_OF_SPADES,
                        Card.FOUR_OF_SPADES
                ] as Set,
                [
                        Card.QUEEN_OF_SPADES,
                        Card.TWO_OF_SPADES
                ] as Set
        )

        then:
        bestCard == Card.TWO_OF_SPADES
    }
}
