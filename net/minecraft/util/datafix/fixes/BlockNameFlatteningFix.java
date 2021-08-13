package net.minecraft.util.datafix.fixes;

import java.util.function.Function;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import com.mojang.datafixers.types.Type;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class BlockNameFlatteningFix extends DataFix {
    public BlockNameFlatteningFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.BLOCK_NAME);
        final Type<?> type3 = this.getOutputSchema().getType(References.BLOCK_NAME);
        final Type<Pair<String, Either<Integer, String>>> type4 = (Type<Pair<String, Either<Integer, String>>>)DSL.named(References.BLOCK_NAME.typeName(), DSL.or(DSL.intType(), (Type)NamespacedSchema.namespacedString()));
        final Type<Pair<String, String>> type5 = (Type<Pair<String, String>>)DSL.named(References.BLOCK_NAME.typeName(), (Type)NamespacedSchema.namespacedString());
        if (!Objects.equals(type2, type4) || !Objects.equals(type3, type5)) {
            throw new IllegalStateException("Expected and actual types don't match.");
        }
        return this.fixTypeEverywhere("BlockNameFlatteningFix", (Type)type4, (Type)type5, dynamicOps -> pair -> pair.mapSecond(either -> (String)either.map(BlockStateData::upgradeBlock, string -> BlockStateData.upgradeBlock(NamespacedSchema.ensureNamespaced(string)))));
    }
}
