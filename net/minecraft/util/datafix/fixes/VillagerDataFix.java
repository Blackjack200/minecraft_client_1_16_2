package net.minecraft.util.datafix.fixes;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DSL;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class VillagerDataFix extends NamedEntityFix {
    public VillagerDataFix(final Schema schema, final String string) {
        super(schema, false, "Villager profession data fix (" + string + ")", References.ENTITY, string);
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        final Dynamic<?> dynamic3 = typed.get(DSL.remainderFinder());
        return typed.set(DSL.remainderFinder(), dynamic3.remove("Profession").remove("Career").remove("CareerLevel").set("VillagerData", dynamic3.createMap((Map)ImmutableMap.of((Object)dynamic3.createString("type"), (Object)dynamic3.createString("minecraft:plains"), (Object)dynamic3.createString("profession"), (Object)dynamic3.createString(upgradeData(dynamic3.get("Profession").asInt(0), dynamic3.get("Career").asInt(0))), (Object)dynamic3.createString("level"), DataFixUtils.orElse(dynamic3.get("CareerLevel").result(), (Object)dynamic3.createInt(1))))));
    }
    
    private static String upgradeData(final int integer1, final int integer2) {
        if (integer1 == 0) {
            if (integer2 == 2) {
                return "minecraft:fisherman";
            }
            if (integer2 == 3) {
                return "minecraft:shepherd";
            }
            if (integer2 == 4) {
                return "minecraft:fletcher";
            }
            return "minecraft:farmer";
        }
        else if (integer1 == 1) {
            if (integer2 == 2) {
                return "minecraft:cartographer";
            }
            return "minecraft:librarian";
        }
        else {
            if (integer1 == 2) {
                return "minecraft:cleric";
            }
            if (integer1 == 3) {
                if (integer2 == 2) {
                    return "minecraft:weaponsmith";
                }
                if (integer2 == 3) {
                    return "minecraft:toolsmith";
                }
                return "minecraft:armorer";
            }
            else if (integer1 == 4) {
                if (integer2 == 2) {
                    return "minecraft:leatherworker";
                }
                return "minecraft:butcher";
            }
            else {
                if (integer1 == 5) {
                    return "minecraft:nitwit";
                }
                return "minecraft:none";
            }
        }
    }
}
