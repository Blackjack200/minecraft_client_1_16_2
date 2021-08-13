package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.util.RealmsTextureManager;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.realms.NarrationHelper;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.realms.RealmsObjectSelectionList;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.FormattedText;
import javax.annotation.Nullable;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TextComponent;
import com.mojang.realmsclient.dto.Ops;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import java.util.Iterator;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.realmsclient.dto.PlayerInfo;
import net.minecraft.realms.RealmsLabel;
import net.minecraft.client.gui.components.Button;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsPlayerScreen extends RealmsScreen {
    private static final Logger LOGGER;
    private static final ResourceLocation OP_ICON_LOCATION;
    private static final ResourceLocation USER_ICON_LOCATION;
    private static final ResourceLocation CROSS_ICON_LOCATION;
    private static final ResourceLocation OPTIONS_BACKGROUND;
    private static final Component NORMAL_USER_TOOLTIP;
    private static final Component OP_TOOLTIP;
    private static final Component REMOVE_ENTRY_TOOLTIP;
    private static final Component INVITED_LABEL;
    private Component toolTip;
    private final RealmsConfigureWorldScreen lastScreen;
    private final RealmsServer serverData;
    private InvitedObjectSelectionList invitedObjectSelectionList;
    private int column1X;
    private int columnWidth;
    private int column2X;
    private Button removeButton;
    private Button opdeopButton;
    private int selectedInvitedIndex;
    private String selectedInvited;
    private int player;
    private boolean stateChanged;
    private RealmsLabel titleLabel;
    private UserAction hoveredUserAction;
    
    public RealmsPlayerScreen(final RealmsConfigureWorldScreen dhp, final RealmsServer dgn) {
        this.selectedInvitedIndex = -1;
        this.player = -1;
        this.hoveredUserAction = UserAction.NONE;
        this.lastScreen = dhp;
        this.serverData = dgn;
    }
    
    public void init() {
        this.column1X = this.width / 2 - 160;
        this.columnWidth = 150;
        this.column2X = this.width / 2 + 12;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        (this.invitedObjectSelectionList = new InvitedObjectSelectionList()).setLeftPos(this.column1X);
        this.<InvitedObjectSelectionList>addWidget(this.invitedObjectSelectionList);
        for (final PlayerInfo dgk3 : this.serverData.players) {
            this.invitedObjectSelectionList.addEntry(dgk3);
        }
        this.<Button>addButton(new Button(this.column2X, RealmsScreen.row(1), this.columnWidth + 10, 20, new TranslatableComponent("mco.configure.world.buttons.invite"), dlg -> this.minecraft.setScreen(new RealmsInviteScreen(this.lastScreen, this, this.serverData))));
        this.removeButton = this.<Button>addButton(new Button(this.column2X, RealmsScreen.row(7), this.columnWidth + 10, 20, new TranslatableComponent("mco.configure.world.invites.remove.tooltip"), dlg -> this.uninvite(this.player)));
        this.opdeopButton = this.<Button>addButton(new Button(this.column2X, RealmsScreen.row(9), this.columnWidth + 10, 20, new TranslatableComponent("mco.configure.world.invites.ops.tooltip"), dlg -> {
            if (((PlayerInfo)this.serverData.players.get(this.player)).isOperator()) {
                this.deop(this.player);
            }
            else {
                this.op(this.player);
            }
            return;
        }));
        this.<Button>addButton(new Button(this.column2X + this.columnWidth / 2 + 2, RealmsScreen.row(12), this.columnWidth / 2 + 10 - 2, 20, CommonComponents.GUI_BACK, dlg -> this.backButtonClicked()));
        this.titleLabel = this.<RealmsLabel>addWidget(new RealmsLabel(new TranslatableComponent("mco.configure.world.players.title"), this.width / 2, 17, 16777215));
        this.narrateLabels();
        this.updateButtonStates();
    }
    
    private void updateButtonStates() {
        this.removeButton.visible = this.shouldRemoveAndOpdeopButtonBeVisible(this.player);
        this.opdeopButton.visible = this.shouldRemoveAndOpdeopButtonBeVisible(this.player);
    }
    
    private boolean shouldRemoveAndOpdeopButtonBeVisible(final int integer) {
        return integer != -1;
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
            this.minecraft.setScreen(this.lastScreen.getNewScreen());
        }
        else {
            this.minecraft.setScreen(this.lastScreen);
        }
    }
    
    private void op(final int integer) {
        this.updateButtonStates();
        final RealmsClient dfy3 = RealmsClient.create();
        final String string4 = ((PlayerInfo)this.serverData.players.get(integer)).getUuid();
        try {
            this.updateOps(dfy3.op(this.serverData.id, string4));
        }
        catch (RealmsServiceException dhf5) {
            RealmsPlayerScreen.LOGGER.error("Couldn't op the user");
        }
    }
    
    private void deop(final int integer) {
        this.updateButtonStates();
        final RealmsClient dfy3 = RealmsClient.create();
        final String string4 = ((PlayerInfo)this.serverData.players.get(integer)).getUuid();
        try {
            this.updateOps(dfy3.deop(this.serverData.id, string4));
        }
        catch (RealmsServiceException dhf5) {
            RealmsPlayerScreen.LOGGER.error("Couldn't deop the user");
        }
    }
    
    private void updateOps(final Ops dgg) {
        for (final PlayerInfo dgk4 : this.serverData.players) {
            dgk4.setOperator(dgg.ops.contains(dgk4.getName()));
        }
    }
    
    private void uninvite(final int integer) {
        this.updateButtonStates();
        if (integer >= 0 && integer < this.serverData.players.size()) {
            final PlayerInfo dgk3 = (PlayerInfo)this.serverData.players.get(integer);
            this.selectedInvited = dgk3.getUuid();
            this.selectedInvitedIndex = integer;
            final RealmsConfirmScreen dhq4 = new RealmsConfirmScreen(boolean1 -> {
                if (boolean1) {
                    final RealmsClient dfy3 = RealmsClient.create();
                    try {
                        dfy3.uninvite(this.serverData.id, this.selectedInvited);
                    }
                    catch (RealmsServiceException dhf4) {
                        RealmsPlayerScreen.LOGGER.error("Couldn't uninvite user");
                    }
                    this.deleteFromInvitedList(this.selectedInvitedIndex);
                    this.player = -1;
                    this.updateButtonStates();
                }
                this.stateChanged = true;
                this.minecraft.setScreen(this);
            }, new TextComponent("Question"), new TranslatableComponent("mco.configure.world.uninvite.question").append(" '").append(dgk3.getName()).append("' ?"));
            this.minecraft.setScreen(dhq4);
        }
    }
    
    private void deleteFromInvitedList(final int integer) {
        this.serverData.players.remove(integer);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.toolTip = null;
        this.hoveredUserAction = UserAction.NONE;
        this.renderBackground(dfj);
        if (this.invitedObjectSelectionList != null) {
            this.invitedObjectSelectionList.render(dfj, integer2, integer3, float4);
        }
        final int integer4 = RealmsScreen.row(12) + 20;
        final Tesselator dfl7 = Tesselator.getInstance();
        final BufferBuilder dfe8 = dfl7.getBuilder();
        this.minecraft.getTextureManager().bind(RealmsPlayerScreen.OPTIONS_BACKGROUND);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final float float5 = 32.0f;
        dfe8.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR);
        dfe8.vertex(0.0, this.height, 0.0).uv(0.0f, (this.height - integer4) / 32.0f + 0.0f).color(64, 64, 64, 255).endVertex();
        dfe8.vertex(this.width, this.height, 0.0).uv(this.width / 32.0f, (this.height - integer4) / 32.0f + 0.0f).color(64, 64, 64, 255).endVertex();
        dfe8.vertex(this.width, integer4, 0.0).uv(this.width / 32.0f, 0.0f).color(64, 64, 64, 255).endVertex();
        dfe8.vertex(0.0, integer4, 0.0).uv(0.0f, 0.0f).color(64, 64, 64, 255).endVertex();
        dfl7.end();
        this.titleLabel.render(this, dfj);
        if (this.serverData != null && this.serverData.players != null) {
            this.font.draw(dfj, new TextComponent("").append(RealmsPlayerScreen.INVITED_LABEL).append(" (").append(Integer.toString(this.serverData.players.size())).append(")"), (float)this.column1X, (float)RealmsScreen.row(0), 10526880);
        }
        else {
            this.font.draw(dfj, RealmsPlayerScreen.INVITED_LABEL, (float)this.column1X, (float)RealmsScreen.row(0), 10526880);
        }
        super.render(dfj, integer2, integer3, float4);
        if (this.serverData == null) {
            return;
        }
        this.renderMousehoverTooltip(dfj, this.toolTip, integer2, integer3);
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
    
    private void drawRemoveIcon(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        final boolean boolean7 = integer4 >= integer2 && integer4 <= integer2 + 9 && integer5 >= integer3 && integer5 <= integer3 + 9 && integer5 < RealmsScreen.row(12) + 20 && integer5 > RealmsScreen.row(1);
        this.minecraft.getTextureManager().bind(RealmsPlayerScreen.CROSS_ICON_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final float float8 = boolean7 ? 7.0f : 0.0f;
        GuiComponent.blit(dfj, integer2, integer3, 0.0f, float8, 8, 7, 8, 14);
        if (boolean7) {
            this.toolTip = RealmsPlayerScreen.REMOVE_ENTRY_TOOLTIP;
            this.hoveredUserAction = UserAction.REMOVE;
        }
    }
    
    private void drawOpped(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        final boolean boolean7 = integer4 >= integer2 && integer4 <= integer2 + 9 && integer5 >= integer3 && integer5 <= integer3 + 9 && integer5 < RealmsScreen.row(12) + 20 && integer5 > RealmsScreen.row(1);
        this.minecraft.getTextureManager().bind(RealmsPlayerScreen.OP_ICON_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final float float8 = boolean7 ? 8.0f : 0.0f;
        GuiComponent.blit(dfj, integer2, integer3, 0.0f, float8, 8, 8, 8, 16);
        if (boolean7) {
            this.toolTip = RealmsPlayerScreen.OP_TOOLTIP;
            this.hoveredUserAction = UserAction.TOGGLE_OP;
        }
    }
    
    private void drawNormal(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        final boolean boolean7 = integer4 >= integer2 && integer4 <= integer2 + 9 && integer5 >= integer3 && integer5 <= integer3 + 9 && integer5 < RealmsScreen.row(12) + 20 && integer5 > RealmsScreen.row(1);
        this.minecraft.getTextureManager().bind(RealmsPlayerScreen.USER_ICON_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final float float8 = boolean7 ? 8.0f : 0.0f;
        GuiComponent.blit(dfj, integer2, integer3, 0.0f, float8, 8, 8, 8, 16);
        if (boolean7) {
            this.toolTip = RealmsPlayerScreen.NORMAL_USER_TOOLTIP;
            this.hoveredUserAction = UserAction.TOGGLE_OP;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        OP_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/op_icon.png");
        USER_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/user_icon.png");
        CROSS_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/cross_player_icon.png");
        OPTIONS_BACKGROUND = new ResourceLocation("minecraft", "textures/gui/options_background.png");
        NORMAL_USER_TOOLTIP = new TranslatableComponent("mco.configure.world.invites.normal.tooltip");
        OP_TOOLTIP = new TranslatableComponent("mco.configure.world.invites.ops.tooltip");
        REMOVE_ENTRY_TOOLTIP = new TranslatableComponent("mco.configure.world.invites.remove.tooltip");
        INVITED_LABEL = new TranslatableComponent("mco.configure.world.invited");
    }
    
    enum UserAction {
        TOGGLE_OP, 
        REMOVE, 
        NONE;
    }
    
    class InvitedObjectSelectionList extends RealmsObjectSelectionList<RealmsPlayerScreen.Entry> {
        public InvitedObjectSelectionList() {
            super(RealmsPlayerScreen.this.columnWidth + 10, RealmsScreen.row(12) + 20, RealmsScreen.row(1), RealmsScreen.row(12) + 20, 13);
        }
        
        public void addEntry(final PlayerInfo dgk) {
            this.addEntry(new RealmsPlayerScreen.Entry(dgk));
        }
        
        @Override
        public int getRowWidth() {
            return (int)(this.width * 1.0);
        }
        
        public boolean isFocused() {
            return RealmsPlayerScreen.this.getFocused() == this;
        }
        
        @Override
        public boolean mouseClicked(final double double1, final double double2, final int integer) {
            if (integer == 0 && double1 < this.getScrollbarPosition() && double2 >= this.y0 && double2 <= this.y1) {
                final int integer2 = RealmsPlayerScreen.this.column1X;
                final int integer3 = RealmsPlayerScreen.this.column1X + RealmsPlayerScreen.this.columnWidth;
                final int integer4 = (int)Math.floor(double2 - this.y0) - this.headerHeight + (int)this.getScrollAmount() - 4;
                final int integer5 = integer4 / this.itemHeight;
                if (double1 >= integer2 && double1 <= integer3 && integer5 >= 0 && integer4 >= 0 && integer5 < this.getItemCount()) {
                    this.selectItem(integer5);
                    this.itemClicked(integer4, integer5, double1, double2, this.width);
                }
                return true;
            }
            return super.mouseClicked(double1, double2, integer);
        }
        
        @Override
        public void itemClicked(final int integer1, final int integer2, final double double3, final double double4, final int integer5) {
            if (integer2 < 0 || integer2 > RealmsPlayerScreen.this.serverData.players.size() || RealmsPlayerScreen.this.hoveredUserAction == UserAction.NONE) {
                return;
            }
            if (RealmsPlayerScreen.this.hoveredUserAction == UserAction.TOGGLE_OP) {
                if (((PlayerInfo)RealmsPlayerScreen.this.serverData.players.get(integer2)).isOperator()) {
                    RealmsPlayerScreen.this.deop(integer2);
                }
                else {
                    RealmsPlayerScreen.this.op(integer2);
                }
            }
            else if (RealmsPlayerScreen.this.hoveredUserAction == UserAction.REMOVE) {
                RealmsPlayerScreen.this.uninvite(integer2);
            }
        }
        
        @Override
        public void selectItem(final int integer) {
            this.setSelectedItem(integer);
            if (integer != -1) {
                NarrationHelper.now(I18n.get("narrator.select", ((PlayerInfo)RealmsPlayerScreen.this.serverData.players.get(integer)).getName()));
            }
            this.selectInviteListItem(integer);
        }
        
        public void selectInviteListItem(final int integer) {
            RealmsPlayerScreen.this.player = integer;
            RealmsPlayerScreen.this.updateButtonStates();
        }
        
        @Override
        public void setSelected(@Nullable final RealmsPlayerScreen.Entry a) {
            super.setSelected(a);
            RealmsPlayerScreen.this.player = this.children().indexOf(a);
            RealmsPlayerScreen.this.updateButtonStates();
        }
        
        public void renderBackground(final PoseStack dfj) {
            RealmsPlayerScreen.this.renderBackground(dfj);
        }
        
        @Override
        public int getScrollbarPosition() {
            return RealmsPlayerScreen.this.column1X + this.width - 5;
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 13;
        }
    }
    
    class Entry extends ObjectSelectionList.Entry<Entry> {
        private final PlayerInfo playerInfo;
        
        public Entry(final PlayerInfo dgk) {
            this.playerInfo = dgk;
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            this.renderInvitedItem(dfj, this.playerInfo, integer4, integer3, integer7, integer8);
        }
        
        private void renderInvitedItem(final PoseStack dfj, final PlayerInfo dgk, final int integer3, final int integer4, final int integer5, final int integer6) {
            int integer7;
            if (!dgk.getAccepted()) {
                integer7 = 10526880;
            }
            else if (dgk.getOnline()) {
                integer7 = 8388479;
            }
            else {
                integer7 = 16777215;
            }
            RealmsPlayerScreen.this.font.draw(dfj, dgk.getName(), (float)(RealmsPlayerScreen.this.column1X + 3 + 12), (float)(integer4 + 1), integer7);
            if (dgk.isOperator()) {
                RealmsPlayerScreen.this.drawOpped(dfj, RealmsPlayerScreen.this.column1X + RealmsPlayerScreen.this.columnWidth - 10, integer4 + 1, integer5, integer6);
            }
            else {
                RealmsPlayerScreen.this.drawNormal(dfj, RealmsPlayerScreen.this.column1X + RealmsPlayerScreen.this.columnWidth - 10, integer4 + 1, integer5, integer6);
            }
            RealmsPlayerScreen.this.drawRemoveIcon(dfj, RealmsPlayerScreen.this.column1X + RealmsPlayerScreen.this.columnWidth - 22, integer4 + 2, integer5, integer6);
            RealmsTextureManager.withBoundFace(dgk.getUuid(), () -> {
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                GuiComponent.blit(dfj, RealmsPlayerScreen.this.column1X + 2 + 2, integer4 + 1, 8, 8, 8.0f, 8.0f, 8, 8, 64, 64);
                GuiComponent.blit(dfj, RealmsPlayerScreen.this.column1X + 2 + 2, integer4 + 1, 8, 8, 40.0f, 8.0f, 8, 8, 64, 64);
            });
        }
    }
}
