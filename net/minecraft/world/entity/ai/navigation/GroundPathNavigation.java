package net.minecraft.world.entity.ai.navigation;

import java.util.Iterator;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;

public class GroundPathNavigation extends PathNavigation {
    private boolean avoidSun;
    
    public GroundPathNavigation(final Mob aqk, final Level bru) {
        super(aqk, bru);
    }
    
    @Override
    protected PathFinder createPathFinder(final int integer) {
        (this.nodeEvaluator = new WalkNodeEvaluator()).setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, integer);
    }
    
    @Override
    protected boolean canUpdatePath() {
        return this.mob.isOnGround() || this.isInLiquid() || this.mob.isPassenger();
    }
    
    @Override
    protected Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), this.getSurfaceY(), this.mob.getZ());
    }
    
    @Override
    public Path createPath(BlockPos fx, final int integer) {
        if (this.level.getBlockState(fx).isAir()) {
            BlockPos fx2;
            for (fx2 = fx.below(); fx2.getY() > 0 && this.level.getBlockState(fx2).isAir(); fx2 = fx2.below()) {}
            if (fx2.getY() > 0) {
                return super.createPath(fx2.above(), integer);
            }
            while (fx2.getY() < this.level.getMaxBuildHeight() && this.level.getBlockState(fx2).isAir()) {
                fx2 = fx2.above();
            }
            fx = fx2;
        }
        if (this.level.getBlockState(fx).getMaterial().isSolid()) {
            BlockPos fx2;
            for (fx2 = fx.above(); fx2.getY() < this.level.getMaxBuildHeight() && this.level.getBlockState(fx2).getMaterial().isSolid(); fx2 = fx2.above()) {}
            return super.createPath(fx2, integer);
        }
        return super.createPath(fx, integer);
    }
    
    @Override
    public Path createPath(final Entity apx, final int integer) {
        return this.createPath(apx.blockPosition(), integer);
    }
    
    private int getSurfaceY() {
        if (!this.mob.isInWater() || !this.canFloat()) {
            return Mth.floor(this.mob.getY() + 0.5);
        }
        int integer2 = Mth.floor(this.mob.getY());
        Block bul3 = this.level.getBlockState(new BlockPos(this.mob.getX(), integer2, this.mob.getZ())).getBlock();
        int integer3 = 0;
        while (bul3 == Blocks.WATER) {
            ++integer2;
            bul3 = this.level.getBlockState(new BlockPos(this.mob.getX(), integer2, this.mob.getZ())).getBlock();
            if (++integer3 > 16) {
                return Mth.floor(this.mob.getY());
            }
        }
        return integer2;
    }
    
    @Override
    protected void trimPath() {
        super.trimPath();
        if (this.avoidSun) {
            if (this.level.canSeeSky(new BlockPos(this.mob.getX(), this.mob.getY() + 0.5, this.mob.getZ()))) {
                return;
            }
            for (int integer2 = 0; integer2 < this.path.getNodeCount(); ++integer2) {
                final Node cwy3 = this.path.getNode(integer2);
                if (this.level.canSeeSky(new BlockPos(cwy3.x, cwy3.y, cwy3.z))) {
                    this.path.truncateNodes(integer2);
                    return;
                }
            }
        }
    }
    
    @Override
    protected boolean canMoveDirectly(final Vec3 dck1, final Vec3 dck2, int integer3, final int integer4, int integer5) {
        int integer6 = Mth.floor(dck1.x);
        int integer7 = Mth.floor(dck1.z);
        double double9 = dck2.x - dck1.x;
        double double10 = dck2.z - dck1.z;
        final double double11 = double9 * double9 + double10 * double10;
        if (double11 < 1.0E-8) {
            return false;
        }
        final double double12 = 1.0 / Math.sqrt(double11);
        double9 *= double12;
        double10 *= double12;
        integer3 += 2;
        integer5 += 2;
        if (!this.canWalkOn(integer6, Mth.floor(dck1.y), integer7, integer3, integer4, integer5, dck1, double9, double10)) {
            return false;
        }
        integer3 -= 2;
        integer5 -= 2;
        final double double13 = 1.0 / Math.abs(double9);
        final double double14 = 1.0 / Math.abs(double10);
        double double15 = integer6 - dck1.x;
        double double16 = integer7 - dck1.z;
        if (double9 >= 0.0) {
            ++double15;
        }
        if (double10 >= 0.0) {
            ++double16;
        }
        double15 /= double9;
        double16 /= double10;
        final int integer8 = (double9 < 0.0) ? -1 : 1;
        final int integer9 = (double10 < 0.0) ? -1 : 1;
        final int integer10 = Mth.floor(dck2.x);
        final int integer11 = Mth.floor(dck2.z);
        int integer12 = integer10 - integer6;
        int integer13 = integer11 - integer7;
        while (integer12 * integer8 > 0 || integer13 * integer9 > 0) {
            if (double15 < double16) {
                double15 += double13;
                integer6 += integer8;
                integer12 = integer10 - integer6;
            }
            else {
                double16 += double14;
                integer7 += integer9;
                integer13 = integer11 - integer7;
            }
            if (!this.canWalkOn(integer6, Mth.floor(dck1.y), integer7, integer3, integer4, integer5, dck1, double9, double10)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean canWalkOn(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final Vec3 dck, final double double8, final double double9) {
        final int integer7 = integer1 - integer4 / 2;
        final int integer8 = integer3 - integer6 / 2;
        if (!this.canWalkAbove(integer7, integer2, integer8, integer4, integer5, integer6, dck, double8, double9)) {
            return false;
        }
        for (int integer9 = integer7; integer9 < integer7 + integer4; ++integer9) {
            for (int integer10 = integer8; integer10 < integer8 + integer6; ++integer10) {
                final double double10 = integer9 + 0.5 - dck.x;
                final double double11 = integer10 + 0.5 - dck.z;
                if (double10 * double8 + double11 * double9 >= 0.0) {
                    BlockPathTypes cww21 = this.nodeEvaluator.getBlockPathType(this.level, integer9, integer2 - 1, integer10, this.mob, integer4, integer5, integer6, true, true);
                    if (!this.hasValidPathType(cww21)) {
                        return false;
                    }
                    cww21 = this.nodeEvaluator.getBlockPathType(this.level, integer9, integer2, integer10, this.mob, integer4, integer5, integer6, true, true);
                    final float float22 = this.mob.getPathfindingMalus(cww21);
                    if (float22 < 0.0f || float22 >= 8.0f) {
                        return false;
                    }
                    if (cww21 == BlockPathTypes.DAMAGE_FIRE || cww21 == BlockPathTypes.DANGER_FIRE || cww21 == BlockPathTypes.DAMAGE_OTHER) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    protected boolean hasValidPathType(final BlockPathTypes cww) {
        return cww != BlockPathTypes.WATER && cww != BlockPathTypes.LAVA && cww != BlockPathTypes.OPEN;
    }
    
    private boolean canWalkAbove(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final Vec3 dck, final double double8, final double double9) {
        for (final BlockPos fx14 : BlockPos.betweenClosed(new BlockPos(integer1, integer2, integer3), new BlockPos(integer1 + integer4 - 1, integer2 + integer5 - 1, integer3 + integer6 - 1))) {
            final double double10 = fx14.getX() + 0.5 - dck.x;
            final double double11 = fx14.getZ() + 0.5 - dck.z;
            if (double10 * double8 + double11 * double9 < 0.0) {
                continue;
            }
            if (!this.level.getBlockState(fx14).isPathfindable(this.level, fx14, PathComputationType.LAND)) {
                return false;
            }
        }
        return true;
    }
    
    public void setCanOpenDoors(final boolean boolean1) {
        this.nodeEvaluator.setCanOpenDoors(boolean1);
    }
    
    public boolean canOpenDoors() {
        return this.nodeEvaluator.canPassDoors();
    }
    
    public void setAvoidSun(final boolean boolean1) {
        this.avoidSun = boolean1;
    }
}
