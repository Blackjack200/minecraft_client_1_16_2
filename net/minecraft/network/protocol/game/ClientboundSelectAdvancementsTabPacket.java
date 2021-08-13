package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;

public class ClientboundSelectAdvancementsTabPacket implements Packet<ClientGamePacketListener> {
    @Nullable
    private ResourceLocation tab;
    
    public ClientboundSelectAdvancementsTabPacket() {
    }
    
    public ClientboundSelectAdvancementsTabPacket(@Nullable final ResourceLocation vk) {
        this.tab = vk;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleSelectAdvancementsTab(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        if (nf.readBoolean()) {
            this.tab = nf.readResourceLocation();
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBoolean(this.tab != null);
        if (this.tab != null) {
            nf.writeResourceLocation(this.tab);
        }
    }
    
    @Nullable
    public ResourceLocation getTab() {
        return this.tab;
    }
}
