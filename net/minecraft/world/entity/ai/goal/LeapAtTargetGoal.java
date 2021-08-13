package net.minecraft.world.entity.ai.goal;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class LeapAtTargetGoal extends Goal {
    private final Mob mob;
    private LivingEntity target;
    private final float yd;
    
    public LeapAtTargetGoal(final Mob aqk, final float float2) {
        this.mob = aqk;
        this.yd = float2;
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.JUMP, (Enum)Flag.MOVE));
    }
    
    @Override
    public boolean canUse() {
        if (this.mob.isVehicle()) {
            return false;
        }
        this.target = this.mob.getTarget();
        if (this.target == null) {
            return false;
        }
        final double double2 = this.mob.distanceToSqr(this.target);
        return double2 >= 4.0 && double2 <= 16.0 && this.mob.isOnGround() && this.mob.getRandom().nextInt(5) == 0;
    }
    
    @Override
    public boolean canContinueToUse() {
        return !this.mob.isOnGround();
    }
    
    @Override
    public void start() {
        final Vec3 dck2 = this.mob.getDeltaMovement();
        Vec3 dck3 = new Vec3(this.target.getX() - this.mob.getX(), 0.0, this.target.getZ() - this.mob.getZ());
        if (dck3.lengthSqr() > 1.0E-7) {
            dck3 = dck3.normalize().scale(0.4).add(dck2.scale(0.2));
        }
        this.mob.setDeltaMovement(dck3.x, this.yd, dck3.z);
    }
}
