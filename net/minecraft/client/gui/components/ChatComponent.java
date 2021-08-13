package net.minecraft.client.gui.components;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.screens.ChatScreen;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Style;
import java.util.Iterator;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Queues;
import com.google.common.collect.Lists;
import java.util.Deque;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.Component;
import net.minecraft.client.GuiMessage;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.GuiComponent;

public class ChatComponent extends GuiComponent {
    private static final Logger LOGGER;
    private final Minecraft minecraft;
    private final List<String> recentChat;
    private final List<GuiMessage<Component>> allMessages;
    private final List<GuiMessage<FormattedCharSequence>> trimmedMessages;
    private final Deque<Component> chatQueue;
    private int chatScrollbarPos;
    private boolean newMessageSinceScroll;
    private long lastMessage;
    
    public ChatComponent(final Minecraft djw) {
        this.recentChat = (List<String>)Lists.newArrayList();
        this.allMessages = (List<GuiMessage<Component>>)Lists.newArrayList();
        this.trimmedMessages = (List<GuiMessage<FormattedCharSequence>>)Lists.newArrayList();
        this.chatQueue = (Deque<Component>)Queues.newArrayDeque();
        this.lastMessage = 0L;
        this.minecraft = djw;
    }
    
    public void render(final PoseStack dfj, final int integer) {
        if (this.isChatHidden()) {
            return;
        }
        this.processPendingMessages();
        final int integer2 = this.getLinesPerPage();
        final int integer3 = this.trimmedMessages.size();
        if (integer3 <= 0) {
            return;
        }
        boolean boolean6 = false;
        if (this.isChatFocused()) {
            boolean6 = true;
        }
        final double double7 = this.getScale();
        final int integer4 = Mth.ceil(this.getWidth() / double7);
        RenderSystem.pushMatrix();
        RenderSystem.translatef(2.0f, 8.0f, 0.0f);
        RenderSystem.scaled(double7, double7, 1.0);
        final double double8 = this.minecraft.options.chatOpacity * 0.8999999761581421 + 0.10000000149011612;
        final double double9 = this.minecraft.options.textBackgroundOpacity;
        final double double10 = 9.0 * (this.minecraft.options.chatLineSpacing + 1.0);
        final double double11 = -8.0 * (this.minecraft.options.chatLineSpacing + 1.0) + 4.0 * this.minecraft.options.chatLineSpacing;
        int integer5 = 0;
        for (int integer6 = 0; integer6 + this.chatScrollbarPos < this.trimmedMessages.size() && integer6 < integer2; ++integer6) {
            final GuiMessage<FormattedCharSequence> djr20 = (GuiMessage<FormattedCharSequence>)this.trimmedMessages.get(integer6 + this.chatScrollbarPos);
            if (djr20 != null) {
                final int integer7 = integer - djr20.getAddedTime();
                if (integer7 < 200 || boolean6) {
                    final double double12 = boolean6 ? 1.0 : getTimeFactor(integer7);
                    final int integer8 = (int)(255.0 * double12 * double8);
                    final int integer9 = (int)(255.0 * double12 * double9);
                    ++integer5;
                    if (integer8 > 3) {
                        final int integer10 = 0;
                        final double double13 = -integer6 * double10;
                        dfj.pushPose();
                        dfj.translate(0.0, 0.0, 50.0);
                        GuiComponent.fill(dfj, -2, (int)(double13 - double10), 0 + integer4 + 4, (int)double13, integer9 << 24);
                        RenderSystem.enableBlend();
                        dfj.translate(0.0, 0.0, 50.0);
                        this.minecraft.font.drawShadow(dfj, djr20.getMessage(), 0.0f, (float)(int)(double13 + double11), 16777215 + (integer8 << 24));
                        RenderSystem.disableAlphaTest();
                        RenderSystem.disableBlend();
                        dfj.popPose();
                    }
                }
            }
        }
        if (!this.chatQueue.isEmpty()) {
            final int integer6 = (int)(128.0 * double8);
            final int integer11 = (int)(255.0 * double9);
            dfj.pushPose();
            dfj.translate(0.0, 0.0, 50.0);
            GuiComponent.fill(dfj, -2, 0, integer4 + 4, 9, integer11 << 24);
            RenderSystem.enableBlend();
            dfj.translate(0.0, 0.0, 50.0);
            this.minecraft.font.drawShadow(dfj, new TranslatableComponent("chat.queue", new Object[] { this.chatQueue.size() }), 0.0f, 1.0f, 16777215 + (integer6 << 24));
            dfj.popPose();
            RenderSystem.disableAlphaTest();
            RenderSystem.disableBlend();
        }
        if (boolean6) {
            this.minecraft.font.getClass();
            final int integer6 = 9;
            RenderSystem.translatef(-3.0f, 0.0f, 0.0f);
            final int integer11 = integer3 * integer6 + integer3;
            final int integer7 = integer5 * integer6 + integer5;
            final int integer12 = this.chatScrollbarPos * integer7 / integer3;
            final int integer13 = integer7 * integer7 / integer11;
            if (integer11 != integer7) {
                final int integer8 = (integer12 > 0) ? 170 : 96;
                final int integer9 = this.newMessageSinceScroll ? 13382451 : 3355562;
                GuiComponent.fill(dfj, 0, -integer12, 2, -integer12 - integer13, integer9 + (integer8 << 24));
                GuiComponent.fill(dfj, 2, -integer12, 1, -integer12 - integer13, 13421772 + (integer8 << 24));
            }
        }
        RenderSystem.popMatrix();
    }
    
    private boolean isChatHidden() {
        return this.minecraft.options.chatVisibility == ChatVisiblity.HIDDEN;
    }
    
    private static double getTimeFactor(final int integer) {
        double double2 = integer / 200.0;
        double2 = 1.0 - double2;
        double2 *= 10.0;
        double2 = Mth.clamp(double2, 0.0, 1.0);
        double2 *= double2;
        return double2;
    }
    
    public void clearMessages(final boolean boolean1) {
        this.chatQueue.clear();
        this.trimmedMessages.clear();
        this.allMessages.clear();
        if (boolean1) {
            this.recentChat.clear();
        }
    }
    
    public void addMessage(final Component nr) {
        this.addMessage(nr, 0);
    }
    
    private void addMessage(final Component nr, final int integer) {
        this.addMessage(nr, integer, this.minecraft.gui.getGuiTicks(), false);
        ChatComponent.LOGGER.info("[CHAT] {}", nr.getString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n"));
    }
    
    private void addMessage(final Component nr, final int integer2, final int integer3, final boolean boolean4) {
        if (integer2 != 0) {
            this.removeById(integer2);
        }
        final int integer4 = Mth.floor(this.getWidth() / this.getScale());
        final List<FormattedCharSequence> list7 = ComponentRenderUtils.wrapComponents(nr, integer4, this.minecraft.font);
        final boolean boolean5 = this.isChatFocused();
        for (final FormattedCharSequence aex10 : list7) {
            if (boolean5 && this.chatScrollbarPos > 0) {
                this.newMessageSinceScroll = true;
                this.scrollChat(1.0);
            }
            this.trimmedMessages.add(0, new GuiMessage(integer3, aex10, integer2));
        }
        while (this.trimmedMessages.size() > 100) {
            this.trimmedMessages.remove(this.trimmedMessages.size() - 1);
        }
        if (!boolean4) {
            this.allMessages.add(0, new GuiMessage(integer3, nr, integer2));
            while (this.allMessages.size() > 100) {
                this.allMessages.remove(this.allMessages.size() - 1);
            }
        }
    }
    
    public void rescaleChat() {
        this.trimmedMessages.clear();
        this.resetChatScroll();
        for (int integer2 = this.allMessages.size() - 1; integer2 >= 0; --integer2) {
            final GuiMessage<Component> djr3 = (GuiMessage<Component>)this.allMessages.get(integer2);
            this.addMessage(djr3.getMessage(), djr3.getId(), djr3.getAddedTime(), true);
        }
    }
    
    public List<String> getRecentChat() {
        return this.recentChat;
    }
    
    public void addRecentChat(final String string) {
        if (this.recentChat.isEmpty() || !((String)this.recentChat.get(this.recentChat.size() - 1)).equals(string)) {
            this.recentChat.add(string);
        }
    }
    
    public void resetChatScroll() {
        this.chatScrollbarPos = 0;
        this.newMessageSinceScroll = false;
    }
    
    public void scrollChat(final double double1) {
        this.chatScrollbarPos += (int)double1;
        final int integer4 = this.trimmedMessages.size();
        if (this.chatScrollbarPos > integer4 - this.getLinesPerPage()) {
            this.chatScrollbarPos = integer4 - this.getLinesPerPage();
        }
        if (this.chatScrollbarPos <= 0) {
            this.chatScrollbarPos = 0;
            this.newMessageSinceScroll = false;
        }
    }
    
    public boolean handleChatQueueClicked(final double double1, final double double2) {
        if (!this.isChatFocused() || this.minecraft.options.hideGui || this.isChatHidden() || this.chatQueue.isEmpty()) {
            return false;
        }
        final double double3 = double1 - 2.0;
        final double double4 = this.minecraft.getWindow().getGuiScaledHeight() - double2 - 40.0;
        if (double3 <= Mth.floor(this.getWidth() / this.getScale()) && double4 < 0.0 && double4 > Mth.floor(-9.0 * this.getScale())) {
            this.addMessage((Component)this.chatQueue.remove());
            this.lastMessage = System.currentTimeMillis();
            return true;
        }
        return false;
    }
    
    @Nullable
    public Style getClickedComponentStyleAt(final double double1, final double double2) {
        if (!this.isChatFocused() || this.minecraft.options.hideGui || this.isChatHidden()) {
            return null;
        }
        double double3 = double1 - 2.0;
        double double4 = this.minecraft.getWindow().getGuiScaledHeight() - double2 - 40.0;
        double3 = Mth.floor(double3 / this.getScale());
        double4 = Mth.floor(double4 / (this.getScale() * (this.minecraft.options.chatLineSpacing + 1.0)));
        if (double3 < 0.0 || double4 < 0.0) {
            return null;
        }
        final int integer10 = Math.min(this.getLinesPerPage(), this.trimmedMessages.size());
        if (double3 <= Mth.floor(this.getWidth() / this.getScale())) {
            final double n = double4;
            this.minecraft.font.getClass();
            if (n < 9 * integer10 + integer10) {
                final double n2 = double4;
                this.minecraft.font.getClass();
                final int integer11 = (int)(n2 / 9.0 + this.chatScrollbarPos);
                if (integer11 >= 0 && integer11 < this.trimmedMessages.size()) {
                    final GuiMessage<FormattedCharSequence> djr12 = (GuiMessage<FormattedCharSequence>)this.trimmedMessages.get(integer11);
                    return this.minecraft.font.getSplitter().componentStyleAtWidth(djr12.getMessage(), (int)double3);
                }
            }
        }
        return null;
    }
    
    private boolean isChatFocused() {
        return this.minecraft.screen instanceof ChatScreen;
    }
    
    private void removeById(final int integer) {
        this.trimmedMessages.removeIf(djr -> djr.getId() == integer);
        this.allMessages.removeIf(djr -> djr.getId() == integer);
    }
    
    public int getWidth() {
        return getWidth(this.minecraft.options.chatWidth);
    }
    
    public int getHeight() {
        return getHeight((this.isChatFocused() ? this.minecraft.options.chatHeightFocused : this.minecraft.options.chatHeightUnfocused) / (this.minecraft.options.chatLineSpacing + 1.0));
    }
    
    public double getScale() {
        return this.minecraft.options.chatScale;
    }
    
    public static int getWidth(final double double1) {
        final int integer3 = 320;
        final int integer4 = 40;
        return Mth.floor(double1 * 280.0 + 40.0);
    }
    
    public static int getHeight(final double double1) {
        final int integer3 = 180;
        final int integer4 = 20;
        return Mth.floor(double1 * 160.0 + 20.0);
    }
    
    public int getLinesPerPage() {
        return this.getHeight() / 9;
    }
    
    private long getChatRateMillis() {
        return (long)(this.minecraft.options.chatDelay * 1000.0);
    }
    
    private void processPendingMessages() {
        if (this.chatQueue.isEmpty()) {
            return;
        }
        final long long2 = System.currentTimeMillis();
        if (long2 - this.lastMessage >= this.getChatRateMillis()) {
            this.addMessage((Component)this.chatQueue.remove());
            this.lastMessage = long2;
        }
    }
    
    public void enqueueMessage(final Component nr) {
        if (this.minecraft.options.chatDelay <= 0.0) {
            this.addMessage(nr);
        }
        else {
            final long long3 = System.currentTimeMillis();
            if (long3 - this.lastMessage >= this.getChatRateMillis()) {
                this.addMessage(nr);
                this.lastMessage = long3;
            }
            else {
                this.chatQueue.add(nr);
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
