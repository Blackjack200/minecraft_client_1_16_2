package net.minecraft.world.item;

import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class LeadItem extends Item {
    public LeadItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        final BlockPos fx4 = bnx.getClickedPos();
        final Block bul5 = bru3.getBlockState(fx4).getBlock();
        if (bul5.is(BlockTags.FENCES)) {
            final Player bft6 = bnx.getPlayer();
            if (!bru3.isClientSide && bft6 != null) {
                bindPlayerMobs(bft6, bru3, fx4);
            }
            return InteractionResult.sidedSuccess(bru3.isClientSide);
        }
        return InteractionResult.PASS;
    }
    
    public static InteractionResult bindPlayerMobs(final Player bft, final Level bru, final BlockPos fx) {
        LeashFenceKnotEntity bcn4 = null;
        boolean boolean5 = false;
        final double double6 = 7.0;
        final int integer8 = fx.getX();
        final int integer9 = fx.getY();
        final int integer10 = fx.getZ();
        final List<Mob> list11 = bru.<Mob>getEntitiesOfClass((java.lang.Class<? extends Mob>)Mob.class, new AABB(integer8 - 7.0, integer9 - 7.0, integer10 - 7.0, integer8 + 7.0, integer9 + 7.0, integer10 + 7.0));
        for (final Mob aqk13 : list11) {
            if (aqk13.getLeashHolder() == bft) {
                if (bcn4 == null) {
                    bcn4 = LeashFenceKnotEntity.getOrCreateKnot(bru, fx);
                }
                aqk13.setLeashedTo(bcn4, true);
                boolean5 = true;
            }
        }
        return boolean5 ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }
}
