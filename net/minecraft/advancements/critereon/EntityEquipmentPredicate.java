package net.minecraft.advancements.critereon;

import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonElement;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;

public class EntityEquipmentPredicate {
    public static final EntityEquipmentPredicate ANY;
    public static final EntityEquipmentPredicate CAPTAIN;
    private final ItemPredicate head;
    private final ItemPredicate chest;
    private final ItemPredicate legs;
    private final ItemPredicate feet;
    private final ItemPredicate mainhand;
    private final ItemPredicate offhand;
    
    public EntityEquipmentPredicate(final ItemPredicate bq1, final ItemPredicate bq2, final ItemPredicate bq3, final ItemPredicate bq4, final ItemPredicate bq5, final ItemPredicate bq6) {
        this.head = bq1;
        this.chest = bq2;
        this.legs = bq3;
        this.feet = bq4;
        this.mainhand = bq5;
        this.offhand = bq6;
    }
    
    public boolean matches(@Nullable final Entity apx) {
        if (this == EntityEquipmentPredicate.ANY) {
            return true;
        }
        if (!(apx instanceof LivingEntity)) {
            return false;
        }
        final LivingEntity aqj3 = (LivingEntity)apx;
        return this.head.matches(aqj3.getItemBySlot(EquipmentSlot.HEAD)) && this.chest.matches(aqj3.getItemBySlot(EquipmentSlot.CHEST)) && this.legs.matches(aqj3.getItemBySlot(EquipmentSlot.LEGS)) && this.feet.matches(aqj3.getItemBySlot(EquipmentSlot.FEET)) && this.mainhand.matches(aqj3.getItemBySlot(EquipmentSlot.MAINHAND)) && this.offhand.matches(aqj3.getItemBySlot(EquipmentSlot.OFFHAND));
    }
    
    public static EntityEquipmentPredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return EntityEquipmentPredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "equipment");
        final ItemPredicate bq3 = ItemPredicate.fromJson(jsonObject2.get("head"));
        final ItemPredicate bq4 = ItemPredicate.fromJson(jsonObject2.get("chest"));
        final ItemPredicate bq5 = ItemPredicate.fromJson(jsonObject2.get("legs"));
        final ItemPredicate bq6 = ItemPredicate.fromJson(jsonObject2.get("feet"));
        final ItemPredicate bq7 = ItemPredicate.fromJson(jsonObject2.get("mainhand"));
        final ItemPredicate bq8 = ItemPredicate.fromJson(jsonObject2.get("offhand"));
        return new EntityEquipmentPredicate(bq3, bq4, bq5, bq6, bq7, bq8);
    }
    
    public JsonElement serializeToJson() {
        if (this == EntityEquipmentPredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("head", this.head.serializeToJson());
        jsonObject2.add("chest", this.chest.serializeToJson());
        jsonObject2.add("legs", this.legs.serializeToJson());
        jsonObject2.add("feet", this.feet.serializeToJson());
        jsonObject2.add("mainhand", this.mainhand.serializeToJson());
        jsonObject2.add("offhand", this.offhand.serializeToJson());
        return (JsonElement)jsonObject2;
    }
    
    static {
        ANY = new EntityEquipmentPredicate(ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY);
        CAPTAIN = new EntityEquipmentPredicate(ItemPredicate.Builder.item().of(Items.WHITE_BANNER).hasNbt(Raid.getLeaderBannerInstance().getTag()).build(), ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY, ItemPredicate.ANY);
    }
    
    public static class Builder {
        private ItemPredicate head;
        private ItemPredicate chest;
        private ItemPredicate legs;
        private ItemPredicate feet;
        private ItemPredicate mainhand;
        private ItemPredicate offhand;
        
        public Builder() {
            this.head = ItemPredicate.ANY;
            this.chest = ItemPredicate.ANY;
            this.legs = ItemPredicate.ANY;
            this.feet = ItemPredicate.ANY;
            this.mainhand = ItemPredicate.ANY;
            this.offhand = ItemPredicate.ANY;
        }
        
        public static Builder equipment() {
            return new Builder();
        }
        
        public Builder head(final ItemPredicate bq) {
            this.head = bq;
            return this;
        }
        
        public Builder chest(final ItemPredicate bq) {
            this.chest = bq;
            return this;
        }
        
        public Builder legs(final ItemPredicate bq) {
            this.legs = bq;
            return this;
        }
        
        public Builder feet(final ItemPredicate bq) {
            this.feet = bq;
            return this;
        }
        
        public EntityEquipmentPredicate build() {
            return new EntityEquipmentPredicate(this.head, this.chest, this.legs, this.feet, this.mainhand, this.offhand);
        }
    }
}
