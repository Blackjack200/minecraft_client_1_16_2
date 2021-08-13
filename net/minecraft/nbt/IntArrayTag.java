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

public class IntArrayTag extends CollectionTag<IntTag> {
    public static final TagType<IntArrayTag> TYPE;
    private int[] data;
    
    public IntArrayTag(final int[] arr) {
        this.data = arr;
    }
    
    public IntArrayTag(final List<Integer> list) {
        this(toArray(list));
    }
    
    private static int[] toArray(final List<Integer> list) {
        final int[] arr2 = new int[list.size()];
        for (int integer3 = 0; integer3 < list.size(); ++integer3) {
            final Integer integer4 = (Integer)list.get(integer3);
            arr2[integer3] = ((integer4 == null) ? 0 : integer4);
        }
        return arr2;
    }
    
    public void write(final DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.data.length);
        for (final int integer6 : this.data) {
            dataOutput.writeInt(integer6);
        }
    }
    
    public byte getId() {
        return 11;
    }
    
    public TagType<IntArrayTag> getType() {
        return IntArrayTag.TYPE;
    }
    
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder("[I;");
        for (int integer3 = 0; integer3 < this.data.length; ++integer3) {
            if (integer3 != 0) {
                stringBuilder2.append(',');
            }
            stringBuilder2.append(this.data[integer3]);
        }
        return stringBuilder2.append(']').toString();
    }
    
    public IntArrayTag copy() {
        final int[] arr2 = new int[this.data.length];
        System.arraycopy(this.data, 0, arr2, 0, this.data.length);
        return new IntArrayTag(arr2);
    }
    
    public boolean equals(final Object object) {
        return this == object || (object instanceof IntArrayTag && Arrays.equals(this.data, ((IntArrayTag)object).data));
    }
    
    public int hashCode() {
        return Arrays.hashCode(this.data);
    }
    
    public int[] getAsIntArray() {
        return this.data;
    }
    
    public Component getPrettyDisplay(final String string, final int integer) {
        final Component nr4 = new TextComponent("I").withStyle(IntArrayTag.SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
        final MutableComponent nx5 = new TextComponent("[").append(nr4).append(";");
        for (int integer2 = 0; integer2 < this.data.length; ++integer2) {
            nx5.append(" ").append(new TextComponent(String.valueOf(this.data[integer2])).withStyle(IntArrayTag.SYNTAX_HIGHLIGHTING_NUMBER));
            if (integer2 != this.data.length - 1) {
                nx5.append(",");
            }
        }
        nx5.append("]");
        return nx5;
    }
    
    public int size() {
        return this.data.length;
    }
    
    public IntTag get(final int integer) {
        return IntTag.valueOf(this.data[integer]);
    }
    
    @Override
    public IntTag set(final int integer, final IntTag mi) {
        final int integer2 = this.data[integer];
        this.data[integer] = mi.getAsInt();
        return IntTag.valueOf(integer2);
    }
    
    @Override
    public void add(final int integer, final IntTag mi) {
        this.data = ArrayUtils.add(this.data, integer, mi.getAsInt());
    }
    
    @Override
    public boolean setTag(final int integer, final Tag mt) {
        if (mt instanceof NumericTag) {
            this.data[integer] = ((NumericTag)mt).getAsInt();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean addTag(final int integer, final Tag mt) {
        if (mt instanceof NumericTag) {
            this.data = ArrayUtils.add(this.data, integer, ((NumericTag)mt).getAsInt());
            return true;
        }
        return false;
    }
    
    @Override
    public IntTag remove(final int integer) {
        final int integer2 = this.data[integer];
        this.data = ArrayUtils.remove(this.data, integer);
        return IntTag.valueOf(integer2);
    }
    
    @Override
    public byte getElementType() {
        return 3;
    }
    
    public void clear() {
        this.data = new int[0];
    }
    
    static {
        TYPE = new TagType<IntArrayTag>() {
            public IntArrayTag load(final DataInput dataInput, final int integer, final NbtAccounter mm) throws IOException {
                mm.accountBits(192L);
                final int integer2 = dataInput.readInt();
                mm.accountBits(32L * integer2);
                final int[] arr6 = new int[integer2];
                for (int integer3 = 0; integer3 < integer2; ++integer3) {
                    arr6[integer3] = dataInput.readInt();
                }
                return new IntArrayTag(arr6);
            }
            
            public String getName() {
                return "INT[]";
            }
            
            public String getPrettyName() {
                return "TAG_Int_Array";
            }
        };
    }
}
