package net.minecraft.world.item;

import java.util.Iterator;
import java.util.List;
import net.minecraft.nbt.CompoundTag;

public interface DyeableLeatherItem {
    default boolean hasCustomColor(final ItemStack bly) {
        final CompoundTag md3 = bly.getTagElement("display");
        return md3 != null && md3.contains("color", 99);
    }
    
    default int getColor(final ItemStack bly) {
        final CompoundTag md3 = bly.getTagElement("display");
        if (md3 != null && md3.contains("color", 99)) {
            return md3.getInt("color");
        }
        return 10511680;
    }
    
    default void clearColor(final ItemStack bly) {
        final CompoundTag md3 = bly.getTagElement("display");
        if (md3 != null && md3.contains("color")) {
            md3.remove("color");
        }
    }
    
    default void setColor(final ItemStack bly, final int integer) {
        bly.getOrCreateTagElement("display").putInt("color", integer);
    }
    
    default ItemStack dyeArmor(final ItemStack bly, final List<DyeItem> list) {
        ItemStack bly2 = ItemStack.EMPTY;
        final int[] arr4 = new int[3];
        int integer5 = 0;
        int integer6 = 0;
        DyeableLeatherItem bky7 = null;
        final Item blu8 = bly.getItem();
        if (blu8 instanceof DyeableLeatherItem) {
            bky7 = (DyeableLeatherItem)blu8;
            bly2 = bly.copy();
            bly2.setCount(1);
            if (bky7.hasCustomColor(bly)) {
                final int integer7 = bky7.getColor(bly2);
                final float float10 = (integer7 >> 16 & 0xFF) / 255.0f;
                final float float11 = (integer7 >> 8 & 0xFF) / 255.0f;
                final float float12 = (integer7 & 0xFF) / 255.0f;
                integer5 += (int)(Math.max(float10, Math.max(float11, float12)) * 255.0f);
                final int[] array = arr4;
                final int n = 0;
                array[n] += (int)(float10 * 255.0f);
                final int[] array2 = arr4;
                final int n2 = 1;
                array2[n2] += (int)(float11 * 255.0f);
                final int[] array3 = arr4;
                final int n3 = 2;
                array3[n3] += (int)(float12 * 255.0f);
                ++integer6;
            }
            for (final DyeItem bkv10 : list) {
                final float[] arr5 = bkv10.getDyeColor().getTextureDiffuseColors();
                final int integer8 = (int)(arr5[0] * 255.0f);
                final int integer9 = (int)(arr5[1] * 255.0f);
                final int integer10 = (int)(arr5[2] * 255.0f);
                integer5 += Math.max(integer8, Math.max(integer9, integer10));
                final int[] array4 = arr4;
                final int n4 = 0;
                array4[n4] += integer8;
                final int[] array5 = arr4;
                final int n5 = 1;
                array5[n5] += integer9;
                final int[] array6 = arr4;
                final int n6 = 2;
                array6[n6] += integer10;
                ++integer6;
            }
        }
        if (bky7 == null) {
            return ItemStack.EMPTY;
        }
        int integer7 = arr4[0] / integer6;
        int integer11 = arr4[1] / integer6;
        int integer12 = arr4[2] / integer6;
        final float float12 = integer5 / (float)integer6;
        final float float13 = (float)Math.max(integer7, Math.max(integer11, integer12));
        integer7 = (int)(integer7 * float12 / float13);
        integer11 = (int)(integer11 * float12 / float13);
        integer12 = (int)(integer12 * float12 / float13);
        int integer10 = integer7;
        integer10 = (integer10 << 8) + integer11;
        integer10 = (integer10 << 8) + integer12;
        bky7.setColor(bly2, integer10);
        return bly2;
    }
}
