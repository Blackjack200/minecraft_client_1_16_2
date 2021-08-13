package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.util.Mth;
import net.minecraft.network.FriendlyByteBuf;
import com.google.common.collect.Lists;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import java.util.List;
import net.minecraft.network.protocol.Packet;

public class ClientboundExplodePacket implements Packet<ClientGamePacketListener> {
    private double x;
    private double y;
    private double z;
    private float power;
    private List<BlockPos> toBlow;
    private float knockbackX;
    private float knockbackY;
    private float knockbackZ;
    
    public ClientboundExplodePacket() {
    }
    
    public ClientboundExplodePacket(final double double1, final double double2, final double double3, final float float4, final List<BlockPos> list, final Vec3 dck) {
        this.x = double1;
        this.y = double2;
        this.z = double3;
        this.power = float4;
        this.toBlow = (List<BlockPos>)Lists.newArrayList((Iterable)list);
        if (dck != null) {
            this.knockbackX = (float)dck.x;
            this.knockbackY = (float)dck.y;
            this.knockbackZ = (float)dck.z;
        }
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.x = nf.readFloat();
        this.y = nf.readFloat();
        this.z = nf.readFloat();
        this.power = nf.readFloat();
        final int integer3 = nf.readInt();
        this.toBlow = (List<BlockPos>)Lists.newArrayListWithCapacity(integer3);
        final int integer4 = Mth.floor(this.x);
        final int integer5 = Mth.floor(this.y);
        final int integer6 = Mth.floor(this.z);
        for (int integer7 = 0; integer7 < integer3; ++integer7) {
            final int integer8 = nf.readByte() + integer4;
            final int integer9 = nf.readByte() + integer5;
            final int integer10 = nf.readByte() + integer6;
            this.toBlow.add(new BlockPos(integer8, integer9, integer10));
        }
        this.knockbackX = nf.readFloat();
        this.knockbackY = nf.readFloat();
        this.knockbackZ = nf.readFloat();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeFloat((float)this.x);
        nf.writeFloat((float)this.y);
        nf.writeFloat((float)this.z);
        nf.writeFloat(this.power);
        nf.writeInt(this.toBlow.size());
        final int integer3 = Mth.floor(this.x);
        final int integer4 = Mth.floor(this.y);
        final int integer5 = Mth.floor(this.z);
        for (final BlockPos fx7 : this.toBlow) {
            final int integer6 = fx7.getX() - integer3;
            final int integer7 = fx7.getY() - integer4;
            final int integer8 = fx7.getZ() - integer5;
            nf.writeByte(integer6);
            nf.writeByte(integer7);
            nf.writeByte(integer8);
        }
        nf.writeFloat(this.knockbackX);
        nf.writeFloat(this.knockbackY);
        nf.writeFloat(this.knockbackZ);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleExplosion(this);
    }
    
    public float getKnockbackX() {
        return this.knockbackX;
    }
    
    public float getKnockbackY() {
        return this.knockbackY;
    }
    
    public float getKnockbackZ() {
        return this.knockbackZ;
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
    
    public float getPower() {
        return this.power;
    }
    
    public List<BlockPos> getToBlow() {
        return this.toBlow;
    }
}
