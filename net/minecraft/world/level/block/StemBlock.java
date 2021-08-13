package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.core.Direction;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class StemBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty AGE;
    protected static final VoxelShape[] SHAPE_BY_AGE;
    private final StemGrownBlock fruit;
    
    protected StemBlock(final StemGrownBlock cak, final Properties c) {
        super(c);
        this.fruit = cak;
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)StemBlock.AGE, 0));
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return StemBlock.SHAPE_BY_AGE[cee.<Integer>getValue((Property<Integer>)StemBlock.AGE)];
    }
    
    @Override
    protected boolean mayPlaceOn(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return cee.is(Blocks.FARMLAND);
    }
    
    public void randomTick(BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (aag.getRawBrightness(fx, 0) < 9) {
            return;
        }
        final float float6 = CropBlock.getGrowthSpeed(this, aag, fx);
        if (random.nextInt((int)(25.0f / float6) + 1) == 0) {
            final int integer7 = cee.<Integer>getValue((Property<Integer>)StemBlock.AGE);
            if (integer7 < 7) {
                cee = ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)StemBlock.AGE, integer7 + 1);
                aag.setBlock(fx, cee, 2);
            }
            else {
                final Direction gc8 = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                final BlockPos fx2 = fx.relative(gc8);
                final BlockState cee2 = aag.getBlockState(fx2.below());
                if (aag.getBlockState(fx2).isAir() && (cee2.is(Blocks.FARMLAND) || cee2.is(Blocks.DIRT) || cee2.is(Blocks.COARSE_DIRT) || cee2.is(Blocks.PODZOL) || cee2.is(Blocks.GRASS_BLOCK))) {
                    aag.setBlockAndUpdate(fx2, this.fruit.defaultBlockState());
                    aag.setBlockAndUpdate(fx, ((StateHolder<O, BlockState>)this.fruit.getAttachedStem().defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)HorizontalDirectionalBlock.FACING, gc8));
                }
            }
        }
    }
    
    @Nullable
    protected Item getSeedItem() {
        if (this.fruit == Blocks.PUMPKIN) {
            return Items.PUMPKIN_SEEDS;
        }
        if (this.fruit == Blocks.MELON) {
            return Items.MELON_SEEDS;
        }
        return null;
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        final Item blu5 = this.getSeedItem();
        return (blu5 == null) ? ItemStack.EMPTY : new ItemStack(blu5);
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        return cee.<Integer>getValue((Property<Integer>)StemBlock.AGE) != 7;
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return true;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        final int integer6 = Math.min(7, cee.<Integer>getValue((Property<Integer>)StemBlock.AGE) + Mth.nextInt(aag.random, 2, 5));
        final BlockState cee2 = ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)StemBlock.AGE, integer6);
        aag.setBlock(fx, cee2, 2);
        if (integer6 == 7) {
            cee2.randomTick(aag, fx, aag.random);
        }
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(StemBlock.AGE);
    }
    
    public StemGrownBlock getFruit() {
        return this.fruit;
    }
    
    static {
        AGE = BlockStateProperties.AGE_7;
        SHAPE_BY_AGE = new VoxelShape[] { Block.box(7.0, 0.0, 7.0, 9.0, 2.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 4.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 6.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 8.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 10.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 12.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 14.0, 9.0), Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0) };
    }
}
