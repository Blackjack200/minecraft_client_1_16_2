package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class DummySensor extends Sensor<LivingEntity> {
    @Override
    protected void doTick(final ServerLevel aag, final LivingEntity aqj) {
    }
    
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of();
    }
}
