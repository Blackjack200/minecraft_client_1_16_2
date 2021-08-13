package net.minecraft.world.entity.ai.goal;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class WrappedGoal extends Goal {
    private final Goal goal;
    private final int priority;
    private boolean isRunning;
    
    public WrappedGoal(final int integer, final Goal avs) {
        this.priority = integer;
        this.goal = avs;
    }
    
    public boolean canBeReplacedBy(final WrappedGoal axi) {
        return this.isInterruptable() && axi.getPriority() < this.getPriority();
    }
    
    @Override
    public boolean canUse() {
        return this.goal.canUse();
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.goal.canContinueToUse();
    }
    
    @Override
    public boolean isInterruptable() {
        return this.goal.isInterruptable();
    }
    
    @Override
    public void start() {
        if (this.isRunning) {
            return;
        }
        this.isRunning = true;
        this.goal.start();
    }
    
    @Override
    public void stop() {
        if (!this.isRunning) {
            return;
        }
        this.isRunning = false;
        this.goal.stop();
    }
    
    @Override
    public void tick() {
        this.goal.tick();
    }
    
    @Override
    public void setFlags(final EnumSet<Flag> enumSet) {
        this.goal.setFlags(enumSet);
    }
    
    @Override
    public EnumSet<Flag> getFlags() {
        return this.goal.getFlags();
    }
    
    public boolean isRunning() {
        return this.isRunning;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public Goal getGoal() {
        return this.goal;
    }
    
    public boolean equals(@Nullable final Object object) {
        return this == object || (object != null && this.getClass() == object.getClass() && this.goal.equals(((WrappedGoal)object).goal));
    }
    
    public int hashCode() {
        return this.goal.hashCode();
    }
}
