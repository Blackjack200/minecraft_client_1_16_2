package net.minecraft.world.level.material;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.GameRules;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.BlockGetter;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;

public abstract class LavaFluid extends FlowingFluid {
    @Override
    public Fluid getFlowing() {
        return Fluids.FLOWING_LAVA;
    }
    
    @Override
    public Fluid getSource() {
        return Fluids.LAVA;
    }
    
    @Override
    public Item getBucket() {
        return Items.LAVA_BUCKET;
    }
    
    public void animateTick(final Level bru, final BlockPos fx, final FluidState cuu, final Random random) {
        final BlockPos fx2 = fx.above();
        if (bru.getBlockState(fx2).isAir() && !bru.getBlockState(fx2).isSolidRender(bru, fx2)) {
            if (random.nextInt(100) == 0) {
                final double double7 = fx.getX() + random.nextDouble();
                final double double8 = fx.getY() + 1.0;
                final double double9 = fx.getZ() + random.nextDouble();
                bru.addParticle(ParticleTypes.LAVA, double7, double8, double9, 0.0, 0.0, 0.0);
                bru.playLocalSound(double7, double8, double9, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
            }
            if (random.nextInt(200) == 0) {
                bru.playLocalSound(fx.getX(), fx.getY(), fx.getZ(), SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.15f, false);
            }
        }
    }
    
    public void randomTick(final Level bru, final BlockPos fx, final FluidState cuu, final Random random) {
        if (!bru.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)) {
            return;
        }
        final int integer6 = random.nextInt(3);
        if (integer6 > 0) {
            BlockPos fx2 = fx;
            for (int integer7 = 0; integer7 < integer6; ++integer7) {
                fx2 = fx2.offset(random.nextInt(3) - 1, 1, random.nextInt(3) - 1);
                if (!bru.isLoaded(fx2)) {
                    return;
                }
                final BlockState cee9 = bru.getBlockState(fx2);
                if (cee9.isAir()) {
                    if (this.hasFlammableNeighbours(bru, fx2)) {
                        bru.setBlockAndUpdate(fx2, BaseFireBlock.getState(bru, fx2));
                        return;
                    }
                }
                else if (cee9.getMaterial().blocksMotion()) {
                    return;
                }
            }
        }
        else {
            for (int integer8 = 0; integer8 < 3; ++integer8) {
                final BlockPos fx3 = fx.offset(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);
                if (!bru.isLoaded(fx3)) {
                    return;
                }
                if (bru.isEmptyBlock(fx3.above()) && this.isFlammable(bru, fx3)) {
                    bru.setBlockAndUpdate(fx3.above(), BaseFireBlock.getState(bru, fx3));
                }
            }
        }
    }
    
    private boolean hasFlammableNeighbours(final LevelReader brw, final BlockPos fx) {
        for (final Direction gc7 : Direction.values()) {
            if (this.isFlammable(brw, fx.relative(gc7))) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isFlammable(final LevelReader brw, final BlockPos fx) {
        return (fx.getY() < 0 || fx.getY() >= 256 || brw.hasChunkAt(fx)) && brw.getBlockState(fx).getMaterial().isFlammable();
    }
    
    @Nullable
    public ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_LAVA;
    }
    
    @Override
    protected void beforeDestroyingBlock(final LevelAccessor brv, final BlockPos fx, final BlockState cee) {
        this.fizz(brv, fx);
    }
    
    public int getSlopeFindDistance(final LevelReader brw) {
        return brw.dimensionType().ultraWarm() ? 4 : 2;
    }
    
    public BlockState createLegacyBlock(final FluidState cuu) {
        return ((StateHolder<O, BlockState>)Blocks.LAVA.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)LiquidBlock.LEVEL, FlowingFluid.getLegacyLevel(cuu));
    }
    
    @Override
    public boolean isSame(final Fluid cut) {
        return cut == Fluids.LAVA || cut == Fluids.FLOWING_LAVA;
    }
    
    public int getDropOff(final LevelReader brw) {
        return brw.dimensionType().ultraWarm() ? 1 : 2;
    }
    
    public boolean canBeReplacedWith(final FluidState cuu, final BlockGetter bqz, final BlockPos fx, final Fluid cut, final Direction gc) {
        return cuu.getHeight(bqz, fx) >= 0.44444445f && cut.is(FluidTags.WATER);
    }
    
    @Override
    public int getTickDelay(final LevelReader brw) {
        return brw.dimensionType().ultraWarm() ? 10 : 30;
    }
    
    public int getSpreadDelay(final Level bru, final BlockPos fx, final FluidState cuu3, final FluidState cuu4) {
        int integer6 = this.getTickDelay(bru);
        if (!cuu3.isEmpty() && !cuu4.isEmpty() && !cuu3.<Boolean>getValue((Property<Boolean>)LavaFluid.FALLING) && !cuu4.<Boolean>getValue((Property<Boolean>)LavaFluid.FALLING) && cuu4.getHeight(bru, fx) > cuu3.getHeight(bru, fx) && bru.getRandom().nextInt(4) != 0) {
            integer6 *= 4;
        }
        return integer6;
    }
    
    private void fizz(final LevelAccessor brv, final BlockPos fx) {
        brv.levelEvent(1501, fx, 0);
    }
    
    @Override
    protected boolean canConvertToSource() {
        return false;
    }
    
    @Override
    protected void spreadTo(final LevelAccessor brv, final BlockPos fx, final BlockState cee, final Direction gc, final FluidState cuu) {
        if (gc == Direction.DOWN) {
            final FluidState cuu2 = brv.getFluidState(fx);
            if (this.is(FluidTags.LAVA) && cuu2.is(FluidTags.WATER)) {
                if (cee.getBlock() instanceof LiquidBlock) {
                    brv.setBlock(fx, Blocks.STONE.defaultBlockState(), 3);
                }
                this.fizz(brv, fx);
                return;
            }
        }
        super.spreadTo(brv, fx, cee, gc, cuu);
    }
    
    @Override
    protected boolean isRandomlyTicking() {
        return true;
    }
    
    @Override
    protected float getExplosionResistance() {
        return 100.0f;
    }
    
    public static class Source extends LavaFluid {
        @Override
        public int getAmount(final FluidState cuu) {
            return 8;
        }
        
        @Override
        public boolean isSource(final FluidState cuu) {
            return true;
        }
    }
    
    public static class Flowing extends LavaFluid {
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
