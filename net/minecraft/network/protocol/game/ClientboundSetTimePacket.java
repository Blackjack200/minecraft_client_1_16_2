package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetTimePacket implements Packet<ClientGamePacketListener> {
    private long gameTime;
    private long dayTime;
    
    public ClientboundSetTimePacket() {
    }
    
    public ClientboundSetTimePacket(final long long1, final long long2, final boolean boolean3) {
        this.gameTime = long1;
        this.dayTime = long2;
        if (!boolean3) {
            this.dayTime = -this.dayTime;
            if (this.dayTime == 0L) {
                this.dayTime = -1L;
            }
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.gameTime = nf.readLong();
        this.dayTime = nf.readLong();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeLong(this.gameTime);
        nf.writeLong(this.dayTime);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetTime(this);
    }
    
    public long getGameTime() {
        return this.gameTime;
    }
    
    public long getDayTime() {
        return this.dayTime;
    }
}
