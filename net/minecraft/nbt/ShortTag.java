package net.minecraft.nbt;

import java.io.DataInput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import java.io.IOException;
import java.io.DataOutput;

public class ShortTag extends NumericTag {
    public static final TagType<ShortTag> TYPE;
    private final short data;
    
    private ShortTag(final short short1) {
        this.data = short1;
    }
    
    public static ShortTag valueOf(final short short1) {
        if (short1 >= -128 && short1 <= 1024) {
            return Cache.cache[short1 + 128];
        }
        return new ShortTag(short1);
    }
    
    public void write(final DataOutput dataOutput) throws IOException {
        dataOutput.writeShort((int)this.data);
    }
    
    public byte getId() {
        return 2;
    }
    
    public TagType<ShortTag> getType() {
        return ShortTag.TYPE;
    }
    
    public String toString() {
        return new StringBuilder().append((int)this.data).append("s").toString();
    }
    
    public ShortTag copy() {
        return this;
    }
    
    public boolean equals(final Object object) {
        return this == object || (object instanceof ShortTag && this.data == ((ShortTag)object).data);
    }
    
    public int hashCode() {
        return this.data;
    }
    
    public Component getPrettyDisplay(final String string, final int integer) {
        final Component nr4 = new TextComponent("s").withStyle(ShortTag.SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
        return new TextComponent(String.valueOf((int)this.data)).append(nr4).withStyle(ShortTag.SYNTAX_HIGHLIGHTING_NUMBER);
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
        return (byte)(this.data & 0xFF);
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
        TYPE = new TagType<ShortTag>() {
            public ShortTag load(final DataInput dataInput, final int integer, final NbtAccounter mm) throws IOException {
                mm.accountBits(80L);
                return ShortTag.valueOf(dataInput.readShort());
            }
            
            public String getName() {
                return "SHORT";
            }
            
            public String getPrettyName() {
                return "TAG_Short";
            }
            
            public boolean isValue() {
                return true;
            }
        };
    }
    
    static class Cache {
        static final ShortTag[] cache;
        
        static {
            cache = new ShortTag[1153];
            for (int integer1 = 0; integer1 < Cache.cache.length; ++integer1) {
                Cache.cache[integer1] = new ShortTag((short)(-128 + integer1), null);
            }
        }
    }
}
