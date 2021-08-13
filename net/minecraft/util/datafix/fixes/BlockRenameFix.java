package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public abstract class BlockRenameFix extends DataFix {
    private final String name;
    
    public BlockRenameFix(final Schema schema, final String string) {
        super(schema, false);
        this.name = string;
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.BLOCK_NAME);
        final Type<Pair<String, String>> type3 = (Type<Pair<String, String>>)DSL.named(References.BLOCK_NAME.typeName(), (Type)NamespacedSchema.namespacedString());
        if (!Objects.equals(type2, type3)) {
            throw new IllegalStateException("block type is not what was expected.");
        }
        final TypeRewriteRule typeRewriteRule4 = this.fixTypeEverywhere(this.name + " for block", (Type)type3, dynamicOps -> pair -> pair.mapSecond(this::fixBlock));
        final TypeRewriteRule typeRewriteRule5 = this.fixTypeEverywhereTyped(this.name + " for block_state", this.getInputSchema().getType(References.BLOCK_STATE), typed -> typed.update(DSL.remainderFinder(), dynamic -> {
            final Optional<String> optional3 = (Optional<String>)dynamic.get("Name").asString().result();
            if (optional3.isPresent()) {
                return dynamic.set("Name", dynamic.createString(this.fixBlock((String)optional3.get())));
            }
            return dynamic;
        }));
        return TypeRewriteRule.seq(typeRewriteRule4, typeRewriteRule5);
    }
    
    protected abstract String fixBlock(final String string);
    
    public static DataFix create(final Schema schema, final String string, final Function<String, String> function) {
        return new BlockRenameFix(schema, string) {
            @Override
            protected String fixBlock(final String string) {
                return (String)function.apply(string);
            }
        };
    }
}
