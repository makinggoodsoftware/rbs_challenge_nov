package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.DealInProgress
import com.mgs.rbsnov.domain.FinishedDeal
import com.mgs.rbsnov.domain.Hands
import com.mgs.rbsnov.domain.Player
import spock.lang.Specification

class GameDeveloperSpecification extends Specification {
    DealsDeveloper gameDeveloper = new DealsDeveloper(dealRules, finishedDealScorer, dealInProgressFactory, cardsFilter)

    def "should develop game" (){
        when:
        Map<Card, Set<FinishedDeal>> developed = gameDeveloper.develop(
                new DealInProgress(null, [] as Set, startginPlayer, Player.SOUTH),
                new Hands(
                        [Card.ACE_OF_SPADES] as Set,
                        [Card.ACE_OF_CLUBS] as Set,
                        [Card.TEN_OF_CLUBS] as Set,
                        [Card.FOUR_OF_SPADES] as Set,

                )
        )

        then:
        developed.size() == 1
    }
}
