package net.minecraft.world.level.block;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FurnaceBlock extends AbstractFurnaceBlock {
    protected FurnaceBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new FurnaceBlockEntity();
    }
    
    @Override
    protected void openContainer(final Level bru, final BlockPos fx, final Player bft) {
        final BlockEntity ccg5 = bru.getBlockEntity(fx);
        if (ccg5 instanceof FurnaceBlockEntity) {
            bft.openMenu((MenuProvider)ccg5);
            bft.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (!cee.<Boolean>getValue((Property<Boolean>)FurnaceBlock.LIT)) {
            return;
        }
        final double double6 = fx.getX() + 0.5;
        final double double7 = fx.getY();
        final double double8 = fx.getZ() + 0.5;
        if (random.nextDouble() < 0.1) {
            bru.playLocalSound(double6, double7, double8, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0f, 1.0f, false);
        }
        final Direction gc12 = cee.<Direction>getValue((Property<Direction>)FurnaceBlock.FACING);
        final Direction.Axis a13 = gc12.getAxis();
        final double double9 = 0.52;
        final double double10 = random.nextDouble() * 0.6 - 0.3;
        final double double11 = (a13 == Direction.Axis.X) ? (gc12.getStepX() * 0.52) : double10;
        final double double12 = random.nextDouble() * 6.0 / 16.0;
        final double double13 = (a13 == Direction.Axis.Z) ? (gc12.getStepZ() * 0.52) : double10;
        bru.addParticle(ParticleTypes.SMOKE, double6 + double11, double7 + double12, double8 + double13, 0.0, 0.0, 0.0);
        bru.addParticle(ParticleTypes.FLAME, double6 + double11, double7 + double12, double8 + double13, 0.0, 0.0, 0.0);
    }
}
