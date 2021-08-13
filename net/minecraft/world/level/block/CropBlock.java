package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
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

public class CropBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty AGE;
    private static final VoxelShape[] SHAPE_BY_AGE;
    
    protected CropBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)this.getAgeProperty(), 0));
    }
    
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return CropBlock.SHAPE_BY_AGE[cee.<Integer>getValue((Property<Integer>)this.getAgeProperty())];
    }
    
    @Override
    protected boolean mayPlaceOn(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return cee.is(Blocks.FARMLAND);
    }
    
    public IntegerProperty getAgeProperty() {
        return CropBlock.AGE;
    }
    
    public int getMaxAge() {
        return 7;
    }
    
    protected int getAge(final BlockState cee) {
        return cee.<Integer>getValue((Property<Integer>)this.getAgeProperty());
    }
    
    public BlockState getStateForAge(final int integer) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)this.getAgeProperty(), integer);
    }
    
    public boolean isMaxAge(final BlockState cee) {
        return cee.<Integer>getValue((Property<Integer>)this.getAgeProperty()) >= this.getMaxAge();
    }
    
    @Override
    public boolean isRandomlyTicking(final BlockState cee) {
        return !this.isMaxAge(cee);
    }
    
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (aag.getRawBrightness(fx, 0) >= 9) {
            final int integer6 = this.getAge(cee);
            if (integer6 < this.getMaxAge()) {
                final float float7 = getGrowthSpeed(this, aag, fx);
                if (random.nextInt((int)(25.0f / float7) + 1) == 0) {
                    aag.setBlock(fx, this.getStateForAge(integer6 + 1), 2);
                }
            }
        }
    }
    
    public void growCrops(final Level bru, final BlockPos fx, final BlockState cee) {
        int integer5 = this.getAge(cee) + this.getBonemealAgeIncrease(bru);
        final int integer6 = this.getMaxAge();
        if (integer5 > integer6) {
            integer5 = integer6;
        }
        bru.setBlock(fx, this.getStateForAge(integer5), 2);
    }
    
    protected int getBonemealAgeIncrease(final Level bru) {
        return Mth.nextInt(bru.random, 2, 5);
    }
    
    protected static float getGrowthSpeed(final Block bul, final BlockGetter bqz, final BlockPos fx) {
        float float4 = 1.0f;
        final BlockPos fx2 = fx.below();
        for (int integer6 = -1; integer6 <= 1; ++integer6) {
            for (int integer7 = -1; integer7 <= 1; ++integer7) {
                float float5 = 0.0f;
                final BlockState cee9 = bqz.getBlockState(fx2.offset(integer6, 0, integer7));
                if (cee9.is(Blocks.FARMLAND)) {
                    float5 = 1.0f;
                    if (cee9.<Integer>getValue((Property<Integer>)FarmBlock.MOISTURE) > 0) {
                        float5 = 3.0f;
                    }
                }
                if (integer6 != 0 || integer7 != 0) {
                    float5 /= 4.0f;
                }
                float4 += float5;
            }
        }
        final BlockPos fx3 = fx.north();
        final BlockPos fx4 = fx.south();
        final BlockPos fx5 = fx.west();
        final BlockPos fx6 = fx.east();
        final boolean boolean10 = bul == bqz.getBlockState(fx5).getBlock() || bul == bqz.getBlockState(fx6).getBlock();
        final boolean boolean11 = bul == bqz.getBlockState(fx3).getBlock() || bul == bqz.getBlockState(fx4).getBlock();
        if (boolean10 && boolean11) {
            float4 /= 2.0f;
        }
        else {
            final boolean boolean12 = bul == bqz.getBlockState(fx5.north()).getBlock() || bul == bqz.getBlockState(fx6.north()).getBlock() || bul == bqz.getBlockState(fx6.south()).getBlock() || bul == bqz.getBlockState(fx5.south()).getBlock();
            if (boolean12) {
                float4 /= 2.0f;
            }
        }
        return float4;
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return (brw.getRawBrightness(fx, 0) >= 8 || brw.canSeeSky(fx)) && super.canSurvive(cee, brw, fx);
    }
    
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        if (apx instanceof Ravager && bru.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            bru.destroyBlock(fx, true, apx);
        }
        super.entityInside(cee, bru, fx, apx);
    }
    
    protected ItemLike getBaseSeedId() {
        return Items.WHEAT_SEEDS;
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return new ItemStack(this.getBaseSeedId());
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        return !this.isMaxAge(cee);
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return true;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        this.growCrops(aag, fx, cee);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(CropBlock.AGE);
    }
    
    static {
        AGE = BlockStateProperties.AGE_7;
        SHAPE_BY_AGE = new VoxelShape[] { Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0) };
    }
}
