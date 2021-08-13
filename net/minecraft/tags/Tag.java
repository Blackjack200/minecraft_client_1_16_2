package net.minecraft.tags;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import java.util.stream.Stream;
import java.util.Iterator;
import java.util.function.Consumer;
import com.google.common.collect.ImmutableSet;
import java.util.function.Function;
import com.google.common.collect.Lists;
import java.util.Optional;
import com.mojang.serialization.DataResult;
import java.util.Set;
import java.util.Random;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import com.mojang.serialization.Codec;
import java.util.function.Supplier;

public interface Tag<T> {
    default <T> Codec<Tag<T>> codec(final Supplier<TagCollection<T>> supplier) {
        return (Codec<Tag<T>>)ResourceLocation.CODEC.flatXmap(vk -> (DataResult)Optional.ofNullable(((TagCollection)supplier.get()).getTag(vk)).map(DataResult::success).orElseGet(() -> DataResult.error(new StringBuilder().append("Unknown tag: ").append(vk).toString())), aej -> (DataResult)Optional.ofNullable(((TagCollection)supplier.get()).getId(aej)).map(DataResult::success).orElseGet(() -> DataResult.error(new StringBuilder().append("Unknown tag: ").append(aej).toString())));
    }
    
    boolean contains(final T object);
    
    List<T> getValues();
    
    default T getRandomElement(final Random random) {
        final List<T> list3 = this.getValues();
        return (T)list3.get(random.nextInt(list3.size()));
    }
    
    default <T> Tag<T> fromSet(final Set<T> set) {
        return SetTag.<T>create(set);
    }
    
    public static class BuilderEntry {
        private final Entry entry;
        private final String source;
        
        private BuilderEntry(final Entry d, final String string) {
            this.entry = d;
            this.source = string;
        }
        
        public Entry getEntry() {
            return this.entry;
        }
        
        public String toString() {
            return this.entry.toString() + " (from " + this.source + ")";
        }
    }
    
    public static class Builder {
        private final List<BuilderEntry> entries;
        
        public Builder() {
            this.entries = (List<BuilderEntry>)Lists.newArrayList();
        }
        
        public static Builder tag() {
            return new Builder();
        }
        
        public Builder add(final BuilderEntry b) {
            this.entries.add(b);
            return this;
        }
        
        public Builder add(final Entry d, final String string) {
            return this.add(new BuilderEntry(d, string));
        }
        
        public Builder addElement(final ResourceLocation vk, final String string) {
            return this.add(new ElementEntry(vk), string);
        }
        
        public Builder addTag(final ResourceLocation vk, final String string) {
            return this.add(new TagEntry(vk), string);
        }
        
        public <T> Optional<Tag<T>> build(final Function<ResourceLocation, Tag<T>> function1, final Function<ResourceLocation, T> function2) {
            final ImmutableSet.Builder<T> builder4 = (ImmutableSet.Builder<T>)ImmutableSet.builder();
            for (final BuilderEntry b6 : this.entries) {
                if (!b6.getEntry().<T>build(function1, function2, (java.util.function.Consumer<T>)builder4::add)) {
                    return (Optional<Tag<T>>)Optional.empty();
                }
            }
            return (Optional<Tag<T>>)Optional.of(Tag.fromSet((java.util.Set<Object>)builder4.build()));
        }
        
        public Stream<BuilderEntry> getEntries() {
            return (Stream<BuilderEntry>)this.entries.stream();
        }
        
        public <T> Stream<BuilderEntry> getUnresolvedEntries(final Function<ResourceLocation, Tag<T>> function1, final Function<ResourceLocation, T> function2) {
            return (Stream<BuilderEntry>)this.getEntries().filter(b -> !b.getEntry().build((java.util.function.Function<ResourceLocation, Tag<Object>>)function1, (java.util.function.Function<ResourceLocation, Object>)function2, (java.util.function.Consumer<Object>)(object -> {})));
        }
        
        public Builder addFromJson(final JsonObject jsonObject, final String string) {
            final JsonArray jsonArray4 = GsonHelper.getAsJsonArray(jsonObject, "values");
            final List<Entry> list5 = (List<Entry>)Lists.newArrayList();
            for (final JsonElement jsonElement7 : jsonArray4) {
                list5.add(parseEntry(jsonElement7));
            }
            if (GsonHelper.getAsBoolean(jsonObject, "replace", false)) {
                this.entries.clear();
            }
            list5.forEach(d -> this.entries.add(new BuilderEntry(d, string)));
            return this;
        }
        
        private static Entry parseEntry(final JsonElement jsonElement) {
            String string2;
            boolean boolean3;
            if (jsonElement.isJsonObject()) {
                final JsonObject jsonObject4 = jsonElement.getAsJsonObject();
                string2 = GsonHelper.getAsString(jsonObject4, "id");
                boolean3 = GsonHelper.getAsBoolean(jsonObject4, "required", true);
            }
            else {
                string2 = GsonHelper.convertToString(jsonElement, "id");
                boolean3 = true;
            }
            if (string2.startsWith("#")) {
                final ResourceLocation vk4 = new ResourceLocation(string2.substring(1));
                return boolean3 ? new TagEntry(vk4) : new OptionalTagEntry(vk4);
            }
            final ResourceLocation vk4 = new ResourceLocation(string2);
            return boolean3 ? new ElementEntry(vk4) : new OptionalElementEntry(vk4);
        }
        
        public JsonObject serializeToJson() {
            final JsonObject jsonObject2 = new JsonObject();
            final JsonArray jsonArray3 = new JsonArray();
            for (final BuilderEntry b5 : this.entries) {
                b5.getEntry().serializeTo(jsonArray3);
            }
            jsonObject2.addProperty("replace", Boolean.valueOf(false));
            jsonObject2.add("values", (JsonElement)jsonArray3);
            return jsonObject2;
        }
    }
    
    public static class ElementEntry implements Entry {
        private final ResourceLocation id;
        
        public ElementEntry(final ResourceLocation vk) {
            this.id = vk;
        }
        
        public <T> boolean build(final Function<ResourceLocation, Tag<T>> function1, final Function<ResourceLocation, T> function2, final Consumer<T> consumer) {
            final T object5 = (T)function2.apply(this.id);
            if (object5 == null) {
                return false;
            }
            consumer.accept(object5);
            return true;
        }
        
        public void serializeTo(final JsonArray jsonArray) {
            jsonArray.add(this.id.toString());
        }
        
        public String toString() {
            return this.id.toString();
        }
    }
    
    public static class OptionalElementEntry implements Entry {
        private final ResourceLocation id;
        
        public OptionalElementEntry(final ResourceLocation vk) {
            this.id = vk;
        }
        
        public <T> boolean build(final Function<ResourceLocation, Tag<T>> function1, final Function<ResourceLocation, T> function2, final Consumer<T> consumer) {
            final T object5 = (T)function2.apply(this.id);
            if (object5 != null) {
                consumer.accept(object5);
            }
            return true;
        }
        
        public void serializeTo(final JsonArray jsonArray) {
            final JsonObject jsonObject3 = new JsonObject();
            jsonObject3.addProperty("id", this.id.toString());
            jsonObject3.addProperty("required", Boolean.valueOf(false));
            jsonArray.add((JsonElement)jsonObject3);
        }
        
        public String toString() {
            return this.id.toString() + "?";
        }
    }
    
    public static class TagEntry implements Entry {
        private final ResourceLocation id;
        
        public TagEntry(final ResourceLocation vk) {
            this.id = vk;
        }
        
        public <T> boolean build(final Function<ResourceLocation, Tag<T>> function1, final Function<ResourceLocation, T> function2, final Consumer<T> consumer) {
            final Tag<T> aej5 = (Tag<T>)function1.apply(this.id);
            if (aej5 == null) {
                return false;
            }
            aej5.getValues().forEach((Consumer)consumer);
            return true;
        }
        
        public void serializeTo(final JsonArray jsonArray) {
            jsonArray.add(new StringBuilder().append("#").append(this.id).toString());
        }
        
        public String toString() {
            return new StringBuilder().append("#").append(this.id).toString();
        }
    }
    
    public static class OptionalTagEntry implements Entry {
        private final ResourceLocation id;
        
        public OptionalTagEntry(final ResourceLocation vk) {
            this.id = vk;
        }
        
        public <T> boolean build(final Function<ResourceLocation, Tag<T>> function1, final Function<ResourceLocation, T> function2, final Consumer<T> consumer) {
            final Tag<T> aej5 = (Tag<T>)function1.apply(this.id);
            if (aej5 != null) {
                aej5.getValues().forEach((Consumer)consumer);
            }
            return true;
        }
        
        public void serializeTo(final JsonArray jsonArray) {
            final JsonObject jsonObject3 = new JsonObject();
            jsonObject3.addProperty("id", new StringBuilder().append("#").append(this.id).toString());
            jsonObject3.addProperty("required", Boolean.valueOf(false));
            jsonArray.add((JsonElement)jsonObject3);
        }
        
        public String toString() {
            return new StringBuilder().append("#").append(this.id).append("?").toString();
        }
    }
    
    public interface Named<T> extends Tag<T> {
        ResourceLocation getName();
    }
    
    public interface Entry {
         <T> boolean build(final Function<ResourceLocation, Tag<T>> function1, final Function<ResourceLocation, T> function2, final Consumer<T> consumer);
        
        void serializeTo(final JsonArray jsonArray);
    }
}
