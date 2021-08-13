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

public class ItemShulkerBoxColorFix extends DataFix {
    public static final String[] NAMES_BY_COLOR;
    
    public ItemShulkerBoxColorFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.ITEM_STACK);
        final OpticFinder<Pair<String, String>> opticFinder3 = (OpticFinder<Pair<String, String>>)DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), (Type)NamespacedSchema.namespacedString()));
        final OpticFinder<?> opticFinder4 = type2.findField("tag");
        final OpticFinder<?> opticFinder5 = opticFinder4.type().findField("BlockEntityTag");
        return this.fixTypeEverywhereTyped("ItemShulkerBoxColorFix", (Type)type2, typed -> {
            final Optional<Pair<String, String>> optional5 = (Optional<Pair<String, String>>)typed.getOptional(opticFinder3);
            if (optional5.isPresent() && Objects.equals(((Pair)optional5.get()).getSecond(), "minecraft:shulker_box")) {
                final Optional<? extends Typed<?>> optional6 = typed.getOptionalTyped(opticFinder4);
                if (optional6.isPresent()) {
                    final Typed<?> typed2 = optional6.get();
                    final Optional<? extends Typed<?>> optional7 = typed2.getOptionalTyped(opticFinder5);
                    if (optional7.isPresent()) {
                        final Typed<?> typed3 = optional7.get();
                        final Dynamic<?> dynamic10 = typed3.get(DSL.remainderFinder());
                        final int integer11 = dynamic10.get("Color").asInt(0);
                        dynamic10.remove("Color");
                        return typed.set(opticFinder4, typed2.set(opticFinder5, typed3.set(DSL.remainderFinder(), dynamic10))).set(opticFinder3, Pair.of((Object)References.ITEM_NAME.typeName(), (Object)ItemShulkerBoxColorFix.NAMES_BY_COLOR[integer11 % 16]));
                    }
                }
            }
            return typed;
        });
    }
    
    static {
        NAMES_BY_COLOR = new String[] { "minecraft:white_shulker_box", "minecraft:orange_shulker_box", "minecraft:magenta_shulker_box", "minecraft:light_blue_shulker_box", "minecraft:yellow_shulker_box", "minecraft:lime_shulker_box", "minecraft:pink_shulker_box", "minecraft:gray_shulker_box", "minecraft:silver_shulker_box", "minecraft:cyan_shulker_box", "minecraft:purple_shulker_box", "minecraft:blue_shulker_box", "minecraft:brown_shulker_box", "minecraft:green_shulker_box", "minecraft:red_shulker_box", "minecraft:black_shulker_box" };
    }
}
