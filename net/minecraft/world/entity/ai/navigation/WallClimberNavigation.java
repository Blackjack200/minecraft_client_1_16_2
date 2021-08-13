package net.minecraft.world.entity.ai.navigation;

import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;
import net.minecraft.core.BlockPos;

public class WallClimberNavigation extends GroundPathNavigation {
    private BlockPos pathToPosition;
    
    public WallClimberNavigation(final Mob aqk, final Level bru) {
        super(aqk, bru);
    }
    
    @Override
    public Path createPath(final BlockPos fx, final int integer) {
        this.pathToPosition = fx;
        return super.createPath(fx, integer);
    }
    
    @Override
    public Path createPath(final Entity apx, final int integer) {
        this.pathToPosition = apx.blockPosition();
        return super.createPath(apx, integer);
    }
    
    @Override
    public boolean moveTo(final Entity apx, final double double2) {
        final Path cxa5 = this.createPath(apx, 0);
        if (cxa5 != null) {
            return this.moveTo(cxa5, double2);
        }
        this.pathToPosition = apx.blockPosition();
        this.speedModifier = double2;
        return true;
    }
    
    @Override
    public void tick() {
        if (this.isDone()) {
            if (this.pathToPosition != null) {
                if (this.pathToPosition.closerThan(this.mob.position(), this.mob.getBbWidth()) || (this.mob.getY() > this.pathToPosition.getY() && new BlockPos(this.pathToPosition.getX(), this.mob.getY(), this.pathToPosition.getZ()).closerThan(this.mob.position(), this.mob.getBbWidth()))) {
                    this.pathToPosition = null;
                }
                else {
                    this.mob.getMoveControl().setWantedPosition(this.pathToPosition.getX(), this.pathToPosition.getY(), this.pathToPosition.getZ(), this.speedModifier);
                }
            }
            return;
        }
        super.tick();
    }
}
