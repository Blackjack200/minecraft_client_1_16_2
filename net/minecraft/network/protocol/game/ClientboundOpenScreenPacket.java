package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import javax.annotation.Nullable;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;

public class ClientboundOpenScreenPacket implements Packet<ClientGamePacketListener> {
    private int containerId;
    private int type;
    private Component title;
    
    public ClientboundOpenScreenPacket() {
    }
    
    public ClientboundOpenScreenPacket(final int integer, final MenuType<?> bjb, final Component nr) {
        this.containerId = integer;
        this.type = Registry.MENU.getId(bjb);
        this.title = nr;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.containerId = nf.readVarInt();
        this.type = nf.readVarInt();
        this.title = nf.readComponent();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.containerId);
        nf.writeVarInt(this.type);
        nf.writeComponent(this.title);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleOpenScreen(this);
    }
    
    public int getContainerId() {
        return this.containerId;
    }
    
    @Nullable
    public MenuType<?> getType() {
        return Registry.MENU.byId(this.type);
    }
    
    public Component getTitle() {
        return this.title;
    }
}
