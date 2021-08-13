package net.minecraft.world.level.storage.loot.predicates;

import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import net.minecraft.core.Registry;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import net.minecraft.world.item.enchantment.Enchantment;

public class BonusLevelTableCondition implements LootItemCondition {
    private final Enchantment enchantment;
    private final float[] values;
    
    private BonusLevelTableCondition(final Enchantment bpp, final float[] arr) {
        this.enchantment = bpp;
        this.values = arr;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.TABLE_BONUS;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of(LootContextParams.TOOL);
    }
    
    public boolean test(final LootContext cys) {
        final ItemStack bly3 = cys.<ItemStack>getParamOrNull(LootContextParams.TOOL);
        final int integer4 = (bly3 != null) ? EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, bly3) : 0;
        final float float5 = this.values[Math.min(integer4, this.values.length - 1)];
        return cys.getRandom().nextFloat() < float5;
    }
    
    public static Builder bonusLevelFlatChance(final Enchantment bpp, final float... arr) {
        return () -> new BonusLevelTableCondition(bpp, arr);
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<BonusLevelTableCondition> {
        public void serialize(final JsonObject jsonObject, final BonusLevelTableCondition dbc, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("enchantment", Registry.ENCHANTMENT.getKey(dbc.enchantment).toString());
            jsonObject.add("chances", jsonSerializationContext.serialize(dbc.values));
        }
        
        public BonusLevelTableCondition deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final ResourceLocation vk4 = new ResourceLocation(GsonHelper.getAsString(jsonObject, "enchantment"));
            final Enchantment bpp5 = (Enchantment)Registry.ENCHANTMENT.getOptional(vk4).orElseThrow(() -> new JsonParseException(new StringBuilder().append("Invalid enchantment id: ").append(vk4).toString()));
            final float[] arr6 = GsonHelper.<float[]>getAsObject(jsonObject, "chances", jsonDeserializationContext, (java.lang.Class<? extends float[]>)float[].class);
            return new BonusLevelTableCondition(bpp5, arr6, null);
        }
    }
}
