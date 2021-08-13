package net.minecraft.util.datafix;

import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.DSL;

public enum DataFixTypes {
    LEVEL(References.LEVEL), 
    PLAYER(References.PLAYER), 
    CHUNK(References.CHUNK), 
    HOTBAR(References.HOTBAR), 
    OPTIONS(References.OPTIONS), 
    STRUCTURE(References.STRUCTURE), 
    STATS(References.STATS), 
    SAVED_DATA(References.SAVED_DATA), 
    ADVANCEMENTS(References.ADVANCEMENTS), 
    POI_CHUNK(References.POI_CHUNK), 
    WORLD_GEN_SETTINGS(References.WORLD_GEN_SETTINGS);
    
    private final DSL.TypeReference type;
    
    private DataFixTypes(final DSL.TypeReference typeReference) {
        this.type = typeReference;
    }
    
    public DSL.TypeReference getType() {
        return this.type;
    }
}
