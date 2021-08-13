package net.minecraft.util.datafix.fixes;

import java.util.Objects;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.TypeRewriteRule;
import java.util.Optional;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class OminousBannerRenameFix extends DataFix {
    public OminousBannerRenameFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    private Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        final Optional<? extends Dynamic<?>> optional3 = dynamic.get("display").result();
        if (optional3.isPresent()) {
            Dynamic<?> dynamic2 = optional3.get();
            final Optional<String> optional4 = (Optional<String>)dynamic2.get("Name").asString().result();
            if (optional4.isPresent()) {
                String string6 = (String)optional4.get();
                string6 = string6.replace("\"translate\":\"block.minecraft.illager_banner\"", "\"translate\":\"block.minecraft.ominous_banner\"");
                dynamic2 = dynamic2.set("Name", dynamic2.createString(string6));
            }
            return dynamic.set("display", (Dynamic)dynamic2);
        }
        return dynamic;
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.ITEM_STACK);
        final OpticFinder<Pair<String, String>> opticFinder3 = (OpticFinder<Pair<String, String>>)DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), (Type)NamespacedSchema.namespacedString()));
        final OpticFinder<?> opticFinder4 = type2.findField("tag");
        return this.fixTypeEverywhereTyped("OminousBannerRenameFix", (Type)type2, typed -> {
            final Optional<Pair<String, String>> optional5 = (Optional<Pair<String, String>>)typed.getOptional(opticFinder3);
            if (optional5.isPresent() && Objects.equals(((Pair)optional5.get()).getSecond(), "minecraft:white_banner")) {
                final Optional<? extends Typed<?>> optional6 = typed.getOptionalTyped(opticFinder4);
                if (optional6.isPresent()) {
                    final Typed<?> typed2 = optional6.get();
                    final Dynamic<?> dynamic8 = typed2.get(DSL.remainderFinder());
                    return typed.set(opticFinder4, typed2.set(DSL.remainderFinder(), this.fixTag(dynamic8)));
                }
            }
            return typed;
        });
    }
}
