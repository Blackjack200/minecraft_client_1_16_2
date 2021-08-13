package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.scores.PlayerTeam;
import com.google.common.collect.Lists;
import net.minecraft.world.scores.Team;
import net.minecraft.network.chat.TextComponent;
import java.util.Collection;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetPlayerTeamPacket implements Packet<ClientGamePacketListener> {
    private String name;
    private Component displayName;
    private Component playerPrefix;
    private Component playerSuffix;
    private String nametagVisibility;
    private String collisionRule;
    private ChatFormatting color;
    private final Collection<String> players;
    private int method;
    private int options;
    
    public ClientboundSetPlayerTeamPacket() {
        this.name = "";
        this.displayName = TextComponent.EMPTY;
        this.playerPrefix = TextComponent.EMPTY;
        this.playerSuffix = TextComponent.EMPTY;
        this.nametagVisibility = Team.Visibility.ALWAYS.name;
        this.collisionRule = Team.CollisionRule.ALWAYS.name;
        this.color = ChatFormatting.RESET;
        this.players = (Collection<String>)Lists.newArrayList();
    }
    
    public ClientboundSetPlayerTeamPacket(final PlayerTeam ddi, final int integer) {
        this.name = "";
        this.displayName = TextComponent.EMPTY;
        this.playerPrefix = TextComponent.EMPTY;
        this.playerSuffix = TextComponent.EMPTY;
        this.nametagVisibility = Team.Visibility.ALWAYS.name;
        this.collisionRule = Team.CollisionRule.ALWAYS.name;
        this.color = ChatFormatting.RESET;
        this.players = (Collection<String>)Lists.newArrayList();
        this.name = ddi.getName();
        this.method = integer;
        if (integer == 0 || integer == 2) {
            this.displayName = ddi.getDisplayName();
            this.options = ddi.packOptions();
            this.nametagVisibility = ddi.getNameTagVisibility().name;
            this.collisionRule = ddi.getCollisionRule().name;
            this.color = ddi.getColor();
            this.playerPrefix = ddi.getPlayerPrefix();
            this.playerSuffix = ddi.getPlayerSuffix();
        }
        if (integer == 0) {
            this.players.addAll((Collection)ddi.getPlayers());
        }
    }
    
    public ClientboundSetPlayerTeamPacket(final PlayerTeam ddi, final Collection<String> collection, final int integer) {
        this.name = "";
        this.displayName = TextComponent.EMPTY;
        this.playerPrefix = TextComponent.EMPTY;
        this.playerSuffix = TextComponent.EMPTY;
        this.nametagVisibility = Team.Visibility.ALWAYS.name;
        this.collisionRule = Team.CollisionRule.ALWAYS.name;
        this.color = ChatFormatting.RESET;
        this.players = (Collection<String>)Lists.newArrayList();
        if (integer != 3 && integer != 4) {
            throw new IllegalArgumentException("Method must be join or leave for player constructor");
        }
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("Players cannot be null/empty");
        }
        this.method = integer;
        this.name = ddi.getName();
        this.players.addAll((Collection)collection);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.name = nf.readUtf(16);
        this.method = nf.readByte();
        if (this.method == 0 || this.method == 2) {
            this.displayName = nf.readComponent();
            this.options = nf.readByte();
            this.nametagVisibility = nf.readUtf(40);
            this.collisionRule = nf.readUtf(40);
            this.color = nf.<ChatFormatting>readEnum(ChatFormatting.class);
            this.playerPrefix = nf.readComponent();
            this.playerSuffix = nf.readComponent();
        }
        if (this.method == 0 || this.method == 3 || this.method == 4) {
            for (int integer3 = nf.readVarInt(), integer4 = 0; integer4 < integer3; ++integer4) {
                this.players.add(nf.readUtf(40));
            }
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeUtf(this.name);
        nf.writeByte(this.method);
        if (this.method == 0 || this.method == 2) {
            nf.writeComponent(this.displayName);
            nf.writeByte(this.options);
            nf.writeUtf(this.nametagVisibility);
            nf.writeUtf(this.collisionRule);
            nf.writeEnum(this.color);
            nf.writeComponent(this.playerPrefix);
            nf.writeComponent(this.playerSuffix);
        }
        if (this.method == 0 || this.method == 3 || this.method == 4) {
            nf.writeVarInt(this.players.size());
            for (final String string4 : this.players) {
                nf.writeUtf(string4);
            }
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetPlayerTeamPacket(this);
    }
    
    public String getName() {
        return this.name;
    }
    
    public Component getDisplayName() {
        return this.displayName;
    }
    
    public Collection<String> getPlayers() {
        return this.players;
    }
    
    public int getMethod() {
        return this.method;
    }
    
    public int getOptions() {
        return this.options;
    }
    
    public ChatFormatting getColor() {
        return this.color;
    }
    
    public String getNametagVisibility() {
        return this.nametagVisibility;
    }
    
    public String getCollisionRule() {
        return this.collisionRule;
    }
    
    public Component getPlayerPrefix() {
        return this.playerPrefix;
    }
    
    public Component getPlayerSuffix() {
        return this.playerSuffix;
    }
}
