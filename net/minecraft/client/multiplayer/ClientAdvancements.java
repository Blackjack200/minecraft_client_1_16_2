package net.minecraft.client.multiplayer;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import java.util.Iterator;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;
import java.util.Map;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class ClientAdvancements {
    private static final Logger LOGGER;
    private final Minecraft minecraft;
    private final AdvancementList advancements;
    private final Map<Advancement, AdvancementProgress> progress;
    @Nullable
    private Listener listener;
    @Nullable
    private Advancement selectedTab;
    
    public ClientAdvancements(final Minecraft djw) {
        this.advancements = new AdvancementList();
        this.progress = (Map<Advancement, AdvancementProgress>)Maps.newHashMap();
        this.minecraft = djw;
    }
    
    public void update(final ClientboundUpdateAdvancementsPacket rt) {
        if (rt.shouldReset()) {
            this.advancements.clear();
            this.progress.clear();
        }
        this.advancements.remove(rt.getRemoved());
        this.advancements.add(rt.getAdded());
        for (final Map.Entry<ResourceLocation, AdvancementProgress> entry4 : rt.getProgress().entrySet()) {
            final Advancement y5 = this.advancements.get((ResourceLocation)entry4.getKey());
            if (y5 != null) {
                final AdvancementProgress aa6 = (AdvancementProgress)entry4.getValue();
                aa6.update(y5.getCriteria(), y5.getRequirements());
                this.progress.put(y5, aa6);
                if (this.listener != null) {
                    this.listener.onUpdateAdvancementProgress(y5, aa6);
                }
                if (rt.shouldReset() || !aa6.isDone() || y5.getDisplay() == null || !y5.getDisplay().shouldShowToast()) {
                    continue;
                }
                this.minecraft.getToasts().addToast(new AdvancementToast(y5));
            }
            else {
                ClientAdvancements.LOGGER.warn("Server informed client about progress for unknown advancement {}", entry4.getKey());
            }
        }
    }
    
    public AdvancementList getAdvancements() {
        return this.advancements;
    }
    
    public void setSelectedTab(@Nullable final Advancement y, final boolean boolean2) {
        final ClientPacketListener dwm4 = this.minecraft.getConnection();
        if (dwm4 != null && y != null && boolean2) {
            dwm4.send(ServerboundSeenAdvancementsPacket.openedTab(y));
        }
        if (this.selectedTab != y) {
            this.selectedTab = y;
            if (this.listener != null) {
                this.listener.onSelectedTabChanged(y);
            }
        }
    }
    
    public void setListener(@Nullable final Listener a) {
        this.listener = a;
        this.advancements.setListener(a);
        if (a != null) {
            for (final Map.Entry<Advancement, AdvancementProgress> entry4 : this.progress.entrySet()) {
                a.onUpdateAdvancementProgress((Advancement)entry4.getKey(), (AdvancementProgress)entry4.getValue());
            }
            a.onSelectedTabChanged(this.selectedTab);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public interface Listener extends AdvancementList.Listener {
        void onUpdateAdvancementProgress(final Advancement y, final AdvancementProgress aa);
        
        void onSelectedTabChanged(@Nullable final Advancement y);
    }
}
