package net.minecraft.world.level.block;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import java.util.Random;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WetSpongeBlock extends Block {
    protected WetSpongeBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (bru.dimensionType().ultraWarm()) {
            bru.setBlock(fx, Blocks.SPONGE.defaultBlockState(), 3);
            bru.levelEvent(2009, fx, 0);
            bru.playSound(null, fx, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, (1.0f + bru.getRandom().nextFloat() * 0.2f) * 0.7f);
        }
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        final Direction gc6 = Direction.getRandom(random);
        if (gc6 == Direction.UP) {
            return;
        }
        final BlockPos fx2 = fx.relative(gc6);
        final BlockState cee2 = bru.getBlockState(fx2);
        if (cee.canOcclude() && cee2.isFaceSturdy(bru, fx2, gc6.getOpposite())) {
            return;
        }
        double double9 = fx.getX();
        double double10 = fx.getY();
        double double11 = fx.getZ();
        if (gc6 == Direction.DOWN) {
            double10 -= 0.05;
            double9 += random.nextDouble();
            double11 += random.nextDouble();
        }
        else {
            double10 += random.nextDouble() * 0.8;
            if (gc6.getAxis() == Direction.Axis.X) {
                double11 += random.nextDouble();
                if (gc6 == Direction.EAST) {
                    double9 += 1.1;
                }
                else {
                    double9 += 0.05;
                }
            }
            else {
                double9 += random.nextDouble();
                if (gc6 == Direction.SOUTH) {
                    double11 += 1.1;
                }
                else {
                    double11 += 0.05;
                }
            }
        }
        bru.addParticle(ParticleTypes.DRIPPING_WATER, double9, double10, double11, 0.0, 0.0, 0.0);
    }
}
