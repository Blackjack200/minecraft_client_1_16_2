package net.minecraft.world.scores;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.saveddata.SavedData;

public class ScoreboardSaveData extends SavedData {
    private static final Logger LOGGER;
    private Scoreboard scoreboard;
    private CompoundTag delayLoad;
    
    public ScoreboardSaveData() {
        super("scoreboard");
    }
    
    public void setScoreboard(final Scoreboard ddk) {
        this.scoreboard = ddk;
        if (this.delayLoad != null) {
            this.load(this.delayLoad);
        }
    }
    
    @Override
    public void load(final CompoundTag md) {
        if (this.scoreboard == null) {
            this.delayLoad = md;
            return;
        }
        this.loadObjectives(md.getList("Objectives", 10));
        this.scoreboard.loadPlayerScores(md.getList("PlayerScores", 10));
        if (md.contains("DisplaySlots", 10)) {
            this.loadDisplaySlots(md.getCompound("DisplaySlots"));
        }
        if (md.contains("Teams", 9)) {
            this.loadTeams(md.getList("Teams", 10));
        }
    }
    
    protected void loadTeams(final ListTag mj) {
        for (int integer3 = 0; integer3 < mj.size(); ++integer3) {
            final CompoundTag md4 = mj.getCompound(integer3);
            String string5 = md4.getString("Name");
            if (string5.length() > 16) {
                string5 = string5.substring(0, 16);
            }
            final PlayerTeam ddi6 = this.scoreboard.addPlayerTeam(string5);
            final Component nr7 = Component.Serializer.fromJson(md4.getString("DisplayName"));
            if (nr7 != null) {
                ddi6.setDisplayName(nr7);
            }
            if (md4.contains("TeamColor", 8)) {
                ddi6.setColor(ChatFormatting.getByName(md4.getString("TeamColor")));
            }
            if (md4.contains("AllowFriendlyFire", 99)) {
                ddi6.setAllowFriendlyFire(md4.getBoolean("AllowFriendlyFire"));
            }
            if (md4.contains("SeeFriendlyInvisibles", 99)) {
                ddi6.setSeeFriendlyInvisibles(md4.getBoolean("SeeFriendlyInvisibles"));
            }
            if (md4.contains("MemberNamePrefix", 8)) {
                final Component nr8 = Component.Serializer.fromJson(md4.getString("MemberNamePrefix"));
                if (nr8 != null) {
                    ddi6.setPlayerPrefix(nr8);
                }
            }
            if (md4.contains("MemberNameSuffix", 8)) {
                final Component nr8 = Component.Serializer.fromJson(md4.getString("MemberNameSuffix"));
                if (nr8 != null) {
                    ddi6.setPlayerSuffix(nr8);
                }
            }
            if (md4.contains("NameTagVisibility", 8)) {
                final Team.Visibility b8 = Team.Visibility.byName(md4.getString("NameTagVisibility"));
                if (b8 != null) {
                    ddi6.setNameTagVisibility(b8);
                }
            }
            if (md4.contains("DeathMessageVisibility", 8)) {
                final Team.Visibility b8 = Team.Visibility.byName(md4.getString("DeathMessageVisibility"));
                if (b8 != null) {
                    ddi6.setDeathMessageVisibility(b8);
                }
            }
            if (md4.contains("CollisionRule", 8)) {
                final Team.CollisionRule a8 = Team.CollisionRule.byName(md4.getString("CollisionRule"));
                if (a8 != null) {
                    ddi6.setCollisionRule(a8);
                }
            }
            this.loadTeamPlayers(ddi6, md4.getList("Players", 8));
        }
    }
    
    protected void loadTeamPlayers(final PlayerTeam ddi, final ListTag mj) {
        for (int integer4 = 0; integer4 < mj.size(); ++integer4) {
            this.scoreboard.addPlayerToTeam(mj.getString(integer4), ddi);
        }
    }
    
    protected void loadDisplaySlots(final CompoundTag md) {
        for (int integer3 = 0; integer3 < 19; ++integer3) {
            if (md.contains(new StringBuilder().append("slot_").append(integer3).toString(), 8)) {
                final String string4 = md.getString(new StringBuilder().append("slot_").append(integer3).toString());
                final Objective ddh5 = this.scoreboard.getObjective(string4);
                this.scoreboard.setDisplayObjective(integer3, ddh5);
            }
        }
    }
    
    protected void loadObjectives(final ListTag mj) {
        for (int integer3 = 0; integer3 < mj.size(); ++integer3) {
            final CompoundTag md4 = mj.getCompound(integer3);
            ObjectiveCriteria.byName(md4.getString("CriteriaName")).ifPresent(ddn -> {
                String string4 = md4.getString("Name");
                if (string4.length() > 16) {
                    string4 = string4.substring(0, 16);
                }
                final Component nr5 = Component.Serializer.fromJson(md4.getString("DisplayName"));
                final ObjectiveCriteria.RenderType a6 = ObjectiveCriteria.RenderType.byId(md4.getString("RenderType"));
                this.scoreboard.addObjective(string4, ddn, nr5, a6);
            });
        }
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        if (this.scoreboard == null) {
            ScoreboardSaveData.LOGGER.warn("Tried to save scoreboard without having a scoreboard...");
            return md;
        }
        md.put("Objectives", (Tag)this.saveObjectives());
        md.put("PlayerScores", (Tag)this.scoreboard.savePlayerScores());
        md.put("Teams", (Tag)this.saveTeams());
        this.saveDisplaySlots(md);
        return md;
    }
    
    protected ListTag saveTeams() {
        final ListTag mj2 = new ListTag();
        final Collection<PlayerTeam> collection3 = this.scoreboard.getPlayerTeams();
        for (final PlayerTeam ddi5 : collection3) {
            final CompoundTag md6 = new CompoundTag();
            md6.putString("Name", ddi5.getName());
            md6.putString("DisplayName", Component.Serializer.toJson(ddi5.getDisplayName()));
            if (ddi5.getColor().getId() >= 0) {
                md6.putString("TeamColor", ddi5.getColor().getName());
            }
            md6.putBoolean("AllowFriendlyFire", ddi5.isAllowFriendlyFire());
            md6.putBoolean("SeeFriendlyInvisibles", ddi5.canSeeFriendlyInvisibles());
            md6.putString("MemberNamePrefix", Component.Serializer.toJson(ddi5.getPlayerPrefix()));
            md6.putString("MemberNameSuffix", Component.Serializer.toJson(ddi5.getPlayerSuffix()));
            md6.putString("NameTagVisibility", ddi5.getNameTagVisibility().name);
            md6.putString("DeathMessageVisibility", ddi5.getDeathMessageVisibility().name);
            md6.putString("CollisionRule", ddi5.getCollisionRule().name);
            final ListTag mj3 = new ListTag();
            for (final String string9 : ddi5.getPlayers()) {
                mj3.add(StringTag.valueOf(string9));
            }
            md6.put("Players", (Tag)mj3);
            mj2.add(md6);
        }
        return mj2;
    }
    
    protected void saveDisplaySlots(final CompoundTag md) {
        final CompoundTag md2 = new CompoundTag();
        boolean boolean4 = false;
        for (int integer5 = 0; integer5 < 19; ++integer5) {
            final Objective ddh6 = this.scoreboard.getDisplayObjective(integer5);
            if (ddh6 != null) {
                md2.putString(new StringBuilder().append("slot_").append(integer5).toString(), ddh6.getName());
                boolean4 = true;
            }
        }
        if (boolean4) {
            md.put("DisplaySlots", (Tag)md2);
        }
    }
    
    protected ListTag saveObjectives() {
        final ListTag mj2 = new ListTag();
        final Collection<Objective> collection3 = this.scoreboard.getObjectives();
        for (final Objective ddh5 : collection3) {
            if (ddh5.getCriteria() == null) {
                continue;
            }
            final CompoundTag md6 = new CompoundTag();
            md6.putString("Name", ddh5.getName());
            md6.putString("CriteriaName", ddh5.getCriteria().getName());
            md6.putString("DisplayName", Component.Serializer.toJson(ddh5.getDisplayName()));
            md6.putString("RenderType", ddh5.getRenderType().getId());
            mj2.add(md6);
        }
        return mj2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
