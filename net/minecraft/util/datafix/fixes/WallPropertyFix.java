package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Set;
import com.mojang.datafixers.DataFix;

public class WallPropertyFix extends DataFix {
    private static final Set<String> WALL_BLOCKS;
    
    public WallPropertyFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("WallPropertyFix", this.getInputSchema().getType(References.BLOCK_STATE), typed -> typed.update(DSL.remainderFinder(), WallPropertyFix::upgradeBlockStateTag));
    }
    
    private static String mapProperty(final String string) {
        return "true".equals(string) ? "low" : "none";
    }
    
    private static <T> Dynamic<T> fixWallProperty(final Dynamic<T> dynamic, final String string) {
        return (Dynamic<T>)dynamic.update(string, dynamic -> (Dynamic)DataFixUtils.orElse(dynamic.asString().result().map(WallPropertyFix::mapProperty).map(dynamic::createString), dynamic));
    }
    
    private static <T> Dynamic<T> upgradeBlockStateTag(final Dynamic<T> dynamic) {
        final boolean boolean2 = dynamic.get("Name").asString().result().filter(WallPropertyFix.WALL_BLOCKS::contains).isPresent();
        if (!boolean2) {
            return dynamic;
        }
        return (Dynamic<T>)dynamic.update("Properties", dynamic -> {
            Dynamic<?> dynamic2 = WallPropertyFix.fixWallProperty((com.mojang.serialization.Dynamic<?>)dynamic, "east");
            dynamic2 = WallPropertyFix.fixWallProperty(dynamic2, "west");
            dynamic2 = WallPropertyFix.fixWallProperty(dynamic2, "north");
            return WallPropertyFix.fixWallProperty(dynamic2, "south");
        });
    }
    
    static {
        WALL_BLOCKS = (Set)ImmutableSet.of("minecraft:andesite_wall", "minecraft:brick_wall", "minecraft:cobblestone_wall", "minecraft:diorite_wall", "minecraft:end_stone_brick_wall", "minecraft:granite_wall", (Object[])new String[] { "minecraft:mossy_cobblestone_wall", "minecraft:mossy_stone_brick_wall", "minecraft:nether_brick_wall", "minecraft:prismarine_wall", "minecraft:red_nether_brick_wall", "minecraft:red_sandstone_wall", "minecraft:sandstone_wall", "minecraft:stone_brick_wall" });
    }
}
