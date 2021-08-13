package net.minecraft.world.scores;

import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.TextComponent;
import com.google.common.collect.Sets;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import java.util.Set;

public class PlayerTeam extends Team {
    private final Scoreboard scoreboard;
    private final String name;
    private final Set<String> players;
    private Component displayName;
    private Component playerPrefix;
    private Component playerSuffix;
    private boolean allowFriendlyFire;
    private boolean seeFriendlyInvisibles;
    private Visibility nameTagVisibility;
    private Visibility deathMessageVisibility;
    private ChatFormatting color;
    private CollisionRule collisionRule;
    private final Style displayNameStyle;
    
    public PlayerTeam(final Scoreboard ddk, final String string) {
        this.players = (Set<String>)Sets.newHashSet();
        this.playerPrefix = TextComponent.EMPTY;
        this.playerSuffix = TextComponent.EMPTY;
        this.allowFriendlyFire = true;
        this.seeFriendlyInvisibles = true;
        this.nameTagVisibility = Visibility.ALWAYS;
        this.deathMessageVisibility = Visibility.ALWAYS;
        this.color = ChatFormatting.RESET;
        this.collisionRule = CollisionRule.ALWAYS;
        this.scoreboard = ddk;
        this.name = string;
        this.displayName = new TextComponent(string);
        this.displayNameStyle = Style.EMPTY.withInsertion(string).withHoverEvent(new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_TEXT, (T)new TextComponent(string)));
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    public Component getDisplayName() {
        return this.displayName;
    }
    
    public MutableComponent getFormattedDisplayName() {
        final MutableComponent nx2 = ComponentUtils.wrapInSquareBrackets(this.displayName.copy().withStyle(this.displayNameStyle));
        final ChatFormatting k3 = this.getColor();
        if (k3 != ChatFormatting.RESET) {
            nx2.withStyle(k3);
        }
        return nx2;
    }
    
    public void setDisplayName(final Component nr) {
        if (nr == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        this.displayName = nr;
        this.scoreboard.onTeamChanged(this);
    }
    
    public void setPlayerPrefix(@Nullable final Component nr) {
        this.playerPrefix = ((nr == null) ? TextComponent.EMPTY : nr);
        this.scoreboard.onTeamChanged(this);
    }
    
    public Component getPlayerPrefix() {
        return this.playerPrefix;
    }
    
    public void setPlayerSuffix(@Nullable final Component nr) {
        this.playerSuffix = ((nr == null) ? TextComponent.EMPTY : nr);
        this.scoreboard.onTeamChanged(this);
    }
    
    public Component getPlayerSuffix() {
        return this.playerSuffix;
    }
    
    @Override
    public Collection<String> getPlayers() {
        return (Collection<String>)this.players;
    }
    
    @Override
    public MutableComponent getFormattedName(final Component nr) {
        final MutableComponent nx3 = new TextComponent("").append(this.playerPrefix).append(nr).append(this.playerSuffix);
        final ChatFormatting k4 = this.getColor();
        if (k4 != ChatFormatting.RESET) {
            nx3.withStyle(k4);
        }
        return nx3;
    }
    
    public static MutableComponent formatNameForTeam(@Nullable final Team ddm, final Component nr) {
        if (ddm == null) {
            return nr.copy();
        }
        return ddm.getFormattedName(nr);
    }
    
    @Override
    public boolean isAllowFriendlyFire() {
        return this.allowFriendlyFire;
    }
    
    public void setAllowFriendlyFire(final boolean boolean1) {
        this.allowFriendlyFire = boolean1;
        this.scoreboard.onTeamChanged(this);
    }
    
    @Override
    public boolean canSeeFriendlyInvisibles() {
        return this.seeFriendlyInvisibles;
    }
    
    public void setSeeFriendlyInvisibles(final boolean boolean1) {
        this.seeFriendlyInvisibles = boolean1;
        this.scoreboard.onTeamChanged(this);
    }
    
    @Override
    public Visibility getNameTagVisibility() {
        return this.nameTagVisibility;
    }
    
    @Override
    public Visibility getDeathMessageVisibility() {
        return this.deathMessageVisibility;
    }
    
    public void setNameTagVisibility(final Visibility b) {
        this.nameTagVisibility = b;
        this.scoreboard.onTeamChanged(this);
    }
    
    public void setDeathMessageVisibility(final Visibility b) {
        this.deathMessageVisibility = b;
        this.scoreboard.onTeamChanged(this);
    }
    
    @Override
    public CollisionRule getCollisionRule() {
        return this.collisionRule;
    }
    
    public void setCollisionRule(final CollisionRule a) {
        this.collisionRule = a;
        this.scoreboard.onTeamChanged(this);
    }
    
    public int packOptions() {
        int integer2 = 0;
        if (this.isAllowFriendlyFire()) {
            integer2 |= 0x1;
        }
        if (this.canSeeFriendlyInvisibles()) {
            integer2 |= 0x2;
        }
        return integer2;
    }
    
    public void unpackOptions(final int integer) {
        this.setAllowFriendlyFire((integer & 0x1) > 0);
        this.setSeeFriendlyInvisibles((integer & 0x2) > 0);
    }
    
    public void setColor(final ChatFormatting k) {
        this.color = k;
        this.scoreboard.onTeamChanged(this);
    }
    
    @Override
    public ChatFormatting getColor() {
        return this.color;
    }
}
