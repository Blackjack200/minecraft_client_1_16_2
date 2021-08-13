package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;

public class GoToWantedItem<E extends LivingEntity> extends Behavior<E> {
    private final Predicate<E> predicate;
    private final int maxDistToWalk;
    private final float speedModifier;
    
    public GoToWantedItem(final float float1, final boolean boolean2, final int integer) {
        this(aqj -> true, float1, boolean2, integer);
    }
    
    public GoToWantedItem(final Predicate<E> predicate, final float float2, final boolean boolean3, final int integer) {
        super((Map)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, (boolean3 ? MemoryStatus.REGISTERED : MemoryStatus.VALUE_ABSENT), MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.VALUE_PRESENT));
        this.predicate = predicate;
        this.maxDistToWalk = integer;
        this.speedModifier = float2;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E aqj) {
        return this.predicate.test(aqj) && this.getClosestLovedItem(aqj).closerThan(aqj, this.maxDistToWalk);
    }
    
    @Override
    protected void start(final ServerLevel aag, final E aqj, final long long3) {
        BehaviorUtils.setWalkAndLookTargetMemories(aqj, this.getClosestLovedItem(aqj), this.speedModifier, 0);
    }
    
    private ItemEntity getClosestLovedItem(final E aqj) {
        return (ItemEntity)aqj.getBrain().<ItemEntity>getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM).get();
    }
}
