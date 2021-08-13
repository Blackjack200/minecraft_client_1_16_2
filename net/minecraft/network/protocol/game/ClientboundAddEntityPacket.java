package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.EntityType;
import java.util.UUID;
import net.minecraft.network.protocol.Packet;

public class ClientboundAddEntityPacket implements Packet<ClientGamePacketListener> {
    private int id;
    private UUID uuid;
    private double x;
    private double y;
    private double z;
    private int xa;
    private int ya;
    private int za;
    private int xRot;
    private int yRot;
    private EntityType<?> type;
    private int data;
    
    public ClientboundAddEntityPacket() {
    }
    
    public ClientboundAddEntityPacket(final int integer1, final UUID uUID, final double double3, final double double4, final double double5, final float float6, final float float7, final EntityType<?> aqb, final int integer9, final Vec3 dck) {
        this.id = integer1;
        this.uuid = uUID;
        this.x = double3;
        this.y = double4;
        this.z = double5;
        this.xRot = Mth.floor(float6 * 256.0f / 360.0f);
        this.yRot = Mth.floor(float7 * 256.0f / 360.0f);
        this.type = aqb;
        this.data = integer9;
        this.xa = (int)(Mth.clamp(dck.x, -3.9, 3.9) * 8000.0);
        this.ya = (int)(Mth.clamp(dck.y, -3.9, 3.9) * 8000.0);
        this.za = (int)(Mth.clamp(dck.z, -3.9, 3.9) * 8000.0);
    }
    
    public ClientboundAddEntityPacket(final Entity apx) {
        this(apx, 0);
    }
    
    public ClientboundAddEntityPacket(final Entity apx, final int integer) {
        this(apx.getId(), apx.getUUID(), apx.getX(), apx.getY(), apx.getZ(), apx.xRot, apx.yRot, apx.getType(), integer, apx.getDeltaMovement());
    }
    
    public ClientboundAddEntityPacket(final Entity apx, final EntityType<?> aqb, final int integer, final BlockPos fx) {
        this(apx.getId(), apx.getUUID(), fx.getX(), fx.getY(), fx.getZ(), apx.xRot, apx.yRot, aqb, integer, apx.getDeltaMovement());
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readVarInt();
        this.uuid = nf.readUUID();
        this.type = Registry.ENTITY_TYPE.byId(nf.readVarInt());
        this.x = nf.readDouble();
        this.y = nf.readDouble();
        this.z = nf.readDouble();
        this.xRot = nf.readByte();
        this.yRot = nf.readByte();
        this.data = nf.readInt();
        this.xa = nf.readShort();
        this.ya = nf.readShort();
        this.za = nf.readShort();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.id);
        nf.writeUUID(this.uuid);
        nf.writeVarInt(Registry.ENTITY_TYPE.getId(this.type));
        nf.writeDouble(this.x);
        nf.writeDouble(this.y);
        nf.writeDouble(this.z);
        nf.writeByte(this.xRot);
        nf.writeByte(this.yRot);
        nf.writeInt(this.data);
        nf.writeShort(this.xa);
        nf.writeShort(this.ya);
        nf.writeShort(this.za);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleAddEntity(this);
    }
    
    public int getId() {
        return this.id;
    }
    
    public UUID getUUID() {
        return this.uuid;
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
    
    public double getXa() {
        return this.xa / 8000.0;
    }
    
    public double getYa() {
        return this.ya / 8000.0;
    }
    
    public double getZa() {
        return this.za / 8000.0;
    }
    
    public int getxRot() {
        return this.xRot;
    }
    
    public int getyRot() {
        return this.yRot;
    }
    
    public EntityType<?> getType() {
        return this.type;
    }
    
    public int getData() {
        return this.data;
    }
}
