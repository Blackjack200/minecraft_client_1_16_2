package net.minecraft.util.datafix.fixes;

import java.util.stream.Collectors;
import java.util.Optional;
import com.mojang.datafixers.DataFixUtils;
import java.util.Objects;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.templates.CompoundList;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class NewVillageFix extends DataFix {
    public NewVillageFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    protected TypeRewriteRule makeRule() {
        final CompoundList.CompoundListType<String, ?> compoundListType2 = DSL.compoundList(DSL.string(), this.getInputSchema().getType(References.STRUCTURE_FEATURE));
        final OpticFinder<? extends List<? extends Pair<String, ?>>> opticFinder3 = compoundListType2.finder();
        return this.cap(compoundListType2);
    }
    
    private <SF> TypeRewriteRule cap(final CompoundList.CompoundListType<String, SF> compoundListType) {
        final Type<?> type3 = this.getInputSchema().getType(References.CHUNK);
        final Type<?> type4 = this.getInputSchema().getType(References.STRUCTURE_FEATURE);
        final OpticFinder<?> opticFinder5 = type3.findField("Level");
        final OpticFinder<?> opticFinder6 = opticFinder5.type().findField("Structures");
        final OpticFinder<?> opticFinder7 = opticFinder6.type().findField("Starts");
        final OpticFinder<List<Pair<String, SF>>> opticFinder8 = (OpticFinder<List<Pair<String, SF>>>)compoundListType.finder();
        return TypeRewriteRule.seq(this.fixTypeEverywhereTyped("NewVillageFix", (Type)type3, typed -> typed.updateTyped(opticFinder5, typed -> typed.updateTyped(opticFinder6, typed -> typed.updateTyped(opticFinder7, typed -> typed.update(opticFinder8, list -> (List)list.stream().filter(pair -> !Objects.equals(pair.getFirst(), "Village")).map(pair -> pair.mapFirst(string -> string.equals("New_Village") ? "Village" : string)).collect(Collectors.toList()))).update(DSL.remainderFinder(), dynamic -> dynamic.update("References", dynamic -> {
            final Optional<? extends Dynamic<?>> optional2 = dynamic.get("New_Village").result();
            return ((Dynamic)DataFixUtils.orElse(optional2.map(dynamic2 -> dynamic.remove("New_Village").set("Village", dynamic2)), dynamic)).remove("Village");
        }))))), this.fixTypeEverywhereTyped("NewVillageStartFix", (Type)type4, typed -> typed.update(DSL.remainderFinder(), dynamic -> dynamic.update("id", dynamic -> Objects.equals(NamespacedSchema.ensureNamespaced(dynamic.asString("")), "minecraft:new_village") ? dynamic.createString("minecraft:village") : dynamic))));
    }
}
