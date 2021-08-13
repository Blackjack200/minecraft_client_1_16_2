package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class PressurePlateBlock extends BasePressurePlateBlock {
    public static final BooleanProperty POWERED;
    private final Sensitivity sensitivity;
    
    protected PressurePlateBlock(final Sensitivity a, final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Boolean>setValue((Property<Comparable>)PressurePlateBlock.POWERED, false));
        this.sensitivity = a;
    }
    
    @Override
    protected int getSignalForState(final BlockState cee) {
        return cee.<Boolean>getValue((Property<Boolean>)PressurePlateBlock.POWERED) ? 15 : 0;
    }
    
    @Override
    protected BlockState setSignalForState(final BlockState cee, final int integer) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)PressurePlateBlock.POWERED, integer > 0);
    }
    
    @Override
    protected void playOnSound(final LevelAccessor brv, final BlockPos fx) {
        if (this.material == Material.WOOD || this.material == Material.NETHER_WOOD) {
            brv.playSound(null, fx, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundSource.BLOCKS, 0.3f, 0.8f);
        }
        else {
            brv.playSound(null, fx, SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundSource.BLOCKS, 0.3f, 0.6f);
        }
    }
    
    @Override
    protected void playOffSound(final LevelAccessor brv, final BlockPos fx) {
        if (this.material == Material.WOOD || this.material == Material.NETHER_WOOD) {
            brv.playSound(null, fx, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundSource.BLOCKS, 0.3f, 0.7f);
        }
        else {
            brv.playSound(null, fx, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF, SoundSource.BLOCKS, 0.3f, 0.5f);
        }
    }
    
    @Override
    protected int getSignalStrength(final Level bru, final BlockPos fx) {
        final AABB dcf4 = PressurePlateBlock.TOUCH_AABB.move(fx);
        List<? extends Entity> list5 = null;
        switch (this.sensitivity) {
            case EVERYTHING: {
                list5 = bru.getEntities(null, dcf4);
                break;
            }
            case MOBS: {
                list5 = bru.getEntitiesOfClass((java.lang.Class<? extends Entity>)LivingEntity.class, dcf4);
                break;
            }
            default: {
                return 0;
            }
        }
        if (!list5.isEmpty()) {
            for (final Entity apx7 : list5) {
                if (!apx7.isIgnoringBlockTriggers()) {
                    return 15;
                }
            }
        }
        return 0;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(PressurePlateBlock.POWERED);
    }
    
    static {
        POWERED = BlockStateProperties.POWERED;
    }
    
    public enum Sensitivity {
        EVERYTHING, 
        MOBS;
    }
}
