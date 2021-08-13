package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class JigsawBlock extends Block implements EntityBlock {
    public static final EnumProperty<FrontAndTop> ORIENTATION;
    
    protected JigsawBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<FrontAndTop, FrontAndTop>setValue(JigsawBlock.ORIENTATION, FrontAndTop.NORTH_UP));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(JigsawBlock.ORIENTATION);
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<FrontAndTop, FrontAndTop>setValue(JigsawBlock.ORIENTATION, bzj.rotation().rotate(cee.<FrontAndTop>getValue(JigsawBlock.ORIENTATION)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return ((StateHolder<O, BlockState>)cee).<FrontAndTop, FrontAndTop>setValue(JigsawBlock.ORIENTATION, byd.rotation().rotate(cee.<FrontAndTop>getValue(JigsawBlock.ORIENTATION)));
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final Direction gc3 = bnv.getClickedFace();
        Direction gc4;
        if (gc3.getAxis() == Direction.Axis.Y) {
            gc4 = bnv.getHorizontalDirection().getOpposite();
        }
        else {
            gc4 = Direction.UP;
        }
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<FrontAndTop, FrontAndTop>setValue(JigsawBlock.ORIENTATION, FrontAndTop.fromFrontAndTop(gc3, gc4));
    }
    
    @Nullable
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new JigsawBlockEntity();
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        final BlockEntity ccg8 = bru.getBlockEntity(fx);
        if (ccg8 instanceof JigsawBlockEntity && bft.canUseGameMasterBlocks()) {
            bft.openJigsawBlock((JigsawBlockEntity)ccg8);
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    public static boolean canAttach(final StructureTemplate.StructureBlockInfo c1, final StructureTemplate.StructureBlockInfo c2) {
        final Direction gc3 = getFrontFacing(c1.state);
        final Direction gc4 = getFrontFacing(c2.state);
        final Direction gc5 = getTopFacing(c1.state);
        final Direction gc6 = getTopFacing(c2.state);
        final JigsawBlockEntity.JointType a7 = (JigsawBlockEntity.JointType)JigsawBlockEntity.JointType.byName(c1.nbt.getString("joint")).orElseGet(() -> gc3.getAxis().isHorizontal() ? JigsawBlockEntity.JointType.ALIGNED : JigsawBlockEntity.JointType.ROLLABLE);
        final boolean boolean8 = a7 == JigsawBlockEntity.JointType.ROLLABLE;
        return gc3 == gc4.getOpposite() && (boolean8 || gc5 == gc6) && c1.nbt.getString("target").equals(c2.nbt.getString("name"));
    }
    
    public static Direction getFrontFacing(final BlockState cee) {
        return cee.<FrontAndTop>getValue(JigsawBlock.ORIENTATION).front();
    }
    
    public static Direction getTopFacing(final BlockState cee) {
        return cee.<FrontAndTop>getValue(JigsawBlock.ORIENTATION).top();
    }
    
    static {
        ORIENTATION = BlockStateProperties.ORIENTATION;
    }
}
