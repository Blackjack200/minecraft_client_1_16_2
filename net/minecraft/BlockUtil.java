package net.minecraft;

import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.ints.IntStack;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Vec3i;
import java.util.function.Predicate;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;

public class BlockUtil {
    public static FoundRectangle getLargestRectangleAround(final BlockPos fx, final Direction.Axis a2, final int integer3, final Direction.Axis a4, final int integer5, final Predicate<BlockPos> predicate) {
        final BlockPos.MutableBlockPos a5 = fx.mutable();
        final Direction gc8 = Direction.get(Direction.AxisDirection.NEGATIVE, a2);
        final Direction gc9 = gc8.getOpposite();
        final Direction gc10 = Direction.get(Direction.AxisDirection.NEGATIVE, a4);
        final Direction gc11 = gc10.getOpposite();
        final int integer6 = getLimit(predicate, a5.set(fx), gc8, integer3);
        final int integer7 = getLimit(predicate, a5.set(fx), gc9, integer3);
        final int integer8 = integer6;
        final IntBounds[] arr15 = new IntBounds[integer8 + 1 + integer7];
        arr15[integer8] = new IntBounds(getLimit(predicate, a5.set(fx), gc10, integer5), getLimit(predicate, a5.set(fx), gc11, integer5));
        final int integer9 = arr15[integer8].min;
        for (int integer10 = 1; integer10 <= integer6; ++integer10) {
            final IntBounds b18 = arr15[integer8 - (integer10 - 1)];
            arr15[integer8 - integer10] = new IntBounds(getLimit(predicate, a5.set(fx).move(gc8, integer10), gc10, b18.min), getLimit(predicate, a5.set(fx).move(gc8, integer10), gc11, b18.max));
        }
        for (int integer10 = 1; integer10 <= integer7; ++integer10) {
            final IntBounds b18 = arr15[integer8 + integer10 - 1];
            arr15[integer8 + integer10] = new IntBounds(getLimit(predicate, a5.set(fx).move(gc9, integer10), gc10, b18.min), getLimit(predicate, a5.set(fx).move(gc9, integer10), gc11, b18.max));
        }
        int integer10 = 0;
        int integer11 = 0;
        int integer12 = 0;
        int integer13 = 0;
        final int[] arr16 = new int[arr15.length];
        for (int integer14 = integer9; integer14 >= 0; --integer14) {
            for (int integer15 = 0; integer15 < arr15.length; ++integer15) {
                final IntBounds b19 = arr15[integer15];
                final int integer16 = integer9 - b19.min;
                final int integer17 = integer9 + b19.max;
                arr16[integer15] = ((integer14 >= integer16 && integer14 <= integer17) ? (integer17 + 1 - integer14) : 0);
            }
            final Pair<IntBounds, Integer> pair23 = getMaxRectangleLocation(arr16);
            final IntBounds b19 = (IntBounds)pair23.getFirst();
            final int integer16 = 1 + b19.max - b19.min;
            final int integer17 = (int)pair23.getSecond();
            if (integer16 * integer17 > integer12 * integer13) {
                integer10 = b19.min;
                integer11 = integer14;
                integer12 = integer16;
                integer13 = integer17;
            }
        }
        return new FoundRectangle(fx.relative(a2, integer10 - integer8).relative(a4, integer11 - integer9), integer12, integer13);
    }
    
    private static int getLimit(final Predicate<BlockPos> predicate, final BlockPos.MutableBlockPos a, final Direction gc, final int integer) {
        int integer2;
        for (integer2 = 0; integer2 < integer && predicate.test(a.move(gc)); ++integer2) {}
        return integer2;
    }
    
    @VisibleForTesting
    static Pair<IntBounds, Integer> getMaxRectangleLocation(final int[] arr) {
        int integer2 = 0;
        int integer3 = 0;
        int integer4 = 0;
        final IntStack intStack5 = (IntStack)new IntArrayList();
        intStack5.push(0);
        for (int integer5 = 1; integer5 <= arr.length; ++integer5) {
            final int integer6 = (integer5 == arr.length) ? 0 : arr[integer5];
            while (!intStack5.isEmpty()) {
                final int integer7 = arr[intStack5.topInt()];
                if (integer6 >= integer7) {
                    intStack5.push(integer5);
                    break;
                }
                intStack5.popInt();
                final int integer8 = intStack5.isEmpty() ? 0 : (intStack5.topInt() + 1);
                if (integer7 * (integer5 - integer8) <= integer4 * (integer3 - integer2)) {
                    continue;
                }
                integer3 = integer5;
                integer2 = integer8;
                integer4 = integer7;
            }
            if (intStack5.isEmpty()) {
                intStack5.push(integer5);
            }
        }
        return (Pair<IntBounds, Integer>)new Pair(new IntBounds(integer2, integer3 - 1), integer4);
    }
    
    public static class IntBounds {
        public final int min;
        public final int max;
        
        public IntBounds(final int integer1, final int integer2) {
            this.min = integer1;
            this.max = integer2;
        }
        
        public String toString() {
            return new StringBuilder().append("IntBounds{min=").append(this.min).append(", max=").append(this.max).append('}').toString();
        }
    }
    
    public static class FoundRectangle {
        public final BlockPos minCorner;
        public final int axis1Size;
        public final int axis2Size;
        
        public FoundRectangle(final BlockPos fx, final int integer2, final int integer3) {
            this.minCorner = fx;
            this.axis1Size = integer2;
            this.axis2Size = integer3;
        }
    }
}
