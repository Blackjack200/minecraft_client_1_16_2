package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class NoteBlock extends Block {
    public static final EnumProperty<NoteBlockInstrument> INSTRUMENT;
    public static final BooleanProperty POWERED;
    public static final IntegerProperty NOTE;
    
    public NoteBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue(NoteBlock.INSTRUMENT, NoteBlockInstrument.HARP)).setValue((Property<Comparable>)NoteBlock.NOTE, 0)).<Comparable, Boolean>setValue((Property<Comparable>)NoteBlock.POWERED, false));
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<NoteBlockInstrument, NoteBlockInstrument>setValue(NoteBlock.INSTRUMENT, NoteBlockInstrument.byState(bnv.getLevel().getBlockState(bnv.getClickedPos().below())));
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == Direction.DOWN) {
            return ((StateHolder<O, BlockState>)cee1).<NoteBlockInstrument, NoteBlockInstrument>setValue(NoteBlock.INSTRUMENT, NoteBlockInstrument.byState(cee3));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        final boolean boolean7 = bru.hasNeighborSignal(fx3);
        if (boolean7 != cee.<Boolean>getValue((Property<Boolean>)NoteBlock.POWERED)) {
            if (boolean7) {
                this.playNote(bru, fx3);
            }
            bru.setBlock(fx3, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)NoteBlock.POWERED, boolean7), 3);
        }
    }
    
    private void playNote(final Level bru, final BlockPos fx) {
        if (bru.getBlockState(fx.above()).isAir()) {
            bru.blockEvent(fx, this, 0, 0);
        }
    }
    
    @Override
    public InteractionResult use(BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        cee = ((StateHolder<O, BlockState>)cee).<Comparable>cycle((Property<Comparable>)NoteBlock.NOTE);
        bru.setBlock(fx, cee, 3);
        this.playNote(bru, fx);
        bft.awardStat(Stats.TUNE_NOTEBLOCK);
        return InteractionResult.CONSUME;
    }
    
    @Override
    public void attack(final BlockState cee, final Level bru, final BlockPos fx, final Player bft) {
        if (bru.isClientSide) {
            return;
        }
        this.playNote(bru, fx);
        bft.awardStat(Stats.PLAY_NOTEBLOCK);
    }
    
    @Override
    public boolean triggerEvent(final BlockState cee, final Level bru, final BlockPos fx, final int integer4, final int integer5) {
        final int integer6 = cee.<Integer>getValue((Property<Integer>)NoteBlock.NOTE);
        final float float8 = (float)Math.pow(2.0, (integer6 - 12) / 12.0);
        bru.playSound(null, fx, cee.<NoteBlockInstrument>getValue(NoteBlock.INSTRUMENT).getSoundEvent(), SoundSource.RECORDS, 3.0f, float8);
        bru.addParticle(ParticleTypes.NOTE, fx.getX() + 0.5, fx.getY() + 1.2, fx.getZ() + 0.5, integer6 / 24.0, 0.0, 0.0);
        return true;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(NoteBlock.INSTRUMENT, NoteBlock.POWERED, NoteBlock.NOTE);
    }
    
    static {
        INSTRUMENT = BlockStateProperties.NOTEBLOCK_INSTRUMENT;
        POWERED = BlockStateProperties.POWERED;
        NOTE = BlockStateProperties.NOTE;
    }
}
