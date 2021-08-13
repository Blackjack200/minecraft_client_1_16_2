package net.minecraft.client;

import java.util.Comparator;
import java.util.Arrays;
import net.minecraft.util.Mth;

public enum CloudStatus {
    OFF(0, "options.off"), 
    FAST(1, "options.clouds.fast"), 
    FANCY(2, "options.clouds.fancy");
    
    private static final CloudStatus[] BY_ID;
    private final int id;
    private final String key;
    
    private CloudStatus(final int integer3, final String string4) {
        this.id = integer3;
        this.key = string4;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public static CloudStatus byId(final int integer) {
        return CloudStatus.BY_ID[Mth.positiveModulo(integer, CloudStatus.BY_ID.length)];
    }
    
    static {
        BY_ID = (CloudStatus[])Arrays.stream((Object[])values()).sorted(Comparator.comparingInt(CloudStatus::getId)).toArray(CloudStatus[]::new);
    }
}
