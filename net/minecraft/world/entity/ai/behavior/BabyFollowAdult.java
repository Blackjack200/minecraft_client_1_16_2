package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.util.IntRange;
import net.minecraft.world.entity.AgableMob;

public class BabyFollowAdult<E extends AgableMob> extends Behavior<E> {
    private final IntRange followRange;
    private final float speedModifier;
    
    public BabyFollowAdult(final IntRange afe, final float float2) {
        super((Map)ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
        this.followRange = afe;
        this.speedModifier = float2;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E apv) {
        if (!apv.isBaby()) {
            return false;
        }
        final AgableMob apv2 = this.getNearestAdult(apv);
        return apv.closerThan(apv2, this.followRange.getMaxInclusive() + 1) && !apv.closerThan(apv2, this.followRange.getMinInclusive());
    }
    
    @Override
    protected void start(final ServerLevel aag, final E apv, final long long3) {
        BehaviorUtils.setWalkAndLookTargetMemories(apv, this.getNearestAdult(apv), this.speedModifier, this.followRange.getMinInclusive() - 1);
    }
    
    private AgableMob getNearestAdult(final E apv) {
        return (AgableMob)apv.getBrain().<AgableMob>getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT).get();
    }
}
