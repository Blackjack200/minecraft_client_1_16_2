package net.minecraft.advancements.critereon;

import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.function.Supplier;
import java.util.function.Function;
import com.mojang.brigadier.StringReader;
import net.minecraft.util.GsonHelper;
import java.util.function.BiFunction;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonNull;
import com.google.gson.JsonElement;
import javax.annotation.Nullable;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public abstract class MinMaxBounds<T extends Number> {
    public static final SimpleCommandExceptionType ERROR_EMPTY;
    public static final SimpleCommandExceptionType ERROR_SWAPPED;
    protected final T min;
    protected final T max;
    
    protected MinMaxBounds(@Nullable final T number1, @Nullable final T number2) {
        this.min = number1;
        this.max = number2;
    }
    
    @Nullable
    public T getMin() {
        return this.min;
    }
    
    @Nullable
    public T getMax() {
        return this.max;
    }
    
    public boolean isAny() {
        return this.min == null && this.max == null;
    }
    
    public JsonElement serializeToJson() {
        if (this.isAny()) {
            return (JsonElement)JsonNull.INSTANCE;
        }
        if (this.min != null && this.min.equals(this.max)) {
            return (JsonElement)new JsonPrimitive((Number)this.min);
        }
        final JsonObject jsonObject2 = new JsonObject();
        if (this.min != null) {
            jsonObject2.addProperty("min", (Number)this.min);
        }
        if (this.max != null) {
            jsonObject2.addProperty("max", (Number)this.max);
        }
        return (JsonElement)jsonObject2;
    }
    
    protected static <T extends Number, R extends MinMaxBounds<T>> R fromJson(@Nullable final JsonElement jsonElement, final R bz, final BiFunction<JsonElement, String, T> biFunction, final BoundsFactory<T, R> a) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return bz;
        }
        if (GsonHelper.isNumberValue(jsonElement)) {
            final T number5 = (T)biFunction.apply(jsonElement, "value");
            return a.create(number5, number5);
        }
        final JsonObject jsonObject5 = GsonHelper.convertToJsonObject(jsonElement, "value");
        final T number6 = (T)(jsonObject5.has("min") ? ((Number)biFunction.apply(jsonObject5.get("min"), "min")) : null);
        final T number7 = (T)(jsonObject5.has("max") ? ((Number)biFunction.apply(jsonObject5.get("max"), "max")) : null);
        return a.create(number6, number7);
    }
    
    protected static <T extends Number, R extends MinMaxBounds<T>> R fromReader(final StringReader stringReader, final BoundsFromReaderFactory<T, R> b, final Function<String, T> function3, final Supplier<DynamicCommandExceptionType> supplier, final Function<T, T> function5) throws CommandSyntaxException {
        if (!stringReader.canRead()) {
            throw MinMaxBounds.ERROR_EMPTY.createWithContext((ImmutableStringReader)stringReader);
        }
        final int integer6 = stringReader.getCursor();
        try {
            final T number7 = MinMaxBounds.<T>optionallyFormat((T)MinMaxBounds.<T>readNumber(stringReader, (java.util.function.Function<String, T>)function3, supplier), function5);
            T number8;
            if (stringReader.canRead(2) && stringReader.peek() == '.' && stringReader.peek(1) == '.') {
                stringReader.skip();
                stringReader.skip();
                number8 = MinMaxBounds.<T>optionallyFormat((T)MinMaxBounds.<T>readNumber(stringReader, (java.util.function.Function<String, T>)function3, supplier), function5);
                if (number7 == null && number8 == null) {
                    throw MinMaxBounds.ERROR_EMPTY.createWithContext((ImmutableStringReader)stringReader);
                }
            }
            else {
                number8 = number7;
            }
            if (number7 == null && number8 == null) {
                throw MinMaxBounds.ERROR_EMPTY.createWithContext((ImmutableStringReader)stringReader);
            }
            return b.create(stringReader, number7, number8);
        }
        catch (CommandSyntaxException commandSyntaxException7) {
            stringReader.setCursor(integer6);
            throw new CommandSyntaxException(commandSyntaxException7.getType(), commandSyntaxException7.getRawMessage(), commandSyntaxException7.getInput(), integer6);
        }
    }
    
    @Nullable
    private static <T extends Number> T readNumber(final StringReader stringReader, final Function<String, T> function, final Supplier<DynamicCommandExceptionType> supplier) throws CommandSyntaxException {
        final int integer4 = stringReader.getCursor();
        while (stringReader.canRead() && isAllowedInputChat(stringReader)) {
            stringReader.skip();
        }
        final String string5 = stringReader.getString().substring(integer4, stringReader.getCursor());
        if (string5.isEmpty()) {
            return null;
        }
        try {
            return (T)function.apply(string5);
        }
        catch (NumberFormatException numberFormatException6) {
            throw ((DynamicCommandExceptionType)supplier.get()).createWithContext((ImmutableStringReader)stringReader, string5);
        }
    }
    
    private static boolean isAllowedInputChat(final StringReader stringReader) {
        final char character2 = stringReader.peek();
        return (character2 >= '0' && character2 <= '9') || character2 == '-' || (character2 == '.' && (!stringReader.canRead(2) || stringReader.peek(1) != '.'));
    }
    
    @Nullable
    private static <T> T optionallyFormat(@Nullable final T object, final Function<T, T> function) {
        return (T)((object == null) ? null : function.apply(object));
    }
    
    static {
        ERROR_EMPTY = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.range.empty"));
        ERROR_SWAPPED = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.range.swapped"));
    }
    
    public static class Ints extends MinMaxBounds<Integer> {
        public static final Ints ANY;
        private final Long minSq;
        private final Long maxSq;
        
        private static Ints create(final StringReader stringReader, @Nullable final Integer integer2, @Nullable final Integer integer3) throws CommandSyntaxException {
            if (integer2 != null && integer3 != null && integer2 > integer3) {
                throw Ints.ERROR_SWAPPED.createWithContext((ImmutableStringReader)stringReader);
            }
            return new Ints(integer2, integer3);
        }
        
        @Nullable
        private static Long squareOpt(@Nullable final Integer integer) {
            return (integer == null) ? null : Long.valueOf(integer * (long)integer);
        }
        
        private Ints(@Nullable final Integer integer1, @Nullable final Integer integer2) {
            super(integer1, integer2);
            this.minSq = squareOpt(integer1);
            this.maxSq = squareOpt(integer2);
        }
        
        public static Ints exactly(final int integer) {
            return new Ints(integer, integer);
        }
        
        public static Ints atLeast(final int integer) {
            return new Ints(integer, null);
        }
        
        public boolean matches(final int integer) {
            return (this.min == null || (int)this.min <= integer) && (this.max == null || (int)this.max >= integer);
        }
        
        public static Ints fromJson(@Nullable final JsonElement jsonElement) {
            return MinMaxBounds.<Integer, Ints>fromJson(jsonElement, Ints.ANY, (java.util.function.BiFunction<JsonElement, String, Integer>)GsonHelper::convertToInt, Ints::new);
        }
        
        public static Ints fromReader(final StringReader stringReader) throws CommandSyntaxException {
            return fromReader(stringReader, (Function<Integer, Integer>)(integer -> integer));
        }
        
        public static Ints fromReader(final StringReader stringReader, final Function<Integer, Integer> function) throws CommandSyntaxException {
            return MinMaxBounds.<Integer, Ints>fromReader(stringReader, Ints::create, (java.util.function.Function<String, Integer>)Integer::parseInt, (Supplier<DynamicCommandExceptionType>)CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidInt, function);
        }
        
        static {
            ANY = new Ints(null, null);
        }
    }
    
    public static class Floats extends MinMaxBounds<Float> {
        public static final Floats ANY;
        private final Double minSq;
        private final Double maxSq;
        
        private static Floats create(final StringReader stringReader, @Nullable final Float float2, @Nullable final Float float3) throws CommandSyntaxException {
            if (float2 != null && float3 != null && float2 > float3) {
                throw Floats.ERROR_SWAPPED.createWithContext((ImmutableStringReader)stringReader);
            }
            return new Floats(float2, float3);
        }
        
        @Nullable
        private static Double squareOpt(@Nullable final Float float1) {
            return (float1 == null) ? null : Double.valueOf(float1 * (double)float1);
        }
        
        private Floats(@Nullable final Float float1, @Nullable final Float float2) {
            super(float1, float2);
            this.minSq = squareOpt(float1);
            this.maxSq = squareOpt(float2);
        }
        
        public static Floats atLeast(final float float1) {
            return new Floats(float1, null);
        }
        
        public boolean matches(final float float1) {
            return (this.min == null || (float)this.min <= float1) && (this.max == null || (float)this.max >= float1);
        }
        
        public boolean matchesSqr(final double double1) {
            return (this.minSq == null || this.minSq <= double1) && (this.maxSq == null || this.maxSq >= double1);
        }
        
        public static Floats fromJson(@Nullable final JsonElement jsonElement) {
            return MinMaxBounds.<Float, Floats>fromJson(jsonElement, Floats.ANY, (java.util.function.BiFunction<JsonElement, String, Float>)GsonHelper::convertToFloat, Floats::new);
        }
        
        public static Floats fromReader(final StringReader stringReader) throws CommandSyntaxException {
            return fromReader(stringReader, (Function<Float, Float>)(float1 -> float1));
        }
        
        public static Floats fromReader(final StringReader stringReader, final Function<Float, Float> function) throws CommandSyntaxException {
            return MinMaxBounds.<Float, Floats>fromReader(stringReader, Floats::create, (java.util.function.Function<String, Float>)Float::parseFloat, (Supplier<DynamicCommandExceptionType>)CommandSyntaxException.BUILT_IN_EXCEPTIONS::readerInvalidFloat, function);
        }
        
        static {
            ANY = new Floats(null, null);
        }
    }
    
    @FunctionalInterface
    public interface BoundsFromReaderFactory<T extends Number, R extends MinMaxBounds<T>> {
        R create(final StringReader stringReader, @Nullable final T number2, @Nullable final T number3) throws CommandSyntaxException;
    }
    
    @FunctionalInterface
    public interface BoundsFactory<T extends Number, R extends MinMaxBounds<T>> {
        R create(@Nullable final T number1, @Nullable final T number2);
    }
}
