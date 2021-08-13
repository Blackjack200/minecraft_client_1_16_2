package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.Packet;

public class ClientboundSetEntityMotionPacket implements Packet<ClientGamePacketListener> {
    private int id;
    private int xa;
    private int ya;
    private int za;
    
    public ClientboundSetEntityMotionPacket() {
    }
    
    public ClientboundSetEntityMotionPacket(final Entity apx) {
        this(apx.getId(), apx.getDeltaMovement());
    }
    
    public ClientboundSetEntityMotionPacket(final int integer, final Vec3 dck) {
        this.id = integer;
        final double double4 = 3.9;
        final double double5 = Mth.clamp(dck.x, -3.9, 3.9);
        final double double6 = Mth.clamp(dck.y, -3.9, 3.9);
        final double double7 = Mth.clamp(dck.z, -3.9, 3.9);
        this.xa = (int)(double5 * 8000.0);
        this.ya = (int)(double6 * 8000.0);
        this.za = (int)(double7 * 8000.0);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.id = nf.readVarInt();
        this.xa = nf.readShort();
        this.ya = nf.readShort();
        this.za = nf.readShort();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.id);
        nf.writeShort(this.xa);
        nf.writeShort(this.ya);
        nf.writeShort(this.za);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSetEntityMotion(this);
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getXa() {
        return this.xa;
    }
    
    public int getYa() {
        return this.ya;
    }
    
    public int getZa() {
        return this.za;
    }
}
