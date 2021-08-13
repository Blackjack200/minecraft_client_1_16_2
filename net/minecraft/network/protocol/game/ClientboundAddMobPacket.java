package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.LivingEntity;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;

public class ClientboundAddMobPacket implements Packet<ClientGamePacketListener> {
    private int id;
    private UUID uuid;
    private int type;
    private double x;
    private double y;
    private double z;
    private int xd;
    private int yd;
    private int zd;
    private byte yRot;
    private byte xRot;
    private byte yHeadRot;
    
    public ClientboundAddMobPacket() {
    }
    
    public ClientboundAddMobPacket(final LivingEntity aqj) {
        this.id = aqj.getId();
        this.uuid = aqj.getUUID();
        this.type = Registry.ENTITY_TYPE.getId(aqj.getType());
        this.x = aqj.getX();
        this.y = aqj.getY();
        this.z = aqj.getZ();
        this.yRot = (byte)(aqj.yRot * 256.0f / 360.0f);
        this.xRot = (byte)(aqj.xRot * 256.0f / 360.0f);
        this.yHeadRot = (byte)(aqj.yHeadRot * 256.0f / 360.0f);
        final double double3 = 3.9;
        final Vec3 dck5 = aqj.getDeltaMovement();
        final double double4 = Mth.clamp(dck5.x, -3.9, 3.9);
        final double double5 = Mth.clamp(dck5.y, -3.9, 3.9);
        final double double6 = Mth.clamp(dck5.z, -3.9, 3.9);
        this.xd = (int)(double4 * 8000.0);
        this.yd = (int)(double5 * 8000.0);
        this.zd = (int)(double6 * 8000.0);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readVarInt();
        this.uuid = nf.readUUID();
        this.type = nf.readVarInt();
        this.x = nf.readDouble();
        this.y = nf.readDouble();
        this.z = nf.readDouble();
        this.yRot = nf.readByte();
        this.xRot = nf.readByte();
        this.yHeadRot = nf.readByte();
        this.xd = nf.readShort();
        this.yd = nf.readShort();
        this.zd = nf.readShort();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.id);
        nf.writeUUID(this.uuid);
        nf.writeVarInt(this.type);
        nf.writeDouble(this.x);
        nf.writeDouble(this.y);
        nf.writeDouble(this.z);
        nf.writeByte(this.yRot);
        nf.writeByte(this.xRot);
        nf.writeByte(this.yHeadRot);
        nf.writeShort(this.xd);
        nf.writeShort(this.yd);
        nf.writeShort(this.zd);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleAddMob(this);
    }
    
    public int getId() {
        return this.id;
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    public int getType() {
        return this.type;
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
    
    public int getXd() {
        return this.xd;
    }
    
    public int getYd() {
        return this.yd;
    }
    
    public int getZd() {
        return this.zd;
    }
    
    public byte getyRot() {
        return this.yRot;
    }
    
    public byte getxRot() {
        return this.xRot;
    }
    
    public byte getyHeadRot() {
        return this.yHeadRot;
    }
}
