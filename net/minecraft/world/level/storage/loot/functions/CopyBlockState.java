package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import net.minecraft.core.Registry;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Sets;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Set;
import net.minecraft.world.level.block.Block;

public class CopyBlockState extends LootItemConditionalFunction {
    private final Block block;
    private final Set<Property<?>> properties;
    
    private CopyBlockState(final LootItemCondition[] arr, final Block bul, final Set<Property<?>> set) {
        super(arr);
        this.block = bul;
        this.properties = set;
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.COPY_STATE;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.BLOCK_STATE);
    }
    
    @Override
    protected ItemStack run(final ItemStack bly, final LootContext cys) {
        final BlockState cee4 = cys.<BlockState>getParamOrNull(LootContextParams.BLOCK_STATE);
        if (cee4 != null) {
            final CompoundTag md5 = bly.getOrCreateTag();
            CompoundTag md6;
            if (md5.contains("BlockStateTag", 10)) {
                md6 = md5.getCompound("BlockStateTag");
            }
            else {
                md6 = new CompoundTag();
                md5.put("BlockStateTag", (Tag)md6);
            }
            this.properties.stream().filter(cee4::hasProperty).forEach(cfg -> md6.putString(cfg.getName(), CopyBlockState.<Comparable>serialize(cee4, (Property<Comparable>)cfg)));
        }
        return bly;
    }
    
    public static Builder copyState(final Block bul) {
        return new Builder(bul);
    }
    
    private static <T extends Comparable<T>> String serialize(final BlockState cee, final Property<T> cfg) {
        final T comparable3 = cee.<T>getValue(cfg);
        return cfg.getName(comparable3);
    }
    
    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        private final Block block;
        private final Set<Property<?>> properties;
        
        private Builder(final Block bul) {
            this.properties = (Set<Property<?>>)Sets.newHashSet();
            this.block = bul;
        }
        
        public Builder copy(final Property<?> cfg) {
            if (!this.block.getStateDefinition().getProperties().contains(cfg)) {
                throw new IllegalStateException(new StringBuilder().append("Property ").append(cfg).append(" is not present on block ").append(this.block).toString());
            }
            this.properties.add(cfg);
            return this;
        }
        
        @Override
        protected Builder getThis() {
            return this;
        }
        
        public LootItemFunction build() {
            return new CopyBlockState(this.getConditions(), this.block, this.properties, null);
        }
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<CopyBlockState> {
        @Override
        public void serialize(final JsonObject jsonObject, final CopyBlockState czw, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, czw, jsonSerializationContext);
            jsonObject.addProperty("block", Registry.BLOCK.getKey(czw.block).toString());
            final JsonArray jsonArray5 = new JsonArray();
            czw.properties.forEach(cfg -> jsonArray5.add(cfg.getName()));
            jsonObject.add("properties", (JsonElement)jsonArray5);
        }
        
        @Override
        public CopyBlockState deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final ResourceLocation vk5 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "block"));
            final Block bul6 = (Block)Registry.BLOCK.getOptional(vk5).orElseThrow(() -> new IllegalArgumentException(new StringBuilder().append("Can't find block ").append(vk5).toString()));
            final StateDefinition<Block, BlockState> cef7 = bul6.getStateDefinition();
            final Set<Property<?>> set8 = (Set<Property<?>>)Sets.newHashSet();
            final JsonArray jsonArray9 = GsonHelper.getAsJsonArray(jsonObject, "properties", (JsonArray)null);
            if (jsonArray9 != null) {
                jsonArray9.forEach(jsonElement -> set8.add(cef7.getProperty(GsonHelper.convertToString(jsonElement, "property"))));
            }
            return new CopyBlockState(arr, bul6, set8, null);
        }
    }
}
