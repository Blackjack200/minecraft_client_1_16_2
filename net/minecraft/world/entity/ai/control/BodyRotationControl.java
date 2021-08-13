package net.minecraft.world.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;

public class BodyRotationControl {
    private final Mob mob;
    private int headStableTime;
    private float lastStableYHeadRot;
    
    public BodyRotationControl(final Mob aqk) {
        this.mob = aqk;
    }
    
    public void clientTick() {
        if (this.isMoving()) {
            this.mob.yBodyRot = this.mob.yRot;
            this.rotateHeadIfNecessary();
            this.lastStableYHeadRot = this.mob.yHeadRot;
            this.headStableTime = 0;
            return;
        }
        if (this.notCarryingMobPassengers()) {
            if (Math.abs(this.mob.yHeadRot - this.lastStableYHeadRot) > 15.0f) {
                this.headStableTime = 0;
                this.lastStableYHeadRot = this.mob.yHeadRot;
                this.rotateBodyIfNecessary();
            }
            else {
                ++this.headStableTime;
                if (this.headStableTime > 10) {
                    this.rotateHeadTowardsFront();
                }
            }
        }
    }
    
    private void rotateBodyIfNecessary() {
        this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, (float)this.mob.getMaxHeadYRot());
    }
    
    private void rotateHeadIfNecessary() {
        this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, (float)this.mob.getMaxHeadYRot());
    }
    
    private void rotateHeadTowardsFront() {
        final int integer2 = this.headStableTime - 10;
        final float float3 = Mth.clamp(integer2 / 10.0f, 0.0f, 1.0f);
        final float float4 = this.mob.getMaxHeadYRot() * (1.0f - float3);
        this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, float4);
    }
    
    private boolean notCarryingMobPassengers() {
        return this.mob.getPassengers().isEmpty() || !(this.mob.getPassengers().get(0) instanceof Mob);
    }
    
    private boolean isMoving() {
        final double double2 = this.mob.getX() - this.mob.xo;
        final double double3 = this.mob.getZ() - this.mob.zo;
        return double2 * double2 + double3 * double3 > 2.500000277905201E-7;
    }
}
