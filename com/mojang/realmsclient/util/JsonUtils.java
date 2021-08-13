package com.mojang.realmsclient.util;

import java.util.Date;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtils {
    public static String getStringOr(final String string1, final JsonObject jsonObject, final String string3) {
        final JsonElement jsonElement4 = jsonObject.get(string1);
        if (jsonElement4 != null) {
            return jsonElement4.isJsonNull() ? string3 : jsonElement4.getAsString();
        }
        return string3;
    }
    
    public static int getIntOr(final String string, final JsonObject jsonObject, final int integer) {
        final JsonElement jsonElement4 = jsonObject.get(string);
        if (jsonElement4 != null) {
            return jsonElement4.isJsonNull() ? integer : jsonElement4.getAsInt();
        }
        return integer;
    }
    
    public static long getLongOr(final String string, final JsonObject jsonObject, final long long3) {
        final JsonElement jsonElement5 = jsonObject.get(string);
        if (jsonElement5 != null) {
            return jsonElement5.isJsonNull() ? long3 : jsonElement5.getAsLong();
        }
        return long3;
    }
    
    public static boolean getBooleanOr(final String string, final JsonObject jsonObject, final boolean boolean3) {
        final JsonElement jsonElement4 = jsonObject.get(string);
        if (jsonElement4 != null) {
            return jsonElement4.isJsonNull() ? boolean3 : jsonElement4.getAsBoolean();
        }
        return boolean3;
    }
    
    public static Date getDateOr(final String string, final JsonObject jsonObject) {
        final JsonElement jsonElement3 = jsonObject.get(string);
        if (jsonElement3 != null) {
            return new Date(Long.parseLong(jsonElement3.getAsString()));
        }
        return new Date();
    }
}
