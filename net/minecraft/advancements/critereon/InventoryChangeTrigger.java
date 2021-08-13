package net.minecraft.advancements.critereon;

import java.util.List;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.ItemLike;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public class InventoryChangeTrigger extends SimpleCriterionTrigger<TriggerInstance> {
    private static final ResourceLocation ID;
    
    public ResourceLocation getId() {
        return InventoryChangeTrigger.ID;
    }
    
    public TriggerInstance createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax) {
        final JsonObject jsonObject2 = GsonHelper.getAsJsonObject(jsonObject, "slots", new JsonObject());
        final MinMaxBounds.Ints d6 = MinMaxBounds.Ints.fromJson(jsonObject2.get("occupied"));
        final MinMaxBounds.Ints d7 = MinMaxBounds.Ints.fromJson(jsonObject2.get("full"));
        final MinMaxBounds.Ints d8 = MinMaxBounds.Ints.fromJson(jsonObject2.get("empty"));
        final ItemPredicate[] arr9 = ItemPredicate.fromJsonArray(jsonObject.get("items"));
        return new TriggerInstance(b, d6, d7, d8, arr9);
    }
    
    public void trigger(final ServerPlayer aah, final Inventory bfs, final ItemStack bly) {
        int integer5 = 0;
        int integer6 = 0;
        int integer7 = 0;
        for (int integer8 = 0; integer8 < bfs.getContainerSize(); ++integer8) {
            final ItemStack bly2 = bfs.getItem(integer8);
            if (bly2.isEmpty()) {
                ++integer6;
            }
            else {
                ++integer7;
                if (bly2.getCount() >= bly2.getMaxStackSize()) {
                    ++integer5;
                }
            }
        }
        this.trigger(aah, bfs, bly, integer5, integer6, integer7);
    }
    
    private void trigger(final ServerPlayer aah, final Inventory bfs, final ItemStack bly, final int integer4, final int integer5, final int integer6) {
        this.trigger(aah, (java.util.function.Predicate<TriggerInstance>)(a -> a.matches(bfs, bly, integer4, integer5, integer6)));
    }
    
    static {
        ID = new ResourceLocation("inventory_changed");
    }
    
    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final MinMaxBounds.Ints slotsOccupied;
        private final MinMaxBounds.Ints slotsFull;
        private final MinMaxBounds.Ints slotsEmpty;
        private final ItemPredicate[] predicates;
        
        public TriggerInstance(final EntityPredicate.Composite b, final MinMaxBounds.Ints d2, final MinMaxBounds.Ints d3, final MinMaxBounds.Ints d4, final ItemPredicate[] arr) {
            super(InventoryChangeTrigger.ID, b);
            this.slotsOccupied = d2;
            this.slotsFull = d3;
            this.slotsEmpty = d4;
            this.predicates = arr;
        }
        
        public static TriggerInstance hasItems(final ItemPredicate... arr) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, arr);
        }
        
        public static TriggerInstance hasItems(final ItemLike... arr) {
            final ItemPredicate[] arr2 = new ItemPredicate[arr.length];
            for (int integer3 = 0; integer3 < arr.length; ++integer3) {
                arr2[integer3] = new ItemPredicate(null, arr[integer3].asItem(), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, EnchantmentPredicate.NONE, EnchantmentPredicate.NONE, null, NbtPredicate.ANY);
            }
            return hasItems(arr2);
        }
        
        @Override
        public JsonObject serializeToJson(final SerializationContext ci) {
            final JsonObject jsonObject3 = super.serializeToJson(ci);
            if (!this.slotsOccupied.isAny() || !this.slotsFull.isAny() || !this.slotsEmpty.isAny()) {
                final JsonObject jsonObject4 = new JsonObject();
                jsonObject4.add("occupied", this.slotsOccupied.serializeToJson());
                jsonObject4.add("full", this.slotsFull.serializeToJson());
                jsonObject4.add("empty", this.slotsEmpty.serializeToJson());
                jsonObject3.add("slots", (JsonElement)jsonObject4);
            }
            if (this.predicates.length > 0) {
                final JsonArray jsonArray4 = new JsonArray();
                for (final ItemPredicate bq8 : this.predicates) {
                    jsonArray4.add(bq8.serializeToJson());
                }
                jsonObject3.add("items", (JsonElement)jsonArray4);
            }
            return jsonObject3;
        }
        
        public boolean matches(final Inventory bfs, final ItemStack bly, final int integer3, final int integer4, final int integer5) {
            if (!this.slotsFull.matches(integer3)) {
                return false;
            }
            if (!this.slotsEmpty.matches(integer4)) {
                return false;
            }
            if (!this.slotsOccupied.matches(integer5)) {
                return false;
            }
            final int integer6 = this.predicates.length;
            if (integer6 == 0) {
                return true;
            }
            if (integer6 == 1) {
                return !bly.isEmpty() && this.predicates[0].matches(bly);
            }
            final List<ItemPredicate> list8 = (List<ItemPredicate>)new ObjectArrayList((Object[])this.predicates);
            for (int integer7 = bfs.getContainerSize(), integer8 = 0; integer8 < integer7; ++integer8) {
                if (list8.isEmpty()) {
                    return true;
                }
                final ItemStack bly2 = bfs.getItem(integer8);
                if (!bly2.isEmpty()) {
                    list8.removeIf(bq -> bq.matches(bly2));
                }
            }
            return list8.isEmpty();
        }
    }
}
