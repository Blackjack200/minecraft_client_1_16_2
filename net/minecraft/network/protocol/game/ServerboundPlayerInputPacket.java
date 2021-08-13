package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ServerboundPlayerInputPacket implements Packet<ServerGamePacketListener> {
    private float xxa;
    private float zza;
    private boolean isJumping;
    private boolean isShiftKeyDown;
    
    public ServerboundPlayerInputPacket() {
    }
    
    public ServerboundPlayerInputPacket(final float float1, final float float2, final boolean boolean3, final boolean boolean4) {
        this.xxa = float1;
        this.zza = float2;
        this.isJumping = boolean3;
        this.isShiftKeyDown = boolean4;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.xxa = nf.readFloat();
        this.zza = nf.readFloat();
        final byte byte3 = nf.readByte();
        this.isJumping = ((byte3 & 0x1) > 0);
        this.isShiftKeyDown = ((byte3 & 0x2) > 0);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeFloat(this.xxa);
        nf.writeFloat(this.zza);
        byte byte3 = 0;
        if (this.isJumping) {
            byte3 |= 0x1;
        }
        if (this.isShiftKeyDown) {
            byte3 |= 0x2;
        }
        nf.writeByte(byte3);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handlePlayerInput(this);
    }
    
    public float getXxa() {
        return this.xxa;
    }
    
    public float getZza() {
        return this.zza;
    }
    
    public boolean isJumping() {
        return this.isJumping;
    }
    
    public boolean isShiftKeyDown() {
        return this.isShiftKeyDown;
    }
}
