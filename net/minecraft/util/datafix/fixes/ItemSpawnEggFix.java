package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.DataResult;
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

public class ItemSpawnEggFix extends DataFix {
    private static final String[] ID_TO_ENTITY;
    
    public ItemSpawnEggFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Schema schema2 = this.getInputSchema();
        final Type<?> type3 = schema2.getType(References.ITEM_STACK);
        final OpticFinder<Pair<String, String>> opticFinder4 = (OpticFinder<Pair<String, String>>)DSL.fieldFinder("id", DSL.named(References.ITEM_NAME.typeName(), (Type)NamespacedSchema.namespacedString()));
        final OpticFinder<String> opticFinder5 = (OpticFinder<String>)DSL.fieldFinder("id", DSL.string());
        final OpticFinder<?> opticFinder6 = type3.findField("tag");
        final OpticFinder<?> opticFinder7 = opticFinder6.type().findField("EntityTag");
        final OpticFinder<?> opticFinder8 = DSL.typeFinder(schema2.getTypeRaw(References.ENTITY));
        final Type<?> type4 = this.getOutputSchema().getTypeRaw(References.ENTITY);
        return this.fixTypeEverywhereTyped("ItemSpawnEggFix", (Type)type3, typed -> {
            final Optional<Pair<String, String>> optional8 = (Optional<Pair<String, String>>)typed.getOptional(opticFinder4);
            if (optional8.isPresent() && Objects.equals(((Pair)optional8.get()).getSecond(), "minecraft:spawn_egg")) {
                Dynamic<?> dynamic9 = typed.get(DSL.remainderFinder());
                final short short10 = dynamic9.get("Damage").asShort((short)0);
                final Optional<? extends Typed<?>> optional9 = typed.getOptionalTyped(opticFinder6);
                final Optional<? extends Typed<?>> optional10 = optional9.flatMap(typed -> typed.getOptionalTyped(opticFinder7));
                final Optional<? extends Typed<?>> optional11 = optional10.flatMap(typed -> typed.getOptionalTyped(opticFinder8));
                final Optional<String> optional12 = (Optional<String>)optional11.flatMap(typed -> typed.getOptional(opticFinder5));
                Typed<?> typed2 = typed;
                final String string16 = ItemSpawnEggFix.ID_TO_ENTITY[short10 & 0xFF];
                if (string16 != null && (!optional12.isPresent() || !Objects.equals(optional12.get(), string16))) {
                    final Typed<?> typed3 = typed.getOrCreateTyped(opticFinder6);
                    final Typed<?> typed4 = typed3.getOrCreateTyped(opticFinder7);
                    final Typed<?> typed5 = typed4.getOrCreateTyped(opticFinder8);
                    final Dynamic<?> dynamic10 = dynamic9;
                    final Typed<?> typed6 = ((Pair)typed5.write().flatMap(dynamic4 -> type4.readTyped(dynamic4.set("id", dynamic10.createString(string16)))).result().orElseThrow(() -> new IllegalStateException("Could not parse new entity"))).getFirst();
                    typed2 = typed2.set(opticFinder6, typed3.set(opticFinder7, typed4.set(opticFinder8, (Typed)typed6)));
                }
                if (short10 != 0) {
                    dynamic9 = dynamic9.set("Damage", dynamic9.createShort((short)0));
                    typed2 = typed2.set(DSL.remainderFinder(), dynamic9);
                }
                return typed2;
            }
            return typed;
        });
    }
    
    static {
        ID_TO_ENTITY = (String[])DataFixUtils.make(new String[256], arr -> {
            arr[1] = "Item";
            arr[2] = "XPOrb";
            arr[7] = "ThrownEgg";
            arr[8] = "LeashKnot";
            arr[9] = "Painting";
            arr[10] = "Arrow";
            arr[11] = "Snowball";
            arr[12] = "Fireball";
            arr[13] = "SmallFireball";
            arr[14] = "ThrownEnderpearl";
            arr[15] = "EyeOfEnderSignal";
            arr[16] = "ThrownPotion";
            arr[17] = "ThrownExpBottle";
            arr[18] = "ItemFrame";
            arr[19] = "WitherSkull";
            arr[20] = "PrimedTnt";
            arr[21] = "FallingSand";
            arr[22] = "FireworksRocketEntity";
            arr[23] = "TippedArrow";
            arr[24] = "SpectralArrow";
            arr[25] = "ShulkerBullet";
            arr[26] = "DragonFireball";
            arr[30] = "ArmorStand";
            arr[41] = "Boat";
            arr[42] = "MinecartRideable";
            arr[43] = "MinecartChest";
            arr[44] = "MinecartFurnace";
            arr[45] = "MinecartTNT";
            arr[46] = "MinecartHopper";
            arr[47] = "MinecartSpawner";
            arr[40] = "MinecartCommandBlock";
            arr[48] = "Mob";
            arr[49] = "Monster";
            arr[50] = "Creeper";
            arr[51] = "Skeleton";
            arr[52] = "Spider";
            arr[53] = "Giant";
            arr[54] = "Zombie";
            arr[55] = "Slime";
            arr[56] = "Ghast";
            arr[57] = "PigZombie";
            arr[58] = "Enderman";
            arr[59] = "CaveSpider";
            arr[60] = "Silverfish";
            arr[61] = "Blaze";
            arr[62] = "LavaSlime";
            arr[63] = "EnderDragon";
            arr[64] = "WitherBoss";
            arr[65] = "Bat";
            arr[66] = "Witch";
            arr[67] = "Endermite";
            arr[68] = "Guardian";
            arr[69] = "Shulker";
            arr[90] = "Pig";
            arr[91] = "Sheep";
            arr[92] = "Cow";
            arr[93] = "Chicken";
            arr[94] = "Squid";
            arr[95] = "Wolf";
            arr[96] = "MushroomCow";
            arr[97] = "SnowMan";
            arr[98] = "Ozelot";
            arr[99] = "VillagerGolem";
            arr[100] = "EntityHorse";
            arr[101] = "Rabbit";
            arr[120] = "Villager";
            arr[200] = "EnderCrystal";
        });
    }
}
