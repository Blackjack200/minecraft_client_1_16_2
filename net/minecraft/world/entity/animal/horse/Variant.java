package net.minecraft.world.entity.animal.horse;

import java.util.Comparator;
import java.util.Arrays;

public enum Variant {
    WHITE(0), 
    CREAMY(1), 
    CHESTNUT(2), 
    BROWN(3), 
    BLACK(4), 
    GRAY(5), 
    DARKBROWN(6);
    
    private static final Variant[] BY_ID;
    private final int id;
    
    private Variant(final int integer3) {
        this.id = integer3;
    }
    
    public int getId() {
        return this.id;
    }
    
    public static Variant byId(final int integer) {
        return Variant.BY_ID[integer % Variant.BY_ID.length];
    }
    
    static {
        BY_ID = (Variant[])Arrays.stream((Object[])values()).sorted(Comparator.comparingInt(Variant::getId)).toArray(Variant[]::new);
    }
}
