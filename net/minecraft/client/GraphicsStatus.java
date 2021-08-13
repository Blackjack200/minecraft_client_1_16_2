package net.minecraft.client;

import java.util.Comparator;
import java.util.Arrays;
import net.minecraft.util.Mth;

public enum GraphicsStatus {
    FAST(0, "options.graphics.fast"), 
    FANCY(1, "options.graphics.fancy"), 
    FABULOUS(2, "options.graphics.fabulous");
    
    private static final GraphicsStatus[] BY_ID;
    private final int id;
    private final String key;
    
    private GraphicsStatus(final int integer3, final String string4) {
        this.id = integer3;
        this.key = string4;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public GraphicsStatus cycleNext() {
        return byId(this.getId() + 1);
    }
    
    public String toString() {
        switch (this) {
            case FAST: {
                return "fast";
            }
            case FANCY: {
                return "fancy";
            }
            case FABULOUS: {
                return "fabulous";
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
    
    public static GraphicsStatus byId(final int integer) {
        return GraphicsStatus.BY_ID[Mth.positiveModulo(integer, GraphicsStatus.BY_ID.length)];
    }
    
    static {
        BY_ID = (GraphicsStatus[])Arrays.stream((Object[])values()).sorted(Comparator.comparingInt(GraphicsStatus::getId)).toArray(GraphicsStatus[]::new);
    }
}
