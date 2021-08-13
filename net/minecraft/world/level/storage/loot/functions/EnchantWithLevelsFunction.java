package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import net.minecraft.world.level.storage.loot.RandomIntGenerators;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import java.util.Random;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.RandomIntGenerator;

public class EnchantWithLevelsFunction extends LootItemConditionalFunction {
    private final RandomIntGenerator levels;
    private final boolean treasure;
    
    private EnchantWithLevelsFunction(final LootItemCondition[] arr, final RandomIntGenerator cyy, final boolean boolean3) {
        super(arr);
        this.levels = cyy;
        this.treasure = boolean3;
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.ENCHANT_WITH_LEVELS;
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        final Random random4 = cys.getRandom();
        return EnchantmentHelper.enchantItem(random4, bly, this.levels.getInt(random4), this.treasure);
    }
    
    public static Builder enchantWithLevels(final RandomIntGenerator cyy) {
        return new Builder(cyy);
    }
    
    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        private final RandomIntGenerator levels;
        private boolean treasure;
        
        public Builder(final RandomIntGenerator cyy) {
            this.levels = cyy;
        }
        
        @Override
        protected Builder getThis() {
            return this;
        }
        
        public Builder allowTreasure() {
            this.treasure = true;
            return this;
        }
        
        public LootItemFunction build() {
            return new EnchantWithLevelsFunction(this.getConditions(), this.levels, this.treasure, null);
        }
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<EnchantWithLevelsFunction> {
        @Override
        public void serialize(final JsonObject jsonObject, final EnchantWithLevelsFunction daa, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, daa, jsonSerializationContext);
            jsonObject.add("levels", RandomIntGenerators.serialize(daa.levels, jsonSerializationContext));
            jsonObject.addProperty("treasure", Boolean.valueOf(daa.treasure));
        }
        
        @Override
        public EnchantWithLevelsFunction deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final RandomIntGenerator cyy5 = RandomIntGenerators.deserialize(jsonObject.get("levels"), jsonDeserializationContext);
            final boolean boolean6 = GsonHelper.getAsBoolean(jsonObject, "treasure", false);
            return new EnchantWithLevelsFunction(arr, cyy5, boolean6, null);
        }
    }
}
