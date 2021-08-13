package net.minecraft.stats;

import org.apache.logging.log4j.LogManager;
import java.util.Optional;
import net.minecraft.ResourceLocationException;
import java.util.function.Consumer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import java.util.Collections;
import java.util.Iterator;
import net.minecraft.resources.ResourceLocation;
import java.util.List;
import net.minecraft.network.protocol.game.ClientboundRecipePacket;
import net.minecraft.advancements.CriteriaTriggers;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;
import java.util.Collection;
import org.apache.logging.log4j.Logger;

public class ServerRecipeBook extends RecipeBook {
    private static final Logger LOGGER;
    
    public int addRecipes(final Collection<Recipe<?>> collection, final ServerPlayer aah) {
        final List<ResourceLocation> list4 = (List<ResourceLocation>)Lists.newArrayList();
        int integer5 = 0;
        for (final Recipe<?> bon7 : collection) {
            final ResourceLocation vk8 = bon7.getId();
            if (!this.known.contains(vk8) && !bon7.isSpecial()) {
                this.add(vk8);
                this.addHighlight(vk8);
                list4.add(vk8);
                CriteriaTriggers.RECIPE_UNLOCKED.trigger(aah, bon7);
                ++integer5;
            }
        }
        this.sendRecipes(ClientboundRecipePacket.State.ADD, aah, list4);
        return integer5;
    }
    
    public int removeRecipes(final Collection<Recipe<?>> collection, final ServerPlayer aah) {
        final List<ResourceLocation> list4 = (List<ResourceLocation>)Lists.newArrayList();
        int integer5 = 0;
        for (final Recipe<?> bon7 : collection) {
            final ResourceLocation vk8 = bon7.getId();
            if (this.known.contains(vk8)) {
                this.remove(vk8);
                list4.add(vk8);
                ++integer5;
            }
        }
        this.sendRecipes(ClientboundRecipePacket.State.REMOVE, aah, list4);
        return integer5;
    }
    
    private void sendRecipes(final ClientboundRecipePacket.State a, final ServerPlayer aah, final List<ResourceLocation> list) {
        aah.connection.send(new ClientboundRecipePacket(a, (Collection<ResourceLocation>)list, (Collection<ResourceLocation>)Collections.emptyList(), this.getBookSettings()));
    }
    
    public CompoundTag toNbt() {
        final CompoundTag md2 = new CompoundTag();
        this.getBookSettings().write(md2);
        final ListTag mj3 = new ListTag();
        for (final ResourceLocation vk5 : this.known) {
            mj3.add(StringTag.valueOf(vk5.toString()));
        }
        md2.put("recipes", (Tag)mj3);
        final ListTag mj4 = new ListTag();
        for (final ResourceLocation vk6 : this.highlight) {
            mj4.add(StringTag.valueOf(vk6.toString()));
        }
        md2.put("toBeDisplayed", (Tag)mj4);
        return md2;
    }
    
    public void fromNbt(final CompoundTag md, final RecipeManager boo) {
        this.setBookSettings(RecipeBookSettings.read(md));
        final ListTag mj4 = md.getList("recipes", 8);
        this.loadRecipes(mj4, (Consumer<Recipe<?>>)this::add, boo);
        final ListTag mj5 = md.getList("toBeDisplayed", 8);
        this.loadRecipes(mj5, (Consumer<Recipe<?>>)this::addHighlight, boo);
    }
    
    private void loadRecipes(final ListTag mj, final Consumer<Recipe<?>> consumer, final RecipeManager boo) {
        for (int integer5 = 0; integer5 < mj.size(); ++integer5) {
            final String string6 = mj.getString(integer5);
            try {
                final ResourceLocation vk7 = new ResourceLocation(string6);
                final Optional<? extends Recipe<?>> optional8 = boo.byKey(vk7);
                if (!optional8.isPresent()) {
                    ServerRecipeBook.LOGGER.error("Tried to load unrecognized recipe: {} removed now.", vk7);
                }
                else {
                    consumer.accept(optional8.get());
                }
            }
            catch (ResourceLocationException v7) {
                ServerRecipeBook.LOGGER.error("Tried to load improperly formatted recipe: {} removed now.", string6);
            }
        }
    }
    
    public void sendInitialRecipeBook(final ServerPlayer aah) {
        aah.connection.send(new ClientboundRecipePacket(ClientboundRecipePacket.State.INIT, (Collection<ResourceLocation>)this.known, (Collection<ResourceLocation>)this.highlight, this.getBookSettings()));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
