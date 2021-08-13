package net.minecraft.world.level.storage.loot.entries;

import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import net.minecraft.tags.SerializationTags;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import java.util.Iterator;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.item.Item;
import net.minecraft.tags.Tag;

public class TagEntry extends LootPoolSingletonContainer {
    private final Tag<Item> tag;
    private final boolean expand;
    
    private TagEntry(final Tag<Item> aej, final boolean boolean2, final int integer3, final int integer4, final LootItemCondition[] arr, final LootItemFunction[] arr) {
        super(integer3, integer4, arr, arr);
        this.tag = aej;
        this.expand = boolean2;
    }
    
    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.TAG;
    }
    
    public void createItemStack(final Consumer<ItemStack> consumer, final LootContext cys) {
        this.tag.getValues().forEach(blu -> consumer.accept(new ItemStack(blu)));
    }
    
    private boolean expandTag(final LootContext cys, final Consumer<LootPoolEntry> consumer) {
        if (this.canRun(cys)) {
            for (final Item blu5 : this.tag.getValues()) {
                consumer.accept(new EntryBase() {
                    public void createItemStack(final Consumer<ItemStack> consumer, final LootContext cys) {
                        consumer.accept((Object)new ItemStack(blu5));
                    }
                });
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean expand(final LootContext cys, final Consumer<LootPoolEntry> consumer) {
        if (this.expand) {
            return this.expandTag(cys, consumer);
        }
        return super.expand(cys, consumer);
    }
    
    public static Builder<?> expandTag(final Tag<Item> aej) {
        return LootPoolSingletonContainer.simpleBuilder((integer2, integer3, arr, arr) -> new TagEntry(aej, true, integer2, integer3, arr, arr));
    }
    
    public static class Serializer extends LootPoolSingletonContainer.Serializer<TagEntry> {
        @Override
        public void serialize(final JsonObject jsonObject, final TagEntry czs, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, czs, jsonSerializationContext);
            jsonObject.addProperty("name", SerializationTags.getInstance().getItems().getIdOrThrow(czs.tag).toString());
            jsonObject.addProperty("expand", Boolean.valueOf(czs.expand));
        }
        
        @Override
        protected TagEntry deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final int integer3, final int integer4, final LootItemCondition[] arr, final LootItemFunction[] arr) {
            final ResourceLocation vk8 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "name"));
            final Tag<Item> aej9 = SerializationTags.getInstance().getItems().getTag(vk8);
            if (aej9 == null) {
                throw new JsonParseException(new StringBuilder().append("Can't find tag: ").append(vk8).toString());
            }
            final boolean boolean10 = GsonHelper.getAsBoolean(jsonObject, "expand");
            return new TagEntry(aej9, boolean10, integer3, integer4, arr, arr, null);
        }
    }
}
