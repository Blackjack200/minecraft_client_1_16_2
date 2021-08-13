package net.minecraft.client.gui.font;

import net.minecraft.client.StringSplitter;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.SharedConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TextFieldHelper {
    private final Supplier<String> getMessageFn;
    private final Consumer<String> setMessageFn;
    private final Supplier<String> getClipboardFn;
    private final Consumer<String> setClipboardFn;
    private final Predicate<String> stringValidator;
    private int cursorPos;
    private int selectionPos;
    
    public TextFieldHelper(final Supplier<String> supplier1, final Consumer<String> consumer2, final Supplier<String> supplier3, final Consumer<String> consumer4, final Predicate<String> predicate) {
        this.getMessageFn = supplier1;
        this.setMessageFn = consumer2;
        this.getClipboardFn = supplier3;
        this.setClipboardFn = consumer4;
        this.stringValidator = predicate;
        this.setCursorToEnd();
    }
    
    public static Supplier<String> createClipboardGetter(final Minecraft djw) {
        return (Supplier<String>)(() -> getClipboardContents(djw));
    }
    
    public static String getClipboardContents(final Minecraft djw) {
        return ChatFormatting.stripFormatting(djw.keyboardHandler.getClipboard().replaceAll("\\r", ""));
    }
    
    public static Consumer<String> createClipboardSetter(final Minecraft djw) {
        return (Consumer<String>)(string -> setClipboardContents(djw, string));
    }
    
    public static void setClipboardContents(final Minecraft djw, final String string) {
        djw.keyboardHandler.setClipboard(string);
    }
    
    public boolean charTyped(final char character) {
        if (SharedConstants.isAllowedChatCharacter(character)) {
            this.insertText((String)this.getMessageFn.get(), Character.toString(character));
        }
        return true;
    }
    
    public boolean keyPressed(final int integer) {
        if (Screen.isSelectAll(integer)) {
            this.selectAll();
            return true;
        }
        if (Screen.isCopy(integer)) {
            this.copy();
            return true;
        }
        if (Screen.isPaste(integer)) {
            this.paste();
            return true;
        }
        if (Screen.isCut(integer)) {
            this.cut();
            return true;
        }
        if (integer == 259) {
            this.removeCharsFromCursor(-1);
            return true;
        }
        if (integer == 261) {
            this.removeCharsFromCursor(1);
        }
        else {
            if (integer == 263) {
                if (Screen.hasControlDown()) {
                    this.moveByWords(-1, Screen.hasShiftDown());
                }
                else {
                    this.moveByChars(-1, Screen.hasShiftDown());
                }
                return true;
            }
            if (integer == 262) {
                if (Screen.hasControlDown()) {
                    this.moveByWords(1, Screen.hasShiftDown());
                }
                else {
                    this.moveByChars(1, Screen.hasShiftDown());
                }
                return true;
            }
            if (integer == 268) {
                this.setCursorToStart(Screen.hasShiftDown());
                return true;
            }
            if (integer == 269) {
                this.setCursorToEnd(Screen.hasShiftDown());
                return true;
            }
        }
        return false;
    }
    
    private int clampToMsgLength(final int integer) {
        return Mth.clamp(integer, 0, ((String)this.getMessageFn.get()).length());
    }
    
    private void insertText(String string1, final String string2) {
        if (this.selectionPos != this.cursorPos) {
            string1 = this.deleteSelection(string1);
        }
        this.cursorPos = Mth.clamp(this.cursorPos, 0, string1.length());
        final String string3 = new StringBuilder(string1).insert(this.cursorPos, string2).toString();
        if (this.stringValidator.test(string3)) {
            this.setMessageFn.accept(string3);
            final int min = Math.min(string3.length(), this.cursorPos + string2.length());
            this.cursorPos = min;
            this.selectionPos = min;
        }
    }
    
    public void insertText(final String string) {
        this.insertText((String)this.getMessageFn.get(), string);
    }
    
    private void resetSelectionIfNeeded(final boolean boolean1) {
        if (!boolean1) {
            this.selectionPos = this.cursorPos;
        }
    }
    
    public void moveByChars(final int integer, final boolean boolean2) {
        this.cursorPos = Util.offsetByCodepoints((String)this.getMessageFn.get(), this.cursorPos, integer);
        this.resetSelectionIfNeeded(boolean2);
    }
    
    public void moveByWords(final int integer, final boolean boolean2) {
        this.cursorPos = StringSplitter.getWordPosition((String)this.getMessageFn.get(), integer, this.cursorPos, true);
        this.resetSelectionIfNeeded(boolean2);
    }
    
    public void removeCharsFromCursor(final int integer) {
        final String string3 = (String)this.getMessageFn.get();
        if (!string3.isEmpty()) {
            String string4;
            if (this.selectionPos != this.cursorPos) {
                string4 = this.deleteSelection(string3);
            }
            else {
                final int integer2 = Util.offsetByCodepoints(string3, this.cursorPos, integer);
                final int integer3 = Math.min(integer2, this.cursorPos);
                final int integer4 = Math.max(integer2, this.cursorPos);
                string4 = new StringBuilder(string3).delete(integer3, integer4).toString();
                if (integer < 0) {
                    final int n = integer3;
                    this.cursorPos = n;
                    this.selectionPos = n;
                }
            }
            this.setMessageFn.accept(string4);
        }
    }
    
    public void cut() {
        final String string2 = (String)this.getMessageFn.get();
        this.setClipboardFn.accept(this.getSelected(string2));
        this.setMessageFn.accept(this.deleteSelection(string2));
    }
    
    public void paste() {
        this.insertText((String)this.getMessageFn.get(), (String)this.getClipboardFn.get());
        this.selectionPos = this.cursorPos;
    }
    
    public void copy() {
        this.setClipboardFn.accept(this.getSelected((String)this.getMessageFn.get()));
    }
    
    public void selectAll() {
        this.selectionPos = 0;
        this.cursorPos = ((String)this.getMessageFn.get()).length();
    }
    
    private String getSelected(final String string) {
        final int integer3 = Math.min(this.cursorPos, this.selectionPos);
        final int integer4 = Math.max(this.cursorPos, this.selectionPos);
        return string.substring(integer3, integer4);
    }
    
    private String deleteSelection(final String string) {
        if (this.selectionPos == this.cursorPos) {
            return string;
        }
        final int integer3 = Math.min(this.cursorPos, this.selectionPos);
        final int integer4 = Math.max(this.cursorPos, this.selectionPos);
        final String string2 = string.substring(0, integer3) + string.substring(integer4);
        final int n = integer3;
        this.cursorPos = n;
        this.selectionPos = n;
        return string2;
    }
    
    private void setCursorToStart(final boolean boolean1) {
        this.cursorPos = 0;
        this.resetSelectionIfNeeded(boolean1);
    }
    
    public void setCursorToEnd() {
        this.setCursorToEnd(false);
    }
    
    private void setCursorToEnd(final boolean boolean1) {
        this.cursorPos = ((String)this.getMessageFn.get()).length();
        this.resetSelectionIfNeeded(boolean1);
    }
    
    public int getCursorPos() {
        return this.cursorPos;
    }
    
    public void setCursorPos(final int integer, final boolean boolean2) {
        this.cursorPos = this.clampToMsgLength(integer);
        this.resetSelectionIfNeeded(boolean2);
    }
    
    public int getSelectionPos() {
        return this.selectionPos;
    }
    
    public void setSelectionRange(final int integer1, final int integer2) {
        final int integer3 = ((String)this.getMessageFn.get()).length();
        this.cursorPos = Mth.clamp(integer1, 0, integer3);
        this.selectionPos = Mth.clamp(integer2, 0, integer3);
    }
    
    public boolean isSelecting() {
        return this.cursorPos != this.selectionPos;
    }
}
