package net.minecraft.nbt;

import java.io.DataInput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import java.io.IOException;
import java.io.DataOutput;

public class ByteTag extends NumericTag {
    public static final TagType<ByteTag> TYPE;
    public static final ByteTag ZERO;
    public static final ByteTag ONE;
    private final byte data;
    
    private ByteTag(final byte byte1) {
        this.data = byte1;
    }
    
    public static ByteTag valueOf(final byte byte1) {
        return Cache.cache[128 + byte1];
    }
    
    public static ByteTag valueOf(final boolean boolean1) {
        return boolean1 ? ByteTag.ONE : ByteTag.ZERO;
    }
    
    public void write(final DataOutput dataOutput) throws IOException {
        dataOutput.writeByte((int)this.data);
    }
    
    public byte getId() {
        return 1;
    }
    
    public TagType<ByteTag> getType() {
        return ByteTag.TYPE;
    }
    
    public String toString() {
        return new StringBuilder().append((int)this.data).append("b").toString();
    }
    
    public ByteTag copy() {
        return this;
    }
    
    public boolean equals(final Object object) {
        return this == object || (object instanceof ByteTag && this.data == ((ByteTag)object).data);
    }
    
    public int hashCode() {
        return this.data;
    }
    
    public Component getPrettyDisplay(final String string, final int integer) {
        final Component nr4 = new TextComponent("b").withStyle(ByteTag.SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
        return new TextComponent(String.valueOf((int)this.data)).append(nr4).withStyle(ByteTag.SYNTAX_HIGHLIGHTING_NUMBER);
    }
    
    @Override
    public long getAsLong() {
        return this.data;
    }
    
    @Override
    public int getAsInt() {
        return this.data;
    }
    
    @Override
    public short getAsShort() {
        return this.data;
    }
    
    @Override
    public byte getAsByte() {
        return this.data;
    }
    
    @Override
    public double getAsDouble() {
        return this.data;
    }
    
    @Override
    public float getAsFloat() {
        return this.data;
    }
    
    @Override
    public Number getAsNumber() {
        return this.data;
    }
    
    static {
        TYPE = new TagType<ByteTag>() {
            public ByteTag load(final DataInput dataInput, final int integer, final NbtAccounter mm) throws IOException {
                mm.accountBits(72L);
                return ByteTag.valueOf(dataInput.readByte());
            }
            
            public String getName() {
                return "BYTE";
            }
            
            public String getPrettyName() {
                return "TAG_Byte";
            }
            
            public boolean isValue() {
                return true;
            }
        };
        ZERO = valueOf((byte)0);
        ONE = valueOf((byte)1);
    }
    
    static class Cache {
        private static final ByteTag[] cache;
        
        static {
            cache = new ByteTag[256];
            for (int integer1 = 0; integer1 < Cache.cache.length; ++integer1) {
                Cache.cache[integer1] = new ByteTag((byte)(integer1 - 128), null);
            }
        }
    }
}
