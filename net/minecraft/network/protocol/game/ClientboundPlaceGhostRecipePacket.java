package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;

public class ClientboundPlaceGhostRecipePacket implements Packet<ClientGamePacketListener> {
    private int containerId;
    private ResourceLocation recipe;
    
    public ClientboundPlaceGhostRecipePacket() {
    }
    
    public ClientboundPlaceGhostRecipePacket(final int integer, final Recipe<?> bon) {
        this.containerId = integer;
        this.recipe = bon.getId();
    }
    
    public ResourceLocation getRecipe() {
        return this.recipe;
    }
    
    public int getContainerId() {
        return this.containerId;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.containerId = nf.readByte();
        this.recipe = nf.readResourceLocation();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeByte(this.containerId);
        nf.writeResourceLocation(this.recipe);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handlePlaceRecipe(this);
    }
}
