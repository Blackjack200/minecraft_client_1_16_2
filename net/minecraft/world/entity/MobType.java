package net.minecraft.world.entity;

public class MobType {
    public static final MobType UNDEFINED;
    public static final MobType UNDEAD;
    public static final MobType ARTHROPOD;
    public static final MobType ILLAGER;
    public static final MobType WATER;
    
    static {
        UNDEFINED = new MobType();
        UNDEAD = new MobType();
        ARTHROPOD = new MobType();
        ILLAGER = new MobType();
        WATER = new MobType();
    }
}
