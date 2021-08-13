package net.minecraft.world;

import java.util.Comparator;
import java.util.Arrays;
import javax.annotation.Nullable;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;

public enum Difficulty {
    PEACEFUL(0, "peaceful"), 
    EASY(1, "easy"), 
    NORMAL(2, "normal"), 
    HARD(3, "hard");
    
    private static final Difficulty[] BY_ID;
    private final int id;
    private final String key;
    
    private Difficulty(final int integer3, final String string4) {
        this.id = integer3;
        this.key = string4;
    }
    
    public int getId() {
        return this.id;
    }
    
    public Component getDisplayName() {
        return new TranslatableComponent("options.difficulty." + this.key);
    }
    
    public static Difficulty byId(final int integer) {
        return Difficulty.BY_ID[integer % Difficulty.BY_ID.length];
    }
    
    @Nullable
    public static Difficulty byName(final String string) {
        for (final Difficulty aoo5 : values()) {
            if (aoo5.key.equals(string)) {
                return aoo5;
            }
        }
        return null;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public Difficulty nextById() {
        return Difficulty.BY_ID[(this.id + 1) % Difficulty.BY_ID.length];
    }
    
    static {
        BY_ID = (Difficulty[])Arrays.stream((Object[])values()).sorted(Comparator.comparingInt(Difficulty::getId)).toArray(Difficulty[]::new);
    }
}
