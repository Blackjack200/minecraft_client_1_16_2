package net.minecraft.world.scores;

import java.util.stream.Collectors;
import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Map;
import java.util.Collection;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import javax.annotation.Nullable;

public abstract class Team {
    public boolean isAlliedTo(@Nullable final Team ddm) {
        return ddm != null && this == ddm;
    }
    
    public abstract String getName();
    
    public abstract MutableComponent getFormattedName(final Component nr);
    
    public abstract boolean canSeeFriendlyInvisibles();
    
    public abstract boolean isAllowFriendlyFire();
    
    public abstract Visibility getNameTagVisibility();
    
    public abstract ChatFormatting getColor();
    
    public abstract Collection<String> getPlayers();
    
    public abstract Visibility getDeathMessageVisibility();
    
    public abstract CollisionRule getCollisionRule();
    
    public enum Visibility {
        ALWAYS("always", 0), 
        NEVER("never", 1), 
        HIDE_FOR_OTHER_TEAMS("hideForOtherTeams", 2), 
        HIDE_FOR_OWN_TEAM("hideForOwnTeam", 3);
        
        private static final Map<String, Visibility> BY_NAME;
        public final String name;
        public final int id;
        
        @Nullable
        public static Visibility byName(final String string) {
            return (Visibility)Visibility.BY_NAME.get(string);
        }
        
        private Visibility(final String string3, final int integer4) {
            this.name = string3;
            this.id = integer4;
        }
        
        public Component getDisplayName() {
            return new TranslatableComponent("team.visibility." + this.name);
        }
        
        static {
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(b -> b.name, b -> b));
        }
    }
    
    public enum CollisionRule {
        ALWAYS("always", 0), 
        NEVER("never", 1), 
        PUSH_OTHER_TEAMS("pushOtherTeams", 2), 
        PUSH_OWN_TEAM("pushOwnTeam", 3);
        
        private static final Map<String, CollisionRule> BY_NAME;
        public final String name;
        public final int id;
        
        @Nullable
        public static CollisionRule byName(final String string) {
            return (CollisionRule)CollisionRule.BY_NAME.get(string);
        }
        
        private CollisionRule(final String string3, final int integer4) {
            this.name = string3;
            this.id = integer4;
        }
        
        public Component getDisplayName() {
            return new TranslatableComponent("team.collision." + this.name);
        }
        
        static {
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(a -> a.name, a -> a));
        }
    }
}
