package net.minecraft.network.protocol;

import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;

public interface Packet<T extends PacketListener> {
    void read(final FriendlyByteBuf nf) throws IOException;
    
    void write(final FriendlyByteBuf nf) throws IOException;
    
    void handle(final T ni);
    
    default boolean isSkippable() {
        return false;
    }
}
