package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import net.minecraft.util.IntRange;
import net.minecraft.world.entity.LivingEntity;

public class RunSometimes<E extends LivingEntity> extends Behavior<E> {
    private boolean resetTicks;
    private boolean wasRunning;
    private final IntRange interval;
    private final Behavior<? super E> wrappedBehavior;
    private int ticksUntilNextStart;
    
    public RunSometimes(final Behavior<? super E> ars, final IntRange afe) {
        this(ars, false, afe);
    }
    
    public RunSometimes(final Behavior<? super E> ars, final boolean boolean2, final IntRange afe) {
        super(ars.entryCondition);
        this.wrappedBehavior = ars;
        this.resetTicks = !boolean2;
        this.interval = afe;
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final E aqj) {
        if (!this.wrappedBehavior.checkExtraStartConditions(aag, aqj)) {
            return false;
        }
        if (this.resetTicks) {
            this.resetTicksUntilNextStart(aag);
            this.resetTicks = false;
        }
        if (this.ticksUntilNextStart > 0) {
            --this.ticksUntilNextStart;
        }
        return !this.wasRunning && this.ticksUntilNextStart == 0;
    }
    
    @Override
    protected void start(final ServerLevel aag, final E aqj, final long long3) {
        this.wrappedBehavior.start(aag, aqj, long3);
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final E aqj, final long long3) {
        return this.wrappedBehavior.canStillUse(aag, aqj, long3);
    }
    
    @Override
    protected void tick(final ServerLevel aag, final E aqj, final long long3) {
        this.wrappedBehavior.tick(aag, aqj, long3);
        this.wasRunning = (this.wrappedBehavior.getStatus() == Status.RUNNING);
    }
    
    @Override
    protected void stop(final ServerLevel aag, final E aqj, final long long3) {
        this.resetTicksUntilNextStart(aag);
        this.wrappedBehavior.stop(aag, aqj, long3);
    }
    
    private void resetTicksUntilNextStart(final ServerLevel aag) {
        this.ticksUntilNextStart = this.interval.randomValue(aag.random);
    }
    
    @Override
    protected boolean timedOut(final long long1) {
        return false;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append("RunSometimes: ").append(this.wrappedBehavior).toString();
    }
}
