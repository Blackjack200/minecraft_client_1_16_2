package net.minecraft.world.level.block;

import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.Mth;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class OreBlock extends Block {
    public OreBlock(final Properties c) {
        super(c);
    }
    
    protected int xpOnDrop(final Random random) {
        if (this == Blocks.COAL_ORE) {
            return Mth.nextInt(random, 0, 2);
        }
        if (this == Blocks.DIAMOND_ORE) {
            return Mth.nextInt(random, 3, 7);
        }
        if (this == Blocks.EMERALD_ORE) {
            return Mth.nextInt(random, 3, 7);
        }
        if (this == Blocks.LAPIS_ORE) {
            return Mth.nextInt(random, 2, 5);
        }
        if (this == Blocks.NETHER_QUARTZ_ORE) {
            return Mth.nextInt(random, 2, 5);
        }
        if (this == Blocks.NETHER_GOLD_ORE) {
            return Mth.nextInt(random, 0, 1);
        }
        return 0;
    }
    
    @Override
    public void spawnAfterBreak(final BlockState cee, final ServerLevel aag, final BlockPos fx, final ItemStack bly) {
        super.spawnAfterBreak(cee, aag, fx, bly);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, bly) == 0) {
            final int integer6 = this.xpOnDrop(aag.random);
            if (integer6 > 0) {
                this.popExperience(aag, fx, integer6);
            }
        }
    }
}
