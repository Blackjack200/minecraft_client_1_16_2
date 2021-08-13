package net.minecraft.nbt;

import java.io.IOException;
import java.io.DataInput;

public interface TagType<T extends Tag> {
    T load(final DataInput dataInput, final int integer, final NbtAccounter mm) throws IOException;
    
    default boolean isValue() {
        return false;
    }
    
    String getName();
    
    String getPrettyName();
    
    default TagType<EndTag> createInvalid(final int integer) {
        return new TagType<EndTag>() {
            public EndTag load(final DataInput dataInput, final int integer, final NbtAccounter mm) throws IOException {
                throw new IllegalArgumentException(new StringBuilder().append("Invalid tag id: ").append(integer).toString());
            }
            
            public String getName() {
                return new StringBuilder().append("INVALID[").append(integer).append("]").toString();
            }
            
            public String getPrettyName() {
                return new StringBuilder().append("UNKNOWN_").append(integer).toString();
            }
        };
    }
}
