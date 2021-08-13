package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.Packet;

public class ServerboundMoveVehiclePacket implements Packet<ServerGamePacketListener> {
    private double x;
    private double y;
    private double z;
    private float yRot;
    private float xRot;
    
    public ServerboundMoveVehiclePacket() {
    }
    
    public ServerboundMoveVehiclePacket(final Entity apx) {
        this.x = apx.getX();
        this.y = apx.getY();
        this.z = apx.getZ();
        this.yRot = apx.yRot;
        this.xRot = apx.xRot;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.x = nf.readDouble();
        this.y = nf.readDouble();
        this.z = nf.readDouble();
        this.yRot = nf.readFloat();
        this.xRot = nf.readFloat();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeDouble(this.x);
        nf.writeDouble(this.y);
        nf.writeDouble(this.z);
        nf.writeFloat(this.yRot);
        nf.writeFloat(this.xRot);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleMoveVehicle(this);
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
    
    public float getYRot() {
        return this.yRot;
    }
    
    public float getXRot() {
        return this.xRot;
    }
}
