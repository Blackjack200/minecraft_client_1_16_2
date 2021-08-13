package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonObject;
import java.util.List;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;

public class RealmsServerPlayerList extends ValueObject {
    private static final Logger LOGGER;
    private static final JsonParser JSON_PARSER;
    public long serverId;
    public List<String> players;
    
    public static RealmsServerPlayerList parse(final JsonObject jsonObject) {
        final RealmsServerPlayerList dgr2 = new RealmsServerPlayerList();
        try {
            dgr2.serverId = JsonUtils.getLongOr("serverId", jsonObject, -1L);
            final String string3 = JsonUtils.getStringOr("playerList", jsonObject, (String)null);
            if (string3 != null) {
                final JsonElement jsonElement4 = RealmsServerPlayerList.JSON_PARSER.parse(string3);
                if (jsonElement4.isJsonArray()) {
                    dgr2.players = parsePlayers(jsonElement4.getAsJsonArray());
                }
                else {
                    dgr2.players = (List<String>)Lists.newArrayList();
                }
            }
            else {
                dgr2.players = (List<String>)Lists.newArrayList();
            }
        }
        catch (Exception exception3) {
            RealmsServerPlayerList.LOGGER.error("Could not parse RealmsServerPlayerList: " + exception3.getMessage());
        }
        return dgr2;
    }
    
    private static List<String> parsePlayers(final JsonArray jsonArray) {
        final List<String> list2 = (List<String>)Lists.newArrayList();
        for (final JsonElement jsonElement4 : jsonArray) {
            try {
                list2.add(jsonElement4.getAsString());
            }
            catch (Exception ex) {}
        }
        return list2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        JSON_PARSER = new JsonParser();
    }
}
