package net.minecraft.client.gui.components;

import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.network.chat.Component;
import java.util.Iterator;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.BossEvent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Maps;
import java.util.UUID;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.GuiComponent;

public class BossHealthOverlay extends GuiComponent {
    private static final ResourceLocation GUI_BARS_LOCATION;
    private final Minecraft minecraft;
    private final Map<UUID, LerpingBossEvent> events;
    
    public BossHealthOverlay(final Minecraft djw) {
        this.events = (Map<UUID, LerpingBossEvent>)Maps.newLinkedHashMap();
        this.minecraft = djw;
    }
    
    public void render(final PoseStack dfj) {
        if (this.events.isEmpty()) {
            return;
        }
        final int integer3 = this.minecraft.getWindow().getGuiScaledWidth();
        int integer4 = 12;
        for (final LerpingBossEvent dlp6 : this.events.values()) {
            final int integer5 = integer3 / 2 - 91;
            final int integer6 = integer4;
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.minecraft.getTextureManager().bind(BossHealthOverlay.GUI_BARS_LOCATION);
            this.drawBar(dfj, integer5, integer6, dlp6);
            final Component nr9 = dlp6.getName();
            final int integer7 = this.minecraft.font.width(nr9);
            final int integer8 = integer3 / 2 - integer7 / 2;
            final int integer9 = integer6 - 9;
            this.minecraft.font.drawShadow(dfj, nr9, (float)integer8, (float)integer9, 16777215);
            final int n = integer4;
            final int n2 = 10;
            this.minecraft.font.getClass();
            integer4 = n + (n2 + 9);
            if (integer4 >= this.minecraft.getWindow().getGuiScaledHeight() / 3) {
                break;
            }
        }
    }
    
    private void drawBar(final PoseStack dfj, final int integer2, final int integer3, final BossEvent aoh) {
        this.blit(dfj, integer2, integer3, 0, aoh.getColor().ordinal() * 5 * 2, 182, 5);
        if (aoh.getOverlay() != BossEvent.BossBarOverlay.PROGRESS) {
            this.blit(dfj, integer2, integer3, 0, 80 + (aoh.getOverlay().ordinal() - 1) * 5 * 2, 182, 5);
        }
        final int integer4 = (int)(aoh.getPercent() * 183.0f);
        if (integer4 > 0) {
            this.blit(dfj, integer2, integer3, 0, aoh.getColor().ordinal() * 5 * 2 + 5, integer4, 5);
            if (aoh.getOverlay() != BossEvent.BossBarOverlay.PROGRESS) {
                this.blit(dfj, integer2, integer3, 0, 80 + (aoh.getOverlay().ordinal() - 1) * 5 * 2 + 5, integer4, 5);
            }
        }
    }
    
    public void update(final ClientboundBossEventPacket oz) {
        if (oz.getOperation() == ClientboundBossEventPacket.Operation.ADD) {
            this.events.put(oz.getId(), new LerpingBossEvent(oz));
        }
        else if (oz.getOperation() == ClientboundBossEventPacket.Operation.REMOVE) {
            this.events.remove(oz.getId());
        }
        else {
            ((LerpingBossEvent)this.events.get(oz.getId())).update(oz);
        }
    }
    
    public void reset() {
        this.events.clear();
    }
    
    public boolean shouldPlayMusic() {
        if (!this.events.isEmpty()) {
            for (final BossEvent aoh3 : this.events.values()) {
                if (aoh3.shouldPlayBossMusic()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean shouldDarkenScreen() {
        if (!this.events.isEmpty()) {
            for (final BossEvent aoh3 : this.events.values()) {
                if (aoh3.shouldDarkenScreen()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean shouldCreateWorldFog() {
        if (!this.events.isEmpty()) {
            for (final BossEvent aoh3 : this.events.values()) {
                if (aoh3.shouldCreateWorldFog()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    static {
        GUI_BARS_LOCATION = new ResourceLocation("textures/gui/bars.png");
    }
}
