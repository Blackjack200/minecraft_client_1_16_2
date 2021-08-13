package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.Typed;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;

public abstract class NamedEntityFix extends DataFix {
    private final String name;
    private final String entityName;
    private final DSL.TypeReference type;
    
    public NamedEntityFix(final Schema schema, final boolean boolean2, final String string3, final DSL.TypeReference typeReference, final String string5) {
        super(schema, boolean2);
        this.name = string3;
        this.type = typeReference;
        this.entityName = string5;
    }
    
    public TypeRewriteRule makeRule() {
        final OpticFinder<?> opticFinder2 = DSL.namedChoice(this.entityName, this.getInputSchema().getChoiceType(this.type, this.entityName));
        return this.fixTypeEverywhereTyped(this.name, this.getInputSchema().getType(this.type), this.getOutputSchema().getType(this.type), typed -> typed.updateTyped(opticFinder2, this.getOutputSchema().getChoiceType(this.type, this.entityName), this::fix));
    }
    
    protected abstract Typed<?> fix(final Typed<?> typed);
}
