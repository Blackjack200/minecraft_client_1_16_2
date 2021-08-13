package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.Typed;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class EntityPaintingItemFrameDirectionFix extends DataFix {
    private static final int[][] DIRECTIONS;
    
    public EntityPaintingItemFrameDirectionFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    private Dynamic<?> doFix(Dynamic<?> dynamic, final boolean boolean2, final boolean boolean3) {
        if ((boolean2 || boolean3) && !dynamic.get("Facing").asNumber().result().isPresent()) {
            int integer5;
            if (dynamic.get("Direction").asNumber().result().isPresent()) {
                integer5 = dynamic.get("Direction").asByte((byte)0) % EntityPaintingItemFrameDirectionFix.DIRECTIONS.length;
                final int[] arr6 = EntityPaintingItemFrameDirectionFix.DIRECTIONS[integer5];
                dynamic = dynamic.set("TileX", dynamic.createInt(dynamic.get("TileX").asInt(0) + arr6[0]));
                dynamic = dynamic.set("TileY", dynamic.createInt(dynamic.get("TileY").asInt(0) + arr6[1]));
                dynamic = dynamic.set("TileZ", dynamic.createInt(dynamic.get("TileZ").asInt(0) + arr6[2]));
                dynamic = dynamic.remove("Direction");
                if (boolean3 && dynamic.get("ItemRotation").asNumber().result().isPresent()) {
                    dynamic = dynamic.set("ItemRotation", dynamic.createByte((byte)(dynamic.get("ItemRotation").asByte((byte)0) * 2)));
                }
            }
            else {
                integer5 = dynamic.get("Dir").asByte((byte)0) % EntityPaintingItemFrameDirectionFix.DIRECTIONS.length;
                dynamic = dynamic.remove("Dir");
            }
            dynamic = dynamic.set("Facing", dynamic.createByte((byte)integer5));
        }
        return dynamic;
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getChoiceType(References.ENTITY, "Painting");
        final OpticFinder<?> opticFinder3 = DSL.namedChoice("Painting", (Type)type2);
        final Type<?> type3 = this.getInputSchema().getChoiceType(References.ENTITY, "ItemFrame");
        final OpticFinder<?> opticFinder4 = DSL.namedChoice("ItemFrame", (Type)type3);
        final Type<?> type4 = this.getInputSchema().getType(References.ENTITY);
        final TypeRewriteRule typeRewriteRule7 = this.fixTypeEverywhereTyped("EntityPaintingFix", (Type)type4, typed -> typed.updateTyped(opticFinder3, type2, typed -> typed.update(DSL.remainderFinder(), dynamic -> this.doFix(dynamic, true, false))));
        final TypeRewriteRule typeRewriteRule8 = this.fixTypeEverywhereTyped("EntityItemFrameFix", (Type)type4, typed -> typed.updateTyped(opticFinder4, type3, typed -> typed.update(DSL.remainderFinder(), dynamic -> this.doFix(dynamic, false, true))));
        return TypeRewriteRule.seq(typeRewriteRule7, typeRewriteRule8);
    }
    
    static {
        DIRECTIONS = new int[][] { { 0, 0, 1 }, { -1, 0, 0 }, { 0, 0, -1 }, { 1, 0, 0 } };
    }
}
