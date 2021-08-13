package net.minecraft.world.level.storage.loot.functions;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import java.util.function.Function;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.nbt.CompoundTag;

public class SetNbtFunction extends LootItemConditionalFunction {
    private final CompoundTag tag;
    
    private SetNbtFunction(final LootItemCondition[] arr, final CompoundTag md) {
        super(arr);
        this.tag = md;
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_NBT;
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        bly.getOrCreateTag().merge(this.tag);
        return bly;
    }
    
    public static Builder<?> setTag(final CompoundTag md) {
        return LootItemConditionalFunction.simpleBuilder((Function<LootItemCondition[], LootItemFunction>)(arr -> new SetNbtFunction(arr, md)));
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<SetNbtFunction> {
        @Override
        public void serialize(final JsonObject jsonObject, final SetNbtFunction dar, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, dar, jsonSerializationContext);
            jsonObject.addProperty("tag", dar.tag.toString());
        }
        
        @Override
        public SetNbtFunction deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            try {
                final CompoundTag md5 = TagParser.parseTag(GsonHelper.getAsString(jsonObject, "tag"));
                return new SetNbtFunction(arr, md5, null);
            }
            catch (CommandSyntaxException commandSyntaxException5) {
                throw new JsonSyntaxException(commandSyntaxException5.getMessage());
            }
        }
    }
}
