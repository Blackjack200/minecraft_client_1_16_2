package net.minecraft.world.entity.ai.memory;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.behavior.PositionTracker;

public class WalkTarget {
    private final PositionTracker target;
    private final float speedModifier;
    private final int closeEnoughDist;
    
    public WalkTarget(final BlockPos fx, final float float2, final int integer) {
        this(new BlockPosTracker(fx), float2, integer);
    }
    
    public WalkTarget(final Vec3 dck, final float float2, final int integer) {
        this(new BlockPosTracker(new BlockPos(dck)), float2, integer);
    }
    
    public WalkTarget(final PositionTracker asy, final float float2, final int integer) {
        this.target = asy;
        this.speedModifier = float2;
        this.closeEnoughDist = integer;
    }
    
    public PositionTracker getTarget() {
        return this.target;
    }
    
    public float getSpeedModifier() {
        return this.speedModifier;
    }
    
    public int getCloseEnoughDist() {
        return this.closeEnoughDist;
    }
}
