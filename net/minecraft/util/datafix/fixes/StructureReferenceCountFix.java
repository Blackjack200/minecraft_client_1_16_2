package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class StructureReferenceCountFix extends DataFix {
    public StructureReferenceCountFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.STRUCTURE_FEATURE);
        return this.fixTypeEverywhereTyped("Structure Reference Fix", (Type)type2, typed -> typed.update(DSL.remainderFinder(), StructureReferenceCountFix::setCountToAtLeastOne));
    }
    
    private static <T> Dynamic<T> setCountToAtLeastOne(final Dynamic<T> dynamic) {
        return (Dynamic<T>)dynamic.update("references", dynamic -> dynamic.createInt((int)dynamic.asNumber().map(Number::intValue).result().filter(integer -> integer > 0).orElse(1)));
    }
}
