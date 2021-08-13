package net.minecraft.util.datafix.fixes;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import com.google.common.collect.Lists;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.OpticFinder;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Unit;
import java.util.List;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class EntityEquipmentToArmorAndHandFix extends DataFix {
    public EntityEquipmentToArmorAndHandFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        return this.cap((com.mojang.datafixers.types.Type<Object>)this.getInputSchema().getTypeRaw(References.ITEM_STACK));
    }
    
    private <IS> TypeRewriteRule cap(final Type<IS> type) {
        final Type<Pair<Either<List<IS>, Unit>, Dynamic<?>>> type2 = (Type<Pair<Either<List<IS>, Unit>, Dynamic<?>>>)DSL.and(DSL.optional((Type)DSL.field("Equipment", (Type)DSL.list((Type)type))), DSL.remainderType());
        final Type<Pair<Either<List<IS>, Unit>, Pair<Either<List<IS>, Unit>, Dynamic<?>>>> type3 = (Type<Pair<Either<List<IS>, Unit>, Pair<Either<List<IS>, Unit>, Dynamic<?>>>>)DSL.and(DSL.optional((Type)DSL.field("ArmorItems", (Type)DSL.list((Type)type))), DSL.optional((Type)DSL.field("HandItems", (Type)DSL.list((Type)type))), DSL.remainderType());
        final OpticFinder<Pair<Either<List<IS>, Unit>, Dynamic<?>>> opticFinder5 = (OpticFinder<Pair<Either<List<IS>, Unit>, Dynamic<?>>>)DSL.typeFinder((Type)type2);
        final OpticFinder<List<IS>> opticFinder6 = (OpticFinder<List<IS>>)DSL.fieldFinder("Equipment", (Type)DSL.list((Type)type));
        return this.fixTypeEverywhereTyped("EntityEquipmentToArmorAndHandFix", this.getInputSchema().getType(References.ENTITY), this.getOutputSchema().getType(References.ENTITY), typed -> {
            Either<List<Object>, Unit> either6 = (Either<List<Object>, Unit>)Either.right(DSL.unit());
            Either<List<Object>, Unit> either7 = (Either<List<Object>, Unit>)Either.right(DSL.unit());
            Dynamic<?> dynamic8 = typed.getOrCreate(DSL.remainderFinder());
            final Optional<List<Object>> optional9 = (Optional<List<Object>>)typed.getOptional(opticFinder6);
            if (optional9.isPresent()) {
                final List<Object> list10 = (List<Object>)optional9.get();
                final Object object11 = ((Pair)type.read(dynamic8.emptyMap()).result().orElseThrow(() -> new IllegalStateException("Could not parse newly created empty itemstack."))).getFirst();
                if (!list10.isEmpty()) {
                    either6 = (Either<List<Object>, Unit>)Either.left(Lists.newArrayList(new Object[] { list10.get(0), object11 }));
                }
                if (list10.size() > 1) {
                    final List<Object> list11 = (List<Object>)Lists.newArrayList(new Object[] { object11, object11, object11, object11 });
                    for (int integer13 = 1; integer13 < Math.min(list10.size(), 5); ++integer13) {
                        list11.set(integer13 - 1, list10.get(integer13));
                    }
                    either7 = (Either<List<Object>, Unit>)Either.left(list11);
                }
            }
            final Dynamic<?> dynamic9 = dynamic8;
            final Optional<? extends Stream<? extends Dynamic<?>>> optional10 = dynamic8.get("DropChances").asStreamOpt().result();
            if (optional10.isPresent()) {
                final Iterator<? extends Dynamic<?>> iterator12 = Stream.concat((Stream)optional10.get(), Stream.generate(() -> dynamic9.createInt(0))).iterator();
                final float float13 = ((Dynamic)iterator12.next()).asFloat(0.0f);
                if (!dynamic8.get("HandDropChances").result().isPresent()) {
                    final Dynamic<?> dynamic10 = dynamic8.createList(Stream.of((Object[])new Float[] { float13, 0.0f }).map(dynamic8::createFloat));
                    dynamic8 = dynamic8.set("HandDropChances", (Dynamic)dynamic10);
                }
                if (!dynamic8.get("ArmorDropChances").result().isPresent()) {
                    final Dynamic<?> dynamic10 = dynamic8.createList(Stream.of((Object[])new Float[] { ((Dynamic)iterator12.next()).asFloat(0.0f), ((Dynamic)iterator12.next()).asFloat(0.0f), ((Dynamic)iterator12.next()).asFloat(0.0f), ((Dynamic)iterator12.next()).asFloat(0.0f) }).map(dynamic8::createFloat));
                    dynamic8 = dynamic8.set("ArmorDropChances", (Dynamic)dynamic10);
                }
                dynamic8 = dynamic8.remove("DropChances");
            }
            return typed.set(opticFinder5, type3, Pair.of((Object)either6, (Object)Pair.of((Object)either7, (Object)dynamic8)));
        });
    }
}
