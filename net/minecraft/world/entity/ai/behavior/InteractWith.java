package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.Brain;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public class InteractWith<E extends LivingEntity, T extends LivingEntity> extends Behavior<E> {
    private final int maxDist;
    private final float speedModifier;
    private final EntityType<? extends T> type;
    private final int interactionRangeSqr;
    private final Predicate<T> targetFilter;
    private final Predicate<E> selfFilter;
    private final MemoryModuleType<T> memory;
    
    public InteractWith(final EntityType<? extends T> aqb, final int integer2, final Predicate<E> predicate3, final Predicate<T> predicate4, final MemoryModuleType<T> aya, final float float6, final int integer7) {
        super((Map)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
        this.type = aqb;
        this.speedModifier = float6;
        this.interactionRangeSqr = integer2 * integer2;
        this.maxDist = integer7;
        this.targetFilter = predicate4;
        this.selfFilter = predicate3;
        this.memory = aya;
    }
    
    public static <T extends LivingEntity> InteractWith<LivingEntity, T> of(final EntityType<? extends T> aqb, final int integer2, final MemoryModuleType<T> aya, final float float4, final int integer5) {
        return new InteractWith<LivingEntity, T>(aqb, integer2, (java.util.function.Predicate<LivingEntity>)(aqj -> true), (java.util.function.Predicate<T>)(aqj -> true), aya, float4, integer5);
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E aqj) {
        return this.selfFilter.test(aqj) && this.seesAtLeastOneValidTarget(aqj);
    }
    
    private boolean seesAtLeastOneValidTarget(final E aqj) {
        final List<LivingEntity> list3 = (List<LivingEntity>)aqj.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get();
        return list3.stream().anyMatch(this::isTargetValid);
    }
    
    private boolean isTargetValid(final LivingEntity aqj) {
        return this.type.equals(aqj.getType()) && this.targetFilter.test(aqj);
    }
    
    @Override
    protected void start(final ServerLevel aag, final E aqj, final long long3) {
        final Brain<?> arc6 = aqj.getBrain();
        arc6.<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).ifPresent(list -> list.stream().filter(aqj -> this.type.equals(aqj.getType())).map(aqj -> aqj).filter(aqj2 -> aqj2.distanceToSqr(aqj) <= this.interactionRangeSqr).filter((Predicate)this.targetFilter).findFirst().ifPresent(aqj -> {
            arc6.<LivingEntity>setMemory(this.memory, aqj);
            arc6.<EntityTracker>setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(aqj, true));
            arc6.<WalkTarget>setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(aqj, false), this.speedModifier, this.maxDist));
        }));
    }
}
