package com.mojang.realmsclient.dto;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PingResult extends ValueObject implements ReflectionBasedSerialization {
    @SerializedName("pingResults")
    public List<RegionPingResult> pingResults;
    @SerializedName("worldIds")
    public List<Long> worldIds;
    
    public PingResult() {
        this.pingResults = (List<RegionPingResult>)Lists.newArrayList();
        this.worldIds = (List<Long>)Lists.newArrayList();
    }
}
