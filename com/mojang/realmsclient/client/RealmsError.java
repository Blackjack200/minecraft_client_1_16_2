package com.mojang.realmsclient.client;

import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;

public class RealmsError {
    private static final Logger LOGGER;
    private final String errorMessage;
    private final int errorCode;
    
    private RealmsError(final String string, final int integer) {
        this.errorMessage = string;
        this.errorCode = integer;
    }
    
    public static RealmsError create(final String string) {
        try {
            final JsonParser jsonParser2 = new JsonParser();
            final JsonObject jsonObject3 = jsonParser2.parse(string).getAsJsonObject();
            final String string2 = JsonUtils.getStringOr("errorMsg", jsonObject3, "");
            final int integer5 = JsonUtils.getIntOr("errorCode", jsonObject3, -1);
            return new RealmsError(string2, integer5);
        }
        catch (Exception exception2) {
            RealmsError.LOGGER.error("Could not parse RealmsError: " + exception2.getMessage());
            RealmsError.LOGGER.error("The error was: " + string);
            return new RealmsError("Failed to parse response from server", -1);
        }
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public int getErrorCode() {
        return this.errorCode;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
