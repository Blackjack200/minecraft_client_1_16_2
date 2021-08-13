package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;
import net.minecraft.network.protocol.Packet;

public class ClientboundMoveEntityPacket implements Packet<ClientGamePacketListener> {
    protected int entityId;
    protected short xa;
    protected short ya;
    protected short za;
    protected byte yRot;
    protected byte xRot;
    protected boolean onGround;
    protected boolean hasRot;
    protected boolean hasPos;
    
    public static long entityToPacket(final double double1) {
        return Mth.lfloor(double1 * 4096.0);
    }
    
    public static double packetToEntity(final long long1) {
        return long1 / 4096.0;
    }
    
    public Vec3 updateEntityPosition(final Vec3 dck) {
        final double double3 = (this.xa == 0) ? dck.x : packetToEntity(entityToPacket(dck.x) + this.xa);
        final double double4 = (this.ya == 0) ? dck.y : packetToEntity(entityToPacket(dck.y) + this.ya);
        final double double5 = (this.za == 0) ? dck.z : packetToEntity(entityToPacket(dck.z) + this.za);
        return new Vec3(double3, double4, double5);
    }
    
    public static Vec3 packetToEntity(final long long1, final long long2, final long long3) {
        return new Vec3((double)long1, (double)long2, (double)long3).scale(2.44140625E-4);
    }
    
    public ClientboundMoveEntityPacket() {
    }
    
    public ClientboundMoveEntityPacket(final int integer) {
        this.entityId = integer;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.entityId = nf.readVarInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.entityId);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleMoveEntity(this);
    }
    
    public String toString() {
        return "Entity_" + super.toString();
    }
    
    @Nullable
    public Entity getEntity(final Level bru) {
        return bru.getEntity(this.entityId);
    }
    
    public byte getyRot() {
        return this.yRot;
    }
    
    public byte getxRot() {
        return this.xRot;
    }
    
    public boolean hasRotation() {
        return this.hasRot;
    }
    
    public boolean hasPosition() {
        return this.hasPos;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public static class PosRot extends ClientboundMoveEntityPacket {
        public PosRot() {
            this.hasRot = true;
            this.hasPos = true;
        }
        
        public PosRot(final int integer, final short short2, final short short3, final short short4, final byte byte5, final byte byte6, final boolean boolean7) {
            super(integer);
            this.xa = short2;
            this.ya = short3;
            this.za = short4;
            this.yRot = byte5;
            this.xRot = byte6;
            this.onGround = boolean7;
            this.hasRot = true;
            this.hasPos = true;
        }
        
        @Override
        public void read(final FriendlyByteBuf nf) throws IOException {
            super.read(nf);
            this.xa = nf.readShort();
            this.ya = nf.readShort();
            this.za = nf.readShort();
            this.yRot = nf.readByte();
            this.xRot = nf.readByte();
            this.onGround = nf.readBoolean();
        }
        
        @Override
        public void write(final FriendlyByteBuf nf) throws IOException {
            super.write(nf);
            nf.writeShort(this.xa);
            nf.writeShort(this.ya);
            nf.writeShort(this.za);
            nf.writeByte(this.yRot);
            nf.writeByte(this.xRot);
            nf.writeBoolean(this.onGround);
        }
    }
    
    public static class Pos extends ClientboundMoveEntityPacket {
        public Pos() {
            this.hasPos = true;
        }
        
        public Pos(final int integer, final short short2, final short short3, final short short4, final boolean boolean5) {
            super(integer);
            this.xa = short2;
            this.ya = short3;
            this.za = short4;
            this.onGround = boolean5;
            this.hasPos = true;
        }
        
        @Override
        public void read(final FriendlyByteBuf nf) throws IOException {
            super.read(nf);
            this.xa = nf.readShort();
            this.ya = nf.readShort();
            this.za = nf.readShort();
            this.onGround = nf.readBoolean();
        }
        
        @Override
        public void write(final FriendlyByteBuf nf) throws IOException {
            super.write(nf);
            nf.writeShort(this.xa);
            nf.writeShort(this.ya);
            nf.writeShort(this.za);
            nf.writeBoolean(this.onGround);
        }
    }
    
    public static class Rot extends ClientboundMoveEntityPacket {
        public Rot() {
            this.hasRot = true;
        }
        
        public Rot(final int integer, final byte byte2, final byte byte3, final boolean boolean4) {
            super(integer);
            this.yRot = byte2;
            this.xRot = byte3;
            this.hasRot = true;
            this.onGround = boolean4;
        }
        
        @Override
        public void read(final FriendlyByteBuf nf) throws IOException {
            super.read(nf);
            this.yRot = nf.readByte();
            this.xRot = nf.readByte();
            this.onGround = nf.readBoolean();
        }
        
        @Override
        public void write(final FriendlyByteBuf nf) throws IOException {
            super.write(nf);
            nf.writeByte(this.yRot);
            nf.writeByte(this.xRot);
            nf.writeBoolean(this.onGround);
        }
    }
}
