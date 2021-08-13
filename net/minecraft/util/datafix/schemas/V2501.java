package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V2501 extends NamespacedSchema {
    public V2501(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    private static void registerFurnace(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register((Map)map, string, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "RecipesUsed", DSL.compoundList(References.RECIPE.in(schema), DSL.constType(DSL.intType()))));
    }
    
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = (Map<String, Supplier<TypeTemplate>>)super.registerBlockEntities(schema);
        registerFurnace(schema, map3, "minecraft:furnace");
        registerFurnace(schema, map3, "minecraft:smoker");
        registerFurnace(schema, map3, "minecraft:blast_furnace");
        return map3;
    }
}
