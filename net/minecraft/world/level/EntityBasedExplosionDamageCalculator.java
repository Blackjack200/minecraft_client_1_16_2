package net.minecraft.world.level;

import java.util.Optional;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

public class EntityBasedExplosionDamageCalculator extends ExplosionDamageCalculator {
    private final Entity source;
    
    public EntityBasedExplosionDamageCalculator(final Entity apx) {
        this.source = apx;
    }
    
    @Override
    public Optional<Float> getBlockExplosionResistance(final Explosion brm, final BlockGetter bqz, final BlockPos fx, final BlockState cee, final FluidState cuu) {
        return (Optional<Float>)super.getBlockExplosionResistance(brm, bqz, fx, cee, cuu).map(float6 -> this.source.getBlockExplosionResistance(brm, bqz, fx, cee, cuu, float6));
    }
    
    @Override
    public boolean shouldBlockExplode(final Explosion brm, final BlockGetter bqz, final BlockPos fx, final BlockState cee, final float float5) {
        return this.source.shouldBlockExplode(brm, bqz, fx, cee, float5);
    }
}
