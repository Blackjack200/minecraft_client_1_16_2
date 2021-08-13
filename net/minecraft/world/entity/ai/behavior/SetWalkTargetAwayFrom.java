package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import java.util.function.Function;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.PathfinderMob;

public class SetWalkTargetAwayFrom<T> extends Behavior<PathfinderMob> {
    private final MemoryModuleType<T> walkAwayFromMemory;
    private final float speedModifier;
    private final int desiredDistance;
    private final Function<T, Vec3> toPosition;
    
    public SetWalkTargetAwayFrom(final MemoryModuleType<T> aya, final float float2, final int integer, final boolean boolean4, final Function<T, Vec3> function) {
        super((Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, (boolean4 ? MemoryStatus.REGISTERED : MemoryStatus.VALUE_ABSENT), aya, MemoryStatus.VALUE_PRESENT));
        this.walkAwayFromMemory = aya;
        this.speedModifier = float2;
        this.desiredDistance = integer;
        this.toPosition = function;
    }
    
    public static SetWalkTargetAwayFrom<BlockPos> pos(final MemoryModuleType<BlockPos> aya, final float float2, final int integer, final boolean boolean4) {
        return new SetWalkTargetAwayFrom<BlockPos>(aya, float2, integer, boolean4, (java.util.function.Function<BlockPos, Vec3>)Vec3::atBottomCenterOf);
    }
    
    public static SetWalkTargetAwayFrom<? extends Entity> entity(final MemoryModuleType<? extends Entity> aya, final float float2, final int integer, final boolean boolean4) {
        return new SetWalkTargetAwayFrom<Entity>(aya, float2, integer, boolean4, (java.util.function.Function<? extends Entity, Vec3>)Entity::position);
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final PathfinderMob aqr) {
        return !this.alreadyWalkingAwayFromPosWithSameSpeed(aqr) && aqr.position().closerThan(this.getPosToAvoid(aqr), this.desiredDistance);
    }
    
    private Vec3 getPosToAvoid(final PathfinderMob aqr) {
        return (Vec3)this.toPosition.apply(aqr.getBrain().<T>getMemory(this.walkAwayFromMemory).get());
    }
    
    private boolean alreadyWalkingAwayFromPosWithSameSpeed(final PathfinderMob aqr) {
        if (!aqr.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET)) {
            return false;
        }
        final WalkTarget ayc3 = (WalkTarget)aqr.getBrain().<WalkTarget>getMemory(MemoryModuleType.WALK_TARGET).get();
        if (ayc3.getSpeedModifier() != this.speedModifier) {
            return false;
        }
        final Vec3 dck4 = ayc3.getTarget().currentPosition().subtract(aqr.position());
        final Vec3 dck5 = this.getPosToAvoid(aqr).subtract(aqr.position());
        return dck4.dot(dck5) < 0.0;
    }
    
    @Override
    protected void start(final ServerLevel aag, final PathfinderMob aqr, final long long3) {
        moveAwayFrom(aqr, this.getPosToAvoid(aqr), this.speedModifier);
    }
    
    private static void moveAwayFrom(final PathfinderMob aqr, final Vec3 dck, final float float3) {
        for (int integer4 = 0; integer4 < 10; ++integer4) {
            final Vec3 dck2 = RandomPos.getLandPosAvoid(aqr, 16, 7, dck);
            if (dck2 != null) {
                aqr.getBrain().<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(dck2, float3, 0));
                return;
            }
        }
    }
}
