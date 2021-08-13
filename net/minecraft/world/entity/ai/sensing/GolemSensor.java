package net.minecraft.world.entity.ai.sensing;

import net.minecraft.world.entity.EntityType;
import java.util.Optional;
import java.util.List;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class GolemSensor extends Sensor<LivingEntity> {
    public GolemSensor() {
        this(200);
    }
    
    public GolemSensor(final int integer) {
        super(integer);
    }
    
    @Override
    protected void doTick(final ServerLevel aag, final LivingEntity aqj) {
        checkForNearbyGolem(aqj);
    }
    
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.LIVING_ENTITIES);
    }
    
    public static void checkForNearbyGolem(final LivingEntity aqj) {
        final Optional<List<LivingEntity>> optional2 = aqj.getBrain().<List<LivingEntity>>getMemory(MemoryModuleType.LIVING_ENTITIES);
        if (!optional2.isPresent()) {
            return;
        }
        final boolean boolean3 = ((List)optional2.get()).stream().anyMatch(aqj -> aqj.getType().equals(EntityType.IRON_GOLEM));
        if (boolean3) {
            golemDetected(aqj);
        }
    }
    
    public static void golemDetected(final LivingEntity aqj) {
        aqj.getBrain().<Boolean>setMemoryWithExpiry(MemoryModuleType.GOLEM_DETECTED_RECENTLY, true, 600L);
    }
}
