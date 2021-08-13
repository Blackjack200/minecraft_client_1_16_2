package net.minecraft.nbt;

import java.io.DataInput;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import java.util.Arrays;
import java.io.IOException;
import java.io.DataOutput;
import java.util.List;
import it.unimi.dsi.fastutil.longs.LongSet;

public class LongArrayTag extends CollectionTag<LongTag> {
    public static final TagType<LongArrayTag> TYPE;
    private long[] data;
    
    public LongArrayTag(final long[] arr) {
        this.data = arr;
    }
    
    public LongArrayTag(final LongSet longSet) {
        this.data = longSet.toLongArray();
    }
    
    public LongArrayTag(final List<Long> list) {
        this(toArray(list));
    }
    
    private static long[] toArray(final List<Long> list) {
        final long[] arr2 = new long[list.size()];
        for (int integer3 = 0; integer3 < list.size(); ++integer3) {
            final Long long4 = (Long)list.get(integer3);
            arr2[integer3] = ((long4 == null) ? 0L : long4);
        }
        return arr2;
    }
    
    public void write(final DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.data.length);
        for (final long long6 : this.data) {
            dataOutput.writeLong(long6);
        }
    }
    
    public byte getId() {
        return 12;
    }
    
    public TagType<LongArrayTag> getType() {
        return LongArrayTag.TYPE;
    }
    
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder("[L;");
        for (int integer3 = 0; integer3 < this.data.length; ++integer3) {
            if (integer3 != 0) {
                stringBuilder2.append(',');
            }
            stringBuilder2.append(this.data[integer3]).append('L');
        }
        return stringBuilder2.append(']').toString();
    }
    
    public LongArrayTag copy() {
        final long[] arr2 = new long[this.data.length];
        System.arraycopy(this.data, 0, arr2, 0, this.data.length);
        return new LongArrayTag(arr2);
    }
    
    public boolean equals(final Object object) {
        return this == object || (object instanceof LongArrayTag && Arrays.equals(this.data, ((LongArrayTag)object).data));
    }
    
    public int hashCode() {
        return Arrays.hashCode(this.data);
    }
    
    public Component getPrettyDisplay(final String string, final int integer) {
        final Component nr4 = new TextComponent("L").withStyle(LongArrayTag.SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
        final MutableComponent nx5 = new TextComponent("[").append(nr4).append(";");
        for (int integer2 = 0; integer2 < this.data.length; ++integer2) {
            final MutableComponent nx6 = new TextComponent(String.valueOf(this.data[integer2])).withStyle(LongArrayTag.SYNTAX_HIGHLIGHTING_NUMBER);
            nx5.append(" ").append(nx6).append(nr4);
            if (integer2 != this.data.length - 1) {
                nx5.append(",");
            }
        }
        nx5.append("]");
        return nx5;
    }
    
    public long[] getAsLongArray() {
        return this.data;
    }
    
    public int size() {
        return this.data.length;
    }
    
    public LongTag get(final int integer) {
        return LongTag.valueOf(this.data[integer]);
    }
    
    @Override
    public LongTag set(final int integer, final LongTag ml) {
        final long long4 = this.data[integer];
        this.data[integer] = ml.getAsLong();
        return LongTag.valueOf(long4);
    }
    
    @Override
    public void add(final int integer, final LongTag ml) {
        this.data = ArrayUtils.add(this.data, integer, ml.getAsLong());
    }
    
    @Override
    public boolean setTag(final int integer, final Tag mt) {
        if (mt instanceof NumericTag) {
            this.data[integer] = ((NumericTag)mt).getAsLong();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean addTag(final int integer, final Tag mt) {
        if (mt instanceof NumericTag) {
            this.data = ArrayUtils.add(this.data, integer, ((NumericTag)mt).getAsLong());
            return true;
        }
        return false;
    }
    
    @Override
    public LongTag remove(final int integer) {
        final long long3 = this.data[integer];
        this.data = ArrayUtils.remove(this.data, integer);
        return LongTag.valueOf(long3);
    }
    
    @Override
    public byte getElementType() {
        return 4;
    }
    
    public void clear() {
        this.data = new long[0];
    }
    
    static {
        TYPE = new TagType<LongArrayTag>() {
            public LongArrayTag load(final DataInput dataInput, final int integer, final NbtAccounter mm) throws IOException {
                mm.accountBits(192L);
                final int integer2 = dataInput.readInt();
                mm.accountBits(64L * integer2);
                final long[] arr6 = new long[integer2];
                for (int integer3 = 0; integer3 < integer2; ++integer3) {
                    arr6[integer3] = dataInput.readLong();
                }
                return new LongArrayTag(arr6);
            }
            
            public String getName() {
                return "LONG[]";
            }
            
            public String getPrettyName() {
                return "TAG_Long_Array";
            }
        };
    }
}
