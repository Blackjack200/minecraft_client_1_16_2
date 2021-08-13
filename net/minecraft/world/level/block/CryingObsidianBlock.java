package net.minecraft.world.level.block;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class CryingObsidianBlock extends Block {
    public CryingObsidianBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (random.nextInt(5) != 0) {
            return;
        }
        final Direction gc6 = Direction.getRandom(random);
        if (gc6 == Direction.UP) {
            return;
        }
        final BlockPos fx2 = fx.relative(gc6);
        final BlockState cee2 = bru.getBlockState(fx2);
        if (cee.canOcclude() && cee2.isFaceSturdy(bru, fx2, gc6.getOpposite())) {
            return;
        }
        final double double9 = (gc6.getStepX() == 0) ? random.nextDouble() : (0.5 + gc6.getStepX() * 0.6);
        final double double10 = (gc6.getStepY() == 0) ? random.nextDouble() : (0.5 + gc6.getStepY() * 0.6);
        final double double11 = (gc6.getStepZ() == 0) ? random.nextDouble() : (0.5 + gc6.getStepZ() * 0.6);
        bru.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, fx.getX() + double9, fx.getY() + double10, fx.getZ() + double11, 0.0, 0.0, 0.0);
    }
}
