package net.minecraft.world.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.Blocks;
import java.util.function.Consumer;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;

public class ShearsItem extends Item {
    public ShearsItem(final Properties a) {
        super(a);
    }
    
    @Override
    public boolean mineBlock(final ItemStack bly, final Level bru, final BlockState cee, final BlockPos fx, final LivingEntity aqj) {
        if (!bru.isClientSide && !cee.getBlock().is(BlockTags.FIRE)) {
            bly.<LivingEntity>hurtAndBreak(1, aqj, (java.util.function.Consumer<LivingEntity>)(aqj -> aqj.broadcastBreakEvent(EquipmentSlot.MAINHAND)));
        }
        return cee.is(BlockTags.LEAVES) || cee.is(Blocks.COBWEB) || cee.is(Blocks.GRASS) || cee.is(Blocks.FERN) || cee.is(Blocks.DEAD_BUSH) || cee.is(Blocks.VINE) || cee.is(Blocks.TRIPWIRE) || cee.is(BlockTags.WOOL) || super.mineBlock(bly, bru, cee, fx, aqj);
    }
    
    @Override
    public boolean isCorrectToolForDrops(final BlockState cee) {
        return cee.is(Blocks.COBWEB) || cee.is(Blocks.REDSTONE_WIRE) || cee.is(Blocks.TRIPWIRE);
    }
    
    @Override
    public float getDestroySpeed(final ItemStack bly, final BlockState cee) {
        if (cee.is(Blocks.COBWEB) || cee.is(BlockTags.LEAVES)) {
            return 15.0f;
        }
        if (cee.is(BlockTags.WOOL)) {
            return 5.0f;
        }
        return super.getDestroySpeed(bly, cee);
    }
}
