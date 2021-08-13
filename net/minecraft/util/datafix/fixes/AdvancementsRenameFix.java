package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.function.Function;
import com.mojang.datafixers.DataFix;

public class AdvancementsRenameFix extends DataFix {
    private final String name;
    private final Function<String, String> renamer;
    
    public AdvancementsRenameFix(final Schema schema, final boolean boolean2, final String string, final Function<String, String> function) {
        super(schema, boolean2);
        this.name = string;
        this.renamer = function;
    }
    
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped(this.name, this.getInputSchema().getType(References.ADVANCEMENTS), typed -> typed.update(DSL.remainderFinder(), dynamic -> dynamic.updateMapValues(pair -> {
            final String string4 = ((Dynamic)pair.getFirst()).asString("");
            return pair.mapFirst(dynamic3 -> dynamic.createString((String)this.renamer.apply(string4)));
        })));
    }
}
