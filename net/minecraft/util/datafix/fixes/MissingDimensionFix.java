package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.Typed;
import java.util.List;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.templates.CompoundList;
import com.mojang.datafixers.FieldFinder;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.types.templates.TaggedChoice;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.util.Unit;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.DSL;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class MissingDimensionFix extends DataFix {
    public MissingDimensionFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    private static <A> Type<Pair<A, Dynamic<?>>> fields(final String string, final Type<A> type) {
        return (Type<Pair<A, Dynamic<?>>>)DSL.and((Type)DSL.field(string, (Type)type), DSL.remainderType());
    }
    
    private static <A> Type<Pair<Either<A, Unit>, Dynamic<?>>> optionalFields(final String string, final Type<A> type) {
        return (Type<Pair<Either<A, Unit>, Dynamic<?>>>)DSL.and(DSL.optional((Type)DSL.field(string, (Type)type)), DSL.remainderType());
    }
    
    private static <A1, A2> Type<Pair<Either<A1, Unit>, Pair<Either<A2, Unit>, Dynamic<?>>>> optionalFields(final String string1, final Type<A1> type2, final String string3, final Type<A2> type4) {
        return (Type<Pair<Either<A1, Unit>, Pair<Either<A2, Unit>, Dynamic<?>>>>)DSL.and(DSL.optional((Type)DSL.field(string1, (Type)type2)), DSL.optional((Type)DSL.field(string3, (Type)type4)), DSL.remainderType());
    }
    
    protected TypeRewriteRule makeRule() {
        final Schema schema2 = this.getInputSchema();
        final TaggedChoice.TaggedChoiceType<String> taggedChoiceType3 = (TaggedChoice.TaggedChoiceType<String>)new TaggedChoice.TaggedChoiceType("type", DSL.string(), (Map)ImmutableMap.of("minecraft:debug", DSL.remainderType(), "minecraft:flat", MissingDimensionFix.<com.mojang.datafixers.util.Pair<com.mojang.datafixers.util.Either<Object, Unit>, com.mojang.datafixers.util.Pair<com.mojang.datafixers.util.Either<Object, Unit>, Dynamic<?>>>>optionalFields("settings", MissingDimensionFix.optionalFields("biome", (com.mojang.datafixers.types.Type<Object>)schema2.getType(References.BIOME), "layers", (com.mojang.datafixers.types.Type<Object>)DSL.list((Type)MissingDimensionFix.optionalFields("block", (com.mojang.datafixers.types.Type<Object>)schema2.getType(References.BLOCK_NAME))))), "minecraft:noise", MissingDimensionFix.optionalFields("biome_source", (com.mojang.datafixers.types.Type<Object>)DSL.taggedChoiceType("type", DSL.string(), (Map)ImmutableMap.of("minecraft:fixed", (Object)MissingDimensionFix.fields("biome", (com.mojang.datafixers.types.Type<Object>)schema2.getType(References.BIOME)), "minecraft:multi_noise", (Object)DSL.list((Type)MissingDimensionFix.fields("biome", (com.mojang.datafixers.types.Type<Object>)schema2.getType(References.BIOME))), "minecraft:checkerboard", (Object)MissingDimensionFix.fields("biomes", (com.mojang.datafixers.types.Type<Object>)DSL.list(schema2.getType(References.BIOME))), "minecraft:vanilla_layered", (Object)DSL.remainderType(), "minecraft:the_end", (Object)DSL.remainderType())), "settings", (com.mojang.datafixers.types.Type<Object>)DSL.or(DSL.string(), (Type)MissingDimensionFix.optionalFields("default_block", (com.mojang.datafixers.types.Type<Object>)schema2.getType(References.BLOCK_NAME), "default_fluid", (com.mojang.datafixers.types.Type<Object>)schema2.getType(References.BLOCK_NAME))))));
        final CompoundList.CompoundListType<String, ?> compoundListType4 = DSL.compoundList((Type)NamespacedSchema.namespacedString(), (Type)MissingDimensionFix.fields("generator", (com.mojang.datafixers.types.Type<Object>)taggedChoiceType3));
        final Type<?> type5 = DSL.and((Type)compoundListType4, DSL.remainderType());
        final Type<?> type6 = schema2.getType(References.WORLD_GEN_SETTINGS);
        final FieldFinder<?> fieldFinder7 = new FieldFinder("dimensions", (Type)type5);
        if (!type6.findFieldType("dimensions").equals(type5)) {
            throw new IllegalStateException();
        }
        final OpticFinder<? extends List<? extends Pair<String, ?>>> opticFinder8 = compoundListType4.finder();
        return this.fixTypeEverywhereTyped("MissingDimensionFix", (Type)type6, typed -> typed.updateTyped((OpticFinder)fieldFinder7, typed4 -> typed4.updateTyped(opticFinder8, typed3 -> {
            if (!(typed3.getValue() instanceof List)) {
                throw new IllegalStateException("List exptected");
            }
            if (((List)typed3.getValue()).isEmpty()) {
                final Dynamic<?> dynamic5 = typed.get(DSL.remainderFinder());
                final Dynamic<?> dynamic6 = this.recreateSettings(dynamic5);
                return (Typed)DataFixUtils.orElse(compoundListType4.readTyped((Dynamic)dynamic6).result().map(Pair::getFirst), typed3);
            }
            return typed3;
        })));
    }
    
    private <T> Dynamic<T> recreateSettings(final Dynamic<T> dynamic) {
        final long long3 = dynamic.get("seed").asLong(0L);
        return (Dynamic<T>)new Dynamic(dynamic.getOps(), WorldGenSettingsFix.<T>vanillaLevels(dynamic, long3, (com.mojang.serialization.Dynamic<T>)WorldGenSettingsFix.<T>defaultOverworld((com.mojang.serialization.Dynamic<T>)dynamic, long3), false));
    }
}
