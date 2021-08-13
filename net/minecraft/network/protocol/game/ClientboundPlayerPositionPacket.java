package net.minecraft.network.protocol.game;

import java.util.Iterator;
import java.util.EnumSet;
import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Set;
import net.minecraft.network.protocol.Packet;

public class ClientboundPlayerPositionPacket implements Packet<ClientGamePacketListener> {
    private double x;
    private double y;
    private double z;
    private float yRot;
    private float xRot;
    private Set<RelativeArgument> relativeArguments;
    private int id;
    
    public ClientboundPlayerPositionPacket() {
    }
    
    public ClientboundPlayerPositionPacket(final double double1, final double double2, final double double3, final float float4, final float float5, final Set<RelativeArgument> set, final int integer) {
        this.x = double1;
        this.y = double2;
        this.z = double3;
        this.yRot = float4;
        this.xRot = float5;
        this.relativeArguments = set;
        this.id = integer;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.x = nf.readDouble();
        this.y = nf.readDouble();
        this.z = nf.readDouble();
        this.yRot = nf.readFloat();
        this.xRot = nf.readFloat();
        this.relativeArguments = RelativeArgument.unpack(nf.readUnsignedByte());
        this.id = nf.readVarInt();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeDouble(this.x);
        nf.writeDouble(this.y);
        nf.writeDouble(this.z);
        nf.writeFloat(this.yRot);
        nf.writeFloat(this.xRot);
        nf.writeByte(RelativeArgument.pack(this.relativeArguments));
        nf.writeVarInt(this.id);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleMovePlayer(this);
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
    
    public float getYRot() {
        return this.yRot;
    }
    
    public float getXRot() {
        return this.xRot;
    }
    
    public int getId() {
        return this.id;
    }
    
    public Set<RelativeArgument> getRelativeArguments() {
        return this.relativeArguments;
    }
    
    public enum RelativeArgument {
        X(0), 
        Y(1), 
        Z(2), 
        Y_ROT(3), 
        X_ROT(4);
        
        private final int bit;
        
        private RelativeArgument(final int integer3) {
            this.bit = integer3;
        }
        
        private int getMask() {
            return 1 << this.bit;
        }
        
        private boolean isSet(final int integer) {
            return (integer & this.getMask()) == this.getMask();
        }
        
        public static Set<RelativeArgument> unpack(final int integer) {
            final Set<RelativeArgument> set2 = (Set<RelativeArgument>)EnumSet.noneOf((Class)RelativeArgument.class);
            for (final RelativeArgument a6 : values()) {
                if (a6.isSet(integer)) {
                    set2.add(a6);
                }
            }
            return set2;
        }
        
        public static int pack(final Set<RelativeArgument> set) {
            int integer2 = 0;
            for (final RelativeArgument a4 : set) {
                integer2 |= a4.getMask();
            }
            return integer2;
        }
    }
}
