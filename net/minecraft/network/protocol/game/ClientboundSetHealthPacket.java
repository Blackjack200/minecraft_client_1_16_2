package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetHealthPacket implements Packet<ClientGamePacketListener> {
    private float health;
    private int food;
    private float saturation;
    
    public ClientboundSetHealthPacket() {
    }
    
    public ClientboundSetHealthPacket(final float float1, final int integer, final float float3) {
        this.health = float1;
        this.food = integer;
        this.saturation = float3;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.health = nf.readFloat();
        this.food = nf.readVarInt();
        this.saturation = nf.readFloat();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeFloat(this.health);
        nf.writeVarInt(this.food);
        nf.writeFloat(this.saturation);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetHealth(this);
    }
    
    public float getHealth() {
        return this.health;
    }
    
    public int getFood() {
        return this.food;
    }
    
    public float getSaturation() {
        return this.saturation;
    }
}
