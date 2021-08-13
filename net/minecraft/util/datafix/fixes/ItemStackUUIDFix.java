package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.Typed;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;

public class ItemStackUUIDFix extends AbstractUUIDFix {
    public ItemStackUUIDFix(final Schema schema) {
        super(schema, References.ITEM_STACK);
    }
    
    public TypeRewriteRule makeRule() {
        final OpticFinder<Pair<String, String>> opticFinder2 = (OpticFinder<Pair<String, String>>)DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), (Type)NamespacedSchema.namespacedString()));
        return this.fixTypeEverywhereTyped("ItemStackUUIDFix", this.getInputSchema().getType(this.typeReference), typed -> {
            final OpticFinder<?> opticFinder2 = typed.getType().findField("tag");
            return typed.updateTyped((OpticFinder)opticFinder2, typed3 -> typed3.update(DSL.remainderFinder(), dynamic -> {
                dynamic = this.updateAttributeModifiers(dynamic);
                if (typed.getOptional(opticFinder2).map(pair -> "minecraft:player_head".equals(pair.getSecond())).orElse(false)) {
                    dynamic = this.updateSkullOwner(dynamic);
                }
                return dynamic;
            }));
        });
    }
    
    private Dynamic<?> updateAttributeModifiers(final Dynamic<?> dynamic) {
        return dynamic.update("AttributeModifiers", dynamic2 -> dynamic.createList(dynamic2.asStream().map(dynamic -> (Dynamic)AbstractUUIDFix.replaceUUIDLeastMost(dynamic, "UUID", "UUID").orElse(dynamic))));
    }
    
    private Dynamic<?> updateSkullOwner(final Dynamic<?> dynamic) {
        return dynamic.update("SkullOwner", dynamic -> (Dynamic)AbstractUUIDFix.replaceUUIDString(dynamic, "Id", "Id").orElse(dynamic));
    }
}
