package com.mojang.realmsclient.dto;

import java.util.Locale;
import com.google.gson.annotations.SerializedName;

public class RegionPingResult extends ValueObject implements ReflectionBasedSerialization {
    @SerializedName("regionName")
    private final String regionName;
    @SerializedName("ping")
    private final int ping;
    
    public RegionPingResult(final String string, final int integer) {
        this.regionName = string;
        this.ping = integer;
    }
    
    public int ping() {
        return this.ping;
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s --> %.2f ms", new Object[] { this.regionName, Float.valueOf(this.ping) });
    }
}
