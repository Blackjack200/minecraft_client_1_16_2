package net.minecraft.util;

import net.minecraft.Util;
import java.util.Random;
import java.util.List;

public class WeighedRandom {
    public static int getTotalWeight(final List<? extends WeighedRandomItem> list) {
        int integer2 = 0;
        for (int integer3 = 0, integer4 = list.size(); integer3 < integer4; ++integer3) {
            final WeighedRandomItem a5 = (WeighedRandomItem)list.get(integer3);
            integer2 += a5.weight;
        }
        return integer2;
    }
    
    public static <T extends WeighedRandomItem> T getRandomItem(final Random random, final List<T> list, final int integer) {
        if (integer <= 0) {
            throw Util.<IllegalArgumentException>pauseInIde(new IllegalArgumentException());
        }
        final int integer2 = random.nextInt(integer);
        return WeighedRandom.<T>getWeightedItem(list, integer2);
    }
    
    public static <T extends WeighedRandomItem> T getWeightedItem(final List<T> list, int integer) {
        for (int integer2 = 0, integer3 = list.size(); integer2 < integer3; ++integer2) {
            final T a5 = (T)list.get(integer2);
            integer -= a5.weight;
            if (integer < 0) {
                return a5;
            }
        }
        return null;
    }
    
    public static <T extends WeighedRandomItem> T getRandomItem(final Random random, final List<T> list) {
        return WeighedRandom.<T>getRandomItem(random, list, getTotalWeight(list));
    }
    
    public static class WeighedRandomItem {
        protected final int weight;
        
        public WeighedRandomItem(final int integer) {
            this.weight = integer;
        }
    }
}
