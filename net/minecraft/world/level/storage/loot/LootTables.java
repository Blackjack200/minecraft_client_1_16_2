package net.minecraft.world.level.storage.loot;

import org.apache.logging.log4j.LogManager;
import java.util.Set;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.function.Function;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import com.google.gson.JsonElement;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;

public class LootTables extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER;
    private static final Gson GSON;
    private Map<ResourceLocation, LootTable> tables;
    private final PredicateManager predicateManager;
    
    public LootTables(final PredicateManager cyx) {
        super(LootTables.GSON, "loot_tables");
        this.tables = (Map<ResourceLocation, LootTable>)ImmutableMap.of();
        this.predicateManager = cyx;
    }
    
    public LootTable get(final ResourceLocation vk) {
        return (LootTable)this.tables.getOrDefault(vk, LootTable.EMPTY);
    }
    
    @Override
    protected void apply(final Map<ResourceLocation, JsonElement> map, final ResourceManager acf, final ProfilerFiller ant) {
        final ImmutableMap.Builder<ResourceLocation, LootTable> builder5 = (ImmutableMap.Builder<ResourceLocation, LootTable>)ImmutableMap.builder();
        final JsonElement jsonElement6 = (JsonElement)map.remove(BuiltInLootTables.EMPTY);
        if (jsonElement6 != null) {
            LootTables.LOGGER.warn("Datapack tried to redefine {} loot table, ignoring", BuiltInLootTables.EMPTY);
        }
        map.forEach((vk, jsonElement) -> {
            try {
                final LootTable cyv4 = (LootTable)LootTables.GSON.fromJson(jsonElement, (Class)LootTable.class);
                builder5.put(vk, cyv4);
            }
            catch (Exception exception4) {
                LootTables.LOGGER.error("Couldn't parse loot table {}", vk, exception4);
            }
        });
        builder5.put(BuiltInLootTables.EMPTY, LootTable.EMPTY);
        final ImmutableMap<ResourceLocation, LootTable> immutableMap7 = (ImmutableMap<ResourceLocation, LootTable>)builder5.build();
        final ValidationContext czd8 = new ValidationContext(LootContextParamSets.ALL_PARAMS, (Function<ResourceLocation, LootItemCondition>)this.predicateManager::get, (Function<ResourceLocation, LootTable>)immutableMap7::get);
        immutableMap7.forEach((vk, cyv) -> validate(czd8, vk, cyv));
        czd8.getProblems().forEach((string1, string2) -> LootTables.LOGGER.warn("Found validation problem in " + string1 + ": " + string2));
        this.tables = (Map<ResourceLocation, LootTable>)immutableMap7;
    }
    
    public static void validate(final ValidationContext czd, final ResourceLocation vk, final LootTable cyv) {
        cyv.validate(czd.setParams(cyv.getParamSet()).enterTable(new StringBuilder().append("{").append(vk).append("}").toString(), vk));
    }
    
    public static JsonElement serialize(final LootTable cyv) {
        return LootTables.GSON.toJsonTree(cyv);
    }
    
    public Set<ResourceLocation> getIds() {
        return (Set<ResourceLocation>)this.tables.keySet();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = Deserializers.createLootTableSerializer().create();
    }
}
