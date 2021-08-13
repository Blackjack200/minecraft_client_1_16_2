package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;

public class ClientboundAddPlayerPacket implements Packet<ClientGamePacketListener> {
    private int entityId;
    private UUID playerId;
    private double x;
    private double y;
    private double z;
    private byte yRot;
    private byte xRot;
    
    public ClientboundAddPlayerPacket() {
    }
    
    public ClientboundAddPlayerPacket(final Player bft) {
        this.entityId = bft.getId();
        this.playerId = bft.getGameProfile().getId();
        this.x = bft.getX();
        this.y = bft.getY();
        this.z = bft.getZ();
        this.yRot = (byte)(bft.yRot * 256.0f / 360.0f);
        this.xRot = (byte)(bft.xRot * 256.0f / 360.0f);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.entityId = nf.readVarInt();
        this.playerId = nf.readUUID();
        this.x = nf.readDouble();
        this.y = nf.readDouble();
        this.z = nf.readDouble();
        this.yRot = nf.readByte();
        this.xRot = nf.readByte();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.entityId);
        nf.writeUUID(this.playerId);
        nf.writeDouble(this.x);
        nf.writeDouble(this.y);
        nf.writeDouble(this.z);
        nf.writeByte(this.yRot);
        nf.writeByte(this.xRot);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleAddPlayer(this);
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public UUID getPlayerId() {
        return this.playerId;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public byte getyRot() {
        return this.yRot;
    }
    
    public byte getxRot() {
        return this.xRot;
    }
}
