package net.minecraft;

import org.apache.logging.log4j.LogManager;
import java.io.InputStream;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.Reader;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import java.util.UUID;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import com.mojang.bridge.game.GameVersion;

public class DetectedVersion implements GameVersion {
    private static final Logger LOGGER;
    public static final GameVersion BUILT_IN;
    private final String id;
    private final String name;
    private final boolean stable;
    private final int worldVersion;
    private final int protocolVersion;
    private final int packVersion;
    private final Date buildTime;
    private final String releaseTarget;
    
    private DetectedVersion() {
        this.id = UUID.randomUUID().toString().replaceAll("-", "");
        this.name = "1.16.2";
        this.stable = true;
        this.worldVersion = 2578;
        this.protocolVersion = 751;
        this.packVersion = 6;
        this.buildTime = new Date();
        this.releaseTarget = "1.16.2";
    }
    
    private DetectedVersion(final JsonObject jsonObject) {
        this.id = GsonHelper.getAsString(jsonObject, "id");
        this.name = GsonHelper.getAsString(jsonObject, "name");
        this.releaseTarget = GsonHelper.getAsString(jsonObject, "release_target");
        this.stable = GsonHelper.getAsBoolean(jsonObject, "stable");
        this.worldVersion = GsonHelper.getAsInt(jsonObject, "world_version");
        this.protocolVersion = GsonHelper.getAsInt(jsonObject, "protocol_version");
        this.packVersion = GsonHelper.getAsInt(jsonObject, "pack_version");
        this.buildTime = Date.from(ZonedDateTime.parse((CharSequence)GsonHelper.getAsString(jsonObject, "build_time")).toInstant());
    }
    
    public static GameVersion tryDetectVersion() {
        try (final InputStream inputStream1 = DetectedVersion.class.getResourceAsStream("/version.json")) {
            if (inputStream1 == null) {
                DetectedVersion.LOGGER.warn("Missing version information!");
                return DetectedVersion.BUILT_IN;
            }
            try (final InputStreamReader inputStreamReader3 = new InputStreamReader(inputStream1)) {
                return (GameVersion)new DetectedVersion(GsonHelper.parse((Reader)inputStreamReader3));
            }
        }
        catch (IOException | JsonParseException ex2) {
            final Exception ex;
            final Exception exception1 = ex;
            throw new IllegalStateException("Game version information is corrupt", (Throwable)exception1);
        }
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getReleaseTarget() {
        return this.releaseTarget;
    }
    
    public int getWorldVersion() {
        return this.worldVersion;
    }
    
    public int getProtocolVersion() {
        return this.protocolVersion;
    }
    
    public int getPackVersion() {
        return this.packVersion;
    }
    
    public Date getBuildTime() {
        return this.buildTime;
    }
    
    public boolean isStable() {
        return this.stable;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        BUILT_IN = (GameVersion)new DetectedVersion();
    }
}
