package net.minecraft.world.level.storage;

import net.minecraft.CrashReportCategory;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.timers.TimerQueue;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.GameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GameType;

public class DerivedLevelData implements ServerLevelData {
    private final WorldData worldData;
    private final ServerLevelData wrapped;
    
    public DerivedLevelData(final WorldData cyk, final ServerLevelData cyj) {
        this.worldData = cyk;
        this.wrapped = cyj;
    }
    
    public int getXSpawn() {
        return this.wrapped.getXSpawn();
    }
    
    public int getYSpawn() {
        return this.wrapped.getYSpawn();
    }
    
    public int getZSpawn() {
        return this.wrapped.getZSpawn();
    }
    
    public float getSpawnAngle() {
        return this.wrapped.getSpawnAngle();
    }
    
    public long getGameTime() {
        return this.wrapped.getGameTime();
    }
    
    public long getDayTime() {
        return this.wrapped.getDayTime();
    }
    
    public String getLevelName() {
        return this.worldData.getLevelName();
    }
    
    public int getClearWeatherTime() {
        return this.wrapped.getClearWeatherTime();
    }
    
    public void setClearWeatherTime(final int integer) {
    }
    
    public boolean isThundering() {
        return this.wrapped.isThundering();
    }
    
    public int getThunderTime() {
        return this.wrapped.getThunderTime();
    }
    
    public boolean isRaining() {
        return this.wrapped.isRaining();
    }
    
    public int getRainTime() {
        return this.wrapped.getRainTime();
    }
    
    public GameType getGameType() {
        return this.worldData.getGameType();
    }
    
    public void setXSpawn(final int integer) {
    }
    
    public void setYSpawn(final int integer) {
    }
    
    public void setZSpawn(final int integer) {
    }
    
    public void setSpawnAngle(final float float1) {
    }
    
    public void setGameTime(final long long1) {
    }
    
    public void setDayTime(final long long1) {
    }
    
    public void setSpawn(final BlockPos fx, final float float2) {
    }
    
    public void setThundering(final boolean boolean1) {
    }
    
    public void setThunderTime(final int integer) {
    }
    
    public void setRaining(final boolean boolean1) {
    }
    
    public void setRainTime(final int integer) {
    }
    
    public void setGameType(final GameType brr) {
    }
    
    public boolean isHardcore() {
        return this.worldData.isHardcore();
    }
    
    public boolean getAllowCommands() {
        return this.worldData.getAllowCommands();
    }
    
    public boolean isInitialized() {
        return this.wrapped.isInitialized();
    }
    
    public void setInitialized(final boolean boolean1) {
    }
    
    public GameRules getGameRules() {
        return this.worldData.getGameRules();
    }
    
    public WorldBorder.Settings getWorldBorder() {
        return this.wrapped.getWorldBorder();
    }
    
    public void setWorldBorder(final WorldBorder.Settings c) {
    }
    
    public Difficulty getDifficulty() {
        return this.worldData.getDifficulty();
    }
    
    public boolean isDifficultyLocked() {
        return this.worldData.isDifficultyLocked();
    }
    
    public TimerQueue<MinecraftServer> getScheduledEvents() {
        return this.wrapped.getScheduledEvents();
    }
    
    public int getWanderingTraderSpawnDelay() {
        return 0;
    }
    
    public void setWanderingTraderSpawnDelay(final int integer) {
    }
    
    public int getWanderingTraderSpawnChance() {
        return 0;
    }
    
    public void setWanderingTraderSpawnChance(final int integer) {
    }
    
    public void setWanderingTraderId(final UUID uUID) {
    }
    
    public void fillCrashReportCategory(final CrashReportCategory m) {
        m.setDetail("Derived", true);
        this.wrapped.fillCrashReportCategory(m);
    }
}
