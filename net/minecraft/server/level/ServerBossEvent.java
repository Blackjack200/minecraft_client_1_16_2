package net.minecraft.server.level;

import java.util.Collection;
import com.google.common.collect.Lists;
import java.util.Iterator;
import net.minecraft.network.protocol.Packet;
import com.google.common.base.Objects;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import java.util.Collections;
import com.google.common.collect.Sets;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import java.util.Set;
import net.minecraft.world.BossEvent;

public class ServerBossEvent extends BossEvent {
    private final Set<ServerPlayer> players;
    private final Set<ServerPlayer> unmodifiablePlayers;
    private boolean visible;
    
    public ServerBossEvent(final Component nr, final BossBarColor a, final BossBarOverlay b) {
        super(Mth.createInsecureUUID(), nr, a, b);
        this.players = (Set<ServerPlayer>)Sets.newHashSet();
        this.unmodifiablePlayers = (Set<ServerPlayer>)Collections.unmodifiableSet((Set)this.players);
        this.visible = true;
    }
    
    @Override
    public void setPercent(final float float1) {
        if (float1 != this.percent) {
            super.setPercent(float1);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_PCT);
        }
    }
    
    @Override
    public void setColor(final BossBarColor a) {
        if (a != this.color) {
            super.setColor(a);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_STYLE);
        }
    }
    
    @Override
    public void setOverlay(final BossBarOverlay b) {
        if (b != this.overlay) {
            super.setOverlay(b);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_STYLE);
        }
    }
    
    @Override
    public BossEvent setDarkenScreen(final boolean boolean1) {
        if (boolean1 != this.darkenScreen) {
            super.setDarkenScreen(boolean1);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_PROPERTIES);
        }
        return this;
    }
    
    @Override
    public BossEvent setPlayBossMusic(final boolean boolean1) {
        if (boolean1 != this.playBossMusic) {
            super.setPlayBossMusic(boolean1);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_PROPERTIES);
        }
        return this;
    }
    
    @Override
    public BossEvent setCreateWorldFog(final boolean boolean1) {
        if (boolean1 != this.createWorldFog) {
            super.setCreateWorldFog(boolean1);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_PROPERTIES);
        }
        return this;
    }
    
    @Override
    public void setName(final Component nr) {
        if (!Objects.equal(nr, this.name)) {
            super.setName(nr);
            this.broadcast(ClientboundBossEventPacket.Operation.UPDATE_NAME);
        }
    }
    
    private void broadcast(final ClientboundBossEventPacket.Operation a) {
        if (this.visible) {
            final ClientboundBossEventPacket oz3 = new ClientboundBossEventPacket(a, this);
            for (final ServerPlayer aah5 : this.players) {
                aah5.connection.send(oz3);
            }
        }
    }
    
    public void addPlayer(final ServerPlayer aah) {
        if (this.players.add(aah) && this.visible) {
            aah.connection.send(new ClientboundBossEventPacket(ClientboundBossEventPacket.Operation.ADD, this));
        }
    }
    
    public void removePlayer(final ServerPlayer aah) {
        if (this.players.remove(aah) && this.visible) {
            aah.connection.send(new ClientboundBossEventPacket(ClientboundBossEventPacket.Operation.REMOVE, this));
        }
    }
    
    public void removeAllPlayers() {
        if (!this.players.isEmpty()) {
            for (final ServerPlayer aah3 : Lists.newArrayList((Iterable)this.players)) {
                this.removePlayer(aah3);
            }
        }
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean boolean1) {
        if (boolean1 != this.visible) {
            this.visible = boolean1;
            for (final ServerPlayer aah4 : this.players) {
                aah4.connection.send(new ClientboundBossEventPacket(boolean1 ? ClientboundBossEventPacket.Operation.ADD : ClientboundBossEventPacket.Operation.REMOVE, this));
            }
        }
    }
    
    public Collection<ServerPlayer> getPlayers() {
        return (Collection<ServerPlayer>)this.unmodifiablePlayers;
    }
}
