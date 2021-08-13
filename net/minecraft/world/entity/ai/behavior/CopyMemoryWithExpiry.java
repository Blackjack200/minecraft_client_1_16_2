package net.minecraft.world.entity.ai.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.util.IntRange;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.function.Predicate;
import net.minecraft.world.entity.Mob;

public class CopyMemoryWithExpiry<E extends Mob, T> extends Behavior<E> {
    private final Predicate<E> predicate;
    private final MemoryModuleType<? extends T> sourceMemory;
    private final MemoryModuleType<T> targetMemory;
    private final IntRange durationOfCopy;
    
    public CopyMemoryWithExpiry(final Predicate<E> predicate, final MemoryModuleType<? extends T> aya2, final MemoryModuleType<T> aya3, final IntRange afe) {
        super((Map)ImmutableMap.of(aya2, MemoryStatus.VALUE_PRESENT, aya3, MemoryStatus.VALUE_ABSENT));
        this.predicate = predicate;
        this.sourceMemory = aya2;
        this.targetMemory = aya3;
        this.durationOfCopy = afe;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E aqk) {
        return this.predicate.test(aqk);
    }
    
    @Override
    protected void start(final ServerLevel aag, final E aqk, final long long3) {
        final Brain<?> arc6 = aqk.getBrain();
        arc6.setMemoryWithExpiry((MemoryModuleType<Object>)this.targetMemory, arc6.getMemory(this.sourceMemory).get(), this.durationOfCopy.randomValue(aag.random));
    }
}
