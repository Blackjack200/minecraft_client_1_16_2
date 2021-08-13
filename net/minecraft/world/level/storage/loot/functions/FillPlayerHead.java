package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.LootContext;

public class FillPlayerHead extends LootItemConditionalFunction {
    private final LootContext.EntityTarget entityTarget;
    
    public FillPlayerHead(final LootItemCondition[] arr, final LootContext.EntityTarget c) {
        super(arr);
        this.entityTarget = c;
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.FILL_PLAYER_HEAD;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)ImmutableSet.of(this.entityTarget.getParam());
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        if (bly.getItem() == Items.PLAYER_HEAD) {
            final Entity apx4 = cys.<Entity>getParamOrNull(this.entityTarget.getParam());
            if (apx4 instanceof Player) {
                final GameProfile gameProfile5 = ((Player)apx4).getGameProfile();
                bly.getOrCreateTag().put("SkullOwner", (Tag)NbtUtils.writeGameProfile(new CompoundTag(), gameProfile5));
            }
        }
        return bly;
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<FillPlayerHead> {
        @Override
        public void serialize(final JsonObject jsonObject, final FillPlayerHead dac, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, dac, jsonSerializationContext);
            jsonObject.add("entity", jsonSerializationContext.serialize(dac.entityTarget));
        }
        
        @Override
        public FillPlayerHead deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final LootContext.EntityTarget c5 = GsonHelper.<LootContext.EntityTarget>getAsObject(jsonObject, "entity", jsonDeserializationContext, (java.lang.Class<? extends LootContext.EntityTarget>)LootContext.EntityTarget.class);
            return new FillPlayerHead(arr, c5);
        }
    }
}
