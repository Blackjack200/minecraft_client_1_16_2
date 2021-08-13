package com.mojang.realmsclient.gui.screens;

import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.AbstractSelectionList;
import javax.annotation.Nullable;
import net.minecraft.realms.NarrationHelper;
import java.util.Arrays;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.realms.RealmsObjectSelectionList;
import java.text.SimpleDateFormat;
import net.minecraft.ChatFormatting;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.Font;
import java.util.Date;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import java.util.Iterator;
import java.util.stream.Collectors;
import com.google.common.collect.Lists;
import net.minecraft.realms.RealmsLabel;
import net.minecraft.world.level.storage.LevelSummary;
import java.util.List;
import net.minecraft.client.gui.components.Button;
import java.text.DateFormat;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsSelectFileToUploadScreen extends RealmsScreen {
    private static final Logger LOGGER;
    private static final Component WORLD_TEXT;
    private static final Component REQUIRES_CONVERSION_TEXT;
    private static final Component HARDCORE_TEXT;
    private static final Component CHEATS_TEXT;
    private static final DateFormat DATE_FORMAT;
    private final RealmsResetWorldScreen lastScreen;
    private final long worldId;
    private final int slotId;
    private Button uploadButton;
    private List<LevelSummary> levelList;
    private int selectedWorld;
    private WorldSelectionList worldSelectionList;
    private RealmsLabel titleLabel;
    private RealmsLabel subtitleLabel;
    private RealmsLabel noWorldsLabel;
    private final Runnable callback;
    
    public RealmsSelectFileToUploadScreen(final long long1, final int integer, final RealmsResetWorldScreen dic, final Runnable runnable) {
        this.levelList = (List<LevelSummary>)Lists.newArrayList();
        this.selectedWorld = -1;
        this.lastScreen = dic;
        this.worldId = long1;
        this.slotId = integer;
        this.callback = runnable;
    }
    
    private void loadLevelList() throws Exception {
        this.levelList = (List<LevelSummary>)this.minecraft.getLevelSource().getLevelList().stream().sorted((cye1, cye2) -> {
            if (cye1.getLastPlayed() < cye2.getLastPlayed()) {
                return 1;
            }
            if (cye1.getLastPlayed() > cye2.getLastPlayed()) {
                return -1;
            }
            return cye1.getLevelId().compareTo(cye2.getLevelId());
        }).collect(Collectors.toList());
        for (final LevelSummary cye3 : this.levelList) {
            this.worldSelectionList.addEntry(cye3);
        }
    }
    
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.worldSelectionList = new WorldSelectionList();
        try {
            this.loadLevelList();
        }
        catch (Exception exception2) {
            RealmsSelectFileToUploadScreen.LOGGER.error("Couldn't load level list", (Throwable)exception2);
            this.minecraft.setScreen(new RealmsGenericErrorScreen(new TextComponent("Unable to load worlds"), Component.nullToEmpty(exception2.getMessage()), this.lastScreen));
            return;
        }
        this.<WorldSelectionList>addWidget(this.worldSelectionList);
        this.uploadButton = this.<Button>addButton(new Button(this.width / 2 - 154, this.height - 32, 153, 20, new TranslatableComponent("mco.upload.button.name"), dlg -> this.upload()));
        this.uploadButton.active = (this.selectedWorld >= 0 && this.selectedWorld < this.levelList.size());
        this.<Button>addButton(new Button(this.width / 2 + 6, this.height - 32, 153, 20, CommonComponents.GUI_BACK, dlg -> this.minecraft.setScreen(this.lastScreen)));
        this.titleLabel = this.<RealmsLabel>addWidget(new RealmsLabel(new TranslatableComponent("mco.upload.select.world.title"), this.width / 2, 13, 16777215));
        this.subtitleLabel = this.<RealmsLabel>addWidget(new RealmsLabel(new TranslatableComponent("mco.upload.select.world.subtitle"), this.width / 2, RealmsScreen.row(-1), 10526880));
        if (this.levelList.isEmpty()) {
            this.noWorldsLabel = this.<RealmsLabel>addWidget(new RealmsLabel(new TranslatableComponent("mco.upload.select.world.none"), this.width / 2, this.height / 2 - 20, 16777215));
        }
        else {
            this.noWorldsLabel = null;
        }
        this.narrateLabels();
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    private void upload() {
        if (this.selectedWorld != -1 && !((LevelSummary)this.levelList.get(this.selectedWorld)).isHardcore()) {
            final LevelSummary cye2 = (LevelSummary)this.levelList.get(this.selectedWorld);
            this.minecraft.setScreen(new RealmsUploadScreen(this.worldId, this.slotId, this.lastScreen, cye2, this.callback));
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.worldSelectionList.render(dfj, integer2, integer3, float4);
        this.titleLabel.render(this, dfj);
        this.subtitleLabel.render(this, dfj);
        if (this.noWorldsLabel != null) {
            this.noWorldsLabel.render(this, dfj);
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.minecraft.setScreen(this.lastScreen);
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    private static Component gameModeName(final LevelSummary cye) {
        return cye.getGameMode().getDisplayName();
    }
    
    private static String formatLastPlayed(final LevelSummary cye) {
        return RealmsSelectFileToUploadScreen.DATE_FORMAT.format(new Date(cye.getLastPlayed()));
    }
    
    static {
        LOGGER = LogManager.getLogger();
        WORLD_TEXT = new TranslatableComponent("selectWorld.world");
        REQUIRES_CONVERSION_TEXT = new TranslatableComponent("selectWorld.conversion");
        HARDCORE_TEXT = new TranslatableComponent("mco.upload.hardcore").withStyle(ChatFormatting.DARK_RED);
        CHEATS_TEXT = new TranslatableComponent("selectWorld.cheats");
        DATE_FORMAT = (DateFormat)new SimpleDateFormat();
    }
    
    class WorldSelectionList extends RealmsObjectSelectionList<RealmsSelectFileToUploadScreen.Entry> {
        public WorldSelectionList() {
            super(RealmsSelectFileToUploadScreen.this.width, RealmsSelectFileToUploadScreen.this.height, RealmsScreen.row(0), RealmsSelectFileToUploadScreen.this.height - 40, 36);
        }
        
        public void addEntry(final LevelSummary cye) {
            this.addEntry(new RealmsSelectFileToUploadScreen.Entry(cye));
        }
        
        @Override
        public int getMaxPosition() {
            return RealmsSelectFileToUploadScreen.this.levelList.size() * 36;
        }
        
        public boolean isFocused() {
            return RealmsSelectFileToUploadScreen.this.getFocused() == this;
        }
        
        public void renderBackground(final PoseStack dfj) {
            RealmsSelectFileToUploadScreen.this.renderBackground(dfj);
        }
        
        @Override
        public void selectItem(final int integer) {
            this.setSelectedItem(integer);
            if (integer != -1) {
                final LevelSummary cye3 = (LevelSummary)RealmsSelectFileToUploadScreen.this.levelList.get(integer);
                final String string4 = I18n.get("narrator.select.list.position", integer + 1, RealmsSelectFileToUploadScreen.this.levelList.size());
                final String string5 = NarrationHelper.join((Iterable<String>)Arrays.asList((Object[])new String[] { cye3.getLevelName(), formatLastPlayed(cye3), gameModeName(cye3).getString(), string4 }));
                NarrationHelper.now(I18n.get("narrator.select", string5));
            }
        }
        
        @Override
        public void setSelected(@Nullable final RealmsSelectFileToUploadScreen.Entry a) {
            super.setSelected(a);
            RealmsSelectFileToUploadScreen.this.selectedWorld = this.children().indexOf(a);
            RealmsSelectFileToUploadScreen.this.uploadButton.active = (RealmsSelectFileToUploadScreen.this.selectedWorld >= 0 && RealmsSelectFileToUploadScreen.this.selectedWorld < this.getItemCount() && !((LevelSummary)RealmsSelectFileToUploadScreen.this.levelList.get(RealmsSelectFileToUploadScreen.this.selectedWorld)).isHardcore());
        }
    }
    
    class Entry extends ObjectSelectionList.Entry<Entry> {
        private final LevelSummary levelSummary;
        private final String name;
        private final String id;
        private final Component info;
        
        public Entry(final LevelSummary cye) {
            this.levelSummary = cye;
            this.name = cye.getLevelName();
            this.id = cye.getLevelId() + " (" + formatLastPlayed(cye) + ")";
            if (cye.isRequiresConversion()) {
                this.info = RealmsSelectFileToUploadScreen.REQUIRES_CONVERSION_TEXT;
            }
            else {
                Component nr4;
                if (cye.isHardcore()) {
                    nr4 = RealmsSelectFileToUploadScreen.HARDCORE_TEXT;
                }
                else {
                    nr4 = gameModeName(cye);
                }
                if (cye.hasCheats()) {
                    nr4 = nr4.copy().append(", ").append(RealmsSelectFileToUploadScreen.CHEATS_TEXT);
                }
                this.info = nr4;
            }
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            this.renderItem(dfj, this.levelSummary, integer2, integer4, integer3);
        }
        
        public boolean mouseClicked(final double double1, final double double2, final int integer) {
            RealmsSelectFileToUploadScreen.this.worldSelectionList.selectItem(RealmsSelectFileToUploadScreen.this.levelList.indexOf(this.levelSummary));
            return true;
        }
        
        protected void renderItem(final PoseStack dfj, final LevelSummary cye, final int integer3, final int integer4, final int integer5) {
            String string7;
            if (this.name.isEmpty()) {
                string7 = new StringBuilder().append(RealmsSelectFileToUploadScreen.WORLD_TEXT).append(" ").append(integer3 + 1).toString();
            }
            else {
                string7 = this.name;
            }
            RealmsSelectFileToUploadScreen.this.font.draw(dfj, string7, (float)(integer4 + 2), (float)(integer5 + 1), 16777215);
            RealmsSelectFileToUploadScreen.this.font.draw(dfj, this.id, (float)(integer4 + 2), (float)(integer5 + 12), 8421504);
            RealmsSelectFileToUploadScreen.this.font.draw(dfj, this.info, (float)(integer4 + 2), (float)(integer5 + 12 + 10), 8421504);
        }
    }
}
