package net.minecraft.world.phys;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class BlockHitResult extends HitResult {
    private final Direction direction;
    private final BlockPos blockPos;
    private final boolean miss;
    private final boolean inside;
    
    public static BlockHitResult miss(final Vec3 dck, final Direction gc, final BlockPos fx) {
        return new BlockHitResult(true, dck, gc, fx, false);
    }
    
    public BlockHitResult(final Vec3 dck, final Direction gc, final BlockPos fx, final boolean boolean4) {
        this(false, dck, gc, fx, boolean4);
    }
    
    private BlockHitResult(final boolean boolean1, final Vec3 dck, final Direction gc, final BlockPos fx, final boolean boolean5) {
        super(dck);
        this.miss = boolean1;
        this.direction = gc;
        this.blockPos = fx;
        this.inside = boolean5;
    }
    
    public BlockHitResult withDirection(final Direction gc) {
        return new BlockHitResult(this.miss, this.location, gc, this.blockPos, this.inside);
    }
    
    public BlockHitResult withPosition(final BlockPos fx) {
        return new BlockHitResult(this.miss, this.location, this.direction, fx, this.inside);
    }
    
    public BlockPos getBlockPos() {
        return this.blockPos;
    }
    
    public Direction getDirection() {
        return this.direction;
    }
    
    @Override
    public Type getType() {
        return this.miss ? Type.MISS : Type.BLOCK;
    }
    
    public boolean isInside() {
        return this.inside;
    }
}
