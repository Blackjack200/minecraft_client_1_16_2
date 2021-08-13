package net.minecraft.world.level.storage;

import com.mojang.serialization.OptionalDynamic;
import net.minecraft.SharedConstants;
import com.mojang.serialization.Dynamic;

public class LevelVersion {
    private final int levelDataVersion;
    private final long lastPlayed;
    private final String minecraftVersionName;
    private final int minecraftVersion;
    private final boolean snapshot;
    
    public LevelVersion(final int integer1, final long long2, final String string, final int integer4, final boolean boolean5) {
        this.levelDataVersion = integer1;
        this.lastPlayed = long2;
        this.minecraftVersionName = string;
        this.minecraftVersion = integer4;
        this.snapshot = boolean5;
    }
    
    public static LevelVersion parse(final Dynamic<?> dynamic) {
        final int integer2 = dynamic.get("version").asInt(0);
        final long long3 = dynamic.get("LastPlayed").asLong(0L);
        final OptionalDynamic<?> optionalDynamic5 = dynamic.get("Version");
        if (optionalDynamic5.result().isPresent()) {
            return new LevelVersion(integer2, long3, optionalDynamic5.get("Name").asString(SharedConstants.getCurrentVersion().getName()), optionalDynamic5.get("Id").asInt(SharedConstants.getCurrentVersion().getWorldVersion()), optionalDynamic5.get("Snapshot").asBoolean(!SharedConstants.getCurrentVersion().isStable()));
        }
        return new LevelVersion(integer2, long3, "", 0, false);
    }
    
    public int levelDataVersion() {
        return this.levelDataVersion;
    }
    
    public long lastPlayed() {
        return this.lastPlayed;
    }
    
    public String minecraftVersionName() {
        return this.minecraftVersionName;
    }
    
    public int minecraftVersion() {
        return this.minecraftVersion;
    }
    
    public boolean snapshot() {
        return this.snapshot;
    }
}
