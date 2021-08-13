package net.minecraft.world.level.storage;

import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReportCategory;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;

public interface LevelData {
    int getXSpawn();
    
    int getYSpawn();
    
    int getZSpawn();
    
    float getSpawnAngle();
    
    long getGameTime();
    
    long getDayTime();
    
    boolean isThundering();
    
    boolean isRaining();
    
    void setRaining(final boolean boolean1);
    
    boolean isHardcore();
    
    GameRules getGameRules();
    
    Difficulty getDifficulty();
    
    boolean isDifficultyLocked();
    
    default void fillCrashReportCategory(final CrashReportCategory m) {
        m.setDetail("Level spawn location", (CrashReportDetail<String>)(() -> CrashReportCategory.formatLocation(this.getXSpawn(), this.getYSpawn(), this.getZSpawn())));
        m.setDetail("Level time", (CrashReportDetail<String>)(() -> String.format("%d game time, %d day time", new Object[] { this.getGameTime(), this.getDayTime() })));
    }
}
