package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import java.util.EnumSet;
import net.minecraft.world.entity.PathfinderMob;

public class MoveTowardsRestrictionGoal extends Goal {
    private final PathfinderMob mob;
    private double wantedX;
    private double wantedY;
    private double wantedZ;
    private final double speedModifier;
    
    public MoveTowardsRestrictionGoal(final PathfinderMob aqr, final double double2) {
        this.mob = aqr;
        this.speedModifier = double2;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.MOVE));
    }
    
    @Override
    public boolean canUse() {
        if (this.mob.isWithinRestriction()) {
            return false;
        }
        final Vec3 dck2 = RandomPos.getPosTowards(this.mob, 16, 7, Vec3.atBottomCenterOf(this.mob.getRestrictCenter()));
        if (dck2 == null) {
            return false;
        }
        this.wantedX = dck2.x;
        this.wantedY = dck2.y;
        this.wantedZ = dck2.z;
        return true;
    }
    
    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }
    
    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
    }
}
