package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.util.task.SwitchSlotTask;
import com.mojang.realmsclient.util.task.SwitchMinigameTask;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.util.task.CloseServerTask;
import com.mojang.realmsclient.util.task.LongRunningTask;
import com.mojang.realmsclient.util.task.OpenServerTask;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.mojang.realmsclient.gui.RealmsWorldSlotButton;
import net.minecraft.network.chat.CommonComponents;
import com.mojang.realmsclient.dto.RealmsWorldOptions;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.client.gui.components.Button;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.RealmsMainScreen;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class RealmsConfigureWorldScreen extends RealmsScreenWithCallback {
    private static final Logger LOGGER;
    private static final ResourceLocation ON_ICON_LOCATION;
    private static final ResourceLocation OFF_ICON_LOCATION;
    private static final ResourceLocation EXPIRED_ICON_LOCATION;
    private static final ResourceLocation EXPIRES_SOON_ICON_LOCATION;
    private static final Component TITLE;
    private static final Component WORLD_TITLE;
    private static final Component MINIGAME_PREFIX;
    private static final Component SERVER_EXPIRED_TOOLTIP;
    private static final Component SERVER_EXPIRING_SOON_TOOLTIP;
    private static final Component SERVER_EXPIRING_IN_DAY_TOOLTIP;
    private static final Component SERVER_OPEN_TOOLTIP;
    private static final Component SERVER_CLOSED_TOOLTIP;
    @Nullable
    private Component toolTip;
    private final RealmsMainScreen lastScreen;
    @Nullable
    private RealmsServer serverData;
    private final long serverId;
    private int leftX;
    private int rightX;
    private Button playersButton;
    private Button settingsButton;
    private Button subscriptionButton;
    private Button optionsButton;
    private Button backupButton;
    private Button resetWorldButton;
    private Button switchMinigameButton;
    private boolean stateChanged;
    private int animTick;
    private int clicks;
    
    public RealmsConfigureWorldScreen(final RealmsMainScreen dft, final long long2) {
        this.lastScreen = dft;
        this.serverId = long2;
    }
    
    public void init() {
        if (this.serverData == null) {
            this.fetchServerData(this.serverId);
        }
        this.leftX = this.width / 2 - 187;
        this.rightX = this.width / 2 + 190;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.playersButton = this.<Button>addButton(new Button(this.centerButton(0, 3), RealmsScreen.row(0), 100, 20, new TranslatableComponent("mco.configure.world.buttons.players"), dlg -> this.minecraft.setScreen(new RealmsPlayerScreen(this, this.serverData))));
        this.settingsButton = this.<Button>addButton(new Button(this.centerButton(1, 3), RealmsScreen.row(0), 100, 20, new TranslatableComponent("mco.configure.world.buttons.settings"), dlg -> this.minecraft.setScreen(new RealmsSettingsScreen(this, this.serverData.clone()))));
        this.subscriptionButton = this.<Button>addButton(new Button(this.centerButton(2, 3), RealmsScreen.row(0), 100, 20, new TranslatableComponent("mco.configure.world.buttons.subscription"), dlg -> this.minecraft.setScreen(new RealmsSubscriptionInfoScreen(this, this.serverData.clone(), this.lastScreen))));
        for (int integer2 = 1; integer2 < 5; ++integer2) {
            this.addSlotButton(integer2);
        }
        final RealmsSelectWorldTemplateScreen dif3;
        this.switchMinigameButton = this.<Button>addButton(new Button(this.leftButton(0), RealmsScreen.row(13) - 5, 100, 20, new TranslatableComponent("mco.configure.world.buttons.switchminigame"), dlg -> {
            dif3 = new RealmsSelectWorldTemplateScreen(this, RealmsServer.WorldType.MINIGAME);
            dif3.setTitle(new TranslatableComponent("mco.template.title.minigame"));
            this.minecraft.setScreen(dif3);
            return;
        }));
        this.optionsButton = this.<Button>addButton(new Button(this.leftButton(0), RealmsScreen.row(13) - 5, 90, 20, new TranslatableComponent("mco.configure.world.buttons.options"), dlg -> this.minecraft.setScreen(new RealmsSlotOptionsScreen(this, ((RealmsWorldOptions)this.serverData.slots.get(this.serverData.activeSlot)).clone(), this.serverData.worldType, this.serverData.activeSlot))));
        this.backupButton = this.<Button>addButton(new Button(this.leftButton(1), RealmsScreen.row(13) - 5, 90, 20, new TranslatableComponent("mco.configure.world.backup"), dlg -> this.minecraft.setScreen(new RealmsBackupScreen(this, this.serverData.clone(), this.serverData.activeSlot))));
        this.resetWorldButton = this.<Button>addButton(new Button(this.leftButton(2), RealmsScreen.row(13) - 5, 90, 20, new TranslatableComponent("mco.configure.world.buttons.resetworld"), dlg -> this.minecraft.setScreen(new RealmsResetWorldScreen(this, this.serverData.clone(), () -> this.minecraft.setScreen(this.getNewScreen()), () -> this.minecraft.setScreen(this.getNewScreen())))));
        this.<Button>addButton(new Button(this.rightX - 80 + 8, RealmsScreen.row(13) - 5, 70, 20, CommonComponents.GUI_BACK, dlg -> this.backButtonClicked()));
        this.backupButton.active = true;
        if (this.serverData == null) {
            this.hideMinigameButtons();
            this.hideRegularButtons();
            this.playersButton.active = false;
            this.settingsButton.active = false;
            this.subscriptionButton.active = false;
        }
        else {
            this.disableButtons();
            if (this.isMinigame()) {
                this.hideRegularButtons();
            }
            else {
                this.hideMinigameButtons();
            }
        }
    }
    
    private void addSlotButton(final int integer) {
        final int integer2 = this.frame(integer);
        final int integer3 = RealmsScreen.row(5) + 5;
        final RealmsWorldSlotButton.State b4;
        final IllegalStateException ex;
        final RealmsWorldSlotButton dhj5 = new RealmsWorldSlotButton(integer2, integer3, 80, 80, (Supplier<RealmsServer>)(() -> this.serverData), (Consumer<Component>)(nr -> this.toolTip = nr), integer, dlg -> {
            b4 = dlg.getState();
            if (b4 != null) {
                switch (b4.action) {
                    case NOTHING: {
                        break;
                    }
                    case JOIN: {
                        this.joinRealm(this.serverData);
                        break;
                    }
                    case SWITCH_SLOT: {
                        if (b4.minigame) {
                            this.switchToMinigame();
                            break;
                        }
                        else if (b4.empty) {
                            this.switchToEmptySlot(integer, this.serverData);
                            break;
                        }
                        else {
                            this.switchToFullSlot(integer, this.serverData);
                            break;
                        }
                        break;
                    }
                    default: {
                        new IllegalStateException(new StringBuilder().append("Unknown action ").append(b4.action).toString());
                        throw ex;
                    }
                }
            }
            return;
        });
        this.<RealmsWorldSlotButton>addButton(dhj5);
    }
    
    private int leftButton(final int integer) {
        return this.leftX + integer * 95;
    }
    
    private int centerButton(final int integer1, final int integer2) {
        return this.width / 2 - (integer2 * 105 - 5) / 2 + integer1 * 105;
    }
    
    @Override
    public void tick() {
        super.tick();
        ++this.animTick;
        --this.clicks;
        if (this.clicks < 0) {
            this.clicks = 0;
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.toolTip = null;
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, RealmsConfigureWorldScreen.TITLE, this.width / 2, RealmsScreen.row(4), 16777215);
        super.render(dfj, integer2, integer3, float4);
        if (this.serverData == null) {
            GuiComponent.drawCenteredString(dfj, this.font, RealmsConfigureWorldScreen.WORLD_TITLE, this.width / 2, 17, 16777215);
            return;
        }
        final String string6 = this.serverData.getName();
        final int integer4 = this.font.width(string6);
        final int integer5 = (this.serverData.state == RealmsServer.State.CLOSED) ? 10526880 : 8388479;
        final int integer6 = this.font.width(RealmsConfigureWorldScreen.WORLD_TITLE);
        GuiComponent.drawCenteredString(dfj, this.font, RealmsConfigureWorldScreen.WORLD_TITLE, this.width / 2, 12, 16777215);
        GuiComponent.drawCenteredString(dfj, this.font, string6, this.width / 2, 24, integer5);
        final int integer7 = Math.min(this.centerButton(2, 3) + 80 - 11, this.width / 2 + integer4 / 2 + integer6 / 2 + 10);
        this.drawServerStatus(dfj, integer7, 7, integer2, integer3);
        if (this.isMinigame()) {
            this.font.draw(dfj, RealmsConfigureWorldScreen.MINIGAME_PREFIX.copy().append(this.serverData.getMinigameName()), (float)(this.leftX + 80 + 20 + 10), (float)RealmsScreen.row(13), 16777215);
        }
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(dfj, this.toolTip, integer2, integer3);
        }
    }
    
    private int frame(final int integer) {
        return this.leftX + (integer - 1) * 98;
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
        if (this.stateChanged) {
            this.lastScreen.removeSelection();
        }
        this.minecraft.setScreen(this.lastScreen);
    }
    
    private void fetchServerData(final long long1) {
        new Thread(() -> {
            final RealmsClient dfy4 = RealmsClient.create();
            try {
                this.serverData = dfy4.getOwnWorld(long1);
                this.disableButtons();
                if (this.isMinigame()) {
                    this.show(this.switchMinigameButton);
                }
                else {
                    this.show(this.optionsButton);
                    this.show(this.backupButton);
                    this.show(this.resetWorldButton);
                }
            }
            catch (RealmsServiceException dhf5) {
                RealmsConfigureWorldScreen.LOGGER.error("Couldn't get own world");
                this.minecraft.execute(() -> this.minecraft.setScreen(new RealmsGenericErrorScreen(Component.nullToEmpty(dhf5.getMessage()), this.lastScreen)));
            }
        }).start();
    }
    
    private void disableButtons() {
        this.playersButton.active = !this.serverData.expired;
        this.settingsButton.active = !this.serverData.expired;
        this.subscriptionButton.active = true;
        this.switchMinigameButton.active = !this.serverData.expired;
        this.optionsButton.active = !this.serverData.expired;
        this.resetWorldButton.active = !this.serverData.expired;
    }
    
    private void joinRealm(final RealmsServer dgn) {
        if (this.serverData.state == RealmsServer.State.OPEN) {
            this.lastScreen.play(dgn, new RealmsConfigureWorldScreen(this.lastScreen.newScreen(), this.serverId));
        }
        else {
            this.openTheWorld(true, new RealmsConfigureWorldScreen(this.lastScreen.newScreen(), this.serverId));
        }
    }
    
    private void switchToMinigame() {
        final RealmsSelectWorldTemplateScreen dif2 = new RealmsSelectWorldTemplateScreen(this, RealmsServer.WorldType.MINIGAME);
        dif2.setTitle(new TranslatableComponent("mco.template.title.minigame"));
        dif2.setWarning(new TranslatableComponent("mco.minigame.world.info.line1"), new TranslatableComponent("mco.minigame.world.info.line2"));
        this.minecraft.setScreen(dif2);
    }
    
    private void switchToFullSlot(final int integer, final RealmsServer dgn) {
        final Component nr4 = new TranslatableComponent("mco.configure.world.slot.switch.question.line1");
        final Component nr5 = new TranslatableComponent("mco.configure.world.slot.switch.question.line2");
        this.minecraft.setScreen(new RealmsLongConfirmationScreen(boolean3 -> {
            if (boolean3) {
                this.minecraft.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen, new SwitchSlotTask(dgn.id, integer, () -> this.minecraft.setScreen(this.getNewScreen()))));
            }
            else {
                this.minecraft.setScreen(this);
            }
        }, RealmsLongConfirmationScreen.Type.Info, nr4, nr5, true));
    }
    
    private void switchToEmptySlot(final int integer, final RealmsServer dgn) {
        final Component nr4 = new TranslatableComponent("mco.configure.world.slot.switch.question.line1");
        final Component nr5 = new TranslatableComponent("mco.configure.world.slot.switch.question.line2");
        this.minecraft.setScreen(new RealmsLongConfirmationScreen(boolean3 -> {
            if (boolean3) {
                final RealmsResetWorldScreen dic5 = new RealmsResetWorldScreen(this, dgn, new TranslatableComponent("mco.configure.world.switch.slot"), new TranslatableComponent("mco.configure.world.switch.slot.subtitle"), 10526880, CommonComponents.GUI_CANCEL, () -> this.minecraft.setScreen(this.getNewScreen()), () -> this.minecraft.setScreen(this.getNewScreen()));
                dic5.setSlot(integer);
                dic5.setResetTitle(new TranslatableComponent("mco.create.world.reset.title"));
                this.minecraft.setScreen(dic5);
            }
            else {
                this.minecraft.setScreen(this);
            }
        }, RealmsLongConfirmationScreen.Type.Info, nr4, nr5, true));
    }
    
    protected void renderMousehoverTooltip(final PoseStack dfj, @Nullable final Component nr, final int integer3, final int integer4) {
        int integer5 = integer3 + 12;
        final int integer6 = integer4 - 12;
        final int integer7 = this.font.width(nr);
        if (integer5 + integer7 + 3 > this.rightX) {
            integer5 = integer5 - integer7 - 20;
        }
        this.fillGradient(dfj, integer5 - 3, integer6 - 3, integer5 + integer7 + 3, integer6 + 8 + 3, -1073741824, -1073741824);
        this.font.drawShadow(dfj, nr, (float)integer5, (float)integer6, 16777215);
    }
    
    private void drawServerStatus(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        if (this.serverData.expired) {
            this.drawExpired(dfj, integer2, integer3, integer4, integer5);
        }
        else if (this.serverData.state == RealmsServer.State.CLOSED) {
            this.drawClose(dfj, integer2, integer3, integer4, integer5);
        }
        else if (this.serverData.state == RealmsServer.State.OPEN) {
            if (this.serverData.daysLeft < 7) {
                this.drawExpiring(dfj, integer2, integer3, integer4, integer5, this.serverData.daysLeft);
            }
            else {
                this.drawOpen(dfj, integer2, integer3, integer4, integer5);
            }
        }
    }
    
    private void drawExpired(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        this.minecraft.getTextureManager().bind(RealmsConfigureWorldScreen.EXPIRED_ICON_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GuiComponent.blit(dfj, integer2, integer3, 0.0f, 0.0f, 10, 28, 10, 28);
        if (integer4 >= integer2 && integer4 <= integer2 + 9 && integer5 >= integer3 && integer5 <= integer3 + 27) {
            this.toolTip = RealmsConfigureWorldScreen.SERVER_EXPIRED_TOOLTIP;
        }
    }
    
    private void drawExpiring(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        this.minecraft.getTextureManager().bind(RealmsConfigureWorldScreen.EXPIRES_SOON_ICON_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.animTick % 20 < 10) {
            GuiComponent.blit(dfj, integer2, integer3, 0.0f, 0.0f, 10, 28, 20, 28);
        }
        else {
            GuiComponent.blit(dfj, integer2, integer3, 10.0f, 0.0f, 10, 28, 20, 28);
        }
        if (integer4 >= integer2 && integer4 <= integer2 + 9 && integer5 >= integer3 && integer5 <= integer3 + 27) {
            if (integer6 <= 0) {
                this.toolTip = RealmsConfigureWorldScreen.SERVER_EXPIRING_SOON_TOOLTIP;
            }
            else if (integer6 == 1) {
                this.toolTip = RealmsConfigureWorldScreen.SERVER_EXPIRING_IN_DAY_TOOLTIP;
            }
            else {
                this.toolTip = new TranslatableComponent("mco.selectServer.expires.days", new Object[] { integer6 });
            }
        }
    }
    
    private void drawOpen(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        this.minecraft.getTextureManager().bind(RealmsConfigureWorldScreen.ON_ICON_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GuiComponent.blit(dfj, integer2, integer3, 0.0f, 0.0f, 10, 28, 10, 28);
        if (integer4 >= integer2 && integer4 <= integer2 + 9 && integer5 >= integer3 && integer5 <= integer3 + 27) {
            this.toolTip = RealmsConfigureWorldScreen.SERVER_OPEN_TOOLTIP;
        }
    }
    
    private void drawClose(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        this.minecraft.getTextureManager().bind(RealmsConfigureWorldScreen.OFF_ICON_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GuiComponent.blit(dfj, integer2, integer3, 0.0f, 0.0f, 10, 28, 10, 28);
        if (integer4 >= integer2 && integer4 <= integer2 + 9 && integer5 >= integer3 && integer5 <= integer3 + 27) {
            this.toolTip = RealmsConfigureWorldScreen.SERVER_CLOSED_TOOLTIP;
        }
    }
    
    private boolean isMinigame() {
        return this.serverData != null && this.serverData.worldType == RealmsServer.WorldType.MINIGAME;
    }
    
    private void hideRegularButtons() {
        this.hide(this.optionsButton);
        this.hide(this.backupButton);
        this.hide(this.resetWorldButton);
    }
    
    private void hide(final Button dlg) {
        dlg.visible = false;
        this.children.remove(dlg);
        this.buttons.remove(dlg);
    }
    
    private void show(final Button dlg) {
        dlg.visible = true;
        this.<Button>addButton(dlg);
    }
    
    private void hideMinigameButtons() {
        this.hide(this.switchMinigameButton);
    }
    
    public void saveSlotSettings(final RealmsWorldOptions dgt) {
        final RealmsWorldOptions dgt2 = (RealmsWorldOptions)this.serverData.slots.get(this.serverData.activeSlot);
        dgt.templateId = dgt2.templateId;
        dgt.templateImage = dgt2.templateImage;
        final RealmsClient dfy4 = RealmsClient.create();
        try {
            dfy4.updateSlot(this.serverData.id, this.serverData.activeSlot, dgt);
            this.serverData.slots.put(this.serverData.activeSlot, dgt);
        }
        catch (RealmsServiceException dhf5) {
            RealmsConfigureWorldScreen.LOGGER.error("Couldn't save slot settings");
            this.minecraft.setScreen(new RealmsGenericErrorScreen(dhf5, this));
            return;
        }
        this.minecraft.setScreen(this);
    }
    
    public void saveSettings(final String string1, final String string2) {
        final String string3 = string2.trim().isEmpty() ? null : string2;
        final RealmsClient dfy5 = RealmsClient.create();
        try {
            dfy5.update(this.serverData.id, string1, string3);
            this.serverData.setName(string1);
            this.serverData.setDescription(string3);
        }
        catch (RealmsServiceException dhf6) {
            RealmsConfigureWorldScreen.LOGGER.error("Couldn't save settings");
            this.minecraft.setScreen(new RealmsGenericErrorScreen(dhf6, this));
            return;
        }
        this.minecraft.setScreen(this);
    }
    
    public void openTheWorld(final boolean boolean1, final Screen doq) {
        this.minecraft.setScreen(new RealmsLongRunningMcoTaskScreen(doq, new OpenServerTask(this.serverData, this, this.lastScreen, boolean1)));
    }
    
    public void closeTheWorld(final Screen doq) {
        this.minecraft.setScreen(new RealmsLongRunningMcoTaskScreen(doq, new CloseServerTask(this.serverData, this)));
    }
    
    public void stateChanged() {
        this.stateChanged = true;
    }
    
    @Override
    protected void callback(@Nullable final WorldTemplate dhb) {
        if (dhb == null) {
            return;
        }
        if (WorldTemplate.WorldTemplateType.MINIGAME == dhb.type) {
            this.minecraft.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen, new SwitchMinigameTask(this.serverData.id, dhb, this.getNewScreen())));
        }
    }
    
    public RealmsConfigureWorldScreen getNewScreen() {
        return new RealmsConfigureWorldScreen(this.lastScreen, this.serverId);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ON_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/on_icon.png");
        OFF_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/off_icon.png");
        EXPIRED_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/expired_icon.png");
        EXPIRES_SOON_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/expires_soon_icon.png");
        TITLE = new TranslatableComponent("mco.configure.worlds.title");
        WORLD_TITLE = new TranslatableComponent("mco.configure.world.title");
        MINIGAME_PREFIX = new TranslatableComponent("mco.configure.current.minigame").append(": ");
        SERVER_EXPIRED_TOOLTIP = new TranslatableComponent("mco.selectServer.expired");
        SERVER_EXPIRING_SOON_TOOLTIP = new TranslatableComponent("mco.selectServer.expires.soon");
        SERVER_EXPIRING_IN_DAY_TOOLTIP = new TranslatableComponent("mco.selectServer.expires.day");
        SERVER_OPEN_TOOLTIP = new TranslatableComponent("mco.selectServer.open");
        SERVER_CLOSED_TOOLTIP = new TranslatableComponent("mco.selectServer.closed");
    }
}
