package net.minecraft.util.datafix.fixes;

import java.util.Objects;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.OpticFinder;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.templates.List;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class VillagerTradeFix extends NamedEntityFix {
    public VillagerTradeFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "Villager trade fix", References.ENTITY, "minecraft:villager");
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        final OpticFinder<?> opticFinder3 = typed.getType().findField("Offers");
        final OpticFinder<?> opticFinder4 = opticFinder3.type().findField("Recipes");
        final Type<?> type5 = opticFinder4.type();
        if (!(type5 instanceof List.ListType)) {
            throw new IllegalStateException("Recipes are expected to be a list.");
        }
        final List.ListType<?> listType6 = type5;
        final Type<?> type6 = listType6.getElement();
        final OpticFinder<?> opticFinder5 = DSL.typeFinder((Type)type6);
        final OpticFinder<?> opticFinder6 = type6.findField("buy");
        final OpticFinder<?> opticFinder7 = type6.findField("buyB");
        final OpticFinder<?> opticFinder8 = type6.findField("sell");
        final OpticFinder<Pair<String, String>> opticFinder9 = (OpticFinder<Pair<String, String>>)DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), (Type)NamespacedSchema.namespacedString()));
        final Function<Typed<?>, Typed<?>> function13 = (Function<Typed<?>, Typed<?>>)(typed -> this.updateItemStack(opticFinder9, typed));
        return typed.updateTyped((OpticFinder)opticFinder3, typed -> typed.updateTyped(opticFinder4, typed -> typed.updateTyped(opticFinder5, typed -> typed.updateTyped(opticFinder6, function13).updateTyped(opticFinder7, function13).updateTyped(opticFinder8, function13))));
    }
    
    private Typed<?> updateItemStack(final OpticFinder<Pair<String, String>> opticFinder, final Typed<?> typed) {
        return typed.update((OpticFinder)opticFinder, pair -> pair.mapSecond(string -> Objects.equals(string, "minecraft:carved_pumpkin") ? "minecraft:pumpkin" : string));
    }
}
