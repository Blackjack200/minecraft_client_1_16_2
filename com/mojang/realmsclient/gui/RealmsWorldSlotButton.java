package com.mojang.realmsclient.gui;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.Mth;
import com.mojang.realmsclient.util.RealmsTextureManager;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.realmsclient.dto.RealmsWorldOptions;
import net.minecraft.network.chat.TextComponent;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import com.mojang.realmsclient.dto.RealmsServer;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.components.TickableWidget;
import net.minecraft.client.gui.components.Button;

public class RealmsWorldSlotButton extends Button implements TickableWidget {
    public static final ResourceLocation SLOT_FRAME_LOCATION;
    public static final ResourceLocation EMPTY_SLOT_LOCATION;
    public static final ResourceLocation DEFAULT_WORLD_SLOT_1;
    public static final ResourceLocation DEFAULT_WORLD_SLOT_2;
    public static final ResourceLocation DEFAULT_WORLD_SLOT_3;
    private static final Component SLOT_ACTIVE_TOOLTIP;
    private static final Component SWITCH_TO_MINIGAME_SLOT_TOOLTIP;
    private static final Component SWITCH_TO_WORLD_SLOT_TOOLTIP;
    private final Supplier<RealmsServer> serverDataProvider;
    private final Consumer<Component> toolTipSetter;
    private final int slotIndex;
    private int animTick;
    @Nullable
    private State state;
    
    public RealmsWorldSlotButton(final int integer1, final int integer2, final int integer3, final int integer4, final Supplier<RealmsServer> supplier, final Consumer<Component> consumer, final int integer7, final OnPress a) {
        super(integer1, integer2, integer3, integer4, TextComponent.EMPTY, a);
        this.serverDataProvider = supplier;
        this.slotIndex = integer7;
        this.toolTipSetter = consumer;
    }
    
    @Nullable
    public State getState() {
        return this.state;
    }
    
    @Override
    public void tick() {
        ++this.animTick;
        final RealmsServer dgn2 = (RealmsServer)this.serverDataProvider.get();
        if (dgn2 == null) {
            return;
        }
        final RealmsWorldOptions dgt5 = (RealmsWorldOptions)dgn2.slots.get(this.slotIndex);
        final boolean boolean10 = this.slotIndex == 4;
        boolean boolean11;
        String string4;
        long long6;
        String string5;
        boolean boolean12;
        if (boolean10) {
            boolean11 = (dgn2.worldType == RealmsServer.WorldType.MINIGAME);
            string4 = "Minigame";
            long6 = dgn2.minigameId;
            string5 = dgn2.minigameImage;
            boolean12 = (dgn2.minigameId == -1);
        }
        else {
            boolean11 = (dgn2.activeSlot == this.slotIndex && dgn2.worldType != RealmsServer.WorldType.MINIGAME);
            string4 = dgt5.getSlotName(this.slotIndex);
            long6 = dgt5.templateId;
            string5 = dgt5.templateImage;
            boolean12 = dgt5.empty;
        }
        final Action a11 = getAction(dgn2, boolean11, boolean10);
        final Pair<Component, Component> pair12 = this.getTooltipAndNarration(dgn2, string4, boolean12, boolean10, a11);
        this.state = new State(boolean11, string4, long6, string5, boolean12, boolean10, a11, (Component)pair12.getFirst());
        this.setMessage((Component)pair12.getSecond());
    }
    
    private static Action getAction(final RealmsServer dgn, final boolean boolean2, final boolean boolean3) {
        if (boolean2) {
            if (!dgn.expired && dgn.state != RealmsServer.State.UNINITIALIZED) {
                return Action.JOIN;
            }
        }
        else {
            if (!boolean3) {
                return Action.SWITCH_SLOT;
            }
            if (!dgn.expired) {
                return Action.SWITCH_SLOT;
            }
        }
        return Action.NOTHING;
    }
    
    private Pair<Component, Component> getTooltipAndNarration(final RealmsServer dgn, final String string, final boolean boolean3, final boolean boolean4, final Action a) {
        if (a == Action.NOTHING) {
            return (Pair<Component, Component>)Pair.of(null, new TextComponent(string));
        }
        Component nr7;
        if (boolean4) {
            if (boolean3) {
                nr7 = TextComponent.EMPTY;
            }
            else {
                nr7 = new TextComponent(" ").append(string).append(" ").append(dgn.minigameName);
            }
        }
        else {
            nr7 = new TextComponent(" ").append(string);
        }
        Component nr8;
        if (a == Action.JOIN) {
            nr8 = RealmsWorldSlotButton.SLOT_ACTIVE_TOOLTIP;
        }
        else {
            nr8 = (boolean4 ? RealmsWorldSlotButton.SWITCH_TO_MINIGAME_SLOT_TOOLTIP : RealmsWorldSlotButton.SWITCH_TO_WORLD_SLOT_TOOLTIP);
        }
        final Component nr9 = nr8.copy().append(nr7);
        return (Pair<Component, Component>)Pair.of(nr8, nr9);
    }
    
    @Override
    public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (this.state == null) {
            return;
        }
        this.drawSlotFrame(dfj, this.x, this.y, integer2, integer3, this.state.isCurrentlyActiveSlot, this.state.slotName, this.slotIndex, this.state.imageId, this.state.image, this.state.empty, this.state.minigame, this.state.action, this.state.actionPrompt);
    }
    
    private void drawSlotFrame(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6, final String string7, final int integer8, final long long9, @Nullable final String string10, final boolean boolean11, final boolean boolean12, final Action a, @Nullable final Component nr) {
        final boolean boolean13 = this.isHovered();
        if (this.isMouseOver(integer4, integer5) && nr != null) {
            this.toolTipSetter.accept(nr);
        }
        final Minecraft djw18 = Minecraft.getInstance();
        final TextureManager ejv19 = djw18.getTextureManager();
        if (boolean12) {
            RealmsTextureManager.bindWorldTemplate(String.valueOf(long9), string10);
        }
        else if (boolean11) {
            ejv19.bind(RealmsWorldSlotButton.EMPTY_SLOT_LOCATION);
        }
        else if (string10 != null && long9 != -1L) {
            RealmsTextureManager.bindWorldTemplate(String.valueOf(long9), string10);
        }
        else if (integer8 == 1) {
            ejv19.bind(RealmsWorldSlotButton.DEFAULT_WORLD_SLOT_1);
        }
        else if (integer8 == 2) {
            ejv19.bind(RealmsWorldSlotButton.DEFAULT_WORLD_SLOT_2);
        }
        else if (integer8 == 3) {
            ejv19.bind(RealmsWorldSlotButton.DEFAULT_WORLD_SLOT_3);
        }
        if (boolean6) {
            final float float20 = 0.85f + 0.15f * Mth.cos(this.animTick * 0.2f);
            RenderSystem.color4f(float20, float20, float20, 1.0f);
        }
        else {
            RenderSystem.color4f(0.56f, 0.56f, 0.56f, 1.0f);
        }
        GuiComponent.blit(dfj, integer2 + 3, integer3 + 3, 0.0f, 0.0f, 74, 74, 74, 74);
        ejv19.bind(RealmsWorldSlotButton.SLOT_FRAME_LOCATION);
        final boolean boolean14 = boolean13 && a != Action.NOTHING;
        if (boolean14) {
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        else if (boolean6) {
            RenderSystem.color4f(0.8f, 0.8f, 0.8f, 1.0f);
        }
        else {
            RenderSystem.color4f(0.56f, 0.56f, 0.56f, 1.0f);
        }
        GuiComponent.blit(dfj, integer2, integer3, 0.0f, 0.0f, 80, 80, 80, 80);
        GuiComponent.drawCenteredString(dfj, djw18.font, string7, integer2 + 40, integer3 + 66, 16777215);
    }
    
    static {
        SLOT_FRAME_LOCATION = new ResourceLocation("realms", "textures/gui/realms/slot_frame.png");
        EMPTY_SLOT_LOCATION = new ResourceLocation("realms", "textures/gui/realms/empty_frame.png");
        DEFAULT_WORLD_SLOT_1 = new ResourceLocation("minecraft", "textures/gui/title/background/panorama_0.png");
        DEFAULT_WORLD_SLOT_2 = new ResourceLocation("minecraft", "textures/gui/title/background/panorama_2.png");
        DEFAULT_WORLD_SLOT_3 = new ResourceLocation("minecraft", "textures/gui/title/background/panorama_3.png");
        SLOT_ACTIVE_TOOLTIP = new TranslatableComponent("mco.configure.world.slot.tooltip.active");
        SWITCH_TO_MINIGAME_SLOT_TOOLTIP = new TranslatableComponent("mco.configure.world.slot.tooltip.minigame");
        SWITCH_TO_WORLD_SLOT_TOOLTIP = new TranslatableComponent("mco.configure.world.slot.tooltip");
    }
    
    public enum Action {
        NOTHING, 
        SWITCH_SLOT, 
        JOIN;
    }
    
    public static class State {
        private final boolean isCurrentlyActiveSlot;
        private final String slotName;
        private final long imageId;
        private final String image;
        public final boolean empty;
        public final boolean minigame;
        public final Action action;
        @Nullable
        private final Component actionPrompt;
        
        State(final boolean boolean1, final String string2, final long long3, @Nullable final String string4, final boolean boolean5, final boolean boolean6, final Action a, @Nullable final Component nr) {
            this.isCurrentlyActiveSlot = boolean1;
            this.slotName = string2;
            this.imageId = long3;
            this.image = string4;
            this.empty = boolean5;
            this.minigame = boolean6;
            this.action = a;
            this.actionPrompt = nr;
        }
    }
}
