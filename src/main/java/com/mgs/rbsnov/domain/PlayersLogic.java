package com.mgs.rbsnov.domain;

import com.mgs.rbsnov.logic.PlayerLogic;

import java.util.HashMap;
import java.util.Map;

public class PlayersLogic {
    private final Map<Player, PlayerLogic> playersLogic;

    public static PlayersLogic from (PlayerLogic southPlayerLogic, PlayerLogic wesrPlayerLogic, PlayerLogic northPlayerLogic, PlayerLogic eastPlayerLogic){
        Map<Player, PlayerLogic> asMap = new HashMap<>();
        asMap.put(Player.SOUTH, southPlayerLogic);
        asMap.put(Player.WEST, wesrPlayerLogic);
        asMap.put(Player.NORTH, northPlayerLogic);
        asMap.put(Player.EAST, eastPlayerLogic);

        return new PlayersLogic(asMap);
    }

    PlayersLogic(Map<Player, PlayerLogic> playersLogic) {
        this.playersLogic = playersLogic;
    }

    public PlayerLogic get (Player player){
        return playersLogic.get(player);
    }
}
