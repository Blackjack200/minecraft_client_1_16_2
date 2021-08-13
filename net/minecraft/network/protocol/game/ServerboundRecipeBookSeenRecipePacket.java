package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.Packet;

public class ServerboundRecipeBookSeenRecipePacket implements Packet<ServerGamePacketListener> {
    private ResourceLocation recipe;
    
    public ServerboundRecipeBookSeenRecipePacket() {
    }
    
    public ServerboundRecipeBookSeenRecipePacket(final Recipe<?> bon) {
        this.recipe = bon.getId();
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.recipe = nf.readResourceLocation();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeResourceLocation(this.recipe);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleRecipeBookSeenRecipePacket(this);
    }
    
    public ResourceLocation getRecipe() {
        return this.recipe;
    }
}
