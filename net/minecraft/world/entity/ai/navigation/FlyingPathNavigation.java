package net.minecraft.world.entity.ai.navigation;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.util.Mth;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;

public class FlyingPathNavigation extends PathNavigation {
    public FlyingPathNavigation(final Mob aqk, final Level bru) {
        super(aqk, bru);
    }
    
    @Override
    protected PathFinder createPathFinder(final int integer) {
        (this.nodeEvaluator = new FlyNodeEvaluator()).setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, integer);
    }
    
    @Override
    protected boolean canUpdatePath() {
        return (this.canFloat() && this.isInLiquid()) || !this.mob.isPassenger();
    }
    
    @Override
    protected Vec3 getTempMobPos() {
        return this.mob.position();
    }
    
    @Override
    public Path createPath(final Entity apx, final int integer) {
        return this.createPath(apx.blockPosition(), integer);
    }
    
    @Override
    public void tick() {
        ++this.tick;
        if (this.hasDelayedRecomputation) {
            this.recomputePath();
        }
        if (this.isDone()) {
            return;
        }
        if (this.canUpdatePath()) {
            this.followThePath();
        }
        else if (this.path != null && !this.path.isDone()) {
            final Vec3 dck2 = this.path.getNextEntityPos(this.mob);
            if (Mth.floor(this.mob.getX()) == Mth.floor(dck2.x) && Mth.floor(this.mob.getY()) == Mth.floor(dck2.y) && Mth.floor(this.mob.getZ()) == Mth.floor(dck2.z)) {
                this.path.advance();
            }
        }
        DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
        if (this.isDone()) {
            return;
        }
        final Vec3 dck2 = this.path.getNextEntityPos(this.mob);
        this.mob.getMoveControl().setWantedPosition(dck2.x, dck2.y, dck2.z, this.speedModifier);
    }
    
    @Override
    protected boolean canMoveDirectly(final Vec3 dck1, final Vec3 dck2, final int integer3, final int integer4, final int integer5) {
        int integer6 = Mth.floor(dck1.x);
        int integer7 = Mth.floor(dck1.y);
        int integer8 = Mth.floor(dck1.z);
        double double10 = dck2.x - dck1.x;
        double double11 = dck2.y - dck1.y;
        double double12 = dck2.z - dck1.z;
        final double double13 = double10 * double10 + double11 * double11 + double12 * double12;
        if (double13 < 1.0E-8) {
            return false;
        }
        final double double14 = 1.0 / Math.sqrt(double13);
        double10 *= double14;
        double11 *= double14;
        double12 *= double14;
        final double double15 = 1.0 / Math.abs(double10);
        final double double16 = 1.0 / Math.abs(double11);
        final double double17 = 1.0 / Math.abs(double12);
        double double18 = integer6 - dck1.x;
        double double19 = integer7 - dck1.y;
        double double20 = integer8 - dck1.z;
        if (double10 >= 0.0) {
            ++double18;
        }
        if (double11 >= 0.0) {
            ++double19;
        }
        if (double12 >= 0.0) {
            ++double20;
        }
        double18 /= double10;
        double19 /= double11;
        double20 /= double12;
        final int integer9 = (double10 < 0.0) ? -1 : 1;
        final int integer10 = (double11 < 0.0) ? -1 : 1;
        final int integer11 = (double12 < 0.0) ? -1 : 1;
        final int integer12 = Mth.floor(dck2.x);
        final int integer13 = Mth.floor(dck2.y);
        final int integer14 = Mth.floor(dck2.z);
        int integer15 = integer12 - integer6;
        int integer16 = integer13 - integer7;
        int integer17 = integer14 - integer8;
        while (integer15 * integer9 > 0 || integer16 * integer10 > 0 || integer17 * integer11 > 0) {
            if (double18 < double20 && double18 <= double19) {
                double18 += double15;
                integer6 += integer9;
                integer15 = integer12 - integer6;
            }
            else if (double19 < double18 && double19 <= double20) {
                double19 += double16;
                integer7 += integer10;
                integer16 = integer13 - integer7;
            }
            else {
                double20 += double17;
                integer8 += integer11;
                integer17 = integer14 - integer8;
            }
        }
        return true;
    }
    
    public void setCanOpenDoors(final boolean boolean1) {
        this.nodeEvaluator.setCanOpenDoors(boolean1);
    }
    
    public void setCanPassDoors(final boolean boolean1) {
        this.nodeEvaluator.setCanPassDoors(boolean1);
    }
    
    @Override
    public boolean isStableDestination(final BlockPos fx) {
        return this.level.getBlockState(fx).entityCanStandOn(this.level, fx, this.mob);
    }
}
