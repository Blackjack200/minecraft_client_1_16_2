package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.network.protocol.Packet;

public class ServerboundRecipeBookChangeSettingsPacket implements Packet<ServerGamePacketListener> {
    private RecipeBookType bookType;
    private boolean isOpen;
    private boolean isFiltering;
    
    public ServerboundRecipeBookChangeSettingsPacket() {
    }
    
    public ServerboundRecipeBookChangeSettingsPacket(final RecipeBookType bjh, final boolean boolean2, final boolean boolean3) {
        this.bookType = bjh;
        this.isOpen = boolean2;
        this.isFiltering = boolean3;
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.bookType = nf.<RecipeBookType>readEnum(RecipeBookType.class);
        this.isOpen = nf.readBoolean();
        this.isFiltering = nf.readBoolean();
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeEnum(this.bookType);
        nf.writeBoolean(this.isOpen);
        nf.writeBoolean(this.isFiltering);
    }
    
    public void handle(final ServerGamePacketListener sa) {
        sa.handleRecipeBookChangeSettingsPacket(this);
    }
    
    public RecipeBookType getBookType() {
        return this.bookType;
    }
    
    public boolean isOpen() {
        return this.isOpen;
    }
    
    public boolean isFiltering() {
        return this.isFiltering;
    }
}
