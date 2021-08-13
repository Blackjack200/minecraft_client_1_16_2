package net.minecraft.world.item;

import net.minecraft.world.level.dimension.end.EndDragonFight;
import java.util.List;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public class EndCrystalItem extends Item {
    public EndCrystalItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        final BlockPos fx4 = bnx.getClickedPos();
        final BlockState cee5 = bru3.getBlockState(fx4);
        if (!cee5.is(Blocks.OBSIDIAN) && !cee5.is(Blocks.BEDROCK)) {
            return InteractionResult.FAIL;
        }
        final BlockPos fx5 = fx4.above();
        if (!bru3.isEmptyBlock(fx5)) {
            return InteractionResult.FAIL;
        }
        final double double7 = fx5.getX();
        final double double8 = fx5.getY();
        final double double9 = fx5.getZ();
        final List<Entity> list13 = bru3.getEntities(null, new AABB(double7, double8, double9, double7 + 1.0, double8 + 2.0, double9 + 1.0));
        if (!list13.isEmpty()) {
            return InteractionResult.FAIL;
        }
        if (bru3 instanceof ServerLevel) {
            final EndCrystal bbn14 = new EndCrystal(bru3, double7 + 0.5, double8, double9 + 0.5);
            bbn14.setShowBottom(false);
            bru3.addFreshEntity(bbn14);
            final EndDragonFight chd15 = ((ServerLevel)bru3).dragonFight();
            if (chd15 != null) {
                chd15.tryRespawn();
            }
        }
        bnx.getItemInHand().shrink(1);
        return InteractionResult.sidedSuccess(bru3.isClientSide);
    }
    
    @Override
    public boolean isFoil(final ItemStack bly) {
        return true;
    }
}
