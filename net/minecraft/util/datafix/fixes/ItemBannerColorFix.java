package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import java.util.stream.Stream;
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

public class ItemBannerColorFix extends DataFix {
    public ItemBannerColorFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.ITEM_STACK);
        final OpticFinder<Pair<String, String>> opticFinder3 = (OpticFinder<Pair<String, String>>)DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), (Type)NamespacedSchema.namespacedString()));
        final OpticFinder<?> opticFinder4 = type2.findField("tag");
        final OpticFinder<?> opticFinder5 = opticFinder4.type().findField("BlockEntityTag");
        return this.fixTypeEverywhereTyped("ItemBannerColorFix", (Type)type2, typed -> {
            final Optional<Pair<String, String>> optional5 = (Optional<Pair<String, String>>)typed.getOptional(opticFinder3);
            if (optional5.isPresent() && Objects.equals(((Pair)optional5.get()).getSecond(), "minecraft:banner")) {
                Dynamic<?> dynamic6 = typed.get(DSL.remainderFinder());
                final Optional<? extends Typed<?>> optional6 = typed.getOptionalTyped(opticFinder4);
                if (optional6.isPresent()) {
                    final Typed<?> typed2 = optional6.get();
                    final Optional<? extends Typed<?>> optional7 = typed2.getOptionalTyped(opticFinder5);
                    if (optional7.isPresent()) {
                        final Typed<?> typed3 = optional7.get();
                        final Dynamic<?> dynamic7 = typed2.get(DSL.remainderFinder());
                        final Dynamic<?> dynamic8 = typed3.getOrCreate(DSL.remainderFinder());
                        if (dynamic8.get("Base").asNumber().result().isPresent()) {
                            dynamic6 = dynamic6.set("Damage", dynamic6.createShort((short)(dynamic8.get("Base").asInt(0) & 0xF)));
                            final Optional<? extends Dynamic<?>> optional8 = dynamic7.get("display").result();
                            if (optional8.isPresent()) {
                                final Dynamic<?> dynamic9 = optional8.get();
                                final Dynamic<?> dynamic10 = dynamic9.createMap((Map)ImmutableMap.of(dynamic9.createString("Lore"), dynamic9.createList(Stream.of((Object)dynamic9.createString("(+NBT")))));
                                if (Objects.equals(dynamic9, dynamic10)) {
                                    return typed.set(DSL.remainderFinder(), dynamic6);
                                }
                            }
                            dynamic8.remove("Base");
                            return typed.set(DSL.remainderFinder(), dynamic6).set(opticFinder4, typed2.set(opticFinder5, typed3.set(DSL.remainderFinder(), dynamic8)));
                        }
                    }
                }
                return typed.set(DSL.remainderFinder(), dynamic6);
            }
            return typed;
        });
    }
}
