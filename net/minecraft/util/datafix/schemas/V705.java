package net.minecraft.util.datafix.schemas;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.google.common.collect.Maps;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.Hook;

public class V705 extends NamespacedSchema {
    protected static final Hook.HookFunction ADD_NAMES;
    
    public V705(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    protected static void registerMob(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register((Map)map, string, () -> V100.equipment(schema));
    }
    
    protected static void registerThrowableProjectile(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register((Map)map, string, () -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
    }
    
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = (Map<String, Supplier<TypeTemplate>>)Maps.newHashMap();
        schema.registerSimple((Map)map3, "minecraft:area_effect_cloud");
        registerMob(schema, map3, "minecraft:armor_stand");
        schema.register((Map)map3, "minecraft:arrow", string -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
        registerMob(schema, map3, "minecraft:bat");
        registerMob(schema, map3, "minecraft:blaze");
        schema.registerSimple((Map)map3, "minecraft:boat");
        registerMob(schema, map3, "minecraft:cave_spider");
        schema.register((Map)map3, "minecraft:chest_minecart", string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
        registerMob(schema, map3, "minecraft:chicken");
        schema.register((Map)map3, "minecraft:commandblock_minecart", string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema)));
        registerMob(schema, map3, "minecraft:cow");
        registerMob(schema, map3, "minecraft:creeper");
        schema.register((Map)map3, "minecraft:donkey", string -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema)));
        schema.registerSimple((Map)map3, "minecraft:dragon_fireball");
        registerThrowableProjectile(schema, map3, "minecraft:egg");
        registerMob(schema, map3, "minecraft:elder_guardian");
        schema.registerSimple((Map)map3, "minecraft:ender_crystal");
        registerMob(schema, map3, "minecraft:ender_dragon");
        schema.register((Map)map3, "minecraft:enderman", string -> DSL.optionalFields("carried", References.BLOCK_NAME.in(schema), V100.equipment(schema)));
        registerMob(schema, map3, "minecraft:endermite");
        registerThrowableProjectile(schema, map3, "minecraft:ender_pearl");
        schema.registerSimple((Map)map3, "minecraft:eye_of_ender_signal");
        schema.register((Map)map3, "minecraft:falling_block", string -> DSL.optionalFields("Block", References.BLOCK_NAME.in(schema), "TileEntityData", References.BLOCK_ENTITY.in(schema)));
        registerThrowableProjectile(schema, map3, "minecraft:fireball");
        schema.register((Map)map3, "minecraft:fireworks_rocket", string -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(schema)));
        schema.register((Map)map3, "minecraft:furnace_minecart", string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema)));
        registerMob(schema, map3, "minecraft:ghast");
        registerMob(schema, map3, "minecraft:giant");
        registerMob(schema, map3, "minecraft:guardian");
        schema.register((Map)map3, "minecraft:hopper_minecart", string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
        schema.register((Map)map3, "minecraft:horse", string -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(schema), "SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema)));
        registerMob(schema, map3, "minecraft:husk");
        schema.register((Map)map3, "minecraft:item", string -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
        schema.register((Map)map3, "minecraft:item_frame", string -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
        schema.registerSimple((Map)map3, "minecraft:leash_knot");
        registerMob(schema, map3, "minecraft:magma_cube");
        schema.register((Map)map3, "minecraft:minecart", string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema)));
        registerMob(schema, map3, "minecraft:mooshroom");
        schema.register((Map)map3, "minecraft:mule", string -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema)));
        registerMob(schema, map3, "minecraft:ocelot");
        schema.registerSimple((Map)map3, "minecraft:painting");
        schema.registerSimple((Map)map3, "minecraft:parrot");
        registerMob(schema, map3, "minecraft:pig");
        registerMob(schema, map3, "minecraft:polar_bear");
        schema.register((Map)map3, "minecraft:potion", string -> DSL.optionalFields("Potion", References.ITEM_STACK.in(schema), "inTile", References.BLOCK_NAME.in(schema)));
        registerMob(schema, map3, "minecraft:rabbit");
        registerMob(schema, map3, "minecraft:sheep");
        registerMob(schema, map3, "minecraft:shulker");
        schema.registerSimple((Map)map3, "minecraft:shulker_bullet");
        registerMob(schema, map3, "minecraft:silverfish");
        registerMob(schema, map3, "minecraft:skeleton");
        schema.register((Map)map3, "minecraft:skeleton_horse", string -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema)));
        registerMob(schema, map3, "minecraft:slime");
        registerThrowableProjectile(schema, map3, "minecraft:small_fireball");
        registerThrowableProjectile(schema, map3, "minecraft:snowball");
        registerMob(schema, map3, "minecraft:snowman");
        schema.register((Map)map3, "minecraft:spawner_minecart", string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), References.UNTAGGED_SPAWNER.in(schema)));
        schema.register((Map)map3, "minecraft:spectral_arrow", string -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
        registerMob(schema, map3, "minecraft:spider");
        registerMob(schema, map3, "minecraft:squid");
        registerMob(schema, map3, "minecraft:stray");
        schema.registerSimple((Map)map3, "minecraft:tnt");
        schema.register((Map)map3, "minecraft:tnt_minecart", string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema)));
        schema.register((Map)map3, "minecraft:villager", string -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(schema), "buyB", References.ITEM_STACK.in(schema), "sell", References.ITEM_STACK.in(schema)))), V100.equipment(schema)));
        registerMob(schema, map3, "minecraft:villager_golem");
        registerMob(schema, map3, "minecraft:witch");
        registerMob(schema, map3, "minecraft:wither");
        registerMob(schema, map3, "minecraft:wither_skeleton");
        registerThrowableProjectile(schema, map3, "minecraft:wither_skull");
        registerMob(schema, map3, "minecraft:wolf");
        registerThrowableProjectile(schema, map3, "minecraft:xp_bottle");
        schema.registerSimple((Map)map3, "minecraft:xp_orb");
        registerMob(schema, map3, "minecraft:zombie");
        schema.register((Map)map3, "minecraft:zombie_horse", string -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema)));
        registerMob(schema, map3, "minecraft:zombie_pigman");
        registerMob(schema, map3, "minecraft:zombie_villager");
        schema.registerSimple((Map)map3, "minecraft:evocation_fangs");
        registerMob(schema, map3, "minecraft:evocation_illager");
        schema.registerSimple((Map)map3, "minecraft:illusion_illager");
        schema.register((Map)map3, "minecraft:llama", string -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), "DecorItem", References.ITEM_STACK.in(schema), V100.equipment(schema)));
        schema.registerSimple((Map)map3, "minecraft:llama_spit");
        registerMob(schema, map3, "minecraft:vex");
        registerMob(schema, map3, "minecraft:vindication_illager");
        return map3;
    }
    
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> map2, final Map<String, Supplier<TypeTemplate>> map3) {
        super.registerTypes(schema, (Map)map2, (Map)map3);
        schema.registerType(true, References.ENTITY, () -> DSL.taggedChoiceLazy("id", (Type)NamespacedSchema.namespacedString(), map2));
        schema.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(schema), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(schema), "BlockEntityTag", References.BLOCK_ENTITY.in(schema), "CanDestroy", DSL.list(References.BLOCK_NAME.in(schema)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(schema)))), V705.ADD_NAMES, Hook.HookFunction.IDENTITY));
    }
    
    static {
        ADD_NAMES = (Hook.HookFunction)new Hook.HookFunction() {
            public <T> T apply(final DynamicOps<T> dynamicOps, final T object) {
                return V99.<T>addNames((com.mojang.serialization.Dynamic<T>)new Dynamic((DynamicOps)dynamicOps, object), V704.ITEM_TO_BLOCKENTITY, "minecraft:armor_stand");
            }
        };
    }
}
