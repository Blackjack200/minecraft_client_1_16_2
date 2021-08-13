package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;

public class RunIf<E extends LivingEntity> extends Behavior<E> {
    private final Predicate<E> predicate;
    private final Behavior<? super E> wrappedBehavior;
    private final boolean checkWhileRunningAlso;
    
    public RunIf(final Map<MemoryModuleType<?>, MemoryStatus> map, final Predicate<E> predicate, final Behavior<? super E> ars, final boolean boolean4) {
        super(mergeMaps(map, ars.entryCondition));
        this.predicate = predicate;
        this.wrappedBehavior = ars;
        this.checkWhileRunningAlso = boolean4;
    }
    
    private static Map<MemoryModuleType<?>, MemoryStatus> mergeMaps(final Map<MemoryModuleType<?>, MemoryStatus> map1, final Map<MemoryModuleType<?>, MemoryStatus> map2) {
        final Map<MemoryModuleType<?>, MemoryStatus> map3 = (Map<MemoryModuleType<?>, MemoryStatus>)Maps.newHashMap();
        map3.putAll((Map)map1);
        map3.putAll((Map)map2);
        return map3;
    }
    
    public RunIf(final Predicate<E> predicate, final Behavior<? super E> ars) {
        this((Map)ImmutableMap.of(), predicate, ars, false);
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E aqj) {
        return this.predicate.test(aqj) && this.wrappedBehavior.checkExtraStartConditions(aag, aqj);
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final E aqj, final long long3) {
        return this.checkWhileRunningAlso && this.predicate.test(aqj) && this.wrappedBehavior.canStillUse(aag, aqj, long3);
    }
    
    @Override
    protected boolean timedOut(final long long1) {
        return false;
    }
    
    @Override
    protected void start(final ServerLevel aag, final E aqj, final long long3) {
        this.wrappedBehavior.start(aag, aqj, long3);
    }
    
    @Override
    protected void tick(final ServerLevel aag, final E aqj, final long long3) {
        this.wrappedBehavior.tick(aag, aqj, long3);
    }
    
    @Override
    protected void stop(final ServerLevel aag, final E aqj, final long long3) {
        this.wrappedBehavior.stop(aag, aqj, long3);
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append("RunIf: ").append(this.wrappedBehavior).toString();
    }
}
