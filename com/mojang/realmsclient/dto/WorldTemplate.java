package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonObject;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;

public class WorldTemplate extends ValueObject {
    private static final Logger LOGGER;
    public String id;
    public String name;
    public String version;
    public String author;
    public String link;
    @Nullable
    public String image;
    public String trailer;
    public String recommendedPlayers;
    public WorldTemplateType type;
    
    public WorldTemplate() {
        this.id = "";
        this.name = "";
        this.version = "";
        this.author = "";
        this.link = "";
        this.trailer = "";
        this.recommendedPlayers = "";
        this.type = WorldTemplateType.WORLD_TEMPLATE;
    }
    
    public static WorldTemplate parse(final JsonObject jsonObject) {
        final WorldTemplate dhb2 = new WorldTemplate();
        try {
            dhb2.id = JsonUtils.getStringOr("id", jsonObject, "");
            dhb2.name = JsonUtils.getStringOr("name", jsonObject, "");
            dhb2.version = JsonUtils.getStringOr("version", jsonObject, "");
            dhb2.author = JsonUtils.getStringOr("author", jsonObject, "");
            dhb2.link = JsonUtils.getStringOr("link", jsonObject, "");
            dhb2.image = JsonUtils.getStringOr("image", jsonObject, (String)null);
            dhb2.trailer = JsonUtils.getStringOr("trailer", jsonObject, "");
            dhb2.recommendedPlayers = JsonUtils.getStringOr("recommendedPlayers", jsonObject, "");
            dhb2.type = WorldTemplateType.valueOf(JsonUtils.getStringOr("type", jsonObject, WorldTemplateType.WORLD_TEMPLATE.name()));
        }
        catch (Exception exception3) {
            WorldTemplate.LOGGER.error("Could not parse WorldTemplate: " + exception3.getMessage());
        }
        return dhb2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public enum WorldTemplateType {
        WORLD_TEMPLATE, 
        MINIGAME, 
        ADVENTUREMAP, 
        EXPERIENCE, 
        INSPIRATION;
    }
}
