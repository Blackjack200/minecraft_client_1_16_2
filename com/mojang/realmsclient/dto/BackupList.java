package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.common.collect.Lists;
import com.google.gson.JsonParser;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class BackupList extends ValueObject {
    private static final Logger LOGGER;
    public List<Backup> backups;
    
    public static BackupList parse(final String string) {
        final JsonParser jsonParser2 = new JsonParser();
        final BackupList dge3 = new BackupList();
        dge3.backups = (List<Backup>)Lists.newArrayList();
        try {
            final JsonElement jsonElement4 = jsonParser2.parse(string).getAsJsonObject().get("backups");
            if (jsonElement4.isJsonArray()) {
                final Iterator<JsonElement> iterator5 = (Iterator<JsonElement>)jsonElement4.getAsJsonArray().iterator();
                while (iterator5.hasNext()) {
                    dge3.backups.add(Backup.parse((JsonElement)iterator5.next()));
                }
            }
        }
        catch (Exception exception4) {
            BackupList.LOGGER.error("Could not parse BackupList: " + exception4.getMessage());
        }
        return dge3;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
