package net.minecraft.util;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import com.mojang.serialization.Keyable;
import java.util.Optional;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import com.mojang.serialization.Codec;
import java.util.function.Function;
import java.util.function.Supplier;

public interface StringRepresentable {
    String getSerializedName();
    
    default <E extends Enum> Codec<E> fromEnum(final Supplier<E[]> supplier, final Function<? super String, ? extends E> function) {
        final E[] arr3 = (E[])supplier.get();
        return StringRepresentable.<E>fromStringResolver((java.util.function.ToIntFunction<E>)Enum::ordinal, (java.util.function.IntFunction<E>)(integer -> arr3[integer]), function);
    }
    
    default <E extends StringRepresentable> Codec<E> fromStringResolver(final ToIntFunction<E> toIntFunction, final IntFunction<E> intFunction, final Function<? super String, ? extends E> function) {
        return (Codec<E>)new Codec<E>() {
            public <T> DataResult<T> encode(final E afp, final DynamicOps<T> dynamicOps, final T object) {
                if (dynamicOps.compressMaps()) {
                    return (DataResult<T>)dynamicOps.mergeToPrimitive(object, dynamicOps.createInt(toIntFunction.applyAsInt(afp)));
                }
                return (DataResult<T>)dynamicOps.mergeToPrimitive(object, dynamicOps.createString(afp.getSerializedName()));
            }
            
            public <T> DataResult<Pair<E, T>> decode(final DynamicOps<T> dynamicOps, final T object) {
                if (dynamicOps.compressMaps()) {
                    return (DataResult<Pair<E, T>>)dynamicOps.getNumberValue(object).flatMap(number -> (DataResult)Optional.ofNullable(intFunction.apply(number.intValue())).map(DataResult::success).orElseGet(() -> DataResult.error(new StringBuilder().append("Unknown element id: ").append(number).toString()))).map(afp -> Pair.of(afp, dynamicOps.empty()));
                }
                return (DataResult<Pair<E, T>>)dynamicOps.getStringValue(object).flatMap(string -> (DataResult)Optional.ofNullable(function.apply(string)).map(DataResult::success).orElseGet(() -> DataResult.error("Unknown element name: " + string))).map(afp -> Pair.of(afp, dynamicOps.empty()));
            }
            
            public String toString() {
                return new StringBuilder().append("StringRepresentable[").append(toIntFunction).append("]").toString();
            }
        };
    }
    
    default Keyable keys(final StringRepresentable[] arr) {
        return (Keyable)new Keyable() {
            public <T> Stream<T> keys(final DynamicOps<T> dynamicOps) {
                if (dynamicOps.compressMaps()) {
                    return (Stream<T>)IntStream.range(0, arr.length).mapToObj(dynamicOps::createInt);
                }
                return (Stream<T>)Arrays.stream((Object[])arr).map(StringRepresentable::getSerializedName).map(dynamicOps::createString);
            }
        };
    }
}
