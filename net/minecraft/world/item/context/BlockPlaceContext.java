package net.minecraft.world.item.context;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;

public class BlockPlaceContext extends UseOnContext {
    private final BlockPos relativePos;
    protected boolean replaceClicked;
    
    public BlockPlaceContext(final Player bft, final InteractionHand aoq, final ItemStack bly, final BlockHitResult dcg) {
        this(bft.level, bft, aoq, bly, dcg);
    }
    
    public BlockPlaceContext(final UseOnContext bnx) {
        this(bnx.getLevel(), bnx.getPlayer(), bnx.getHand(), bnx.getItemInHand(), bnx.getHitResult());
    }
    
    protected BlockPlaceContext(final Level bru, @Nullable final Player bft, final InteractionHand aoq, final ItemStack bly, final BlockHitResult dcg) {
        super(bru, bft, aoq, bly, dcg);
        this.replaceClicked = true;
        this.relativePos = dcg.getBlockPos().relative(dcg.getDirection());
        this.replaceClicked = bru.getBlockState(dcg.getBlockPos()).canBeReplaced(this);
    }
    
    public static BlockPlaceContext at(final BlockPlaceContext bnv, final BlockPos fx, final Direction gc) {
        return new BlockPlaceContext(bnv.getLevel(), bnv.getPlayer(), bnv.getHand(), bnv.getItemInHand(), new BlockHitResult(new Vec3(fx.getX() + 0.5 + gc.getStepX() * 0.5, fx.getY() + 0.5 + gc.getStepY() * 0.5, fx.getZ() + 0.5 + gc.getStepZ() * 0.5), gc, fx, false));
    }
    
    @Override
    public BlockPos getClickedPos() {
        return this.replaceClicked ? super.getClickedPos() : this.relativePos;
    }
    
    public boolean canPlace() {
        return this.replaceClicked || this.getLevel().getBlockState(this.getClickedPos()).canBeReplaced(this);
    }
    
    public boolean replacingClickedOnBlock() {
        return this.replaceClicked;
    }
    
    public Direction getNearestLookingDirection() {
        return Direction.orderedByNearest(this.getPlayer())[0];
    }
    
    public Direction[] getNearestLookingDirections() {
        final Direction[] arr2 = Direction.orderedByNearest(this.getPlayer());
        if (this.replaceClicked) {
            return arr2;
        }
        Direction gc3;
        int integer4;
        for (gc3 = this.getClickedFace(), integer4 = 0; integer4 < arr2.length && arr2[integer4] != gc3.getOpposite(); ++integer4) {}
        if (integer4 > 0) {
            System.arraycopy(arr2, 0, arr2, 1, integer4);
            arr2[0] = gc3.getOpposite();
        }
        return arr2;
    }
}
