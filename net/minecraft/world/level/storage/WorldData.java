package net.minecraft.world.level.storage;

import com.mojang.serialization.Lifecycle;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.LevelSettings;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReportCategory;
import java.util.Set;
import net.minecraft.world.level.DataPackConfig;

public interface WorldData {
    DataPackConfig getDataPackConfig();
    
    void setDataPackConfig(final DataPackConfig brh);
    
    boolean wasModded();
    
    Set<String> getKnownServerBrands();
    
    void setModdedInfo(final String string, final boolean boolean2);
    
    default void fillCrashReportCategory(final CrashReportCategory m) {
        m.setDetail("Known server brands", (CrashReportDetail<String>)(() -> String.join(", ", (Iterable)this.getKnownServerBrands())));
        m.setDetail("Level was modded", (CrashReportDetail<String>)(() -> Boolean.toString(this.wasModded())));
        m.setDetail("Level storage version", (CrashReportDetail<String>)(() -> {
            final int integer2 = this.getVersion();
            return String.format("0x%05X - %s", new Object[] { integer2, this.getStorageVersionName(integer2) });
        }));
    }
    
    default String getStorageVersionName(final int integer) {
        switch (integer) {
            case 19133: {
                return "Anvil";
            }
            case 19132: {
                return "McRegion";
            }
            default: {
                return "Unknown?";
            }
        }
    }
    
    @Nullable
    CompoundTag getCustomBossEvents();
    
    void setCustomBossEvents(@Nullable final CompoundTag md);
    
    ServerLevelData overworldData();
    
    LevelSettings getLevelSettings();
    
    CompoundTag createTag(final RegistryAccess gn, @Nullable final CompoundTag md);
    
    boolean isHardcore();
    
    int getVersion();
    
    String getLevelName();
    
    GameType getGameType();
    
    void setGameType(final GameType brr);
    
    boolean getAllowCommands();
    
    Difficulty getDifficulty();
    
    void setDifficulty(final Difficulty aoo);
    
    boolean isDifficultyLocked();
    
    void setDifficultyLocked(final boolean boolean1);
    
    GameRules getGameRules();
    
    CompoundTag getLoadedPlayerTag();
    
    CompoundTag endDragonFightData();
    
    void setEndDragonFightData(final CompoundTag md);
    
    WorldGenSettings worldGenSettings();
    
    Lifecycle worldGenSettingsLifecycle();
}
