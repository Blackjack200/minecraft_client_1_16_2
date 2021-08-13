package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.phys.AABB;
import java.util.stream.Collectors;
import java.util.List;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Comparator;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class NearestLivingEntitySensor extends Sensor<LivingEntity> {
    @Override
    protected void doTick(final ServerLevel aag, final LivingEntity aqj) {
        final AABB dcf4 = aqj.getBoundingBox().inflate(16.0, 16.0, 16.0);
        final List<LivingEntity> list5 = aag.<LivingEntity>getEntitiesOfClass((java.lang.Class<? extends LivingEntity>)LivingEntity.class, dcf4, (java.util.function.Predicate<? super LivingEntity>)(aqj2 -> aqj2 != aqj && aqj2.isAlive()));
        list5.sort(Comparator.comparingDouble(aqj::distanceToSqr));
        final Brain<?> arc6 = aqj.getBrain();
        arc6.<List<LivingEntity>>setMemory(MemoryModuleType.LIVING_ENTITIES, list5);
        arc6.setMemory((MemoryModuleType<Object>)MemoryModuleType.VISIBLE_LIVING_ENTITIES, list5.stream().filter(aqj2 -> Sensor.isEntityTargetable(aqj, aqj2)).collect(Collectors.toList()));
    }
    
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES);
    }
}
