package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import java.util.Objects;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;
import com.mojang.datafixers.DataFix;

public class ChunkStatusFix2 extends DataFix {
    private static final Map<String, String> RENAMES_AND_DOWNGRADES;
    
    public ChunkStatusFix2(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.CHUNK);
        final Type<?> type3 = type2.findFieldType("Level");
        final OpticFinder<?> opticFinder4 = DSL.fieldFinder("Level", (Type)type3);
        return this.fixTypeEverywhereTyped("ChunkStatusFix2", (Type)type2, this.getOutputSchema().getType(References.CHUNK), typed -> typed.updateTyped(opticFinder4, typed -> {
            final Dynamic<?> dynamic2 = typed.get(DSL.remainderFinder());
            final String string3 = dynamic2.get("Status").asString("empty");
            final String string4 = (String)ChunkStatusFix2.RENAMES_AND_DOWNGRADES.getOrDefault(string3, "empty");
            if (Objects.equals(string3, string4)) {
                return typed;
            }
            return typed.set(DSL.remainderFinder(), dynamic2.set("Status", dynamic2.createString(string4)));
        }));
    }
    
    static {
        RENAMES_AND_DOWNGRADES = (Map)ImmutableMap.builder().put("structure_references", "empty").put("biomes", "empty").put("base", "surface").put("carved", "carvers").put("liquid_carved", "liquid_carvers").put("decorated", "features").put("lighted", "light").put("mobs_spawned", "spawn").put("finalized", "heightmaps").put("fullchunk", "full").build();
    }
}
