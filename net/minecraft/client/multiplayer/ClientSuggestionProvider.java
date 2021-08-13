package net.minecraft.client.multiplayer;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Set;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import java.util.Locale;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.player.LocalPlayer;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import java.util.Collections;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.Collection;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;

public class ClientSuggestionProvider implements SharedSuggestionProvider {
    private final ClientPacketListener connection;
    private final Minecraft minecraft;
    private int pendingSuggestionsId;
    private CompletableFuture<Suggestions> pendingSuggestionsFuture;
    
    public ClientSuggestionProvider(final ClientPacketListener dwm, final Minecraft djw) {
        this.pendingSuggestionsId = -1;
        this.connection = dwm;
        this.minecraft = djw;
    }
    
    public Collection<String> getOnlinePlayerNames() {
        final List<String> list2 = (List<String>)Lists.newArrayList();
        for (final PlayerInfo dwp4 : this.connection.getOnlinePlayers()) {
            list2.add(dwp4.getProfile().getName());
        }
        return (Collection<String>)list2;
    }
    
    public Collection<String> getSelectedEntities() {
        if (this.minecraft.hitResult != null && this.minecraft.hitResult.getType() == HitResult.Type.ENTITY) {
            return (Collection<String>)Collections.singleton(((EntityHitResult)this.minecraft.hitResult).getEntity().getStringUUID());
        }
        return (Collection<String>)Collections.emptyList();
    }
    
    public Collection<String> getAllTeams() {
        return this.connection.getLevel().getScoreboard().getTeamNames();
    }
    
    public Collection<ResourceLocation> getAvailableSoundEvents() {
        return this.minecraft.getSoundManager().getAvailableSounds();
    }
    
    public Stream<ResourceLocation> getRecipeNames() {
        return this.connection.getRecipeManager().getRecipeIds();
    }
    
    public boolean hasPermission(final int integer) {
        final LocalPlayer dze3 = this.minecraft.player;
        return (dze3 != null) ? dze3.hasPermissions(integer) : (integer == 0);
    }
    
    public CompletableFuture<Suggestions> customSuggestion(final CommandContext<SharedSuggestionProvider> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        if (this.pendingSuggestionsFuture != null) {
            this.pendingSuggestionsFuture.cancel(false);
        }
        this.pendingSuggestionsFuture = (CompletableFuture<Suggestions>)new CompletableFuture();
        final int integer4 = ++this.pendingSuggestionsId;
        this.connection.send(new ServerboundCommandSuggestionPacket(integer4, commandContext.getInput()));
        return this.pendingSuggestionsFuture;
    }
    
    private static String prettyPrint(final double double1) {
        return String.format(Locale.ROOT, "%.2f", new Object[] { double1 });
    }
    
    private static String prettyPrint(final int integer) {
        return Integer.toString(integer);
    }
    
    public Collection<TextCoordinates> getRelevantCoordinates() {
        final HitResult dci2 = this.minecraft.hitResult;
        if (dci2 == null || dci2.getType() != HitResult.Type.BLOCK) {
            return super.getRelevantCoordinates();
        }
        final BlockPos fx3 = ((BlockHitResult)dci2).getBlockPos();
        return (Collection<TextCoordinates>)Collections.singleton(new TextCoordinates(prettyPrint(fx3.getX()), prettyPrint(fx3.getY()), prettyPrint(fx3.getZ())));
    }
    
    public Collection<TextCoordinates> getAbsoluteCoordinates() {
        final HitResult dci2 = this.minecraft.hitResult;
        if (dci2 == null || dci2.getType() != HitResult.Type.BLOCK) {
            return super.getAbsoluteCoordinates();
        }
        final Vec3 dck3 = dci2.getLocation();
        return (Collection<TextCoordinates>)Collections.singleton(new TextCoordinates(prettyPrint(dck3.x), prettyPrint(dck3.y), prettyPrint(dck3.z)));
    }
    
    public Set<ResourceKey<Level>> levels() {
        return this.connection.levels();
    }
    
    public RegistryAccess registryAccess() {
        return this.connection.registryAccess();
    }
    
    public void completeCustomSuggestions(final int integer, final Suggestions suggestions) {
        if (integer == this.pendingSuggestionsId) {
            this.pendingSuggestionsFuture.complete(suggestions);
            this.pendingSuggestionsFuture = null;
            this.pendingSuggestionsId = -1;
        }
    }
}
