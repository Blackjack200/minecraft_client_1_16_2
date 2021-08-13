package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.Brain;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;

public class SetEntityLookTarget extends Behavior<LivingEntity> {
    private final Predicate<LivingEntity> predicate;
    private final float maxDistSqr;
    
    public SetEntityLookTarget(final MobCategory aql, final float float2) {
        this((Predicate<LivingEntity>)(aqj -> aql.equals(aqj.getType().getCategory())), float2);
    }
    
    public SetEntityLookTarget(final EntityType<?> aqb, final float float2) {
        this((Predicate<LivingEntity>)(aqj -> aqb.equals(aqj.getType())), float2);
    }
    
    public SetEntityLookTarget(final float float1) {
        this((Predicate<LivingEntity>)(aqj -> true), float1);
    }
    
    public SetEntityLookTarget(final Predicate<LivingEntity> predicate, final float float2) {
        super((Map)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
        this.predicate = predicate;
        this.maxDistSqr = float2 * float2;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final LivingEntity aqj) {
        return ((List)aqj.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get()).stream().anyMatch((Predicate)this.predicate);
    }
    
    @Override
    protected void start(final ServerLevel aag, final LivingEntity aqj, final long long3) {
        final Brain<?> arc6 = aqj.getBrain();
        arc6.<List<LivingEntity>>getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).ifPresent(list -> list.stream().filter((Predicate)this.predicate).filter(aqj2 -> aqj2.distanceToSqr(aqj) <= this.maxDistSqr).findFirst().ifPresent(aqj -> arc6.<EntityTracker>setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(aqj, true))));
    }
}
