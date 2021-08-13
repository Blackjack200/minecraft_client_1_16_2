package com.mojang.realmsclient.dto;

import com.google.gson.annotations.SerializedName;

public class RealmsDescriptionDto extends ValueObject implements ReflectionBasedSerialization {
    @SerializedName("name")
    public String name;
    @SerializedName("description")
    public String description;
    
    public RealmsDescriptionDto(final String string1, final String string2) {
        this.name = string1;
        this.description = string2;
    }
}
