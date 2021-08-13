package com.mojang.realmsclient.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.realmsclient.util.RealmsTextureManager;
import com.mojang.realmsclient.gui.RowButton;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.realms.NarrationHelper;
import java.util.Arrays;
import com.mojang.realmsclient.util.RealmsUtil;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.realms.RealmsObjectSelectionList;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Collection;
import com.mojang.realmsclient.dto.PendingInvite;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.util.stream.Collectors;
import java.util.List;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.realms.RealmsLabel;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsPendingInvitesScreen extends RealmsScreen {
    private static final Logger LOGGER;
    private static final ResourceLocation ACCEPT_ICON_LOCATION;
    private static final ResourceLocation REJECT_ICON_LOCATION;
    private static final Component NO_PENDING_INVITES_TEXT;
    private static final Component ACCEPT_INVITE_TOOLTIP;
    private static final Component REJECT_INVITE_TOOLTIP;
    private final Screen lastScreen;
    @Nullable
    private Component toolTip;
    private boolean loaded;
    private PendingInvitationSelectionList pendingInvitationSelectionList;
    private RealmsLabel titleLabel;
    private int selectedInvite;
    private Button acceptButton;
    private Button rejectButton;
    
    public RealmsPendingInvitesScreen(final Screen doq) {
        this.selectedInvite = -1;
        this.lastScreen = doq;
    }
    
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.pendingInvitationSelectionList = new PendingInvitationSelectionList();
        new Thread("Realms-pending-invitations-fetcher") {
            public void run() {
                final RealmsClient dfy2 = RealmsClient.create();
                try {
                    final List<PendingInvite> list3 = dfy2.pendingInvites().pendingInvites;
                    final List<Entry> list4 = (List<Entry>)list3.stream().map(dgh -> new Entry(dgh)).collect(Collectors.toList());
                    RealmsPendingInvitesScreen.this.minecraft.execute(() -> RealmsPendingInvitesScreen.this.pendingInvitationSelectionList.replaceEntries((java.util.Collection<Entry>)list4));
                }
                catch (RealmsServiceException dhf3) {
                    RealmsPendingInvitesScreen.LOGGER.error("Couldn't list invites");
                }
                finally {
                    RealmsPendingInvitesScreen.this.loaded = true;
                }
            }
        }.start();
        this.<PendingInvitationSelectionList>addWidget(this.pendingInvitationSelectionList);
        this.acceptButton = this.<Button>addButton(new Button(this.width / 2 - 174, this.height - 32, 100, 20, new TranslatableComponent("mco.invites.button.accept"), dlg -> {
            this.accept(this.selectedInvite);
            this.selectedInvite = -1;
            this.updateButtonStates();
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 - 50, this.height - 32, 100, 20, CommonComponents.GUI_DONE, dlg -> this.minecraft.setScreen(new RealmsMainScreen(this.lastScreen))));
        this.rejectButton = this.<Button>addButton(new Button(this.width / 2 + 74, this.height - 32, 100, 20, new TranslatableComponent("mco.invites.button.reject"), dlg -> {
            this.reject(this.selectedInvite);
            this.selectedInvite = -1;
            this.updateButtonStates();
            return;
        }));
        this.<RealmsLabel>addWidget(this.titleLabel = new RealmsLabel(new TranslatableComponent("mco.invites.title"), this.width / 2, 12, 16777215));
        this.narrateLabels();
        this.updateButtonStates();
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.minecraft.setScreen(new RealmsMainScreen(this.lastScreen));
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    private void updateList(final int integer) {
        this.pendingInvitationSelectionList.removeAtIndex(integer);
    }
    
    private void reject(final int integer) {
        if (integer < this.pendingInvitationSelectionList.getItemCount()) {
            new Thread("Realms-reject-invitation") {
                public void run() {
                    try {
                        final RealmsClient dfy2 = RealmsClient.create();
                        dfy2.rejectInvitation(((Entry)RealmsPendingInvitesScreen.this.pendingInvitationSelectionList.children().get(integer)).pendingInvite.invitationId);
                        RealmsPendingInvitesScreen.this.minecraft.execute(() -> RealmsPendingInvitesScreen.this.updateList(integer));
                    }
                    catch (RealmsServiceException dhf2) {
                        RealmsPendingInvitesScreen.LOGGER.error("Couldn't reject invite");
                    }
                }
            }.start();
        }
    }
    
    private void accept(final int integer) {
        if (integer < this.pendingInvitationSelectionList.getItemCount()) {
            new Thread("Realms-accept-invitation") {
                public void run() {
                    try {
                        final RealmsClient dfy2 = RealmsClient.create();
                        dfy2.acceptInvitation(((Entry)RealmsPendingInvitesScreen.this.pendingInvitationSelectionList.children().get(integer)).pendingInvite.invitationId);
                        RealmsPendingInvitesScreen.this.minecraft.execute(() -> RealmsPendingInvitesScreen.this.updateList(integer));
                    }
                    catch (RealmsServiceException dhf2) {
                        RealmsPendingInvitesScreen.LOGGER.error("Couldn't accept invite");
                    }
                }
            }.start();
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.toolTip = null;
        this.renderBackground(dfj);
        this.pendingInvitationSelectionList.render(dfj, integer2, integer3, float4);
        this.titleLabel.render(this, dfj);
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(dfj, this.toolTip, integer2, integer3);
        }
        if (this.pendingInvitationSelectionList.getItemCount() == 0 && this.loaded) {
            GuiComponent.drawCenteredString(dfj, this.font, RealmsPendingInvitesScreen.NO_PENDING_INVITES_TEXT, this.width / 2, this.height / 2 - 20, 16777215);
        }
        super.render(dfj, integer2, integer3, float4);
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
    
    private void updateButtonStates() {
        this.acceptButton.visible = this.shouldAcceptAndRejectButtonBeVisible(this.selectedInvite);
        this.rejectButton.visible = this.shouldAcceptAndRejectButtonBeVisible(this.selectedInvite);
    }
    
    private boolean shouldAcceptAndRejectButtonBeVisible(final int integer) {
        return integer != -1;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ACCEPT_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/accept_icon.png");
        REJECT_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/reject_icon.png");
        NO_PENDING_INVITES_TEXT = new TranslatableComponent("mco.invites.nopending");
        ACCEPT_INVITE_TOOLTIP = new TranslatableComponent("mco.invites.button.accept");
        REJECT_INVITE_TOOLTIP = new TranslatableComponent("mco.invites.button.reject");
    }
    
    class PendingInvitationSelectionList extends RealmsObjectSelectionList<RealmsPendingInvitesScreen.Entry> {
        public PendingInvitationSelectionList() {
            super(RealmsPendingInvitesScreen.this.width, RealmsPendingInvitesScreen.this.height, 32, RealmsPendingInvitesScreen.this.height - 40, 36);
        }
        
        public void removeAtIndex(final int integer) {
            this.remove(integer);
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 36;
        }
        
        @Override
        public int getRowWidth() {
            return 260;
        }
        
        public boolean isFocused() {
            return RealmsPendingInvitesScreen.this.getFocused() == this;
        }
        
        public void renderBackground(final PoseStack dfj) {
            RealmsPendingInvitesScreen.this.renderBackground(dfj);
        }
        
        @Override
        public void selectItem(final int integer) {
            this.setSelectedItem(integer);
            if (integer != -1) {
                final List<RealmsPendingInvitesScreen.Entry> list3 = RealmsPendingInvitesScreen.this.pendingInvitationSelectionList.children();
                final PendingInvite dgh4 = ((RealmsPendingInvitesScreen.Entry)list3.get(integer)).pendingInvite;
                final String string5 = I18n.get("narrator.select.list.position", integer + 1, list3.size());
                final String string6 = NarrationHelper.join((Iterable<String>)Arrays.asList((Object[])new String[] { dgh4.worldName, dgh4.worldOwnerName, RealmsUtil.convertToAgePresentationFromInstant(dgh4.date), string5 }));
                NarrationHelper.now(I18n.get("narrator.select", string6));
            }
            this.selectInviteListItem(integer);
        }
        
        public void selectInviteListItem(final int integer) {
            RealmsPendingInvitesScreen.this.selectedInvite = integer;
            RealmsPendingInvitesScreen.this.updateButtonStates();
        }
        
        @Override
        public void setSelected(@Nullable final RealmsPendingInvitesScreen.Entry a) {
            super.setSelected(a);
            RealmsPendingInvitesScreen.this.selectedInvite = this.children().indexOf(a);
            RealmsPendingInvitesScreen.this.updateButtonStates();
        }
    }
    
    class Entry extends ObjectSelectionList.Entry<Entry> {
        private final PendingInvite pendingInvite;
        private final List<RowButton> rowButtons;
        
        Entry(final PendingInvite dgh) {
            this.pendingInvite = dgh;
            this.rowButtons = (List<RowButton>)Arrays.asList((Object[])new RowButton[] { new AcceptRowButton(), new RejectRowButton() });
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            this.renderPendingInvitationItem(dfj, this.pendingInvite, integer4, integer3, integer7, integer8);
        }
        
        public boolean mouseClicked(final double double1, final double double2, final int integer) {
            RowButton.rowButtonMouseClicked(RealmsPendingInvitesScreen.this.pendingInvitationSelectionList, this, this.rowButtons, integer, double1, double2);
            return true;
        }
        
        private void renderPendingInvitationItem(final PoseStack dfj, final PendingInvite dgh, final int integer3, final int integer4, final int integer5, final int integer6) {
            RealmsPendingInvitesScreen.this.font.draw(dfj, dgh.worldName, (float)(integer3 + 38), (float)(integer4 + 1), 16777215);
            RealmsPendingInvitesScreen.this.font.draw(dfj, dgh.worldOwnerName, (float)(integer3 + 38), (float)(integer4 + 12), 7105644);
            RealmsPendingInvitesScreen.this.font.draw(dfj, RealmsUtil.convertToAgePresentationFromInstant(dgh.date), (float)(integer3 + 38), (float)(integer4 + 24), 7105644);
            RowButton.drawButtonsInRow(dfj, this.rowButtons, RealmsPendingInvitesScreen.this.pendingInvitationSelectionList, integer3, integer4, integer5, integer6);
            RealmsTextureManager.withBoundFace(dgh.worldOwnerUuid, () -> {
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                GuiComponent.blit(dfj, integer3, integer4, 32, 32, 8.0f, 8.0f, 8, 8, 64, 64);
                GuiComponent.blit(dfj, integer3, integer4, 32, 32, 40.0f, 8.0f, 8, 8, 64, 64);
            });
        }
        
        class AcceptRowButton extends RowButton {
            AcceptRowButton() {
                super(15, 15, 215, 5);
            }
            
            @Override
            protected void draw(final PoseStack dfj, final int integer2, final int integer3, final boolean boolean4) {
                RealmsPendingInvitesScreen.this.minecraft.getTextureManager().bind(RealmsPendingInvitesScreen.ACCEPT_ICON_LOCATION);
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                final float float6 = boolean4 ? 19.0f : 0.0f;
                GuiComponent.blit(dfj, integer2, integer3, float6, 0.0f, 18, 18, 37, 18);
                if (boolean4) {
                    RealmsPendingInvitesScreen.this.toolTip = RealmsPendingInvitesScreen.ACCEPT_INVITE_TOOLTIP;
                }
            }
            
            @Override
            public void onClick(final int integer) {
                RealmsPendingInvitesScreen.this.accept(integer);
            }
        }
        
        class RejectRowButton extends RowButton {
            RejectRowButton() {
                super(15, 15, 235, 5);
            }
            
            @Override
            protected void draw(final PoseStack dfj, final int integer2, final int integer3, final boolean boolean4) {
                RealmsPendingInvitesScreen.this.minecraft.getTextureManager().bind(RealmsPendingInvitesScreen.REJECT_ICON_LOCATION);
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                final float float6 = boolean4 ? 19.0f : 0.0f;
                GuiComponent.blit(dfj, integer2, integer3, float6, 0.0f, 18, 18, 37, 18);
                if (boolean4) {
                    RealmsPendingInvitesScreen.this.toolTip = RealmsPendingInvitesScreen.REJECT_INVITE_TOOLTIP;
                }
            }
            
            @Override
            public void onClick(final int integer) {
                RealmsPendingInvitesScreen.this.reject(integer);
            }
        }
    }
}
