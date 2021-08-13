package net.minecraft.world.entity.vehicle;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class Minecart extends AbstractMinecart {
    public Minecart(final EntityType<?> aqb, final Level bru) {
        super(aqb, bru);
    }
    
    public Minecart(final Level bru, final double double2, final double double3, final double double4) {
        super(EntityType.MINECART, bru, double2, double3, double4);
    }
    
    @Override
    public InteractionResult interact(final Player bft, final InteractionHand aoq) {
        if (bft.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }
        if (this.isVehicle()) {
            return InteractionResult.PASS;
        }
        if (!this.level.isClientSide) {
            return bft.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public void activateMinecart(final int integer1, final int integer2, final int integer3, final boolean boolean4) {
        if (boolean4) {
            if (this.isVehicle()) {
                this.ejectPassengers();
            }
            if (this.getHurtTime() == 0) {
                this.setHurtDir(-this.getHurtDir());
                this.setHurtTime(10);
                this.setDamage(50.0f);
                this.markHurt();
            }
        }
    }
    
    @Override
    public Type getMinecartType() {
        return Type.RIDEABLE;
    }
}
