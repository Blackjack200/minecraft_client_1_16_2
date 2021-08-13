package net.minecraft.world.entity.ai.goal;

import java.util.Collection;
import java.util.EnumSet;

public abstract class Goal {
    private final EnumSet<Flag> flags;
    
    public Goal() {
        this.flags = (EnumSet<Flag>)EnumSet.noneOf((Class)Flag.class);
    }
    
    public abstract boolean canUse();
    
    public boolean canContinueToUse() {
        return this.canUse();
    }
    
    public boolean isInterruptable() {
        return true;
    }
    
    public void start() {
    }
    
    public void stop() {
    }
    
    public void tick() {
    }
    
    public void setFlags(final EnumSet<Flag> enumSet) {
        this.flags.clear();
        this.flags.addAll((Collection)enumSet);
    }
    
    public String toString() {
        return this.getClass().getSimpleName();
    }
    
    public EnumSet<Flag> getFlags() {
        return this.flags;
    }
    
    public enum Flag {
        MOVE, 
        LOOK, 
        JUMP, 
        TARGET;
    }
}
