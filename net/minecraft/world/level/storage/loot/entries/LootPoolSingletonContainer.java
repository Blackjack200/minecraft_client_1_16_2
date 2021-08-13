package net.minecraft.world.level.storage.loot.entries;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import org.apache.commons.lang3.ArrayUtils;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import java.util.function.BiFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;

public abstract class LootPoolSingletonContainer extends LootPoolEntryContainer {
    protected final int weight;
    protected final int quality;
    protected final LootItemFunction[] functions;
    private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
    private final LootPoolEntry entry;
    
    protected LootPoolSingletonContainer(final int integer1, final int integer2, final LootItemCondition[] arr, final LootItemFunction[] arr) {
        super(arr);
        this.entry = new EntryBase() {
            public void createItemStack(final Consumer<ItemStack> consumer, final LootContext cys) {
                LootPoolSingletonContainer.this.createItemStack(LootItemFunction.decorate(LootPoolSingletonContainer.this.compositeFunction, consumer, cys), cys);
            }
        };
        this.weight = integer1;
        this.quality = integer2;
        this.functions = arr;
        this.compositeFunction = LootItemFunctions.compose(arr);
    }
    
    @Override
    public void validate(final ValidationContext czd) {
        super.validate(czd);
        for (int integer3 = 0; integer3 < this.functions.length; ++integer3) {
            this.functions[integer3].validate(czd.forChild(new StringBuilder().append(".functions[").append(integer3).append("]").toString()));
        }
    }
    
    protected abstract void createItemStack(final Consumer<ItemStack> consumer, final LootContext cys);
    
    public boolean expand(final LootContext cys, final Consumer<LootPoolEntry> consumer) {
        if (this.canRun(cys)) {
            consumer.accept(this.entry);
            return true;
        }
        return false;
    }
    
    public static Builder<?> simpleBuilder(final EntryConstructor d) {
        return new DummyBuilder(d);
    }
    
    public abstract class EntryBase implements LootPoolEntry {
        protected EntryBase() {
        }
        
        public int getWeight(final float float1) {
            return Math.max(Mth.floor(LootPoolSingletonContainer.this.weight + LootPoolSingletonContainer.this.quality * float1), 0);
        }
    }
    
    public abstract static class Builder<T extends Builder<T>> extends LootPoolEntryContainer.Builder<T> implements FunctionUserBuilder<T> {
        protected int weight;
        protected int quality;
        private final List<LootItemFunction> functions;
        
        public Builder() {
            this.weight = 1;
            this.quality = 0;
            this.functions = (List<LootItemFunction>)Lists.newArrayList();
        }
        
        @Override
        public T apply(final LootItemFunction.Builder a) {
            this.functions.add(a.build());
            return this.getThis();
        }
        
        protected LootItemFunction[] getFunctions() {
            return (LootItemFunction[])this.functions.toArray((Object[])new LootItemFunction[0]);
        }
        
        public T setWeight(final int integer) {
            this.weight = integer;
            return this.getThis();
        }
        
        public T setQuality(final int integer) {
            this.quality = integer;
            return this.getThis();
        }
    }
    
    static class DummyBuilder extends Builder<DummyBuilder> {
        private final EntryConstructor constructor;
        
        public DummyBuilder(final EntryConstructor d) {
            this.constructor = d;
        }
        
        @Override
        protected DummyBuilder getThis() {
            return this;
        }
        
        @Override
        public LootPoolEntryContainer build() {
            return this.constructor.build(this.weight, this.quality, this.getConditions(), this.getFunctions());
        }
    }
    
    public abstract static class Serializer<T extends LootPoolSingletonContainer> extends LootPoolEntryContainer.Serializer<T> {
        public void serialize(final JsonObject jsonObject, final T czp, final JsonSerializationContext jsonSerializationContext) {
            if (czp.weight != 1) {
                jsonObject.addProperty("weight", (Number)czp.weight);
            }
            if (czp.quality != 0) {
                jsonObject.addProperty("quality", (Number)czp.quality);
            }
            if (!ArrayUtils.isEmpty((Object[])czp.functions)) {
                jsonObject.add("functions", jsonSerializationContext.serialize(czp.functions));
            }
        }
        
        @Override
        public final T deserializeCustom(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
            final int integer5 = GsonHelper.getAsInt(jsonObject, "weight", 1);
            final int integer6 = GsonHelper.getAsInt(jsonObject, "quality", 0);
            final LootItemFunction[] arr2 = GsonHelper.<LootItemFunction[]>getAsObject(jsonObject, "functions", new LootItemFunction[0], jsonDeserializationContext, (java.lang.Class<? extends LootItemFunction[]>)LootItemFunction[].class);
            return this.deserialize(jsonObject, jsonDeserializationContext, integer5, integer6, arr, arr2);
        }
        
        protected abstract T deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final int integer3, final int integer4, final LootItemCondition[] arr, final LootItemFunction[] arr);
    }
    
    @FunctionalInterface
    public interface EntryConstructor {
        LootPoolSingletonContainer build(final int integer1, final int integer2, final LootItemCondition[] arr, final LootItemFunction[] arr);
    }
}
