package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.TypeRewriteRule;
import java.util.Optional;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;
import com.mojang.datafixers.DataFix;

public class JigsawRotationFix extends DataFix {
    private static final Map<String, String> renames;
    
    public JigsawRotationFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    private static Dynamic<?> fix(final Dynamic<?> dynamic) {
        final Optional<String> optional2 = (Optional<String>)dynamic.get("Name").asString().result();
        if (optional2.equals(Optional.of("minecraft:jigsaw"))) {
            return dynamic.update("Properties", dynamic -> {
                final String string2 = dynamic.get("facing").asString("north");
                return dynamic.remove("facing").set("orientation", dynamic.createString((String)JigsawRotationFix.renames.getOrDefault(string2, string2)));
            });
        }
        return dynamic;
    }
    
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("jigsaw_rotation_fix", this.getInputSchema().getType(References.BLOCK_STATE), typed -> typed.update(DSL.remainderFinder(), JigsawRotationFix::fix));
    }
    
    static {
        renames = (Map)ImmutableMap.builder().put("down", "down_south").put("up", "up_north").put("north", "north_up").put("south", "south_up").put("west", "west_up").put("east", "east_up").build();
    }
}
