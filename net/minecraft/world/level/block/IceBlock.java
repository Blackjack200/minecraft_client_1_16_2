package net.minecraft.world.level.block;

import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LightLayer;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class IceBlock extends HalfTransparentBlock {
    public IceBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public void playerDestroy(final Level bru, final Player bft, final BlockPos fx, final BlockState cee, @Nullable final BlockEntity ccg, final ItemStack bly) {
        super.playerDestroy(bru, bft, fx, cee, ccg, bly);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, bly) == 0) {
            if (bru.dimensionType().ultraWarm()) {
                bru.removeBlock(fx, false);
                return;
            }
            final Material cux8 = bru.getBlockState(fx.below()).getMaterial();
            if (cux8.blocksMotion() || cux8.isLiquid()) {
                bru.setBlockAndUpdate(fx, Blocks.WATER.defaultBlockState());
            }
        }
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, final BlockPos fx, final Random random) {
        if (aag.getBrightness(LightLayer.BLOCK, fx) > 11 - cee.getLightBlock(aag, fx)) {
            this.melt(cee, aag, fx);
        }
    }
    
    protected void melt(final BlockState cee, final Level bru, final BlockPos fx) {
        if (bru.dimensionType().ultraWarm()) {
            bru.removeBlock(fx, false);
            return;
        }
        bru.setBlockAndUpdate(fx, Blocks.WATER.defaultBlockState());
        bru.neighborChanged(fx, Blocks.WATER, fx);
    }
    
    @Override
    public PushReaction getPistonPushReaction(final BlockState cee) {
        return PushReaction.NORMAL;
    }
}
