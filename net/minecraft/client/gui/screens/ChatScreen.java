package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Style;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import java.util.function.Consumer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;

public class ChatScreen extends Screen {
    private String historyBuffer;
    private int historyPos;
    protected EditBox input;
    private String initial;
    private CommandSuggestions commandSuggestions;
    
    public ChatScreen(final String string) {
        super(NarratorChatListener.NO_TITLE);
        this.historyBuffer = "";
        this.historyPos = -1;
        this.initial = "";
        this.initial = string;
    }
    
    @Override
    protected void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.historyPos = this.minecraft.gui.getChat().getRecentChat().size();
        (this.input = new EditBox(this.font, 4, this.height - 12, this.width - 4, 12, new TranslatableComponent("chat.editBox")) {
            @Override
            protected MutableComponent createNarrationMessage() {
                return super.createNarrationMessage().append(ChatScreen.this.commandSuggestions.getNarrationMessage());
            }
        }).setMaxLength(256);
        this.input.setBordered(false);
        this.input.setValue(this.initial);
        this.input.setResponder((Consumer<String>)this::onEdited);
        this.children.add(this.input);
        (this.commandSuggestions = new CommandSuggestions(this.minecraft, this, this.input, this.font, false, false, 1, 10, true, -805306368)).updateCommandInfo();
        this.setInitialFocus(this.input);
    }
    
    @Override
    public void resize(final Minecraft djw, final int integer2, final int integer3) {
        final String string5 = this.input.getValue();
        this.init(djw, integer2, integer3);
        this.setChatLine(string5);
        this.commandSuggestions.updateCommandInfo();
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
        this.minecraft.gui.getChat().resetChatScroll();
    }
    
    @Override
    public void tick() {
        this.input.tick();
    }
    
    private void onEdited(final String string) {
        final String string2 = this.input.getValue();
        this.commandSuggestions.setAllowSuggestions(!string2.equals(this.initial));
        this.commandSuggestions.updateCommandInfo();
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (this.commandSuggestions.keyPressed(integer1, integer2, integer3)) {
            return true;
        }
        if (super.keyPressed(integer1, integer2, integer3)) {
            return true;
        }
        if (integer1 == 256) {
            this.minecraft.setScreen(null);
            return true;
        }
        if (integer1 == 257 || integer1 == 335) {
            final String string5 = this.input.getValue().trim();
            if (!string5.isEmpty()) {
                this.sendMessage(string5);
            }
            this.minecraft.setScreen(null);
            return true;
        }
        if (integer1 == 265) {
            this.moveInHistory(-1);
            return true;
        }
        if (integer1 == 264) {
            this.moveInHistory(1);
            return true;
        }
        if (integer1 == 266) {
            this.minecraft.gui.getChat().scrollChat(this.minecraft.gui.getChat().getLinesPerPage() - 1);
            return true;
        }
        if (integer1 == 267) {
            this.minecraft.gui.getChat().scrollChat(-this.minecraft.gui.getChat().getLinesPerPage() + 1);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean mouseScrolled(final double double1, final double double2, double double3) {
        if (double3 > 1.0) {
            double3 = 1.0;
        }
        if (double3 < -1.0) {
            double3 = -1.0;
        }
        if (this.commandSuggestions.mouseScrolled(double3)) {
            return true;
        }
        if (!Screen.hasShiftDown()) {
            double3 *= 7.0;
        }
        this.minecraft.gui.getChat().scrollChat(double3);
        return true;
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (this.commandSuggestions.mouseClicked((int)double1, (int)double2, integer)) {
            return true;
        }
        if (integer == 0) {
            final ChatComponent dlh7 = this.minecraft.gui.getChat();
            if (dlh7.handleChatQueueClicked(double1, double2)) {
                return true;
            }
            final Style ob8 = dlh7.getClickedComponentStyleAt(double1, double2);
            if (ob8 != null && this.handleComponentClicked(ob8)) {
                return true;
            }
        }
        return this.input.mouseClicked(double1, double2, integer) || super.mouseClicked(double1, double2, integer);
    }
    
    @Override
    protected void insertText(final String string, final boolean boolean2) {
        if (boolean2) {
            this.input.setValue(string);
        }
        else {
            this.input.insertText(string);
        }
    }
    
    public void moveInHistory(final int integer) {
        int integer2 = this.historyPos + integer;
        final int integer3 = this.minecraft.gui.getChat().getRecentChat().size();
        integer2 = Mth.clamp(integer2, 0, integer3);
        if (integer2 == this.historyPos) {
            return;
        }
        if (integer2 == integer3) {
            this.historyPos = integer3;
            this.input.setValue(this.historyBuffer);
            return;
        }
        if (this.historyPos == integer3) {
            this.historyBuffer = this.input.getValue();
        }
        this.input.setValue((String)this.minecraft.gui.getChat().getRecentChat().get(integer2));
        this.commandSuggestions.setAllowSuggestions(false);
        this.historyPos = integer2;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.setFocused(this.input);
        this.input.setFocus(true);
        GuiComponent.fill(dfj, 2, this.height - 14, this.width - 2, this.height - 2, this.minecraft.options.getBackgroundColor(Integer.MIN_VALUE));
        this.input.render(dfj, integer2, integer3, float4);
        this.commandSuggestions.render(dfj, integer2, integer3);
        final Style ob6 = this.minecraft.gui.getChat().getClickedComponentStyleAt(integer2, integer3);
        if (ob6 != null && ob6.getHoverEvent() != null) {
            this.renderComponentHoverEffect(dfj, ob6, integer2, integer3);
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    private void setChatLine(final String string) {
        this.input.setValue(string);
    }
}
