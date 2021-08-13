package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import com.mojang.serialization.Dynamic;
import java.util.Objects;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class ItemStackMapIdFix extends DataFix {
    public ItemStackMapIdFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.ITEM_STACK);
        final OpticFinder<Pair<String, String>> opticFinder3 = (OpticFinder<Pair<String, String>>)DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), (Type)NamespacedSchema.namespacedString()));
        final OpticFinder<?> opticFinder4 = type2.findField("tag");
        return this.fixTypeEverywhereTyped("ItemInstanceMapIdFix", (Type)type2, typed -> {
            final Optional<Pair<String, String>> optional4 = (Optional<Pair<String, String>>)typed.getOptional(opticFinder3);
            if (optional4.isPresent() && Objects.equals(((Pair)optional4.get()).getSecond(), "minecraft:filled_map")) {
                final Dynamic<?> dynamic5 = typed.get(DSL.remainderFinder());
                final Typed<?> typed2 = typed.getOrCreateTyped(opticFinder4);
                Dynamic<?> dynamic6 = typed2.get(DSL.remainderFinder());
                dynamic6 = dynamic6.set("map", dynamic6.createInt(dynamic5.get("Damage").asInt(0)));
                return typed.set(opticFinder4, typed2.set(DSL.remainderFinder(), dynamic6));
            }
            return typed;
        });
    }
}
