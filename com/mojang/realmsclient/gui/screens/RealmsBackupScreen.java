package com.mojang.realmsclient.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.realms.NarrationHelper;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.realms.RealmsObjectSelectionList;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FormattedText;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.util.task.RestoreTask;
import com.mojang.realmsclient.util.task.LongRunningTask;
import com.mojang.realmsclient.util.task.DownloadTask;
import com.mojang.realmsclient.dto.RealmsWorldOptions;
import java.util.Date;
import com.mojang.realmsclient.util.RealmsUtil;
import java.text.DateFormat;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Iterator;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import java.util.Collections;
import net.minecraft.realms.RealmsLabel;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.client.gui.components.Button;
import javax.annotation.Nullable;
import com.mojang.realmsclient.dto.Backup;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsBackupScreen extends RealmsScreen {
    private static final Logger LOGGER;
    private static final ResourceLocation PLUS_ICON_LOCATION;
    private static final ResourceLocation RESTORE_ICON_LOCATION;
    private static final Component RESTORE_TOOLTIP;
    private static final Component HAS_CHANGES_TOOLTIP;
    private static final Component TITLE;
    private static final Component NO_BACKUPS_LABEL;
    private static int lastScrollPosition;
    private final RealmsConfigureWorldScreen lastScreen;
    private List<Backup> backups;
    @Nullable
    private Component toolTip;
    private BackupObjectSelectionList backupObjectSelectionList;
    private int selectedBackup;
    private final int slotId;
    private Button downloadButton;
    private Button restoreButton;
    private Button changesButton;
    private Boolean noBackups;
    private final RealmsServer serverData;
    private RealmsLabel titleLabel;
    
    public RealmsBackupScreen(final RealmsConfigureWorldScreen dhp, final RealmsServer dgn, final int integer) {
        this.backups = (List<Backup>)Collections.emptyList();
        this.selectedBackup = -1;
        this.noBackups = false;
        this.lastScreen = dhp;
        this.serverData = dgn;
        this.slotId = integer;
    }
    
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.backupObjectSelectionList = new BackupObjectSelectionList();
        if (RealmsBackupScreen.lastScrollPosition != -1) {
            this.backupObjectSelectionList.setScrollAmount(RealmsBackupScreen.lastScrollPosition);
        }
        new Thread("Realms-fetch-backups") {
            public void run() {
                final RealmsClient dfy2 = RealmsClient.create();
                try {
                    final List<Backup> list3 = dfy2.backupsFor(RealmsBackupScreen.this.serverData.id).backups;
                    RealmsBackupScreen.this.minecraft.execute(() -> {
                        RealmsBackupScreen.this.backups = list3;
                        RealmsBackupScreen.this.noBackups = RealmsBackupScreen.this.backups.isEmpty();
                        RealmsBackupScreen.this.backupObjectSelectionList.clear();
                        for (final Backup dgd4 : RealmsBackupScreen.this.backups) {
                            RealmsBackupScreen.this.backupObjectSelectionList.addEntry(dgd4);
                        }
                        RealmsBackupScreen.this.generateChangeList();
                    });
                }
                catch (RealmsServiceException dhf3) {
                    RealmsBackupScreen.LOGGER.error("Couldn't request backups", (Throwable)dhf3);
                }
            }
        }.start();
        this.downloadButton = this.<Button>addButton(new Button(this.width - 135, RealmsScreen.row(1), 120, 20, new TranslatableComponent("mco.backup.button.download"), dlg -> this.downloadClicked()));
        this.restoreButton = this.<Button>addButton(new Button(this.width - 135, RealmsScreen.row(3), 120, 20, new TranslatableComponent("mco.backup.button.restore"), dlg -> this.restoreClicked(this.selectedBackup)));
        this.changesButton = this.<Button>addButton(new Button(this.width - 135, RealmsScreen.row(5), 120, 20, new TranslatableComponent("mco.backup.changes.tooltip"), dlg -> {
            this.minecraft.setScreen(new RealmsBackupInfoScreen(this, (Backup)this.backups.get(this.selectedBackup)));
            this.selectedBackup = -1;
            return;
        }));
        this.<Button>addButton(new Button(this.width - 100, this.height - 35, 85, 20, CommonComponents.GUI_BACK, dlg -> this.minecraft.setScreen(this.lastScreen)));
        this.<BackupObjectSelectionList>addWidget(this.backupObjectSelectionList);
        this.titleLabel = this.<RealmsLabel>addWidget(new RealmsLabel(new TranslatableComponent("mco.configure.world.backup"), this.width / 2, 12, 16777215));
        this.magicalSpecialHackyFocus(this.backupObjectSelectionList);
        this.updateButtonStates();
        this.narrateLabels();
    }
    
    private void generateChangeList() {
        if (this.backups.size() <= 1) {
            return;
        }
        for (int integer2 = 0; integer2 < this.backups.size() - 1; ++integer2) {
            final Backup dgd3 = (Backup)this.backups.get(integer2);
            final Backup dgd4 = (Backup)this.backups.get(integer2 + 1);
            if (!dgd3.metadata.isEmpty()) {
                if (!dgd4.metadata.isEmpty()) {
                    for (final String string6 : dgd3.metadata.keySet()) {
                        if (!string6.contains("Uploaded") && dgd4.metadata.containsKey(string6)) {
                            if (((String)dgd3.metadata.get(string6)).equals(dgd4.metadata.get(string6))) {
                                continue;
                            }
                            this.addToChangeList(dgd3, string6);
                        }
                        else {
                            this.addToChangeList(dgd3, string6);
                        }
                    }
                }
            }
        }
    }
    
    private void addToChangeList(final Backup dgd, final String string) {
        if (string.contains("Uploaded")) {
            final String string2 = DateFormat.getDateTimeInstance(3, 3).format(dgd.lastModifiedDate);
            dgd.changeList.put(string, string2);
            dgd.setUploadedVersion(true);
        }
        else {
            dgd.changeList.put(string, dgd.metadata.get(string));
        }
    }
    
    private void updateButtonStates() {
        this.restoreButton.visible = this.shouldRestoreButtonBeVisible();
        this.changesButton.visible = this.shouldChangesButtonBeVisible();
    }
    
    private boolean shouldChangesButtonBeVisible() {
        return this.selectedBackup != -1 && !((Backup)this.backups.get(this.selectedBackup)).changeList.isEmpty();
    }
    
    private boolean shouldRestoreButtonBeVisible() {
        return this.selectedBackup != -1 && !this.serverData.expired;
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.minecraft.setScreen(this.lastScreen);
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    private void restoreClicked(final int integer) {
        if (integer >= 0 && integer < this.backups.size() && !this.serverData.expired) {
            this.selectedBackup = integer;
            final Date date3 = ((Backup)this.backups.get(integer)).lastModifiedDate;
            final String string4 = DateFormat.getDateTimeInstance(3, 3).format(date3);
            final String string5 = RealmsUtil.convertToAgePresentationFromInstant(date3);
            final Component nr6 = new TranslatableComponent("mco.configure.world.restore.question.line1", new Object[] { string4, string5 });
            final Component nr7 = new TranslatableComponent("mco.configure.world.restore.question.line2");
            this.minecraft.setScreen(new RealmsLongConfirmationScreen(boolean1 -> {
                if (boolean1) {
                    this.restore();
                }
                else {
                    this.selectedBackup = -1;
                    this.minecraft.setScreen(this);
                }
            }, RealmsLongConfirmationScreen.Type.Warning, nr6, nr7, true));
        }
    }
    
    private void downloadClicked() {
        final Component nr2 = new TranslatableComponent("mco.configure.world.restore.download.question.line1");
        final Component nr3 = new TranslatableComponent("mco.configure.world.restore.download.question.line2");
        this.minecraft.setScreen(new RealmsLongConfirmationScreen(boolean1 -> {
            if (boolean1) {
                this.downloadWorldData();
            }
            else {
                this.minecraft.setScreen(this);
            }
        }, RealmsLongConfirmationScreen.Type.Info, nr2, nr3, true));
    }
    
    private void downloadWorldData() {
        this.minecraft.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen.getNewScreen(), new DownloadTask(this.serverData.id, this.slotId, this.serverData.name + " (" + ((RealmsWorldOptions)this.serverData.slots.get(this.serverData.activeSlot)).getSlotName(this.serverData.activeSlot) + ")", this)));
    }
    
    private void restore() {
        final Backup dgd2 = (Backup)this.backups.get(this.selectedBackup);
        this.selectedBackup = -1;
        this.minecraft.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen.getNewScreen(), new RestoreTask(dgd2, this.serverData.id, this.lastScreen)));
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.toolTip = null;
        this.renderBackground(dfj);
        this.backupObjectSelectionList.render(dfj, integer2, integer3, float4);
        this.titleLabel.render(this, dfj);
        this.font.draw(dfj, RealmsBackupScreen.TITLE, (float)((this.width - 150) / 2 - 90), 20.0f, 10526880);
        if (this.noBackups) {
            this.font.draw(dfj, RealmsBackupScreen.NO_BACKUPS_LABEL, 20.0f, (float)(this.height / 2 - 10), 16777215);
        }
        this.downloadButton.active = !this.noBackups;
        super.render(dfj, integer2, integer3, float4);
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(dfj, this.toolTip, integer2, integer3);
        }
    }
    
    protected void renderMousehoverTooltip(final PoseStack dfj, @Nullable final Component nr, final int integer3, final int integer4) {
        if (nr == null) {
            return;
        }
        final int integer5 = integer3 + 12;
        final int integer6 = integer4 - 12;
        final int integer7 = this.font.width(nr);
        this.fillGradient(dfj, integer5 - 3, integer6 - 3, integer5 + integer7 + 3, integer6 + 8 + 3, -1073741824, -1073741824);
        this.font.drawShadow(dfj, nr, (float)integer5, (float)integer6, 16777215);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        PLUS_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/plus_icon.png");
        RESTORE_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/restore_icon.png");
        RESTORE_TOOLTIP = new TranslatableComponent("mco.backup.button.restore");
        HAS_CHANGES_TOOLTIP = new TranslatableComponent("mco.backup.changes.tooltip");
        TITLE = new TranslatableComponent("mco.configure.world.backup");
        NO_BACKUPS_LABEL = new TranslatableComponent("mco.backup.nobackups");
        RealmsBackupScreen.lastScrollPosition = -1;
    }
    
    class BackupObjectSelectionList extends RealmsObjectSelectionList<RealmsBackupScreen.Entry> {
        public BackupObjectSelectionList() {
            super(RealmsBackupScreen.this.width - 150, RealmsBackupScreen.this.height, 32, RealmsBackupScreen.this.height - 15, 36);
        }
        
        public void addEntry(final Backup dgd) {
            this.addEntry(new RealmsBackupScreen.Entry(dgd));
        }
        
        @Override
        public int getRowWidth() {
            return (int)(this.width * 0.93);
        }
        
        public boolean isFocused() {
            return RealmsBackupScreen.this.getFocused() == this;
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 36;
        }
        
        public void renderBackground(final PoseStack dfj) {
            RealmsBackupScreen.this.renderBackground(dfj);
        }
        
        @Override
        public boolean mouseClicked(final double double1, final double double2, final int integer) {
            if (integer != 0) {
                return false;
            }
            if (double1 < this.getScrollbarPosition() && double2 >= this.y0 && double2 <= this.y1) {
                final int integer2 = this.width / 2 - 92;
                final int integer3 = this.width;
                final int integer4 = (int)Math.floor(double2 - this.y0) - this.headerHeight + (int)this.getScrollAmount();
                final int integer5 = integer4 / this.itemHeight;
                if (double1 >= integer2 && double1 <= integer3 && integer5 >= 0 && integer4 >= 0 && integer5 < this.getItemCount()) {
                    this.selectItem(integer5);
                    this.itemClicked(integer4, integer5, double1, double2, this.width);
                }
                return true;
            }
            return false;
        }
        
        @Override
        public int getScrollbarPosition() {
            return this.width - 5;
        }
        
        @Override
        public void itemClicked(final int integer1, final int integer2, final double double3, final double double4, final int integer5) {
            final int integer6 = this.width - 35;
            final int integer7 = integer2 * this.itemHeight + 36 - (int)this.getScrollAmount();
            final int integer8 = integer6 + 10;
            final int integer9 = integer7 - 3;
            if (double3 >= integer6 && double3 <= integer6 + 9 && double4 >= integer7 && double4 <= integer7 + 9) {
                if (!((Backup)RealmsBackupScreen.this.backups.get(integer2)).changeList.isEmpty()) {
                    RealmsBackupScreen.this.selectedBackup = -1;
                    RealmsBackupScreen.lastScrollPosition = (int)this.getScrollAmount();
                    this.minecraft.setScreen(new RealmsBackupInfoScreen(RealmsBackupScreen.this, (Backup)RealmsBackupScreen.this.backups.get(integer2)));
                }
            }
            else if (double3 >= integer8 && double3 < integer8 + 13 && double4 >= integer9 && double4 < integer9 + 15) {
                RealmsBackupScreen.lastScrollPosition = (int)this.getScrollAmount();
                RealmsBackupScreen.this.restoreClicked(integer2);
            }
        }
        
        @Override
        public void selectItem(final int integer) {
            this.setSelectedItem(integer);
            if (integer != -1) {
                NarrationHelper.now(I18n.get("narrator.select", ((Backup)RealmsBackupScreen.this.backups.get(integer)).lastModifiedDate.toString()));
            }
            this.selectInviteListItem(integer);
        }
        
        public void selectInviteListItem(final int integer) {
            RealmsBackupScreen.this.selectedBackup = integer;
            RealmsBackupScreen.this.updateButtonStates();
        }
        
        @Override
        public void setSelected(@Nullable final RealmsBackupScreen.Entry b) {
            super.setSelected(b);
            RealmsBackupScreen.this.selectedBackup = this.children().indexOf(b);
            RealmsBackupScreen.this.updateButtonStates();
        }
    }
    
    class Entry extends ObjectSelectionList.Entry<Entry> {
        private final Backup backup;
        
        public Entry(final Backup dgd) {
            this.backup = dgd;
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            this.renderBackupItem(dfj, this.backup, integer4 - 40, integer3, integer7, integer8);
        }
        
        private void renderBackupItem(final PoseStack dfj, final Backup dgd, final int integer3, final int integer4, final int integer5, final int integer6) {
            final int integer7 = dgd.isUploadedVersion() ? -8388737 : 16777215;
            RealmsBackupScreen.this.font.draw(dfj, "Backup (" + RealmsUtil.convertToAgePresentationFromInstant(dgd.lastModifiedDate) + ")", (float)(integer3 + 40), (float)(integer4 + 1), integer7);
            RealmsBackupScreen.this.font.draw(dfj, this.getMediumDatePresentation(dgd.lastModifiedDate), (float)(integer3 + 40), (float)(integer4 + 12), 5000268);
            final int integer8 = RealmsBackupScreen.this.width - 175;
            final int integer9 = -3;
            final int integer10 = integer8 - 10;
            final int integer11 = 0;
            if (!RealmsBackupScreen.this.serverData.expired) {
                this.drawRestore(dfj, integer8, integer4 - 3, integer5, integer6);
            }
            if (!dgd.changeList.isEmpty()) {
                this.drawInfo(dfj, integer10, integer4 + 0, integer5, integer6);
            }
        }
        
        private String getMediumDatePresentation(final Date date) {
            return DateFormat.getDateTimeInstance(3, 3).format(date);
        }
        
        private void drawRestore(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
            final boolean boolean7 = integer4 >= integer2 && integer4 <= integer2 + 12 && integer5 >= integer3 && integer5 <= integer3 + 14 && integer5 < RealmsBackupScreen.this.height - 15 && integer5 > 32;
            RealmsBackupScreen.this.minecraft.getTextureManager().bind(RealmsBackupScreen.RESTORE_ICON_LOCATION);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.pushMatrix();
            RenderSystem.scalef(0.5f, 0.5f, 0.5f);
            final float float8 = boolean7 ? 28.0f : 0.0f;
            GuiComponent.blit(dfj, integer2 * 2, integer3 * 2, 0.0f, float8, 23, 28, 23, 56);
            RenderSystem.popMatrix();
            if (boolean7) {
                RealmsBackupScreen.this.toolTip = RealmsBackupScreen.RESTORE_TOOLTIP;
            }
        }
        
        private void drawInfo(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
            final boolean boolean7 = integer4 >= integer2 && integer4 <= integer2 + 8 && integer5 >= integer3 && integer5 <= integer3 + 8 && integer5 < RealmsBackupScreen.this.height - 15 && integer5 > 32;
            RealmsBackupScreen.this.minecraft.getTextureManager().bind(RealmsBackupScreen.PLUS_ICON_LOCATION);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.pushMatrix();
            RenderSystem.scalef(0.5f, 0.5f, 0.5f);
            final float float8 = boolean7 ? 15.0f : 0.0f;
            GuiComponent.blit(dfj, integer2 * 2, integer3 * 2, 0.0f, float8, 15, 15, 15, 30);
            RenderSystem.popMatrix();
            if (boolean7) {
                RealmsBackupScreen.this.toolTip = RealmsBackupScreen.HAS_CHANGES_TOOLTIP;
            }
        }
    }
}
