package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import com.google.common.collect.Lists;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Dynamic;
import java.util.List;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class FurnaceRecipeFix extends DataFix {
    public FurnaceRecipeFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    protected TypeRewriteRule makeRule() {
        return this.cap((com.mojang.datafixers.types.Type<Object>)this.getOutputSchema().getTypeRaw(References.RECIPE));
    }
    
    private <R> TypeRewriteRule cap(final Type<R> type) {
        final Type<Pair<Either<Pair<List<Pair<R, Integer>>, Dynamic<?>>, Unit>, Dynamic<?>>> type2 = (Type<Pair<Either<Pair<List<Pair<R, Integer>>, Dynamic<?>>, Unit>, Dynamic<?>>>)DSL.and(DSL.optional((Type)DSL.field("RecipesUsed", DSL.and((Type)DSL.compoundList((Type)type, DSL.intType()), DSL.remainderType()))), DSL.remainderType());
        final OpticFinder<?> opticFinder4 = DSL.namedChoice("minecraft:furnace", this.getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:furnace"));
        final OpticFinder<?> opticFinder5 = DSL.namedChoice("minecraft:blast_furnace", this.getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:blast_furnace"));
        final OpticFinder<?> opticFinder6 = DSL.namedChoice("minecraft:smoker", this.getInputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:smoker"));
        final Type<?> type3 = this.getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:furnace");
        final Type<?> type4 = this.getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:blast_furnace");
        final Type<?> type5 = this.getOutputSchema().getChoiceType(References.BLOCK_ENTITY, "minecraft:smoker");
        final Type<?> type6 = this.getInputSchema().getType(References.BLOCK_ENTITY);
        final Type<?> type7 = this.getOutputSchema().getType(References.BLOCK_ENTITY);
        return this.fixTypeEverywhereTyped("FurnaceRecipesFix", (Type)type6, (Type)type7, typed -> typed.updateTyped(opticFinder4, type3, typed -> this.updateFurnaceContents((com.mojang.datafixers.types.Type<Object>)type, (com.mojang.datafixers.types.Type<com.mojang.datafixers.util.Pair<com.mojang.datafixers.util.Either<com.mojang.datafixers.util.Pair<java.util.List<com.mojang.datafixers.util.Pair<Object, Integer>>, Dynamic<?>>, Unit>, Dynamic<?>>>)type2, typed)).updateTyped(opticFinder5, type4, typed -> this.updateFurnaceContents((com.mojang.datafixers.types.Type<Object>)type, (com.mojang.datafixers.types.Type<com.mojang.datafixers.util.Pair<com.mojang.datafixers.util.Either<com.mojang.datafixers.util.Pair<java.util.List<com.mojang.datafixers.util.Pair<Object, Integer>>, Dynamic<?>>, Unit>, Dynamic<?>>>)type2, typed)).updateTyped(opticFinder6, type5, typed -> this.updateFurnaceContents((com.mojang.datafixers.types.Type<Object>)type, (com.mojang.datafixers.types.Type<com.mojang.datafixers.util.Pair<com.mojang.datafixers.util.Either<com.mojang.datafixers.util.Pair<java.util.List<com.mojang.datafixers.util.Pair<Object, Integer>>, Dynamic<?>>, Unit>, Dynamic<?>>>)type2, typed)));
    }
    
    private <R> Typed<?> updateFurnaceContents(final Type<R> type1, final Type<Pair<Either<Pair<List<Pair<R, Integer>>, Dynamic<?>>, Unit>, Dynamic<?>>> type2, final Typed<?> typed) {
        Dynamic<?> dynamic5 = typed.getOrCreate(DSL.remainderFinder());
        final int integer6 = dynamic5.get("RecipesUsedSize").asInt(0);
        dynamic5 = dynamic5.remove("RecipesUsedSize");
        final List<Pair<R, Integer>> list7 = (List<Pair<R, Integer>>)Lists.newArrayList();
        for (int integer7 = 0; integer7 < integer6; ++integer7) {
            final String string9 = new StringBuilder().append("RecipeLocation").append(integer7).toString();
            final String string10 = new StringBuilder().append("RecipeAmount").append(integer7).toString();
            final Optional<? extends Dynamic<?>> optional11 = dynamic5.get(string9).result();
            final int integer8 = dynamic5.get(string10).asInt(0);
            if (integer8 > 0) {
                optional11.ifPresent(dynamic -> {
                    final Optional<? extends Pair<Object, ? extends Dynamic<?>>> optional5 = type1.read(dynamic).result();
                    optional5.ifPresent(pair -> list7.add(Pair.of(pair.getFirst(), (Object)integer8)));
                });
            }
            dynamic5 = dynamic5.remove(string9).remove(string10);
        }
        return typed.set(DSL.remainderFinder(), (Type)type2, Pair.of((Object)Either.left((Object)Pair.of((Object)list7, (Object)dynamic5.emptyMap())), (Object)dynamic5));
    }
}
