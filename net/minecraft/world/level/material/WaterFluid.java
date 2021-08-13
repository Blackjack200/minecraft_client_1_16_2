package net.minecraft.world.level.material;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;

public abstract class WaterFluid extends FlowingFluid {
    @Override
    public Fluid getFlowing() {
        return Fluids.FLOWING_WATER;
    }
    
    @Override
    public Fluid getSource() {
        return Fluids.WATER;
    }
    
    @Override
    public Item getBucket() {
        return Items.WATER_BUCKET;
    }
    
    public void animateTick(final Level bru, final BlockPos fx, final FluidState cuu, final Random random) {
        if (!cuu.isSource() && !cuu.<Boolean>getValue((Property<Boolean>)WaterFluid.FALLING)) {
            if (random.nextInt(64) == 0) {
                bru.playLocalSound(fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25f + 0.75f, random.nextFloat() + 0.5f, false);
            }
        }
        else if (random.nextInt(10) == 0) {
            bru.addParticle(ParticleTypes.UNDERWATER, fx.getX() + random.nextDouble(), fx.getY() + random.nextDouble(), fx.getZ() + random.nextDouble(), 0.0, 0.0, 0.0);
        }
    }
    
    @Nullable
    public ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }
    
    @Override
    protected boolean canConvertToSource() {
        return true;
    }
    
    @Override
    protected void beforeDestroyingBlock(final LevelAccessor brv, final BlockPos fx, final BlockState cee) {
        final BlockEntity ccg5 = cee.getBlock().isEntityBlock() ? brv.getBlockEntity(fx) : null;
        Block.dropResources(cee, brv, fx, ccg5);
    }
    
    public int getSlopeFindDistance(final LevelReader brw) {
        return 4;
    }
    
    public BlockState createLegacyBlock(final FluidState cuu) {
        return ((StateHolder<O, BlockState>)Blocks.WATER.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)LiquidBlock.LEVEL, FlowingFluid.getLegacyLevel(cuu));
    }
    
    @Override
    public boolean isSame(final Fluid cut) {
        return cut == Fluids.WATER || cut == Fluids.FLOWING_WATER;
    }
    
    public int getDropOff(final LevelReader brw) {
        return 1;
    }
    
    @Override
    public int getTickDelay(final LevelReader brw) {
        return 5;
    }
    
    public boolean canBeReplacedWith(final FluidState cuu, final BlockGetter bqz, final BlockPos fx, final Fluid cut, final Direction gc) {
        return gc == Direction.DOWN && !cut.is(FluidTags.WATER);
    }
    
    @Override
    protected float getExplosionResistance() {
        return 100.0f;
    }
    
    public static class Source extends WaterFluid {
        @Override
        public int getAmount(final FluidState cuu) {
            return 8;
        }
        
        @Override
        public boolean isSource(final FluidState cuu) {
            return true;
        }
    }
    
    public static class Flowing extends WaterFluid {
        @Override
        protected void createFluidStateDefinition(final StateDefinition.Builder<Fluid, FluidState> a) {
            super.createFluidStateDefinition(a);
            a.add(Flowing.LEVEL);
        }
        
        @Override
        public int getAmount(final FluidState cuu) {
            return cuu.<Integer>getValue((Property<Integer>)Flowing.LEVEL);
        }
        
        @Override
        public boolean isSource(final FluidState cuu) {
            return false;
        }
    }
}
