package net.minecraft.advancements.critereon;

import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.ImmutableStringReader;
import java.util.function.Function;
import com.mojang.brigadier.StringReader;
import javax.annotation.Nullable;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class WrappedMinMaxBounds {
    public static final WrappedMinMaxBounds ANY;
    public static final SimpleCommandExceptionType ERROR_INTS_ONLY;
    private final Float min;
    private final Float max;
    
    public WrappedMinMaxBounds(@Nullable final Float float1, @Nullable final Float float2) {
        this.min = float1;
        this.max = float2;
    }
    
    @Nullable
    public Float getMin() {
        return this.min;
    }
    
    @Nullable
    public Float getMax() {
        return this.max;
    }
    
    public static WrappedMinMaxBounds fromReader(final StringReader stringReader, final boolean boolean2, final Function<Float, Float> function) throws CommandSyntaxException {
        if (!stringReader.canRead()) {
            throw MinMaxBounds.ERROR_EMPTY.createWithContext((ImmutableStringReader)stringReader);
        }
        final int integer4 = stringReader.getCursor();
        final Float float5 = optionallyFormat(readNumber(stringReader, boolean2), function);
        Float float6;
        if (stringReader.canRead(2) && stringReader.peek() == '.' && stringReader.peek(1) == '.') {
            stringReader.skip();
            stringReader.skip();
            float6 = optionallyFormat(readNumber(stringReader, boolean2), function);
            if (float5 == null && float6 == null) {
                stringReader.setCursor(integer4);
                throw MinMaxBounds.ERROR_EMPTY.createWithContext((ImmutableStringReader)stringReader);
            }
        }
        else {
            if (!boolean2 && stringReader.canRead() && stringReader.peek() == '.') {
                stringReader.setCursor(integer4);
                throw WrappedMinMaxBounds.ERROR_INTS_ONLY.createWithContext((ImmutableStringReader)stringReader);
            }
            float6 = float5;
        }
        if (float5 == null && float6 == null) {
            stringReader.setCursor(integer4);
            throw MinMaxBounds.ERROR_EMPTY.createWithContext((ImmutableStringReader)stringReader);
        }
        return new WrappedMinMaxBounds(float5, float6);
    }
    
    @Nullable
    private static Float readNumber(final StringReader stringReader, final boolean boolean2) throws CommandSyntaxException {
        final int integer3 = stringReader.getCursor();
        while (stringReader.canRead() && isAllowedNumber(stringReader, boolean2)) {
            stringReader.skip();
        }
        final String string4 = stringReader.getString().substring(integer3, stringReader.getCursor());
        if (string4.isEmpty()) {
            return null;
        }
        try {
            return Float.parseFloat(string4);
        }
        catch (NumberFormatException numberFormatException5) {
            if (boolean2) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidDouble().createWithContext((ImmutableStringReader)stringReader, string4);
            }
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext((ImmutableStringReader)stringReader, string4);
        }
    }
    
    private static boolean isAllowedNumber(final StringReader stringReader, final boolean boolean2) {
        final char character3 = stringReader.peek();
        return (character3 >= '0' && character3 <= '9') || character3 == '-' || (boolean2 && character3 == '.' && (!stringReader.canRead(2) || stringReader.peek(1) != '.'));
    }
    
    @Nullable
    private static Float optionallyFormat(@Nullable final Float float1, final Function<Float, Float> function) {
        return (float1 == null) ? null : ((Float)function.apply(float1));
    }
    
    static {
        ANY = new WrappedMinMaxBounds(null, null);
        ERROR_INTS_ONLY = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.range.ints"));
    }
}
