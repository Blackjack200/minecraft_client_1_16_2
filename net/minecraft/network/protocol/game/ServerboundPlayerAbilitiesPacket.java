package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.network.protocol.Packet;

public class ServerboundPlayerAbilitiesPacket implements Packet<ServerGamePacketListener> {
    private boolean isFlying;
    
    public ServerboundPlayerAbilitiesPacket() {
    }
    
    public ServerboundPlayerAbilitiesPacket(final Abilities bfq) {
        this.isFlying = bfq.flying;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        final byte byte3 = nf.readByte();
        this.isFlying = ((byte3 & 0x2) != 0x0);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        byte byte3 = 0;
        if (this.isFlying) {
            byte3 |= 0x2;
        }
        nf.writeByte(byte3);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handlePlayerAbilities(this);
    }
    
    public boolean isFlying() {
        return this.isFlying;
    }
}
