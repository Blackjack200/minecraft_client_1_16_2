package net.minecraft.world.entity.animal.horse;

import java.util.Comparator;
import java.util.Arrays;

public enum Markings {
    NONE(0), 
    WHITE(1), 
    WHITE_FIELD(2), 
    WHITE_DOTS(3), 
    BLACK_DOTS(4);
    
    private static final Markings[] BY_ID;
    private final int id;
    
    private Markings(final int integer3) {
        this.id = integer3;
    }
    
    public int getId() {
        return this.id;
    }
    
    public static Markings byId(final int integer) {
        return Markings.BY_ID[integer % Markings.BY_ID.length];
    }
    
    static {
        BY_ID = (Markings[])Arrays.stream((Object[])values()).sorted(Comparator.comparingInt(Markings::getId)).toArray(Markings[]::new);
    }
}
