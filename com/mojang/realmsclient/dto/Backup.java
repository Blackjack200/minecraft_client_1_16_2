package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.Set;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Date;
import org.apache.logging.log4j.Logger;

public class Backup extends ValueObject {
    private static final Logger LOGGER;
    public String backupId;
    public Date lastModifiedDate;
    public long size;
    private boolean uploadedVersion;
    public Map<String, String> metadata;
    public Map<String, String> changeList;
    
    public Backup() {
        this.metadata = (Map<String, String>)Maps.newHashMap();
        this.changeList = (Map<String, String>)Maps.newHashMap();
    }
    
    public static Backup parse(final JsonElement jsonElement) {
        final JsonObject jsonObject2 = jsonElement.getAsJsonObject();
        final Backup dgd3 = new Backup();
        try {
            dgd3.backupId = JsonUtils.getStringOr("backupId", jsonObject2, "");
            dgd3.lastModifiedDate = JsonUtils.getDateOr("lastModifiedDate", jsonObject2);
            dgd3.size = JsonUtils.getLongOr("size", jsonObject2, 0L);
            if (jsonObject2.has("metadata")) {
                final JsonObject jsonObject3 = jsonObject2.getAsJsonObject("metadata");
                final Set<Map.Entry<String, JsonElement>> set5 = (Set<Map.Entry<String, JsonElement>>)jsonObject3.entrySet();
                for (final Map.Entry<String, JsonElement> entry7 : set5) {
                    if (!((JsonElement)entry7.getValue()).isJsonNull()) {
                        dgd3.metadata.put(format((String)entry7.getKey()), ((JsonElement)entry7.getValue()).getAsString());
                    }
                }
            }
        }
        catch (Exception exception4) {
            Backup.LOGGER.error("Could not parse Backup: " + exception4.getMessage());
        }
        return dgd3;
    }
    
    private static String format(final String string) {
        final String[] arr2 = string.split("_");
        final StringBuilder stringBuilder3 = new StringBuilder();
        for (final String string2 : arr2) {
            if (string2 != null && string2.length() >= 1) {
                if ("of".equals(string2)) {
                    stringBuilder3.append(string2).append(" ");
                }
                else {
                    final char character8 = Character.toUpperCase(string2.charAt(0));
                    stringBuilder3.append(character8).append(string2.substring(1)).append(" ");
                }
            }
        }
        return stringBuilder3.toString();
    }
    
    public boolean isUploadedVersion() {
        return this.uploadedVersion;
    }
    
    public void setUploadedVersion(final boolean boolean1) {
        this.uploadedVersion = boolean1;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
