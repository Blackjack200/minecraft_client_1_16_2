package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import java.util.Objects;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.ServerScoreboard;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetScorePacket implements Packet<ClientGamePacketListener> {
    private String owner;
    @Nullable
    private String objectiveName;
    private int score;
    private ServerScoreboard.Method method;
    
    public ClientboundSetScorePacket() {
        this.owner = "";
    }
    
    public ClientboundSetScorePacket(final ServerScoreboard.Method a, @Nullable final String string2, final String string3, final int integer) {
        this.owner = "";
        if (a != ServerScoreboard.Method.REMOVE && string2 == null) {
            throw new IllegalArgumentException("Need an objective name");
        }
        this.owner = string3;
        this.objectiveName = string2;
        this.score = integer;
        this.method = a;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.owner = nf.readUtf(40);
        this.method = nf.<ServerScoreboard.Method>readEnum(ServerScoreboard.Method.class);
        final String string3 = nf.readUtf(16);
        this.objectiveName = (Objects.equals(string3, "") ? null : string3);
        if (this.method != ServerScoreboard.Method.REMOVE) {
            this.score = nf.readVarInt();
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeUtf(this.owner);
        nf.writeEnum(this.method);
        nf.writeUtf((this.objectiveName == null) ? "" : this.objectiveName);
        if (this.method != ServerScoreboard.Method.REMOVE) {
            nf.writeVarInt(this.score);
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetScore(this);
    }
    
    public String getOwner() {
        return this.owner;
    }
    
    @Nullable
    public String getObjectiveName() {
        return this.objectiveName;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public ServerScoreboard.Method getMethod() {
        return this.method;
    }
}
