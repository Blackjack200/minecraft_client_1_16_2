package net.minecraft.client.gui.components;

import net.minecraft.network.chat.Style;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.Util;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.MutableComponent;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Consumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.events.GuiEventListener;

public class EditBox extends AbstractWidget implements Widget, GuiEventListener {
    private final Font font;
    private String value;
    private int maxLength;
    private int frame;
    private boolean bordered;
    private boolean canLoseFocus;
    private boolean isEditable;
    private boolean shiftPressed;
    private int displayPos;
    private int cursorPos;
    private int highlightPos;
    private int textColor;
    private int textColorUneditable;
    private String suggestion;
    private Consumer<String> responder;
    private Predicate<String> filter;
    private BiFunction<String, Integer, FormattedCharSequence> formatter;
    
    public EditBox(final Font dkr, final int integer2, final int integer3, final int integer4, final int integer5, final Component nr) {
        this(dkr, integer2, integer3, integer4, integer5, null, nr);
    }
    
    public EditBox(final Font dkr, final int integer2, final int integer3, final int integer4, final int integer5, @Nullable final EditBox dln, final Component nr) {
        super(integer2, integer3, integer4, integer5, nr);
        this.value = "";
        this.maxLength = 32;
        this.bordered = true;
        this.canLoseFocus = true;
        this.isEditable = true;
        this.textColor = 14737632;
        this.textColorUneditable = 7368816;
        this.filter = (Predicate<String>)Objects::nonNull;
        this.formatter = (BiFunction<String, Integer, FormattedCharSequence>)((string, integer) -> FormattedCharSequence.forward(string, Style.EMPTY));
        this.font = dkr;
        if (dln != null) {
            this.setValue(dln.getValue());
        }
    }
    
    public void setResponder(final Consumer<String> consumer) {
        this.responder = consumer;
    }
    
    public void setFormatter(final BiFunction<String, Integer, FormattedCharSequence> biFunction) {
        this.formatter = biFunction;
    }
    
    public void tick() {
        ++this.frame;
    }
    
    @Override
    protected MutableComponent createNarrationMessage() {
        final Component nr2 = this.getMessage();
        return new TranslatableComponent("gui.narrate.editBox", new Object[] { nr2, this.value });
    }
    
    public void setValue(final String string) {
        if (!this.filter.test(string)) {
            return;
        }
        if (string.length() > this.maxLength) {
            this.value = string.substring(0, this.maxLength);
        }
        else {
            this.value = string;
        }
        this.moveCursorToEnd();
        this.setHighlightPos(this.cursorPos);
        this.onValueChange(string);
    }
    
    public String getValue() {
        return this.value;
    }
    
    public String getHighlighted() {
        final int integer2 = (this.cursorPos < this.highlightPos) ? this.cursorPos : this.highlightPos;
        final int integer3 = (this.cursorPos < this.highlightPos) ? this.highlightPos : this.cursorPos;
        return this.value.substring(integer2, integer3);
    }
    
    public void setFilter(final Predicate<String> predicate) {
        this.filter = predicate;
    }
    
    public void insertText(final String string) {
        final int integer3 = (this.cursorPos < this.highlightPos) ? this.cursorPos : this.highlightPos;
        final int integer4 = (this.cursorPos < this.highlightPos) ? this.highlightPos : this.cursorPos;
        final int integer5 = this.maxLength - this.value.length() - (integer3 - integer4);
        String string2 = SharedConstants.filterText(string);
        int integer6 = string2.length();
        if (integer5 < integer6) {
            string2 = string2.substring(0, integer5);
            integer6 = integer5;
        }
        final String string3 = new StringBuilder(this.value).replace(integer3, integer4, string2).toString();
        if (!this.filter.test(string3)) {
            return;
        }
        this.value = string3;
        this.setCursorPosition(integer3 + integer6);
        this.setHighlightPos(this.cursorPos);
        this.onValueChange(this.value);
    }
    
    private void onValueChange(final String string) {
        if (this.responder != null) {
            this.responder.accept(string);
        }
        this.nextNarration = Util.getMillis() + 500L;
    }
    
    private void deleteText(final int integer) {
        if (Screen.hasControlDown()) {
            this.deleteWords(integer);
        }
        else {
            this.deleteChars(integer);
        }
    }
    
    public void deleteWords(final int integer) {
        if (this.value.isEmpty()) {
            return;
        }
        if (this.highlightPos != this.cursorPos) {
            this.insertText("");
            return;
        }
        this.deleteChars(this.getWordPosition(integer) - this.cursorPos);
    }
    
    public void deleteChars(final int integer) {
        if (this.value.isEmpty()) {
            return;
        }
        if (this.highlightPos != this.cursorPos) {
            this.insertText("");
            return;
        }
        final int integer2 = this.getCursorPos(integer);
        final int integer3 = Math.min(integer2, this.cursorPos);
        final int integer4 = Math.max(integer2, this.cursorPos);
        if (integer3 == integer4) {
            return;
        }
        final String string6 = new StringBuilder(this.value).delete(integer3, integer4).toString();
        if (!this.filter.test(string6)) {
            return;
        }
        this.value = string6;
        this.moveCursorTo(integer3);
    }
    
    public int getWordPosition(final int integer) {
        return this.getWordPosition(integer, this.getCursorPosition());
    }
    
    private int getWordPosition(final int integer1, final int integer2) {
        return this.getWordPosition(integer1, integer2, true);
    }
    
    private int getWordPosition(final int integer1, final int integer2, final boolean boolean3) {
        int integer3 = integer2;
        final boolean boolean4 = integer1 < 0;
        for (int integer4 = Math.abs(integer1), integer5 = 0; integer5 < integer4; ++integer5) {
            if (boolean4) {
                while (boolean3 && integer3 > 0 && this.value.charAt(integer3 - 1) == ' ') {
                    --integer3;
                }
                while (integer3 > 0 && this.value.charAt(integer3 - 1) != ' ') {
                    --integer3;
                }
            }
            else {
                final int integer6 = this.value.length();
                integer3 = this.value.indexOf(32, integer3);
                if (integer3 == -1) {
                    integer3 = integer6;
                }
                else {
                    while (boolean3 && integer3 < integer6 && this.value.charAt(integer3) == ' ') {
                        ++integer3;
                    }
                }
            }
        }
        return integer3;
    }
    
    public void moveCursor(final int integer) {
        this.moveCursorTo(this.getCursorPos(integer));
    }
    
    private int getCursorPos(final int integer) {
        return Util.offsetByCodepoints(this.value, this.cursorPos, integer);
    }
    
    public void moveCursorTo(final int integer) {
        this.setCursorPosition(integer);
        if (!this.shiftPressed) {
            this.setHighlightPos(this.cursorPos);
        }
        this.onValueChange(this.value);
    }
    
    public void setCursorPosition(final int integer) {
        this.cursorPos = Mth.clamp(integer, 0, this.value.length());
    }
    
    public void moveCursorToStart() {
        this.moveCursorTo(0);
    }
    
    public void moveCursorToEnd() {
        this.moveCursorTo(this.value.length());
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (!this.canConsumeInput()) {
            return false;
        }
        this.shiftPressed = Screen.hasShiftDown();
        if (Screen.isSelectAll(integer1)) {
            this.moveCursorToEnd();
            this.setHighlightPos(0);
            return true;
        }
        if (Screen.isCopy(integer1)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
            return true;
        }
        if (Screen.isPaste(integer1)) {
            if (this.isEditable) {
                this.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
            }
            return true;
        }
        if (Screen.isCut(integer1)) {
            Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
            if (this.isEditable) {
                this.insertText("");
            }
            return true;
        }
        switch (integer1) {
            case 263: {
                if (Screen.hasControlDown()) {
                    this.moveCursorTo(this.getWordPosition(-1));
                }
                else {
                    this.moveCursor(-1);
                }
                return true;
            }
            case 262: {
                if (Screen.hasControlDown()) {
                    this.moveCursorTo(this.getWordPosition(1));
                }
                else {
                    this.moveCursor(1);
                }
                return true;
            }
            case 259: {
                if (this.isEditable) {
                    this.shiftPressed = false;
                    this.deleteText(-1);
                    this.shiftPressed = Screen.hasShiftDown();
                }
                return true;
            }
            case 261: {
                if (this.isEditable) {
                    this.shiftPressed = false;
                    this.deleteText(1);
                    this.shiftPressed = Screen.hasShiftDown();
                }
                return true;
            }
            case 268: {
                this.moveCursorToStart();
                return true;
            }
            case 269: {
                this.moveCursorToEnd();
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public boolean canConsumeInput() {
        return this.isVisible() && this.isFocused() && this.isEditable();
    }
    
    @Override
    public boolean charTyped(final char character, final int integer) {
        if (!this.canConsumeInput()) {
            return false;
        }
        if (SharedConstants.isAllowedChatCharacter(character)) {
            if (this.isEditable) {
                this.insertText(Character.toString(character));
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (!this.isVisible()) {
            return false;
        }
        final boolean boolean7 = double1 >= this.x && double1 < this.x + this.width && double2 >= this.y && double2 < this.y + this.height;
        if (this.canLoseFocus) {
            this.setFocus(boolean7);
        }
        if (this.isFocused() && boolean7 && integer == 0) {
            int integer2 = Mth.floor(double1) - this.x;
            if (this.bordered) {
                integer2 -= 4;
            }
            final String string9 = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
            this.moveCursorTo(this.font.plainSubstrByWidth(string9, integer2).length() + this.displayPos);
            return true;
        }
        return false;
    }
    
    public void setFocus(final boolean boolean1) {
        super.setFocused(boolean1);
    }
    
    @Override
    public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (!this.isVisible()) {
            return;
        }
        if (this.isBordered()) {
            final int integer4 = this.isFocused() ? -1 : -6250336;
            GuiComponent.fill(dfj, this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, integer4);
            GuiComponent.fill(dfj, this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
        }
        final int integer4 = this.isEditable ? this.textColor : this.textColorUneditable;
        final int integer5 = this.cursorPos - this.displayPos;
        int integer6 = this.highlightPos - this.displayPos;
        final String string9 = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
        final boolean boolean10 = integer5 >= 0 && integer5 <= string9.length();
        final boolean boolean11 = this.isFocused() && this.frame / 6 % 2 == 0 && boolean10;
        final int integer7 = this.bordered ? (this.x + 4) : this.x;
        final int integer8 = this.bordered ? (this.y + (this.height - 8) / 2) : this.y;
        int integer9 = integer7;
        if (integer6 > string9.length()) {
            integer6 = string9.length();
        }
        if (!string9.isEmpty()) {
            final String string10 = boolean10 ? string9.substring(0, integer5) : string9;
            integer9 = this.font.drawShadow(dfj, (FormattedCharSequence)this.formatter.apply(string10, this.displayPos), (float)integer9, (float)integer8, integer4);
        }
        final boolean boolean12 = this.cursorPos < this.value.length() || this.value.length() >= this.getMaxLength();
        int integer10 = integer9;
        if (!boolean10) {
            integer10 = ((integer5 > 0) ? (integer7 + this.width) : integer7);
        }
        else if (boolean12) {
            --integer10;
            --integer9;
        }
        if (!string9.isEmpty() && boolean10 && integer5 < string9.length()) {
            this.font.drawShadow(dfj, (FormattedCharSequence)this.formatter.apply(string9.substring(integer5), this.cursorPos), (float)integer9, (float)integer8, integer4);
        }
        if (!boolean12 && this.suggestion != null) {
            this.font.drawShadow(dfj, this.suggestion, (float)(integer10 - 1), (float)integer8, -8355712);
        }
        if (boolean11) {
            if (boolean12) {
                final int integer12 = integer10;
                final int integer13 = integer8 - 1;
                final int integer14 = integer10 + 1;
                final int n = integer8 + 1;
                this.font.getClass();
                GuiComponent.fill(dfj, integer12, integer13, integer14, n + 9, -3092272);
            }
            else {
                this.font.drawShadow(dfj, "_", (float)integer10, (float)integer8, integer4);
            }
        }
        if (integer6 != integer5) {
            final int integer11 = integer7 + this.font.width(string9.substring(0, integer6));
            final int integer15 = integer10;
            final int integer16 = integer8 - 1;
            final int integer17 = integer11 - 1;
            final int n2 = integer8 + 1;
            this.font.getClass();
            this.renderHighlight(integer15, integer16, integer17, n2 + 9);
        }
    }
    
    private void renderHighlight(int integer1, int integer2, int integer3, int integer4) {
        if (integer1 < integer3) {
            final int integer5 = integer1;
            integer1 = integer3;
            integer3 = integer5;
        }
        if (integer2 < integer4) {
            final int integer5 = integer2;
            integer2 = integer4;
            integer4 = integer5;
        }
        if (integer3 > this.x + this.width) {
            integer3 = this.x + this.width;
        }
        if (integer1 > this.x + this.width) {
            integer1 = this.x + this.width;
        }
        final Tesselator dfl6 = Tesselator.getInstance();
        final BufferBuilder dfe7 = dfl6.getBuilder();
        RenderSystem.color4f(0.0f, 0.0f, 255.0f, 255.0f);
        RenderSystem.disableTexture();
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        dfe7.begin(7, DefaultVertexFormat.POSITION);
        dfe7.vertex(integer1, integer4, 0.0).endVertex();
        dfe7.vertex(integer3, integer4, 0.0).endVertex();
        dfe7.vertex(integer3, integer2, 0.0).endVertex();
        dfe7.vertex(integer1, integer2, 0.0).endVertex();
        dfl6.end();
        RenderSystem.disableColorLogicOp();
        RenderSystem.enableTexture();
    }
    
    public void setMaxLength(final int integer) {
        this.maxLength = integer;
        if (this.value.length() > integer) {
            this.onValueChange(this.value = this.value.substring(0, integer));
        }
    }
    
    private int getMaxLength() {
        return this.maxLength;
    }
    
    public int getCursorPosition() {
        return this.cursorPos;
    }
    
    private boolean isBordered() {
        return this.bordered;
    }
    
    public void setBordered(final boolean boolean1) {
        this.bordered = boolean1;
    }
    
    public void setTextColor(final int integer) {
        this.textColor = integer;
    }
    
    public void setTextColorUneditable(final int integer) {
        this.textColorUneditable = integer;
    }
    
    @Override
    public boolean changeFocus(final boolean boolean1) {
        return this.visible && this.isEditable && super.changeFocus(boolean1);
    }
    
    @Override
    public boolean isMouseOver(final double double1, final double double2) {
        return this.visible && double1 >= this.x && double1 < this.x + this.width && double2 >= this.y && double2 < this.y + this.height;
    }
    
    @Override
    protected void onFocusedChanged(final boolean boolean1) {
        if (boolean1) {
            this.frame = 0;
        }
    }
    
    private boolean isEditable() {
        return this.isEditable;
    }
    
    public void setEditable(final boolean boolean1) {
        this.isEditable = boolean1;
    }
    
    public int getInnerWidth() {
        return this.isBordered() ? (this.width - 8) : this.width;
    }
    
    public void setHighlightPos(final int integer) {
        final int integer2 = this.value.length();
        this.highlightPos = Mth.clamp(integer, 0, integer2);
        if (this.font != null) {
            if (this.displayPos > integer2) {
                this.displayPos = integer2;
            }
            final int integer3 = this.getInnerWidth();
            final String string5 = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), integer3);
            final int integer4 = string5.length() + this.displayPos;
            if (this.highlightPos == this.displayPos) {
                this.displayPos -= this.font.plainSubstrByWidth(this.value, integer3, true).length();
            }
            if (this.highlightPos > integer4) {
                this.displayPos += this.highlightPos - integer4;
            }
            else if (this.highlightPos <= this.displayPos) {
                this.displayPos -= this.displayPos - this.highlightPos;
            }
            this.displayPos = Mth.clamp(this.displayPos, 0, integer2);
        }
    }
    
    public void setCanLoseFocus(final boolean boolean1) {
        this.canLoseFocus = boolean1;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean boolean1) {
        this.visible = boolean1;
    }
    
    public void setSuggestion(@Nullable final String string) {
        this.suggestion = string;
    }
    
    public int getScreenX(final int integer) {
        if (integer > this.value.length()) {
            return this.x;
        }
        return this.x + this.font.width(this.value.substring(0, integer));
    }
    
    public void setX(final int integer) {
        this.x = integer;
    }
}
