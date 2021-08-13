package net.minecraft.client;

import java.util.Comparator;
import java.util.Arrays;
import net.minecraft.util.Mth;

public enum AttackIndicatorStatus {
    OFF(0, "options.off"), 
    CROSSHAIR(1, "options.attack.crosshair"), 
    HOTBAR(2, "options.attack.hotbar");
    
    private static final AttackIndicatorStatus[] BY_ID;
    private final int id;
    private final String key;
    
    private AttackIndicatorStatus(final int integer3, final String string4) {
        this.id = integer3;
        this.key = string4;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public static AttackIndicatorStatus byId(final int integer) {
        return AttackIndicatorStatus.BY_ID[Mth.positiveModulo(integer, AttackIndicatorStatus.BY_ID.length)];
    }
    
    static {
        BY_ID = (AttackIndicatorStatus[])Arrays.stream((Object[])values()).sorted(Comparator.comparingInt(AttackIndicatorStatus::getId)).toArray(AttackIndicatorStatus[]::new);
    }
}
