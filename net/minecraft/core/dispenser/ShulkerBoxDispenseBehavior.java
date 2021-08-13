package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockSource;

public class ShulkerBoxDispenseBehavior extends OptionalDispenseItemBehavior {
    @Override
    protected ItemStack execute(final BlockSource fy, final ItemStack bly) {
        this.setSuccess(false);
        final Item blu4 = bly.getItem();
        if (blu4 instanceof BlockItem) {
            final Direction gc5 = fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING);
            final BlockPos fx6 = fy.getPos().relative(gc5);
            final Direction gc6 = fy.getLevel().isEmptyBlock(fx6.below()) ? gc5 : Direction.UP;
            this.setSuccess(((BlockItem)blu4).place(new DirectionalPlaceContext(fy.getLevel(), fx6, gc5, bly, gc6)).consumesAction());
        }
        return bly;
    }
}
