package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;

public class RealmsServerAddress extends ValueObject {
    private static final Logger LOGGER;
    public String address;
    public String resourcePackUrl;
    public String resourcePackHash;
    
    public static RealmsServerAddress parse(final String string) {
        final JsonParser jsonParser2 = new JsonParser();
        final RealmsServerAddress dgo3 = new RealmsServerAddress();
        try {
            final JsonObject jsonObject4 = jsonParser2.parse(string).getAsJsonObject();
            dgo3.address = JsonUtils.getStringOr("address", jsonObject4, (String)null);
            dgo3.resourcePackUrl = JsonUtils.getStringOr("resourcePackUrl", jsonObject4, (String)null);
            dgo3.resourcePackHash = JsonUtils.getStringOr("resourcePackHash", jsonObject4, (String)null);
        }
        catch (Exception exception4) {
            RealmsServerAddress.LOGGER.error("Could not parse RealmsServerAddress: " + exception4.getMessage());
        }
        return dgo3;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
