package net.minecraft.world.level;

import net.minecraft.world.entity.player.Abilities;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;

public enum GameType {
    NOT_SET(-1, ""), 
    SURVIVAL(0, "survival"), 
    CREATIVE(1, "creative"), 
    ADVENTURE(2, "adventure"), 
    SPECTATOR(3, "spectator");
    
    private final int id;
    private final String name;
    
    private GameType(final int integer3, final String string4) {
        this.id = integer3;
        this.name = string4;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Component getDisplayName() {
        return new TranslatableComponent("gameMode." + this.name);
    }
    
    public void updatePlayerAbilities(final Abilities bfq) {
        if (this == GameType.CREATIVE) {
            bfq.mayfly = true;
            bfq.instabuild = true;
            bfq.invulnerable = true;
        }
        else if (this == GameType.SPECTATOR) {
            bfq.mayfly = true;
            bfq.instabuild = false;
            bfq.invulnerable = true;
            bfq.flying = true;
        }
        else {
            bfq.mayfly = false;
            bfq.instabuild = false;
            bfq.invulnerable = false;
            bfq.flying = false;
        }
        bfq.mayBuild = !this.isBlockPlacingRestricted();
    }
    
    public boolean isBlockPlacingRestricted() {
        return this == GameType.ADVENTURE || this == GameType.SPECTATOR;
    }
    
    public boolean isCreative() {
        return this == GameType.CREATIVE;
    }
    
    public boolean isSurvival() {
        return this == GameType.SURVIVAL || this == GameType.ADVENTURE;
    }
    
    public static GameType byId(final int integer) {
        return byId(integer, GameType.SURVIVAL);
    }
    
    public static GameType byId(final int integer, final GameType brr) {
        for (final GameType brr2 : values()) {
            if (brr2.id == integer) {
                return brr2;
            }
        }
        return brr;
    }
    
    public static GameType byName(final String string) {
        return byName(string, GameType.SURVIVAL);
    }
    
    public static GameType byName(final String string, final GameType brr) {
        for (final GameType brr2 : values()) {
            if (brr2.name.equals(string)) {
                return brr2;
            }
        }
        return brr;
    }
}
