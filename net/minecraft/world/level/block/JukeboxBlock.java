package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class JukeboxBlock extends BaseEntityBlock {
    public static final BooleanProperty HAS_RECORD;
    
    protected JukeboxBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Comparable, Boolean>setValue((Property<Comparable>)JukeboxBlock.HAS_RECORD, false));
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, @Nullable final LivingEntity aqj, final ItemStack bly) {
        super.setPlacedBy(bru, fx, cee, aqj, bly);
        final CompoundTag md7 = bly.getOrCreateTag();
        if (md7.contains("BlockEntityTag")) {
            final CompoundTag md8 = md7.getCompound("BlockEntityTag");
            if (md8.contains("RecordItem")) {
                bru.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)JukeboxBlock.HAS_RECORD, true), 2);
            }
        }
    }
    
    @Override
    public InteractionResult use(BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (cee.<Boolean>getValue((Property<Boolean>)JukeboxBlock.HAS_RECORD)) {
            this.dropRecording(bru, fx);
            cee = ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)JukeboxBlock.HAS_RECORD, false);
            bru.setBlock(fx, cee, 2);
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    public void setRecord(final LevelAccessor brv, final BlockPos fx, final BlockState cee, final ItemStack bly) {
        final BlockEntity ccg6 = brv.getBlockEntity(fx);
        if (!(ccg6 instanceof JukeboxBlockEntity)) {
            return;
        }
        ((JukeboxBlockEntity)ccg6).setRecord(bly.copy());
        brv.setBlock(fx, ((StateHolder<O, BlockState>)cee).<Comparable, Boolean>setValue((Property<Comparable>)JukeboxBlock.HAS_RECORD, true), 2);
    }
    
    private void dropRecording(final Level bru, final BlockPos fx) {
        if (bru.isClientSide) {
            return;
        }
        final BlockEntity ccg4 = bru.getBlockEntity(fx);
        if (!(ccg4 instanceof JukeboxBlockEntity)) {
            return;
        }
        final JukeboxBlockEntity ccx5 = (JukeboxBlockEntity)ccg4;
        final ItemStack bly6 = ccx5.getRecord();
        if (bly6.isEmpty()) {
            return;
        }
        bru.levelEvent(1010, fx, 0);
        ccx5.clearContent();
        final float float7 = 0.7f;
        final double double8 = bru.random.nextFloat() * 0.7f + 0.15000000596046448;
        final double double9 = bru.random.nextFloat() * 0.7f + 0.06000000238418579 + 0.6;
        final double double10 = bru.random.nextFloat() * 0.7f + 0.15000000596046448;
        final ItemStack bly7 = bly6.copy();
        final ItemEntity bcs15 = new ItemEntity(bru, fx.getX() + double8, fx.getY() + double9, fx.getZ() + double10, bly7);
        bcs15.setDefaultPickUpDelay();
        bru.addFreshEntity(bcs15);
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        this.dropRecording(bru, fx);
        super.onRemove(cee1, bru, fx, cee4, boolean5);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new JukeboxBlockEntity();
    }
    
    @Override
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        final BlockEntity ccg5 = bru.getBlockEntity(fx);
        if (ccg5 instanceof JukeboxBlockEntity) {
            final Item blu6 = ((JukeboxBlockEntity)ccg5).getRecord().getItem();
            if (blu6 instanceof RecordItem) {
                return ((RecordItem)blu6).getAnalogOutput();
            }
        }
        return 0;
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(JukeboxBlock.HAS_RECORD);
    }
    
    static {
        HAS_RECORD = BlockStateProperties.HAS_RECORD;
    }
}
