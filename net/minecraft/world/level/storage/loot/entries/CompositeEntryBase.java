package net.minecraft.world.level.storage.loot.entries;

import net.minecraft.util.GsonHelper;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public abstract class CompositeEntryBase extends LootPoolEntryContainer {
    protected final LootPoolEntryContainer[] children;
    private final ComposableEntryContainer composedChildren;
    
    protected CompositeEntryBase(final LootPoolEntryContainer[] arr, final LootItemCondition[] arr) {
        super(arr);
        this.children = arr;
        this.composedChildren = this.compose(arr);
    }
    
    @Override
    public void validate(final ValidationContext czd) {
        super.validate(czd);
        if (this.children.length == 0) {
            czd.reportProblem("Empty children list");
        }
        for (int integer3 = 0; integer3 < this.children.length; ++integer3) {
            this.children[integer3].validate(czd.forChild(new StringBuilder().append(".entry[").append(integer3).append("]").toString()));
        }
    }
    
    protected abstract ComposableEntryContainer compose(final ComposableEntryContainer[] arr);
    
    public final boolean expand(final LootContext cys, final Consumer<LootPoolEntry> consumer) {
        return this.canRun(cys) && this.composedChildren.expand(cys, consumer);
    }
    
    public static <T extends CompositeEntryBase> Serializer<T> createSerializer(final CompositeEntryConstructor<T> a) {
        return new Serializer<T>() {
            public void serialize(final JsonObject jsonObject, final T czg, final JsonSerializationContext jsonSerializationContext) {
                jsonObject.add("children", jsonSerializationContext.serialize(czg.children));
            }
            
            @Override
            public final T deserializeCustom(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext, final LootItemCondition[] arr) {
                final LootPoolEntryContainer[] arr2 = GsonHelper.<LootPoolEntryContainer[]>getAsObject(jsonObject, "children", jsonDeserializationContext, (java.lang.Class<? extends LootPoolEntryContainer[]>)LootPoolEntryContainer[].class);
                return a.create(arr2, arr);
            }
        };
    }
    
    @FunctionalInterface
    public interface CompositeEntryConstructor<T extends CompositeEntryBase> {
        T create(final LootPoolEntryContainer[] arr, final LootItemCondition[] arr);
    }
}
