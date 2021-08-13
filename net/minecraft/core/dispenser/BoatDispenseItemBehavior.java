package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockSource;
import net.minecraft.world.entity.vehicle.Boat;

public class BoatDispenseItemBehavior extends DefaultDispenseItemBehavior {
    private final DefaultDispenseItemBehavior defaultDispenseItemBehavior;
    private final Boat.Type type;
    
    public BoatDispenseItemBehavior(final Boat.Type b) {
        this.defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
        this.type = b;
    }
    
    public ItemStack execute(final BlockSource fy, final ItemStack bly) {
        final Direction gc4 = fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING);
        final Level bru5 = fy.getLevel();
        final double double6 = fy.x() + gc4.getStepX() * 1.125f;
        final double double7 = fy.y() + gc4.getStepY() * 1.125f;
        final double double8 = fy.z() + gc4.getStepZ() * 1.125f;
        final BlockPos fx12 = fy.getPos().relative(gc4);
        double double9;
        if (bru5.getFluidState(fx12).is(FluidTags.WATER)) {
            double9 = 1.0;
        }
        else {
            if (!bru5.getBlockState(fx12).isAir() || !bru5.getFluidState(fx12.below()).is(FluidTags.WATER)) {
                return this.defaultDispenseItemBehavior.dispense(fy, bly);
            }
            double9 = 0.0;
        }
        final Boat bhk15 = new Boat(bru5, double6, double7 + double9, double8);
        bhk15.setType(this.type);
        bhk15.yRot = gc4.toYRot();
        bru5.addFreshEntity(bhk15);
        bly.shrink(1);
        return bly;
    }
    
    @Override
    protected void playSound(final BlockSource fy) {
        fy.getLevel().levelEvent(1000, fy.getPos(), 0);
    }
}
