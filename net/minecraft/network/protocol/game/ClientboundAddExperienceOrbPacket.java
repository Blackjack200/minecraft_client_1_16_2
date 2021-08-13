package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.network.protocol.Packet;

public class ClientboundAddExperienceOrbPacket implements Packet<ClientGamePacketListener> {
    private int id;
    private double x;
    private double y;
    private double z;
    private int value;
    
    public ClientboundAddExperienceOrbPacket() {
    }
    
    public ClientboundAddExperienceOrbPacket(final ExperienceOrb aqd) {
        this.id = aqd.getId();
        this.x = aqd.getX();
        this.y = aqd.getY();
        this.z = aqd.getZ();
        this.value = aqd.getValue();
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readVarInt();
        this.x = nf.readDouble();
        this.y = nf.readDouble();
        this.z = nf.readDouble();
        this.value = nf.readShort();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.id);
        nf.writeDouble(this.x);
        nf.writeDouble(this.y);
        nf.writeDouble(this.z);
        nf.writeShort(this.value);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleAddExperienceOrb(this);
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
    
    public int getValue() {
        return this.value;
    }
}
