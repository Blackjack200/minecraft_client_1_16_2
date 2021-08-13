package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class WorldTemplatePaginatedList extends ValueObject {
    private static final Logger LOGGER;
    public List<WorldTemplate> templates;
    public int page;
    public int size;
    public int total;
    
    public WorldTemplatePaginatedList() {
    }
    
    public WorldTemplatePaginatedList(final int integer) {
        this.templates = (List<WorldTemplate>)Collections.emptyList();
        this.page = 0;
        this.size = integer;
        this.total = -1;
    }
    
    public static WorldTemplatePaginatedList parse(final String string) {
        final WorldTemplatePaginatedList dhc2 = new WorldTemplatePaginatedList();
        dhc2.templates = (List<WorldTemplate>)Lists.newArrayList();
        try {
            final JsonParser jsonParser3 = new JsonParser();
            final JsonObject jsonObject4 = jsonParser3.parse(string).getAsJsonObject();
            if (jsonObject4.get("templates").isJsonArray()) {
                final Iterator<JsonElement> iterator5 = (Iterator<JsonElement>)jsonObject4.get("templates").getAsJsonArray().iterator();
                while (iterator5.hasNext()) {
                    dhc2.templates.add(WorldTemplate.parse(((JsonElement)iterator5.next()).getAsJsonObject()));
                }
            }
            dhc2.page = JsonUtils.getIntOr("page", jsonObject4, 0);
            dhc2.size = JsonUtils.getIntOr("size", jsonObject4, 0);
            dhc2.total = JsonUtils.getIntOr("total", jsonObject4, 0);
        }
        catch (Exception exception3) {
            WorldTemplatePaginatedList.LOGGER.error("Could not parse WorldTemplatePaginatedList: " + exception3.getMessage());
        }
        return dhc2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
