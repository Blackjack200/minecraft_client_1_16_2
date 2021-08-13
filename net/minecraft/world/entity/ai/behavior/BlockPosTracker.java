package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;

public class BlockPosTracker implements PositionTracker {
    private final BlockPos blockPos;
    private final Vec3 centerPosition;
    
    public BlockPosTracker(final BlockPos fx) {
        this.blockPos = fx;
        this.centerPosition = Vec3.atCenterOf(fx);
    }
    
    public Vec3 currentPosition() {
        return this.centerPosition;
    }
    
    public BlockPos currentBlockPosition() {
        return this.blockPos;
    }
    
    public boolean isVisibleBy(final LivingEntity aqj) {
        return true;
    }
    
    public String toString() {
        return new StringBuilder().append("BlockPosTracker{blockPos=").append(this.blockPos).append(", centerPosition=").append(this.centerPosition).append('}').toString();
    }
}
