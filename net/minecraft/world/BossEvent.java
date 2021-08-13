package net.minecraft.world;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import java.util.UUID;

public abstract class BossEvent {
    private final UUID id;
    protected Component name;
    protected float percent;
    protected BossBarColor color;
    protected BossBarOverlay overlay;
    protected boolean darkenScreen;
    protected boolean playBossMusic;
    protected boolean createWorldFog;
    
    public BossEvent(final UUID uUID, final Component nr, final BossBarColor a, final BossBarOverlay b) {
        this.id = uUID;
        this.name = nr;
        this.color = a;
        this.overlay = b;
        this.percent = 1.0f;
    }
    
    public UUID getId() {
        return this.id;
    }
    
    public Component getName() {
        return this.name;
    }
    
    public void setName(final Component nr) {
        this.name = nr;
    }
    
    public float getPercent() {
        return this.percent;
    }
    
    public void setPercent(final float float1) {
        this.percent = float1;
    }
    
    public BossBarColor getColor() {
        return this.color;
    }
    
    public void setColor(final BossBarColor a) {
        this.color = a;
    }
    
    public BossBarOverlay getOverlay() {
        return this.overlay;
    }
    
    public void setOverlay(final BossBarOverlay b) {
        this.overlay = b;
    }
    
    public boolean shouldDarkenScreen() {
        return this.darkenScreen;
    }
    
    public BossEvent setDarkenScreen(final boolean boolean1) {
        this.darkenScreen = boolean1;
        return this;
    }
    
    public boolean shouldPlayBossMusic() {
        return this.playBossMusic;
    }
    
    public BossEvent setPlayBossMusic(final boolean boolean1) {
        this.playBossMusic = boolean1;
        return this;
    }
    
    public BossEvent setCreateWorldFog(final boolean boolean1) {
        this.createWorldFog = boolean1;
        return this;
    }
    
    public boolean shouldCreateWorldFog() {
        return this.createWorldFog;
    }
    
    public enum BossBarColor {
        PINK("pink", ChatFormatting.RED), 
        BLUE("blue", ChatFormatting.BLUE), 
        RED("red", ChatFormatting.DARK_RED), 
        GREEN("green", ChatFormatting.GREEN), 
        YELLOW("yellow", ChatFormatting.YELLOW), 
        PURPLE("purple", ChatFormatting.DARK_BLUE), 
        WHITE("white", ChatFormatting.WHITE);
        
        private final String name;
        private final ChatFormatting formatting;
        
        private BossBarColor(final String string3, final ChatFormatting k) {
            this.name = string3;
            this.formatting = k;
        }
        
        public ChatFormatting getFormatting() {
            return this.formatting;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static BossBarColor byName(final String string) {
            for (final BossBarColor a5 : values()) {
                if (a5.name.equals(string)) {
                    return a5;
                }
            }
            return BossBarColor.WHITE;
        }
    }
    
    public enum BossBarOverlay {
        PROGRESS("progress"), 
        NOTCHED_6("notched_6"), 
        NOTCHED_10("notched_10"), 
        NOTCHED_12("notched_12"), 
        NOTCHED_20("notched_20");
        
        private final String name;
        
        private BossBarOverlay(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static BossBarOverlay byName(final String string) {
            for (final BossBarOverlay b5 : values()) {
                if (b5.name.equals(string)) {
                    return b5;
                }
            }
            return BossBarOverlay.PROGRESS;
        }
    }
}
