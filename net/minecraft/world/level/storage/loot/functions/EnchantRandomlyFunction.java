package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonSyntaxException;
import net.minecraft.util.GsonHelper;
import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import net.minecraft.resources.ResourceLocation;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonArray;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Sets;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import java.util.function.Function;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.ItemLike;
import net.minecraft.util.Mth;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class EnchantRandomlyFunction extends LootItemConditionalFunction {
    private static final Logger LOGGER;
    private final List<Enchantment> enchantments;
    
    private EnchantRandomlyFunction(final LootItemCondition[] arr, final Collection<Enchantment> collection) {
        super(arr);
        this.enchantments = (List<Enchantment>)ImmutableList.copyOf((Collection)collection);
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.ENCHANT_RANDOMLY;
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        final Random random5 = cys.getRandom();
        Enchantment bpp4;
        if (this.enchantments.isEmpty()) {
            final boolean boolean6 = bly.getItem() == Items.BOOK;
            final List<Enchantment> list7 = (List<Enchantment>)Registry.ENCHANTMENT.stream().filter(Enchantment::isDiscoverable).filter(bpp -> boolean6 || bpp.canEnchant(bly)).collect(Collectors.toList());
            if (list7.isEmpty()) {
                EnchantRandomlyFunction.LOGGER.warn("Couldn't find a compatible enchantment for {}", bly);
                return bly;
            }
            bpp4 = (Enchantment)list7.get(random5.nextInt(list7.size()));
        }
        else {
            bpp4 = (Enchantment)this.enchantments.get(random5.nextInt(this.enchantments.size()));
        }
        return enchantItem(bly, bpp4, random5);
    }
    
    private static ItemStack enchantItem(ItemStack bly, final Enchantment bpp, final Random random) {
        final int integer4 = Mth.nextInt(random, bpp.getMinLevel(), bpp.getMaxLevel());
        if (bly.getItem() == Items.BOOK) {
            bly = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantedBookItem.addEnchantment(bly, new EnchantmentInstance(bpp, integer4));
        }
        else {
            bly.enchant(bpp, integer4);
        }
        return bly;
    }
    
    public static LootItemConditionalFunction.Builder<?> randomApplicableEnchantment() {
        return LootItemConditionalFunction.simpleBuilder((Function<LootItemCondition[], LootItemFunction>)(arr -> new EnchantRandomlyFunction(arr, (Collection<Enchantment>)ImmutableList.of())));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        private final Set<Enchantment> enchantments;
        
        public Builder() {
            this.enchantments = (Set<Enchantment>)Sets.newHashSet();
        }
        
        @Override
        protected Builder getThis() {
            return this;
        }
        
        public Builder withEnchantment(final Enchantment bpp) {
            this.enchantments.add(bpp);
            return this;
        }
        
        public LootItemFunction build() {
            return new EnchantRandomlyFunction(this.getConditions(), (Collection)this.enchantments, null);
        }
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<EnchantRandomlyFunction> {
        @Override
        public void serialize(final JsonObject jsonObject, final EnchantRandomlyFunction czz, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, czz, jsonSerializationContext);
            if (!czz.enchantments.isEmpty()) {
                final JsonArray jsonArray5 = new JsonArray();
                for (final Enchantment bpp7 : czz.enchantments) {
                    final ResourceLocation vk8 = Registry.ENCHANTMENT.getKey(bpp7);
                    if (vk8 == null) {
                        throw new IllegalArgumentException(new StringBuilder().append("Don't know how to serialize enchantment ").append(bpp7).toString());
                    }
                    jsonArray5.add((JsonElement)new JsonPrimitive(vk8.toString()));
                }
                jsonObject.add("enchantments", (JsonElement)jsonArray5);
            }
        }
        
        @Override
        public EnchantRandomlyFunction deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final List<Enchantment> list5 = (List<Enchantment>)Lists.newArrayList();
            if (jsonObject.has("enchantments")) {
                final JsonArray jsonArray6 = GsonHelper.getAsJsonArray(jsonObject, "enchantments");
                for (final JsonElement jsonElement8 : jsonArray6) {
                    final String string9 = GsonHelper.convertToString(jsonElement8, "enchantment");
                    final Enchantment bpp10 = (Enchantment)Registry.ENCHANTMENT.getOptional(new ResourceLocation(string9)).orElseThrow(() -> new JsonSyntaxException("Unknown enchantment '" + string9 + "'"));
                    list5.add(bpp10);
                }
            }
            return new EnchantRandomlyFunction(arr, (Collection)list5, null);
        }
    }
}
