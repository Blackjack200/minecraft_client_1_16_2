package net.minecraft.world.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface ItemSteerable {
    boolean boost();
    
    void travelWithInput(final Vec3 dck);
    
    float getSteeringSpeed();
    
    default boolean travel(final Mob aqk, final ItemBasedSteering aqg, final Vec3 dck) {
        if (!aqk.isAlive()) {
            return false;
        }
        final Entity apx5 = aqk.getPassengers().isEmpty() ? null : ((Entity)aqk.getPassengers().get(0));
        if (!aqk.isVehicle() || !aqk.canBeControlledByRider() || !(apx5 instanceof Player)) {
            aqk.maxUpStep = 0.5f;
            aqk.flyingSpeed = 0.02f;
            this.travelWithInput(dck);
            return false;
        }
        aqk.yRot = apx5.yRot;
        aqk.yRotO = aqk.yRot;
        aqk.xRot = apx5.xRot * 0.5f;
        aqk.setRot(aqk.yRot, aqk.xRot);
        aqk.yBodyRot = aqk.yRot;
        aqk.yHeadRot = aqk.yRot;
        aqk.maxUpStep = 1.0f;
        aqk.flyingSpeed = aqk.getSpeed() * 0.1f;
        if (aqg.boosting && aqg.boostTime++ > aqg.boostTimeTotal) {
            aqg.boosting = false;
        }
        if (aqk.isControlledByLocalInstance()) {
            float float6 = this.getSteeringSpeed();
            if (aqg.boosting) {
                float6 += float6 * 1.15f * Mth.sin(aqg.boostTime / (float)aqg.boostTimeTotal * 3.1415927f);
            }
            aqk.setSpeed(float6);
            this.travelWithInput(new Vec3(0.0, 0.0, 1.0));
            aqk.lerpSteps = 0;
        }
        else {
            aqk.calculateEntityAnimation(aqk, false);
            aqk.setDeltaMovement(Vec3.ZERO);
        }
        return true;
    }
}
