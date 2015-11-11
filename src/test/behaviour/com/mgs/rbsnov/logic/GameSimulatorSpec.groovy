package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.GameResult
import com.mgs.rbsnov.spring.Config
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import javax.annotation.Resource

@ContextConfiguration(classes = Config)
public class GameSimulatorSpec extends Specification{
    @Resource
    GameSimulator gameSimulator


    def "game simulator" (){
        given:
        PlayerLogic southPlayerLogic = new PlayerLogic()
        PlayerLogic westPlayerLogic = new PlayerLogic()
        PlayerLogic northPlayerLogic = new PlayerLogic()
        PlayerLogic eastPlayerLogic = new PlayerLogic()

        when:
        GameResult gameResult = gameSimulator.start (southPlayerLogic, westPlayerLogic, northPlayerLogic, eastPlayerLogic)

        then:
        gameResult.roundResults.size() == 13
    }
}
