package net.minecraft.world.entity.ai.control;

import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;

public class MoveControl {
    protected final Mob mob;
    protected double wantedX;
    protected double wantedY;
    protected double wantedZ;
    protected double speedModifier;
    protected float strafeForwards;
    protected float strafeRight;
    protected Operation operation;
    
    public MoveControl(final Mob aqk) {
        this.operation = Operation.WAIT;
        this.mob = aqk;
    }
    
    public boolean hasWanted() {
        return this.operation == Operation.MOVE_TO;
    }
    
    public double getSpeedModifier() {
        return this.speedModifier;
    }
    
    public void setWantedPosition(final double double1, final double double2, final double double3, final double double4) {
        this.wantedX = double1;
        this.wantedY = double2;
        this.wantedZ = double3;
        this.speedModifier = double4;
        if (this.operation != Operation.JUMPING) {
            this.operation = Operation.MOVE_TO;
        }
    }
    
    public void strafe(final float float1, final float float2) {
        this.operation = Operation.STRAFE;
        this.strafeForwards = float1;
        this.strafeRight = float2;
        this.speedModifier = 0.25;
    }
    
    public void tick() {
        if (this.operation == Operation.STRAFE) {
            final float float2 = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
            final float float3 = (float)this.speedModifier * float2;
            float float4 = this.strafeForwards;
            float float5 = this.strafeRight;
            float float6 = Mth.sqrt(float4 * float4 + float5 * float5);
            if (float6 < 1.0f) {
                float6 = 1.0f;
            }
            float6 = float3 / float6;
            float4 *= float6;
            float5 *= float6;
            final float float7 = Mth.sin(this.mob.yRot * 0.017453292f);
            final float float8 = Mth.cos(this.mob.yRot * 0.017453292f);
            final float float9 = float4 * float8 - float5 * float7;
            final float float10 = float5 * float8 + float4 * float7;
            if (!this.isWalkable(float9, float10)) {
                this.strafeForwards = 1.0f;
                this.strafeRight = 0.0f;
            }
            this.mob.setSpeed(float3);
            this.mob.setZza(this.strafeForwards);
            this.mob.setXxa(this.strafeRight);
            this.operation = Operation.WAIT;
        }
        else if (this.operation == Operation.MOVE_TO) {
            this.operation = Operation.WAIT;
            final double double2 = this.wantedX - this.mob.getX();
            final double double3 = this.wantedZ - this.mob.getZ();
            final double double4 = this.wantedY - this.mob.getY();
            final double double5 = double2 * double2 + double4 * double4 + double3 * double3;
            if (double5 < 2.500000277905201E-7) {
                this.mob.setZza(0.0f);
                return;
            }
            final float float10 = (float)(Mth.atan2(double3, double2) * 57.2957763671875) - 90.0f;
            this.mob.yRot = this.rotlerp(this.mob.yRot, float10, 90.0f);
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            final BlockPos fx11 = this.mob.blockPosition();
            final BlockState cee12 = this.mob.level.getBlockState(fx11);
            final Block bul13 = cee12.getBlock();
            final VoxelShape dde14 = cee12.getCollisionShape(this.mob.level, fx11);
            if ((double4 > this.mob.maxUpStep && double2 * double2 + double3 * double3 < Math.max(1.0f, this.mob.getBbWidth())) || (!dde14.isEmpty() && this.mob.getY() < dde14.max(Direction.Axis.Y) + fx11.getY() && !bul13.is(BlockTags.DOORS) && !bul13.is(BlockTags.FENCES))) {
                this.mob.getJumpControl().jump();
                this.operation = Operation.JUMPING;
            }
        }
        else if (this.operation == Operation.JUMPING) {
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            if (this.mob.isOnGround()) {
                this.operation = Operation.WAIT;
            }
        }
        else {
            this.mob.setZza(0.0f);
        }
    }
    
    private boolean isWalkable(final float float1, final float float2) {
        final PathNavigation ayg4 = this.mob.getNavigation();
        if (ayg4 != null) {
            final NodeEvaluator cwz5 = ayg4.getNodeEvaluator();
            if (cwz5 != null && cwz5.getBlockPathType(this.mob.level, Mth.floor(this.mob.getX() + float1), Mth.floor(this.mob.getY()), Mth.floor(this.mob.getZ() + float2)) != BlockPathTypes.WALKABLE) {
                return false;
            }
        }
        return true;
    }
    
    protected float rotlerp(final float float1, final float float2, final float float3) {
        float float4 = Mth.wrapDegrees(float2 - float1);
        if (float4 > float3) {
            float4 = float3;
        }
        if (float4 < -float3) {
            float4 = -float3;
        }
        float float5 = float1 + float4;
        if (float5 < 0.0f) {
            float5 += 360.0f;
        }
        else if (float5 > 360.0f) {
            float5 -= 360.0f;
        }
        return float5;
    }
    
    public double getWantedX() {
        return this.wantedX;
    }
    
    public double getWantedY() {
        return this.wantedY;
    }
    
    public double getWantedZ() {
        return this.wantedZ;
    }
    
    public enum Operation {
        WAIT, 
        MOVE_TO, 
        STRAFE, 
        JUMPING;
    }
}
