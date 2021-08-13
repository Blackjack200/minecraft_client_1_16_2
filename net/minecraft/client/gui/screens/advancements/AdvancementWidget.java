package net.minecraft.client.gui.screens.advancements;

import net.minecraft.client.gui.Font;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.client.StringSplitter;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.locale.Language;
import com.google.common.collect.Lists;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.GuiComponent;

public class AdvancementWidget extends GuiComponent {
    private static final ResourceLocation WIDGETS_LOCATION;
    private static final int[] TEST_SPLIT_OFFSETS;
    private final AdvancementTab tab;
    private final Advancement advancement;
    private final DisplayInfo display;
    private final FormattedCharSequence title;
    private final int width;
    private final List<FormattedCharSequence> description;
    private final Minecraft minecraft;
    private AdvancementWidget parent;
    private final List<AdvancementWidget> children;
    private AdvancementProgress progress;
    private final int x;
    private final int y;
    
    public AdvancementWidget(final AdvancementTab dpb, final Minecraft djw, final Advancement y, final DisplayInfo ah) {
        this.children = (List<AdvancementWidget>)Lists.newArrayList();
        this.tab = dpb;
        this.advancement = y;
        this.display = ah;
        this.minecraft = djw;
        this.title = Language.getInstance().getVisualOrder(djw.font.substrByWidth(ah.getTitle(), 163));
        this.x = Mth.floor(ah.getX() * 28.0f);
        this.y = Mth.floor(ah.getY() * 27.0f);
        final int integer6 = y.getMaxCriteraRequired();
        final int integer7 = String.valueOf(integer6).length();
        final int integer8 = (integer6 > 1) ? (djw.font.width("  ") + djw.font.width("0") * integer7 * 2 + djw.font.width("/")) : 0;
        int integer9 = 29 + djw.font.width(this.title) + integer8;
        this.description = Language.getInstance().getVisualOrder(this.findOptimalLines(ComponentUtils.mergeStyles(ah.getDescription().copy(), Style.EMPTY.withColor(ah.getFrame().getChatColor())), integer9));
        for (final FormattedCharSequence aex11 : this.description) {
            integer9 = Math.max(integer9, djw.font.width(aex11));
        }
        this.width = integer9 + 3 + 5;
    }
    
    private static float getMaxWidth(final StringSplitter dkg, final List<FormattedText> list) {
        return (float)list.stream().mapToDouble(dkg::stringWidth).max().orElse(0.0);
    }
    
    private List<FormattedText> findOptimalLines(final Component nr, final int integer) {
        final StringSplitter dkg4 = this.minecraft.font.getSplitter();
        List<FormattedText> list5 = null;
        float float6 = Float.MAX_VALUE;
        for (final int integer2 : AdvancementWidget.TEST_SPLIT_OFFSETS) {
            final List<FormattedText> list6 = dkg4.splitLines(nr, integer - integer2, Style.EMPTY);
            final float float7 = Math.abs(getMaxWidth(dkg4, list6) - integer);
            if (float7 <= 10.0f) {
                return list6;
            }
            if (float7 < float6) {
                float6 = float7;
                list5 = list6;
            }
        }
        return list5;
    }
    
    @Nullable
    private AdvancementWidget getFirstVisibleParent(Advancement y) {
        do {
            y = y.getParent();
        } while (y != null && y.getDisplay() == null);
        if (y == null || y.getDisplay() == null) {
            return null;
        }
        return this.tab.getWidget(y);
    }
    
    public void drawConnectivity(final PoseStack dfj, final int integer2, final int integer3, final boolean boolean4) {
        if (this.parent != null) {
            final int integer4 = integer2 + this.parent.x + 13;
            final int integer5 = integer2 + this.parent.x + 26 + 4;
            final int integer6 = integer3 + this.parent.y + 13;
            final int integer7 = integer2 + this.x + 13;
            final int integer8 = integer3 + this.y + 13;
            final int integer9 = boolean4 ? -16777216 : -1;
            if (boolean4) {
                this.hLine(dfj, integer5, integer4, integer6 - 1, integer9);
                this.hLine(dfj, integer5 + 1, integer4, integer6, integer9);
                this.hLine(dfj, integer5, integer4, integer6 + 1, integer9);
                this.hLine(dfj, integer7, integer5 - 1, integer8 - 1, integer9);
                this.hLine(dfj, integer7, integer5 - 1, integer8, integer9);
                this.hLine(dfj, integer7, integer5 - 1, integer8 + 1, integer9);
                this.vLine(dfj, integer5 - 1, integer8, integer6, integer9);
                this.vLine(dfj, integer5 + 1, integer8, integer6, integer9);
            }
            else {
                this.hLine(dfj, integer5, integer4, integer6, integer9);
                this.hLine(dfj, integer7, integer5, integer8, integer9);
                this.vLine(dfj, integer5, integer8, integer6, integer9);
            }
        }
        for (final AdvancementWidget dpd7 : this.children) {
            dpd7.drawConnectivity(dfj, integer2, integer3, boolean4);
        }
    }
    
    public void draw(final PoseStack dfj, final int integer2, final int integer3) {
        if (!this.display.isHidden() || (this.progress != null && this.progress.isDone())) {
            final float float5 = (this.progress == null) ? 0.0f : this.progress.getPercent();
            AdvancementWidgetType dpe6;
            if (float5 >= 1.0f) {
                dpe6 = AdvancementWidgetType.OBTAINED;
            }
            else {
                dpe6 = AdvancementWidgetType.UNOBTAINED;
            }
            this.minecraft.getTextureManager().bind(AdvancementWidget.WIDGETS_LOCATION);
            this.blit(dfj, integer2 + this.x + 3, integer3 + this.y, this.display.getFrame().getTexture(), 128 + dpe6.getIndex() * 26, 26, 26);
            this.minecraft.getItemRenderer().renderAndDecorateFakeItem(this.display.getIcon(), integer2 + this.x + 8, integer3 + this.y + 5);
        }
        for (final AdvancementWidget dpd6 : this.children) {
            dpd6.draw(dfj, integer2, integer3);
        }
    }
    
    public void setProgress(final AdvancementProgress aa) {
        this.progress = aa;
    }
    
    public void addChild(final AdvancementWidget dpd) {
        this.children.add(dpd);
    }
    
    public void drawHover(final PoseStack dfj, final int integer2, final int integer3, final float float4, final int integer5, final int integer6) {
        final boolean boolean8 = integer5 + integer2 + this.x + this.width + 26 >= this.tab.getScreen().width;
        final String string9 = (this.progress == null) ? null : this.progress.getProgressText();
        final int integer7 = (string9 == null) ? 0 : this.minecraft.font.width(string9);
        final int n = 113 - integer3 - this.y - 26;
        final int n2 = 6;
        final int size = this.description.size();
        this.minecraft.font.getClass();
        final boolean boolean9 = n <= n2 + size * 9;
        final float float5 = (this.progress == null) ? 0.0f : this.progress.getPercent();
        int integer8 = Mth.floor(float5 * this.width);
        AdvancementWidgetType dpe13;
        AdvancementWidgetType dpe14;
        AdvancementWidgetType dpe15;
        if (float5 >= 1.0f) {
            integer8 = this.width / 2;
            dpe13 = AdvancementWidgetType.OBTAINED;
            dpe14 = AdvancementWidgetType.OBTAINED;
            dpe15 = AdvancementWidgetType.OBTAINED;
        }
        else if (integer8 < 2) {
            integer8 = this.width / 2;
            dpe13 = AdvancementWidgetType.UNOBTAINED;
            dpe14 = AdvancementWidgetType.UNOBTAINED;
            dpe15 = AdvancementWidgetType.UNOBTAINED;
        }
        else if (integer8 > this.width - 2) {
            integer8 = this.width / 2;
            dpe13 = AdvancementWidgetType.OBTAINED;
            dpe14 = AdvancementWidgetType.OBTAINED;
            dpe15 = AdvancementWidgetType.UNOBTAINED;
        }
        else {
            dpe13 = AdvancementWidgetType.OBTAINED;
            dpe14 = AdvancementWidgetType.UNOBTAINED;
            dpe15 = AdvancementWidgetType.UNOBTAINED;
        }
        final int integer9 = this.width - integer8;
        this.minecraft.getTextureManager().bind(AdvancementWidget.WIDGETS_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.enableBlend();
        final int integer10 = integer3 + this.y;
        int integer11;
        if (boolean8) {
            integer11 = integer2 + this.x - this.width + 26 + 6;
        }
        else {
            integer11 = integer2 + this.x;
        }
        final int n3 = 32;
        final int size2 = this.description.size();
        this.minecraft.font.getClass();
        final int integer12 = n3 + size2 * 9;
        if (!this.description.isEmpty()) {
            if (boolean9) {
                this.render9Sprite(dfj, integer11, integer10 + 26 - integer12, this.width, integer12, 10, 200, 26, 0, 52);
            }
            else {
                this.render9Sprite(dfj, integer11, integer10, this.width, integer12, 10, 200, 26, 0, 52);
            }
        }
        this.blit(dfj, integer11, integer10, 0, dpe13.getIndex() * 26, integer8, 26);
        this.blit(dfj, integer11 + integer8, integer10, 200 - integer9, dpe14.getIndex() * 26, integer9, 26);
        this.blit(dfj, integer2 + this.x + 3, integer3 + this.y, this.display.getFrame().getTexture(), 128 + dpe15.getIndex() * 26, 26, 26);
        if (boolean8) {
            this.minecraft.font.drawShadow(dfj, this.title, (float)(integer11 + 5), (float)(integer3 + this.y + 9), -1);
            if (string9 != null) {
                this.minecraft.font.drawShadow(dfj, string9, (float)(integer2 + this.x - integer7), (float)(integer3 + this.y + 9), -1);
            }
        }
        else {
            this.minecraft.font.drawShadow(dfj, this.title, (float)(integer2 + this.x + 32), (float)(integer3 + this.y + 9), -1);
            if (string9 != null) {
                this.minecraft.font.drawShadow(dfj, string9, (float)(integer2 + this.x + this.width - integer7 - 5), (float)(integer3 + this.y + 9), -1);
            }
        }
        if (boolean9) {
            for (int integer13 = 0; integer13 < this.description.size(); ++integer13) {
                final Font font = this.minecraft.font;
                final FormattedCharSequence aex = (FormattedCharSequence)this.description.get(integer13);
                final float float6 = (float)(integer11 + 5);
                final int n4 = integer10 + 26 - integer12 + 7;
                final int n5 = integer13;
                this.minecraft.font.getClass();
                font.draw(dfj, aex, float6, (float)(n4 + n5 * 9), -5592406);
            }
        }
        else {
            for (int integer13 = 0; integer13 < this.description.size(); ++integer13) {
                final Font font2 = this.minecraft.font;
                final FormattedCharSequence aex2 = (FormattedCharSequence)this.description.get(integer13);
                final float float7 = (float)(integer11 + 5);
                final int n6 = integer3 + this.y + 9 + 17;
                final int n7 = integer13;
                this.minecraft.font.getClass();
                font2.draw(dfj, aex2, float7, (float)(n6 + n7 * 9), -5592406);
            }
        }
        this.minecraft.getItemRenderer().renderAndDecorateFakeItem(this.display.getIcon(), integer2 + this.x + 8, integer3 + this.y + 5);
    }
    
    protected void render9Sprite(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final int integer9, final int integer10) {
        this.blit(dfj, integer2, integer3, integer9, integer10, integer6, integer6);
        this.renderRepeating(dfj, integer2 + integer6, integer3, integer4 - integer6 - integer6, integer6, integer9 + integer6, integer10, integer7 - integer6 - integer6, integer8);
        this.blit(dfj, integer2 + integer4 - integer6, integer3, integer9 + integer7 - integer6, integer10, integer6, integer6);
        this.blit(dfj, integer2, integer3 + integer5 - integer6, integer9, integer10 + integer8 - integer6, integer6, integer6);
        this.renderRepeating(dfj, integer2 + integer6, integer3 + integer5 - integer6, integer4 - integer6 - integer6, integer6, integer9 + integer6, integer10 + integer8 - integer6, integer7 - integer6 - integer6, integer8);
        this.blit(dfj, integer2 + integer4 - integer6, integer3 + integer5 - integer6, integer9 + integer7 - integer6, integer10 + integer8 - integer6, integer6, integer6);
        this.renderRepeating(dfj, integer2, integer3 + integer6, integer6, integer5 - integer6 - integer6, integer9, integer10 + integer6, integer7, integer8 - integer6 - integer6);
        this.renderRepeating(dfj, integer2 + integer6, integer3 + integer6, integer4 - integer6 - integer6, integer5 - integer6 - integer6, integer9 + integer6, integer10 + integer6, integer7 - integer6 - integer6, integer8 - integer6 - integer6);
        this.renderRepeating(dfj, integer2 + integer4 - integer6, integer3 + integer6, integer6, integer5 - integer6 - integer6, integer9 + integer7 - integer6, integer10 + integer6, integer7, integer8 - integer6 - integer6);
    }
    
    protected void renderRepeating(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final int integer9) {
        for (int integer10 = 0; integer10 < integer4; integer10 += integer8) {
            final int integer11 = integer2 + integer10;
            final int integer12 = Math.min(integer8, integer4 - integer10);
            for (int integer13 = 0; integer13 < integer5; integer13 += integer9) {
                final int integer14 = integer3 + integer13;
                final int integer15 = Math.min(integer9, integer5 - integer13);
                this.blit(dfj, integer11, integer14, integer6, integer7, integer12, integer15);
            }
        }
    }
    
    public boolean isMouseOver(final int integer1, final int integer2, final int integer3, final int integer4) {
        if (this.display.isHidden() && (this.progress == null || !this.progress.isDone())) {
            return false;
        }
        final int integer5 = integer1 + this.x;
        final int integer6 = integer5 + 26;
        final int integer7 = integer2 + this.y;
        final int integer8 = integer7 + 26;
        return integer3 >= integer5 && integer3 <= integer6 && integer4 >= integer7 && integer4 <= integer8;
    }
    
    public void attachToParent() {
        if (this.parent == null && this.advancement.getParent() != null) {
            this.parent = this.getFirstVisibleParent(this.advancement);
            if (this.parent != null) {
                this.parent.addChild(this);
            }
        }
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getX() {
        return this.x;
    }
    
    static {
        WIDGETS_LOCATION = new ResourceLocation("textures/gui/advancements/widgets.png");
        TEST_SPLIT_OFFSETS = new int[] { 0, 10, -10, 25, -25 };
    }
}
