package net.minecraft.world.entity.decoration;

import net.minecraft.core.Registry;

public class Motive {
    public static final Motive KEBAB;
    public static final Motive AZTEC;
    public static final Motive ALBAN;
    public static final Motive AZTEC2;
    public static final Motive BOMB;
    public static final Motive PLANT;
    public static final Motive WASTELAND;
    public static final Motive POOL;
    public static final Motive COURBET;
    public static final Motive SEA;
    public static final Motive SUNSET;
    public static final Motive CREEBET;
    public static final Motive WANDERER;
    public static final Motive GRAHAM;
    public static final Motive MATCH;
    public static final Motive BUST;
    public static final Motive STAGE;
    public static final Motive VOID;
    public static final Motive SKULL_AND_ROSES;
    public static final Motive WITHER;
    public static final Motive FIGHTERS;
    public static final Motive POINTER;
    public static final Motive PIGSCENE;
    public static final Motive BURNING_SKULL;
    public static final Motive SKELETON;
    public static final Motive DONKEY_KONG;
    private final int width;
    private final int height;
    
    private static Motive register(final String string, final int integer2, final int integer3) {
        return Registry.<Motive>register(Registry.MOTIVE, string, new Motive(integer2, integer3));
    }
    
    public Motive(final int integer1, final int integer2) {
        this.width = integer1;
        this.height = integer2;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    static {
        KEBAB = register("kebab", 16, 16);
        AZTEC = register("aztec", 16, 16);
        ALBAN = register("alban", 16, 16);
        AZTEC2 = register("aztec2", 16, 16);
        BOMB = register("bomb", 16, 16);
        PLANT = register("plant", 16, 16);
        WASTELAND = register("wasteland", 16, 16);
        POOL = register("pool", 32, 16);
        COURBET = register("courbet", 32, 16);
        SEA = register("sea", 32, 16);
        SUNSET = register("sunset", 32, 16);
        CREEBET = register("creebet", 32, 16);
        WANDERER = register("wanderer", 16, 32);
        GRAHAM = register("graham", 16, 32);
        MATCH = register("match", 32, 32);
        BUST = register("bust", 32, 32);
        STAGE = register("stage", 32, 32);
        VOID = register("void", 32, 32);
        SKULL_AND_ROSES = register("skull_and_roses", 32, 32);
        WITHER = register("wither", 32, 32);
        FIGHTERS = register("fighters", 64, 32);
        POINTER = register("pointer", 64, 64);
        PIGSCENE = register("pigscene", 64, 64);
        BURNING_SKULL = register("burning_skull", 64, 64);
        SKELETON = register("skeleton", 64, 48);
        DONKEY_KONG = register("donkey_kong", 64, 48);
    }
}
