package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Player
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
                ]
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
}
