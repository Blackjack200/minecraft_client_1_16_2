package net.minecraft.world.entity.ai.goal;

import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;
import net.minecraft.world.entity.PathfinderMob;

public class RandomStrollGoal extends Goal {
    protected final PathfinderMob mob;
    protected double wantedX;
    protected double wantedY;
    protected double wantedZ;
    protected final double speedModifier;
    protected int interval;
    protected boolean forceTrigger;
    private boolean checkNoActionTime;
    
    public RandomStrollGoal(final PathfinderMob aqr, final double double2) {
        this(aqr, double2, 120);
    }
    
    public RandomStrollGoal(final PathfinderMob aqr, final double double2, final int integer) {
        this(aqr, double2, integer, true);
    }
    
    public RandomStrollGoal(final PathfinderMob aqr, final double double2, final int integer, final boolean boolean4) {
        this.mob = aqr;
        this.speedModifier = double2;
        this.interval = integer;
        this.checkNoActionTime = boolean4;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
    }
    
    @Override
    public boolean canUse() {
        if (this.mob.isVehicle()) {
            return false;
        }
        if (!this.forceTrigger) {
            if (this.checkNoActionTime && this.mob.getNoActionTime() >= 100) {
                return false;
            }
            if (this.mob.getRandom().nextInt(this.interval) != 0) {
                return false;
            }
        }
        final Vec3 dck2 = this.getPosition();
        if (dck2 == null) {
            return false;
        }
        this.wantedX = dck2.x;
        this.wantedY = dck2.y;
        this.wantedZ = dck2.z;
        this.forceTrigger = false;
        return true;
    }
    
    @Nullable
    protected Vec3 getPosition() {
        return RandomPos.getPos(this.mob, 10, 7);
    }
    
    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone() && !this.mob.isVehicle();
    }
    
    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }
    
    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        super.stop();
    }
    
    public void trigger() {
        this.forceTrigger = true;
    }
    
    public void setInterval(final int integer) {
        this.interval = integer;
    }
}
