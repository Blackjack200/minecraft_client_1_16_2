package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V1451_6 extends NamespacedSchema {
    public V1451_6(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> map2, final Map<String, Supplier<TypeTemplate>> map3) {
        super.registerTypes(schema, (Map)map2, (Map)map3);
        final Supplier<TypeTemplate> supplier5 = (Supplier<TypeTemplate>)(() -> DSL.compoundList(References.ITEM_NAME.in(schema), DSL.constType(DSL.intType())));
        schema.registerType(false, References.STATS, () -> DSL.optionalFields("stats", DSL.optionalFields("minecraft:mined", DSL.compoundList(References.BLOCK_NAME.in(schema), DSL.constType(DSL.intType())), "minecraft:crafted", (TypeTemplate)supplier5.get(), "minecraft:used", (TypeTemplate)supplier5.get(), "minecraft:broken", (TypeTemplate)supplier5.get(), "minecraft:picked_up", (TypeTemplate)supplier5.get(), DSL.optionalFields("minecraft:dropped", (TypeTemplate)supplier5.get(), "minecraft:killed", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType())), "minecraft:killed_by", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType())), "minecraft:custom", DSL.compoundList(DSL.constType((Type)NamespacedSchema.namespacedString()), DSL.constType(DSL.intType()))))));
    }
}
