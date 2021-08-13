package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.function.Function;
import com.mojang.datafixers.DataFix;

public class RecipesRenameFix extends DataFix {
    private final String name;
    private final Function<String, String> renamer;
    
    public RecipesRenameFix(final Schema schema, final boolean boolean2, final String string, final Function<String, String> function) {
        super(schema, boolean2);
        this.name = string;
        this.renamer = function;
    }
    
    protected TypeRewriteRule makeRule() {
        final Type<Pair<String, String>> type2 = (Type<Pair<String, String>>)DSL.named(References.RECIPE.typeName(), (Type)NamespacedSchema.namespacedString());
        if (!Objects.equals(type2, this.getInputSchema().getType(References.RECIPE))) {
            throw new IllegalStateException("Recipe type is not what was expected.");
        }
        return this.fixTypeEverywhere(this.name, (Type)type2, dynamicOps -> pair -> pair.mapSecond((Function)this.renamer));
    }
}
