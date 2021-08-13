package net.minecraft.world.level.pathfinder;

import net.minecraft.network.FriendlyByteBuf;

public class Target extends Node {
    private float bestHeuristic;
    private Node bestNode;
    private boolean reached;
    
    public Target(final Node cwy) {
        super(cwy.x, cwy.y, cwy.z);
        this.bestHeuristic = Float.MAX_VALUE;
    }
    
    public Target(final int integer1, final int integer2, final int integer3) {
        super(integer1, integer2, integer3);
        this.bestHeuristic = Float.MAX_VALUE;
    }
    
    public void updateBest(final float float1, final Node cwy) {
        if (float1 < this.bestHeuristic) {
            this.bestHeuristic = float1;
            this.bestNode = cwy;
        }
    }
    
    public Node getBestNode() {
        return this.bestNode;
    }
    
    public void setReached() {
        this.reached = true;
    }
    
    public static Target createFromStream(final FriendlyByteBuf nf) {
        final Target cxe2 = new Target(nf.readInt(), nf.readInt(), nf.readInt());
        cxe2.walkedDistance = nf.readFloat();
        cxe2.costMalus = nf.readFloat();
        cxe2.closed = nf.readBoolean();
        cxe2.type = BlockPathTypes.values()[nf.readInt()];
        cxe2.f = nf.readFloat();
        return cxe2;
    }
}
