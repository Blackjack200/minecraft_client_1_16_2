package com.mojang.realmsclient.util;

import com.google.gson.annotations.SerializedName;
import net.minecraft.client.Minecraft;
import com.mojang.realmsclient.dto.ReflectionBasedSerialization;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import java.nio.charset.StandardCharsets;
import com.mojang.realmsclient.dto.GuardedSerializer;

public class RealmsPersistence {
    private static final GuardedSerializer GSON;
    
    public static RealmsPersistenceData readFile() {
        final File file1 = getPathToData();
        try {
            return RealmsPersistence.GSON.<RealmsPersistenceData>fromJson(FileUtils.readFileToString(file1, StandardCharsets.UTF_8), RealmsPersistenceData.class);
        }
        catch (IOException iOException2) {
            return new RealmsPersistenceData();
        }
    }
    
    public static void writeFile(final RealmsPersistenceData a) {
        final File file2 = getPathToData();
        try {
            FileUtils.writeStringToFile(file2, RealmsPersistence.GSON.toJson(a), StandardCharsets.UTF_8);
        }
        catch (IOException ex) {}
    }
    
    private static File getPathToData() {
        return new File(Minecraft.getInstance().gameDirectory, "realms_persistence.json");
    }
    
    static {
        GSON = new GuardedSerializer();
    }
    
    public static class RealmsPersistenceData implements ReflectionBasedSerialization {
        @SerializedName("newsLink")
        public String newsLink;
        @SerializedName("hasUnreadNews")
        public boolean hasUnreadNews;
    }
}
