package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import java.util.Locale;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class OptionsLowerCaseLanguageFix extends DataFix {
    public OptionsLowerCaseLanguageFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("OptionsLowerCaseLanguageFix", this.getInputSchema().getType(References.OPTIONS), typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            final Optional<String> optional2 = (Optional<String>)dynamic.get("lang").asString().result();
            if (optional2.isPresent()) {
                return dynamic.set("lang", dynamic.createString(((String)optional2.get()).toLowerCase(Locale.ROOT)));
            }
            return dynamic;
        }));
    }
}
