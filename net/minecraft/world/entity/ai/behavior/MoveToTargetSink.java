package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import java.util.Optional;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.Mob;

public class MoveToTargetSink extends Behavior<Mob> {
    private int remainingCooldown;
    @Nullable
    private Path path;
    @Nullable
    private BlockPos lastTargetPos;
    private float speedModifier;
    
    public MoveToTargetSink() {
        this(150, 250);
    }
    
    public MoveToTargetSink(final int integer1, final int integer2) {
        super((Map)ImmutableMap.of(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED, MemoryModuleType.PATH, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_PRESENT), integer1, integer2);
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Mob aqk) {
        if (this.remainingCooldown > 0) {
            --this.remainingCooldown;
            return false;
        }
        final Brain<?> arc4 = aqk.getBrain();
        final WalkTarget ayc5 = (WalkTarget)arc4.<WalkTarget>getMemory(MemoryModuleType.WALK_TARGET).get();
        final boolean boolean6 = this.reachedTarget(aqk, ayc5);
        if (!boolean6 && this.tryComputePath(aqk, ayc5, aag.getGameTime())) {
            this.lastTargetPos = ayc5.getTarget().currentBlockPosition();
            return true;
        }
        arc4.<WalkTarget>eraseMemory(MemoryModuleType.WALK_TARGET);
        if (boolean6) {
            arc4.<Long>eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        }
        return false;
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Mob aqk, final long long3) {
        if (this.path == null || this.lastTargetPos == null) {
            return false;
        }
        final Optional<WalkTarget> optional6 = aqk.getBrain().<WalkTarget>getMemory(MemoryModuleType.WALK_TARGET);
        final PathNavigation ayg7 = aqk.getNavigation();
        return !ayg7.isDone() && optional6.isPresent() && !this.reachedTarget(aqk, (WalkTarget)optional6.get());
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Mob aqk, final long long3) {
        if (aqk.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET) && !this.reachedTarget(aqk, (WalkTarget)aqk.getBrain().<WalkTarget>getMemory(MemoryModuleType.WALK_TARGET).get())) {
            this.remainingCooldown = aag.getRandom().nextInt(40);
        }
        aqk.getNavigation().stop();
        aqk.getBrain().<WalkTarget>eraseMemory(MemoryModuleType.WALK_TARGET);
        aqk.getBrain().<Path>eraseMemory(MemoryModuleType.PATH);
        this.path = null;
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Mob aqk, final long long3) {
        aqk.getBrain().<Path>setMemory(MemoryModuleType.PATH, this.path);
        aqk.getNavigation().moveTo(this.path, this.speedModifier);
    }
    
    @Override
    protected void tick(final ServerLevel aag, final Mob aqk, final long long3) {
        final Path cxa6 = aqk.getNavigation().getPath();
        final Brain<?> arc7 = aqk.getBrain();
        if (this.path != cxa6) {
            this.path = cxa6;
            arc7.<Path>setMemory(MemoryModuleType.PATH, cxa6);
        }
        if (cxa6 == null || this.lastTargetPos == null) {
            return;
        }
        final WalkTarget ayc8 = (WalkTarget)arc7.<WalkTarget>getMemory(MemoryModuleType.WALK_TARGET).get();
        if (ayc8.getTarget().currentBlockPosition().distSqr(this.lastTargetPos) > 4.0 && this.tryComputePath(aqk, ayc8, aag.getGameTime())) {
            this.lastTargetPos = ayc8.getTarget().currentBlockPosition();
            this.stop(aag, aqk, long3);
        }
    }
    
    private boolean tryComputePath(final Mob aqk, final WalkTarget ayc, final long long3) {
        final BlockPos fx6 = ayc.getTarget().currentBlockPosition();
        this.path = aqk.getNavigation().createPath(fx6, 0);
        this.speedModifier = ayc.getSpeedModifier();
        final Brain<?> arc7 = aqk.getBrain();
        if (this.reachedTarget(aqk, ayc)) {
            arc7.<Long>eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        }
        else {
            final boolean boolean8 = this.path != null && this.path.canReach();
            if (boolean8) {
                arc7.<Long>eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            }
            else if (!arc7.hasMemoryValue(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)) {
                arc7.<Long>setMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, long3);
            }
            if (this.path != null) {
                return true;
            }
            final Vec3 dck9 = RandomPos.getPosTowards((PathfinderMob)aqk, 10, 7, Vec3.atBottomCenterOf(fx6));
            if (dck9 != null) {
                this.path = aqk.getNavigation().createPath(dck9.x, dck9.y, dck9.z, 0);
                return this.path != null;
            }
        }
        return false;
    }
    
    private boolean reachedTarget(final Mob aqk, final WalkTarget ayc) {
        return ayc.getTarget().currentBlockPosition().distManhattan(aqk.blockPosition()) <= ayc.getCloseEnoughDist();
    }
}
