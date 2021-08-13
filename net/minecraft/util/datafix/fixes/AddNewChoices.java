package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.util.Pair;
import java.util.function.Function;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;

public class AddNewChoices extends DataFix {
    private final String name;
    private final DSL.TypeReference type;
    
    public AddNewChoices(final Schema schema, final String string, final DSL.TypeReference typeReference) {
        super(schema, true);
        this.name = string;
        this.type = typeReference;
    }
    
    public TypeRewriteRule makeRule() {
        final TaggedChoice.TaggedChoiceType<?> taggedChoiceType2 = this.getInputSchema().findChoiceType(this.type);
        final TaggedChoice.TaggedChoiceType<?> taggedChoiceType3 = this.getOutputSchema().findChoiceType(this.type);
        return this.cap(this.name, taggedChoiceType2, taggedChoiceType3);
    }
    
    protected final <K> TypeRewriteRule cap(final String string, final TaggedChoice.TaggedChoiceType<K> taggedChoiceType2, final TaggedChoice.TaggedChoiceType<?> taggedChoiceType3) {
        if (taggedChoiceType2.getKeyType() != taggedChoiceType3.getKeyType()) {
            throw new IllegalStateException("Could not inject: key type is not the same");
        }
        final TaggedChoice.TaggedChoiceType<K> taggedChoiceType4 = (TaggedChoice.TaggedChoiceType<K>)taggedChoiceType3;
        return this.fixTypeEverywhere(string, (Type)taggedChoiceType2, (Type)taggedChoiceType4, dynamicOps -> pair -> {
            if (!taggedChoiceType4.hasType(pair.getFirst())) {
                throw new IllegalArgumentException(String.format("Unknown type %s in %s ", new Object[] { pair.getFirst(), this.type }));
            }
            return pair;
        });
    }
}
