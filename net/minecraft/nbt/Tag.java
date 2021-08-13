package net.minecraft.nbt;

import net.minecraft.network.chat.Component;
import java.io.IOException;
import java.io.DataOutput;
import net.minecraft.ChatFormatting;

public interface Tag {
    public static final ChatFormatting SYNTAX_HIGHLIGHTING_KEY = ChatFormatting.AQUA;
    public static final ChatFormatting SYNTAX_HIGHLIGHTING_STRING = ChatFormatting.GREEN;
    public static final ChatFormatting SYNTAX_HIGHLIGHTING_NUMBER = ChatFormatting.GOLD;
    public static final ChatFormatting SYNTAX_HIGHLIGHTING_NUMBER_TYPE = ChatFormatting.RED;
    
    void write(final DataOutput dataOutput) throws IOException;
    
    String toString();
    
    byte getId();
    
    TagType<?> getType();
    
    Tag copy();
    
    default String getAsString() {
        return this.toString();
    }
    
    default Component getPrettyDisplay() {
        return this.getPrettyDisplay("", 0);
    }
    
    Component getPrettyDisplay(final String string, final int integer);
}
