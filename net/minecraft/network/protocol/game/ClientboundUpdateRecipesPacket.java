package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.world.item.crafting.Recipe;
import java.util.List;
import net.minecraft.network.protocol.Packet;

public class ClientboundUpdateRecipesPacket implements Packet<ClientGamePacketListener> {
    private List<Recipe<?>> recipes;
    
    public ClientboundUpdateRecipesPacket() {
    }
    
    public ClientboundUpdateRecipesPacket(final Collection<Recipe<?>> collection) {
        this.recipes = (List<Recipe<?>>)Lists.newArrayList((Iterable)collection);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleUpdateRecipes(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.recipes = (List<Recipe<?>>)Lists.newArrayList();
        for (int integer3 = nf.readVarInt(), integer4 = 0; integer4 < integer3; ++integer4) {
            this.recipes.add(fromNetwork(nf));
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeVarInt(this.recipes.size());
        for (final Recipe<?> bon4 : this.recipes) {
            ClientboundUpdateRecipesPacket.<Recipe<?>>toNetwork(bon4, nf);
        }
    }
    
    public List<Recipe<?>> getRecipes() {
        return this.recipes;
    }
    
    public static Recipe<?> fromNetwork(final FriendlyByteBuf nf) {
        final ResourceLocation vk2 = nf.readResourceLocation();
        final ResourceLocation vk3 = nf.readResourceLocation();
        return ((RecipeSerializer)Registry.RECIPE_SERIALIZER.getOptional(vk2).orElseThrow(() -> new IllegalArgumentException(new StringBuilder().append("Unknown recipe serializer ").append(vk2).toString()))).fromNetwork(vk3, nf);
    }
    
    public static <T extends Recipe<?>> void toNetwork(final T bon, final FriendlyByteBuf nf) {
        nf.writeResourceLocation(Registry.RECIPE_SERIALIZER.getKey(bon.getSerializer()));
        nf.writeResourceLocation(bon.getId());
        bon.getSerializer().toNetwork(nf, bon);
    }
}
