package net.minecraft.world.level.pathfinder;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;

public abstract class NodeEvaluator {
    protected PathNavigationRegion level;
    protected Mob mob;
    protected final Int2ObjectMap<Node> nodes;
    protected int entityWidth;
    protected int entityHeight;
    protected int entityDepth;
    protected boolean canPassDoors;
    protected boolean canOpenDoors;
    protected boolean canFloat;
    
    public NodeEvaluator() {
        this.nodes = (Int2ObjectMap<Node>)new Int2ObjectOpenHashMap();
    }
    
    public void prepare(final PathNavigationRegion bsf, final Mob aqk) {
        this.level = bsf;
        this.mob = aqk;
        this.nodes.clear();
        this.entityWidth = Mth.floor(aqk.getBbWidth() + 1.0f);
        this.entityHeight = Mth.floor(aqk.getBbHeight() + 1.0f);
        this.entityDepth = Mth.floor(aqk.getBbWidth() + 1.0f);
    }
    
    public void done() {
        this.level = null;
        this.mob = null;
    }
    
    protected Node getNode(final BlockPos fx) {
        return this.getNode(fx.getX(), fx.getY(), fx.getZ());
    }
    
    protected Node getNode(final int integer1, final int integer2, final int integer3) {
        return (Node)this.nodes.computeIfAbsent(Node.createHash(integer1, integer2, integer3), integer4 -> new Node(integer1, integer2, integer3));
    }
    
    public abstract Node getStart();
    
    public abstract Target getGoal(final double double1, final double double2, final double double3);
    
    public abstract int getNeighbors(final Node[] arr, final Node cwy);
    
    public abstract BlockPathTypes getBlockPathType(final BlockGetter bqz, final int integer2, final int integer3, final int integer4, final Mob aqk, final int integer6, final int integer7, final int integer8, final boolean boolean9, final boolean boolean10);
    
    public abstract BlockPathTypes getBlockPathType(final BlockGetter bqz, final int integer2, final int integer3, final int integer4);
    
    public void setCanPassDoors(final boolean boolean1) {
        this.canPassDoors = boolean1;
    }
    
    public void setCanOpenDoors(final boolean boolean1) {
        this.canOpenDoors = boolean1;
    }
    
    public void setCanFloat(final boolean boolean1) {
        this.canFloat = boolean1;
    }
    
    public boolean canPassDoors() {
        return this.canPassDoors;
    }
    
    public boolean canOpenDoors() {
        return this.canOpenDoors;
    }
    
    public boolean canFloat() {
        return this.canFloat;
    }
}
