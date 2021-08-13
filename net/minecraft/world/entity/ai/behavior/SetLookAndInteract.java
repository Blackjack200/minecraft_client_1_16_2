package net.minecraft.world.entity.ai.behavior;

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

public class SetLookAndInteract extends Behavior<LivingEntity> {
    private final EntityType<?> type;
    private final int interactionRangeSqr;
    private final Predicate<LivingEntity> targetFilter;
    private final Predicate<LivingEntity> selfFilter;
    
    public SetLookAndInteract(final EntityType<?> aqb, final int integer, final Predicate<LivingEntity> predicate3, final Predicate<LivingEntity> predicate4) {
        super((Map)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
        this.type = aqb;
        this.interactionRangeSqr = integer * integer;
        this.targetFilter = predicate4;
        this.selfFilter = predicate3;
    }
    
    public SetLookAndInteract(final EntityType<?> aqb, final int integer) {
        this(aqb, integer, (Predicate<LivingEntity>)(aqj -> true), (Predicate<LivingEntity>)(aqj -> true));
    }
    
    public boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        return this.selfFilter.test(aqj) && this.getVisibleEntities(aqj).stream().anyMatch(this::isMatchingTarget);
    }
    
    public void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        super.start(aag, aqj, long3);
        final Brain<?> arc6 = aqj.getBrain();
        arc6.<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).ifPresent(list -> list.stream().filter(aqj2 -> aqj2.distanceToSqr(aqj) <= this.interactionRangeSqr).filter(this::isMatchingTarget).findFirst().ifPresent(aqj -> {
            arc6.<LivingEntity>setMemory(MemoryModuleType.INTERACTION_TARGET, aqj);
            arc6.<EntityTracker>setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(aqj, true));
        }));
    }
    
    private boolean isMatchingTarget(final LivingEntity aqj) {
        return this.type.equals(aqj.getType()) && this.targetFilter.test(aqj);
    }
    
    private List<LivingEntity> getVisibleEntities(final LivingEntity aqj) {
        return (List<LivingEntity>)aqj.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get();
    }
}
