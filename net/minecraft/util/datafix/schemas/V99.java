package net.minecraft.util.datafix.schemas;

import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.DataFixUtils;
import org.apache.logging.log4j.LogManager;
import com.mojang.datafixers.types.Type;
import java.util.HashMap;
import java.util.Objects;
import com.mojang.serialization.Dynamic;
import com.google.common.collect.Maps;
import java.util.function.Supplier;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.types.templates.Hook;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import com.mojang.datafixers.schemas.Schema;

public class V99 extends Schema {
    private static final Logger LOGGER;
    private static final Map<String, String> ITEM_TO_BLOCKENTITY;
    protected static final Hook.HookFunction ADD_NAMES;
    
    public V99(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    protected static TypeTemplate equipment(final Schema schema) {
        return DSL.optionalFields("Equipment", DSL.list(References.ITEM_STACK.in(schema)));
    }
    
    protected static void registerMob(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register((Map)map, string, () -> equipment(schema));
    }
    
    protected static void registerThrowableProjectile(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register((Map)map, string, () -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
    }
    
    protected static void registerMinecart(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register((Map)map, string, () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema)));
    }
    
    protected static void registerInventory(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register((Map)map, string, () -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema))));
    }
    
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = (Map<String, Supplier<TypeTemplate>>)Maps.newHashMap();
        schema.register((Map)map3, "Item", string -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
        schema.registerSimple((Map)map3, "XPOrb");
        registerThrowableProjectile(schema, map3, "ThrownEgg");
        schema.registerSimple((Map)map3, "LeashKnot");
        schema.registerSimple((Map)map3, "Painting");
        schema.register((Map)map3, "Arrow", string -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
        schema.register((Map)map3, "TippedArrow", string -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
        schema.register((Map)map3, "SpectralArrow", string -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema)));
        registerThrowableProjectile(schema, map3, "Snowball");
        registerThrowableProjectile(schema, map3, "Fireball");
        registerThrowableProjectile(schema, map3, "SmallFireball");
        registerThrowableProjectile(schema, map3, "ThrownEnderpearl");
        schema.registerSimple((Map)map3, "EyeOfEnderSignal");
        schema.register((Map)map3, "ThrownPotion", string -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema), "Potion", References.ITEM_STACK.in(schema)));
        registerThrowableProjectile(schema, map3, "ThrownExpBottle");
        schema.register((Map)map3, "ItemFrame", string -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
        registerThrowableProjectile(schema, map3, "WitherSkull");
        schema.registerSimple((Map)map3, "PrimedTnt");
        schema.register((Map)map3, "FallingSand", string -> DSL.optionalFields("Block", References.BLOCK_NAME.in(schema), "TileEntityData", References.BLOCK_ENTITY.in(schema)));
        schema.register((Map)map3, "FireworksRocketEntity", string -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(schema)));
        schema.registerSimple((Map)map3, "Boat");
        schema.register((Map)map3, "Minecart", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
        registerMinecart(schema, map3, "MinecartRideable");
        schema.register((Map)map3, "MinecartChest", string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
        registerMinecart(schema, map3, "MinecartFurnace");
        registerMinecart(schema, map3, "MinecartTNT");
        schema.register((Map)map3, "MinecartSpawner", () -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), References.UNTAGGED_SPAWNER.in(schema)));
        schema.register((Map)map3, "MinecartHopper", string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
        registerMinecart(schema, map3, "MinecartCommandBlock");
        registerMob(schema, map3, "ArmorStand");
        registerMob(schema, map3, "Creeper");
        registerMob(schema, map3, "Skeleton");
        registerMob(schema, map3, "Spider");
        registerMob(schema, map3, "Giant");
        registerMob(schema, map3, "Zombie");
        registerMob(schema, map3, "Slime");
        registerMob(schema, map3, "Ghast");
        registerMob(schema, map3, "PigZombie");
        schema.register((Map)map3, "Enderman", string -> DSL.optionalFields("carried", References.BLOCK_NAME.in(schema), equipment(schema)));
        registerMob(schema, map3, "CaveSpider");
        registerMob(schema, map3, "Silverfish");
        registerMob(schema, map3, "Blaze");
        registerMob(schema, map3, "LavaSlime");
        registerMob(schema, map3, "EnderDragon");
        registerMob(schema, map3, "WitherBoss");
        registerMob(schema, map3, "Bat");
        registerMob(schema, map3, "Witch");
        registerMob(schema, map3, "Endermite");
        registerMob(schema, map3, "Guardian");
        registerMob(schema, map3, "Pig");
        registerMob(schema, map3, "Sheep");
        registerMob(schema, map3, "Cow");
        registerMob(schema, map3, "Chicken");
        registerMob(schema, map3, "Squid");
        registerMob(schema, map3, "Wolf");
        registerMob(schema, map3, "MushroomCow");
        registerMob(schema, map3, "SnowMan");
        registerMob(schema, map3, "Ozelot");
        registerMob(schema, map3, "VillagerGolem");
        schema.register((Map)map3, "EntityHorse", string -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "ArmorItem", References.ITEM_STACK.in(schema), "SaddleItem", References.ITEM_STACK.in(schema), equipment(schema)));
        registerMob(schema, map3, "Rabbit");
        schema.register((Map)map3, "Villager", string -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(schema), "buyB", References.ITEM_STACK.in(schema), "sell", References.ITEM_STACK.in(schema)))), equipment(schema)));
        schema.registerSimple((Map)map3, "EnderCrystal");
        schema.registerSimple((Map)map3, "AreaEffectCloud");
        schema.registerSimple((Map)map3, "ShulkerBullet");
        registerMob(schema, map3, "Shulker");
        return map3;
    }
    
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = (Map<String, Supplier<TypeTemplate>>)Maps.newHashMap();
        registerInventory(schema, map3, "Furnace");
        registerInventory(schema, map3, "Chest");
        schema.registerSimple((Map)map3, "EnderChest");
        schema.register((Map)map3, "RecordPlayer", string -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(schema)));
        registerInventory(schema, map3, "Trap");
        registerInventory(schema, map3, "Dropper");
        schema.registerSimple((Map)map3, "Sign");
        schema.register((Map)map3, "MobSpawner", string -> References.UNTAGGED_SPAWNER.in(schema));
        schema.registerSimple((Map)map3, "Music");
        schema.registerSimple((Map)map3, "Piston");
        registerInventory(schema, map3, "Cauldron");
        schema.registerSimple((Map)map3, "EnchantTable");
        schema.registerSimple((Map)map3, "Airportal");
        schema.registerSimple((Map)map3, "Control");
        schema.registerSimple((Map)map3, "Beacon");
        schema.registerSimple((Map)map3, "Skull");
        schema.registerSimple((Map)map3, "DLDetector");
        registerInventory(schema, map3, "Hopper");
        schema.registerSimple((Map)map3, "Comparator");
        schema.register((Map)map3, "FlowerPot", string -> DSL.optionalFields("Item", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(schema))));
        schema.registerSimple((Map)map3, "Banner");
        schema.registerSimple((Map)map3, "Structure");
        schema.registerSimple((Map)map3, "EndGateway");
        return map3;
    }
    
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> map2, final Map<String, Supplier<TypeTemplate>> map3) {
        schema.registerType(false, References.LEVEL, DSL::remainder);
        schema.registerType(false, References.PLAYER, () -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "EnderItems", DSL.list(References.ITEM_STACK.in(schema))));
        schema.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema)), "TileEntities", DSL.list(References.BLOCK_ENTITY.in(schema)), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema))))));
        schema.registerType(true, References.BLOCK_ENTITY, () -> DSL.taggedChoiceLazy("id", DSL.string(), map3));
        schema.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Riding", References.ENTITY_TREE.in(schema), References.ENTITY.in(schema)));
        schema.registerType(false, References.ENTITY_NAME, () -> DSL.constType((Type)NamespacedSchema.namespacedString()));
        schema.registerType(true, References.ENTITY, () -> DSL.taggedChoiceLazy("id", DSL.string(), map2));
        schema.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(schema)), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(schema), "BlockEntityTag", References.BLOCK_ENTITY.in(schema), "CanDestroy", DSL.list(References.BLOCK_NAME.in(schema)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(schema)))), V99.ADD_NAMES, Hook.HookFunction.IDENTITY));
        schema.registerType(false, References.OPTIONS, DSL::remainder);
        schema.registerType(false, References.BLOCK_NAME, () -> DSL.or(DSL.constType(DSL.intType()), DSL.constType((Type)NamespacedSchema.namespacedString())));
        schema.registerType(false, References.ITEM_NAME, () -> DSL.constType((Type)NamespacedSchema.namespacedString()));
        schema.registerType(false, References.STATS, DSL::remainder);
        schema.registerType(false, References.SAVED_DATA, () -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(schema)), "Objectives", DSL.list(References.OBJECTIVE.in(schema)), "Teams", DSL.list(References.TEAM.in(schema)))));
        schema.registerType(false, References.STRUCTURE_FEATURE, DSL::remainder);
        schema.registerType(false, References.OBJECTIVE, DSL::remainder);
        schema.registerType(false, References.TEAM, DSL::remainder);
        schema.registerType(true, References.UNTAGGED_SPAWNER, DSL::remainder);
        schema.registerType(false, References.POI_CHUNK, DSL::remainder);
        schema.registerType(true, References.WORLD_GEN_SETTINGS, DSL::remainder);
    }
    
    protected static <T> T addNames(final Dynamic<T> dynamic, final Map<String, String> map, final String string) {
        return (T)dynamic.update("tag", dynamic4 -> dynamic4.update("BlockEntityTag", dynamic3 -> {
            final String string4 = dynamic.get("id").asString("");
            final String string5 = (String)map.get(NamespacedSchema.ensureNamespaced(string4));
            if (string5 == null) {
                V99.LOGGER.warn("Unable to resolve BlockEntity for ItemStack: {}", string4);
                return dynamic3;
            }
            return dynamic3.set("id", dynamic.createString(string5));
        }).update("EntityTag", dynamic3 -> {
            final String string2 = dynamic.get("id").asString("");
            if (Objects.equals(NamespacedSchema.ensureNamespaced(string2), "minecraft:armor_stand")) {
                return dynamic3.set("id", dynamic.createString(string));
            }
            return dynamic3;
        })).getValue();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ITEM_TO_BLOCKENTITY = (Map)DataFixUtils.make(Maps.newHashMap(), hashMap -> {
            hashMap.put("minecraft:furnace", "Furnace");
            hashMap.put("minecraft:lit_furnace", "Furnace");
            hashMap.put("minecraft:chest", "Chest");
            hashMap.put("minecraft:trapped_chest", "Chest");
            hashMap.put("minecraft:ender_chest", "EnderChest");
            hashMap.put("minecraft:jukebox", "RecordPlayer");
            hashMap.put("minecraft:dispenser", "Trap");
            hashMap.put("minecraft:dropper", "Dropper");
            hashMap.put("minecraft:sign", "Sign");
            hashMap.put("minecraft:mob_spawner", "MobSpawner");
            hashMap.put("minecraft:noteblock", "Music");
            hashMap.put("minecraft:brewing_stand", "Cauldron");
            hashMap.put("minecraft:enhanting_table", "EnchantTable");
            hashMap.put("minecraft:command_block", "CommandBlock");
            hashMap.put("minecraft:beacon", "Beacon");
            hashMap.put("minecraft:skull", "Skull");
            hashMap.put("minecraft:daylight_detector", "DLDetector");
            hashMap.put("minecraft:hopper", "Hopper");
            hashMap.put("minecraft:banner", "Banner");
            hashMap.put("minecraft:flower_pot", "FlowerPot");
            hashMap.put("minecraft:repeating_command_block", "CommandBlock");
            hashMap.put("minecraft:chain_command_block", "CommandBlock");
            hashMap.put("minecraft:standing_sign", "Sign");
            hashMap.put("minecraft:wall_sign", "Sign");
            hashMap.put("minecraft:piston_head", "Piston");
            hashMap.put("minecraft:daylight_detector_inverted", "DLDetector");
            hashMap.put("minecraft:unpowered_comparator", "Comparator");
            hashMap.put("minecraft:powered_comparator", "Comparator");
            hashMap.put("minecraft:wall_banner", "Banner");
            hashMap.put("minecraft:standing_banner", "Banner");
            hashMap.put("minecraft:structure_block", "Structure");
            hashMap.put("minecraft:end_portal", "Airportal");
            hashMap.put("minecraft:end_gateway", "EndGateway");
            hashMap.put("minecraft:shield", "Banner");
        });
        ADD_NAMES = (Hook.HookFunction)new Hook.HookFunction() {
            public <T> T apply(final DynamicOps<T> dynamicOps, final T object) {
                return V99.<T>addNames((com.mojang.serialization.Dynamic<T>)new Dynamic((DynamicOps)dynamicOps, object), (Map<String, String>)V99.ITEM_TO_BLOCKENTITY, "ArmorStand");
            }
        };
    }
}
