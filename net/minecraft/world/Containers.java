package net.minecraft.world;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.util.Random;

public class Containers {
    private static final Random RANDOM;
    
    public static void dropContents(final Level bru, final BlockPos fx, final Container aok) {
        dropContents(bru, fx.getX(), fx.getY(), fx.getZ(), aok);
    }
    
    public static void dropContents(final Level bru, final Entity apx, final Container aok) {
        dropContents(bru, apx.getX(), apx.getY(), apx.getZ(), aok);
    }
    
    private static void dropContents(final Level bru, final double double2, final double double3, final double double4, final Container aok) {
        for (int integer9 = 0; integer9 < aok.getContainerSize(); ++integer9) {
            dropItemStack(bru, double2, double3, double4, aok.getItem(integer9));
        }
    }
    
    public static void dropContents(final Level bru, final BlockPos fx, final NonNullList<ItemStack> gj) {
        gj.forEach(bly -> dropItemStack(bru, fx.getX(), fx.getY(), fx.getZ(), bly));
    }
    
    public static void dropItemStack(final Level bru, final double double2, final double double3, final double double4, final ItemStack bly) {
        final double double5 = EntityType.ITEM.getWidth();
        final double double6 = 1.0 - double5;
        final double double7 = double5 / 2.0;
        final double double8 = Math.floor(double2) + Containers.RANDOM.nextDouble() * double6 + double7;
        final double double9 = Math.floor(double3) + Containers.RANDOM.nextDouble() * double6;
        final double double10 = Math.floor(double4) + Containers.RANDOM.nextDouble() * double6 + double7;
        while (!bly.isEmpty()) {
            final ItemEntity bcs21 = new ItemEntity(bru, double8, double9, double10, bly.split(Containers.RANDOM.nextInt(21) + 10));
            final float float22 = 0.05f;
            bcs21.setDeltaMovement(Containers.RANDOM.nextGaussian() * 0.05000000074505806, Containers.RANDOM.nextGaussian() * 0.05000000074505806 + 0.20000000298023224, Containers.RANDOM.nextGaussian() * 0.05000000074505806);
            bru.addFreshEntity(bcs21);
        }
    }
    
    static {
        RANDOM = new Random();
    }
}
