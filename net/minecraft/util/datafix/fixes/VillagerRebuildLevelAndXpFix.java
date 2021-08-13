package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.templates.List;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import net.minecraft.util.Mth;
import com.mojang.datafixers.DataFix;

public class VillagerRebuildLevelAndXpFix extends DataFix {
    private static final int[] LEVEL_XP_THRESHOLDS;
    
    public static int getMinXpPerLevel(final int integer) {
        return VillagerRebuildLevelAndXpFix.LEVEL_XP_THRESHOLDS[Mth.clamp(integer - 1, 0, VillagerRebuildLevelAndXpFix.LEVEL_XP_THRESHOLDS.length - 1)];
    }
    
    public VillagerRebuildLevelAndXpFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getChoiceType(References.ENTITY, "minecraft:villager");
        final OpticFinder<?> opticFinder3 = DSL.namedChoice("minecraft:villager", (Type)type2);
        final OpticFinder<?> opticFinder4 = type2.findField("Offers");
        final Type<?> type3 = opticFinder4.type();
        final OpticFinder<?> opticFinder5 = type3.findField("Recipes");
        final List.ListType<?> listType7 = opticFinder5.type();
        final OpticFinder<?> opticFinder6 = listType7.getElement().finder();
        return this.fixTypeEverywhereTyped("Villager level and xp rebuild", this.getInputSchema().getType(References.ENTITY), typed -> typed.updateTyped(opticFinder3, type2, typed -> {
            final Dynamic<?> dynamic5 = typed.get(DSL.remainderFinder());
            int integer6 = dynamic5.get("VillagerData").get("level").asInt(0);
            Typed<?> typed2 = typed;
            if (integer6 == 0 || integer6 == 1) {
                final int integer7 = (int)typed.getOptionalTyped(opticFinder4).flatMap(typed -> typed.getOptionalTyped(opticFinder5)).map(typed -> typed.getAllTyped(opticFinder6).size()).orElse(0);
                integer6 = Mth.clamp(integer7 / 2, 1, 5);
                if (integer6 > 1) {
                    typed2 = addLevel(typed2, integer6);
                }
            }
            final Optional<Number> optional8 = (Optional<Number>)dynamic5.get("Xp").asNumber().result();
            if (!optional8.isPresent()) {
                typed2 = addXpFromLevel(typed2, integer6);
            }
            return typed2;
        }));
    }
    
    private static Typed<?> addLevel(final Typed<?> typed, final int integer) {
        return typed.update(DSL.remainderFinder(), dynamic -> dynamic.update("VillagerData", dynamic -> dynamic.set("level", dynamic.createInt(integer))));
    }
    
    private static Typed<?> addXpFromLevel(final Typed<?> typed, final int integer) {
        final int integer2 = getMinXpPerLevel(integer);
        return typed.update(DSL.remainderFinder(), dynamic -> dynamic.set("Xp", dynamic.createInt(integer2)));
    }
    
    static {
        LEVEL_XP_THRESHOLDS = new int[] { 0, 10, 50, 100, 150 };
    }
}
