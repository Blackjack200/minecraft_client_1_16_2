package net.minecraft.nbt;

import java.io.DataInput;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import java.io.IOException;
import java.io.DataOutput;

public class FloatTag extends NumericTag {
    public static final FloatTag ZERO;
    public static final TagType<FloatTag> TYPE;
    private final float data;
    
    private FloatTag(final float float1) {
        this.data = float1;
    }
    
    public static FloatTag valueOf(final float float1) {
        if (float1 == 0.0f) {
            return FloatTag.ZERO;
        }
        return new FloatTag(float1);
    }
    
    public void write(final DataOutput dataOutput) throws IOException {
        dataOutput.writeFloat(this.data);
    }
    
    public byte getId() {
        return 5;
    }
    
    public TagType<FloatTag> getType() {
        return FloatTag.TYPE;
    }
    
    public String toString() {
        return new StringBuilder().append(this.data).append("f").toString();
    }
    
    public FloatTag copy() {
        return this;
    }
    
    public boolean equals(final Object object) {
        return this == object || (object instanceof FloatTag && this.data == ((FloatTag)object).data);
    }
    
    public int hashCode() {
        return Float.floatToIntBits(this.data);
    }
    
    public Component getPrettyDisplay(final String string, final int integer) {
        final Component nr4 = new TextComponent("f").withStyle(FloatTag.SYNTAX_HIGHLIGHTING_NUMBER_TYPE);
        return new TextComponent(String.valueOf(this.data)).append(nr4).withStyle(FloatTag.SYNTAX_HIGHLIGHTING_NUMBER);
    }
    
    @Override
    public long getAsLong() {
        return (long)this.data;
    }
    
    @Override
    public int getAsInt() {
        return Mth.floor(this.data);
    }
    
    @Override
    public short getAsShort() {
        return (short)(Mth.floor(this.data) & 0xFFFF);
    }
    
    @Override
    public byte getAsByte() {
        return (byte)(Mth.floor(this.data) & 0xFF);
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
        ZERO = new FloatTag(0.0f);
        TYPE = new TagType<FloatTag>() {
            public FloatTag load(final DataInput dataInput, final int integer, final NbtAccounter mm) throws IOException {
                mm.accountBits(96L);
                return FloatTag.valueOf(dataInput.readFloat());
            }
            
            public String getName() {
                return "FLOAT";
            }
            
            public String getPrettyName() {
                return "TAG_Float";
            }
            
            public boolean isValue() {
                return true;
            }
        };
    }
}
