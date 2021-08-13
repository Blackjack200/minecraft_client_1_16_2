package net.minecraft.world.entity;

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

public enum MobCategory implements StringRepresentable {
    MONSTER("monster", 70, false, false, 128), 
    CREATURE("creature", 10, true, true, 128), 
    AMBIENT("ambient", 15, true, false, 128), 
    WATER_CREATURE("water_creature", 5, true, false, 128), 
    WATER_AMBIENT("water_ambient", 20, true, false, 64), 
    MISC("misc", -1, true, true, 128);
    
    public static final Codec<MobCategory> CODEC;
    private static final Map<String, MobCategory> BY_NAME;
    private final int max;
    private final boolean isFriendly;
    private final boolean isPersistent;
    private final String name;
    private final int noDespawnDistance = 32;
    private final int despawnDistance;
    
    private MobCategory(final String string3, final int integer4, final boolean boolean5, final boolean boolean6, final int integer7) {
        this.name = string3;
        this.max = integer4;
        this.isFriendly = boolean5;
        this.isPersistent = boolean6;
        this.despawnDistance = integer7;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getSerializedName() {
        return this.name;
    }
    
    public static MobCategory byName(final String string) {
        return (MobCategory)MobCategory.BY_NAME.get(string);
    }
    
    public int getMaxInstancesPerChunk() {
        return this.max;
    }
    
    public boolean isFriendly() {
        return this.isFriendly;
    }
    
    public boolean isPersistent() {
        return this.isPersistent;
    }
    
    public int getDespawnDistance() {
        return this.despawnDistance;
    }
    
    public int getNoDespawnDistance() {
        return 32;
    }
    
    static {
        CODEC = StringRepresentable.<MobCategory>fromEnum((java.util.function.Supplier<MobCategory[]>)MobCategory::values, (java.util.function.Function<? super String, ? extends MobCategory>)MobCategory::byName);
        BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(MobCategory::getName, aql -> aql));
    }
}
