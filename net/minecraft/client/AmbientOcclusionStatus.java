package net.minecraft.client;

import java.util.Comparator;
import java.util.Arrays;
import net.minecraft.util.Mth;

public enum AmbientOcclusionStatus {
    OFF(0, "options.ao.off"), 
    MIN(1, "options.ao.min"), 
    MAX(2, "options.ao.max");
    
    private static final AmbientOcclusionStatus[] BY_ID;
    private final int id;
    private final String key;
    
    private AmbientOcclusionStatus(final int integer3, final String string4) {
        this.id = integer3;
        this.key = string4;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public static AmbientOcclusionStatus byId(final int integer) {
        return AmbientOcclusionStatus.BY_ID[Mth.positiveModulo(integer, AmbientOcclusionStatus.BY_ID.length)];
    }
    
    static {
        BY_ID = (AmbientOcclusionStatus[])Arrays.stream((Object[])values()).sorted(Comparator.comparingInt(AmbientOcclusionStatus::getId)).toArray(AmbientOcclusionStatus[]::new);
    }
}
