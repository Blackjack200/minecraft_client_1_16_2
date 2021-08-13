package net.minecraft.world.level.block;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SmokerBlock extends AbstractFurnaceBlock {
    protected SmokerBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new SmokerBlockEntity();
    }
    
    @Override
    protected void openContainer(final Level bru, final BlockPos fx, final Player bft) {
        final BlockEntity ccg5 = bru.getBlockEntity(fx);
        if (ccg5 instanceof SmokerBlockEntity) {
            bft.openMenu((MenuProvider)ccg5);
            bft.awardStat(Stats.INTERACT_WITH_SMOKER);
        }
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (!cee.<Boolean>getValue((Property<Boolean>)SmokerBlock.LIT)) {
            return;
        }
        final double double6 = fx.getX() + 0.5;
        final double double7 = fx.getY();
        final double double8 = fx.getZ() + 0.5;
        if (random.nextDouble() < 0.1) {
            bru.playLocalSound(double6, double7, double8, SoundEvents.SMOKER_SMOKE, SoundSource.BLOCKS, 1.0f, 1.0f, false);
        }
        bru.addParticle(ParticleTypes.SMOKE, double6, double7 + 1.1, double8, 0.0, 0.0, 0.0);
    }
}
