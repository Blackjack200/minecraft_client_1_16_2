package net.minecraft.world.level.storage.loot.entries;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import org.apache.commons.lang3.ArrayUtils;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public abstract class LootPoolEntryContainer implements ComposableEntryContainer {
    protected final LootItemCondition[] conditions;
    private final Predicate<LootContext> compositeCondition;
    
    protected LootPoolEntryContainer(final LootItemCondition[] arr) {
        this.conditions = arr;
        this.compositeCondition = LootItemConditions.<LootContext>andConditions((java.util.function.Predicate<LootContext>[])arr);
    }
    
    public void validate(final ValidationContext czd) {
        for (int integer3 = 0; integer3 < this.conditions.length; ++integer3) {
            this.conditions[integer3].validate(czd.forChild(new StringBuilder().append(".condition[").append(integer3).append("]").toString()));
        }
    }
    
    protected final boolean canRun(final LootContext cys) {
        return this.compositeCondition.test(cys);
    }
    
    public abstract LootPoolEntryType getType();
    
    public abstract static class Builder<T extends Builder<T>> implements ConditionUserBuilder<T> {
        private final List<LootItemCondition> conditions;
        
        public Builder() {
            this.conditions = (List<LootItemCondition>)Lists.newArrayList();
        }
        
        protected abstract T getThis();
        
        public T when(final LootItemCondition.Builder a) {
            this.conditions.add(a.build());
            return this.getThis();
        }
        
        public final T unwrap() {
            return this.getThis();
        }
        
        protected LootItemCondition[] getConditions() {
            return (LootItemCondition[])this.conditions.toArray((Object[])new LootItemCondition[0]);
        }
        
        public AlternativesEntry.Builder otherwise(final Builder<?> a) {
            return new AlternativesEntry.Builder(new Builder[] { this, a });
        }
        
        public abstract LootPoolEntryContainer build();
    }
    
    public abstract static class Builder<T extends Builder<T>> implements ConditionUserBuilder<T> {
        private final List<LootItemCondition> conditions;
        
        public Builder() {
            this.conditions = (List<LootItemCondition>)Lists.newArrayList();
        }
        
        protected abstract T getThis();
        
        public T when(final LootItemCondition.Builder a) {
            this.conditions.add(a.build());
            return this.getThis();
        }
        
        public final T unwrap() {
            return this.getThis();
        }
        
        protected LootItemCondition[] getConditions() {
            return (LootItemCondition[])this.conditions.toArray((Object[])new LootItemCondition[0]);
        }
        
        public AlternativesEntry.Builder otherwise(final Builder<?> a) {
            return new AlternativesEntry.Builder(new Builder[] { this, a });
        }
        
        public abstract LootPoolEntryContainer build();
    }
    
    public abstract static class Serializer<T extends LootPoolEntryContainer> implements net.minecraft.world.level.storage.loot.Serializer<T> {
        public final void serialize(final JsonObject jsonObject, final T czn, final JsonSerializationContext jsonSerializationContext) {
            if (!ArrayUtils.isEmpty((Object[])czn.conditions)) {
                jsonObject.add("conditions", jsonSerializationContext.serialize(czn.conditions));
            }
            this.serialize(jsonObject, czn, jsonSerializationContext);
        }
        
        public final T deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final LootItemCondition[] arr4 = GsonHelper.<LootItemCondition[]>getAsObject(jsonObject, "conditions", new LootItemCondition[0], jsonDeserializationContext, (java.lang.Class<? extends LootItemCondition[]>)LootItemCondition[].class);
            return this.deserializeCustom(jsonObject, jsonDeserializationContext, arr4);
        }
        
        public abstract void serialize(final JsonObject jsonObject, final T czn, final JsonSerializationContext jsonSerializationContext);
        
        public abstract T deserializeCustom(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr);
    }
}
