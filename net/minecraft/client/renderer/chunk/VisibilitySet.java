package net.minecraft.client.renderer.chunk;

import java.util.Iterator;
import net.minecraft.core.Direction;
import java.util.Set;
import java.util.BitSet;

public class VisibilitySet {
    private static final int FACINGS;
    private final BitSet data;
    
    public VisibilitySet() {
        this.data = new BitSet(VisibilitySet.FACINGS * VisibilitySet.FACINGS);
    }
    
    public void add(final Set<Direction> set) {
        for (final Direction gc4 : set) {
            for (final Direction gc5 : set) {
                this.set(gc4, gc5, true);
            }
        }
    }
    
    public void set(final Direction gc1, final Direction gc2, final boolean boolean3) {
        this.data.set(gc1.ordinal() + gc2.ordinal() * VisibilitySet.FACINGS, boolean3);
        this.data.set(gc2.ordinal() + gc1.ordinal() * VisibilitySet.FACINGS, boolean3);
    }
    
    public void setAll(final boolean boolean1) {
        this.data.set(0, this.data.size(), boolean1);
    }
    
    public boolean visibilityBetween(final Direction gc1, final Direction gc2) {
        return this.data.get(gc1.ordinal() + gc2.ordinal() * VisibilitySet.FACINGS);
    }
    
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(' ');
        for (final Direction gc6 : Direction.values()) {
            stringBuilder2.append(' ').append(gc6.toString().toUpperCase().charAt(0));
        }
        stringBuilder2.append('\n');
        for (final Direction gc6 : Direction.values()) {
            stringBuilder2.append(gc6.toString().toUpperCase().charAt(0));
            for (final Direction gc7 : Direction.values()) {
                if (gc6 == gc7) {
                    stringBuilder2.append("  ");
                }
                else {
                    final boolean boolean11 = this.visibilityBetween(gc6, gc7);
                    stringBuilder2.append(' ').append(boolean11 ? 'Y' : 'n');
                }
            }
            stringBuilder2.append('\n');
        }
        return stringBuilder2.toString();
    }
    
    static {
        FACINGS = Direction.values().length;
    }
}
