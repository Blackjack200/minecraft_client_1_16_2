package net.minecraft.advancements.critereon;

import java.util.function.Predicate;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.AdvancementProgress;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import net.minecraft.core.Registry;
import net.minecraft.stats.StatType;
import com.google.gson.JsonArray;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.PlayerAdvancements;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.stats.RecipeBook;
import java.util.Iterator;
import net.minecraft.stats.StatsCounter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.stats.Stat;
import java.util.Map;
import net.minecraft.world.level.GameType;

public class PlayerPredicate {
    public static final PlayerPredicate ANY;
    private final MinMaxBounds.Ints level;
    private final GameType gameType;
    private final Map<Stat<?>, MinMaxBounds.Ints> stats;
    private final Object2BooleanMap<ResourceLocation> recipes;
    private final Map<ResourceLocation, AdvancementPredicate> advancements;
    
    private static AdvancementPredicate advancementPredicateFromJson(final JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            final boolean boolean2 = jsonElement.getAsBoolean();
            return new AdvancementDonePredicate(boolean2);
        }
        final Object2BooleanMap<String> object2BooleanMap2 = (Object2BooleanMap<String>)new Object2BooleanOpenHashMap();
        final JsonObject jsonObject3 = GsonHelper.convertToJsonObject(jsonElement, "criterion data");
        jsonObject3.entrySet().forEach(entry -> {
            final boolean boolean3 = GsonHelper.convertToBoolean((JsonElement)entry.getValue(), "criterion test");
            object2BooleanMap2.put(entry.getKey(), boolean3);
        });
        return new AdvancementCriterionsPredicate(object2BooleanMap2);
    }
    
    private PlayerPredicate(final MinMaxBounds.Ints d, final GameType brr, final Map<Stat<?>, MinMaxBounds.Ints> map3, final Object2BooleanMap<ResourceLocation> object2BooleanMap, final Map<ResourceLocation, AdvancementPredicate> map5) {
        this.level = d;
        this.gameType = brr;
        this.stats = map3;
        this.recipes = object2BooleanMap;
        this.advancements = map5;
    }
    
    public boolean matches(final Entity apx) {
        if (this == PlayerPredicate.ANY) {
            return true;
        }
        if (!(apx instanceof ServerPlayer)) {
            return false;
        }
        final ServerPlayer aah3 = (ServerPlayer)apx;
        if (!this.level.matches(aah3.experienceLevel)) {
            return false;
        }
        if (this.gameType != GameType.NOT_SET && this.gameType != aah3.gameMode.getGameModeForPlayer()) {
            return false;
        }
        final StatsCounter adz4 = aah3.getStats();
        for (final Map.Entry<Stat<?>, MinMaxBounds.Ints> entry6 : this.stats.entrySet()) {
            final int integer7 = adz4.getValue(entry6.getKey());
            if (!((MinMaxBounds.Ints)entry6.getValue()).matches(integer7)) {
                return false;
            }
        }
        final RecipeBook adr5 = aah3.getRecipeBook();
        for (final Object2BooleanMap.Entry<ResourceLocation> entry7 : this.recipes.object2BooleanEntrySet()) {
            if (adr5.contains((ResourceLocation)entry7.getKey()) != entry7.getBooleanValue()) {
                return false;
            }
        }
        if (!this.advancements.isEmpty()) {
            final PlayerAdvancements vt6 = aah3.getAdvancements();
            final ServerAdvancementManager vv7 = aah3.getServer().getAdvancements();
            for (final Map.Entry<ResourceLocation, AdvancementPredicate> entry8 : this.advancements.entrySet()) {
                final Advancement y10 = vv7.getAdvancement((ResourceLocation)entry8.getKey());
                if (y10 == null || !((AdvancementPredicate)entry8.getValue()).test(vt6.getOrStartProgress(y10))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static PlayerPredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return PlayerPredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "player");
        final MinMaxBounds.Ints d3 = MinMaxBounds.Ints.fromJson(jsonObject2.get("level"));
        final String string4 = GsonHelper.getAsString(jsonObject2, "gamemode", "");
        final GameType brr5 = GameType.byName(string4, GameType.NOT_SET);
        final Map<Stat<?>, MinMaxBounds.Ints> map6 = (Map<Stat<?>, MinMaxBounds.Ints>)Maps.newHashMap();
        final JsonArray jsonArray7 = GsonHelper.getAsJsonArray(jsonObject2, "stats", (JsonArray)null);
        if (jsonArray7 != null) {
            for (final JsonElement jsonElement2 : jsonArray7) {
                final JsonObject jsonObject3 = GsonHelper.convertToJsonObject(jsonElement2, "stats entry");
                final ResourceLocation vk11 = new ResourceLocation(GsonHelper.getAsString(jsonObject3, "type"));
                final StatType<?> adx12 = Registry.STAT_TYPE.get(vk11);
                if (adx12 == null) {
                    throw new JsonParseException(new StringBuilder().append("Invalid stat type: ").append(vk11).toString());
                }
                final ResourceLocation vk12 = new ResourceLocation(GsonHelper.getAsString(jsonObject3, "stat"));
                final Stat<?> adv14 = PlayerPredicate.getStat(adx12, vk12);
                final MinMaxBounds.Ints d4 = MinMaxBounds.Ints.fromJson(jsonObject3.get("value"));
                map6.put(adv14, d4);
            }
        }
        final Object2BooleanMap<ResourceLocation> object2BooleanMap8 = (Object2BooleanMap<ResourceLocation>)new Object2BooleanOpenHashMap();
        final JsonObject jsonObject4 = GsonHelper.getAsJsonObject(jsonObject2, "recipes", new JsonObject());
        for (final Map.Entry<String, JsonElement> entry11 : jsonObject4.entrySet()) {
            final ResourceLocation vk13 = new ResourceLocation((String)entry11.getKey());
            final boolean boolean13 = GsonHelper.convertToBoolean((JsonElement)entry11.getValue(), "recipe present");
            object2BooleanMap8.put(vk13, boolean13);
        }
        final Map<ResourceLocation, AdvancementPredicate> map7 = (Map<ResourceLocation, AdvancementPredicate>)Maps.newHashMap();
        final JsonObject jsonObject5 = GsonHelper.getAsJsonObject(jsonObject2, "advancements", new JsonObject());
        for (final Map.Entry<String, JsonElement> entry12 : jsonObject5.entrySet()) {
            final ResourceLocation vk14 = new ResourceLocation((String)entry12.getKey());
            final AdvancementPredicate c15 = advancementPredicateFromJson((JsonElement)entry12.getValue());
            map7.put(vk14, c15);
        }
        return new PlayerPredicate(d3, brr5, map6, object2BooleanMap8, map7);
    }
    
    private static <T> Stat<T> getStat(final StatType<T> adx, final ResourceLocation vk) {
        final Registry<T> gm3 = adx.getRegistry();
        final T object4 = gm3.get(vk);
        if (object4 == null) {
            throw new JsonParseException(new StringBuilder().append("Unknown object ").append(vk).append(" for stat type ").append(Registry.STAT_TYPE.getKey(adx)).toString());
        }
        return adx.get(object4);
    }
    
    private static <T> ResourceLocation getStatValueId(final Stat<T> adv) {
        return adv.getType().getRegistry().getKey(adv.getValue());
    }
    
    public JsonElement serializeToJson() {
        if (this == PlayerPredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("level", this.level.serializeToJson());
        if (this.gameType != GameType.NOT_SET) {
            jsonObject2.addProperty("gamemode", this.gameType.getName());
        }
        if (!this.stats.isEmpty()) {
            final JsonArray jsonArray3 = new JsonArray();
            this.stats.forEach((adv, d) -> {
                final JsonObject jsonObject4 = new JsonObject();
                jsonObject4.addProperty("type", Registry.STAT_TYPE.getKey(adv.getType()).toString());
                jsonObject4.addProperty("stat", PlayerPredicate.getStatValueId((Stat<Object>)adv).toString());
                jsonObject4.add("value", d.serializeToJson());
                jsonArray3.add((JsonElement)jsonObject4);
            });
            jsonObject2.add("stats", (JsonElement)jsonArray3);
        }
        if (!this.recipes.isEmpty()) {
            final JsonObject jsonObject3 = new JsonObject();
            this.recipes.forEach((vk, boolean3) -> jsonObject3.addProperty(vk.toString(), boolean3));
            jsonObject2.add("recipes", (JsonElement)jsonObject3);
        }
        if (!this.advancements.isEmpty()) {
            final JsonObject jsonObject3 = new JsonObject();
            this.advancements.forEach((vk, c) -> jsonObject3.add(vk.toString(), c.toJson()));
            jsonObject2.add("advancements", (JsonElement)jsonObject3);
        }
        return (JsonElement)jsonObject2;
    }
    
    static {
        ANY = new Builder().build();
    }
    
    static class AdvancementDonePredicate implements AdvancementPredicate {
        private final boolean state;
        
        public AdvancementDonePredicate(final boolean boolean1) {
            this.state = boolean1;
        }
        
        public JsonElement toJson() {
            return (JsonElement)new JsonPrimitive(Boolean.valueOf(this.state));
        }
        
        public boolean test(final AdvancementProgress aa) {
            return aa.isDone() == this.state;
        }
    }
    
    static class AdvancementCriterionsPredicate implements AdvancementPredicate {
        private final Object2BooleanMap<String> criterions;
        
        public AdvancementCriterionsPredicate(final Object2BooleanMap<String> object2BooleanMap) {
            this.criterions = object2BooleanMap;
        }
        
        public JsonElement toJson() {
            final JsonObject jsonObject2 = new JsonObject();
            this.criterions.forEach(jsonObject2::addProperty);
            return (JsonElement)jsonObject2;
        }
        
        public boolean test(final AdvancementProgress aa) {
            for (final Object2BooleanMap.Entry<String> entry4 : this.criterions.object2BooleanEntrySet()) {
                final CriterionProgress ae5 = aa.getCriterion((String)entry4.getKey());
                if (ae5 == null || ae5.isDone() != entry4.getBooleanValue()) {
                    return false;
                }
            }
            return true;
        }
    }
    
    public static class Builder {
        private MinMaxBounds.Ints level;
        private GameType gameType;
        private final Map<Stat<?>, MinMaxBounds.Ints> stats;
        private final Object2BooleanMap<ResourceLocation> recipes;
        private final Map<ResourceLocation, AdvancementPredicate> advancements;
        
        public Builder() {
            this.level = MinMaxBounds.Ints.ANY;
            this.gameType = GameType.NOT_SET;
            this.stats = (Map<Stat<?>, MinMaxBounds.Ints>)Maps.newHashMap();
            this.recipes = (Object2BooleanMap<ResourceLocation>)new Object2BooleanOpenHashMap();
            this.advancements = (Map<ResourceLocation, AdvancementPredicate>)Maps.newHashMap();
        }
        
        public PlayerPredicate build() {
            return new PlayerPredicate(this.level, this.gameType, this.stats, this.recipes, this.advancements, null);
        }
    }
    
    interface AdvancementPredicate extends Predicate<AdvancementProgress> {
        JsonElement toJson();
    }
}
