package net.minecraft.advancements.critereon;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.storage.loot.LootTable;
import java.util.function.Function;
import net.minecraft.world.level.storage.loot.ValidationContext;
import com.google.gson.JsonElement;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import com.google.gson.JsonArray;
import net.minecraft.world.level.storage.loot.Deserializers;
import com.google.gson.Gson;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class DeserializationContext {
    private static final Logger LOGGER;
    private final ResourceLocation id;
    private final PredicateManager predicateManager;
    private final Gson predicateGson;
    
    public DeserializationContext(final ResourceLocation vk, final PredicateManager cyx) {
        this.predicateGson = Deserializers.createConditionSerializer().create();
        this.id = vk;
        this.predicateManager = cyx;
    }
    
    public final LootItemCondition[] deserializeConditions(final JsonArray jsonArray, final String string, final LootContextParamSet dax) {
        final LootItemCondition[] arr5 = (LootItemCondition[])this.predicateGson.fromJson((JsonElement)jsonArray, (Class)LootItemCondition[].class);
        final ValidationContext czd6 = new ValidationContext(dax, (Function<ResourceLocation, LootItemCondition>)this.predicateManager::get, (Function<ResourceLocation, LootTable>)(vk -> null));
        for (final LootItemCondition dbl10 : arr5) {
            dbl10.validate(czd6);
            czd6.getProblems().forEach((string2, string3) -> DeserializationContext.LOGGER.warn("Found validation problem in advancement trigger {}/{}: {}", string, string2, string3));
        }
        return arr5;
    }
    
    public ResourceLocation getAdvancementId() {
        return this.id;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
