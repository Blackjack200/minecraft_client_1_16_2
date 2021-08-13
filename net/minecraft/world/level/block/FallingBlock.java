package net.minecraft.world.level.block;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.material.Material;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FallingBlock extends Block {
    public FallingBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        bru.getBlockTicks().scheduleTick(fx, this, this.getDelayAfterPlace());
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        brv.getBlockTicks().scheduleTick(fx5, this, this.getDelayAfterPlace());
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public void tick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (!isFree(aag.getBlockState(fx.below())) || fx.getY() < 0) {
            return;
        }
        final FallingBlockEntity bcr6 = new FallingBlockEntity(aag, fx.getX() + 0.5, fx.getY(), fx.getZ() + 0.5, aag.getBlockState(fx));
        this.falling(bcr6);
        aag.addFreshEntity(bcr6);
    }
    
    protected void falling(final FallingBlockEntity bcr) {
    }
    
    protected int getDelayAfterPlace() {
        return 2;
    }
    
    public static boolean isFree(final BlockState cee) {
        final Material cux2 = cee.getMaterial();
        return cee.isAir() || cee.is(BlockTags.FIRE) || cux2.isLiquid() || cux2.isReplaceable();
    }
    
    public void onLand(final Level bru, final BlockPos fx, final BlockState cee3, final BlockState cee4, final FallingBlockEntity bcr) {
    }
    
    public void onBroken(final Level bru, final BlockPos fx, final FallingBlockEntity bcr) {
    }
    
    @Override
    public void animateTick(final BlockState cee, final Level bru, final BlockPos fx, final Random random) {
        if (random.nextInt(16) == 0) {
            final BlockPos fx2 = fx.below();
            if (isFree(bru.getBlockState(fx2))) {
                final double double7 = fx.getX() + random.nextDouble();
                final double double8 = fx.getY() - 0.05;
                final double double9 = fx.getZ() + random.nextDouble();
                bru.addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, cee), double7, double8, double9, 0.0, 0.0, 0.0);
            }
        }
    }
    
    public int getDustColor(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return -16777216;
    }
}
