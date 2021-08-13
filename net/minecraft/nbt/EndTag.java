package net.minecraft.nbt;

import java.io.DataInput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import java.io.IOException;
import java.io.DataOutput;

public class EndTag implements Tag {
    public static final TagType<EndTag> TYPE;
    public static final EndTag INSTANCE;
    
    private EndTag() {
    }
    
    public void write(final DataOutput dataOutput) throws IOException {
    }
    
    public byte getId() {
        return 0;
    }
    
    public TagType<EndTag> getType() {
        return EndTag.TYPE;
    }
    
    public String toString() {
        return "END";
    }
    
    public EndTag copy() {
        return this;
    }
    
    public Component getPrettyDisplay(final String string, final int integer) {
        return TextComponent.EMPTY;
    }
    
    static {
        TYPE = new TagType<EndTag>() {
            public EndTag load(final DataInput dataInput, final int integer, final NbtAccounter mm) {
                mm.accountBits(64L);
                return EndTag.INSTANCE;
            }
            
            public String getName() {
                return "END";
            }
            
            public String getPrettyName() {
                return "TAG_End";
            }
            
            public boolean isValue() {
                return true;
            }
        };
        INSTANCE = new EndTag();
    }
}
