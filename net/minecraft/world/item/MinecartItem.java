package net.minecraft.world.item;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.core.dispenser.DispenseItemBehavior;

public class MinecartItem extends Item {
    private static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR;
    private final AbstractMinecart.Type type;
    
    public MinecartItem(final AbstractMinecart.Type a, final Properties a) {
        super(a);
        this.type = a;
        DispenserBlock.registerBehavior(this, MinecartItem.DISPENSE_ITEM_BEHAVIOR);
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Level bru3 = bnx.getLevel();
        final BlockPos fx4 = bnx.getClickedPos();
        final BlockState cee5 = bru3.getBlockState(fx4);
        if (!cee5.is(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        }
        final ItemStack bly6 = bnx.getItemInHand();
        if (!bru3.isClientSide) {
            final RailShape cfh7 = (cee5.getBlock() instanceof BaseRailBlock) ? cee5.<RailShape>getValue(((BaseRailBlock)cee5.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
            double double8 = 0.0;
            if (cfh7.isAscending()) {
                double8 = 0.5;
            }
            final AbstractMinecart bhi10 = AbstractMinecart.createMinecart(bru3, fx4.getX() + 0.5, fx4.getY() + 0.0625 + double8, fx4.getZ() + 0.5, this.type);
            if (bly6.hasCustomHoverName()) {
                bhi10.setCustomName(bly6.getHoverName());
            }
            bru3.addFreshEntity(bhi10);
        }
        bly6.shrink(1);
        return InteractionResult.sidedSuccess(bru3.isClientSide);
    }
    
    static {
        DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
            
            public ItemStack execute(final BlockSource fy, final ItemStack bly) {
                final Direction gc4 = fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING);
                final Level bru5 = fy.getLevel();
                final double double6 = fy.x() + gc4.getStepX() * 1.125;
                final double double7 = Math.floor(fy.y()) + gc4.getStepY();
                final double double8 = fy.z() + gc4.getStepZ() * 1.125;
                final BlockPos fx12 = fy.getPos().relative(gc4);
                final BlockState cee13 = bru5.getBlockState(fx12);
                final RailShape cfh14 = (cee13.getBlock() instanceof BaseRailBlock) ? cee13.<RailShape>getValue(((BaseRailBlock)cee13.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
                double double9;
                if (cee13.is(BlockTags.RAILS)) {
                    if (cfh14.isAscending()) {
                        double9 = 0.6;
                    }
                    else {
                        double9 = 0.1;
                    }
                }
                else {
                    if (!cee13.isAir() || !bru5.getBlockState(fx12.below()).is(BlockTags.RAILS)) {
                        return this.defaultDispenseItemBehavior.dispense(fy, bly);
                    }
                    final BlockState cee14 = bru5.getBlockState(fx12.below());
                    final RailShape cfh15 = (cee14.getBlock() instanceof BaseRailBlock) ? cee14.<RailShape>getValue(((BaseRailBlock)cee14.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
                    if (gc4 == Direction.DOWN || !cfh15.isAscending()) {
                        double9 = -0.9;
                    }
                    else {
                        double9 = -0.4;
                    }
                }
                final AbstractMinecart bhi17 = AbstractMinecart.createMinecart(bru5, double6, double7 + double9, double8, ((MinecartItem)bly.getItem()).type);
                if (bly.hasCustomHoverName()) {
                    bhi17.setCustomName(bly.getHoverName());
                }
                bru5.addFreshEntity(bhi17);
                bly.shrink(1);
                return bly;
            }
            
            @Override
            protected void playSound(final BlockSource fy) {
                fy.getLevel().levelEvent(1000, fy.getPos(), 0);
            }
        };
    }
}
