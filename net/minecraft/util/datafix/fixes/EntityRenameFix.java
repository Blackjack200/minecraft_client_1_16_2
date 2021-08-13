package net.minecraft.util.datafix.fixes;

import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.types.templates.TaggedChoice;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public abstract class EntityRenameFix extends DataFix {
    protected final String name;
    
    public EntityRenameFix(final String string, final Schema schema, final boolean boolean3) {
        super(schema, boolean3);
        this.name = string;
    }
    
    public TypeRewriteRule makeRule() {
        final TaggedChoice.TaggedChoiceType<String> taggedChoiceType2 = (TaggedChoice.TaggedChoiceType<String>)this.getInputSchema().findChoiceType(References.ENTITY);
        final TaggedChoice.TaggedChoiceType<String> taggedChoiceType3 = (TaggedChoice.TaggedChoiceType<String>)this.getOutputSchema().findChoiceType(References.ENTITY);
        return this.fixTypeEverywhere(this.name, (Type)taggedChoiceType2, (Type)taggedChoiceType3, dynamicOps -> pair -> {
            final String string6 = (String)pair.getFirst();
            final Type<?> type7 = taggedChoiceType2.types().get(string6);
            final Pair<String, Typed<?>> pair2 = this.fix(string6, this.getEntity(pair.getSecond(), dynamicOps, type7));
            final Type<?> type8 = taggedChoiceType3.types().get(pair2.getFirst());
            if (!type8.equals(((Typed)pair2.getSecond()).getType(), true, true)) {
                throw new IllegalStateException(String.format("Dynamic type check failed: %s not equal to %s", new Object[] { type8, ((Typed)pair2.getSecond()).getType() }));
            }
            return Pair.of(pair2.getFirst(), ((Typed)pair2.getSecond()).getValue());
        });
    }
    
    private <A> Typed<A> getEntity(final Object object, final DynamicOps<?> dynamicOps, final Type<A> type) {
        return (Typed<A>)new Typed((Type)type, (DynamicOps)dynamicOps, object);
    }
    
    protected abstract Pair<String, Typed<?>> fix(final String string, final Typed<?> typed);
}
