package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.util.Iterator;
import java.io.IOException;
import com.google.common.collect.Lists;
import net.minecraft.network.FriendlyByteBuf;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import net.minecraft.stats.RecipeBookSettings;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import net.minecraft.network.protocol.Packet;

public class ClientboundRecipePacket implements Packet<ClientGamePacketListener> {
    private State state;
    private List<ResourceLocation> recipes;
    private List<ResourceLocation> toHighlight;
    private RecipeBookSettings bookSettings;
    
    public ClientboundRecipePacket() {
    }
    
    public ClientboundRecipePacket(final State a, final Collection<ResourceLocation> collection2, final Collection<ResourceLocation> collection3, final RecipeBookSettings ads) {
        this.state = a;
        this.recipes = (List<ResourceLocation>)ImmutableList.copyOf((Collection)collection2);
        this.toHighlight = (List<ResourceLocation>)ImmutableList.copyOf((Collection)collection3);
        this.bookSettings = ads;
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleAddOrRemoveRecipes(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.state = nf.<State>readEnum(State.class);
        this.bookSettings = RecipeBookSettings.read(nf);
        int integer3 = nf.readVarInt();
        this.recipes = (List<ResourceLocation>)Lists.newArrayList();
        for (int integer4 = 0; integer4 < integer3; ++integer4) {
            this.recipes.add(nf.readResourceLocation());
        }
        if (this.state == State.INIT) {
            integer3 = nf.readVarInt();
            this.toHighlight = (List<ResourceLocation>)Lists.newArrayList();
            for (int integer4 = 0; integer4 < integer3; ++integer4) {
                this.toHighlight.add(nf.readResourceLocation());
            }
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeEnum(this.state);
        this.bookSettings.write(nf);
        nf.writeVarInt(this.recipes.size());
        for (final ResourceLocation vk4 : this.recipes) {
            nf.writeResourceLocation(vk4);
        }
        if (this.state == State.INIT) {
            nf.writeVarInt(this.toHighlight.size());
            for (final ResourceLocation vk4 : this.toHighlight) {
                nf.writeResourceLocation(vk4);
            }
        }
    }
    
    public List<ResourceLocation> getRecipes() {
        return this.recipes;
    }
    
    public List<ResourceLocation> getHighlights() {
        return this.toHighlight;
    }
    
    public RecipeBookSettings getBookSettings() {
        return this.bookSettings;
    }
    
    public State getState() {
        return this.state;
    }
    
    public enum State {
        INIT, 
        ADD, 
        REMOVE;
    }
}
