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

public class BedItemColorFix extends DataFix {
    public BedItemColorFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final OpticFinder<Pair<String, String>> opticFinder2 = (OpticFinder<Pair<String, String>>)DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), (Type)NamespacedSchema.namespacedString()));
        return this.fixTypeEverywhereTyped("BedItemColorFix", this.getInputSchema().getType(References.ITEM_STACK), typed -> {
            final Optional<Pair<String, String>> optional3 = (Optional<Pair<String, String>>)typed.getOptional(opticFinder2);
            if (optional3.isPresent() && Objects.equals(((Pair)optional3.get()).getSecond(), "minecraft:bed")) {
                final Dynamic<?> dynamic4 = typed.get(DSL.remainderFinder());
                if (dynamic4.get("Damage").asInt(0) == 0) {
                    return typed.set(DSL.remainderFinder(), dynamic4.set("Damage", dynamic4.createShort((short)14)));
                }
            }
            return typed;
        });
    }
}
