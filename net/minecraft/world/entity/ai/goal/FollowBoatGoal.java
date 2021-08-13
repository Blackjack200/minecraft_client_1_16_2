package net.minecraft.world.entity.ai.goal;

import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.PathfinderMob;

public class FollowBoatGoal extends Goal {
    private int timeToRecalcPath;
    private final PathfinderMob mob;
    private Player following;
    private BoatGoals currentGoal;
    
    public FollowBoatGoal(final PathfinderMob aqr) {
        this.mob = aqr;
    }
    
    @Override
    public boolean canUse() {
        final List<Boat> list2 = this.mob.level.<Boat>getEntitiesOfClass((java.lang.Class<? extends Boat>)Boat.class, this.mob.getBoundingBox().inflate(5.0));
        boolean boolean3 = false;
        for (final Boat bhk5 : list2) {
            final Entity apx6 = bhk5.getControllingPassenger();
            if (apx6 instanceof Player && (Mth.abs(((Player)apx6).xxa) > 0.0f || Mth.abs(((Player)apx6).zza) > 0.0f)) {
                boolean3 = true;
                break;
            }
        }
        return (this.following != null && (Mth.abs(this.following.xxa) > 0.0f || Mth.abs(this.following.zza) > 0.0f)) || boolean3;
    }
    
    @Override
    public boolean isInterruptable() {
        return true;
    }
    
    @Override
    public boolean canContinueToUse() {
        return this.following != null && this.following.isPassenger() && (Mth.abs(this.following.xxa) > 0.0f || Mth.abs(this.following.zza) > 0.0f);
    }
    
    @Override
    public void start() {
        final List<Boat> list2 = this.mob.level.<Boat>getEntitiesOfClass((java.lang.Class<? extends Boat>)Boat.class, this.mob.getBoundingBox().inflate(5.0));
        for (final Boat bhk4 : list2) {
            if (bhk4.getControllingPassenger() != null && bhk4.getControllingPassenger() instanceof Player) {
                this.following = (Player)bhk4.getControllingPassenger();
                break;
            }
        }
        this.timeToRecalcPath = 0;
        this.currentGoal = BoatGoals.GO_TO_BOAT;
    }
    
    @Override
    public void stop() {
        this.following = null;
    }
    
    @Override
    public void tick() {
        final boolean boolean2 = Mth.abs(this.following.xxa) > 0.0f || Mth.abs(this.following.zza) > 0.0f;
        final float float3 = (this.currentGoal == BoatGoals.GO_IN_BOAT_DIRECTION) ? (boolean2 ? 0.01f : 0.0f) : 0.015f;
        this.mob.moveRelative(float3, new Vec3(this.mob.xxa, this.mob.yya, this.mob.zza));
        this.mob.move(MoverType.SELF, this.mob.getDeltaMovement());
        final int timeToRecalcPath = this.timeToRecalcPath - 1;
        this.timeToRecalcPath = timeToRecalcPath;
        if (timeToRecalcPath > 0) {
            return;
        }
        this.timeToRecalcPath = 10;
        if (this.currentGoal == BoatGoals.GO_TO_BOAT) {
            BlockPos fx4 = this.following.blockPosition().relative(this.following.getDirection().getOpposite());
            fx4 = fx4.offset(0, -1, 0);
            this.mob.getNavigation().moveTo(fx4.getX(), fx4.getY(), fx4.getZ(), 1.0);
            if (this.mob.distanceTo(this.following) < 4.0f) {
                this.timeToRecalcPath = 0;
                this.currentGoal = BoatGoals.GO_IN_BOAT_DIRECTION;
            }
        }
        else if (this.currentGoal == BoatGoals.GO_IN_BOAT_DIRECTION) {
            final Direction gc4 = this.following.getMotionDirection();
            final BlockPos fx5 = this.following.blockPosition().relative(gc4, 10);
            this.mob.getNavigation().moveTo(fx5.getX(), fx5.getY() - 1, fx5.getZ(), 1.0);
            if (this.mob.distanceTo(this.following) > 12.0f) {
                this.timeToRecalcPath = 0;
                this.currentGoal = BoatGoals.GO_TO_BOAT;
            }
        }
    }
}
