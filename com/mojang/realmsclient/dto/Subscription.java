package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;

public class Subscription extends ValueObject {
    private static final Logger LOGGER;
    public long startDate;
    public int daysLeft;
    public SubscriptionType type;
    
    public Subscription() {
        this.type = SubscriptionType.NORMAL;
    }
    
    public static Subscription parse(final String string) {
        final Subscription dgx2 = new Subscription();
        try {
            final JsonParser jsonParser3 = new JsonParser();
            final JsonObject jsonObject4 = jsonParser3.parse(string).getAsJsonObject();
            dgx2.startDate = JsonUtils.getLongOr("startDate", jsonObject4, 0L);
            dgx2.daysLeft = JsonUtils.getIntOr("daysLeft", jsonObject4, 0);
            dgx2.type = typeFrom(JsonUtils.getStringOr("subscriptionType", jsonObject4, SubscriptionType.NORMAL.name()));
        }
        catch (Exception exception3) {
            Subscription.LOGGER.error("Could not parse Subscription: " + exception3.getMessage());
        }
        return dgx2;
    }
    
    private static SubscriptionType typeFrom(final String string) {
        try {
            return SubscriptionType.valueOf(string);
        }
        catch (Exception exception2) {
            return SubscriptionType.NORMAL;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public enum SubscriptionType {
        NORMAL, 
        RECURRING;
    }
}
