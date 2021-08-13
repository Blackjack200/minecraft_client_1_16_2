package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagContainer;
import net.minecraft.network.protocol.Packet;

public class ClientboundUpdateTagsPacket implements Packet<ClientGamePacketListener> {
    private TagContainer tags;
    
    public ClientboundUpdateTagsPacket() {
    }
    
    public ClientboundUpdateTagsPacket(final TagContainer ael) {
        this.tags = ael;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.tags = TagContainer.deserializeFromNetwork(nf);
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        this.tags.serializeToNetwork(nf);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleUpdateTags(this);
    }
    
    public TagContainer getTags() {
        return this.tags;
    }
}
