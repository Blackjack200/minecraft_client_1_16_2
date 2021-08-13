package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.protocol.Packet;

public class ClientboundPlayerLookAtPacket implements Packet<ClientGamePacketListener> {
    private double x;
    private double y;
    private double z;
    private int entity;
    private EntityAnchorArgument.Anchor fromAnchor;
    private EntityAnchorArgument.Anchor toAnchor;
    private boolean atEntity;
    
    public ClientboundPlayerLookAtPacket() {
    }
    
    public ClientboundPlayerLookAtPacket(final EntityAnchorArgument.Anchor a, final double double2, final double double3, final double double4) {
        this.fromAnchor = a;
        this.x = double2;
        this.y = double3;
        this.z = double4;
    }
    
    public ClientboundPlayerLookAtPacket(final EntityAnchorArgument.Anchor a1, final Entity apx, final EntityAnchorArgument.Anchor a3) {
        this.fromAnchor = a1;
        this.entity = apx.getId();
        this.toAnchor = a3;
        final Vec3 dck5 = a3.apply(apx);
        this.x = dck5.x;
        this.y = dck5.y;
        this.z = dck5.z;
        this.atEntity = true;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.fromAnchor = nf.<EntityAnchorArgument.Anchor>readEnum(EntityAnchorArgument.Anchor.class);
        this.x = nf.readDouble();
        this.y = nf.readDouble();
        this.z = nf.readDouble();
        if (nf.readBoolean()) {
            this.atEntity = true;
            this.entity = nf.readVarInt();
            this.toAnchor = nf.<EntityAnchorArgument.Anchor>readEnum(EntityAnchorArgument.Anchor.class);
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeEnum(this.fromAnchor);
        nf.writeDouble(this.x);
        nf.writeDouble(this.y);
        nf.writeDouble(this.z);
        nf.writeBoolean(this.atEntity);
        if (this.atEntity) {
            nf.writeVarInt(this.entity);
            nf.writeEnum(this.toAnchor);
        }
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleLookAt(this);
    }
    
    public EntityAnchorArgument.Anchor getFromAnchor() {
        return this.fromAnchor;
    }
    
    @Nullable
    public Vec3 getPosition(final Level bru) {
        if (!this.atEntity) {
            return new Vec3(this.x, this.y, this.z);
        }
        final Entity apx3 = bru.getEntity(this.entity);
        if (apx3 == null) {
            return new Vec3(this.x, this.y, this.z);
        }
        return this.toAnchor.apply(apx3);
    }
}
