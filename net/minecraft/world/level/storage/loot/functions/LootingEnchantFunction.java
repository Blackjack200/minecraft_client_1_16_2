package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.RandomValueBounds;

public class LootingEnchantFunction extends LootItemConditionalFunction {
    private final RandomValueBounds value;
    private final int limit;
    
    private LootingEnchantFunction(final LootItemCondition[] arr, final RandomValueBounds cza, final int integer) {
        super(arr);
        this.value = cza;
        this.limit = integer;
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.LOOTING_ENCHANT;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.KILLER_ENTITY);
    }
    
    private boolean hasLimit() {
        return this.limit > 0;
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        final Entity apx4 = cys.<Entity>getParamOrNull(LootContextParams.KILLER_ENTITY);
        if (apx4 instanceof LivingEntity) {
            final int integer5 = EnchantmentHelper.getMobLooting((LivingEntity)apx4);
            if (integer5 == 0) {
                return bly;
            }
            final float float6 = integer5 * this.value.getFloat(cys.getRandom());
            bly.grow(Math.round(float6));
            if (this.hasLimit() && bly.getCount() > this.limit) {
                bly.setCount(this.limit);
            }
        }
        return bly;
    }
    
    public static Builder lootingMultiplier(final RandomValueBounds cza) {
        return new Builder(cza);
    }
    
    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        private final RandomValueBounds count;
        private int limit;
        
        public Builder(final RandomValueBounds cza) {
            this.limit = 0;
            this.count = cza;
        }
        
        @Override
        protected Builder getThis() {
            return this;
        }
        
        public Builder setLimit(final int integer) {
            this.limit = integer;
            return this;
        }
        
        public LootItemFunction build() {
            return new LootingEnchantFunction(this.getConditions(), this.count, this.limit, null);
        }
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<LootingEnchantFunction> {
        @Override
        public void serialize(final JsonObject jsonObject, final LootingEnchantFunction daj, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, daj, jsonSerializationContext);
            jsonObject.add("count", jsonSerializationContext.serialize(daj.value));
            if (daj.hasLimit()) {
                jsonObject.add("limit", jsonSerializationContext.serialize(daj.limit));
            }
        }
        
        @Override
        public LootingEnchantFunction deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final int integer5 = GsonHelper.getAsInt(jsonObject, "limit", 0);
            return new LootingEnchantFunction(arr, GsonHelper.<RandomValueBounds>getAsObject(jsonObject, "count", jsonDeserializationContext, (java.lang.Class<? extends RandomValueBounds>)RandomValueBounds.class), integer5, null);
        }
    }
}
