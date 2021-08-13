package com.mojang.realmsclient.dto;

import com.google.gson.annotations.SerializedName;

public class RealmsWorldResetDto extends ValueObject implements ReflectionBasedSerialization {
    @SerializedName("seed")
    private final String seed;
    @SerializedName("worldTemplateId")
    private final long worldTemplateId;
    @SerializedName("levelType")
    private final int levelType;
    @SerializedName("generateStructures")
    private final boolean generateStructures;
    
    public RealmsWorldResetDto(final String string, final long long2, final int integer, final boolean boolean4) {
        this.seed = string;
        this.worldTemplateId = long2;
        this.levelType = integer;
        this.generateStructures = boolean4;
    }
}
