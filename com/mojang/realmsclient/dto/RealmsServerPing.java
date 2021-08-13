package com.mojang.realmsclient.dto;

public class RealmsServerPing extends ValueObject {
    public volatile String nrOfPlayers;
    public volatile String playerList;
    
    public RealmsServerPing() {
        this.nrOfPlayers = "0";
        this.playerList = "";
    }
}
