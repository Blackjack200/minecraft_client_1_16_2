package net.minecraft.server;

import net.minecraft.server.level.ServerPlayer;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import java.util.Collection;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import java.util.Arrays;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.network.protocol.game.ClientboundSetDisplayObjectivePacket;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.world.scores.Score;
import com.google.common.collect.Sets;
import net.minecraft.world.scores.Objective;
import java.util.Set;
import net.minecraft.world.scores.Scoreboard;

public class ServerScoreboard extends Scoreboard {
    private final MinecraftServer server;
    private final Set<Objective> trackedObjectives;
    private Runnable[] dirtyListeners;
    
    public ServerScoreboard(final MinecraftServer minecraftServer) {
        this.trackedObjectives = (Set<Objective>)Sets.newHashSet();
        this.dirtyListeners = new Runnable[0];
        this.server = minecraftServer;
    }
    
    @Override
    public void onScoreChanged(final Score ddj) {
        super.onScoreChanged(ddj);
        if (this.trackedObjectives.contains(ddj.getObjective())) {
            this.server.getPlayerList().broadcastAll(new ClientboundSetScorePacket(Method.CHANGE, ddj.getObjective().getName(), ddj.getOwner(), ddj.getScore()));
        }
        this.setDirty();
    }
    
    @Override
    public void onPlayerRemoved(final String string) {
        super.onPlayerRemoved(string);
        this.server.getPlayerList().broadcastAll(new ClientboundSetScorePacket(Method.REMOVE, null, string, 0));
        this.setDirty();
    }
    
    @Override
    public void onPlayerScoreRemoved(final String string, final Objective ddh) {
        super.onPlayerScoreRemoved(string, ddh);
        if (this.trackedObjectives.contains(ddh)) {
            this.server.getPlayerList().broadcastAll(new ClientboundSetScorePacket(Method.REMOVE, ddh.getName(), string, 0));
        }
        this.setDirty();
    }
    
    @Override
    public void setDisplayObjective(final int integer, @Nullable final Objective ddh) {
        final Objective ddh2 = this.getDisplayObjective(integer);
        super.setDisplayObjective(integer, ddh);
        if (ddh2 != ddh && ddh2 != null) {
            if (this.getObjectiveDisplaySlotCount(ddh2) > 0) {
                this.server.getPlayerList().broadcastAll(new ClientboundSetDisplayObjectivePacket(integer, ddh));
            }
            else {
                this.stopTrackingObjective(ddh2);
            }
        }
        if (ddh != null) {
            if (this.trackedObjectives.contains(ddh)) {
                this.server.getPlayerList().broadcastAll(new ClientboundSetDisplayObjectivePacket(integer, ddh));
            }
            else {
                this.startTrackingObjective(ddh);
            }
        }
        this.setDirty();
    }
    
    @Override
    public boolean addPlayerToTeam(final String string, final PlayerTeam ddi) {
        if (super.addPlayerToTeam(string, ddi)) {
            this.server.getPlayerList().broadcastAll(new ClientboundSetPlayerTeamPacket(ddi, (Collection<String>)Arrays.asList((Object[])new String[] { string }), 3));
            this.setDirty();
            return true;
        }
        return false;
    }
    
    @Override
    public void removePlayerFromTeam(final String string, final PlayerTeam ddi) {
        super.removePlayerFromTeam(string, ddi);
        this.server.getPlayerList().broadcastAll(new ClientboundSetPlayerTeamPacket(ddi, (Collection<String>)Arrays.asList((Object[])new String[] { string }), 4));
        this.setDirty();
    }
    
    @Override
    public void onObjectiveAdded(final Objective ddh) {
        super.onObjectiveAdded(ddh);
        this.setDirty();
    }
    
    @Override
    public void onObjectiveChanged(final Objective ddh) {
        super.onObjectiveChanged(ddh);
        if (this.trackedObjectives.contains(ddh)) {
            this.server.getPlayerList().broadcastAll(new ClientboundSetObjectivePacket(ddh, 2));
        }
        this.setDirty();
    }
    
    @Override
    public void onObjectiveRemoved(final Objective ddh) {
        super.onObjectiveRemoved(ddh);
        if (this.trackedObjectives.contains(ddh)) {
            this.stopTrackingObjective(ddh);
        }
        this.setDirty();
    }
    
    @Override
    public void onTeamAdded(final PlayerTeam ddi) {
        super.onTeamAdded(ddi);
        this.server.getPlayerList().broadcastAll(new ClientboundSetPlayerTeamPacket(ddi, 0));
        this.setDirty();
    }
    
    @Override
    public void onTeamChanged(final PlayerTeam ddi) {
        super.onTeamChanged(ddi);
        this.server.getPlayerList().broadcastAll(new ClientboundSetPlayerTeamPacket(ddi, 2));
        this.setDirty();
    }
    
    @Override
    public void onTeamRemoved(final PlayerTeam ddi) {
        super.onTeamRemoved(ddi);
        this.server.getPlayerList().broadcastAll(new ClientboundSetPlayerTeamPacket(ddi, 1));
        this.setDirty();
    }
    
    public void addDirtyListener(final Runnable runnable) {
        (this.dirtyListeners = (Runnable[])Arrays.copyOf((Object[])this.dirtyListeners, this.dirtyListeners.length + 1))[this.dirtyListeners.length - 1] = runnable;
    }
    
    protected void setDirty() {
        for (final Runnable runnable5 : this.dirtyListeners) {
            runnable5.run();
        }
    }
    
    public List<Packet<?>> getStartTrackingPackets(final Objective ddh) {
        final List<Packet<?>> list3 = (List<Packet<?>>)Lists.newArrayList();
        list3.add(new ClientboundSetObjectivePacket(ddh, 0));
        for (int integer4 = 0; integer4 < 19; ++integer4) {
            if (this.getDisplayObjective(integer4) == ddh) {
                list3.add(new ClientboundSetDisplayObjectivePacket(integer4, ddh));
            }
        }
        for (final Score ddj5 : this.getPlayerScores(ddh)) {
            list3.add(new ClientboundSetScorePacket(Method.CHANGE, ddj5.getObjective().getName(), ddj5.getOwner(), ddj5.getScore()));
        }
        return list3;
    }
    
    public void startTrackingObjective(final Objective ddh) {
        final List<Packet<?>> list3 = this.getStartTrackingPackets(ddh);
        for (final ServerPlayer aah5 : this.server.getPlayerList().getPlayers()) {
            for (final Packet<?> oj7 : list3) {
                aah5.connection.send(oj7);
            }
        }
        this.trackedObjectives.add(ddh);
    }
    
    public List<Packet<?>> getStopTrackingPackets(final Objective ddh) {
        final List<Packet<?>> list3 = (List<Packet<?>>)Lists.newArrayList();
        list3.add(new ClientboundSetObjectivePacket(ddh, 1));
        for (int integer4 = 0; integer4 < 19; ++integer4) {
            if (this.getDisplayObjective(integer4) == ddh) {
                list3.add(new ClientboundSetDisplayObjectivePacket(integer4, ddh));
            }
        }
        return list3;
    }
    
    public void stopTrackingObjective(final Objective ddh) {
        final List<Packet<?>> list3 = this.getStopTrackingPackets(ddh);
        for (final ServerPlayer aah5 : this.server.getPlayerList().getPlayers()) {
            for (final Packet<?> oj7 : list3) {
                aah5.connection.send(oj7);
            }
        }
        this.trackedObjectives.remove(ddh);
    }
    
    public int getObjectiveDisplaySlotCount(final Objective ddh) {
        int integer3 = 0;
        for (int integer4 = 0; integer4 < 19; ++integer4) {
            if (this.getDisplayObjective(integer4) == ddh) {
                ++integer3;
            }
        }
        return integer3;
    }
    
    public enum Method {
        CHANGE, 
        REMOVE;
    }
}
