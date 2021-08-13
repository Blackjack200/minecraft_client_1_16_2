package net.minecraft.server.bossevents;

import net.minecraft.server.level.ServerPlayer;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import java.util.Collection;
import net.minecraft.network.chat.Component;
import javax.annotation.Nullable;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public class CustomBossEvents {
    private final Map<ResourceLocation, CustomBossEvent> events;
    
    public CustomBossEvents() {
        this.events = (Map<ResourceLocation, CustomBossEvent>)Maps.newHashMap();
    }
    
    @Nullable
    public CustomBossEvent get(final ResourceLocation vk) {
        return (CustomBossEvent)this.events.get(vk);
    }
    
    public CustomBossEvent create(final ResourceLocation vk, final Component nr) {
        final CustomBossEvent wc4 = new CustomBossEvent(vk, nr);
        this.events.put(vk, wc4);
        return wc4;
    }
    
    public void remove(final CustomBossEvent wc) {
        this.events.remove(wc.getTextId());
    }
    
    public Collection<ResourceLocation> getIds() {
        return (Collection<ResourceLocation>)this.events.keySet();
    }
    
    public Collection<CustomBossEvent> getEvents() {
        return (Collection<CustomBossEvent>)this.events.values();
    }
    
    public CompoundTag save() {
        final CompoundTag md2 = new CompoundTag();
        for (final CustomBossEvent wc4 : this.events.values()) {
            md2.put(wc4.getTextId().toString(), wc4.save());
        }
        return md2;
    }
    
    public void load(final CompoundTag md) {
        for (final String string4 : md.getAllKeys()) {
            final ResourceLocation vk5 = new ResourceLocation(string4);
            this.events.put(vk5, CustomBossEvent.load(md.getCompound(string4), vk5));
        }
    }
    
    public void onPlayerConnect(final ServerPlayer aah) {
        for (final CustomBossEvent wc4 : this.events.values()) {
            wc4.onPlayerConnect(aah);
        }
    }
    
    public void onPlayerDisconnect(final ServerPlayer aah) {
        for (final CustomBossEvent wc4 : this.events.values()) {
            wc4.onPlayerDisconnect(aah);
        }
    }
}
