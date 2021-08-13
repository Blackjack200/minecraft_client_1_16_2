package net.minecraft.world.level.storage.loot;

import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import java.util.function.Predicate;
import org.apache.logging.log4j.LogManager;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import com.google.gson.JsonElement;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;

public class PredicateManager extends SimpleJsonResourceReloadListener {
    private static final Logger LOGGER;
    private static final Gson GSON;
    private Map<ResourceLocation, LootItemCondition> conditions;
    
    public PredicateManager() {
        super(PredicateManager.GSON, "predicates");
        this.conditions = (Map<ResourceLocation, LootItemCondition>)ImmutableMap.of();
    }
    
    @Nullable
    public LootItemCondition get(final ResourceLocation vk) {
        return (LootItemCondition)this.conditions.get(vk);
    }
    
    @Override
    protected void apply(final Map<ResourceLocation, JsonElement> map, final ResourceManager acf, final ProfilerFiller ant) {
        final ImmutableMap.Builder<ResourceLocation, LootItemCondition> builder5 = (ImmutableMap.Builder<ResourceLocation, LootItemCondition>)ImmutableMap.builder();
        map.forEach((vk, jsonElement) -> {
            try {
                if (jsonElement.isJsonArray()) {
                    final LootItemCondition[] arr4 = (LootItemCondition[])PredicateManager.GSON.fromJson(jsonElement, (Class)LootItemCondition[].class);
                    builder5.put(vk, new CompositePredicate(arr4));
                }
                else {
                    final LootItemCondition dbl4 = (LootItemCondition)PredicateManager.GSON.fromJson(jsonElement, (Class)LootItemCondition.class);
                    builder5.put(vk, dbl4);
                }
            }
            catch (Exception exception4) {
                PredicateManager.LOGGER.error("Couldn't parse loot table {}", vk, exception4);
            }
        });
        final Map<ResourceLocation, LootItemCondition> map2 = (Map<ResourceLocation, LootItemCondition>)builder5.build();
        final ValidationContext czd7 = new ValidationContext(LootContextParamSets.ALL_PARAMS, (Function<ResourceLocation, LootItemCondition>)map2::get, (Function<ResourceLocation, LootTable>)(vk -> null));
        map2.forEach((vk, dbl) -> dbl.validate(czd7.enterCondition(new StringBuilder().append("{").append(vk).append("}").toString(), vk)));
        czd7.getProblems().forEach((string1, string2) -> PredicateManager.LOGGER.warn("Found validation problem in " + string1 + ": " + string2));
        this.conditions = map2;
    }
    
    public Set<ResourceLocation> getKeys() {
        return (Set<ResourceLocation>)Collections.unmodifiableSet(this.conditions.keySet());
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = Deserializers.createConditionSerializer().create();
    }
    
    static class CompositePredicate implements LootItemCondition {
        private final LootItemCondition[] terms;
        private final Predicate<LootContext> composedPredicate;
        
        private CompositePredicate(final LootItemCondition[] arr) {
            this.terms = arr;
            this.composedPredicate = LootItemConditions.<LootContext>andConditions((java.util.function.Predicate<LootContext>[])arr);
        }
        
        public final boolean test(final LootContext cys) {
            return this.composedPredicate.test(cys);
        }
        
        public void validate(final ValidationContext czd) {
            super.validate(czd);
            for (int integer3 = 0; integer3 < this.terms.length; ++integer3) {
                this.terms[integer3].validate(czd.forChild(new StringBuilder().append(".term[").append(integer3).append("]").toString()));
            }
        }
        
        public LootItemConditionType getType() {
            throw new UnsupportedOperationException();
        }
    }
}
