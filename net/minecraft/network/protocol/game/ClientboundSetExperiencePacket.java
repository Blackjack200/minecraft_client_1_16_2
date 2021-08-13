package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetExperiencePacket implements Packet<ClientGamePacketListener> {
    private float experienceProgress;
    private int totalExperience;
    private int experienceLevel;
    
    public ClientboundSetExperiencePacket() {
    }
    
    public ClientboundSetExperiencePacket(final float float1, final int integer2, final int integer3) {
        this.experienceProgress = float1;
        this.totalExperience = integer2;
        this.experienceLevel = integer3;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.experienceProgress = nf.readFloat();
        this.experienceLevel = nf.readVarInt();
        this.totalExperience = nf.readVarInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeFloat(this.experienceProgress);
        nf.writeVarInt(this.experienceLevel);
        nf.writeVarInt(this.totalExperience);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetExperience(this);
    }
    
    public float getExperienceProgress() {
        return this.experienceProgress;
    }
    
    public int getTotalExperience() {
        return this.totalExperience;
    }
    
    public int getExperienceLevel() {
        return this.experienceLevel;
    }
}
