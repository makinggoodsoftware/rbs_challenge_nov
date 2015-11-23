package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Card
import com.mgs.rbsnov.domain.GameResult
import com.mgs.rbsnov.domain.Player
import com.mgs.rbsnov.domain.PlayersLogic
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration(classes = Config)
public class GameSimulatorSpec extends Specification{
    @Resource
    GameSimulator gameSimulator
    @Resource
    PlayerLogic playerLogic
    @Resource
    GameScorer gameScorer


    def "game simulator" (){
        given:
        PlayersLogic playersLogic = PlayersLogic.from(playerLogic, playerLogic, playerLogic, playerLogic)

        when:
        GameResult gameResult
        (1..50).each{
            gameResult = gameSimulator.start(playersLogic)
        }

        then:
        gameResult.roundResults.size() == 13
    }

    def "shoot the moon game simulator" (){
        given:
        Set<Card> initialHand = [
                Card.ACE_OF_HEARTS,
                Card.KING_OF_HEARTS,
                Card.QUEEN_OF_HEARTS,
                Card.JACK_OF_HEARTS,
                Card.EIGHT_OF_HEARTS,
                Card.ACE_OF_SPADES,
                Card.QUEEN_OF_SPADES,
                Card.ACE_OF_DIAMONDS,
                Card.TEN_OF_DIAMONDS,
                Card.EIGHT_OF_DIAMONDS,
                Card.TEN_OF_CLUBS,
                Card.SIX_OF_CLUBS,
                Card.FOUR_OF_CLUBS
        ] as Set

        PlayersLogic playersLogic = PlayersLogic.from(playerLogic, playerLogic, playerLogic, playerLogic)

        when:
        List<GameResult> gameResults = []
        (1..10).each{
            gameResults << gameSimulator.start(initialHand, playersLogic)
        }

        then:
        gameResults.each {
            assert gameScorer.finalScores(it.roundResults).heartsScore.get(Player.SOUTH).intValue() == 26
        }
    }

}
