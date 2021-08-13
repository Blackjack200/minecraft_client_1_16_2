package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.function.Predicate;

public class AlternativeLootItemCondition implements LootItemCondition {
    private final LootItemCondition[] terms;
    private final Predicate<LootContext> composedPredicate;
    
    private AlternativeLootItemCondition(final LootItemCondition[] arr) {
        this.terms = arr;
        this.composedPredicate = LootItemConditions.<LootContext>orConditions((java.util.function.Predicate<LootContext>[])arr);
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.ALTERNATIVE;
    }
    
    public final boolean test(final LootContext cys) {
        return this.composedPredicate.test(cys);
    }
    
    public void validate(final ValidationContext czd) {
        super.validate(czd);
        for (int integer3 = 0; integer3 < this.terms.length; ++integer3) {
            this.terms[integer3].validate(czd.forChild(new StringBuilder().append(".term[").append(integer3).append("]").toString()));
        }
    }
    
    public static Builder alternative(final LootItemCondition.Builder... arr) {
        return new Builder(arr);
    }
    
    public static class Builder implements LootItemCondition.Builder {
        private final List<LootItemCondition> terms;
        
        public Builder(final LootItemCondition.Builder... arr) {
            this.terms = (List<LootItemCondition>)Lists.newArrayList();
            for (final LootItemCondition.Builder a6 : arr) {
                this.terms.add(a6.build());
            }
        }
        
        public Builder or(final LootItemCondition.Builder a) {
            this.terms.add(a.build());
            return this;
        }
        
        public LootItemCondition build() {
            return new AlternativeLootItemCondition((LootItemCondition[])this.terms.toArray((Object[])new LootItemCondition[0]), null);
        }
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<AlternativeLootItemCondition> {
        public void serialize(final JsonObject jsonObject, final AlternativeLootItemCondition dbb, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.add("terms", jsonSerializationContext.serialize(dbb.terms));
        }
        
        public AlternativeLootItemCondition deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final LootItemCondition[] arr4 = GsonHelper.<LootItemCondition[]>getAsObject(jsonObject, "terms", jsonDeserializationContext, (java.lang.Class<? extends LootItemCondition[]>)LootItemCondition[].class);
            return new AlternativeLootItemCondition(arr4, null);
        }
    }
}
