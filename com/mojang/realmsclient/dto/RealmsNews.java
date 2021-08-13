package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;

public class RealmsNews extends ValueObject {
    private static final Logger LOGGER;
    public String newsLink;
    
    public static RealmsNews parse(final String string) {
        final RealmsNews dgm2 = new RealmsNews();
        try {
            final JsonParser jsonParser3 = new JsonParser();
            final JsonObject jsonObject4 = jsonParser3.parse(string).getAsJsonObject();
            dgm2.newsLink = JsonUtils.getStringOr("newsLink", jsonObject4, (String)null);
        }
        catch (Exception exception3) {
            RealmsNews.LOGGER.error("Could not parse RealmsNews: " + exception3.getMessage());
        }
        return dgm2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
