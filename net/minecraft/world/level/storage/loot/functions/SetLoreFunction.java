package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.Streams;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import java.util.Iterator;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import java.util.function.UnaryOperator;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import java.util.function.Function;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import javax.annotation.Nullable;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.network.chat.Component;
import java.util.List;

public class SetLoreFunction extends LootItemConditionalFunction {
    private final boolean replace;
    private final List<Component> lore;
    @Nullable
    private final LootContext.EntityTarget resolutionContext;
    
    public SetLoreFunction(final LootItemCondition[] arr, final boolean boolean2, final List<Component> list, @Nullable final LootContext.EntityTarget c) {
        super(arr);
        this.replace = boolean2;
        this.lore = (List<Component>)ImmutableList.copyOf((Collection)list);
        this.resolutionContext = c;
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_LORE;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>)((this.resolutionContext != null) ? ImmutableSet.of(this.resolutionContext.getParam()) : ImmutableSet.of());
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        final ListTag mj4 = this.getLoreTag(bly, !this.lore.isEmpty());
        if (mj4 != null) {
            if (this.replace) {
                mj4.clear();
            }
            final UnaryOperator<Component> unaryOperator5 = SetNameFunction.createResolver(cys, this.resolutionContext);
            this.lore.stream().map((Function)unaryOperator5).map(Component.Serializer::toJson).map(StringTag::valueOf).forEach(mj4::add);
        }
        return bly;
    }
    
    @Nullable
    private ListTag getLoreTag(final ItemStack bly, final boolean boolean2) {
        CompoundTag md4;
        if (bly.hasTag()) {
            md4 = bly.getTag();
        }
        else {
            if (!boolean2) {
                return null;
            }
            md4 = new CompoundTag();
            bly.setTag(md4);
        }
        CompoundTag md5;
        if (md4.contains("display", 10)) {
            md5 = md4.getCompound("display");
        }
        else {
            if (!boolean2) {
                return null;
            }
            md5 = new CompoundTag();
            md4.put("display", (Tag)md5);
        }
        if (md5.contains("Lore", 9)) {
            return md5.getList("Lore", 8);
        }
        if (boolean2) {
            final ListTag mj6 = new ListTag();
            md5.put("Lore", (Tag)mj6);
            return mj6;
        }
        return null;
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<SetLoreFunction> {
        @Override
        public void serialize(final JsonObject jsonObject, final SetLoreFunction dap, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, dap, jsonSerializationContext);
            jsonObject.addProperty("replace", Boolean.valueOf(dap.replace));
            final JsonArray jsonArray5 = new JsonArray();
            for (final Component nr7 : dap.lore) {
                jsonArray5.add(Component.Serializer.toJsonTree(nr7));
            }
            jsonObject.add("lore", (JsonElement)jsonArray5);
            if (dap.resolutionContext != null) {
                jsonObject.add("entity", jsonSerializationContext.serialize(dap.resolutionContext));
            }
        }
        
        @Override
        public SetLoreFunction deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final boolean boolean5 = GsonHelper.getAsBoolean(jsonObject, "replace", false);
            final List<Component> list6 = (List<Component>)Streams.stream((Iterable)GsonHelper.getAsJsonArray(jsonObject, "lore")).map(Component.Serializer::fromJson).collect(ImmutableList.toImmutableList());
            final LootContext.EntityTarget c7 = GsonHelper.<LootContext.EntityTarget>getAsObject(jsonObject, "entity", (LootContext.EntityTarget)null, jsonDeserializationContext, (java.lang.Class<? extends LootContext.EntityTarget>)LootContext.EntityTarget.class);
            return new SetLoreFunction(arr, boolean5, list6, c7);
        }
    }
}
