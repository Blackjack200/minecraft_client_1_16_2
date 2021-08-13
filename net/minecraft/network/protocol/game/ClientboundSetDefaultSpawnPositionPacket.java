package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetDefaultSpawnPositionPacket implements Packet<ClientGamePacketListener> {
    private BlockPos pos;
    private float angle;
    
    public ClientboundSetDefaultSpawnPositionPacket() {
    }
    
    public ClientboundSetDefaultSpawnPositionPacket(final BlockPos fx, final float float2) {
        this.pos = fx;
        this.angle = float2;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.pos = nf.readBlockPos();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBlockPos(this.pos);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetSpawn(this);
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public float getAngle() {
        return this.angle;
    }
}
