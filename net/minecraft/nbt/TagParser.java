package net.minecraft.nbt;

import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import com.google.common.collect.Lists;
import java.util.List;
import com.google.common.annotations.VisibleForTesting;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import java.util.regex.Pattern;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class TagParser {
    public static final SimpleCommandExceptionType ERROR_TRAILING_DATA;
    public static final SimpleCommandExceptionType ERROR_EXPECTED_KEY;
    public static final SimpleCommandExceptionType ERROR_EXPECTED_VALUE;
    public static final Dynamic2CommandExceptionType ERROR_INSERT_MIXED_LIST;
    public static final Dynamic2CommandExceptionType ERROR_INSERT_MIXED_ARRAY;
    public static final DynamicCommandExceptionType ERROR_INVALID_ARRAY;
    private static final Pattern DOUBLE_PATTERN_NOSUFFIX;
    private static final Pattern DOUBLE_PATTERN;
    private static final Pattern FLOAT_PATTERN;
    private static final Pattern BYTE_PATTERN;
    private static final Pattern LONG_PATTERN;
    private static final Pattern SHORT_PATTERN;
    private static final Pattern INT_PATTERN;
    private final StringReader reader;
    
    public static CompoundTag parseTag(final String string) throws CommandSyntaxException {
        return new TagParser(new StringReader(string)).readSingleStruct();
    }
    
    @VisibleForTesting
    CompoundTag readSingleStruct() throws CommandSyntaxException {
        final CompoundTag md2 = this.readStruct();
        this.reader.skipWhitespace();
        if (this.reader.canRead()) {
            throw TagParser.ERROR_TRAILING_DATA.createWithContext((ImmutableStringReader)this.reader);
        }
        return md2;
    }
    
    public TagParser(final StringReader stringReader) {
        this.reader = stringReader;
    }
    
    protected String readKey() throws CommandSyntaxException {
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw TagParser.ERROR_EXPECTED_KEY.createWithContext((ImmutableStringReader)this.reader);
        }
        return this.reader.readString();
    }
    
    protected Tag readTypedValue() throws CommandSyntaxException {
        this.reader.skipWhitespace();
        final int integer2 = this.reader.getCursor();
        if (StringReader.isQuotedStringStart(this.reader.peek())) {
            return StringTag.valueOf(this.reader.readQuotedString());
        }
        final String string3 = this.reader.readUnquotedString();
        if (string3.isEmpty()) {
            this.reader.setCursor(integer2);
            throw TagParser.ERROR_EXPECTED_VALUE.createWithContext((ImmutableStringReader)this.reader);
        }
        return this.type(string3);
    }
    
    private Tag type(final String string) {
        try {
            if (TagParser.FLOAT_PATTERN.matcher((CharSequence)string).matches()) {
                return FloatTag.valueOf(Float.parseFloat(string.substring(0, string.length() - 1)));
            }
            if (TagParser.BYTE_PATTERN.matcher((CharSequence)string).matches()) {
                return ByteTag.valueOf(Byte.parseByte(string.substring(0, string.length() - 1)));
            }
            if (TagParser.LONG_PATTERN.matcher((CharSequence)string).matches()) {
                return LongTag.valueOf(Long.parseLong(string.substring(0, string.length() - 1)));
            }
            if (TagParser.SHORT_PATTERN.matcher((CharSequence)string).matches()) {
                return ShortTag.valueOf(Short.parseShort(string.substring(0, string.length() - 1)));
            }
            if (TagParser.INT_PATTERN.matcher((CharSequence)string).matches()) {
                return IntTag.valueOf(Integer.parseInt(string));
            }
            if (TagParser.DOUBLE_PATTERN.matcher((CharSequence)string).matches()) {
                return DoubleTag.valueOf(Double.parseDouble(string.substring(0, string.length() - 1)));
            }
            if (TagParser.DOUBLE_PATTERN_NOSUFFIX.matcher((CharSequence)string).matches()) {
                return DoubleTag.valueOf(Double.parseDouble(string));
            }
            if ("true".equalsIgnoreCase(string)) {
                return ByteTag.ONE;
            }
            if ("false".equalsIgnoreCase(string)) {
                return ByteTag.ZERO;
            }
        }
        catch (NumberFormatException ex) {}
        return StringTag.valueOf(string);
    }
    
    public Tag readValue() throws CommandSyntaxException {
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw TagParser.ERROR_EXPECTED_VALUE.createWithContext((ImmutableStringReader)this.reader);
        }
        final char character2 = this.reader.peek();
        if (character2 == '{') {
            return this.readStruct();
        }
        if (character2 == '[') {
            return this.readList();
        }
        return this.readTypedValue();
    }
    
    protected Tag readList() throws CommandSyntaxException {
        if (this.reader.canRead(3) && !StringReader.isQuotedStringStart(this.reader.peek(1)) && this.reader.peek(2) == ';') {
            return this.readArrayTag();
        }
        return this.readListTag();
    }
    
    public CompoundTag readStruct() throws CommandSyntaxException {
        this.expect('{');
        final CompoundTag md2 = new CompoundTag();
        this.reader.skipWhitespace();
        while (this.reader.canRead() && this.reader.peek() != '}') {
            final int integer3 = this.reader.getCursor();
            final String string4 = this.readKey();
            if (string4.isEmpty()) {
                this.reader.setCursor(integer3);
                throw TagParser.ERROR_EXPECTED_KEY.createWithContext((ImmutableStringReader)this.reader);
            }
            this.expect(':');
            md2.put(string4, this.readValue());
            if (!this.hasElementSeparator()) {
                break;
            }
            if (!this.reader.canRead()) {
                throw TagParser.ERROR_EXPECTED_KEY.createWithContext((ImmutableStringReader)this.reader);
            }
        }
        this.expect('}');
        return md2;
    }
    
    private Tag readListTag() throws CommandSyntaxException {
        this.expect('[');
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw TagParser.ERROR_EXPECTED_VALUE.createWithContext((ImmutableStringReader)this.reader);
        }
        final ListTag mj2 = new ListTag();
        TagType<?> mv3 = null;
        while (this.reader.peek() != ']') {
            final int integer4 = this.reader.getCursor();
            final Tag mt5 = this.readValue();
            final TagType<?> mv4 = mt5.getType();
            if (mv3 == null) {
                mv3 = mv4;
            }
            else if (mv4 != mv3) {
                this.reader.setCursor(integer4);
                throw TagParser.ERROR_INSERT_MIXED_LIST.createWithContext((ImmutableStringReader)this.reader, mv4.getPrettyName(), mv3.getPrettyName());
            }
            mj2.add(mt5);
            if (!this.hasElementSeparator()) {
                break;
            }
            if (!this.reader.canRead()) {
                throw TagParser.ERROR_EXPECTED_VALUE.createWithContext((ImmutableStringReader)this.reader);
            }
        }
        this.expect(']');
        return mj2;
    }
    
    private Tag readArrayTag() throws CommandSyntaxException {
        this.expect('[');
        final int integer2 = this.reader.getCursor();
        final char character3 = this.reader.read();
        this.reader.read();
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw TagParser.ERROR_EXPECTED_VALUE.createWithContext((ImmutableStringReader)this.reader);
        }
        if (character3 == 'B') {
            return new ByteArrayTag(this.<Byte>readArray(ByteArrayTag.TYPE, ByteTag.TYPE));
        }
        if (character3 == 'L') {
            return new LongArrayTag(this.<Long>readArray(LongArrayTag.TYPE, LongTag.TYPE));
        }
        if (character3 == 'I') {
            return new IntArrayTag(this.<Integer>readArray(IntArrayTag.TYPE, IntTag.TYPE));
        }
        this.reader.setCursor(integer2);
        throw TagParser.ERROR_INVALID_ARRAY.createWithContext((ImmutableStringReader)this.reader, String.valueOf(character3));
    }
    
    private <T extends Number> List<T> readArray(final TagType<?> mv1, final TagType<?> mv2) throws CommandSyntaxException {
        final List<T> list4 = (List<T>)Lists.newArrayList();
        while (this.reader.peek() != ']') {
            final int integer5 = this.reader.getCursor();
            final Tag mt6 = this.readValue();
            final TagType<?> mv3 = mt6.getType();
            if (mv3 != mv2) {
                this.reader.setCursor(integer5);
                throw TagParser.ERROR_INSERT_MIXED_ARRAY.createWithContext((ImmutableStringReader)this.reader, mv3.getPrettyName(), mv1.getPrettyName());
            }
            if (mv2 == ByteTag.TYPE) {
                list4.add(((NumericTag)mt6).getAsByte());
            }
            else if (mv2 == LongTag.TYPE) {
                list4.add(((NumericTag)mt6).getAsLong());
            }
            else {
                list4.add(((NumericTag)mt6).getAsInt());
            }
            if (!this.hasElementSeparator()) {
                break;
            }
            if (!this.reader.canRead()) {
                throw TagParser.ERROR_EXPECTED_VALUE.createWithContext((ImmutableStringReader)this.reader);
            }
        }
        this.expect(']');
        return list4;
    }
    
    private boolean hasElementSeparator() {
        this.reader.skipWhitespace();
        if (this.reader.canRead() && this.reader.peek() == ',') {
            this.reader.skip();
            this.reader.skipWhitespace();
            return true;
        }
        return false;
    }
    
    private void expect(final char character) throws CommandSyntaxException {
        this.reader.skipWhitespace();
        this.reader.expect(character);
    }
    
    static {
        ERROR_TRAILING_DATA = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.nbt.trailing"));
        ERROR_EXPECTED_KEY = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.nbt.expected.key"));
        ERROR_EXPECTED_VALUE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.nbt.expected.value"));
        ERROR_INSERT_MIXED_LIST = new Dynamic2CommandExceptionType((object1, object2) -> new TranslatableComponent("argument.nbt.list.mixed", new Object[] { object1, object2 }));
        ERROR_INSERT_MIXED_ARRAY = new Dynamic2CommandExceptionType((object1, object2) -> new TranslatableComponent("argument.nbt.array.mixed", new Object[] { object1, object2 }));
        ERROR_INVALID_ARRAY = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.nbt.array.invalid", new Object[] { object }));
        DOUBLE_PATTERN_NOSUFFIX = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", 2);
        DOUBLE_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", 2);
        FLOAT_PATTERN = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", 2);
        BYTE_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", 2);
        LONG_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", 2);
        SHORT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", 2);
        INT_PATTERN = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");
    }
}
