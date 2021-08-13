package net.minecraft.core.dispenser;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.core.Position;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockSource;

public class DefaultDispenseItemBehavior implements DispenseItemBehavior {
    public final ItemStack dispense(final BlockSource fy, final ItemStack bly) {
        final ItemStack bly2 = this.execute(fy, bly);
        this.playSound(fy);
        this.playAnimation(fy, fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING));
        return bly2;
    }
    
    protected ItemStack execute(final BlockSource fy, final ItemStack bly) {
        final Direction gc4 = fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING);
        final Position gk5 = DispenserBlock.getDispensePosition(fy);
        final ItemStack bly2 = bly.split(1);
        spawnItem(fy.getLevel(), bly2, 6, gc4, gk5);
        return bly;
    }
    
    public static void spawnItem(final Level bru, final ItemStack bly, final int integer, final Direction gc, final Position gk) {
        final double double6 = gk.x();
        double double7 = gk.y();
        final double double8 = gk.z();
        if (gc.getAxis() == Direction.Axis.Y) {
            double7 -= 0.125;
        }
        else {
            double7 -= 0.15625;
        }
        final ItemEntity bcs12 = new ItemEntity(bru, double6, double7, double8, bly);
        final double double9 = bru.random.nextDouble() * 0.1 + 0.2;
        bcs12.setDeltaMovement(bru.random.nextGaussian() * 0.007499999832361937 * integer + gc.getStepX() * double9, bru.random.nextGaussian() * 0.007499999832361937 * integer + 0.20000000298023224, bru.random.nextGaussian() * 0.007499999832361937 * integer + gc.getStepZ() * double9);
        bru.addFreshEntity(bcs12);
    }
    
    protected void playSound(final BlockSource fy) {
        fy.getLevel().levelEvent(1000, fy.getPos(), 0);
    }
    
    protected void playAnimation(final BlockSource fy, final Direction gc) {
        fy.getLevel().levelEvent(2000, fy.getPos(), gc.get3DDataValue());
    }
}
