package net.minecraft.recipebook;

import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.ShapedRecipe;
import java.util.Iterator;
import net.minecraft.world.item.crafting.Recipe;

public interface PlaceRecipe<T> {
    default void placeRecipe(final int integer1, final int integer2, final int integer3, final Recipe<?> bon, final Iterator<T> iterator, final int integer6) {
        int integer7 = integer1;
        int integer8 = integer2;
        if (bon instanceof ShapedRecipe) {
            final ShapedRecipe bos10 = (ShapedRecipe)bon;
            integer7 = bos10.getWidth();
            integer8 = bos10.getHeight();
        }
        int integer9 = 0;
        for (int integer10 = 0; integer10 < integer2; ++integer10) {
            if (integer9 == integer3) {
                ++integer9;
            }
            boolean boolean12 = integer8 < integer2 / 2.0f;
            int integer11 = Mth.floor(integer2 / 2.0f - integer8 / 2.0f);
            if (boolean12 && integer11 > integer10) {
                integer9 += integer1;
                ++integer10;
            }
            for (int integer12 = 0; integer12 < integer1; ++integer12) {
                if (!iterator.hasNext()) {
                    return;
                }
                boolean12 = (integer7 < integer1 / 2.0f);
                integer11 = Mth.floor(integer1 / 2.0f - integer7 / 2.0f);
                int integer13 = integer7;
                boolean boolean13 = integer12 < integer7;
                if (boolean12) {
                    integer13 = integer11 + integer7;
                    boolean13 = (integer11 <= integer12 && integer12 < integer11 + integer7);
                }
                if (boolean13) {
                    this.addItemToSlot(iterator, integer9, integer6, integer10, integer12);
                }
                else if (integer13 == integer12) {
                    integer9 += integer1 - integer12;
                    break;
                }
                ++integer9;
            }
        }
    }
    
    void addItemToSlot(final Iterator<T> iterator, final int integer2, final int integer3, final int integer4, final int integer5);
}
