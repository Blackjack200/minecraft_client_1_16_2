package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.util.task.OpenServerTask;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.realmsclient.util.RealmsTextureManager;
import com.mojang.realmsclient.gui.RealmsWorldSlotButton;
import com.mojang.realmsclient.dto.WorldDownload;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import com.mojang.realmsclient.util.task.LongRunningTask;
import com.mojang.realmsclient.util.task.SwitchSlotTask;
import com.mojang.realmsclient.dto.RealmsWorldOptions;
import java.util.Map;
import net.minecraft.realms.NarrationHelper;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.List;
import net.minecraft.network.chat.Component;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsBrokenWorldScreen extends RealmsScreen {
    private static final Logger LOGGER;
    private final Screen lastScreen;
    private final RealmsMainScreen mainScreen;
    private RealmsServer serverData;
    private final long serverId;
    private final Component header;
    private final Component[] message;
    private int leftX;
    private int rightX;
    private final List<Integer> slotsThatHasBeenDownloaded;
    private int animTick;
    
    public RealmsBrokenWorldScreen(final Screen doq, final RealmsMainScreen dft, final long long3, final boolean boolean4) {
        this.message = new Component[] { new TranslatableComponent("mco.brokenworld.message.line1"), new TranslatableComponent("mco.brokenworld.message.line2") };
        this.slotsThatHasBeenDownloaded = (List<Integer>)Lists.newArrayList();
        this.lastScreen = doq;
        this.mainScreen = dft;
        this.serverId = long3;
        this.header = (boolean4 ? new TranslatableComponent("mco.brokenworld.minigame.title") : new TranslatableComponent("mco.brokenworld.title"));
    }
    
    public void init() {
        this.leftX = this.width / 2 - 150;
        this.rightX = this.width / 2 + 190;
        this.<Button>addButton(new Button(this.rightX - 80 + 8, RealmsScreen.row(13) - 5, 70, 20, CommonComponents.GUI_BACK, dlg -> this.backButtonClicked()));
        if (this.serverData == null) {
            this.fetchServerData(this.serverId);
        }
        else {
            this.addButtons();
        }
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        NarrationHelper.now((String)Stream.concat(Stream.of(this.header), Stream.of((Object[])this.message)).map(Component::getString).collect(Collectors.joining(" ")));
    }
    
    private void addButtons() {
        for (final Map.Entry<Integer, RealmsWorldOptions> entry3 : this.serverData.slots.entrySet()) {
            final int integer4 = (int)entry3.getKey();
            final boolean boolean5 = integer4 != this.serverData.activeSlot || this.serverData.worldType == RealmsServer.WorldType.MINIGAME;
            Button dlg6;
            if (boolean5) {
                final int n;
                final RealmsResetWorldScreen realmsResetWorldScreen;
                RealmsResetWorldScreen dic4;
                Minecraft minecraft;
                final Screen screen;
                dlg6 = new Button(this.getFramePositionX(integer4), RealmsScreen.row(8), 80, 20, new TranslatableComponent("mco.brokenworld.play"), dlg -> {
                    if (((RealmsWorldOptions)this.serverData.slots.get(n)).empty) {
                        new RealmsResetWorldScreen(this, this.serverData, new TranslatableComponent("mco.configure.world.switch.slot"), new TranslatableComponent("mco.configure.world.switch.slot.subtitle"), 10526880, CommonComponents.GUI_CANCEL, this::doSwitchOrReset, () -> {
                            this.minecraft.setScreen(this);
                            this.doSwitchOrReset();
                        });
                        dic4 = realmsResetWorldScreen;
                        dic4.setSlot(n);
                        dic4.setResetTitle(new TranslatableComponent("mco.create.world.reset.title"));
                        this.minecraft.setScreen(dic4);
                    }
                    else {
                        minecraft = this.minecraft;
                        new RealmsLongRunningMcoTaskScreen(this.lastScreen, new SwitchSlotTask(this.serverData.id, n, this::doSwitchOrReset));
                        minecraft.setScreen(screen);
                    }
                    return;
                });
            }
            else {
                final Component nr4;
                final Component nr5;
                final Object o;
                dlg6 = new Button(this.getFramePositionX(integer4), RealmsScreen.row(8), 80, 20, new TranslatableComponent("mco.brokenworld.download"), dlg -> {
                    nr4 = new TranslatableComponent("mco.configure.world.restore.download.question.line1");
                    nr5 = new TranslatableComponent("mco.configure.world.restore.download.question.line2");
                    this.minecraft.setScreen(new RealmsLongConfirmationScreen(boolean2 -> {
                        if (boolean2) {
                            this.downloadWorld(o);
                        }
                        else {
                            this.minecraft.setScreen(this);
                        }
                    }, RealmsLongConfirmationScreen.Type.Info, nr4, nr5, true));
                    return;
                });
            }
            if (this.slotsThatHasBeenDownloaded.contains(integer4)) {
                dlg6.active = false;
                dlg6.setMessage(new TranslatableComponent("mco.brokenworld.downloaded"));
            }
            this.<Button>addButton(dlg6);
            final RealmsResetWorldScreen dic5;
            final int slot;
            this.<Button>addButton(new Button(this.getFramePositionX(integer4), RealmsScreen.row(10), 80, 20, new TranslatableComponent("mco.brokenworld.reset"), dlg -> {
                dic5 = new RealmsResetWorldScreen(this, this.serverData, this::doSwitchOrReset, () -> {
                    this.minecraft.setScreen(this);
                    this.doSwitchOrReset();
                });
                if (slot != this.serverData.activeSlot || this.serverData.worldType == RealmsServer.WorldType.MINIGAME) {
                    dic5.setSlot(slot);
                }
                this.minecraft.setScreen(dic5);
            }));
        }
    }
    
    @Override
    public void tick() {
        ++this.animTick;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        super.render(dfj, integer2, integer3, float4);
        GuiComponent.drawCenteredString(dfj, this.font, this.header, this.width / 2, 17, 16777215);
        for (int integer4 = 0; integer4 < this.message.length; ++integer4) {
            GuiComponent.drawCenteredString(dfj, this.font, this.message[integer4], this.width / 2, RealmsScreen.row(-1) + 3 + integer4 * 12, 10526880);
        }
        if (this.serverData == null) {
            return;
        }
        for (final Map.Entry<Integer, RealmsWorldOptions> entry7 : this.serverData.slots.entrySet()) {
            if (((RealmsWorldOptions)entry7.getValue()).templateImage != null && ((RealmsWorldOptions)entry7.getValue()).templateId != -1L) {
                this.drawSlotFrame(dfj, this.getFramePositionX((int)entry7.getKey()), RealmsScreen.row(1) + 5, integer2, integer3, this.serverData.activeSlot == (int)entry7.getKey() && !this.isMinigame(), ((RealmsWorldOptions)entry7.getValue()).getSlotName((int)entry7.getKey()), (int)entry7.getKey(), ((RealmsWorldOptions)entry7.getValue()).templateId, ((RealmsWorldOptions)entry7.getValue()).templateImage, ((RealmsWorldOptions)entry7.getValue()).empty);
            }
            else {
                this.drawSlotFrame(dfj, this.getFramePositionX((int)entry7.getKey()), RealmsScreen.row(1) + 5, integer2, integer3, this.serverData.activeSlot == (int)entry7.getKey() && !this.isMinigame(), ((RealmsWorldOptions)entry7.getValue()).getSlotName((int)entry7.getKey()), (int)entry7.getKey(), -1L, null, ((RealmsWorldOptions)entry7.getValue()).empty);
            }
        }
    }
    
    private int getFramePositionX(final int integer) {
        return this.leftX + (integer - 1) * 110;
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.backButtonClicked();
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    private void backButtonClicked() {
        this.minecraft.setScreen(this.lastScreen);
    }
    
    private void fetchServerData(final long long1) {
        new Thread(() -> {
            final RealmsClient dfy4 = RealmsClient.create();
            try {
                this.serverData = dfy4.getOwnWorld(long1);
                this.addButtons();
            }
            catch (RealmsServiceException dhf5) {
                RealmsBrokenWorldScreen.LOGGER.error("Couldn't get own world");
                this.minecraft.setScreen(new RealmsGenericErrorScreen(Component.nullToEmpty(dhf5.getMessage()), this.lastScreen));
            }
        }).start();
    }
    
    public void doSwitchOrReset() {
        new Thread(() -> {
            final RealmsClient dfy2 = RealmsClient.create();
            if (this.serverData.state == RealmsServer.State.CLOSED) {
                this.minecraft.execute(() -> this.minecraft.setScreen(new RealmsLongRunningMcoTaskScreen(this, new OpenServerTask(this.serverData, this, this.mainScreen, true))));
            }
            else {
                try {
                    this.mainScreen.newScreen().play(dfy2.getOwnWorld(this.serverId), this);
                }
                catch (RealmsServiceException dhf3) {
                    RealmsBrokenWorldScreen.LOGGER.error("Couldn't get own world");
                    this.minecraft.execute(() -> this.minecraft.setScreen(this.lastScreen));
                }
            }
        }).start();
    }
    
    private void downloadWorld(final int integer) {
        final RealmsClient dfy3 = RealmsClient.create();
        try {
            final WorldDownload dha4 = dfy3.requestDownloadInfo(this.serverData.id, integer);
            final RealmsDownloadLatestWorldScreen dhs5 = new RealmsDownloadLatestWorldScreen(this, dha4, this.serverData.getWorldName(integer), boolean2 -> {
                if (boolean2) {
                    this.slotsThatHasBeenDownloaded.add(integer);
                    this.children.clear();
                    this.addButtons();
                }
                else {
                    this.minecraft.setScreen(this);
                }
            });
            this.minecraft.setScreen(dhs5);
        }
        catch (RealmsServiceException dhf4) {
            RealmsBrokenWorldScreen.LOGGER.error("Couldn't download world data");
            this.minecraft.setScreen(new RealmsGenericErrorScreen(dhf4, this));
        }
    }
    
    private boolean isMinigame() {
        return this.serverData != null && this.serverData.worldType == RealmsServer.WorldType.MINIGAME;
    }
    
    private void drawSlotFrame(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6, final String string7, final int integer8, final long long9, final String string10, final boolean boolean11) {
        if (boolean11) {
            this.minecraft.getTextureManager().bind(RealmsWorldSlotButton.EMPTY_SLOT_LOCATION);
        }
        else if (string10 != null && long9 != -1L) {
            RealmsTextureManager.bindWorldTemplate(String.valueOf(long9), string10);
        }
        else if (integer8 == 1) {
            this.minecraft.getTextureManager().bind(RealmsWorldSlotButton.DEFAULT_WORLD_SLOT_1);
        }
        else if (integer8 == 2) {
            this.minecraft.getTextureManager().bind(RealmsWorldSlotButton.DEFAULT_WORLD_SLOT_2);
        }
        else if (integer8 == 3) {
            this.minecraft.getTextureManager().bind(RealmsWorldSlotButton.DEFAULT_WORLD_SLOT_3);
        }
        else {
            RealmsTextureManager.bindWorldTemplate(String.valueOf(this.serverData.minigameId), this.serverData.minigameImage);
        }
        if (!boolean6) {
            RenderSystem.color4f(0.56f, 0.56f, 0.56f, 1.0f);
        }
        else if (boolean6) {
            final float float14 = 0.9f + 0.1f * Mth.cos(this.animTick * 0.2f);
            RenderSystem.color4f(float14, float14, float14, 1.0f);
        }
        GuiComponent.blit(dfj, integer2 + 3, integer3 + 3, 0.0f, 0.0f, 74, 74, 74, 74);
        this.minecraft.getTextureManager().bind(RealmsWorldSlotButton.SLOT_FRAME_LOCATION);
        if (boolean6) {
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            RenderSystem.color4f(0.56f, 0.56f, 0.56f, 1.0f);
        }
        GuiComponent.blit(dfj, integer2, integer3, 0.0f, 0.0f, 80, 80, 80, 80);
        GuiComponent.drawCenteredString(dfj, this.font, string7, integer2 + 40, integer3 + 66, 16777215);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
