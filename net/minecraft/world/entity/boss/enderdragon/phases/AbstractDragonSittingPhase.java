package net.minecraft.world.entity.boss.enderdragon.phases;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;

public abstract class AbstractDragonSittingPhase extends AbstractDragonPhaseInstance {
    public AbstractDragonSittingPhase(final EnderDragon bbo) {
        super(bbo);
    }
    
    @Override
    public boolean isSitting() {
        return true;
    }
    
    @Override
    public float onHurt(final DamageSource aph, final float float2) {
        if (aph.getDirectEntity() instanceof AbstractArrow) {
            aph.getDirectEntity().setSecondsOnFire(1);
            return 0.0f;
        }
        return super.onHurt(aph, float2);
    }
}
