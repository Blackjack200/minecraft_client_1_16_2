package net.minecraft.world.level.storage.loot.predicates;

import com.google.gson.JsonSyntaxException;
import java.util.function.Consumer;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import net.minecraft.core.Registry;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.block.Block;

public class LootItemBlockStatePropertyCondition implements LootItemCondition {
    private final Block block;
    private final StatePropertiesPredicate properties;
    
    private LootItemBlockStatePropertyCondition(final Block bul, final StatePropertiesPredicate cm) {
        this.block = bul;
        this.properties = cm;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.BLOCK_STATE_PROPERTY;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.BLOCK_STATE);
    }
    
    public boolean test(final LootContext cys) {
        final BlockState cee3 = cys.<BlockState>getParamOrNull(LootContextParams.BLOCK_STATE);
        return cee3 != null && this.block == cee3.getBlock() && this.properties.matches(cee3);
    }
    
    public static Builder hasBlockStateProperties(final Block bul) {
        return new Builder(bul);
    }
    
    public static class Builder implements LootItemCondition.Builder {
        private final Block block;
        private StatePropertiesPredicate properties;
        
        public Builder(final Block bul) {
            this.properties = StatePropertiesPredicate.ANY;
            this.block = bul;
        }
        
        public Builder setProperties(final StatePropertiesPredicate.Builder a) {
            this.properties = a.build();
            return this;
        }
        
        public LootItemCondition build() {
            return new LootItemBlockStatePropertyCondition(this.block, this.properties, null);
        }
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<LootItemBlockStatePropertyCondition> {
        public void serialize(final JsonObject jsonObject, final LootItemBlockStatePropertyCondition dbk, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("block", Registry.BLOCK.getKey(dbk.block).toString());
            jsonObject.add("properties", dbk.properties.serializeToJson());
        }
        
        public LootItemBlockStatePropertyCondition deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final ResourceLocation vk4 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "block"));
            final Block bul5 = (Block)Registry.BLOCK.getOptional(vk4).orElseThrow(() -> new IllegalArgumentException(new StringBuilder().append("Can't find block ").append(vk4).toString()));
            final StatePropertiesPredicate cm6 = StatePropertiesPredicate.fromJson(jsonObject.get("properties"));
            cm6.checkState(bul5.getStateDefinition(), (Consumer<String>)(string -> {
                throw new JsonSyntaxException(new StringBuilder().append("Block ").append(bul5).append(" has no property ").append(string).toString());
            }));
            return new LootItemBlockStatePropertyCondition(bul5, cm6, null);
        }
    }
}
