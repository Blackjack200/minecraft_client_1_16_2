package net.minecraft.world.scores;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import java.util.function.UnaryOperator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

public class Objective {
    private final Scoreboard scoreboard;
    private final String name;
    private final ObjectiveCriteria criteria;
    private Component displayName;
    private Component formattedDisplayName;
    private ObjectiveCriteria.RenderType renderType;
    
    public Objective(final Scoreboard ddk, final String string, final ObjectiveCriteria ddn, final Component nr, final ObjectiveCriteria.RenderType a) {
        this.scoreboard = ddk;
        this.name = string;
        this.criteria = ddn;
        this.displayName = nr;
        this.formattedDisplayName = this.createFormattedDisplayName();
        this.renderType = a;
    }
    
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ObjectiveCriteria getCriteria() {
        return this.criteria;
    }
    
    public Component getDisplayName() {
        return this.displayName;
    }
    
    private Component createFormattedDisplayName() {
        return ComponentUtils.wrapInSquareBrackets(this.displayName.copy().withStyle((UnaryOperator<Style>)(ob -> ob.withHoverEvent(new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_TEXT, (T)new TextComponent(this.name))))));
    }
    
    public Component getFormattedDisplayName() {
        return this.formattedDisplayName;
    }
    
    public void setDisplayName(final Component nr) {
        this.displayName = nr;
        this.formattedDisplayName = this.createFormattedDisplayName();
        this.scoreboard.onObjectiveChanged(this);
    }
    
    public ObjectiveCriteria.RenderType getRenderType() {
        return this.renderType;
    }
    
    public void setRenderType(final ObjectiveCriteria.RenderType a) {
        this.renderType = a;
        this.scoreboard.onObjectiveChanged(this);
    }
}
