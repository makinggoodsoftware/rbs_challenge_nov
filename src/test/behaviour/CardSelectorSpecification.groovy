import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.Player
import com.mgs.rbsnov.logic.CardSelector
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource


@ContextConfiguration(classes = Config)
class CardSelectorSpecification extends Specification{
    @Resource
    CardSelector cardSelector

    def "should select the best card" (){
        when:
        Card bestCard = cardSelector.bestCard (
                [
                    Card.ACE_OF_HEARTS,
                    Card.EIGHT_OF_HEARTS,
                    Card.FOUR_OF_HEARTS,
                    Card.ACE_OF_DIAMONDS,
                    Card.TEN_OF_DIAMONDS,
                    Card.FOUR_OF_SPADES
                ] as Set,
                [
                        Card.QUEEN_OF_SPADES,
                        Card.TWO_OF_SPADES
                ] as Set,
                Player.SOUTH
        )

        then:
        bestCard == Card.TWO_OF_SPADES
    }
}
