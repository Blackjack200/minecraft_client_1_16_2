package net.minecraft.world.entity.ai.behavior;

import java.util.Iterator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Map;
import net.minecraft.world.entity.LivingEntity;

public abstract class Behavior<E extends LivingEntity> {
    protected final Map<MemoryModuleType<?>, MemoryStatus> entryCondition;
    private Status status;
    private long endTimestamp;
    private final int minDuration;
    private final int maxDuration;
    
    public Behavior(final Map<MemoryModuleType<?>, MemoryStatus> map) {
        this(map, 60);
    }
    
    public Behavior(final Map<MemoryModuleType<?>, MemoryStatus> map, final int integer) {
        this(map, integer, integer);
    }
    
    public Behavior(final Map<MemoryModuleType<?>, MemoryStatus> map, final int integer2, final int integer3) {
        this.status = Status.STOPPED;
        this.minDuration = integer2;
        this.maxDuration = integer3;
        this.entryCondition = map;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    public final boolean tryStart(final ServerLevel aag, final E aqj, final long long3) {
        if (this.hasRequiredMemories(aqj) && this.checkExtraStartConditions(aag, aqj)) {
            this.status = Status.RUNNING;
            final int integer6 = this.minDuration + aag.getRandom().nextInt(this.maxDuration + 1 - this.minDuration);
            this.endTimestamp = long3 + integer6;
            this.start(aag, aqj, long3);
            return true;
        }
        return false;
    }
    
    protected void start(final ServerLevel aag, final E aqj, final long long3) {
    }
    
    public final void tickOrStop(final ServerLevel aag, final E aqj, final long long3) {
        if (!this.timedOut(long3) && this.canStillUse(aag, aqj, long3)) {
            this.tick(aag, aqj, long3);
        }
        else {
            this.doStop(aag, aqj, long3);
        }
    }
    
    protected void tick(final ServerLevel aag, final E aqj, final long long3) {
    }
    
    public final void doStop(final ServerLevel aag, final E aqj, final long long3) {
        this.status = Status.STOPPED;
        this.stop(aag, aqj, long3);
    }
    
    protected void stop(final ServerLevel aag, final E aqj, final long long3) {
    }
    
    protected boolean canStillUse(final ServerLevel aag, final E aqj, final long long3) {
        return false;
    }
    
    protected boolean timedOut(final long long1) {
        return long1 > this.endTimestamp;
    }
    
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E aqj) {
        return true;
    }
    
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
    private boolean hasRequiredMemories(final E aqj) {
        for (final Map.Entry<MemoryModuleType<?>, MemoryStatus> entry4 : this.entryCondition.entrySet()) {
            final MemoryModuleType<?> aya5 = entry4.getKey();
            final MemoryStatus ayb6 = (MemoryStatus)entry4.getValue();
            if (!aqj.getBrain().checkMemory(aya5, ayb6)) {
                return false;
            }
        }
        return true;
    }
    
    public enum Status {
        STOPPED, 
        RUNNING;
    }
}
