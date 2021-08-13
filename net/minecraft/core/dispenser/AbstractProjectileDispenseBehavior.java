package net.minecraft.core.dispenser;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.core.Position;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockSource;

public abstract class AbstractProjectileDispenseBehavior extends DefaultDispenseItemBehavior {
    public ItemStack execute(final BlockSource fy, final ItemStack bly) {
        final Level bru4 = fy.getLevel();
        final Position gk5 = DispenserBlock.getDispensePosition(fy);
        final Direction gc6 = fy.getBlockState().<Direction>getValue((Property<Direction>)DispenserBlock.FACING);
        final Projectile bgj7 = this.getProjectile(bru4, gk5, bly);
        bgj7.shoot(gc6.getStepX(), gc6.getStepY() + 0.1f, gc6.getStepZ(), this.getPower(), this.getUncertainty());
        bru4.addFreshEntity(bgj7);
        bly.shrink(1);
        return bly;
    }
    
    @Override
    protected void playSound(final BlockSource fy) {
        fy.getLevel().levelEvent(1002, fy.getPos(), 0);
    }
    
    protected abstract Projectile getProjectile(final Level bru, final Position gk, final ItemStack bly);
    
    protected float getUncertainty() {
        return 6.0f;
    }
    
    protected float getPower() {
        return 1.1f;
    }
}
