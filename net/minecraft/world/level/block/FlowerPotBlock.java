package net.minecraft.world.level.block;

import com.google.common.collect.Maps;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.Map;

public class FlowerPotBlock extends Block {
    private static final Map<Block, Block> POTTED_BY_CONTENT;
    protected static final VoxelShape SHAPE;
    private final Block content;
    
    public FlowerPotBlock(final Block bul, final Properties c) {
        super(c);
        this.content = bul;
        FlowerPotBlock.POTTED_BY_CONTENT.put(bul, this);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return FlowerPotBlock.SHAPE;
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        final ItemStack bly8 = bft.getItemInHand(aoq);
        final Item blu9 = bly8.getItem();
        final Block bul10 = (Block)((blu9 instanceof BlockItem) ? FlowerPotBlock.POTTED_BY_CONTENT.getOrDefault(((BlockItem)blu9).getBlock(), Blocks.AIR) : Blocks.AIR);
        final boolean boolean11 = bul10 == Blocks.AIR;
        final boolean boolean12 = this.content == Blocks.AIR;
        if (boolean11 != boolean12) {
            if (boolean12) {
                bru.setBlock(fx, bul10.defaultBlockState(), 3);
                bft.awardStat(Stats.POT_FLOWER);
                if (!bft.abilities.instabuild) {
                    bly8.shrink(1);
                }
            }
            else {
                final ItemStack bly9 = new ItemStack(this.content);
                if (bly8.isEmpty()) {
                    bft.setItemInHand(aoq, bly9);
                }
                else if (!bft.addItem(bly9)) {
                    bft.drop(bly9, false);
                }
                bru.setBlock(fx, Blocks.FLOWER_POT.defaultBlockState(), 3);
            }
            return InteractionResult.sidedSuccess(bru.isClientSide);
        }
        return InteractionResult.CONSUME;
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        if (this.content == Blocks.AIR) {
            return super.getCloneItemStack(bqz, fx, cee);
        }
        return new ItemStack(this.content);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc == Direction.DOWN && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    public Block getContent() {
        return this.content;
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        POTTED_BY_CONTENT = (Map)Maps.newHashMap();
        SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0);
    }
}
