package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;
import java.io.IOException;
import com.google.common.collect.Sets;
import net.minecraft.network.FriendlyByteBuf;
import java.util.Iterator;
import com.google.common.collect.Maps;
import java.util.Collection;
import net.minecraft.advancements.AdvancementProgress;
import java.util.Set;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import net.minecraft.network.protocol.Packet;

public class ClientboundUpdateAdvancementsPacket implements Packet<ClientGamePacketListener> {
    private boolean reset;
    private Map<ResourceLocation, Advancement.Builder> added;
    private Set<ResourceLocation> removed;
    private Map<ResourceLocation, AdvancementProgress> progress;
    
    public ClientboundUpdateAdvancementsPacket() {
    }
    
    public ClientboundUpdateAdvancementsPacket(final boolean boolean1, final Collection<Advancement> collection, final Set<ResourceLocation> set, final Map<ResourceLocation, AdvancementProgress> map) {
        this.reset = boolean1;
        this.added = (Map<ResourceLocation, Advancement.Builder>)Maps.newHashMap();
        for (final Advancement y7 : collection) {
            this.added.put(y7.getId(), y7.deconstruct());
        }
        this.removed = set;
        this.progress = (Map<ResourceLocation, AdvancementProgress>)Maps.newHashMap((Map)map);
    }
    
    public void handle(final ClientGamePacketListener om) {
        om.handleUpdateAdvancementsPacket(this);
    }
    
    public void read(final FriendlyByteBuf nf) throws IOException {
        this.reset = nf.readBoolean();
        this.added = (Map<ResourceLocation, Advancement.Builder>)Maps.newHashMap();
        this.removed = (Set<ResourceLocation>)Sets.newLinkedHashSet();
        this.progress = (Map<ResourceLocation, AdvancementProgress>)Maps.newHashMap();
        for (int integer3 = nf.readVarInt(), integer4 = 0; integer4 < integer3; ++integer4) {
            final ResourceLocation vk5 = nf.readResourceLocation();
            final Advancement.Builder a6 = Advancement.Builder.fromNetwork(nf);
            this.added.put(vk5, a6);
        }
        for (int integer3 = nf.readVarInt(), integer4 = 0; integer4 < integer3; ++integer4) {
            final ResourceLocation vk5 = nf.readResourceLocation();
            this.removed.add(vk5);
        }
        for (int integer3 = nf.readVarInt(), integer4 = 0; integer4 < integer3; ++integer4) {
            final ResourceLocation vk5 = nf.readResourceLocation();
            this.progress.put(vk5, AdvancementProgress.fromNetwork(nf));
        }
    }
    
    public void write(final FriendlyByteBuf nf) throws IOException {
        nf.writeBoolean(this.reset);
        nf.writeVarInt(this.added.size());
        for (final Map.Entry<ResourceLocation, Advancement.Builder> entry4 : this.added.entrySet()) {
            final ResourceLocation vk5 = (ResourceLocation)entry4.getKey();
            final Advancement.Builder a6 = (Advancement.Builder)entry4.getValue();
            nf.writeResourceLocation(vk5);
            a6.serializeToNetwork(nf);
        }
        nf.writeVarInt(this.removed.size());
        for (final ResourceLocation vk6 : this.removed) {
            nf.writeResourceLocation(vk6);
        }
        nf.writeVarInt(this.progress.size());
        for (final Map.Entry<ResourceLocation, AdvancementProgress> entry5 : this.progress.entrySet()) {
            nf.writeResourceLocation((ResourceLocation)entry5.getKey());
            ((AdvancementProgress)entry5.getValue()).serializeToNetwork(nf);
        }
    }
    
    public Map<ResourceLocation, Advancement.Builder> getAdded() {
        return this.added;
    }
    
    public Set<ResourceLocation> getRemoved() {
        return this.removed;
    }
    
    public Map<ResourceLocation, AdvancementProgress> getProgress() {
        return this.progress;
    }
    
    public boolean shouldReset() {
        return this.reset;
    }
}
