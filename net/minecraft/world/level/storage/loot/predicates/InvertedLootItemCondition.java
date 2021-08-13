package net.minecraft.world.level.storage.loot.predicates;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import net.minecraft.world.level.storage.loot.LootContext;

public class InvertedLootItemCondition implements LootItemCondition {
    private final LootItemCondition term;
    
    private InvertedLootItemCondition(final LootItemCondition dbl) {
        this.term = dbl;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.INVERTED;
    }
    
    public final boolean test(final LootContext cys) {
        return !this.term.test(cys);
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return this.term.getReferencedContextParams();
    }
    
    public void validate(final ValidationContext czd) {
        super.validate(czd);
        this.term.validate(czd);
    }
    
    public static Builder invert(final Builder a) {
        final InvertedLootItemCondition dbi2 = new InvertedLootItemCondition(a.build());
        return () -> dbi2;
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<InvertedLootItemCondition> {
        public void serialize(final JsonObject jsonObject, final InvertedLootItemCondition dbi, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.add("term", jsonSerializationContext.serialize(dbi.term));
        }
        
        public InvertedLootItemCondition deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            final LootItemCondition dbl4 = GsonHelper.<LootItemCondition>getAsObject(jsonObject, "term", jsonDeserializationContext, (java.lang.Class<? extends LootItemCondition>)LootItemCondition.class);
            return new InvertedLootItemCondition(dbl4, null);
        }
    }
}
