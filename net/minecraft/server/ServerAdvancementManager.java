package net.minecraft.server;

import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.util.GsonHelper;
import java.util.Collection;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.advancements.TreeNodePosition;
import net.minecraft.advancements.Advancement;
import com.google.common.collect.Maps;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.advancements.AdvancementList;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;

public class ServerAdvancementManager extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER;
    private static final Gson GSON;
    private AdvancementList advancements;
    private final PredicateManager predicateManager;
    
    public ServerAdvancementManager(final PredicateManager cyx) {
        super(ServerAdvancementManager.GSON, "advancements");
        this.advancements = new AdvancementList();
        this.predicateManager = cyx;
    }
    
    @Override
    protected void apply(final Map<ResourceLocation, JsonElement> map, final ResourceManager acf, final ProfilerFiller ant) {
        final Map<ResourceLocation, Advancement.Builder> map2 = (Map<ResourceLocation, Advancement.Builder>)Maps.newHashMap();
        map.forEach((vk, jsonElement) -> {
            try {
                final JsonObject jsonObject5 = GsonHelper.convertToJsonObject(jsonElement, "advancement");
                final Advancement.Builder a6 = Advancement.Builder.fromJson(jsonObject5, new DeserializationContext(vk, this.predicateManager));
                map2.put(vk, a6);
            }
            catch (JsonParseException | IllegalArgumentException ex2) {
                final RuntimeException ex;
                final RuntimeException runtimeException5 = ex;
                ServerAdvancementManager.LOGGER.error("Parsing error loading custom advancement {}: {}", vk, runtimeException5.getMessage());
            }
        });
        final AdvancementList z6 = new AdvancementList();
        z6.add(map2);
        for (final Advancement y8 : z6.getRoots()) {
            if (y8.getDisplay() != null) {
                TreeNodePosition.run(y8);
            }
        }
        this.advancements = z6;
    }
    
    @Nullable
    public Advancement getAdvancement(final ResourceLocation vk) {
        return this.advancements.get(vk);
    }
    
    public Collection<Advancement> getAllAdvancements() {
        return this.advancements.getAllAdvancements();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = new GsonBuilder().create();
    }
}
