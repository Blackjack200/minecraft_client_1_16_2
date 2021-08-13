package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import java.util.function.Function;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.RandomValueBounds;
import org.apache.logging.log4j.Logger;

public class SetItemDamageFunction extends LootItemConditionalFunction {
    private static final Logger LOGGER;
    private final RandomValueBounds damage;
    
    private SetItemDamageFunction(final LootItemCondition[] arr, final RandomValueBounds cza) {
        super(arr);
        this.damage = cza;
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_DAMAGE;
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        if (bly.isDamageableItem()) {
            final float float4 = 1.0f - this.damage.getFloat(cys.getRandom());
            bly.setDamageValue(Mth.floor(float4 * bly.getMaxDamage()));
        }
        else {
            SetItemDamageFunction.LOGGER.warn("Couldn't set damage of loot item {}", bly);
        }
        return bly;
    }
    
    public static Builder<?> setDamage(final RandomValueBounds cza) {
        return LootItemConditionalFunction.simpleBuilder((Function<LootItemCondition[], LootItemFunction>)(arr -> new SetItemDamageFunction(arr, cza)));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<SetItemDamageFunction> {
        @Override
        public void serialize(final JsonObject jsonObject, final SetItemDamageFunction dao, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, dao, jsonSerializationContext);
            jsonObject.add("damage", jsonSerializationContext.serialize(dao.damage));
        }
        
        @Override
        public SetItemDamageFunction deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            return new SetItemDamageFunction(arr, GsonHelper.<RandomValueBounds>getAsObject(jsonObject, "damage", jsonDeserializationContext, (java.lang.Class<? extends RandomValueBounds>)RandomValueBounds.class), null);
        }
    }
}
