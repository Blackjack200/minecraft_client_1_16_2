package net.minecraft.world.scores;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.ChatFormatting;
import java.util.Iterator;
import java.util.Comparator;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import java.util.List;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import java.util.Map;

public class Scoreboard {
    private final Map<String, Objective> objectivesByName;
    private final Map<ObjectiveCriteria, List<Objective>> objectivesByCriteria;
    private final Map<String, Map<Objective, Score>> playerScores;
    private final Objective[] displayObjectives;
    private final Map<String, PlayerTeam> teamsByName;
    private final Map<String, PlayerTeam> teamsByPlayer;
    private static String[] displaySlotNames;
    
    public Scoreboard() {
        this.objectivesByName = (Map<String, Objective>)Maps.newHashMap();
        this.objectivesByCriteria = (Map<ObjectiveCriteria, List<Objective>>)Maps.newHashMap();
        this.playerScores = (Map<String, Map<Objective, Score>>)Maps.newHashMap();
        this.displayObjectives = new Objective[19];
        this.teamsByName = (Map<String, PlayerTeam>)Maps.newHashMap();
        this.teamsByPlayer = (Map<String, PlayerTeam>)Maps.newHashMap();
    }
    
    public boolean hasObjective(final String string) {
        return this.objectivesByName.containsKey(string);
    }
    
    public Objective getOrCreateObjective(final String string) {
        return (Objective)this.objectivesByName.get(string);
    }
    
    @Nullable
    public Objective getObjective(@Nullable final String string) {
        return (Objective)this.objectivesByName.get(string);
    }
    
    public Objective addObjective(final String string, final ObjectiveCriteria ddn, final Component nr, final ObjectiveCriteria.RenderType a) {
        if (string.length() > 16) {
            throw new IllegalArgumentException("The objective name '" + string + "' is too long!");
        }
        if (this.objectivesByName.containsKey(string)) {
            throw new IllegalArgumentException("An objective with the name '" + string + "' already exists!");
        }
        final Objective ddh6 = new Objective(this, string, ddn, nr, a);
        ((List)this.objectivesByCriteria.computeIfAbsent(ddn, ddn -> Lists.newArrayList())).add(ddh6);
        this.objectivesByName.put(string, ddh6);
        this.onObjectiveAdded(ddh6);
        return ddh6;
    }
    
    public final void forAllObjectives(final ObjectiveCriteria ddn, final String string, final Consumer<Score> consumer) {
        ((List)this.objectivesByCriteria.getOrDefault(ddn, Collections.emptyList())).forEach(ddh -> consumer.accept(this.getOrCreatePlayerScore(string, ddh)));
    }
    
    public boolean hasPlayerScore(final String string, final Objective ddh) {
        final Map<Objective, Score> map4 = (Map<Objective, Score>)this.playerScores.get(string);
        if (map4 == null) {
            return false;
        }
        final Score ddj5 = (Score)map4.get(ddh);
        return ddj5 != null;
    }
    
    public Score getOrCreatePlayerScore(final String string, final Objective ddh) {
        if (string.length() > 40) {
            throw new IllegalArgumentException("The player name '" + string + "' is too long!");
        }
        final Map<Objective, Score> map4 = (Map<Objective, Score>)this.playerScores.computeIfAbsent(string, string -> Maps.newHashMap());
        return (Score)map4.computeIfAbsent(ddh, ddh -> {
            final Score ddj4 = new Score(this, ddh, string);
            ddj4.setScore(0);
            return ddj4;
        });
    }
    
    public Collection<Score> getPlayerScores(final Objective ddh) {
        final List<Score> list3 = (List<Score>)Lists.newArrayList();
        for (final Map<Objective, Score> map5 : this.playerScores.values()) {
            final Score ddj6 = (Score)map5.get(ddh);
            if (ddj6 != null) {
                list3.add(ddj6);
            }
        }
        list3.sort((Comparator)Score.SCORE_COMPARATOR);
        return (Collection<Score>)list3;
    }
    
    public Collection<Objective> getObjectives() {
        return (Collection<Objective>)this.objectivesByName.values();
    }
    
    public Collection<String> getObjectiveNames() {
        return (Collection<String>)this.objectivesByName.keySet();
    }
    
    public Collection<String> getTrackedPlayers() {
        return (Collection<String>)Lists.newArrayList((Iterable)this.playerScores.keySet());
    }
    
    public void resetPlayerScore(final String string, @Nullable final Objective ddh) {
        if (ddh == null) {
            final Map<Objective, Score> map4 = (Map<Objective, Score>)this.playerScores.remove(string);
            if (map4 != null) {
                this.onPlayerRemoved(string);
            }
        }
        else {
            final Map<Objective, Score> map4 = (Map<Objective, Score>)this.playerScores.get(string);
            if (map4 != null) {
                final Score ddj5 = (Score)map4.remove(ddh);
                if (map4.size() < 1) {
                    final Map<Objective, Score> map5 = (Map<Objective, Score>)this.playerScores.remove(string);
                    if (map5 != null) {
                        this.onPlayerRemoved(string);
                    }
                }
                else if (ddj5 != null) {
                    this.onPlayerScoreRemoved(string, ddh);
                }
            }
        }
    }
    
    public Map<Objective, Score> getPlayerScores(final String string) {
        Map<Objective, Score> map3 = (Map<Objective, Score>)this.playerScores.get(string);
        if (map3 == null) {
            map3 = (Map<Objective, Score>)Maps.newHashMap();
        }
        return map3;
    }
    
    public void removeObjective(final Objective ddh) {
        this.objectivesByName.remove(ddh.getName());
        for (int integer3 = 0; integer3 < 19; ++integer3) {
            if (this.getDisplayObjective(integer3) == ddh) {
                this.setDisplayObjective(integer3, null);
            }
        }
        final List<Objective> list3 = (List<Objective>)this.objectivesByCriteria.get(ddh.getCriteria());
        if (list3 != null) {
            list3.remove(ddh);
        }
        for (final Map<Objective, Score> map5 : this.playerScores.values()) {
            map5.remove(ddh);
        }
        this.onObjectiveRemoved(ddh);
    }
    
    public void setDisplayObjective(final int integer, @Nullable final Objective ddh) {
        this.displayObjectives[integer] = ddh;
    }
    
    @Nullable
    public Objective getDisplayObjective(final int integer) {
        return this.displayObjectives[integer];
    }
    
    public PlayerTeam getPlayerTeam(final String string) {
        return (PlayerTeam)this.teamsByName.get(string);
    }
    
    public PlayerTeam addPlayerTeam(final String string) {
        if (string.length() > 16) {
            throw new IllegalArgumentException("The team name '" + string + "' is too long!");
        }
        PlayerTeam ddi3 = this.getPlayerTeam(string);
        if (ddi3 != null) {
            throw new IllegalArgumentException("A team with the name '" + string + "' already exists!");
        }
        ddi3 = new PlayerTeam(this, string);
        this.teamsByName.put(string, ddi3);
        this.onTeamAdded(ddi3);
        return ddi3;
    }
    
    public void removePlayerTeam(final PlayerTeam ddi) {
        this.teamsByName.remove(ddi.getName());
        for (final String string4 : ddi.getPlayers()) {
            this.teamsByPlayer.remove(string4);
        }
        this.onTeamRemoved(ddi);
    }
    
    public boolean addPlayerToTeam(final String string, final PlayerTeam ddi) {
        if (string.length() > 40) {
            throw new IllegalArgumentException("The player name '" + string + "' is too long!");
        }
        if (this.getPlayersTeam(string) != null) {
            this.removePlayerFromTeam(string);
        }
        this.teamsByPlayer.put(string, ddi);
        return ddi.getPlayers().add(string);
    }
    
    public boolean removePlayerFromTeam(final String string) {
        final PlayerTeam ddi3 = this.getPlayersTeam(string);
        if (ddi3 != null) {
            this.removePlayerFromTeam(string, ddi3);
            return true;
        }
        return false;
    }
    
    public void removePlayerFromTeam(final String string, final PlayerTeam ddi) {
        if (this.getPlayersTeam(string) != ddi) {
            throw new IllegalStateException("Player is either on another team or not on any team. Cannot remove from team '" + ddi.getName() + "'.");
        }
        this.teamsByPlayer.remove(string);
        ddi.getPlayers().remove(string);
    }
    
    public Collection<String> getTeamNames() {
        return (Collection<String>)this.teamsByName.keySet();
    }
    
    public Collection<PlayerTeam> getPlayerTeams() {
        return (Collection<PlayerTeam>)this.teamsByName.values();
    }
    
    @Nullable
    public PlayerTeam getPlayersTeam(final String string) {
        return (PlayerTeam)this.teamsByPlayer.get(string);
    }
    
    public void onObjectiveAdded(final Objective ddh) {
    }
    
    public void onObjectiveChanged(final Objective ddh) {
    }
    
    public void onObjectiveRemoved(final Objective ddh) {
    }
    
    public void onScoreChanged(final Score ddj) {
    }
    
    public void onPlayerRemoved(final String string) {
    }
    
    public void onPlayerScoreRemoved(final String string, final Objective ddh) {
    }
    
    public void onTeamAdded(final PlayerTeam ddi) {
    }
    
    public void onTeamChanged(final PlayerTeam ddi) {
    }
    
    public void onTeamRemoved(final PlayerTeam ddi) {
    }
    
    public static String getDisplaySlotName(final int integer) {
        switch (integer) {
            case 0: {
                return "list";
            }
            case 1: {
                return "sidebar";
            }
            case 2: {
                return "belowName";
            }
            default: {
                if (integer >= 3 && integer <= 18) {
                    final ChatFormatting k2 = ChatFormatting.getById(integer - 3);
                    if (k2 != null && k2 != ChatFormatting.RESET) {
                        return "sidebar.team." + k2.getName();
                    }
                }
                return null;
            }
        }
    }
    
    public static int getDisplaySlotByName(final String string) {
        if ("list".equalsIgnoreCase(string)) {
            return 0;
        }
        if ("sidebar".equalsIgnoreCase(string)) {
            return 1;
        }
        if ("belowName".equalsIgnoreCase(string)) {
            return 2;
        }
        if (string.startsWith("sidebar.team.")) {
            final String string2 = string.substring("sidebar.team.".length());
            final ChatFormatting k3 = ChatFormatting.getByName(string2);
            if (k3 != null && k3.getId() >= 0) {
                return k3.getId() + 3;
            }
        }
        return -1;
    }
    
    public static String[] getDisplaySlotNames() {
        if (Scoreboard.displaySlotNames == null) {
            Scoreboard.displaySlotNames = new String[19];
            for (int integer1 = 0; integer1 < 19; ++integer1) {
                Scoreboard.displaySlotNames[integer1] = getDisplaySlotName(integer1);
            }
        }
        return Scoreboard.displaySlotNames;
    }
    
    public void entityRemoved(final Entity apx) {
        if (apx == null || apx instanceof Player || apx.isAlive()) {
            return;
        }
        final String string3 = apx.getStringUUID();
        this.resetPlayerScore(string3, null);
        this.removePlayerFromTeam(string3);
    }
    
    protected ListTag savePlayerScores() {
        final ListTag mj2 = new ListTag();
        this.playerScores.values().stream().map(Map::values).forEach(collection -> collection.stream().filter(ddj -> ddj.getObjective() != null).forEach(ddj -> {
            final CompoundTag md3 = new CompoundTag();
            md3.putString("Name", ddj.getOwner());
            md3.putString("Objective", ddj.getObjective().getName());
            md3.putInt("Score", ddj.getScore());
            md3.putBoolean("Locked", ddj.isLocked());
            mj2.add(md3);
        }));
        return mj2;
    }
    
    protected void loadPlayerScores(final ListTag mj) {
        for (int integer3 = 0; integer3 < mj.size(); ++integer3) {
            final CompoundTag md4 = mj.getCompound(integer3);
            final Objective ddh5 = this.getOrCreateObjective(md4.getString("Objective"));
            String string6 = md4.getString("Name");
            if (string6.length() > 40) {
                string6 = string6.substring(0, 40);
            }
            final Score ddj7 = this.getOrCreatePlayerScore(string6, ddh5);
            ddj7.setScore(md4.getInt("Score"));
            if (md4.contains("Locked")) {
                ddj7.setLocked(md4.getBoolean("Locked"));
            }
        }
    }
}
