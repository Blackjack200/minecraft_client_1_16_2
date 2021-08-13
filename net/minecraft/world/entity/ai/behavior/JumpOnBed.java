package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import java.util.Optional;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;

public class JumpOnBed extends Behavior<Mob> {
    private final float speedModifier;
    @Nullable
    private BlockPos targetBed;
    private int remainingTimeToReachBed;
    private int remainingJumps;
    private int remainingCooldownUntilNextJump;
    
    public JumpOnBed(final float float1) {
        super((Map)ImmutableMap.of(MemoryModuleType.NEAREST_BED, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.speedModifier = float1;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Mob aqk) {
        return aqk.isBaby() && this.nearBed(aag, aqk);
    }
    
    @Override
    protected void start(final ServerLevel aag, final Mob aqk, final long long3) {
        super.start(aag, aqk, long3);
        this.getNearestBed(aqk).ifPresent(fx -> {
            this.targetBed = fx;
            this.remainingTimeToReachBed = 100;
            this.remainingJumps = 3 + aag.random.nextInt(4);
            this.remainingCooldownUntilNextJump = 0;
            this.startWalkingTowardsBed(aqk, fx);
        });
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Mob aqk, final long long3) {
        super.stop(aag, aqk, long3);
        this.targetBed = null;
        this.remainingTimeToReachBed = 0;
        this.remainingJumps = 0;
        this.remainingCooldownUntilNextJump = 0;
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Mob aqk, final long long3) {
        return aqk.isBaby() && this.targetBed != null && this.isBed(aag, this.targetBed) && !this.tiredOfWalking(aag, aqk) && !this.tiredOfJumping(aag, aqk);
    }
    
    @Override
    protected boolean timedOut(final long long1) {
        return false;
    }
    
    @Override
    protected void tick(final ServerLevel aag, final Mob aqk, final long long3) {
        if (!this.onOrOverBed(aag, aqk)) {
            --this.remainingTimeToReachBed;
            return;
        }
        if (this.remainingCooldownUntilNextJump > 0) {
            --this.remainingCooldownUntilNextJump;
            return;
        }
        if (this.onBedSurface(aag, aqk)) {
            aqk.getJumpControl().jump();
            --this.remainingJumps;
            this.remainingCooldownUntilNextJump = 5;
        }
    }
    
    private void startWalkingTowardsBed(final Mob aqk, final BlockPos fx) {
        aqk.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(fx, this.speedModifier, 0));
    }
    
    private boolean nearBed(final ServerLevel aag, final Mob aqk) {
        return this.onOrOverBed(aag, aqk) || this.getNearestBed(aqk).isPresent();
    }
    
    private boolean onOrOverBed(final ServerLevel aag, final Mob aqk) {
        final BlockPos fx4 = aqk.blockPosition();
        final BlockPos fx5 = fx4.below();
        return this.isBed(aag, fx4) || this.isBed(aag, fx5);
    }
    
    private boolean onBedSurface(final ServerLevel aag, final Mob aqk) {
        return this.isBed(aag, aqk.blockPosition());
    }
    
    private boolean isBed(final ServerLevel aag, final BlockPos fx) {
        return aag.getBlockState(fx).is(BlockTags.BEDS);
    }
    
    private Optional<BlockPos> getNearestBed(final Mob aqk) {
        return aqk.getBrain().<BlockPos>getMemory(MemoryModuleType.NEAREST_BED);
    }
    
    private boolean tiredOfWalking(final ServerLevel aag, final Mob aqk) {
        return !this.onOrOverBed(aag, aqk) && this.remainingTimeToReachBed <= 0;
    }
    
    private boolean tiredOfJumping(final ServerLevel aag, final Mob aqk) {
        return this.onOrOverBed(aag, aqk) && this.remainingJumps <= 0;
    }
}
