package net.minecraft.world.level.pathfinder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;

public class Node {
    public final int x;
    public final int y;
    public final int z;
    private final int hash;
    public int heapIdx;
    public float g;
    public float h;
    public float f;
    public Node cameFrom;
    public boolean closed;
    public float walkedDistance;
    public float costMalus;
    public BlockPathTypes type;
    
    public Node(final int integer1, final int integer2, final int integer3) {
        this.heapIdx = -1;
        this.type = BlockPathTypes.BLOCKED;
        this.x = integer1;
        this.y = integer2;
        this.z = integer3;
        this.hash = createHash(integer1, integer2, integer3);
    }
    
    public Node cloneAndMove(final int integer1, final int integer2, final int integer3) {
        final Node cwy5 = new Node(integer1, integer2, integer3);
        cwy5.heapIdx = this.heapIdx;
        cwy5.g = this.g;
        cwy5.h = this.h;
        cwy5.f = this.f;
        cwy5.cameFrom = this.cameFrom;
        cwy5.closed = this.closed;
        cwy5.walkedDistance = this.walkedDistance;
        cwy5.costMalus = this.costMalus;
        cwy5.type = this.type;
        return cwy5;
    }
    
    public static int createHash(final int integer1, final int integer2, final int integer3) {
        return (integer2 & 0xFF) | (integer1 & 0x7FFF) << 8 | (integer3 & 0x7FFF) << 24 | ((integer1 < 0) ? Integer.MIN_VALUE : 0) | ((integer3 < 0) ? 32768 : 0);
    }
    
    public float distanceTo(final Node cwy) {
        final float float3 = (float)(cwy.x - this.x);
        final float float4 = (float)(cwy.y - this.y);
        final float float5 = (float)(cwy.z - this.z);
        return Mth.sqrt(float3 * float3 + float4 * float4 + float5 * float5);
    }
    
    public float distanceToSqr(final Node cwy) {
        final float float3 = (float)(cwy.x - this.x);
        final float float4 = (float)(cwy.y - this.y);
        final float float5 = (float)(cwy.z - this.z);
        return float3 * float3 + float4 * float4 + float5 * float5;
    }
    
    public float distanceManhattan(final Node cwy) {
        final float float3 = (float)Math.abs(cwy.x - this.x);
        final float float4 = (float)Math.abs(cwy.y - this.y);
        final float float5 = (float)Math.abs(cwy.z - this.z);
        return float3 + float4 + float5;
    }
    
    public float distanceManhattan(final BlockPos fx) {
        final float float3 = (float)Math.abs(fx.getX() - this.x);
        final float float4 = (float)Math.abs(fx.getY() - this.y);
        final float float5 = (float)Math.abs(fx.getZ() - this.z);
        return float3 + float4 + float5;
    }
    
    public BlockPos asBlockPos() {
        return new BlockPos(this.x, this.y, this.z);
    }
    
    public boolean equals(final Object object) {
        if (object instanceof Node) {
            final Node cwy3 = (Node)object;
            return this.hash == cwy3.hash && this.x == cwy3.x && this.y == cwy3.y && this.z == cwy3.z;
        }
        return false;
    }
    
    public int hashCode() {
        return this.hash;
    }
    
    public boolean inOpenSet() {
        return this.heapIdx >= 0;
    }
    
    public String toString() {
        return new StringBuilder().append("Node{x=").append(this.x).append(", y=").append(this.y).append(", z=").append(this.z).append('}').toString();
    }
    
    public static Node createFromStream(final FriendlyByteBuf nf) {
        final Node cwy2 = new Node(nf.readInt(), nf.readInt(), nf.readInt());
        cwy2.walkedDistance = nf.readFloat();
        cwy2.costMalus = nf.readFloat();
        cwy2.closed = nf.readBoolean();
        cwy2.type = BlockPathTypes.values()[nf.readInt()];
        cwy2.f = nf.readFloat();
        return cwy2;
    }
}
