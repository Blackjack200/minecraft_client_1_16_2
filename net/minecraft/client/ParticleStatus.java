package net.minecraft.client;

import java.util.Comparator;
import java.util.Arrays;
import net.minecraft.util.Mth;

public enum ParticleStatus {
    ALL(0, "options.particles.all"), 
    DECREASED(1, "options.particles.decreased"), 
    MINIMAL(2, "options.particles.minimal");
    
    private static final ParticleStatus[] BY_ID;
    private final int id;
    private final String key;
    
    private ParticleStatus(final int integer3, final String string4) {
        this.id = integer3;
        this.key = string4;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public int getId() {
        return this.id;
    }
    
    public static ParticleStatus byId(final int integer) {
        return ParticleStatus.BY_ID[Mth.positiveModulo(integer, ParticleStatus.BY_ID.length)];
    }
    
    static {
        BY_ID = (ParticleStatus[])Arrays.stream((Object[])values()).sorted(Comparator.comparingInt(ParticleStatus::getId)).toArray(ParticleStatus[]::new);
    }
}
