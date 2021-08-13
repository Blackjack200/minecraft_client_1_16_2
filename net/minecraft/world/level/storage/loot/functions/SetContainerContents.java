package net.minecraft.world.level.storage.loot.functions;

import java.util.Arrays;
import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Lists;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.nbt.Tag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import java.util.List;

public class SetContainerContents extends LootItemConditionalFunction {
    private final List<LootPoolEntryContainer> entries;
    
    private SetContainerContents(final LootItemCondition[] arr, final List<LootPoolEntryContainer> list) {
        super(arr);
        this.entries = (List<LootPoolEntryContainer>)ImmutableList.copyOf((Collection)list);
    }
    
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_CONTENTS;
    }
    
    public ItemStack run(final ItemStack bly, final LootContext cys) {
        if (bly.isEmpty()) {
            return bly;
        }
        final NonNullList<ItemStack> gj4 = NonNullList.<ItemStack>create();
        this.entries.forEach(czn -> czn.expand(cys, (Consumer<LootPoolEntry>)(czm -> czm.createItemStack(LootTable.createStackSplitter((Consumer<ItemStack>)gj4::add), cys))));
        final CompoundTag md5 = new CompoundTag();
        ContainerHelper.saveAllItems(md5, gj4);
        final CompoundTag md6 = bly.getOrCreateTag();
        md6.put("BlockEntityTag", (Tag)md5.merge(md6.getCompound("BlockEntityTag")));
        return bly;
    }
    
    @Override
    public void validate(final ValidationContext czd) {
        super.validate(czd);
        for (int integer3 = 0; integer3 < this.entries.size(); ++integer3) {
            ((LootPoolEntryContainer)this.entries.get(integer3)).validate(czd.forChild(new StringBuilder().append(".entry[").append(integer3).append("]").toString()));
        }
    }
    
    public static Builder setContents() {
        return new Builder();
    }
    
    public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
        private final List<LootPoolEntryContainer> entries;
        
        public Builder() {
            this.entries = (List<LootPoolEntryContainer>)Lists.newArrayList();
        }
        
        @Override
        protected Builder getThis() {
            return this;
        }
        
        public Builder withEntry(final LootPoolEntryContainer.Builder<?> a) {
            this.entries.add(a.build());
            return this;
        }
        
        public LootItemFunction build() {
            return new SetContainerContents(this.getConditions(), this.entries, null);
        }
    }
    
    public static class Serializer extends LootItemConditionalFunction.Serializer<SetContainerContents> {
        @Override
        public void serialize(final JsonObject jsonObject, final SetContainerContents dal, final JsonSerializationContext jsonSerializationContext) {
            super.serialize(jsonObject, dal, jsonSerializationContext);
            jsonObject.add("entries", jsonSerializationContext.serialize(dal.entries));
        }
        
        @Override
        public SetContainerContents deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final LootPoolEntryContainer[] arr2 = GsonHelper.<LootPoolEntryContainer[]>getAsObject(jsonObject, "entries", jsonDeserializationContext, (java.lang.Class<? extends LootPoolEntryContainer[]>)LootPoolEntryContainer[].class);
            return new SetContainerContents(arr, Arrays.asList((Object[])arr2), null);
        }
    }
}
