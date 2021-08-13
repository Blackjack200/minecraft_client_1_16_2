package net.minecraft.world.item.enchantment;

import java.util.Iterator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Material;
import net.minecraft.core.Position;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;

public class FrostWalkerEnchantment extends Enchantment {
    public FrostWalkerEnchantment(final Rarity a, final EquipmentSlot... arr) {
        super(a, EnchantmentCategory.ARMOR_FEET, arr);
    }
    
    @Override
    public int getMinCost(final int integer) {
        return integer * 10;
    }
    
    @Override
    public int getMaxCost(final int integer) {
        return this.getMinCost(integer) + 15;
    }
    
    @Override
    public boolean isTreasureOnly() {
        return true;
    }
    
    @Override
    public int getMaxLevel() {
        return 2;
    }
    
    public static void onEntityMoved(final LivingEntity aqj, final Level bru, final BlockPos fx, final int integer) {
        if (!aqj.isOnGround()) {
            return;
        }
        final BlockState cee5 = Blocks.FROSTED_ICE.defaultBlockState();
        final float float6 = (float)Math.min(16, 2 + integer);
        final BlockPos.MutableBlockPos a7 = new BlockPos.MutableBlockPos();
        for (final BlockPos fx2 : BlockPos.betweenClosed(fx.offset(-float6, -1.0, -float6), fx.offset(float6, -1.0, float6))) {
            if (fx2.closerThan(aqj.position(), float6)) {
                a7.set(fx2.getX(), fx2.getY() + 1, fx2.getZ());
                final BlockState cee6 = bru.getBlockState(a7);
                if (!cee6.isAir()) {
                    continue;
                }
                final BlockState cee7 = bru.getBlockState(fx2);
                if (cee7.getMaterial() != Material.WATER || cee7.<Integer>getValue((Property<Integer>)LiquidBlock.LEVEL) != 0 || !cee5.canSurvive(bru, fx2) || !bru.isUnobstructed(cee5, fx2, CollisionContext.empty())) {
                    continue;
                }
                bru.setBlockAndUpdate(fx2, cee5);
                bru.getBlockTicks().scheduleTick(fx2, Blocks.FROSTED_ICE, Mth.nextInt(aqj.getRandom(), 60, 120));
            }
        }
    }
    
    public boolean checkCompatibility(final Enchantment bpp) {
        return super.checkCompatibility(bpp) && bpp != Enchantments.DEPTH_STRIDER;
    }
}
