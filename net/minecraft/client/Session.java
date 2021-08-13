package net.minecraft.client;

import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import java.util.UUID;
import com.mojang.bridge.game.GameSession;

public class Session implements GameSession {
    private final int players;
    private final boolean isRemoteServer;
    private final String difficulty;
    private final String gameMode;
    private final UUID id;
    
    public Session(final ClientLevel dwl, final LocalPlayer dze, final ClientPacketListener dwm) {
        this.players = dwm.getOnlinePlayers().size();
        this.isRemoteServer = !dwm.getConnection().isMemoryConnection();
        this.difficulty = dwl.getDifficulty().getKey();
        final PlayerInfo dwp5 = dwm.getPlayerInfo(dze.getUUID());
        if (dwp5 != null) {
            this.gameMode = dwp5.getGameMode().getName();
        }
        else {
            this.gameMode = "unknown";
        }
        this.id = dwm.getId();
    }
    
    public int getPlayerCount() {
        return this.players;
    }
    
    public boolean isRemoteServer() {
        return this.isRemoteServer;
    }
    
    public String getDifficulty() {
        return this.difficulty;
    }
    
    public String getGameMode() {
        return this.gameMode;
    }
    
    public UUID getSessionId() {
        return this.id;
    }
}
