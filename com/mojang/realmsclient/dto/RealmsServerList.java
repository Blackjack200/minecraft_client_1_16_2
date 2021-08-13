package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class RealmsServerList extends ValueObject {
    private static final Logger LOGGER;
    public List<RealmsServer> servers;
    
    public static RealmsServerList parse(final String string) {
        final RealmsServerList dgp2 = new RealmsServerList();
        dgp2.servers = (List<RealmsServer>)Lists.newArrayList();
        try {
            final JsonParser jsonParser3 = new JsonParser();
            final JsonObject jsonObject4 = jsonParser3.parse(string).getAsJsonObject();
            if (jsonObject4.get("servers").isJsonArray()) {
                final JsonArray jsonArray5 = jsonObject4.get("servers").getAsJsonArray();
                final Iterator<JsonElement> iterator6 = (Iterator<JsonElement>)jsonArray5.iterator();
                while (iterator6.hasNext()) {
                    dgp2.servers.add(RealmsServer.parse(((JsonElement)iterator6.next()).getAsJsonObject()));
                }
            }
        }
        catch (Exception exception3) {
            RealmsServerList.LOGGER.error("Could not parse McoServerList: " + exception3.getMessage());
        }
        return dgp2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
