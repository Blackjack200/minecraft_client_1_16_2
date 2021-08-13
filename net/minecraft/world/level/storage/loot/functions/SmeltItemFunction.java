package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import java.util.function.Function;
import java.util.Optional;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.apache.logging.log4j.Logger;

public class SmeltItemFunction extends LootItemConditionalFunction {
    private static final Logger LOGGER;
    
    private SmeltItemFunction(final LootItemCondition[] arr) {
        super(arr);
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.FURNACE_SMELT;
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        if (bly.isEmpty()) {
            return bly;
        }
        final Optional<SmeltingRecipe> optional4 = cys.getLevel().getRecipeManager().<SimpleContainer, SmeltingRecipe>getRecipeFor(RecipeType.SMELTING, new SimpleContainer(new ItemStack[] { bly }), cys.getLevel());
        if (optional4.isPresent()) {
            final ItemStack bly2 = ((SmeltingRecipe)optional4.get()).getResultItem();
            if (!bly2.isEmpty()) {
                final ItemStack bly3 = bly2.copy();
                bly3.setCount(bly.getCount());
                return bly3;
            }
        }
        SmeltItemFunction.LOGGER.warn("Couldn't smelt {} because there is no smelting recipe", bly);
        return bly;
    }
    
    public static Builder<?> smelted() {
        return LootItemConditionalFunction.simpleBuilder((Function<LootItemCondition[], LootItemFunction>)SmeltItemFunction::new);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<SmeltItemFunction> {
        @Override
        public SmeltItemFunction deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            return new SmeltItemFunction(arr, null);
        }
    }
}
