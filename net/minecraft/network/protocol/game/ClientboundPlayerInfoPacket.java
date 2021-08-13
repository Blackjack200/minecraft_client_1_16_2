package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.network.PacketListener;
import com.google.common.base.MoreObjects;
import java.io.IOException;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Iterator;
import net.minecraft.server.level.ServerPlayer;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.protocol.Packet;

public class ClientboundPlayerInfoPacket implements Packet<ClientGamePacketListener> {
    private Action action;
    private final List<PlayerUpdate> entries;
    
    public ClientboundPlayerInfoPacket() {
        this.entries = (List<PlayerUpdate>)Lists.newArrayList();
    }
    
    public ClientboundPlayerInfoPacket(final Action a, final ServerPlayer... arr) {
        this.entries = (List<PlayerUpdate>)Lists.newArrayList();
        this.action = a;
        for (final ServerPlayer aah7 : arr) {
            this.entries.add(new PlayerUpdate(aah7.getGameProfile(), aah7.latency, aah7.gameMode.getGameModeForPlayer(), aah7.getTabListDisplayName()));
        }
    }
    
    public ClientboundPlayerInfoPacket(final Action a, final Iterable<ServerPlayer> iterable) {
        this.entries = (List<PlayerUpdate>)Lists.newArrayList();
        this.action = a;
        for (final ServerPlayer aah5 : iterable) {
            this.entries.add(new PlayerUpdate(aah5.getGameProfile(), aah5.latency, aah5.gameMode.getGameModeForPlayer(), aah5.getTabListDisplayName()));
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.action = nf.<Action>readEnum(Action.class);
        for (int integer3 = nf.readVarInt(), integer4 = 0; integer4 < integer3; ++integer4) {
            GameProfile gameProfile5 = null;
            int integer5 = 0;
            GameType brr7 = null;
            Component nr8 = null;
            switch (this.action) {
                case ADD_PLAYER: {
                    gameProfile5 = new GameProfile(nf.readUUID(), nf.readUtf(16));
                    for (int integer6 = nf.readVarInt(), integer7 = 0; integer7 < integer6; ++integer7) {
                        final String string11 = nf.readUtf(32767);
                        final String string12 = nf.readUtf(32767);
                        if (nf.readBoolean()) {
                            gameProfile5.getProperties().put(string11, new Property(string11, string12, nf.readUtf(32767)));
                        }
                        else {
                            gameProfile5.getProperties().put(string11, new Property(string11, string12));
                        }
                    }
                    brr7 = GameType.byId(nf.readVarInt());
                    integer5 = nf.readVarInt();
                    if (nf.readBoolean()) {
                        nr8 = nf.readComponent();
                        break;
                    }
                    break;
                }
                case UPDATE_GAME_MODE: {
                    gameProfile5 = new GameProfile(nf.readUUID(), (String)null);
                    brr7 = GameType.byId(nf.readVarInt());
                    break;
                }
                case UPDATE_LATENCY: {
                    gameProfile5 = new GameProfile(nf.readUUID(), (String)null);
                    integer5 = nf.readVarInt();
                    break;
                }
                case UPDATE_DISPLAY_NAME: {
                    gameProfile5 = new GameProfile(nf.readUUID(), (String)null);
                    if (nf.readBoolean()) {
                        nr8 = nf.readComponent();
                        break;
                    }
                    break;
                }
                case REMOVE_PLAYER: {
                    gameProfile5 = new GameProfile(nf.readUUID(), (String)null);
                    break;
                }
            }
            this.entries.add(new PlayerUpdate(gameProfile5, integer5, brr7, nr8));
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeEnum(this.action);
        nf.writeVarInt(this.entries.size());
        for (final PlayerUpdate b4 : this.entries) {
            switch (this.action) {
                case ADD_PLAYER: {
                    nf.writeUUID(b4.getProfile().getId());
                    nf.writeUtf(b4.getProfile().getName());
                    nf.writeVarInt(b4.getProfile().getProperties().size());
                    for (final Property property6 : b4.getProfile().getProperties().values()) {
                        nf.writeUtf(property6.getName());
                        nf.writeUtf(property6.getValue());
                        if (property6.hasSignature()) {
                            nf.writeBoolean(true);
                            nf.writeUtf(property6.getSignature());
                        }
                        else {
                            nf.writeBoolean(false);
                        }
                    }
                    nf.writeVarInt(b4.getGameMode().getId());
                    nf.writeVarInt(b4.getLatency());
                    if (b4.getDisplayName() == null) {
                        nf.writeBoolean(false);
                        continue;
                    }
                    nf.writeBoolean(true);
                    nf.writeComponent(b4.getDisplayName());
                    continue;
                }
                case UPDATE_GAME_MODE: {
                    nf.writeUUID(b4.getProfile().getId());
                    nf.writeVarInt(b4.getGameMode().getId());
                    continue;
                }
                case UPDATE_LATENCY: {
                    nf.writeUUID(b4.getProfile().getId());
                    nf.writeVarInt(b4.getLatency());
                    continue;
                }
                case UPDATE_DISPLAY_NAME: {
                    nf.writeUUID(b4.getProfile().getId());
                    if (b4.getDisplayName() == null) {
                        nf.writeBoolean(false);
                        continue;
                    }
                    nf.writeBoolean(true);
                    nf.writeComponent(b4.getDisplayName());
                    continue;
                }
                case REMOVE_PLAYER: {
                    nf.writeUUID(b4.getProfile().getId());
                    continue;
                }
            }
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handlePlayerInfo(this);
    }
    
    public List<PlayerUpdate> getEntries() {
        return this.entries;
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public String toString() {
        return MoreObjects.toStringHelper(this).add("action", this.action).add("entries", this.entries).toString();
    }
    
    public enum Action {
        ADD_PLAYER, 
        UPDATE_GAME_MODE, 
        UPDATE_LATENCY, 
        UPDATE_DISPLAY_NAME, 
        REMOVE_PLAYER;
    }
    
    public class PlayerUpdate {
        private final int latency;
        private final GameType gameMode;
        private final GameProfile profile;
        private final Component displayName;
        
        public PlayerUpdate(final GameProfile gameProfile, final int integer, @Nullable final GameType brr, @Nullable final Component nr) {
            this.profile = gameProfile;
            this.latency = integer;
            this.gameMode = brr;
            this.displayName = nr;
        }
        
        public GameProfile getProfile() {
            return this.profile;
        }
        
        public int getLatency() {
            return this.latency;
        }
        
        public GameType getGameMode() {
            return this.gameMode;
        }
        
        @Nullable
        public Component getDisplayName() {
            return this.displayName;
        }
        
        public String toString() {
            return MoreObjects.toStringHelper(this).add("latency", this.latency).add("gameMode", this.gameMode).add("profile", this.profile).add("displayName", ((this.displayName == null) ? null : Component.Serializer.toJson(this.displayName))).toString();
        }
    }
}
