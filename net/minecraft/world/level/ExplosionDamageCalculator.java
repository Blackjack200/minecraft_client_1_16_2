package net.minecraft.world.level;

import java.util.Optional;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;

public class ExplosionDamageCalculator {
    public Optional<Float> getBlockExplosionResistance(final Explosion brm, final BlockGetter bqz, final BlockPos fx, final BlockState cee, final FluidState cuu) {
        if (cee.isAir() && cuu.isEmpty()) {
            return (Optional<Float>)Optional.empty();
        }
        return (Optional<Float>)Optional.of(Math.max(cee.getBlock().getExplosionResistance(), cuu.getExplosionResistance()));
    }
    
    public boolean shouldBlockExplode(final Explosion brm, final BlockGetter bqz, final BlockPos fx, final BlockState cee, final float float5) {
        return true;
    }
}
