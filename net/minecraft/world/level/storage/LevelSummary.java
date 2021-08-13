package net.minecraft.world.level.storage;

import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.GameType;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import java.io.File;
import net.minecraft.world.level.LevelSettings;

public class LevelSummary implements Comparable<LevelSummary> {
    private final LevelSettings settings;
    private final LevelVersion levelVersion;
    private final String levelId;
    private final boolean requiresConversion;
    private final boolean locked;
    private final File icon;
    @Nullable
    private Component info;
    
    public LevelSummary(final LevelSettings brx, final LevelVersion cyf, final String string, final boolean boolean4, final boolean boolean5, final File file) {
        this.settings = brx;
        this.levelVersion = cyf;
        this.levelId = string;
        this.locked = boolean5;
        this.icon = file;
        this.requiresConversion = boolean4;
    }
    
    public String getLevelId() {
        return this.levelId;
    }
    
    public String getLevelName() {
        return StringUtils.isEmpty((CharSequence)this.settings.levelName()) ? this.levelId : this.settings.levelName();
    }
    
    public File getIcon() {
        return this.icon;
    }
    
    public boolean isRequiresConversion() {
        return this.requiresConversion;
    }
    
    public long getLastPlayed() {
        return this.levelVersion.lastPlayed();
    }
    
    public int compareTo(final LevelSummary cye) {
        if (this.levelVersion.lastPlayed() < cye.levelVersion.lastPlayed()) {
            return 1;
        }
        if (this.levelVersion.lastPlayed() > cye.levelVersion.lastPlayed()) {
            return -1;
        }
        return this.levelId.compareTo(cye.levelId);
    }
    
    public GameType getGameMode() {
        return this.settings.gameType();
    }
    
    public boolean isHardcore() {
        return this.settings.hardcore();
    }
    
    public boolean hasCheats() {
        return this.settings.allowCommands();
    }
    
    public MutableComponent getWorldVersionName() {
        if (StringUtil.isNullOrEmpty(this.levelVersion.minecraftVersionName())) {
            return new TranslatableComponent("selectWorld.versionUnknown");
        }
        return new TextComponent(this.levelVersion.minecraftVersionName());
    }
    
    public LevelVersion levelVersion() {
        return this.levelVersion;
    }
    
    public boolean markVersionInList() {
        return this.askToOpenWorld() || (!SharedConstants.getCurrentVersion().isStable() && !this.levelVersion.snapshot()) || this.shouldBackup();
    }
    
    public boolean askToOpenWorld() {
        return this.levelVersion.minecraftVersion() > SharedConstants.getCurrentVersion().getWorldVersion();
    }
    
    public boolean shouldBackup() {
        return this.levelVersion.minecraftVersion() < SharedConstants.getCurrentVersion().getWorldVersion();
    }
    
    public boolean isLocked() {
        return this.locked;
    }
    
    public Component getInfo() {
        if (this.info == null) {
            this.info = this.createInfo();
        }
        return this.info;
    }
    
    private Component createInfo() {
        if (this.isLocked()) {
            return new TranslatableComponent("selectWorld.locked").withStyle(ChatFormatting.RED);
        }
        if (this.isRequiresConversion()) {
            return new TranslatableComponent("selectWorld.conversion");
        }
        final MutableComponent nx2 = this.isHardcore() ? new TextComponent("").append(new TranslatableComponent("gameMode.hardcore").withStyle(ChatFormatting.DARK_RED)) : new TranslatableComponent("gameMode." + this.getGameMode().getName());
        if (this.hasCheats()) {
            nx2.append(", ").append(new TranslatableComponent("selectWorld.cheats"));
        }
        final MutableComponent nx3 = this.getWorldVersionName();
        final MutableComponent nx4 = new TextComponent(", ").append(new TranslatableComponent("selectWorld.version")).append(" ");
        if (this.markVersionInList()) {
            nx4.append(nx3.withStyle(this.askToOpenWorld() ? ChatFormatting.RED : ChatFormatting.ITALIC));
        }
        else {
            nx4.append(nx3);
        }
        nx2.append(nx4);
        return nx2;
    }
}
