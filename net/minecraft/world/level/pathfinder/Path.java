package net.minecraft.world.level.pathfinder;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import java.util.Set;
import java.util.List;

public class Path {
    private final List<Node> nodes;
    private Node[] openSet;
    private Node[] closedSet;
    private Set<Target> targetNodes;
    private int nextNodeIndex;
    private final BlockPos target;
    private final float distToTarget;
    private final boolean reached;
    
    public Path(final List<Node> list, final BlockPos fx, final boolean boolean3) {
        this.openSet = new Node[0];
        this.closedSet = new Node[0];
        this.nodes = list;
        this.target = fx;
        this.distToTarget = (list.isEmpty() ? Float.MAX_VALUE : ((Node)this.nodes.get(this.nodes.size() - 1)).distanceManhattan(this.target));
        this.reached = boolean3;
    }
    
    public void advance() {
        ++this.nextNodeIndex;
    }
    
    public boolean notStarted() {
        return this.nextNodeIndex <= 0;
    }
    
    public boolean isDone() {
        return this.nextNodeIndex >= this.nodes.size();
    }
    
    @Nullable
    public Node getEndNode() {
        if (!this.nodes.isEmpty()) {
            return (Node)this.nodes.get(this.nodes.size() - 1);
        }
        return null;
    }
    
    public Node getNode(final int integer) {
        return (Node)this.nodes.get(integer);
    }
    
    public void truncateNodes(final int integer) {
        if (this.nodes.size() > integer) {
            this.nodes.subList(integer, this.nodes.size()).clear();
        }
    }
    
    public void replaceNode(final int integer, final Node cwy) {
        this.nodes.set(integer, cwy);
    }
    
    public int getNodeCount() {
        return this.nodes.size();
    }
    
    public int getNextNodeIndex() {
        return this.nextNodeIndex;
    }
    
    public void setNextNodeIndex(final int integer) {
        this.nextNodeIndex = integer;
    }
    
    public Vec3 getEntityPosAtNode(final Entity apx, final int integer) {
        final Node cwy4 = (Node)this.nodes.get(integer);
        final double double5 = cwy4.x + (int)(apx.getBbWidth() + 1.0f) * 0.5;
        final double double6 = cwy4.y;
        final double double7 = cwy4.z + (int)(apx.getBbWidth() + 1.0f) * 0.5;
        return new Vec3(double5, double6, double7);
    }
    
    public BlockPos getNodePos(final int integer) {
        return ((Node)this.nodes.get(integer)).asBlockPos();
    }
    
    public Vec3 getNextEntityPos(final Entity apx) {
        return this.getEntityPosAtNode(apx, this.nextNodeIndex);
    }
    
    public BlockPos getNextNodePos() {
        return ((Node)this.nodes.get(this.nextNodeIndex)).asBlockPos();
    }
    
    public Node getNextNode() {
        return (Node)this.nodes.get(this.nextNodeIndex);
    }
    
    @Nullable
    public Node getPreviousNode() {
        return (this.nextNodeIndex > 0) ? ((Node)this.nodes.get(this.nextNodeIndex - 1)) : null;
    }
    
    public boolean sameAs(@Nullable final Path cxa) {
        if (cxa == null) {
            return false;
        }
        if (cxa.nodes.size() != this.nodes.size()) {
            return false;
        }
        for (int integer3 = 0; integer3 < this.nodes.size(); ++integer3) {
            final Node cwy4 = (Node)this.nodes.get(integer3);
            final Node cwy5 = (Node)cxa.nodes.get(integer3);
            if (cwy4.x != cwy5.x || cwy4.y != cwy5.y || cwy4.z != cwy5.z) {
                return false;
            }
        }
        return true;
    }
    
    public boolean canReach() {
        return this.reached;
    }
    
    public Node[] getOpenSet() {
        return this.openSet;
    }
    
    public Node[] getClosedSet() {
        return this.closedSet;
    }
    
    public static Path createFromStream(final FriendlyByteBuf nf) {
        final boolean boolean2 = nf.readBoolean();
        final int integer3 = nf.readInt();
        final int integer4 = nf.readInt();
        final Set<Target> set5 = (Set<Target>)Sets.newHashSet();
        for (int integer5 = 0; integer5 < integer4; ++integer5) {
            set5.add(Target.createFromStream(nf));
        }
        final BlockPos fx6 = new BlockPos(nf.readInt(), nf.readInt(), nf.readInt());
        final List<Node> list7 = (List<Node>)Lists.newArrayList();
        for (int integer6 = nf.readInt(), integer7 = 0; integer7 < integer6; ++integer7) {
            list7.add(Node.createFromStream(nf));
        }
        final Node[] arr9 = new Node[nf.readInt()];
        for (int integer8 = 0; integer8 < arr9.length; ++integer8) {
            arr9[integer8] = Node.createFromStream(nf);
        }
        final Node[] arr10 = new Node[nf.readInt()];
        for (int integer9 = 0; integer9 < arr10.length; ++integer9) {
            arr10[integer9] = Node.createFromStream(nf);
        }
        final Path cxa11 = new Path(list7, fx6, boolean2);
        cxa11.openSet = arr9;
        cxa11.closedSet = arr10;
        cxa11.targetNodes = set5;
        cxa11.nextNodeIndex = integer3;
        return cxa11;
    }
    
    public String toString() {
        return new StringBuilder().append("Path(length=").append(this.nodes.size()).append(")").toString();
    }
    
    public BlockPos getTarget() {
        return this.target;
    }
    
    public float getDistToTarget() {
        return this.distToTarget;
    }
}
