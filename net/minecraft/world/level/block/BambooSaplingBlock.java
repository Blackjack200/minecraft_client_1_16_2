package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.BambooLeaves;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.Direction;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BambooSaplingBlock extends Block implements BonemealableBlock {
    protected static final VoxelShape SAPLING_SHAPE;
    
    public BambooSaplingBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        final Vec3 dck6 = cee.getOffset(bqz, fx);
        return BambooSaplingBlock.SAPLING_SHAPE.move(dck6.x, dck6.y, dck6.z);
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (random.nextInt(3) == 0 && aag.isEmptyBlock(fx.above()) && aag.getRawBrightness(fx.above(), 0) >= 9) {
            this.growBamboo(aag, fx);
        }
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        return brw.getBlockState(fx.below()).is(BlockTags.BAMBOO_PLANTABLE_ON);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (!cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (gc == Direction.UP && cee3.is(Blocks.BAMBOO)) {
            brv.setBlock(fx5, Blocks.BAMBOO.defaultBlockState(), 2);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public ItemStack getCloneItemStack(final BlockGetter bqz, final BlockPos fx, final BlockState cee) {
        return new ItemStack(Items.BAMBOO);
    }
    
    @Override
    public boolean isValidBonemealTarget(final BlockGetter bqz, final BlockPos fx, final BlockState cee, final boolean boolean4) {
        return bqz.getBlockState(fx.above()).isAir();
    }
    
    @Override
    public boolean isBonemealSuccess(final Level bru, final Random random, final BlockPos fx, final BlockState cee) {
        return true;
    }
    
    @Override
    public void performBonemeal(final ServerLevel aag, final Random random, final BlockPos fx, final BlockState cee) {
        this.growBamboo(aag, fx);
    }
    
    @Override
    public float getDestroyProgress(final BlockState cee, final Player bft, final BlockGetter bqz, final BlockPos fx) {
        if (bft.getMainHandItem().getItem() instanceof SwordItem) {
            return 1.0f;
        }
        return super.getDestroyProgress(cee, bft, bqz, fx);
    }
    
    protected void growBamboo(final Level bru, final BlockPos fx) {
        bru.setBlock(fx.above(), ((StateHolder<O, BlockState>)Blocks.BAMBOO.defaultBlockState()).<BambooLeaves, BambooLeaves>setValue(BambooBlock.LEAVES, BambooLeaves.SMALL), 3);
    }
    
    static {
        SAPLING_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 12.0, 12.0);
    }
}
