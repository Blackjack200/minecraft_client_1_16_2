package net.minecraft.world.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.EntityType;

public class HangingEntityItem extends Item {
    private final EntityType<? extends HangingEntity> type;
    
    public HangingEntityItem(final EntityType<? extends HangingEntity> aqb, final Properties a) {
        super(a);
        this.type = aqb;
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final BlockPos fx3 = bnx.getClickedPos();
        final Direction gc4 = bnx.getClickedFace();
        final BlockPos fx4 = fx3.relative(gc4);
        final Player bft6 = bnx.getPlayer();
        final ItemStack bly7 = bnx.getItemInHand();
        if (bft6 != null && !this.mayPlace(bft6, gc4, bly7, fx4)) {
            return InteractionResult.FAIL;
        }
        final Level bru8 = bnx.getLevel();
        HangingEntity bcl9;
        if (this.type == EntityType.PAINTING) {
            bcl9 = new Painting(bru8, fx4, gc4);
        }
        else {
            if (this.type != EntityType.ITEM_FRAME) {
                return InteractionResult.sidedSuccess(bru8.isClientSide);
            }
            bcl9 = new ItemFrame(bru8, fx4, gc4);
        }
        final CompoundTag md10 = bly7.getTag();
        if (md10 != null) {
            EntityType.updateCustomEntityTag(bru8, bft6, bcl9, md10);
        }
        if (bcl9.survives()) {
            if (!bru8.isClientSide) {
                bcl9.playPlacementSound();
                bru8.addFreshEntity(bcl9);
            }
            bly7.shrink(1);
            return InteractionResult.sidedSuccess(bru8.isClientSide);
        }
        return InteractionResult.CONSUME;
    }
    
    protected boolean mayPlace(final Player bft, final Direction gc, final ItemStack bly, final BlockPos fx) {
        return !gc.getAxis().isVertical() && bft.mayUseItemAt(fx, gc, bly);
    }
}
