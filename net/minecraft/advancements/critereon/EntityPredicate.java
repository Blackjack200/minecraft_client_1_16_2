package net.minecraft.advancements.critereon;

import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import com.google.gson.JsonArray;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import java.util.function.Predicate;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonElement;
import net.minecraft.world.scores.Team;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;

public class EntityPredicate {
    public static final EntityPredicate ANY;
    private final EntityTypePredicate entityType;
    private final DistancePredicate distanceToPlayer;
    private final LocationPredicate location;
    private final MobEffectsPredicate effects;
    private final NbtPredicate nbt;
    private final EntityFlagsPredicate flags;
    private final EntityEquipmentPredicate equipment;
    private final PlayerPredicate player;
    private final FishingHookPredicate fishingHook;
    private final EntityPredicate vehicle;
    private final EntityPredicate targetedEntity;
    @Nullable
    private final String team;
    @Nullable
    private final ResourceLocation catType;
    
    private EntityPredicate(final EntityTypePredicate bh, final DistancePredicate ay, final LocationPredicate bw, final MobEffectsPredicate ca, final NbtPredicate cb, final EntityFlagsPredicate be, final EntityEquipmentPredicate bd, final PlayerPredicate cg, final FishingHookPredicate bj, @Nullable final String string, @Nullable final ResourceLocation vk) {
        this.entityType = bh;
        this.distanceToPlayer = ay;
        this.location = bw;
        this.effects = ca;
        this.nbt = cb;
        this.flags = be;
        this.equipment = bd;
        this.player = cg;
        this.fishingHook = bj;
        this.vehicle = this;
        this.targetedEntity = this;
        this.team = string;
        this.catType = vk;
    }
    
    private EntityPredicate(final EntityTypePredicate bh, final DistancePredicate ay, final LocationPredicate bw, final MobEffectsPredicate ca, final NbtPredicate cb, final EntityFlagsPredicate be, final EntityEquipmentPredicate bd, final PlayerPredicate cg, final FishingHookPredicate bj, final EntityPredicate bg10, final EntityPredicate bg11, @Nullable final String string, @Nullable final ResourceLocation vk) {
        this.entityType = bh;
        this.distanceToPlayer = ay;
        this.location = bw;
        this.effects = ca;
        this.nbt = cb;
        this.flags = be;
        this.equipment = bd;
        this.player = cg;
        this.fishingHook = bj;
        this.vehicle = bg10;
        this.targetedEntity = bg11;
        this.team = string;
        this.catType = vk;
    }
    
    public boolean matches(final ServerPlayer aah, @Nullable final Entity apx) {
        return this.matches(aah.getLevel(), aah.position(), apx);
    }
    
    public boolean matches(final ServerLevel aag, @Nullable final Vec3 dck, @Nullable final Entity apx) {
        if (this == EntityPredicate.ANY) {
            return true;
        }
        if (apx == null) {
            return false;
        }
        if (!this.entityType.matches(apx.getType())) {
            return false;
        }
        if (dck == null) {
            if (this.distanceToPlayer != DistancePredicate.ANY) {
                return false;
            }
        }
        else if (!this.distanceToPlayer.matches(dck.x, dck.y, dck.z, apx.getX(), apx.getY(), apx.getZ())) {
            return false;
        }
        if (!this.location.matches(aag, apx.getX(), apx.getY(), apx.getZ())) {
            return false;
        }
        if (!this.effects.matches(apx)) {
            return false;
        }
        if (!this.nbt.matches(apx)) {
            return false;
        }
        if (!this.flags.matches(apx)) {
            return false;
        }
        if (!this.equipment.matches(apx)) {
            return false;
        }
        if (!this.player.matches(apx)) {
            return false;
        }
        if (!this.fishingHook.matches(apx)) {
            return false;
        }
        if (!this.vehicle.matches(aag, dck, apx.getVehicle())) {
            return false;
        }
        if (!this.targetedEntity.matches(aag, dck, (apx instanceof Mob) ? ((Mob)apx).getTarget() : null)) {
            return false;
        }
        if (this.team != null) {
            final Team ddm5 = apx.getTeam();
            if (ddm5 == null || !this.team.equals(ddm5.getName())) {
                return false;
            }
        }
        return this.catType == null || (apx instanceof Cat && ((Cat)apx).getResourceLocation().equals(this.catType));
    }
    
    public static EntityPredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return EntityPredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "entity");
        final EntityTypePredicate bh3 = EntityTypePredicate.fromJson(jsonObject2.get("type"));
        final DistancePredicate ay4 = DistancePredicate.fromJson(jsonObject2.get("distance"));
        final LocationPredicate bw5 = LocationPredicate.fromJson(jsonObject2.get("location"));
        final MobEffectsPredicate ca6 = MobEffectsPredicate.fromJson(jsonObject2.get("effects"));
        final NbtPredicate cb7 = NbtPredicate.fromJson(jsonObject2.get("nbt"));
        final EntityFlagsPredicate be8 = EntityFlagsPredicate.fromJson(jsonObject2.get("flags"));
        final EntityEquipmentPredicate bd9 = EntityEquipmentPredicate.fromJson(jsonObject2.get("equipment"));
        final PlayerPredicate cg10 = PlayerPredicate.fromJson(jsonObject2.get("player"));
        final FishingHookPredicate bj11 = FishingHookPredicate.fromJson(jsonObject2.get("fishing_hook"));
        final EntityPredicate bg12 = fromJson(jsonObject2.get("vehicle"));
        final EntityPredicate bg13 = fromJson(jsonObject2.get("targeted_entity"));
        final String string14 = GsonHelper.getAsString(jsonObject2, "team", (String)null);
        final ResourceLocation vk15 = jsonObject2.has("catType") ? new ResourceLocation(GsonHelper.getAsString(jsonObject2, "catType")) : null;
        return new Builder().entityType(bh3).distance(ay4).located(bw5).effects(ca6).nbt(cb7).flags(be8).equipment(bd9).player(cg10).fishingHook(bj11).team(string14).vehicle(bg12).targetedEntity(bg13).catType(vk15).build();
    }
    
    public JsonElement serializeToJson() {
        if (this == EntityPredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("type", this.entityType.serializeToJson());
        jsonObject2.add("distance", this.distanceToPlayer.serializeToJson());
        jsonObject2.add("location", this.location.serializeToJson());
        jsonObject2.add("effects", this.effects.serializeToJson());
        jsonObject2.add("nbt", this.nbt.serializeToJson());
        jsonObject2.add("flags", this.flags.serializeToJson());
        jsonObject2.add("equipment", this.equipment.serializeToJson());
        jsonObject2.add("player", this.player.serializeToJson());
        jsonObject2.add("fishing_hook", this.fishingHook.serializeToJson());
        jsonObject2.add("vehicle", this.vehicle.serializeToJson());
        jsonObject2.add("targeted_entity", this.targetedEntity.serializeToJson());
        jsonObject2.addProperty("team", this.team);
        if (this.catType != null) {
            jsonObject2.addProperty("catType", this.catType.toString());
        }
        return (JsonElement)jsonObject2;
    }
    
    public static LootContext createContext(final ServerPlayer aah, final Entity apx) {
        return new LootContext.Builder(aah.getLevel()).<Entity>withParameter(LootContextParams.THIS_ENTITY, apx).<Vec3>withParameter(LootContextParams.ORIGIN, aah.position()).withRandom(aah.getRandom()).create(LootContextParamSets.ADVANCEMENT_ENTITY);
    }
    
    static {
        ANY = new EntityPredicate(EntityTypePredicate.ANY, DistancePredicate.ANY, LocationPredicate.ANY, MobEffectsPredicate.ANY, NbtPredicate.ANY, EntityFlagsPredicate.ANY, EntityEquipmentPredicate.ANY, PlayerPredicate.ANY, FishingHookPredicate.ANY, null, null);
    }
    
    public static class Builder {
        private EntityTypePredicate entityType;
        private DistancePredicate distanceToPlayer;
        private LocationPredicate location;
        private MobEffectsPredicate effects;
        private NbtPredicate nbt;
        private EntityFlagsPredicate flags;
        private EntityEquipmentPredicate equipment;
        private PlayerPredicate player;
        private FishingHookPredicate fishingHook;
        private EntityPredicate vehicle;
        private EntityPredicate targetedEntity;
        private String team;
        private ResourceLocation catType;
        
        public Builder() {
            this.entityType = EntityTypePredicate.ANY;
            this.distanceToPlayer = DistancePredicate.ANY;
            this.location = LocationPredicate.ANY;
            this.effects = MobEffectsPredicate.ANY;
            this.nbt = NbtPredicate.ANY;
            this.flags = EntityFlagsPredicate.ANY;
            this.equipment = EntityEquipmentPredicate.ANY;
            this.player = PlayerPredicate.ANY;
            this.fishingHook = FishingHookPredicate.ANY;
            this.vehicle = EntityPredicate.ANY;
            this.targetedEntity = EntityPredicate.ANY;
        }
        
        public static Builder entity() {
            return new Builder();
        }
        
        public Builder of(final EntityType<?> aqb) {
            this.entityType = EntityTypePredicate.of(aqb);
            return this;
        }
        
        public Builder of(final Tag<EntityType<?>> aej) {
            this.entityType = EntityTypePredicate.of(aej);
            return this;
        }
        
        public Builder of(final ResourceLocation vk) {
            this.catType = vk;
            return this;
        }
        
        public Builder entityType(final EntityTypePredicate bh) {
            this.entityType = bh;
            return this;
        }
        
        public Builder distance(final DistancePredicate ay) {
            this.distanceToPlayer = ay;
            return this;
        }
        
        public Builder located(final LocationPredicate bw) {
            this.location = bw;
            return this;
        }
        
        public Builder effects(final MobEffectsPredicate ca) {
            this.effects = ca;
            return this;
        }
        
        public Builder nbt(final NbtPredicate cb) {
            this.nbt = cb;
            return this;
        }
        
        public Builder flags(final EntityFlagsPredicate be) {
            this.flags = be;
            return this;
        }
        
        public Builder equipment(final EntityEquipmentPredicate bd) {
            this.equipment = bd;
            return this;
        }
        
        public Builder player(final PlayerPredicate cg) {
            this.player = cg;
            return this;
        }
        
        public Builder fishingHook(final FishingHookPredicate bj) {
            this.fishingHook = bj;
            return this;
        }
        
        public Builder vehicle(final EntityPredicate bg) {
            this.vehicle = bg;
            return this;
        }
        
        public Builder targetedEntity(final EntityPredicate bg) {
            this.targetedEntity = bg;
            return this;
        }
        
        public Builder team(@Nullable final String string) {
            this.team = string;
            return this;
        }
        
        public Builder catType(@Nullable final ResourceLocation vk) {
            this.catType = vk;
            return this;
        }
        
        public EntityPredicate build() {
            return new EntityPredicate(this.entityType, this.distanceToPlayer, this.location, this.effects, this.nbt, this.flags, this.equipment, this.player, this.fishingHook, this.vehicle, this.targetedEntity, this.team, this.catType, null);
        }
    }
    
    public static class Composite {
        public static final Composite ANY;
        private final LootItemCondition[] conditions;
        private final Predicate<LootContext> compositePredicates;
        
        private Composite(final LootItemCondition[] arr) {
            this.conditions = arr;
            this.compositePredicates = LootItemConditions.<LootContext>andConditions((java.util.function.Predicate<LootContext>[])arr);
        }
        
        public static Composite create(final LootItemCondition... arr) {
            return new Composite(arr);
        }
        
        public static Composite fromJson(final JsonObject jsonObject, final String string, final DeserializationContext ax) {
            final JsonElement jsonElement4 = jsonObject.get(string);
            return fromElement(string, ax, jsonElement4);
        }
        
        public static Composite[] fromJsonArray(final JsonObject jsonObject, final String string, final DeserializationContext ax) {
            final JsonElement jsonElement4 = jsonObject.get(string);
            if (jsonElement4 == null || jsonElement4.isJsonNull()) {
                return new Composite[0];
            }
            final JsonArray jsonArray5 = GsonHelper.convertToJsonArray(jsonElement4, string);
            final Composite[] arr6 = new Composite[jsonArray5.size()];
            for (int integer7 = 0; integer7 < jsonArray5.size(); ++integer7) {
                arr6[integer7] = fromElement(string + "[" + integer7 + "]", ax, jsonArray5.get(integer7));
            }
            return arr6;
        }
        
        private static Composite fromElement(final String string, final DeserializationContext ax, @Nullable final JsonElement jsonElement) {
            if (jsonElement != null && jsonElement.isJsonArray()) {
                final LootItemCondition[] arr4 = ax.deserializeConditions(jsonElement.getAsJsonArray(), ax.getAdvancementId().toString() + "/" + string, LootContextParamSets.ADVANCEMENT_ENTITY);
                return new Composite(arr4);
            }
            final EntityPredicate bg4 = EntityPredicate.fromJson(jsonElement);
            return wrap(bg4);
        }
        
        public static Composite wrap(final EntityPredicate bg) {
            if (bg == EntityPredicate.ANY) {
                return Composite.ANY;
            }
            final LootItemCondition dbl2 = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, bg).build();
            return new Composite(new LootItemCondition[] { dbl2 });
        }
        
        public boolean matches(final LootContext cys) {
            return this.compositePredicates.test(cys);
        }
        
        public JsonElement toJson(final SerializationContext ci) {
            if (this.conditions.length == 0) {
                return (JsonElement)JsonNull.INSTANCE;
            }
            return ci.serializeConditions(this.conditions);
        }
        
        public static JsonElement toJson(final Composite[] arr, final SerializationContext ci) {
            if (arr.length == 0) {
                return (JsonElement)JsonNull.INSTANCE;
            }
            final JsonArray jsonArray3 = new JsonArray();
            for (final Composite b7 : arr) {
                jsonArray3.add(b7.toJson(ci));
            }
            return (JsonElement)jsonArray3;
        }
        
        static {
            ANY = new Composite(new LootItemCondition[0]);
        }
    }
}
