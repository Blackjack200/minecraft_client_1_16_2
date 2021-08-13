package net.minecraft.advancements.critereon;

import com.google.gson.JsonPrimitive;
import com.google.gson.JsonNull;
import com.google.gson.JsonSyntaxException;
import net.minecraft.tags.Tag;
import net.minecraft.core.Registry;
import net.minecraft.tags.SerializationTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import net.minecraft.world.entity.EntityType;
import com.google.common.base.Joiner;

public abstract class EntityTypePredicate {
    public static final EntityTypePredicate ANY;
    private static final Joiner COMMA_JOINER;
    
    public abstract boolean matches(final EntityType<?> aqb);
    
    public abstract JsonElement serializeToJson();
    
    public static EntityTypePredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return EntityTypePredicate.ANY;
        }
        final String string2 = GsonHelper.convertToString(jsonElement, "type");
        if (string2.startsWith("#")) {
            final ResourceLocation vk3 = new ResourceLocation(string2.substring(1));
            return new TagPredicate(SerializationTags.getInstance().getEntityTypes().getTagOrEmpty(vk3));
        }
        final ResourceLocation vk3 = new ResourceLocation(string2);
        final EntityType<?> aqb4 = Registry.ENTITY_TYPE.getOptional(vk3).orElseThrow(() -> new JsonSyntaxException(new StringBuilder().append("Unknown entity type '").append(vk3).append("', valid types are: ").append(EntityTypePredicate.COMMA_JOINER.join((Iterable)Registry.ENTITY_TYPE.keySet())).toString()));
        return new TypePredicate(aqb4);
    }
    
    public static EntityTypePredicate of(final EntityType<?> aqb) {
        return new TypePredicate(aqb);
    }
    
    public static EntityTypePredicate of(final Tag<EntityType<?>> aej) {
        return new TagPredicate(aej);
    }
    
    static {
        ANY = new EntityTypePredicate() {
            @Override
            public boolean matches(final EntityType<?> aqb) {
                return true;
            }
            
            @Override
            public JsonElement serializeToJson() {
                return (JsonElement)JsonNull.INSTANCE;
            }
        };
        COMMA_JOINER = Joiner.on(", ");
    }
    
    static class TypePredicate extends EntityTypePredicate {
        private final EntityType<?> type;
        
        public TypePredicate(final EntityType<?> aqb) {
            this.type = aqb;
        }
        
        @Override
        public boolean matches(final EntityType<?> aqb) {
            return this.type == aqb;
        }
        
        @Override
        public JsonElement serializeToJson() {
            return (JsonElement)new JsonPrimitive(Registry.ENTITY_TYPE.getKey(this.type).toString());
        }
    }
    
    static class TagPredicate extends EntityTypePredicate {
        private final Tag<EntityType<?>> tag;
        
        public TagPredicate(final Tag<EntityType<?>> aej) {
            this.tag = aej;
        }
        
        @Override
        public boolean matches(final EntityType<?> aqb) {
            return this.tag.contains(aqb);
        }
        
        @Override
        public JsonElement serializeToJson() {
            return (JsonElement)new JsonPrimitive(new StringBuilder().append("#").append(SerializationTags.getInstance().getEntityTypes().getIdOrThrow(this.tag)).toString());
        }
    }
}
