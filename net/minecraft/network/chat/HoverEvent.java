package net.minecraft.network.chat;

import com.google.common.collect.ImmutableMap;
import java.util.stream.Stream;
import java.util.function.Function;
import java.util.Map;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import java.util.List;
import java.util.UUID;
import net.minecraft.world.entity.EntityType;
import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonElement;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonObject;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;

public class HoverEvent {
    private static final Logger LOGGER;
    private final Action<?> action;
    private final Object value;
    
    public <T> HoverEvent(final Action<T> a, final T object) {
        this.action = a;
        this.value = object;
    }
    
    public Action<?> getAction() {
        return this.action;
    }
    
    @Nullable
    public <T> T getValue(final Action<T> a) {
        if (this.action == a) {
            return (T)((Action<Object>)a).cast(this.value);
        }
        return null;
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final HoverEvent nv3 = (HoverEvent)object;
        return this.action == nv3.action && Objects.equals(this.value, nv3.value);
    }
    
    public String toString() {
        return new StringBuilder().append("HoverEvent{action=").append(this.action).append(", value='").append(this.value).append('\'').append('}').toString();
    }
    
    public int hashCode() {
        int integer2 = this.action.hashCode();
        integer2 = 31 * integer2 + ((this.value != null) ? this.value.hashCode() : 0);
        return integer2;
    }
    
    @Nullable
    public static HoverEvent deserialize(final JsonObject jsonObject) {
        final String string2 = GsonHelper.getAsString(jsonObject, "action", (String)null);
        if (string2 == null) {
            return null;
        }
        final Action<?> a3 = Action.getByName(string2);
        if (a3 == null) {
            return null;
        }
        final JsonElement jsonElement4 = jsonObject.get("contents");
        if (jsonElement4 != null) {
            return a3.deserialize(jsonElement4);
        }
        final Component nr5 = Component.Serializer.fromJson(jsonObject.get("value"));
        if (nr5 != null) {
            return a3.deserializeFromLegacy(nr5);
        }
        return null;
    }
    
    public JsonObject serialize() {
        final JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("action", this.action.getName());
        jsonObject2.add("contents", this.action.serializeArg(this.value));
        return jsonObject2;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class EntityTooltipInfo {
        public final EntityType<?> type;
        public final UUID id;
        @Nullable
        public final Component name;
        @Nullable
        private List<Component> linesCache;
        
        public EntityTooltipInfo(final EntityType<?> aqb, final UUID uUID, @Nullable final Component nr) {
            this.type = aqb;
            this.id = uUID;
            this.name = nr;
        }
        
        @Nullable
        public static EntityTooltipInfo create(final JsonElement jsonElement) {
            if (!jsonElement.isJsonObject()) {
                return null;
            }
            final JsonObject jsonObject2 = jsonElement.getAsJsonObject();
            final EntityType<?> aqb3 = Registry.ENTITY_TYPE.get(new ResourceLocation(GsonHelper.getAsString(jsonObject2, "type")));
            final UUID uUID4 = UUID.fromString(GsonHelper.getAsString(jsonObject2, "id"));
            final Component nr5 = Component.Serializer.fromJson(jsonObject2.get("name"));
            return new EntityTooltipInfo(aqb3, uUID4, nr5);
        }
        
        @Nullable
        public static EntityTooltipInfo create(final Component nr) {
            try {
                final CompoundTag md2 = TagParser.parseTag(nr.getString());
                final Component nr2 = Component.Serializer.fromJson(md2.getString("name"));
                final EntityType<?> aqb4 = Registry.ENTITY_TYPE.get(new ResourceLocation(md2.getString("type")));
                final UUID uUID5 = UUID.fromString(md2.getString("id"));
                return new EntityTooltipInfo(aqb4, uUID5, nr2);
            }
            catch (JsonSyntaxException | CommandSyntaxException ex2) {
                final Exception ex;
                final Exception exception2 = ex;
                return null;
            }
        }
        
        public JsonElement serialize() {
            final JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("type", Registry.ENTITY_TYPE.getKey(this.type).toString());
            jsonObject2.addProperty("id", this.id.toString());
            if (this.name != null) {
                jsonObject2.add("name", Component.Serializer.toJsonTree(this.name));
            }
            return (JsonElement)jsonObject2;
        }
        
        public List<Component> getTooltipLines() {
            if (this.linesCache == null) {
                this.linesCache = (List<Component>)Lists.newArrayList();
                if (this.name != null) {
                    this.linesCache.add(this.name);
                }
                this.linesCache.add(new TranslatableComponent("gui.entity_tooltip.type", new Object[] { this.type.getDescription() }));
                this.linesCache.add(new TextComponent(this.id.toString()));
            }
            return this.linesCache;
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final EntityTooltipInfo b3 = (EntityTooltipInfo)object;
            return this.type.equals(b3.type) && this.id.equals(b3.id) && Objects.equals(this.name, b3.name);
        }
        
        public int hashCode() {
            int integer2 = this.type.hashCode();
            integer2 = 31 * integer2 + this.id.hashCode();
            integer2 = 31 * integer2 + ((this.name != null) ? this.name.hashCode() : 0);
            return integer2;
        }
    }
    
    public static class ItemStackInfo {
        private final Item item;
        private final int count;
        @Nullable
        private final CompoundTag tag;
        @Nullable
        private ItemStack itemStack;
        
        ItemStackInfo(final Item blu, final int integer, @Nullable final CompoundTag md) {
            this.item = blu;
            this.count = integer;
            this.tag = md;
        }
        
        public ItemStackInfo(final ItemStack bly) {
            this(bly.getItem(), bly.getCount(), (bly.getTag() != null) ? bly.getTag().copy() : null);
        }
        
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            final ItemStackInfo c3 = (ItemStackInfo)object;
            return this.count == c3.count && this.item.equals(c3.item) && Objects.equals(this.tag, c3.tag);
        }
        
        public int hashCode() {
            int integer2 = this.item.hashCode();
            integer2 = 31 * integer2 + this.count;
            integer2 = 31 * integer2 + ((this.tag != null) ? this.tag.hashCode() : 0);
            return integer2;
        }
        
        public ItemStack getItemStack() {
            if (this.itemStack == null) {
                this.itemStack = new ItemStack(this.item, this.count);
                if (this.tag != null) {
                    this.itemStack.setTag(this.tag);
                }
            }
            return this.itemStack;
        }
        
        private static ItemStackInfo create(final JsonElement jsonElement) {
            if (jsonElement.isJsonPrimitive()) {
                return new ItemStackInfo(Registry.ITEM.get(new ResourceLocation(jsonElement.getAsString())), 1, null);
            }
            final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "item");
            final Item blu3 = Registry.ITEM.get(new ResourceLocation(GsonHelper.getAsString(jsonObject2, "id")));
            final int integer4 = GsonHelper.getAsInt(jsonObject2, "count", 1);
            if (jsonObject2.has("tag")) {
                final String string5 = GsonHelper.getAsString(jsonObject2, "tag");
                try {
                    final CompoundTag md6 = TagParser.parseTag(string5);
                    return new ItemStackInfo(blu3, integer4, md6);
                }
                catch (CommandSyntaxException commandSyntaxException6) {
                    HoverEvent.LOGGER.warn("Failed to parse tag: {}", string5, commandSyntaxException6);
                }
            }
            return new ItemStackInfo(blu3, integer4, null);
        }
        
        @Nullable
        private static ItemStackInfo create(final Component nr) {
            try {
                final CompoundTag md2 = TagParser.parseTag(nr.getString());
                return new ItemStackInfo(ItemStack.of(md2));
            }
            catch (CommandSyntaxException commandSyntaxException2) {
                HoverEvent.LOGGER.warn("Failed to parse item tag: {}", nr, commandSyntaxException2);
                return null;
            }
        }
        
        private JsonElement serialize() {
            final JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("id", Registry.ITEM.getKey(this.item).toString());
            if (this.count != 1) {
                jsonObject2.addProperty("count", (Number)this.count);
            }
            if (this.tag != null) {
                jsonObject2.addProperty("tag", this.tag.toString());
            }
            return (JsonElement)jsonObject2;
        }
    }
    
    public static class Action<T> {
        public static final Action<Component> SHOW_TEXT;
        public static final Action<ItemStackInfo> SHOW_ITEM;
        public static final Action<EntityTooltipInfo> SHOW_ENTITY;
        private static final Map<String, Action> LOOKUP;
        private final String name;
        private final boolean allowFromServer;
        private final Function<JsonElement, T> argDeserializer;
        private final Function<T, JsonElement> argSerializer;
        private final Function<Component, T> legacyArgDeserializer;
        
        public Action(final String string, final boolean boolean2, final Function<JsonElement, T> function3, final Function<T, JsonElement> function4, final Function<Component, T> function5) {
            this.name = string;
            this.allowFromServer = boolean2;
            this.argDeserializer = function3;
            this.argSerializer = function4;
            this.legacyArgDeserializer = function5;
        }
        
        public boolean isAllowedFromServer() {
            return this.allowFromServer;
        }
        
        public String getName() {
            return this.name;
        }
        
        @Nullable
        public static Action getByName(final String string) {
            return (Action)Action.LOOKUP.get(string);
        }
        
        private T cast(final Object object) {
            return (T)object;
        }
        
        @Nullable
        public HoverEvent deserialize(final JsonElement jsonElement) {
            final T object3 = (T)this.argDeserializer.apply(jsonElement);
            if (object3 == null) {
                return null;
            }
            return new HoverEvent((Action<T>)this, (T)object3);
        }
        
        @Nullable
        public HoverEvent deserializeFromLegacy(final Component nr) {
            final T object3 = (T)this.legacyArgDeserializer.apply(nr);
            if (object3 == null) {
                return null;
            }
            return new HoverEvent((Action<T>)this, (T)object3);
        }
        
        public JsonElement serializeArg(final Object object) {
            return (JsonElement)this.argSerializer.apply(this.cast(object));
        }
        
        public String toString() {
            return "<action " + this.name + ">";
        }
        
        static {
            SHOW_TEXT = new Action<Component>("show_text", true, (java.util.function.Function<JsonElement, Component>)Component.Serializer::fromJson, (java.util.function.Function<Component, JsonElement>)Component.Serializer::toJsonTree, (java.util.function.Function<Component, Component>)Function.identity());
            SHOW_ITEM = new Action<ItemStackInfo>("show_item", true, (java.util.function.Function<JsonElement, ItemStackInfo>)(jsonElement -> create(jsonElement)), (java.util.function.Function<ItemStackInfo, JsonElement>)(object -> ((ItemStackInfo)object).serialize()), (java.util.function.Function<Component, ItemStackInfo>)(nr -> create(nr)));
            SHOW_ENTITY = new Action<EntityTooltipInfo>("show_entity", true, (java.util.function.Function<JsonElement, EntityTooltipInfo>)EntityTooltipInfo::create, (java.util.function.Function<EntityTooltipInfo, JsonElement>)EntityTooltipInfo::serialize, (java.util.function.Function<Component, EntityTooltipInfo>)EntityTooltipInfo::create);
            LOOKUP = (Map)Stream.of((Object[])new Action[] { Action.SHOW_TEXT, Action.SHOW_ITEM, Action.SHOW_ENTITY }).collect(ImmutableMap.toImmutableMap(Action::getName, a -> a));
        }
    }
}
