package net.minecraft.server.level;

import java.util.stream.Stream;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;

public final class PlayerMap {
    private final Object2BooleanMap<ServerPlayer> players;
    
    public PlayerMap() {
        this.players = (Object2BooleanMap<ServerPlayer>)new Object2BooleanOpenHashMap();
    }
    
    public Stream<ServerPlayer> getPlayers(final long long1) {
        return (Stream<ServerPlayer>)this.players.keySet().stream();
    }
    
    public void addPlayer(final long long1, final ServerPlayer aah, final boolean boolean3) {
        this.players.put(aah, boolean3);
    }
    
    public void removePlayer(final long long1, final ServerPlayer aah) {
        this.players.removeBoolean(aah);
    }
    
    public void ignorePlayer(final ServerPlayer aah) {
        this.players.replace(aah, true);
    }
    
    public void unIgnorePlayer(final ServerPlayer aah) {
        this.players.replace(aah, false);
    }
    
    public boolean ignoredOrUnknown(final ServerPlayer aah) {
        return this.players.getOrDefault(aah, true);
    }
    
    public boolean ignored(final ServerPlayer aah) {
        return this.players.getBoolean(aah);
    }
    
    public void updatePlayer(final long long1, final long long2, final ServerPlayer aah) {
    }
}
