package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.GameResult
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
}
