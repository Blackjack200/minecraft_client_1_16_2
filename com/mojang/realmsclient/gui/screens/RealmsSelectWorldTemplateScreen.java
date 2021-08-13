package com.mojang.realmsclient.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.realmsclient.util.RealmsTextureManager;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.AbstractSelectionList;
import java.util.Arrays;
import java.util.Collections;
import net.minecraft.realms.RealmsObjectSelectionList;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.util.Iterator;
import net.minecraft.client.resources.language.I18n;
import com.mojang.datafixers.util.Either;
import java.util.function.Supplier;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.realms.NarrationHelper;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.dto.WorldTemplatePaginatedList;
import com.mojang.realmsclient.util.TextRenderingUtils;
import java.util.List;
import com.mojang.realmsclient.dto.RealmsServer;
import javax.annotation.Nullable;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsSelectWorldTemplateScreen extends RealmsScreen {
    private static final Logger LOGGER;
    private static final ResourceLocation LINK_ICON;
    private static final ResourceLocation TRAILER_ICON;
    private static final ResourceLocation SLOT_FRAME_LOCATION;
    private static final Component PUBLISHER_LINK_TOOLTIP;
    private static final Component TRAILER_LINK_TOOLTIP;
    private final RealmsScreenWithCallback lastScreen;
    private WorldTemplateObjectSelectionList worldTemplateObjectSelectionList;
    private int selectedTemplate;
    private Component title;
    private Button selectButton;
    private Button trailerButton;
    private Button publisherButton;
    @Nullable
    private Component toolTip;
    private String currentLink;
    private final RealmsServer.WorldType worldType;
    private int clicks;
    @Nullable
    private Component[] warning;
    private String warningURL;
    private boolean displayWarning;
    private boolean hoverWarning;
    @Nullable
    private List<TextRenderingUtils.Line> noTemplatesMessage;
    
    public RealmsSelectWorldTemplateScreen(final RealmsScreenWithCallback did, final RealmsServer.WorldType c) {
        this(did, c, null);
    }
    
    public RealmsSelectWorldTemplateScreen(final RealmsScreenWithCallback did, final RealmsServer.WorldType c, @Nullable final WorldTemplatePaginatedList dhc) {
        this.selectedTemplate = -1;
        this.lastScreen = did;
        this.worldType = c;
        if (dhc == null) {
            this.worldTemplateObjectSelectionList = new WorldTemplateObjectSelectionList();
            this.fetchTemplatesAsync(new WorldTemplatePaginatedList(10));
        }
        else {
            this.worldTemplateObjectSelectionList = new WorldTemplateObjectSelectionList((Iterable<WorldTemplate>)Lists.newArrayList((Iterable)dhc.templates));
            this.fetchTemplatesAsync(dhc);
        }
        this.title = new TranslatableComponent("mco.template.title");
    }
    
    public void setTitle(final Component nr) {
        this.title = nr;
    }
    
    public void setWarning(final Component... arr) {
        this.warning = arr;
        this.displayWarning = true;
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (this.hoverWarning && this.warningURL != null) {
            Util.getPlatform().openUri("https://beta.minecraft.net/realms/adventure-maps-in-1-9");
            return true;
        }
        return super.mouseClicked(double1, double2, integer);
    }
    
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.worldTemplateObjectSelectionList = new WorldTemplateObjectSelectionList((Iterable<WorldTemplate>)this.worldTemplateObjectSelectionList.getTemplates());
        this.trailerButton = this.<Button>addButton(new Button(this.width / 2 - 206, this.height - 32, 100, 20, new TranslatableComponent("mco.template.button.trailer"), dlg -> this.onTrailer()));
        this.selectButton = this.<Button>addButton(new Button(this.width / 2 - 100, this.height - 32, 100, 20, new TranslatableComponent("mco.template.button.select"), dlg -> this.selectTemplate()));
        final Component nr2 = (this.worldType == RealmsServer.WorldType.MINIGAME) ? CommonComponents.GUI_CANCEL : CommonComponents.GUI_BACK;
        final Button dlg3 = new Button(this.width / 2 + 6, this.height - 32, 100, 20, nr2, dlg -> this.backButtonClicked());
        this.<Button>addButton(dlg3);
        this.publisherButton = this.<Button>addButton(new Button(this.width / 2 + 112, this.height - 32, 100, 20, new TranslatableComponent("mco.template.button.publisher"), dlg -> this.onPublish()));
        this.selectButton.active = false;
        this.trailerButton.visible = false;
        this.publisherButton.visible = false;
        this.<WorldTemplateObjectSelectionList>addWidget(this.worldTemplateObjectSelectionList);
        this.magicalSpecialHackyFocus(this.worldTemplateObjectSelectionList);
        Stream<Component> stream4 = (Stream<Component>)Stream.of(this.title);
        if (this.warning != null) {
            stream4 = (Stream<Component>)Stream.concat(Stream.of((Object[])this.warning), (Stream)stream4);
        }
        NarrationHelper.now((Iterable<String>)stream4.filter(Objects::nonNull).map(Component::getString).collect(Collectors.toList()));
    }
    
    private void updateButtonStates() {
        this.publisherButton.visible = this.shouldPublisherBeVisible();
        this.trailerButton.visible = this.shouldTrailerBeVisible();
        this.selectButton.active = this.shouldSelectButtonBeActive();
    }
    
    private boolean shouldSelectButtonBeActive() {
        return this.selectedTemplate != -1;
    }
    
    private boolean shouldPublisherBeVisible() {
        return this.selectedTemplate != -1 && !this.getSelectedTemplate().link.isEmpty();
    }
    
    private WorldTemplate getSelectedTemplate() {
        return this.worldTemplateObjectSelectionList.get(this.selectedTemplate);
    }
    
    private boolean shouldTrailerBeVisible() {
        return this.selectedTemplate != -1 && !this.getSelectedTemplate().trailer.isEmpty();
    }
    
    @Override
    public void tick() {
        super.tick();
        --this.clicks;
        if (this.clicks < 0) {
            this.clicks = 0;
        }
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
        this.lastScreen.callback(null);
        this.minecraft.setScreen(this.lastScreen);
    }
    
    private void selectTemplate() {
        if (this.hasValidTemplate()) {
            this.lastScreen.callback(this.getSelectedTemplate());
        }
    }
    
    private boolean hasValidTemplate() {
        return this.selectedTemplate >= 0 && this.selectedTemplate < this.worldTemplateObjectSelectionList.getItemCount();
    }
    
    private void onTrailer() {
        if (this.hasValidTemplate()) {
            final WorldTemplate dhb2 = this.getSelectedTemplate();
            if (!"".equals(dhb2.trailer)) {
                Util.getPlatform().openUri(dhb2.trailer);
            }
        }
    }
    
    private void onPublish() {
        if (this.hasValidTemplate()) {
            final WorldTemplate dhb2 = this.getSelectedTemplate();
            if (!"".equals(dhb2.link)) {
                Util.getPlatform().openUri(dhb2.link);
            }
        }
    }
    
    private void fetchTemplatesAsync(final WorldTemplatePaginatedList dhc) {
        new Thread("realms-template-fetcher") {
            public void run() {
                WorldTemplatePaginatedList dhc2 = dhc;
                final RealmsClient dfy3 = RealmsClient.create();
                while (dhc2 != null) {
                    final Either<WorldTemplatePaginatedList, String> either4 = RealmsSelectWorldTemplateScreen.this.fetchTemplates(dhc2, dfy3);
                    dhc2 = (WorldTemplatePaginatedList)RealmsSelectWorldTemplateScreen.this.minecraft.submit((java.util.function.Supplier<Object>)(() -> {
                        if (either4.right().isPresent()) {
                            RealmsSelectWorldTemplateScreen.LOGGER.error("Couldn't fetch templates: {}", either4.right().get());
                            if (RealmsSelectWorldTemplateScreen.this.worldTemplateObjectSelectionList.isEmpty()) {
                                RealmsSelectWorldTemplateScreen.this.noTemplatesMessage = TextRenderingUtils.decompose(I18n.get("mco.template.select.failure"));
                            }
                            return null;
                        }
                        final WorldTemplatePaginatedList dhc3 = (WorldTemplatePaginatedList)either4.left().get();
                        for (final WorldTemplate dhb5 : dhc3.templates) {
                            RealmsSelectWorldTemplateScreen.this.worldTemplateObjectSelectionList.addEntry(dhb5);
                        }
                        if (dhc3.templates.isEmpty()) {
                            if (RealmsSelectWorldTemplateScreen.this.worldTemplateObjectSelectionList.isEmpty()) {
                                final String string4 = I18n.get("mco.template.select.none", "%link");
                                final TextRenderingUtils.LineSegment b5 = TextRenderingUtils.LineSegment.link(I18n.get("mco.template.select.none.linkTitle"), "https://minecraft.net/realms/content-creator/");
                                RealmsSelectWorldTemplateScreen.this.noTemplatesMessage = TextRenderingUtils.decompose(string4, b5);
                            }
                            return null;
                        }
                        return dhc3;
                    })).join();
                }
            }
        }.start();
    }
    
    private Either<WorldTemplatePaginatedList, String> fetchTemplates(final WorldTemplatePaginatedList dhc, final RealmsClient dfy) {
        try {
            return (Either<WorldTemplatePaginatedList, String>)Either.left(dfy.fetchWorldTemplates(dhc.page + 1, dhc.size, this.worldType));
        }
        catch (RealmsServiceException dhf4) {
            return (Either<WorldTemplatePaginatedList, String>)Either.right(dhf4.getMessage());
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.toolTip = null;
        this.currentLink = null;
        this.hoverWarning = false;
        this.renderBackground(dfj);
        this.worldTemplateObjectSelectionList.render(dfj, integer2, integer3, float4);
        if (this.noTemplatesMessage != null) {
            this.renderMultilineMessage(dfj, integer2, integer3, this.noTemplatesMessage);
        }
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 13, 16777215);
        if (this.displayWarning) {
            final Component[] arr6 = this.warning;
            for (int integer4 = 0; integer4 < arr6.length; ++integer4) {
                final int integer5 = this.font.width(arr6[integer4]);
                final int integer6 = this.width / 2 - integer5 / 2;
                final int integer7 = RealmsScreen.row(-1 + integer4);
                if (integer2 >= integer6 && integer2 <= integer6 + integer5 && integer3 >= integer7) {
                    final int n = integer7;
                    this.font.getClass();
                    if (integer3 <= n + 9) {
                        this.hoverWarning = true;
                    }
                }
            }
            for (int integer4 = 0; integer4 < arr6.length; ++integer4) {
                Component nr8 = arr6[integer4];
                int integer6 = 10526880;
                if (this.warningURL != null) {
                    if (this.hoverWarning) {
                        integer6 = 7107012;
                        nr8 = nr8.copy().withStyle(ChatFormatting.STRIKETHROUGH);
                    }
                    else {
                        integer6 = 3368635;
                    }
                }
                GuiComponent.drawCenteredString(dfj, this.font, nr8, this.width / 2, RealmsScreen.row(-1 + integer4), integer6);
            }
        }
        super.render(dfj, integer2, integer3, float4);
        this.renderMousehoverTooltip(dfj, this.toolTip, integer2, integer3);
    }
    
    private void renderMultilineMessage(final PoseStack dfj, final int integer2, final int integer3, final List<TextRenderingUtils.Line> list) {
        for (int integer4 = 0; integer4 < list.size(); ++integer4) {
            final TextRenderingUtils.Line a7 = (TextRenderingUtils.Line)list.get(integer4);
            final int integer5 = RealmsScreen.row(4 + integer4);
            final int integer6 = a7.segments.stream().mapToInt(b -> this.font.width(b.renderedText())).sum();
            int integer7 = this.width / 2 - integer6 / 2;
            for (final TextRenderingUtils.LineSegment b12 : a7.segments) {
                final int integer8 = b12.isLink() ? 3368635 : 16777215;
                final int integer9 = this.font.drawShadow(dfj, b12.renderedText(), (float)integer7, (float)integer5, integer8);
                if (b12.isLink() && integer2 > integer7 && integer2 < integer9 && integer3 > integer5 - 3 && integer3 < integer5 + 8) {
                    this.toolTip = new TextComponent(b12.getLinkUrl());
                    this.currentLink = b12.getLinkUrl();
                }
                integer7 = integer9;
            }
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
        LINK_ICON = new ResourceLocation("realms", "textures/gui/realms/link_icons.png");
        TRAILER_ICON = new ResourceLocation("realms", "textures/gui/realms/trailer_icons.png");
        SLOT_FRAME_LOCATION = new ResourceLocation("realms", "textures/gui/realms/slot_frame.png");
        PUBLISHER_LINK_TOOLTIP = new TranslatableComponent("mco.template.info.tooltip");
        TRAILER_LINK_TOOLTIP = new TranslatableComponent("mco.template.trailer.tooltip");
    }
    
    class WorldTemplateObjectSelectionList extends RealmsObjectSelectionList<RealmsSelectWorldTemplateScreen.Entry> {
        public WorldTemplateObjectSelectionList(final RealmsSelectWorldTemplateScreen dif) {
            this(dif, (Iterable<WorldTemplate>)Collections.emptyList());
        }
        
        public WorldTemplateObjectSelectionList(final Iterable<WorldTemplate> iterable) {
            super(RealmsSelectWorldTemplateScreen.this.width, RealmsSelectWorldTemplateScreen.this.height, RealmsSelectWorldTemplateScreen.this.displayWarning ? RealmsScreen.row(1) : 32, RealmsSelectWorldTemplateScreen.this.height - 40, 46);
            iterable.forEach(this::addEntry);
        }
        
        public void addEntry(final WorldTemplate dhb) {
            this.addEntry(new RealmsSelectWorldTemplateScreen.Entry(dhb));
        }
        
        @Override
        public boolean mouseClicked(final double double1, final double double2, final int integer) {
            if (integer == 0 && double2 >= this.y0 && double2 <= this.y1) {
                final int integer2 = this.width / 2 - 150;
                if (RealmsSelectWorldTemplateScreen.this.currentLink != null) {
                    Util.getPlatform().openUri(RealmsSelectWorldTemplateScreen.this.currentLink);
                }
                final int integer3 = (int)Math.floor(double2 - this.y0) - this.headerHeight + (int)this.getScrollAmount() - 4;
                final int integer4 = integer3 / this.itemHeight;
                if (double1 >= integer2 && double1 < this.getScrollbarPosition() && integer4 >= 0 && integer3 >= 0 && integer4 < this.getItemCount()) {
                    this.selectItem(integer4);
                    this.itemClicked(integer3, integer4, double1, double2, this.width);
                    if (integer4 >= RealmsSelectWorldTemplateScreen.this.worldTemplateObjectSelectionList.getItemCount()) {
                        return super.mouseClicked(double1, double2, integer);
                    }
                    RealmsSelectWorldTemplateScreen.this.clicks += 7;
                    if (RealmsSelectWorldTemplateScreen.this.clicks >= 10) {
                        RealmsSelectWorldTemplateScreen.this.selectTemplate();
                    }
                    return true;
                }
            }
            return super.mouseClicked(double1, double2, integer);
        }
        
        @Override
        public void selectItem(final int integer) {
            this.setSelectedItem(integer);
            if (integer != -1) {
                final WorldTemplate dhb3 = RealmsSelectWorldTemplateScreen.this.worldTemplateObjectSelectionList.get(integer);
                final String string4 = I18n.get("narrator.select.list.position", integer + 1, RealmsSelectWorldTemplateScreen.this.worldTemplateObjectSelectionList.getItemCount());
                final String string5 = I18n.get("mco.template.select.narrate.version", dhb3.version);
                final String string6 = I18n.get("mco.template.select.narrate.authors", dhb3.author);
                final String string7 = NarrationHelper.join((Iterable<String>)Arrays.asList((Object[])new String[] { dhb3.name, string6, dhb3.recommendedPlayers, string5, string4 }));
                NarrationHelper.now(I18n.get("narrator.select", string7));
            }
        }
        
        @Override
        public void setSelected(@Nullable final RealmsSelectWorldTemplateScreen.Entry a) {
            super.setSelected(a);
            RealmsSelectWorldTemplateScreen.this.selectedTemplate = this.children().indexOf(a);
            RealmsSelectWorldTemplateScreen.this.updateButtonStates();
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 46;
        }
        
        @Override
        public int getRowWidth() {
            return 300;
        }
        
        public void renderBackground(final PoseStack dfj) {
            RealmsSelectWorldTemplateScreen.this.renderBackground(dfj);
        }
        
        public boolean isFocused() {
            return RealmsSelectWorldTemplateScreen.this.getFocused() == this;
        }
        
        public boolean isEmpty() {
            return this.getItemCount() == 0;
        }
        
        public WorldTemplate get(final int integer) {
            return ((RealmsSelectWorldTemplateScreen.Entry)this.children().get(integer)).template;
        }
        
        public List<WorldTemplate> getTemplates() {
            return (List<WorldTemplate>)this.children().stream().map(a -> a.template).collect(Collectors.toList());
        }
    }
    
    class Entry extends ObjectSelectionList.Entry<Entry> {
        private final WorldTemplate template;
        
        public Entry(final WorldTemplate dhb) {
            this.template = dhb;
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            this.renderWorldTemplateItem(dfj, this.template, integer4, integer3, integer7, integer8);
        }
        
        private void renderWorldTemplateItem(final PoseStack dfj, final WorldTemplate dhb, final int integer3, final int integer4, final int integer5, final int integer6) {
            final int integer7 = integer3 + 45 + 20;
            RealmsSelectWorldTemplateScreen.this.font.draw(dfj, dhb.name, (float)integer7, (float)(integer4 + 2), 16777215);
            RealmsSelectWorldTemplateScreen.this.font.draw(dfj, dhb.author, (float)integer7, (float)(integer4 + 15), 7105644);
            RealmsSelectWorldTemplateScreen.this.font.draw(dfj, dhb.version, (float)(integer7 + 227 - RealmsSelectWorldTemplateScreen.this.font.width(dhb.version)), (float)(integer4 + 1), 7105644);
            if (!"".equals(dhb.link) || !"".equals(dhb.trailer) || !"".equals(dhb.recommendedPlayers)) {
                this.drawIcons(dfj, integer7 - 1, integer4 + 25, integer5, integer6, dhb.link, dhb.trailer, dhb.recommendedPlayers);
            }
            this.drawImage(dfj, integer3, integer4 + 1, integer5, integer6, dhb);
        }
        
        private void drawImage(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final WorldTemplate dhb) {
            RealmsTextureManager.bindWorldTemplate(dhb.id, dhb.image);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            GuiComponent.blit(dfj, integer2 + 1, integer3 + 1, 0.0f, 0.0f, 38, 38, 38, 38);
            RealmsSelectWorldTemplateScreen.this.minecraft.getTextureManager().bind(RealmsSelectWorldTemplateScreen.SLOT_FRAME_LOCATION);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            GuiComponent.blit(dfj, integer2, integer3, 0.0f, 0.0f, 40, 40, 40, 40);
        }
        
        private void drawIcons(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final String string6, final String string7, final String string8) {
            if (!"".equals(string8)) {
                RealmsSelectWorldTemplateScreen.this.font.draw(dfj, string8, (float)integer2, (float)(integer3 + 4), 5000268);
            }
            final int integer6 = "".equals(string8) ? 0 : (RealmsSelectWorldTemplateScreen.this.font.width(string8) + 2);
            boolean boolean11 = false;
            boolean boolean12 = false;
            final boolean boolean13 = "".equals(string6);
            if (integer4 >= integer2 + integer6 && integer4 <= integer2 + integer6 + 32 && integer5 >= integer3 && integer5 <= integer3 + 15 && integer5 < RealmsSelectWorldTemplateScreen.this.height - 15 && integer5 > 32) {
                if (integer4 <= integer2 + 15 + integer6 && integer4 > integer6) {
                    if (boolean13) {
                        boolean12 = true;
                    }
                    else {
                        boolean11 = true;
                    }
                }
                else if (!boolean13) {
                    boolean12 = true;
                }
            }
            if (!boolean13) {
                RealmsSelectWorldTemplateScreen.this.minecraft.getTextureManager().bind(RealmsSelectWorldTemplateScreen.LINK_ICON);
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                RenderSystem.pushMatrix();
                RenderSystem.scalef(1.0f, 1.0f, 1.0f);
                final float float14 = boolean11 ? 15.0f : 0.0f;
                GuiComponent.blit(dfj, integer2 + integer6, integer3, float14, 0.0f, 15, 15, 30, 15);
                RenderSystem.popMatrix();
            }
            if (!"".equals(string7)) {
                RealmsSelectWorldTemplateScreen.this.minecraft.getTextureManager().bind(RealmsSelectWorldTemplateScreen.TRAILER_ICON);
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                RenderSystem.pushMatrix();
                RenderSystem.scalef(1.0f, 1.0f, 1.0f);
                final int integer7 = integer2 + integer6 + (boolean13 ? 0 : 17);
                final float float15 = boolean12 ? 15.0f : 0.0f;
                GuiComponent.blit(dfj, integer7, integer3, float15, 0.0f, 15, 15, 30, 15);
                RenderSystem.popMatrix();
            }
            if (boolean11) {
                RealmsSelectWorldTemplateScreen.this.toolTip = RealmsSelectWorldTemplateScreen.PUBLISHER_LINK_TOOLTIP;
                RealmsSelectWorldTemplateScreen.this.currentLink = string6;
            }
            else if (boolean12 && !"".equals(string7)) {
                RealmsSelectWorldTemplateScreen.this.toolTip = RealmsSelectWorldTemplateScreen.TRAILER_LINK_TOOLTIP;
                RealmsSelectWorldTemplateScreen.this.currentLink = string7;
            }
        }
    }
}
