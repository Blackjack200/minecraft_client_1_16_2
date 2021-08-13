package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.OptionalDynamic;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.List;
import com.mojang.serialization.Codec;
import com.mojang.datafixers.DataFix;

public class EntityRedundantChanceTagsFix extends DataFix {
    private static final Codec<List<Float>> FLOAT_LIST_CODEC;
    
    public EntityRedundantChanceTagsFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("EntityRedundantChanceTagsFix", this.getInputSchema().getType(References.ENTITY), typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            if (isZeroList(dynamic.get("HandDropChances"), 2)) {
                dynamic = dynamic.remove("HandDropChances");
            }
            if (isZeroList(dynamic.get("ArmorDropChances"), 4)) {
                dynamic = dynamic.remove("ArmorDropChances");
            }
            return dynamic;
        }));
    }
    
    private static boolean isZeroList(final OptionalDynamic<?> optionalDynamic, final int integer) {
        return (boolean)optionalDynamic.flatMap(EntityRedundantChanceTagsFix.FLOAT_LIST_CODEC::parse).map(list -> list.size() == integer && list.stream().allMatch(float1 -> float1 == 0.0f)).result().orElse(false);
    }
    
    static {
        FLOAT_LIST_CODEC = Codec.FLOAT.listOf();
    }
}
