package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundMovePlayerPacket implements Packet<ServerGamePacketListener> {
    protected double x;
    protected double y;
    protected double z;
    protected float yRot;
    protected float xRot;
    protected boolean onGround;
    protected boolean hasPos;
    protected boolean hasRot;
    
    public ServerboundMovePlayerPacket() {
    }
    
    public ServerboundMovePlayerPacket(final boolean boolean1) {
        this.onGround = boolean1;
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleMovePlayer(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.onGround = (nf.readUnsignedByte() != 0);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.onGround ? 1 : 0);
    }
    
    public double getX(final double double1) {
        return this.hasPos ? this.x : double1;
    }
    
    public double getY(final double double1) {
        return this.hasPos ? this.y : double1;
    }
    
    public double getZ(final double double1) {
        return this.hasPos ? this.z : double1;
    }
    
    public float getYRot(final float float1) {
        return this.hasRot ? this.yRot : float1;
    }
    
    public float getXRot(final float float1) {
        return this.hasRot ? this.xRot : float1;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public static class PosRot extends ServerboundMovePlayerPacket {
        public PosRot() {
            this.hasPos = true;
            this.hasRot = true;
        }
        
        public PosRot(final double double1, final double double2, final double double3, final float float4, final float float5, final boolean boolean6) {
            this.x = double1;
            this.y = double2;
            this.z = double3;
            this.yRot = float4;
            this.xRot = float5;
            this.onGround = boolean6;
            this.hasRot = true;
            this.hasPos = true;
        }
        
        @Override
        public void read(final FriendlyByteBuf nf) throws IOException {
            this.x = nf.readDouble();
            this.y = nf.readDouble();
            this.z = nf.readDouble();
            this.yRot = nf.readFloat();
            this.xRot = nf.readFloat();
            super.read(nf);
        }
        
        @Override
        public void write(final FriendlyByteBuf nf) throws IOException {
            nf.writeDouble(this.x);
            nf.writeDouble(this.y);
            nf.writeDouble(this.z);
            nf.writeFloat(this.yRot);
            nf.writeFloat(this.xRot);
            super.write(nf);
        }
    }
    
    public static class Pos extends ServerboundMovePlayerPacket {
        public Pos() {
            this.hasPos = true;
        }
        
        public Pos(final double double1, final double double2, final double double3, final boolean boolean4) {
            this.x = double1;
            this.y = double2;
            this.z = double3;
            this.onGround = boolean4;
            this.hasPos = true;
        }
        
        @Override
        public void read(final FriendlyByteBuf nf) throws IOException {
            this.x = nf.readDouble();
            this.y = nf.readDouble();
            this.z = nf.readDouble();
            super.read(nf);
        }
        
        @Override
        public void write(final FriendlyByteBuf nf) throws IOException {
            nf.writeDouble(this.x);
            nf.writeDouble(this.y);
            nf.writeDouble(this.z);
            super.write(nf);
        }
    }
    
    public static class Rot extends ServerboundMovePlayerPacket {
        public Rot() {
            this.hasRot = true;
        }
        
        public Rot(final float float1, final float float2, final boolean boolean3) {
            this.yRot = float1;
            this.xRot = float2;
            this.onGround = boolean3;
            this.hasRot = true;
        }
        
        @Override
        public void read(final FriendlyByteBuf nf) throws IOException {
            this.yRot = nf.readFloat();
            this.xRot = nf.readFloat();
            super.read(nf);
        }
        
        @Override
        public void write(final FriendlyByteBuf nf) throws IOException {
            nf.writeFloat(this.yRot);
            nf.writeFloat(this.xRot);
            super.write(nf);
        }
    }
}
