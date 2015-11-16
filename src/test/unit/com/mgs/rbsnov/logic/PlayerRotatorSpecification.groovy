package com.mgs.rbsnov.logic

import com.mgs.rbsnov.domain.Player
import spock.lang.Specification

class PlayerRotatorSpecification extends Specification {
    def "should rotate players" (){
        when:
        PlayerRotator.PlayerRotation playerRotation = new PlayerRotator().clockwiseIterator(Player.NORTH)

        then:
        playerRotation.hasNext()
        playerRotation.next() == Player.NORTH
        playerRotation.hasNext()
        playerRotation.next() == Player.EAST
        playerRotation.hasNext()
        playerRotation.next() == Player.SOUTH
        playerRotation.hasNext()
        playerRotation.next() == Player.WEST
        ! playerRotation.hasNext()
    }
}
