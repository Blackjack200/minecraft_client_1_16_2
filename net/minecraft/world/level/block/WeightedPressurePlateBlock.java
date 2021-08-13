package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class WeightedPressurePlateBlock extends BasePressurePlateBlock {
    public static final IntegerProperty POWER;
    private final int maxWeight;
    
    protected WeightedPressurePlateBlock(final int integer, final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Integer>setValue((Property<Comparable>)WeightedPressurePlateBlock.POWER, 0));
        this.maxWeight = integer;
    }
    
    @Override
    protected int getSignalStrength(final Level bru, final BlockPos fx) {
        final int integer4 = Math.min(bru.<Entity>getEntitiesOfClass((java.lang.Class<? extends Entity>)Entity.class, WeightedPressurePlateBlock.TOUCH_AABB.move(fx)).size(), this.maxWeight);
        if (integer4 > 0) {
            final float float5 = Math.min(this.maxWeight, integer4) / (float)this.maxWeight;
            return Mth.ceil(float5 * 15.0f);
        }
        return 0;
    }
    
    @Override
    protected void playOnSound(final LevelAccessor brv, final BlockPos fx) {
        brv.playSound(null, fx, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundSource.BLOCKS, 0.3f, 0.90000004f);
    }
    
    @Override
    protected void playOffSound(final LevelAccessor brv, final BlockPos fx) {
        brv.playSound(null, fx, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundSource.BLOCKS, 0.3f, 0.75f);
    }
    
    @Override
    protected int getSignalForState(final BlockState cee) {
        return cee.<Integer>getValue((Property<Integer>)WeightedPressurePlateBlock.POWER);
    }
    
    @Override
    protected BlockState setSignalForState(final BlockState cee, final int integer) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Integer>setValue((Property<Comparable>)WeightedPressurePlateBlock.POWER, integer);
    }
    
    @Override
    protected int getPressedTime() {
        return 10;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(WeightedPressurePlateBlock.POWER);
    }
    
    static {
        POWER = BlockStateProperties.POWER;
    }
}
