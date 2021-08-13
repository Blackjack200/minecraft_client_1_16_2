package net.minecraft.world.level.storage.loot.functions;

import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import java.util.function.BiFunction;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface LootItemFunction extends LootContextUser, BiFunction<ItemStack, LootContext, ItemStack> {
    LootItemFunctionType getType();
    
    default Consumer<ItemStack> decorate(final BiFunction<ItemStack, LootContext, ItemStack> biFunction, final Consumer<ItemStack> consumer, final LootContext cys) {
        return (Consumer<ItemStack>)(bly -> consumer.accept(biFunction.apply(bly, cys)));
    }
    
    public interface Builder {
        LootItemFunction build();
    }
}
