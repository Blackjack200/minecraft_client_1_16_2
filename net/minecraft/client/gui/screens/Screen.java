package net.minecraft.client.gui.screens;

import com.google.common.collect.Sets;
import org.apache.logging.log4j.LogManager;
import java.nio.file.Path;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.CrashReportDetail;
import net.minecraft.CrashReport;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.Util;
import java.io.File;
import java.util.Locale;
import java.net.URISyntaxException;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import com.mojang.math.Matrix4f;
import com.mojang.blaze3d.vertex.BufferBuilder;
import java.util.Iterator;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import java.util.Arrays;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Lists;
import java.net.URI;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.entity.ItemRenderer;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import java.util.List;
import net.minecraft.network.chat.Component;
import java.util.Set;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.TickableWidget;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;

public abstract class Screen extends AbstractContainerEventHandler implements TickableWidget, Widget {
    private static final Logger LOGGER;
    private static final Set<String> ALLOWED_PROTOCOLS;
    protected final Component title;
    protected final List<GuiEventListener> children;
    @Nullable
    protected Minecraft minecraft;
    protected ItemRenderer itemRenderer;
    public int width;
    public int height;
    protected final List<AbstractWidget> buttons;
    public boolean passEvents;
    protected Font font;
    private URI clickedLink;
    
    protected Screen(final Component nr) {
        this.children = (List<GuiEventListener>)Lists.newArrayList();
        this.buttons = (List<AbstractWidget>)Lists.newArrayList();
        this.title = nr;
    }
    
    public Component getTitle() {
        return this.title;
    }
    
    public String getNarrationMessage() {
        return this.getTitle().getString();
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        for (int integer4 = 0; integer4 < this.buttons.size(); ++integer4) {
            ((AbstractWidget)this.buttons.get(integer4)).render(dfj, integer2, integer3, float4);
        }
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256 && this.shouldCloseOnEsc()) {
            this.onClose();
            return true;
        }
        if (integer1 == 258) {
            final boolean boolean5 = !hasShiftDown();
            if (!this.changeFocus(boolean5)) {
                this.changeFocus(boolean5);
            }
            return false;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    public boolean shouldCloseOnEsc() {
        return true;
    }
    
    public void onClose() {
        this.minecraft.setScreen(null);
    }
    
    protected <T extends AbstractWidget> T addButton(final T dle) {
        this.buttons.add(dle);
        return this.<T>addWidget(dle);
    }
    
    protected <T extends GuiEventListener> T addWidget(final T dmf) {
        this.children.add(dmf);
        return dmf;
    }
    
    protected void renderTooltip(final PoseStack dfj, final ItemStack bly, final int integer3, final int integer4) {
        this.renderComponentTooltip(dfj, this.getTooltipFromItem(bly), integer3, integer4);
    }
    
    public List<Component> getTooltipFromItem(final ItemStack bly) {
        return bly.getTooltipLines(this.minecraft.player, this.minecraft.options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL);
    }
    
    public void renderTooltip(final PoseStack dfj, final Component nr, final int integer3, final int integer4) {
        this.renderTooltip(dfj, Arrays.asList((Object[])new FormattedCharSequence[] { nr.getVisualOrderText() }), integer3, integer4);
    }
    
    public void renderComponentTooltip(final PoseStack dfj, final List<Component> list, final int integer3, final int integer4) {
        this.renderTooltip(dfj, Lists.transform((List)list, Component::getVisualOrderText), integer3, integer4);
    }
    
    public void renderTooltip(final PoseStack dfj, final List<? extends FormattedCharSequence> list, final int integer3, final int integer4) {
        if (list.isEmpty()) {
            return;
        }
        int integer5 = 0;
        for (final FormattedCharSequence aex8 : list) {
            final int integer6 = this.font.width(aex8);
            if (integer6 > integer5) {
                integer5 = integer6;
            }
        }
        int integer7 = integer3 + 12;
        int integer8 = integer4 - 12;
        final int integer6 = integer5;
        int integer9 = 8;
        if (list.size() > 1) {
            integer9 += 2 + (list.size() - 1) * 10;
        }
        if (integer7 + integer5 > this.width) {
            integer7 -= 28 + integer5;
        }
        if (integer8 + integer9 + 6 > this.height) {
            integer8 = this.height - integer9 - 6;
        }
        dfj.pushPose();
        final int integer10 = -267386864;
        final int integer11 = 1347420415;
        final int integer12 = 1344798847;
        final int integer13 = 400;
        final Tesselator dfl15 = Tesselator.getInstance();
        final BufferBuilder dfe16 = dfl15.getBuilder();
        dfe16.begin(7, DefaultVertexFormat.POSITION_COLOR);
        final Matrix4f b17 = dfj.last().pose();
        GuiComponent.fillGradient(b17, dfe16, integer7 - 3, integer8 - 4, integer7 + integer6 + 3, integer8 - 3, 400, -267386864, -267386864);
        GuiComponent.fillGradient(b17, dfe16, integer7 - 3, integer8 + integer9 + 3, integer7 + integer6 + 3, integer8 + integer9 + 4, 400, -267386864, -267386864);
        GuiComponent.fillGradient(b17, dfe16, integer7 - 3, integer8 - 3, integer7 + integer6 + 3, integer8 + integer9 + 3, 400, -267386864, -267386864);
        GuiComponent.fillGradient(b17, dfe16, integer7 - 4, integer8 - 3, integer7 - 3, integer8 + integer9 + 3, 400, -267386864, -267386864);
        GuiComponent.fillGradient(b17, dfe16, integer7 + integer6 + 3, integer8 - 3, integer7 + integer6 + 4, integer8 + integer9 + 3, 400, -267386864, -267386864);
        GuiComponent.fillGradient(b17, dfe16, integer7 - 3, integer8 - 3 + 1, integer7 - 3 + 1, integer8 + integer9 + 3 - 1, 400, 1347420415, 1344798847);
        GuiComponent.fillGradient(b17, dfe16, integer7 + integer6 + 2, integer8 - 3 + 1, integer7 + integer6 + 3, integer8 + integer9 + 3 - 1, 400, 1347420415, 1344798847);
        GuiComponent.fillGradient(b17, dfe16, integer7 - 3, integer8 - 3, integer7 + integer6 + 3, integer8 - 3 + 1, 400, 1347420415, 1347420415);
        GuiComponent.fillGradient(b17, dfe16, integer7 - 3, integer8 + integer9 + 2, integer7 + integer6 + 3, integer8 + integer9 + 3, 400, 1344798847, 1344798847);
        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425);
        dfe16.end();
        BufferUploader.end(dfe16);
        RenderSystem.shadeModel(7424);
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        final MultiBufferSource.BufferSource a18 = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        dfj.translate(0.0, 0.0, 400.0);
        for (int integer14 = 0; integer14 < list.size(); ++integer14) {
            final FormattedCharSequence aex9 = (FormattedCharSequence)list.get(integer14);
            if (aex9 != null) {
                this.font.drawInBatch(aex9, (float)integer7, (float)integer8, -1, true, b17, a18, false, 0, 15728880);
            }
            if (integer14 == 0) {
                integer8 += 2;
            }
            integer8 += 10;
        }
        a18.endBatch();
        dfj.popPose();
    }
    
    protected void renderComponentHoverEffect(final PoseStack dfj, @Nullable final Style ob, final int integer3, final int integer4) {
        if (ob == null || ob.getHoverEvent() == null) {
            return;
        }
        final HoverEvent nv6 = ob.getHoverEvent();
        final HoverEvent.ItemStackInfo c7 = nv6.<HoverEvent.ItemStackInfo>getValue(HoverEvent.Action.SHOW_ITEM);
        if (c7 != null) {
            this.renderTooltip(dfj, c7.getItemStack(), integer3, integer4);
        }
        else {
            final HoverEvent.EntityTooltipInfo b8 = nv6.<HoverEvent.EntityTooltipInfo>getValue(HoverEvent.Action.SHOW_ENTITY);
            if (b8 != null) {
                if (this.minecraft.options.advancedItemTooltips) {
                    this.renderComponentTooltip(dfj, b8.getTooltipLines(), integer3, integer4);
                }
            }
            else {
                final Component nr9 = nv6.<Component>getValue(HoverEvent.Action.SHOW_TEXT);
                if (nr9 != null) {
                    this.renderTooltip(dfj, this.minecraft.font.split(nr9, Math.max(this.width / 2, 200)), integer3, integer4);
                }
            }
        }
    }
    
    protected void insertText(final String string, final boolean boolean2) {
    }
    
    public boolean handleComponentClicked(@Nullable final Style ob) {
        if (ob == null) {
            return false;
        }
        final ClickEvent np3 = ob.getClickEvent();
        if (hasShiftDown()) {
            if (ob.getInsertion() != null) {
                this.insertText(ob.getInsertion(), false);
            }
        }
        else if (np3 != null) {
            if (np3.getAction() == ClickEvent.Action.OPEN_URL) {
                if (!this.minecraft.options.chatLinks) {
                    return false;
                }
                try {
                    final URI uRI4 = new URI(np3.getValue());
                    final String string5 = uRI4.getScheme();
                    if (string5 == null) {
                        throw new URISyntaxException(np3.getValue(), "Missing protocol");
                    }
                    if (!Screen.ALLOWED_PROTOCOLS.contains(string5.toLowerCase(Locale.ROOT))) {
                        throw new URISyntaxException(np3.getValue(), "Unsupported protocol: " + string5.toLowerCase(Locale.ROOT));
                    }
                    if (this.minecraft.options.chatLinksPrompt) {
                        this.clickedLink = uRI4;
                        this.minecraft.setScreen(new ConfirmLinkScreen(this::confirmLink, np3.getValue(), false));
                    }
                    else {
                        this.openLink(uRI4);
                    }
                }
                catch (URISyntaxException uRISyntaxException4) {
                    Screen.LOGGER.error("Can't open url for {}", np3, uRISyntaxException4);
                }
            }
            else if (np3.getAction() == ClickEvent.Action.OPEN_FILE) {
                final URI uRI4 = new File(np3.getValue()).toURI();
                this.openLink(uRI4);
            }
            else if (np3.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                this.insertText(np3.getValue(), true);
            }
            else if (np3.getAction() == ClickEvent.Action.RUN_COMMAND) {
                this.sendMessage(np3.getValue(), false);
            }
            else if (np3.getAction() == ClickEvent.Action.COPY_TO_CLIPBOARD) {
                this.minecraft.keyboardHandler.setClipboard(np3.getValue());
            }
            else {
                Screen.LOGGER.error("Don't know how to handle {}", np3);
            }
            return true;
        }
        return false;
    }
    
    public void sendMessage(final String string) {
        this.sendMessage(string, true);
    }
    
    public void sendMessage(final String string, final boolean boolean2) {
        if (boolean2) {
            this.minecraft.gui.getChat().addRecentChat(string);
        }
        this.minecraft.player.chat(string);
    }
    
    public void init(final Minecraft djw, final int integer2, final int integer3) {
        this.minecraft = djw;
        this.itemRenderer = djw.getItemRenderer();
        this.font = djw.font;
        this.width = integer2;
        this.height = integer3;
        this.buttons.clear();
        this.children.clear();
        this.setFocused(null);
        this.init();
    }
    
    @Override
    public List<? extends GuiEventListener> children() {
        return this.children;
    }
    
    protected void init() {
    }
    
    @Override
    public void tick() {
    }
    
    public void removed() {
    }
    
    public void renderBackground(final PoseStack dfj) {
        this.renderBackground(dfj, 0);
    }
    
    public void renderBackground(final PoseStack dfj, final int integer) {
        if (this.minecraft.level != null) {
            this.fillGradient(dfj, 0, 0, this.width, this.height, -1072689136, -804253680);
        }
        else {
            this.renderDirtBackground(integer);
        }
    }
    
    public void renderDirtBackground(final int integer) {
        final Tesselator dfl3 = Tesselator.getInstance();
        final BufferBuilder dfe4 = dfl3.getBuilder();
        this.minecraft.getTextureManager().bind(Screen.BACKGROUND_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final float float5 = 32.0f;
        dfe4.begin(7, DefaultVertexFormat.POSITION_TEX_COLOR);
        dfe4.vertex(0.0, this.height, 0.0).uv(0.0f, this.height / 32.0f + integer).color(64, 64, 64, 255).endVertex();
        dfe4.vertex(this.width, this.height, 0.0).uv(this.width / 32.0f, this.height / 32.0f + integer).color(64, 64, 64, 255).endVertex();
        dfe4.vertex(this.width, 0.0, 0.0).uv(this.width / 32.0f, (float)integer).color(64, 64, 64, 255).endVertex();
        dfe4.vertex(0.0, 0.0, 0.0).uv(0.0f, (float)integer).color(64, 64, 64, 255).endVertex();
        dfl3.end();
    }
    
    public boolean isPauseScreen() {
        return true;
    }
    
    private void confirmLink(final boolean boolean1) {
        if (boolean1) {
            this.openLink(this.clickedLink);
        }
        this.clickedLink = null;
        this.minecraft.setScreen(this);
    }
    
    private void openLink(final URI uRI) {
        Util.getPlatform().openUri(uRI);
    }
    
    public static boolean hasControlDown() {
        if (Minecraft.ON_OSX) {
            return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 343) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 347);
        }
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 341) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 345);
    }
    
    public static boolean hasShiftDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344);
    }
    
    public static boolean hasAltDown() {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 342) || InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 346);
    }
    
    public static boolean isCut(final int integer) {
        return integer == 88 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }
    
    public static boolean isPaste(final int integer) {
        return integer == 86 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }
    
    public static boolean isCopy(final int integer) {
        return integer == 67 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }
    
    public static boolean isSelectAll(final int integer) {
        return integer == 65 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }
    
    public void resize(final Minecraft djw, final int integer2, final int integer3) {
        this.init(djw, integer2, integer3);
    }
    
    public static void wrapScreenError(final Runnable runnable, final String string2, final String string3) {
        try {
            runnable.run();
        }
        catch (Throwable throwable4) {
            final CrashReport l5 = CrashReport.forThrowable(throwable4, string2);
            final CrashReportCategory m6 = l5.addCategory("Affected screen");
            m6.setDetail("Screen name", (CrashReportDetail<String>)(() -> string3));
            throw new ReportedException(l5);
        }
    }
    
    protected boolean isValidCharacterForName(final String string, final char character, final int integer) {
        final int integer2 = string.indexOf(58);
        final int integer3 = string.indexOf(47);
        if (character == ':') {
            return (integer3 == -1 || integer <= integer3) && integer2 == -1;
        }
        if (character == '/') {
            return integer > integer2;
        }
        return character == '_' || character == '-' || (character >= 'a' && character <= 'z') || (character >= '0' && character <= '9') || character == '.';
    }
    
    public boolean isMouseOver(final double double1, final double double2) {
        return true;
    }
    
    public void onFilesDrop(final List<Path> list) {
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ALLOWED_PROTOCOLS = (Set)Sets.newHashSet((Object[])new String[] { "http", "https" });
    }
}
