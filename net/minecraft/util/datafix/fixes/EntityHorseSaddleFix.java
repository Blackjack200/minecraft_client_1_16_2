package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class EntityHorseSaddleFix extends NamedEntityFix {
    public EntityHorseSaddleFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "EntityHorseSaddleFix", References.ENTITY, "EntityHorse");
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        final OpticFinder<Pair<String, String>> opticFinder3 = (OpticFinder<Pair<String, String>>)DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), (Type)NamespacedSchema.namespacedString()));
        final Type<?> type4 = this.getInputSchema().getTypeRaw(References.ITEM_STACK);
        final OpticFinder<?> opticFinder4 = DSL.fieldFinder("SaddleItem", (Type)type4);
        final Optional<? extends Typed<?>> optional6 = typed.getOptionalTyped((OpticFinder)opticFinder4);
        final Dynamic<?> dynamic7 = typed.get(DSL.remainderFinder());
        if (!optional6.isPresent() && dynamic7.get("Saddle").asBoolean(false)) {
            Typed<?> typed2 = type4.pointTyped(typed.getOps()).orElseThrow(IllegalStateException::new);
            typed2 = typed2.set((OpticFinder)opticFinder3, Pair.of((Object)References.ITEM_NAME.typeName(), "minecraft:saddle"));
            Dynamic<?> dynamic8 = dynamic7.emptyMap();
            dynamic8 = dynamic8.set("Count", dynamic8.createByte((byte)1));
            dynamic8 = dynamic8.set("Damage", dynamic8.createShort((short)0));
            typed2 = typed2.set(DSL.remainderFinder(), dynamic8);
            dynamic7.remove("Saddle");
            return typed.set((OpticFinder)opticFinder4, (Typed)typed2).set(DSL.remainderFinder(), dynamic7);
        }
        return typed;
    }
}
