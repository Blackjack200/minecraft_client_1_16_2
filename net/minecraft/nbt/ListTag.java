package net.minecraft.nbt;

import java.util.Collection;
import it.unimi.dsi.fastutil.bytes.ByteOpenHashSet;
import java.util.Arrays;
import java.io.DataInput;
import net.minecraft.network.chat.MutableComponent;
import com.google.common.base.Strings;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import java.util.Objects;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.util.Iterator;
import java.io.DataOutput;
import com.google.common.collect.Lists;
import java.util.List;
import it.unimi.dsi.fastutil.bytes.ByteSet;

public class ListTag extends CollectionTag<Tag> {
    public static final TagType<ListTag> TYPE;
    private static final ByteSet INLINE_ELEMENT_TYPES;
    private final List<Tag> list;
    private byte type;
    
    private ListTag(final List<Tag> list, final byte byte2) {
        this.list = list;
        this.type = byte2;
    }
    
    public ListTag() {
        this((List<Tag>)Lists.newArrayList(), (byte)0);
    }
    
    public void write(final DataOutput dataOutput) throws IOException {
        if (this.list.isEmpty()) {
            this.type = 0;
        }
        else {
            this.type = ((Tag)this.list.get(0)).getId();
        }
        dataOutput.writeByte((int)this.type);
        dataOutput.writeInt(this.list.size());
        for (final Tag mt4 : this.list) {
            mt4.write(dataOutput);
        }
    }
    
    public byte getId() {
        return 9;
    }
    
    public TagType<ListTag> getType() {
        return ListTag.TYPE;
    }
    
    public String toString() {
        final StringBuilder stringBuilder2 = new StringBuilder("[");
        for (int integer3 = 0; integer3 < this.list.size(); ++integer3) {
            if (integer3 != 0) {
                stringBuilder2.append(',');
            }
            stringBuilder2.append(this.list.get(integer3));
        }
        return stringBuilder2.append(']').toString();
    }
    
    private void updateTypeAfterRemove() {
        if (this.list.isEmpty()) {
            this.type = 0;
        }
    }
    
    @Override
    public Tag remove(final int integer) {
        final Tag mt3 = (Tag)this.list.remove(integer);
        this.updateTypeAfterRemove();
        return mt3;
    }
    
    public boolean isEmpty() {
        return this.list.isEmpty();
    }
    
    public CompoundTag getCompound(final int integer) {
        if (integer >= 0 && integer < this.list.size()) {
            final Tag mt3 = (Tag)this.list.get(integer);
            if (mt3.getId() == 10) {
                return (CompoundTag)mt3;
            }
        }
        return new CompoundTag();
    }
    
    public ListTag getList(final int integer) {
        if (integer >= 0 && integer < this.list.size()) {
            final Tag mt3 = (Tag)this.list.get(integer);
            if (mt3.getId() == 9) {
                return (ListTag)mt3;
            }
        }
        return new ListTag();
    }
    
    public short getShort(final int integer) {
        if (integer >= 0 && integer < this.list.size()) {
            final Tag mt3 = (Tag)this.list.get(integer);
            if (mt3.getId() == 2) {
                return ((ShortTag)mt3).getAsShort();
            }
        }
        return 0;
    }
    
    public int getInt(final int integer) {
        if (integer >= 0 && integer < this.list.size()) {
            final Tag mt3 = (Tag)this.list.get(integer);
            if (mt3.getId() == 3) {
                return ((IntTag)mt3).getAsInt();
            }
        }
        return 0;
    }
    
    public int[] getIntArray(final int integer) {
        if (integer >= 0 && integer < this.list.size()) {
            final Tag mt3 = (Tag)this.list.get(integer);
            if (mt3.getId() == 11) {
                return ((IntArrayTag)mt3).getAsIntArray();
            }
        }
        return new int[0];
    }
    
    public double getDouble(final int integer) {
        if (integer >= 0 && integer < this.list.size()) {
            final Tag mt3 = (Tag)this.list.get(integer);
            if (mt3.getId() == 6) {
                return ((DoubleTag)mt3).getAsDouble();
            }
        }
        return 0.0;
    }
    
    public float getFloat(final int integer) {
        if (integer >= 0 && integer < this.list.size()) {
            final Tag mt3 = (Tag)this.list.get(integer);
            if (mt3.getId() == 5) {
                return ((FloatTag)mt3).getAsFloat();
            }
        }
        return 0.0f;
    }
    
    public String getString(final int integer) {
        if (integer < 0 || integer >= this.list.size()) {
            return "";
        }
        final Tag mt3 = (Tag)this.list.get(integer);
        if (mt3.getId() == 8) {
            return mt3.getAsString();
        }
        return mt3.toString();
    }
    
    public int size() {
        return this.list.size();
    }
    
    public Tag get(final int integer) {
        return (Tag)this.list.get(integer);
    }
    
    @Override
    public Tag set(final int integer, final Tag mt) {
        final Tag mt2 = this.get(integer);
        if (!this.setTag(integer, mt)) {
            throw new UnsupportedOperationException(String.format("Trying to add tag of type %d to list of %d", new Object[] { mt.getId(), this.type }));
        }
        return mt2;
    }
    
    @Override
    public void add(final int integer, final Tag mt) {
        if (!this.addTag(integer, mt)) {
            throw new UnsupportedOperationException(String.format("Trying to add tag of type %d to list of %d", new Object[] { mt.getId(), this.type }));
        }
    }
    
    @Override
    public boolean setTag(final int integer, final Tag mt) {
        if (this.updateType(mt)) {
            this.list.set(integer, mt);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean addTag(final int integer, final Tag mt) {
        if (this.updateType(mt)) {
            this.list.add(integer, mt);
            return true;
        }
        return false;
    }
    
    private boolean updateType(final Tag mt) {
        if (mt.getId() == 0) {
            return false;
        }
        if (this.type == 0) {
            this.type = mt.getId();
            return true;
        }
        return this.type == mt.getId();
    }
    
    public ListTag copy() {
        final Iterable<Tag> iterable2 = (Iterable<Tag>)(TagTypes.getType(this.type).isValue() ? this.list : Iterables.transform((Iterable)this.list, Tag::copy));
        final List<Tag> list3 = (List<Tag>)Lists.newArrayList((Iterable)iterable2);
        return new ListTag(list3, this.type);
    }
    
    public boolean equals(final Object object) {
        return this == object || (object instanceof ListTag && Objects.equals(this.list, ((ListTag)object).list));
    }
    
    public int hashCode() {
        return this.list.hashCode();
    }
    
    public Component getPrettyDisplay(final String string, final int integer) {
        if (this.isEmpty()) {
            return new TextComponent("[]");
        }
        if (ListTag.INLINE_ELEMENT_TYPES.contains(this.type) && this.size() <= 8) {
            final String string2 = ", ";
            final MutableComponent nx5 = new TextComponent("[");
            for (int integer2 = 0; integer2 < this.list.size(); ++integer2) {
                if (integer2 != 0) {
                    nx5.append(", ");
                }
                nx5.append(((Tag)this.list.get(integer2)).getPrettyDisplay());
            }
            nx5.append("]");
            return nx5;
        }
        final MutableComponent nx6 = new TextComponent("[");
        if (!string.isEmpty()) {
            nx6.append("\n");
        }
        final String string3 = String.valueOf(',');
        for (int integer2 = 0; integer2 < this.list.size(); ++integer2) {
            final MutableComponent nx7 = new TextComponent(Strings.repeat(string, integer + 1));
            nx7.append(((Tag)this.list.get(integer2)).getPrettyDisplay(string, integer + 1));
            if (integer2 != this.list.size() - 1) {
                nx7.append(string3).append(string.isEmpty() ? " " : "\n");
            }
            nx6.append(nx7);
        }
        if (!string.isEmpty()) {
            nx6.append("\n").append(Strings.repeat(string, integer));
        }
        nx6.append("]");
        return nx6;
    }
    
    @Override
    public byte getElementType() {
        return this.type;
    }
    
    public void clear() {
        this.list.clear();
        this.type = 0;
    }
    
    static {
        TYPE = new TagType<ListTag>() {
            public ListTag load(final DataInput dataInput, final int integer, final NbtAccounter mm) throws IOException {
                mm.accountBits(296L);
                if (integer > 512) {
                    throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
                }
                final byte byte5 = dataInput.readByte();
                final int integer2 = dataInput.readInt();
                if (byte5 == 0 && integer2 > 0) {
                    throw new RuntimeException("Missing type on ListTag");
                }
                mm.accountBits(32L * integer2);
                final TagType<?> mv7 = TagTypes.getType(byte5);
                final List<Tag> list8 = (List<Tag>)Lists.newArrayListWithCapacity(integer2);
                for (int integer3 = 0; integer3 < integer2; ++integer3) {
                    list8.add(mv7.load(dataInput, integer + 1, mm));
                }
                return new ListTag(list8, byte5, null);
            }
            
            public String getName() {
                return "LIST";
            }
            
            public String getPrettyName() {
                return "TAG_List";
            }
        };
        INLINE_ELEMENT_TYPES = (ByteSet)new ByteOpenHashSet((Collection)Arrays.asList((Object[])new Byte[] { 1, 2, 3, 4, 5, 6 }));
    }
}
