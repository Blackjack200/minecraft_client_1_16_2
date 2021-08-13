package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class OptionsRenameFieldFix extends DataFix {
    private final String fixName;
    private final String fieldFrom;
    private final String fieldTo;
    
    public OptionsRenameFieldFix(final Schema schema, final boolean boolean2, final String string3, final String string4, final String string5) {
        super(schema, boolean2);
        this.fixName = string3;
        this.fieldFrom = string4;
        this.fieldTo = string5;
    }
    
    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped(this.fixName, this.getInputSchema().getType(References.OPTIONS), typed -> typed.update(DSL.remainderFinder(), dynamic -> (Dynamic)DataFixUtils.orElse(dynamic.get(this.fieldFrom).result().map(dynamic2 -> dynamic.set(this.fieldTo, dynamic2).remove(this.fieldFrom)), dynamic)));
    }
}
