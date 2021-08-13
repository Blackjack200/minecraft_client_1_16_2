package net.minecraft.world.level.storage;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.timers.TimerQueue;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.GameType;
import java.util.UUID;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReportCategory;

public interface ServerLevelData extends WritableLevelData {
    String getLevelName();
    
    void setThundering(final boolean boolean1);
    
    int getRainTime();
    
    void setRainTime(final int integer);
    
    void setThunderTime(final int integer);
    
    int getThunderTime();
    
    default void fillCrashReportCategory(final CrashReportCategory m) {
        super.fillCrashReportCategory(m);
        m.setDetail("Level name", (CrashReportDetail<String>)this::getLevelName);
        m.setDetail("Level game mode", (CrashReportDetail<String>)(() -> String.format("Game mode: %s (ID %d). Hardcore: %b. Cheats: %b", new Object[] { this.getGameType().getName(), this.getGameType().getId(), this.isHardcore(), this.getAllowCommands() })));
        m.setDetail("Level weather", (CrashReportDetail<String>)(() -> String.format("Rain time: %d (now: %b), thunder time: %d (now: %b)", new Object[] { this.getRainTime(), this.isRaining(), this.getThunderTime(), this.isThundering() })));
    }
    
    int getClearWeatherTime();
    
    void setClearWeatherTime(final int integer);
    
    int getWanderingTraderSpawnDelay();
    
    void setWanderingTraderSpawnDelay(final int integer);
    
    int getWanderingTraderSpawnChance();
    
    void setWanderingTraderSpawnChance(final int integer);
    
    void setWanderingTraderId(final UUID uUID);
    
    GameType getGameType();
    
    void setWorldBorder(final WorldBorder.Settings c);
    
    WorldBorder.Settings getWorldBorder();
    
    boolean isInitialized();
    
    void setInitialized(final boolean boolean1);
    
    boolean getAllowCommands();
    
    void setGameType(final GameType brr);
    
    TimerQueue<MinecraftServer> getScheduledEvents();
    
    void setGameTime(final long long1);
    
    void setDayTime(final long long1);
}
