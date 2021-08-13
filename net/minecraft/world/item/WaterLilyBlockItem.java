package net.minecraft.world.item;

import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class WaterLilyBlockItem extends BlockItem {
    public WaterLilyBlockItem(final Block bul, final Properties a) {
        super(bul, a);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        return InteractionResult.PASS;
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        final BlockHitResult dcg5 = Item.getPlayerPOVHitResult(bru, bft, ClipContext.Fluid.SOURCE_ONLY);
        final BlockHitResult dcg6 = dcg5.withPosition(dcg5.getBlockPos().above());
        final InteractionResult aor7 = super.useOn(new UseOnContext(bft, aoq, dcg6));
        return new InteractionResultHolder<ItemStack>(aor7, bft.getItemInHand(aoq));
    }
}
