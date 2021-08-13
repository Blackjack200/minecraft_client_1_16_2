package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;

public class ClientboundStopSoundPacket implements Packet<ClientGamePacketListener> {
    private ResourceLocation name;
    private SoundSource source;
    
    public ClientboundStopSoundPacket() {
    }
    
    public ClientboundStopSoundPacket(@Nullable final ResourceLocation vk, @Nullable final SoundSource adp) {
        this.name = vk;
        this.source = adp;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        final int integer3 = nf.readByte();
        if ((integer3 & 0x1) > 0) {
            this.source = nf.<SoundSource>readEnum(SoundSource.class);
        }
        if ((integer3 & 0x2) > 0) {
            this.name = nf.readResourceLocation();
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        if (this.source != null) {
            if (this.name != null) {
                nf.writeByte(3);
                nf.writeEnum(this.source);
                nf.writeResourceLocation(this.name);
            }
            else {
                nf.writeByte(1);
                nf.writeEnum(this.source);
            }
        }
        else if (this.name != null) {
            nf.writeByte(2);
            nf.writeResourceLocation(this.name);
        }
        else {
            nf.writeByte(0);
        }
    }
    
    @Nullable
    public ResourceLocation getName() {
        return this.name;
    }
    
    @Nullable
    public SoundSource getSource() {
        return this.source;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleStopSoundEvent(this);
    }
}
