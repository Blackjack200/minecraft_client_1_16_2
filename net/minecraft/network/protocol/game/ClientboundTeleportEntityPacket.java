package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.Packet;

public class ClientboundTeleportEntityPacket implements Packet<ClientGamePacketListener> {
    private int id;
    private double x;
    private double y;
    private double z;
    private byte yRot;
    private byte xRot;
    private boolean onGround;
    
    public ClientboundTeleportEntityPacket() {
    }
    
    public ClientboundTeleportEntityPacket(final Entity apx) {
        this.id = apx.getId();
        this.x = apx.getX();
        this.y = apx.getY();
        this.z = apx.getZ();
        this.yRot = (byte)(apx.yRot * 256.0f / 360.0f);
        this.xRot = (byte)(apx.xRot * 256.0f / 360.0f);
        this.onGround = apx.isOnGround();
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readVarInt();
        this.x = nf.readDouble();
        this.y = nf.readDouble();
        this.z = nf.readDouble();
        this.yRot = nf.readByte();
        this.xRot = nf.readByte();
        this.onGround = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.id);
        nf.writeDouble(this.x);
        nf.writeDouble(this.y);
        nf.writeDouble(this.z);
        nf.writeByte(this.yRot);
        nf.writeByte(this.xRot);
        nf.writeBoolean(this.onGround);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleTeleportEntity(this);
    }
    
    public int getId() {
        return this.id;
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
    
    public boolean isOnGround() {
        return this.onGround;
    }
}
