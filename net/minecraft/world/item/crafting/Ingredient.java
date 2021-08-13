package net.minecraft.world.item.crafting;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Collection;
import net.minecraft.tags.SerializationTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import java.util.stream.StreamSupport;
import com.google.gson.JsonSyntaxException;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.ItemLike;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Comparator;
import it.unimi.dsi.fastutil.ints.IntComparators;
import net.minecraft.world.entity.player.StackedContents;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Stream;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.item.ItemStack;
import java.util.function.Predicate;

public final class Ingredient implements Predicate<ItemStack> {
    public static final Ingredient EMPTY;
    private final Value[] values;
    private ItemStack[] itemStacks;
    private IntList stackingIds;
    
    private Ingredient(final Stream<? extends Value> stream) {
        this.values = (Value[])stream.toArray(Value[]::new);
    }
    
    public ItemStack[] getItems() {
        this.dissolve();
        return this.itemStacks;
    }
    
    private void dissolve() {
        if (this.itemStacks == null) {
            this.itemStacks = (ItemStack[])Arrays.stream((Object[])this.values).flatMap(c -> c.getItems().stream()).distinct().toArray(ItemStack[]::new);
        }
    }
    
    public boolean test(@Nullable final ItemStack bly) {
        if (bly == null) {
            return false;
        }
        this.dissolve();
        if (this.itemStacks.length == 0) {
            return bly.isEmpty();
        }
        for (final ItemStack bly2 : this.itemStacks) {
            if (bly2.getItem() == bly.getItem()) {
                return true;
            }
        }
        return false;
    }
    
    public IntList getStackingIds() {
        if (this.stackingIds == null) {
            this.dissolve();
            this.stackingIds = (IntList)new IntArrayList(this.itemStacks.length);
            for (final ItemStack bly5 : this.itemStacks) {
                this.stackingIds.add(StackedContents.getStackingIndex(bly5));
            }
            this.stackingIds.sort((Comparator)IntComparators.NATURAL_COMPARATOR);
        }
        return this.stackingIds;
    }
    
    public void toNetwork(final FriendlyByteBuf nf) {
        this.dissolve();
        nf.writeVarInt(this.itemStacks.length);
        for (int integer3 = 0; integer3 < this.itemStacks.length; ++integer3) {
            nf.writeItem(this.itemStacks[integer3]);
        }
    }
    
    public JsonElement toJson() {
        if (this.values.length == 1) {
            return (JsonElement)this.values[0].serialize();
        }
        final JsonArray jsonArray2 = new JsonArray();
        for (final Value c6 : this.values) {
            jsonArray2.add((JsonElement)c6.serialize());
        }
        return (JsonElement)jsonArray2;
    }
    
    public boolean isEmpty() {
        return this.values.length == 0 && (this.itemStacks == null || this.itemStacks.length == 0) && (this.stackingIds == null || this.stackingIds.isEmpty());
    }
    
    private static Ingredient fromValues(final Stream<? extends Value> stream) {
        final Ingredient bok2 = new Ingredient(stream);
        return (bok2.values.length == 0) ? Ingredient.EMPTY : bok2;
    }
    
    public static Ingredient of(final ItemLike... arr) {
        return of((Stream<ItemStack>)Arrays.stream((Object[])arr).map(ItemStack::new));
    }
    
    public static Ingredient of(final ItemStack... arr) {
        return of((Stream<ItemStack>)Arrays.stream((Object[])arr));
    }
    
    public static Ingredient of(final Stream<ItemStack> stream) {
        return fromValues(stream.filter(bly -> !bly.isEmpty()).map(bly -> new ItemValue(bly)));
    }
    
    public static Ingredient of(final Tag<Item> aej) {
        return fromValues(Stream.of(new TagValue((Tag)aej)));
    }
    
    public static Ingredient fromNetwork(final FriendlyByteBuf nf) {
        final int integer2 = nf.readVarInt();
        return fromValues(Stream.generate(() -> new ItemValue(nf.readItem())).limit((long)integer2));
    }
    
    public static Ingredient fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            throw new JsonSyntaxException("Item cannot be null");
        }
        if (jsonElement.isJsonObject()) {
            return fromValues(Stream.of(valueFromJson(jsonElement.getAsJsonObject())));
        }
        if (!jsonElement.isJsonArray()) {
            throw new JsonSyntaxException("Expected item to be object or array of objects");
        }
        final JsonArray jsonArray2 = jsonElement.getAsJsonArray();
        if (jsonArray2.size() == 0) {
            throw new JsonSyntaxException("Item array cannot be empty, at least one item must be defined");
        }
        return fromValues(StreamSupport.stream(jsonArray2.spliterator(), false).map(jsonElement -> valueFromJson(GsonHelper.convertToJsonObject(jsonElement, "item"))));
    }
    
    private static Value valueFromJson(final JsonObject jsonObject) {
        if (jsonObject.has("item") && jsonObject.has("tag")) {
            throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
        }
        if (jsonObject.has("item")) {
            final ResourceLocation vk2 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "item"));
            final Item blu3 = (Item)Registry.ITEM.getOptional(vk2).orElseThrow(() -> new JsonSyntaxException(new StringBuilder().append("Unknown item '").append(vk2).append("'").toString()));
            return new ItemValue(new ItemStack(blu3));
        }
        if (!jsonObject.has("tag")) {
            throw new JsonParseException("An ingredient entry needs either a tag or an item");
        }
        final ResourceLocation vk2 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "tag"));
        final Tag<Item> aej3 = SerializationTags.getInstance().getItems().getTag(vk2);
        if (aej3 == null) {
            throw new JsonSyntaxException(new StringBuilder().append("Unknown item tag '").append(vk2).append("'").toString());
        }
        return new TagValue((Tag)aej3);
    }
    
    static {
        EMPTY = new Ingredient(Stream.empty());
    }
    
    static class ItemValue implements Value {
        private final ItemStack item;
        
        private ItemValue(final ItemStack bly) {
            this.item = bly;
        }
        
        public Collection<ItemStack> getItems() {
            return (Collection<ItemStack>)Collections.singleton(this.item);
        }
        
        public JsonObject serialize() {
            final JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("item", Registry.ITEM.getKey(this.item.getItem()).toString());
            return jsonObject2;
        }
    }
    
    static class TagValue implements Value {
        private final Tag<Item> tag;
        
        private TagValue(final Tag<Item> aej) {
            this.tag = aej;
        }
        
        public Collection<ItemStack> getItems() {
            final List<ItemStack> list2 = (List<ItemStack>)Lists.newArrayList();
            for (final Item blu4 : this.tag.getValues()) {
                list2.add(new ItemStack(blu4));
            }
            return (Collection<ItemStack>)list2;
        }
        
        public JsonObject serialize() {
            final JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("tag", SerializationTags.getInstance().getItems().getIdOrThrow(this.tag).toString());
            return jsonObject2;
        }
    }
    
    interface Value {
        Collection<ItemStack> getItems();
        
        JsonObject serialize();
    }
}
