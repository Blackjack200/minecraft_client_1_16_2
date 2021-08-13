package net.minecraft.nbt;

import org.apache.logging.log4j.LogManager;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Dynamic;
import net.minecraft.network.chat.MutableComponent;
import com.google.common.base.Strings;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import java.io.DataInput;
import java.util.Objects;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import java.util.Collection;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.ReportedException;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import java.util.Set;
import java.io.IOException;
import java.util.Iterator;
import java.io.DataOutput;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import com.mojang.serialization.Codec;

public class CompoundTag implements Tag {
    public static final Codec<CompoundTag> CODEC;
    private static final Logger LOGGER;
    private static final Pattern SIMPLE_VALUE;
    public static final TagType<CompoundTag> TYPE;
    private final Map<String, Tag> tags;
    
    protected CompoundTag(final Map<String, Tag> map) {
        this.tags = map;
    }
    
    public CompoundTag() {
        this((Map<String, Tag>)Maps.newHashMap());
    }
    
    public void write(final DataOutput dataOutput) throws IOException {
        for (final String string4 : this.tags.keySet()) {
            final Tag mt5 = (Tag)this.tags.get(string4);
            writeNamedTag(string4, mt5, dataOutput);
        }
        dataOutput.writeByte(0);
    }
    
    public Set<String> getAllKeys() {
        return (Set<String>)this.tags.keySet();
    }
    
    public byte getId() {
        return 10;
    }
    
    public TagType<CompoundTag> getType() {
        return CompoundTag.TYPE;
    }
    
    public int size() {
        return this.tags.size();
    }
    
    @Nullable
    public Tag put(final String string, final Tag mt) {
        return (Tag)this.tags.put(string, mt);
    }
    
    public void putByte(final String string, final byte byte2) {
        this.tags.put(string, ByteTag.valueOf(byte2));
    }
    
    public void putShort(final String string, final short short2) {
        this.tags.put(string, ShortTag.valueOf(short2));
    }
    
    public void putInt(final String string, final int integer) {
        this.tags.put(string, IntTag.valueOf(integer));
    }
    
    public void putLong(final String string, final long long2) {
        this.tags.put(string, LongTag.valueOf(long2));
    }
    
    public void putUUID(final String string, final UUID uUID) {
        this.tags.put(string, NbtUtils.createUUID(uUID));
    }
    
    public UUID getUUID(final String string) {
        return NbtUtils.loadUUID(this.get(string));
    }
    
    public boolean hasUUID(final String string) {
        final Tag mt3 = this.get(string);
        return mt3 != null && mt3.getType() == IntArrayTag.TYPE && ((IntArrayTag)mt3).getAsIntArray().length == 4;
    }
    
    public void putFloat(final String string, final float float2) {
        this.tags.put(string, FloatTag.valueOf(float2));
    }
    
    public void putDouble(final String string, final double double2) {
        this.tags.put(string, DoubleTag.valueOf(double2));
    }
    
    public void putString(final String string1, final String string2) {
        this.tags.put(string1, StringTag.valueOf(string2));
    }
    
    public void putByteArray(final String string, final byte[] arr) {
        this.tags.put(string, new ByteArrayTag(arr));
    }
    
    public void putIntArray(final String string, final int[] arr) {
        this.tags.put(string, new IntArrayTag(arr));
    }
    
    public void putIntArray(final String string, final List<Integer> list) {
        this.tags.put(string, new IntArrayTag(list));
    }
    
    public void putLongArray(final String string, final long[] arr) {
        this.tags.put(string, new LongArrayTag(arr));
    }
    
    public void putLongArray(final String string, final List<Long> list) {
        this.tags.put(string, new LongArrayTag(list));
    }
    
    public void putBoolean(final String string, final boolean boolean2) {
        this.tags.put(string, ByteTag.valueOf(boolean2));
    }
    
    @Nullable
    public Tag get(final String string) {
        return (Tag)this.tags.get(string);
    }
    
    public byte getTagType(final String string) {
        final Tag mt3 = (Tag)this.tags.get(string);
        if (mt3 == null) {
            return 0;
        }
        return mt3.getId();
    }
    
    public boolean contains(final String string) {
        return this.tags.containsKey(string);
    }
    
    public boolean contains(final String string, final int integer) {
        final int integer2 = this.getTagType(string);
        return integer2 == integer || (integer == 99 && (integer2 == 1 || integer2 == 2 || integer2 == 3 || integer2 == 4 || integer2 == 5 || integer2 == 6));
    }
    
    public byte getByte(final String string) {
        try {
            if (this.contains(string, 99)) {
                return ((NumericTag)this.tags.get(string)).getAsByte();
            }
        }
        catch (ClassCastException ex) {}
        return 0;
    }
    
    public short getShort(final String string) {
        try {
            if (this.contains(string, 99)) {
                return ((NumericTag)this.tags.get(string)).getAsShort();
            }
        }
        catch (ClassCastException ex) {}
        return 0;
    }
    
    public int getInt(final String string) {
        try {
            if (this.contains(string, 99)) {
                return ((NumericTag)this.tags.get(string)).getAsInt();
            }
        }
        catch (ClassCastException ex) {}
        return 0;
    }
    
    public long getLong(final String string) {
        try {
            if (this.contains(string, 99)) {
                return ((NumericTag)this.tags.get(string)).getAsLong();
            }
        }
        catch (ClassCastException ex) {}
        return 0L;
    }
    
    public float getFloat(final String string) {
        try {
            if (this.contains(string, 99)) {
                return ((NumericTag)this.tags.get(string)).getAsFloat();
            }
        }
        catch (ClassCastException ex) {}
        return 0.0f;
    }
    
    public double getDouble(final String string) {
        try {
            if (this.contains(string, 99)) {
                return ((NumericTag)this.tags.get(string)).getAsDouble();
            }
        }
        catch (ClassCastException ex) {}
        return 0.0;
    }
    
    public String getString(final String string) {
        try {
            if (this.contains(string, 8)) {
                return ((Tag)this.tags.get(string)).getAsString();
            }
        }
        catch (ClassCastException ex) {}
        return "";
    }
    
    public byte[] getByteArray(final String string) {
        try {
            if (this.contains(string, 7)) {
                return ((ByteArrayTag)this.tags.get(string)).getAsByteArray();
            }
        }
        catch (ClassCastException classCastException3) {
            throw new ReportedException(this.createReport(string, ByteArrayTag.TYPE, classCastException3));
        }
        return new byte[0];
    }
    
    public int[] getIntArray(final String string) {
        try {
            if (this.contains(string, 11)) {
                return ((IntArrayTag)this.tags.get(string)).getAsIntArray();
            }
        }
        catch (ClassCastException classCastException3) {
            throw new ReportedException(this.createReport(string, IntArrayTag.TYPE, classCastException3));
        }
        return new int[0];
    }
    
    public long[] getLongArray(final String string) {
        try {
            if (this.contains(string, 12)) {
                return ((LongArrayTag)this.tags.get(string)).getAsLongArray();
            }
        }
        catch (ClassCastException classCastException3) {
            throw new ReportedException(this.createReport(string, LongArrayTag.TYPE, classCastException3));
        }
        return new long[0];
    }
    
    public CompoundTag getCompound(final String string) {
        try {
            if (this.contains(string, 10)) {
                return (CompoundTag)this.tags.get(string);
            }
        }
        catch (ClassCastException classCastException3) {
            throw new ReportedException(this.createReport(string, CompoundTag.TYPE, classCastException3));
        }
        return new CompoundTag();
    }
    
    public ListTag getList(final String string, final int integer) {
        try {
            if (this.getTagType(string) == 9) {
                final ListTag mj4 = (ListTag)this.tags.get(string);
                if (mj4.isEmpty() || mj4.getElementType() == integer) {
                    return mj4;
                }
                return new ListTag();
            }
        }
        catch (ClassCastException classCastException4) {
            throw new ReportedException(this.createReport(string, ListTag.TYPE, classCastException4));
        }
        return new ListTag();
    }
    
    public boolean getBoolean(final String string) {
        return this.getByte(string) != 0;
    }
    
    public void remove(final String string) {
        this.tags.remove(string);
    }
    
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder("{");
        Collection<String> collection3 = (Collection<String>)this.tags.keySet();
        if (CompoundTag.LOGGER.isDebugEnabled()) {
            final List<String> list4 = (List<String>)Lists.newArrayList((Iterable)this.tags.keySet());
            Collections.sort((List)list4);
            collection3 = (Collection<String>)list4;
        }
        for (final String string5 : collection3) {
            if (stringBuilder2.length() != 1) {
                stringBuilder2.append(',');
            }
            stringBuilder2.append(handleEscape(string5)).append(':').append(this.tags.get(string5));
        }
        return stringBuilder2.append('}').toString();
    }
    
    public boolean isEmpty() {
        return this.tags.isEmpty();
    }
    
    private CrashReport createReport(final String string, final TagType<?> mv, final ClassCastException classCastException) {
        final CrashReport l5 = CrashReport.forThrowable((Throwable)classCastException, "Reading NBT data");
        final CrashReportCategory m6 = l5.addCategory("Corrupt NBT tag", 1);
        m6.setDetail("Tag type found", (CrashReportDetail<String>)(() -> ((Tag)this.tags.get(string)).getType().getName()));
        m6.setDetail("Tag type expected", (CrashReportDetail<String>)mv::getName);
        m6.setDetail("Tag name", string);
        return l5;
    }
    
    public CompoundTag copy() {
        final Map<String, Tag> map2 = (Map<String, Tag>)Maps.newHashMap(Maps.transformValues((Map)this.tags, Tag::copy));
        return new CompoundTag(map2);
    }
    
    public boolean equals(final Object object) {
        return this == object || (object instanceof CompoundTag && Objects.equals(this.tags, ((CompoundTag)object).tags));
    }
    
    public int hashCode() {
        return this.tags.hashCode();
    }
    
    private static void writeNamedTag(final String string, final Tag mt, final DataOutput dataOutput) throws IOException {
        dataOutput.writeByte((int)mt.getId());
        if (mt.getId() == 0) {
            return;
        }
        dataOutput.writeUTF(string);
        mt.write(dataOutput);
    }
    
    private static byte readNamedTagType(final DataInput dataInput, final NbtAccounter mm) throws IOException {
        return dataInput.readByte();
    }
    
    private static String readNamedTagName(final DataInput dataInput, final NbtAccounter mm) throws IOException {
        return dataInput.readUTF();
    }
    
    private static Tag readNamedTagData(final TagType<?> mv, final String string, final DataInput dataInput, final int integer, final NbtAccounter mm) {
        try {
            return (Tag)mv.load(dataInput, integer, mm);
        }
        catch (IOException iOException6) {
            final CrashReport l7 = CrashReport.forThrowable((Throwable)iOException6, "Loading NBT data");
            final CrashReportCategory m8 = l7.addCategory("NBT Tag");
            m8.setDetail("Tag name", string);
            m8.setDetail("Tag type", mv.getName());
            throw new ReportedException(l7);
        }
    }
    
    public CompoundTag merge(final CompoundTag md) {
        for (final String string4 : md.tags.keySet()) {
            final Tag mt5 = (Tag)md.tags.get(string4);
            if (mt5.getId() == 10) {
                if (this.contains(string4, 10)) {
                    final CompoundTag md2 = this.getCompound(string4);
                    md2.merge((CompoundTag)mt5);
                }
                else {
                    this.put(string4, mt5.copy());
                }
            }
            else {
                this.put(string4, mt5.copy());
            }
        }
        return this;
    }
    
    protected static String handleEscape(final String string) {
        if (CompoundTag.SIMPLE_VALUE.matcher((CharSequence)string).matches()) {
            return string;
        }
        return StringTag.quoteAndEscape(string);
    }
    
    protected static Component handleEscapePretty(final String string) {
        if (CompoundTag.SIMPLE_VALUE.matcher((CharSequence)string).matches()) {
            return new TextComponent(string).withStyle(CompoundTag.SYNTAX_HIGHLIGHTING_KEY);
        }
        final String string2 = StringTag.quoteAndEscape(string);
        final String string3 = string2.substring(0, 1);
        final Component nr4 = new TextComponent(string2.substring(1, string2.length() - 1)).withStyle(CompoundTag.SYNTAX_HIGHLIGHTING_KEY);
        return new TextComponent(string3).append(nr4).append(string3);
    }
    
    public Component getPrettyDisplay(final String string, final int integer) {
        if (this.tags.isEmpty()) {
            return new TextComponent("{}");
        }
        final MutableComponent nx4 = new TextComponent("{");
        Collection<String> collection5 = (Collection<String>)this.tags.keySet();
        if (CompoundTag.LOGGER.isDebugEnabled()) {
            final List<String> list6 = (List<String>)Lists.newArrayList((Iterable)this.tags.keySet());
            Collections.sort((List)list6);
            collection5 = (Collection<String>)list6;
        }
        if (!string.isEmpty()) {
            nx4.append("\n");
        }
        final Iterator<String> iterator6 = (Iterator<String>)collection5.iterator();
        while (iterator6.hasNext()) {
            final String string2 = (String)iterator6.next();
            final MutableComponent nx5 = new TextComponent(Strings.repeat(string, integer + 1)).append(handleEscapePretty(string2)).append(String.valueOf(':')).append(" ").append(((Tag)this.tags.get(string2)).getPrettyDisplay(string, integer + 1));
            if (iterator6.hasNext()) {
                nx5.append(String.valueOf(',')).append(string.isEmpty() ? " " : "\n");
            }
            nx4.append(nx5);
        }
        if (!string.isEmpty()) {
            nx4.append("\n").append(Strings.repeat(string, integer));
        }
        nx4.append("}");
        return nx4;
    }
    
    protected Map<String, Tag> entries() {
        return (Map<String, Tag>)Collections.unmodifiableMap((Map)this.tags);
    }
    
    static {
        CODEC = Codec.PASSTHROUGH.comapFlatMap(dynamic -> {
            final Tag mt2 = (Tag)dynamic.convert((DynamicOps)NbtOps.INSTANCE).getValue();
            if (mt2 instanceof CompoundTag) {
                return DataResult.success(mt2);
            }
            return DataResult.error(new StringBuilder().append("Not a compound tag: ").append(mt2).toString());
        }, md -> new Dynamic((DynamicOps)NbtOps.INSTANCE, md));
        LOGGER = LogManager.getLogger();
        SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
        TYPE = new TagType<CompoundTag>() {
            public CompoundTag load(final DataInput dataInput, final int integer, final NbtAccounter mm) throws IOException {
                mm.accountBits(384L);
                if (integer > 512) {
                    throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
                }
                final Map<String, Tag> map5 = (Map<String, Tag>)Maps.newHashMap();
                byte byte6;
                while ((byte6 = readNamedTagType(dataInput, mm)) != 0) {
                    final String string7 = readNamedTagName(dataInput, mm);
                    mm.accountBits(224 + 16 * string7.length());
                    final Tag mt8 = readNamedTagData(TagTypes.getType(byte6), string7, dataInput, integer + 1, mm);
                    if (map5.put(string7, mt8) != null) {
                        mm.accountBits(288L);
                    }
                }
                return new CompoundTag(map5);
            }
            
            public String getName() {
                return "COMPOUND";
            }
            
            public String getPrettyName() {
                return "TAG_Compound";
            }
        };
    }
}
