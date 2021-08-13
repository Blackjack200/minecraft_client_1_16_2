package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;

public class ServerboundCustomPayloadPacket implements Packet<ServerGamePacketListener> {
    public static final ResourceLocation BRAND;
    private ResourceLocation identifier;
    private FriendlyByteBuf data;
    
    public ServerboundCustomPayloadPacket() {
    }
    
    public ServerboundCustomPayloadPacket(final ResourceLocation vk, final FriendlyByteBuf nf) {
        this.identifier = vk;
        this.data = nf;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.identifier = nf.readResourceLocation();
        final int integer3 = nf.readableBytes();
        if (integer3 < 0 || integer3 > 32767) {
            throw new IOException("Payload may not be larger than 32767 bytes");
        }
        this.data = new FriendlyByteBuf(nf.readBytes(integer3));
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeResourceLocation(this.identifier);
        nf.writeBytes(this.data);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleCustomPayload(this);
        if (this.data != null) {
            this.data.release();
        }
    }
    
    static {
        BRAND = new ResourceLocation("brand");
    }
}
