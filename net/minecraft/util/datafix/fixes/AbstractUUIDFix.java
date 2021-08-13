package net.minecraft.util.datafix.fixes;

import org.apache.logging.log4j.LogManager;
import java.util.UUID;
import java.util.Arrays;
import java.util.Optional;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.Dynamic;
import java.util.function.Function;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DSL;
import org.apache.logging.log4j.Logger;
import com.mojang.datafixers.DataFix;

public abstract class AbstractUUIDFix extends DataFix {
    protected static final Logger LOGGER;
    protected DSL.TypeReference typeReference;
    
    public AbstractUUIDFix(final Schema schema, final DSL.TypeReference typeReference) {
        super(schema, false);
        this.typeReference = typeReference;
    }
    
    protected Typed<?> updateNamedChoice(final Typed<?> typed, final String string, final Function<Dynamic<?>, Dynamic<?>> function) {
        final Type<?> type5 = this.getInputSchema().getChoiceType(this.typeReference, string);
        final Type<?> type6 = this.getOutputSchema().getChoiceType(this.typeReference, string);
        return typed.updateTyped(DSL.namedChoice(string, (Type)type5), (Type)type6, typed -> typed.update(DSL.remainderFinder(), function));
    }
    
    protected static Optional<Dynamic<?>> replaceUUIDString(final Dynamic<?> dynamic, final String string2, final String string3) {
        return (Optional<Dynamic<?>>)createUUIDFromString(dynamic, string2).map(dynamic4 -> dynamic.remove(string2).set(string3, dynamic4));
    }
    
    protected static Optional<Dynamic<?>> replaceUUIDMLTag(final Dynamic<?> dynamic, final String string2, final String string3) {
        return (Optional<Dynamic<?>>)dynamic.get(string2).result().flatMap(AbstractUUIDFix::createUUIDFromML).map(dynamic4 -> dynamic.remove(string2).set(string3, dynamic4));
    }
    
    protected static Optional<Dynamic<?>> replaceUUIDLeastMost(final Dynamic<?> dynamic, final String string2, final String string3) {
        final String string4 = string2 + "Most";
        final String string5 = string2 + "Least";
        return (Optional<Dynamic<?>>)createUUIDFromLongs(dynamic, string4, string5).map(dynamic5 -> dynamic.remove(string4).remove(string5).set(string3, dynamic5));
    }
    
    protected static Optional<Dynamic<?>> createUUIDFromString(final Dynamic<?> dynamic, final String string) {
        return (Optional<Dynamic<?>>)dynamic.get(string).result().flatMap(dynamic2 -> {
            final String string3 = dynamic2.asString((String)null);
            if (string3 != null) {
                try {
                    final UUID uUID4 = UUID.fromString(string3);
                    return createUUIDTag(dynamic, uUID4.getMostSignificantBits(), uUID4.getLeastSignificantBits());
                }
                catch (IllegalArgumentException ex) {}
            }
            return Optional.empty();
        });
    }
    
    protected static Optional<Dynamic<?>> createUUIDFromML(final Dynamic<?> dynamic) {
        return createUUIDFromLongs(dynamic, "M", "L");
    }
    
    protected static Optional<Dynamic<?>> createUUIDFromLongs(final Dynamic<?> dynamic, final String string2, final String string3) {
        final long long4 = dynamic.get(string2).asLong(0L);
        final long long5 = dynamic.get(string3).asLong(0L);
        if (long4 == 0L || long5 == 0L) {
            return (Optional<Dynamic<?>>)Optional.empty();
        }
        return createUUIDTag(dynamic, long4, long5);
    }
    
    protected static Optional<Dynamic<?>> createUUIDTag(final Dynamic<?> dynamic, final long long2, final long long3) {
        return (Optional<Dynamic<?>>)Optional.of(dynamic.createIntList(Arrays.stream(new int[] { (int)(long2 >> 32), (int)long2, (int)(long3 >> 32), (int)long3 })));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
