package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class StructureBlock extends BaseEntityBlock {
    public static final EnumProperty<StructureMode> MODE;
    
    protected StructureBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new StructureBlockEntity();
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        final BlockEntity ccg8 = bru.getBlockEntity(fx);
        if (ccg8 instanceof StructureBlockEntity) {
            return ((StructureBlockEntity)ccg8).usedBy(bft) ? InteractionResult.sidedSuccess(bru.isClientSide) : InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, @Nullable final LivingEntity aqj, final ItemStack bly) {
        if (bru.isClientSide) {
            return;
        }
        if (aqj != null) {
            final BlockEntity ccg7 = bru.getBlockEntity(fx);
            if (ccg7 instanceof StructureBlockEntity) {
                ((StructureBlockEntity)ccg7).createdBy(aqj);
            }
        }
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<StructureMode, StructureMode>setValue(StructureBlock.MODE, StructureMode.DATA);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(StructureBlock.MODE);
    }
    
    @Override
    public void neighborChanged(final BlockState cee, final Level bru, final BlockPos fx3, final Block bul, final BlockPos fx5, final boolean boolean6) {
        if (!(bru instanceof ServerLevel)) {
            return;
        }
        final BlockEntity ccg8 = bru.getBlockEntity(fx3);
        if (!(ccg8 instanceof StructureBlockEntity)) {
            return;
        }
        final StructureBlockEntity cdg9 = (StructureBlockEntity)ccg8;
        final boolean boolean7 = bru.hasNeighborSignal(fx3);
        final boolean boolean8 = cdg9.isPowered();
        if (boolean7 && !boolean8) {
            cdg9.setPowered(true);
            this.trigger((ServerLevel)bru, cdg9);
        }
        else if (!boolean7 && boolean8) {
            cdg9.setPowered(false);
        }
    }
    
    private void trigger(final ServerLevel aag, final StructureBlockEntity cdg) {
        switch (cdg.getMode()) {
            case SAVE: {
                cdg.saveStructure(false);
                break;
            }
            case LOAD: {
                cdg.loadStructure(aag, false);
                break;
            }
            case CORNER: {
                cdg.unloadStructure();
            }
        }
    }
    
    static {
        MODE = BlockStateProperties.STRUCTUREBLOCK_MODE;
    }
}
