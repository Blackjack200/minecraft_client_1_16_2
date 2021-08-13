package net.minecraft.world.level.storage.loot.functions;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import org.apache.commons.lang3.ArrayUtils;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import java.util.function.Function;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public abstract class LootItemConditionalFunction implements LootItemFunction {
    protected final LootItemCondition[] predicates;
    private final Predicate<LootContext> compositePredicates;
    
    protected LootItemConditionalFunction(final LootItemCondition[] arr) {
        this.predicates = arr;
        this.compositePredicates = LootItemConditions.<LootContext>andConditions((java.util.function.Predicate<LootContext>[])arr);
    }
    
    public final ItemStack apply(final ItemStack bly, final LootContext cys) {
        return this.compositePredicates.test(cys) ? this.run(bly, cys) : bly;
    }
    
    protected abstract ItemStack run(final ItemStack bly, final LootContext cys);
    
    public void validate(final ValidationContext czd) {
        super.validate(czd);
        for (int integer3 = 0; integer3 < this.predicates.length; ++integer3) {
            this.predicates[integer3].validate(czd.forChild(new StringBuilder().append(".conditions[").append(integer3).append("]").toString()));
        }
    }
    
    protected static Builder<?> simpleBuilder(final Function<LootItemCondition[], LootItemFunction> function) {
        return new DummyBuilder(function);
    }
    
    public abstract static class Builder<T extends Builder<T>> implements LootItemFunction.Builder, ConditionUserBuilder<T> {
        private final List<LootItemCondition> conditions;
        
        public Builder() {
            this.conditions = (List<LootItemCondition>)Lists.newArrayList();
        }
        
        public T when(final LootItemCondition.Builder a) {
            this.conditions.add(a.build());
            return this.getThis();
        }
        
        public final T unwrap() {
            return this.getThis();
        }
        
        protected abstract T getThis();
        
        protected LootItemCondition[] getConditions() {
            return (LootItemCondition[])this.conditions.toArray((Object[])new LootItemCondition[0]);
        }
    }
    
    static final class DummyBuilder extends Builder<DummyBuilder> {
        private final Function<LootItemCondition[], LootItemFunction> constructor;
        
        public DummyBuilder(final Function<LootItemCondition[], LootItemFunction> function) {
            this.constructor = function;
        }
        
        @Override
        protected DummyBuilder getThis() {
            return this;
        }
        
        public LootItemFunction build() {
            return (LootItemFunction)this.constructor.apply(this.getConditions());
        }
    }
    
    public abstract static class Serializer<T extends LootItemConditionalFunction> implements net.minecraft.world.level.storage.loot.Serializer<T> {
        public void serialize(final JsonObject jsonObject, final T daf, final JsonSerializationContext jsonSerializationContext) {
            if (!ArrayUtils.isEmpty((Object[])daf.predicates)) {
                jsonObject.add("conditions", jsonSerializationContext.serialize(daf.predicates));
            }
        }
        
        public final T deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final LootItemCondition[] arr4 = GsonHelper.<LootItemCondition[]>getAsObject(jsonObject, "conditions", new LootItemCondition[0], jsonDeserializationContext, (java.lang.Class<? extends LootItemCondition[]>)LootItemCondition[].class);
            return this.deserialize(jsonObject, jsonDeserializationContext, arr4);
        }
        
        public abstract T deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr);
    }
}
