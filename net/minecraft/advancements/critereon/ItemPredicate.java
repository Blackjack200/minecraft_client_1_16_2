package net.minecraft.advancements.critereon;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ItemLike;
import com.google.common.collect.Lists;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.tags.SerializationTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonElement;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.Map;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import javax.annotation.Nullable;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;

public class ItemPredicate {
    public static final ItemPredicate ANY;
    @Nullable
    private final Tag<Item> tag;
    @Nullable
    private final Item item;
    private final MinMaxBounds.Ints count;
    private final MinMaxBounds.Ints durability;
    private final EnchantmentPredicate[] enchantments;
    private final EnchantmentPredicate[] storedEnchantments;
    @Nullable
    private final Potion potion;
    private final NbtPredicate nbt;
    
    public ItemPredicate() {
        this.tag = null;
        this.item = null;
        this.potion = null;
        this.count = MinMaxBounds.Ints.ANY;
        this.durability = MinMaxBounds.Ints.ANY;
        this.enchantments = EnchantmentPredicate.NONE;
        this.storedEnchantments = EnchantmentPredicate.NONE;
        this.nbt = NbtPredicate.ANY;
    }
    
    public ItemPredicate(@Nullable final Tag<Item> aej, @Nullable final Item blu, final MinMaxBounds.Ints d3, final MinMaxBounds.Ints d4, final EnchantmentPredicate[] arr5, final EnchantmentPredicate[] arr6, @Nullable final Potion bnq, final NbtPredicate cb) {
        this.tag = aej;
        this.item = blu;
        this.count = d3;
        this.durability = d4;
        this.enchantments = arr5;
        this.storedEnchantments = arr6;
        this.potion = bnq;
        this.nbt = cb;
    }
    
    public boolean matches(final ItemStack bly) {
        if (this == ItemPredicate.ANY) {
            return true;
        }
        if (this.tag != null && !this.tag.contains(bly.getItem())) {
            return false;
        }
        if (this.item != null && bly.getItem() != this.item) {
            return false;
        }
        if (!this.count.matches(bly.getCount())) {
            return false;
        }
        if (!this.durability.isAny() && !bly.isDamageableItem()) {
            return false;
        }
        if (!this.durability.matches(bly.getMaxDamage() - bly.getDamageValue())) {
            return false;
        }
        if (!this.nbt.matches(bly)) {
            return false;
        }
        if (this.enchantments.length > 0) {
            final Map<Enchantment, Integer> map3 = EnchantmentHelper.deserializeEnchantments(bly.getEnchantmentTags());
            for (final EnchantmentPredicate bb7 : this.enchantments) {
                if (!bb7.containedIn(map3)) {
                    return false;
                }
            }
        }
        if (this.storedEnchantments.length > 0) {
            final Map<Enchantment, Integer> map3 = EnchantmentHelper.deserializeEnchantments(EnchantedBookItem.getEnchantments(bly));
            for (final EnchantmentPredicate bb7 : this.storedEnchantments) {
                if (!bb7.containedIn(map3)) {
                    return false;
                }
            }
        }
        final Potion bnq3 = PotionUtils.getPotion(bly);
        return this.potion == null || this.potion == bnq3;
    }
    
    public static ItemPredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return ItemPredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "item");
        final MinMaxBounds.Ints d3 = MinMaxBounds.Ints.fromJson(jsonObject2.get("count"));
        final MinMaxBounds.Ints d4 = MinMaxBounds.Ints.fromJson(jsonObject2.get("durability"));
        if (jsonObject2.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        }
        final NbtPredicate cb5 = NbtPredicate.fromJson(jsonObject2.get("nbt"));
        Item blu6 = null;
        if (jsonObject2.has("item")) {
            final ResourceLocation vk7 = new ResourceLocation(GsonHelper.getAsString(jsonObject2, "item"));
            blu6 = (Item)Registry.ITEM.getOptional(vk7).orElseThrow(() -> new JsonSyntaxException(new StringBuilder().append("Unknown item id '").append(vk7).append("'").toString()));
        }
        Tag<Item> aej7 = null;
        if (jsonObject2.has("tag")) {
            final ResourceLocation vk8 = new ResourceLocation(GsonHelper.getAsString(jsonObject2, "tag"));
            aej7 = SerializationTags.getInstance().getItems().getTag(vk8);
            if (aej7 == null) {
                throw new JsonSyntaxException(new StringBuilder().append("Unknown item tag '").append(vk8).append("'").toString());
            }
        }
        Potion bnq8 = null;
        if (jsonObject2.has("potion")) {
            final ResourceLocation vk9 = new ResourceLocation(GsonHelper.getAsString(jsonObject2, "potion"));
            bnq8 = (Potion)Registry.POTION.getOptional(vk9).orElseThrow(() -> new JsonSyntaxException(new StringBuilder().append("Unknown potion '").append(vk9).append("'").toString()));
        }
        final EnchantmentPredicate[] arr9 = EnchantmentPredicate.fromJsonArray(jsonObject2.get("enchantments"));
        final EnchantmentPredicate[] arr10 = EnchantmentPredicate.fromJsonArray(jsonObject2.get("stored_enchantments"));
        return new ItemPredicate(aej7, blu6, d3, d4, arr9, arr10, bnq8, cb5);
    }
    
    public JsonElement serializeToJson() {
        if (this == ItemPredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        if (this.item != null) {
            jsonObject2.addProperty("item", Registry.ITEM.getKey(this.item).toString());
        }
        if (this.tag != null) {
            jsonObject2.addProperty("tag", SerializationTags.getInstance().getItems().getIdOrThrow(this.tag).toString());
        }
        jsonObject2.add("count", this.count.serializeToJson());
        jsonObject2.add("durability", this.durability.serializeToJson());
        jsonObject2.add("nbt", this.nbt.serializeToJson());
        if (this.enchantments.length > 0) {
            final JsonArray jsonArray3 = new JsonArray();
            for (final EnchantmentPredicate bb7 : this.enchantments) {
                jsonArray3.add(bb7.serializeToJson());
            }
            jsonObject2.add("enchantments", (JsonElement)jsonArray3);
        }
        if (this.storedEnchantments.length > 0) {
            final JsonArray jsonArray3 = new JsonArray();
            for (final EnchantmentPredicate bb7 : this.storedEnchantments) {
                jsonArray3.add(bb7.serializeToJson());
            }
            jsonObject2.add("stored_enchantments", (JsonElement)jsonArray3);
        }
        if (this.potion != null) {
            jsonObject2.addProperty("potion", Registry.POTION.getKey(this.potion).toString());
        }
        return (JsonElement)jsonObject2;
    }
    
    public static ItemPredicate[] fromJsonArray(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return new ItemPredicate[0];
        }
        final JsonArray jsonArray2 = GsonHelper.convertToJsonArray(jsonElement, "items");
        final ItemPredicate[] arr3 = new ItemPredicate[jsonArray2.size()];
        for (int integer4 = 0; integer4 < arr3.length; ++integer4) {
            arr3[integer4] = fromJson(jsonArray2.get(integer4));
        }
        return arr3;
    }
    
    static {
        ANY = new ItemPredicate();
    }
    
    public static class Builder {
        private final List<EnchantmentPredicate> enchantments;
        private final List<EnchantmentPredicate> storedEnchantments;
        @Nullable
        private Item item;
        @Nullable
        private Tag<Item> tag;
        private MinMaxBounds.Ints count;
        private MinMaxBounds.Ints durability;
        @Nullable
        private Potion potion;
        private NbtPredicate nbt;
        
        private Builder() {
            this.enchantments = (List<EnchantmentPredicate>)Lists.newArrayList();
            this.storedEnchantments = (List<EnchantmentPredicate>)Lists.newArrayList();
            this.count = MinMaxBounds.Ints.ANY;
            this.durability = MinMaxBounds.Ints.ANY;
            this.nbt = NbtPredicate.ANY;
        }
        
        public static Builder item() {
            return new Builder();
        }
        
        public Builder of(final ItemLike brt) {
            this.item = brt.asItem();
            return this;
        }
        
        public Builder of(final Tag<Item> aej) {
            this.tag = aej;
            return this;
        }
        
        public Builder hasNbt(final CompoundTag md) {
            this.nbt = new NbtPredicate(md);
            return this;
        }
        
        public Builder hasEnchantment(final EnchantmentPredicate bb) {
            this.enchantments.add(bb);
            return this;
        }
        
        public ItemPredicate build() {
            return new ItemPredicate(this.tag, this.item, this.count, this.durability, (EnchantmentPredicate[])this.enchantments.toArray((Object[])EnchantmentPredicate.NONE), (EnchantmentPredicate[])this.storedEnchantments.toArray((Object[])EnchantmentPredicate.NONE), this.potion, this.nbt);
        }
    }
}
