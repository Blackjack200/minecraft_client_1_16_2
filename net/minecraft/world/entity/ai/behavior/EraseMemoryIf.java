package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;

public class EraseMemoryIf<E extends LivingEntity> extends Behavior<E> {
    private final Predicate<E> predicate;
    private final MemoryModuleType<?> memoryType;
    
    public EraseMemoryIf(final Predicate<E> predicate, final MemoryModuleType<?> aya) {
        super((Map)ImmutableMap.of(aya, MemoryStatus.VALUE_PRESENT));
        this.predicate = predicate;
        this.memoryType = aya;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E aqj) {
        return this.predicate.test(aqj);
    }
    
    @Override
    protected void start(final ServerLevel aag, final E aqj, final long long3) {
        aqj.getBrain().eraseMemory(this.memoryType);
    }
}
