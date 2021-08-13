package net.minecraft.advancements.critereon;

import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonArray;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.Registry;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import com.google.gson.JsonElement;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentPredicate {
    public static final EnchantmentPredicate ANY;
    public static final EnchantmentPredicate[] NONE;
    private final Enchantment enchantment;
    private final MinMaxBounds.Ints level;
    
    public EnchantmentPredicate() {
        this.enchantment = null;
        this.level = MinMaxBounds.Ints.ANY;
    }
    
    public EnchantmentPredicate(@Nullable final Enchantment bpp, final MinMaxBounds.Ints d) {
        this.enchantment = bpp;
        this.level = d;
    }
    
    public boolean containedIn(final Map<Enchantment, Integer> map) {
        if (this.enchantment != null) {
            if (!map.containsKey(this.enchantment)) {
                return false;
            }
            final int integer3 = (int)map.get(this.enchantment);
            if (this.level != null && !this.level.matches(integer3)) {
                return false;
            }
        }
        else if (this.level != null) {
            for (final Integer integer4 : map.values()) {
                if (this.level.matches(integer4)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public JsonElement serializeToJson() {
        if (this == EnchantmentPredicate.ANY) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        final JsonObject jsonObject2 = new JsonObject();
        if (this.enchantment != null) {
            jsonObject2.addProperty("enchantment", Registry.ENCHANTMENT.getKey(this.enchantment).toString());
        }
        jsonObject2.add("levels", this.level.serializeToJson());
        return (JsonElement)jsonObject2;
    }
    
    public static EnchantmentPredicate fromJson(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return EnchantmentPredicate.ANY;
        }
        final JsonObject jsonObject2 = GsonHelper.convertToJsonObject(jsonElement, "enchantment");
        Enchantment bpp3 = null;
        if (jsonObject2.has("enchantment")) {
            final ResourceLocation vk4 = new ResourceLocation(GsonHelper.getAsString(jsonObject2, "enchantment"));
            bpp3 = (Enchantment)Registry.ENCHANTMENT.getOptional(vk4).orElseThrow(() -> new JsonSyntaxException(new StringBuilder().append("Unknown enchantment '").append(vk4).append("'").toString()));
        }
        final MinMaxBounds.Ints d4 = MinMaxBounds.Ints.fromJson(jsonObject2.get("levels"));
        return new EnchantmentPredicate(bpp3, d4);
    }
    
    public static EnchantmentPredicate[] fromJsonArray(@Nullable final JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return EnchantmentPredicate.NONE;
        }
        final JsonArray jsonArray2 = GsonHelper.convertToJsonArray(jsonElement, "enchantments");
        final EnchantmentPredicate[] arr3 = new EnchantmentPredicate[jsonArray2.size()];
        for (int integer4 = 0; integer4 < arr3.length; ++integer4) {
            arr3[integer4] = fromJson(jsonArray2.get(integer4));
        }
        return arr3;
    }
    
    static {
        ANY = new EnchantmentPredicate();
        NONE = new EnchantmentPredicate[0];
    }
}
