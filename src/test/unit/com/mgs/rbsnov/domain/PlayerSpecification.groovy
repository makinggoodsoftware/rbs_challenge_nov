package com.mgs.rbsnov.domain

import spock.lang.Specification

class PlayerSpecification extends Specification {

    def "should move player clockwise" (){
        expect:
        Player.SOUTH.moveClockWise(0) == Player.SOUTH
        Player.SOUTH.moveClockWise(1) == Player.WEST
        Player.SOUTH.moveClockWise(2) == Player.NORTH
        Player.SOUTH.moveClockWise(3) == Player.EAST

        Player.SOUTH.moveClockWise(4) == Player.SOUTH.moveClockWise(0)
        Player.SOUTH.moveClockWise(5) == Player.SOUTH.moveClockWise(1)
        Player.SOUTH.moveClockWise(6) == Player.SOUTH.moveClockWise(2)
        Player.SOUTH.moveClockWise(7) == Player.SOUTH.moveClockWise(3)

    }
}
