package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.annotations.VisibleForTesting;
import java.util.Set;
import java.util.Map;
import com.google.common.collect.Streams;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializer;
import java.util.function.Predicate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.client.renderer.block.model.MultiVariant;

public class Selector {
    private final Condition condition;
    private final MultiVariant variant;
    
    public Selector(final Condition ebi, final MultiVariant ebf) {
        if (ebi == null) {
            throw new IllegalArgumentException("Missing condition for selector");
        }
        if (ebf == null) {
            throw new IllegalArgumentException("Missing variant for selector");
        }
        this.condition = ebi;
        this.variant = ebf;
    }
    
    public MultiVariant getVariant() {
        return this.variant;
    }
    
    public Predicate<BlockState> getPredicate(final StateDefinition<Block, BlockState> cef) {
        return this.condition.getPredicate(cef);
    }
    
    public boolean equals(final Object object) {
        return this == object;
    }
    
    public int hashCode() {
        return System.identityHashCode(this);
    }
    
    public static class Deserializer implements JsonDeserializer<Selector> {
        public Selector deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            final JsonObject jsonObject5 = jsonElement.getAsJsonObject();
            return new Selector(this.getSelector(jsonObject5), (MultiVariant)jsonDeserializationContext.deserialize(jsonObject5.get("apply"), (Type)MultiVariant.class));
        }
        
        private Condition getSelector(final JsonObject jsonObject) {
            if (jsonObject.has("when")) {
                return getCondition(GsonHelper.getAsJsonObject(jsonObject, "when"));
            }
            return Condition.TRUE;
        }
        
        @VisibleForTesting
        static Condition getCondition(final JsonObject jsonObject) {
            final Set<Map.Entry<String, JsonElement>> set2 = (Set<Map.Entry<String, JsonElement>>)jsonObject.entrySet();
            if (set2.isEmpty()) {
                throw new JsonParseException("No elements found in selector");
            }
            if (set2.size() != 1) {
                return new AndCondition(set2.stream().map(Deserializer::getKeyValueCondition).collect(Collectors.toList()));
            }
            if (jsonObject.has("OR")) {
                final List<Condition> list3 = (List<Condition>)Streams.stream((Iterable)GsonHelper.getAsJsonArray(jsonObject, "OR")).map(jsonElement -> getCondition(jsonElement.getAsJsonObject())).collect(Collectors.toList());
                return new OrCondition(list3);
            }
            if (jsonObject.has("AND")) {
                final List<Condition> list3 = (List<Condition>)Streams.stream((Iterable)GsonHelper.getAsJsonArray(jsonObject, "AND")).map(jsonElement -> getCondition(jsonElement.getAsJsonObject())).collect(Collectors.toList());
                return new AndCondition(list3);
            }
            return getKeyValueCondition((Map.Entry<String, JsonElement>)set2.iterator().next());
        }
        
        private static Condition getKeyValueCondition(final Map.Entry<String, JsonElement> entry) {
            return new KeyValueCondition((String)entry.getKey(), ((JsonElement)entry.getValue()).getAsString());
        }
    }
}
