package net.minecraft.network.chat;

import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.server.MinecraftServer;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import com.mojang.brigadier.StringReader;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.selector.EntitySelector;

public class ScoreComponent extends BaseComponent implements ContextAwareComponent {
    private final String name;
    @Nullable
    private final EntitySelector selector;
    private final String objective;
    
    @Nullable
    private static EntitySelector parseSelector(final String string) {
        try {
            return new EntitySelectorParser(new StringReader(string)).parse();
        }
        catch (CommandSyntaxException ex) {
            return null;
        }
    }
    
    public ScoreComponent(final String string1, final String string2) {
        this(string1, parseSelector(string1), string2);
    }
    
    private ScoreComponent(final String string1, @Nullable final EntitySelector fc, final String string3) {
        this.name = string1;
        this.selector = fc;
        this.objective = string3;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getObjective() {
        return this.objective;
    }
    
    private String findTargetName(final CommandSourceStack db) throws CommandSyntaxException {
        if (this.selector != null) {
            final List<? extends Entity> list3 = this.selector.findEntities(db);
            if (!list3.isEmpty()) {
                if (list3.size() != 1) {
                    throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
                }
                return ((Entity)list3.get(0)).getScoreboardName();
            }
        }
        return this.name;
    }
    
    private String getScore(final String string, final CommandSourceStack db) {
        final MinecraftServer minecraftServer4 = db.getServer();
        if (minecraftServer4 != null) {
            final Scoreboard ddk5 = minecraftServer4.getScoreboard();
            final Objective ddh6 = ddk5.getObjective(this.objective);
            if (ddk5.hasPlayerScore(string, ddh6)) {
                final Score ddj7 = ddk5.getOrCreatePlayerScore(string, ddh6);
                return Integer.toString(ddj7.getScore());
            }
        }
        return "";
    }
    
    @Override
    public ScoreComponent plainCopy() {
        return new ScoreComponent(this.name, this.selector, this.objective);
    }
    
    @Override
    public MutableComponent resolve(@Nullable final CommandSourceStack db, @Nullable final Entity apx, final int integer) throws CommandSyntaxException {
        if (db == null) {
            return new TextComponent("");
        }
        final String string5 = this.findTargetName(db);
        final String string6 = (apx != null && string5.equals("*")) ? apx.getScoreboardName() : string5;
        return new TextComponent(this.getScore(string6, db));
    }
    
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof ScoreComponent) {
            final ScoreComponent nz3 = (ScoreComponent)object;
            return this.name.equals(nz3.name) && this.objective.equals(nz3.objective) && super.equals(object);
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "ScoreComponent{name='" + this.name + '\'' + "objective='" + this.objective + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }
}
