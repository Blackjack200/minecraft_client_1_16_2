package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.gui.Font;
import java.util.Arrays;
import it.unimi.dsi.fastutil.ints.IntList;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.client.StringSplitter;
import net.minecraft.Util;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.components.events.GuiEventListener;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.SharedConstants;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundEditBookPacket;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import java.util.ListIterator;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import java.util.function.Predicate;
import java.util.function.Consumer;
import java.util.function.Supplier;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.chat.NarratorChatListener;
import javax.annotation.Nullable;
import net.minecraft.world.InteractionHand;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.font.TextFieldHelper;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;

public class BookEditScreen extends Screen {
    private static final Component EDIT_TITLE_LABEL;
    private static final Component FINALIZE_WARNING_LABEL;
    private static final FormattedCharSequence BLACK_CURSOR;
    private static final FormattedCharSequence GRAY_CURSOR;
    private final Player owner;
    private final ItemStack book;
    private boolean isModified;
    private boolean isSigning;
    private int frameTick;
    private int currentPage;
    private final List<String> pages;
    private String title;
    private final TextFieldHelper pageEdit;
    private final TextFieldHelper titleEdit;
    private long lastClickTime;
    private int lastIndex;
    private PageButton forwardButton;
    private PageButton backButton;
    private Button doneButton;
    private Button signButton;
    private Button finalizeButton;
    private Button cancelButton;
    private final InteractionHand hand;
    @Nullable
    private DisplayCache displayCache;
    private Component pageMsg;
    private final Component ownerText;
    
    public BookEditScreen(final Player bft, final ItemStack bly, final InteractionHand aoq) {
        super(NarratorChatListener.NO_TITLE);
        this.pages = (List<String>)Lists.newArrayList();
        this.title = "";
        this.pageEdit = new TextFieldHelper((Supplier<String>)this::getCurrentPageText, (Consumer<String>)this::setCurrentPageText, (Supplier<String>)this::getClipboard, (Consumer<String>)this::setClipboard, (Predicate<String>)(string -> string.length() < 1024 && this.font.wordWrapHeight(string, 114) <= 128));
        this.titleEdit = new TextFieldHelper((Supplier<String>)(() -> this.title), (Consumer<String>)(string -> this.title = string), (Supplier<String>)this::getClipboard, (Consumer<String>)this::setClipboard, (Predicate<String>)(string -> string.length() < 16));
        this.lastIndex = -1;
        this.displayCache = DisplayCache.EMPTY;
        this.pageMsg = TextComponent.EMPTY;
        this.owner = bft;
        this.book = bly;
        this.hand = aoq;
        final CompoundTag md5 = bly.getTag();
        if (md5 != null) {
            final ListTag mj6 = md5.getList("pages", 8).copy();
            for (int integer7 = 0; integer7 < mj6.size(); ++integer7) {
                this.pages.add(mj6.getString(integer7));
            }
        }
        if (this.pages.isEmpty()) {
            this.pages.add("");
        }
        this.ownerText = new TranslatableComponent("book.byAuthor", new Object[] { bft.getName() }).withStyle(ChatFormatting.DARK_GRAY);
    }
    
    private void setClipboard(final String string) {
        if (this.minecraft != null) {
            TextFieldHelper.setClipboardContents(this.minecraft, string);
        }
    }
    
    private String getClipboard() {
        return (this.minecraft != null) ? TextFieldHelper.getClipboardContents(this.minecraft) : "";
    }
    
    private int getNumPages() {
        return this.pages.size();
    }
    
    @Override
    public void tick() {
        super.tick();
        ++this.frameTick;
    }
    
    @Override
    protected void init() {
        this.clearDisplayCache();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.signButton = this.<Button>addButton(new Button(this.width / 2 - 100, 196, 98, 20, new TranslatableComponent("book.signButton"), dlg -> {
            this.isSigning = true;
            this.updateButtonVisibility();
            return;
        }));
        this.doneButton = this.<Button>addButton(new Button(this.width / 2 + 2, 196, 98, 20, CommonComponents.GUI_DONE, dlg -> {
            this.minecraft.setScreen(null);
            this.saveChanges(false);
            return;
        }));
        this.finalizeButton = this.<Button>addButton(new Button(this.width / 2 - 100, 196, 98, 20, new TranslatableComponent("book.finalizeButton"), dlg -> {
            if (this.isSigning) {
                this.saveChanges(true);
                this.minecraft.setScreen(null);
            }
            return;
        }));
        this.cancelButton = this.<Button>addButton(new Button(this.width / 2 + 2, 196, 98, 20, CommonComponents.GUI_CANCEL, dlg -> {
            if (this.isSigning) {
                this.isSigning = false;
            }
            this.updateButtonVisibility();
            return;
        }));
        final int integer2 = (this.width - 192) / 2;
        final int integer3 = 2;
        this.forwardButton = this.<PageButton>addButton(new PageButton(integer2 + 116, 159, true, dlg -> this.pageForward(), true));
        this.backButton = this.<PageButton>addButton(new PageButton(integer2 + 43, 159, false, dlg -> this.pageBack(), true));
        this.updateButtonVisibility();
    }
    
    private void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }
        this.updateButtonVisibility();
        this.clearDisplayCacheAfterPageChange();
    }
    
    private void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        }
        else {
            this.appendPageToBook();
            if (this.currentPage < this.getNumPages() - 1) {
                ++this.currentPage;
            }
        }
        this.updateButtonVisibility();
        this.clearDisplayCacheAfterPageChange();
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    private void updateButtonVisibility() {
        this.backButton.visible = (!this.isSigning && this.currentPage > 0);
        this.forwardButton.visible = !this.isSigning;
        this.doneButton.visible = !this.isSigning;
        this.signButton.visible = !this.isSigning;
        this.cancelButton.visible = this.isSigning;
        this.finalizeButton.visible = this.isSigning;
        this.finalizeButton.active = !this.title.trim().isEmpty();
    }
    
    private void eraseEmptyTrailingPages() {
        final ListIterator<String> listIterator2 = (ListIterator<String>)this.pages.listIterator(this.pages.size());
        while (listIterator2.hasPrevious() && ((String)listIterator2.previous()).isEmpty()) {
            listIterator2.remove();
        }
    }
    
    private void saveChanges(final boolean boolean1) {
        if (!this.isModified) {
            return;
        }
        this.eraseEmptyTrailingPages();
        final ListTag mj3 = new ListTag();
        this.pages.stream().map(StringTag::valueOf).forEach(mj3::add);
        if (!this.pages.isEmpty()) {
            this.book.addTagElement("pages", (Tag)mj3);
        }
        if (boolean1) {
            this.book.addTagElement("author", (Tag)StringTag.valueOf(this.owner.getGameProfile().getName()));
            this.book.addTagElement("title", (Tag)StringTag.valueOf(this.title.trim()));
        }
        this.minecraft.getConnection().send(new ServerboundEditBookPacket(this.book, boolean1, this.hand));
    }
    
    private void appendPageToBook() {
        if (this.getNumPages() >= 100) {
            return;
        }
        this.pages.add("");
        this.isModified = true;
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (super.keyPressed(integer1, integer2, integer3)) {
            return true;
        }
        if (this.isSigning) {
            return this.titleKeyPressed(integer1, integer2, integer3);
        }
        final boolean boolean5 = this.bookKeyPressed(integer1, integer2, integer3);
        if (boolean5) {
            this.clearDisplayCache();
            return true;
        }
        return false;
    }
    
    @Override
    public boolean charTyped(final char character, final int integer) {
        if (super.charTyped(character, integer)) {
            return true;
        }
        if (this.isSigning) {
            final boolean boolean4 = this.titleEdit.charTyped(character);
            if (boolean4) {
                this.updateButtonVisibility();
                return this.isModified = true;
            }
            return false;
        }
        else {
            if (SharedConstants.isAllowedChatCharacter(character)) {
                this.pageEdit.insertText(Character.toString(character));
                this.clearDisplayCache();
                return true;
            }
            return false;
        }
    }
    
    private boolean bookKeyPressed(final int integer1, final int integer2, final int integer3) {
        if (Screen.isSelectAll(integer1)) {
            this.pageEdit.selectAll();
            return true;
        }
        if (Screen.isCopy(integer1)) {
            this.pageEdit.copy();
            return true;
        }
        if (Screen.isPaste(integer1)) {
            this.pageEdit.paste();
            return true;
        }
        if (Screen.isCut(integer1)) {
            this.pageEdit.cut();
            return true;
        }
        switch (integer1) {
            case 259: {
                this.pageEdit.removeCharsFromCursor(-1);
                return true;
            }
            case 261: {
                this.pageEdit.removeCharsFromCursor(1);
                return true;
            }
            case 257:
            case 335: {
                this.pageEdit.insertText("\n");
                return true;
            }
            case 263: {
                this.pageEdit.moveByChars(-1, Screen.hasShiftDown());
                return true;
            }
            case 262: {
                this.pageEdit.moveByChars(1, Screen.hasShiftDown());
                return true;
            }
            case 265: {
                this.keyUp();
                return true;
            }
            case 264: {
                this.keyDown();
                return true;
            }
            case 266: {
                this.backButton.onPress();
                return true;
            }
            case 267: {
                this.forwardButton.onPress();
                return true;
            }
            case 268: {
                this.keyHome();
                return true;
            }
            case 269: {
                this.keyEnd();
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    private void keyUp() {
        this.changeLine(-1);
    }
    
    private void keyDown() {
        this.changeLine(1);
    }
    
    private void changeLine(final int integer) {
        final int integer2 = this.pageEdit.getCursorPos();
        final int integer3 = this.getDisplayCache().changeLine(integer2, integer);
        this.pageEdit.setCursorPos(integer3, Screen.hasShiftDown());
    }
    
    private void keyHome() {
        final int integer2 = this.pageEdit.getCursorPos();
        final int integer3 = this.getDisplayCache().findLineStart(integer2);
        this.pageEdit.setCursorPos(integer3, Screen.hasShiftDown());
    }
    
    private void keyEnd() {
        final DisplayCache a2 = this.getDisplayCache();
        final int integer3 = this.pageEdit.getCursorPos();
        final int integer4 = a2.findLineEnd(integer3);
        this.pageEdit.setCursorPos(integer4, Screen.hasShiftDown());
    }
    
    private boolean titleKeyPressed(final int integer1, final int integer2, final int integer3) {
        switch (integer1) {
            case 259: {
                this.titleEdit.removeCharsFromCursor(-1);
                this.updateButtonVisibility();
                return this.isModified = true;
            }
            case 257:
            case 335: {
                if (!this.title.isEmpty()) {
                    this.saveChanges(true);
                    this.minecraft.setScreen(null);
                }
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    private String getCurrentPageText() {
        if (this.currentPage >= 0 && this.currentPage < this.pages.size()) {
            return (String)this.pages.get(this.currentPage);
        }
        return "";
    }
    
    private void setCurrentPageText(final String string) {
        if (this.currentPage >= 0 && this.currentPage < this.pages.size()) {
            this.pages.set(this.currentPage, string);
            this.isModified = true;
            this.clearDisplayCache();
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.setFocused(null);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(BookViewScreen.BOOK_LOCATION);
        final int integer4 = (this.width - 192) / 2;
        final int integer5 = 2;
        this.blit(dfj, integer4, 2, 0, 0, 192, 192);
        if (this.isSigning) {
            final boolean boolean8 = this.frameTick / 6 % 2 == 0;
            final FormattedCharSequence aex9 = FormattedCharSequence.composite(FormattedCharSequence.forward(this.title, Style.EMPTY), boolean8 ? BookEditScreen.BLACK_CURSOR : BookEditScreen.GRAY_CURSOR);
            final int integer6 = this.font.width(BookEditScreen.EDIT_TITLE_LABEL);
            this.font.draw(dfj, BookEditScreen.EDIT_TITLE_LABEL, (float)(integer4 + 36 + (114 - integer6) / 2), 34.0f, 0);
            final int integer7 = this.font.width(aex9);
            this.font.draw(dfj, aex9, (float)(integer4 + 36 + (114 - integer7) / 2), 50.0f, 0);
            final int integer8 = this.font.width(this.ownerText);
            this.font.draw(dfj, this.ownerText, (float)(integer4 + 36 + (114 - integer8) / 2), 60.0f, 0);
            this.font.drawWordWrap(BookEditScreen.FINALIZE_WARNING_LABEL, integer4 + 36, 82, 114, 0);
        }
        else {
            final int integer9 = this.font.width(this.pageMsg);
            this.font.draw(dfj, this.pageMsg, (float)(integer4 - integer9 + 192 - 44), 18.0f, 0);
            final DisplayCache a9 = this.getDisplayCache();
            for (final LineInfo b13 : a9.lines) {
                this.font.draw(dfj, b13.asComponent, (float)b13.x, (float)b13.y, -16777216);
            }
            this.renderHighlight(a9.selection);
            this.renderCursor(dfj, a9.cursor, a9.cursorAtEnd);
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    private void renderCursor(final PoseStack dfj, Pos2i c, final boolean boolean3) {
        if (this.frameTick / 6 % 2 == 0) {
            c = this.convertLocalToScreen(c);
            if (!boolean3) {
                final int x = c.x;
                final int integer3 = c.y - 1;
                final int integer4 = c.x + 1;
                final int y = c.y;
                this.font.getClass();
                GuiComponent.fill(dfj, x, integer3, integer4, y + 9, -16777216);
            }
            else {
                this.font.draw(dfj, "_", (float)c.x, (float)c.y, 0);
            }
        }
    }
    
    private void renderHighlight(final Rect2i[] arr) {
        final Tesselator dfl3 = Tesselator.getInstance();
        final BufferBuilder dfe4 = dfl3.getBuilder();
        RenderSystem.color4f(0.0f, 0.0f, 255.0f, 255.0f);
        RenderSystem.disableTexture();
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        dfe4.begin(7, DefaultVertexFormat.POSITION);
        for (final Rect2i ead8 : arr) {
            final int integer9 = ead8.getX();
            final int integer10 = ead8.getY();
            final int integer11 = integer9 + ead8.getWidth();
            final int integer12 = integer10 + ead8.getHeight();
            dfe4.vertex(integer9, integer12, 0.0).endVertex();
            dfe4.vertex(integer11, integer12, 0.0).endVertex();
            dfe4.vertex(integer11, integer10, 0.0).endVertex();
            dfe4.vertex(integer9, integer10, 0.0).endVertex();
        }
        dfl3.end();
        RenderSystem.disableColorLogicOp();
        RenderSystem.enableTexture();
    }
    
    private Pos2i convertScreenToLocal(final Pos2i c) {
        return new Pos2i(c.x - (this.width - 192) / 2 - 36, c.y - 32);
    }
    
    private Pos2i convertLocalToScreen(final Pos2i c) {
        return new Pos2i(c.x + (this.width - 192) / 2 + 36, c.y + 32);
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (super.mouseClicked(double1, double2, integer)) {
            return true;
        }
        if (integer == 0) {
            final long long7 = Util.getMillis();
            final DisplayCache a9 = this.getDisplayCache();
            final int integer2 = a9.getIndexAtPosition(this.font, this.convertScreenToLocal(new Pos2i((int)double1, (int)double2)));
            if (integer2 >= 0) {
                if (integer2 == this.lastIndex && long7 - this.lastClickTime < 250L) {
                    if (!this.pageEdit.isSelecting()) {
                        this.selectWord(integer2);
                    }
                    else {
                        this.pageEdit.selectAll();
                    }
                }
                else {
                    this.pageEdit.setCursorPos(integer2, Screen.hasShiftDown());
                }
                this.clearDisplayCache();
            }
            this.lastIndex = integer2;
            this.lastClickTime = long7;
        }
        return true;
    }
    
    private void selectWord(final int integer) {
        final String string3 = this.getCurrentPageText();
        this.pageEdit.setSelectionRange(StringSplitter.getWordPosition(string3, -1, integer, false), StringSplitter.getWordPosition(string3, 1, integer, false));
    }
    
    @Override
    public boolean mouseDragged(final double double1, final double double2, final int integer, final double double4, final double double5) {
        if (super.mouseDragged(double1, double2, integer, double4, double5)) {
            return true;
        }
        if (integer == 0) {
            final DisplayCache a11 = this.getDisplayCache();
            final int integer2 = a11.getIndexAtPosition(this.font, this.convertScreenToLocal(new Pos2i((int)double1, (int)double2)));
            this.pageEdit.setCursorPos(integer2, true);
            this.clearDisplayCache();
        }
        return true;
    }
    
    private DisplayCache getDisplayCache() {
        if (this.displayCache == null) {
            this.displayCache = this.rebuildDisplayCache();
            this.pageMsg = new TranslatableComponent("book.pageIndicator", new Object[] { this.currentPage + 1, this.getNumPages() });
        }
        return this.displayCache;
    }
    
    private void clearDisplayCache() {
        this.displayCache = null;
    }
    
    private void clearDisplayCacheAfterPageChange() {
        this.pageEdit.setCursorToEnd();
        this.clearDisplayCache();
    }
    
    private DisplayCache rebuildDisplayCache() {
        final String string2 = this.getCurrentPageText();
        if (string2.isEmpty()) {
            return DisplayCache.EMPTY;
        }
        final int integer9 = this.pageEdit.getCursorPos();
        final int integer10 = this.pageEdit.getSelectionPos();
        final IntList intList5 = (IntList)new IntArrayList();
        final List<LineInfo> list6 = (List<LineInfo>)Lists.newArrayList();
        final MutableInt mutableInt7 = new MutableInt();
        final MutableBoolean mutableBoolean8 = new MutableBoolean();
        final StringSplitter dkg9 = this.font.getSplitter();
        final MutableInt mutableInt8;
        final int integer11;
        final String s;
        final String string3;
        final MutableBoolean mutableBoolean9;
        final String string4;
        final int n;
        final int integer12;
        final Pos2i c14;
        final IntList list8;
        final List list9;
        dkg9.splitLines(string2, 114, Style.EMPTY, true, (ob, integer7, integer8) -> {
            integer11 = mutableInt8.getAndIncrement();
            string3 = s.substring(integer7, integer8);
            mutableBoolean9.setValue(string3.endsWith("\n"));
            string4 = StringUtils.stripEnd(string3, " \n");
            this.font.getClass();
            integer12 = n * 9;
            c14 = this.convertLocalToScreen(new Pos2i(0, integer12));
            list8.add(integer7);
            list9.add(new LineInfo(ob, string4, c14.x, c14.y));
            return;
        });
        final int[] arr10 = intList5.toIntArray();
        final boolean boolean11 = integer9 == string2.length();
        Pos2i c15;
        if (boolean11 && mutableBoolean8.isTrue()) {
            final int integer22 = 0;
            final int size = list6.size();
            this.font.getClass();
            c15 = new Pos2i(integer22, size * 9);
        }
        else {
            final int integer13 = findLineFromPos(arr10, integer9);
            final int width;
            final int integer14 = width = this.font.width(string2.substring(arr10[integer13], integer9));
            final int n2 = integer13;
            this.font.getClass();
            c15 = new Pos2i(width, n2 * 9);
        }
        final List<Rect2i> list7 = (List<Rect2i>)Lists.newArrayList();
        if (integer9 != integer10) {
            final int integer14 = Math.min(integer9, integer10);
            final int integer15 = Math.max(integer9, integer10);
            final int integer16 = findLineFromPos(arr10, integer14);
            final int integer17 = findLineFromPos(arr10, integer15);
            if (integer16 == integer17) {
                final int n3 = integer16;
                this.font.getClass();
                final int integer18 = n3 * 9;
                final int integer19 = arr10[integer16];
                list7.add(this.createPartialLineSelection(string2, dkg9, integer14, integer15, integer18, integer19));
            }
            else {
                final int integer18 = (integer16 + 1 > arr10.length) ? string2.length() : arr10[integer16 + 1];
                final List<Rect2i> list10 = list7;
                final String string6 = string2;
                final StringSplitter dkg10 = dkg9;
                final int integer23 = integer14;
                final int integer24 = integer18;
                final int n4 = integer16;
                this.font.getClass();
                list10.add(this.createPartialLineSelection(string6, dkg10, integer23, integer24, n4 * 9, arr10[integer16]));
                for (int integer19 = integer16 + 1; integer19 < integer17; ++integer19) {
                    final int n5 = integer19;
                    this.font.getClass();
                    final int integer20 = n5 * 9;
                    final String string5 = string2.substring(arr10[integer19], arr10[integer19 + 1]);
                    final int integer21 = (int)dkg9.stringWidth(string5);
                    final List<Rect2i> list11 = list7;
                    final Pos2i c16 = new Pos2i(0, integer20);
                    final int integer25 = integer21;
                    final int n6 = integer20;
                    this.font.getClass();
                    list11.add(this.createSelection(c16, new Pos2i(integer25, n6 + 9)));
                }
                final List<Rect2i> list12 = list7;
                final String string7 = string2;
                final StringSplitter dkg11 = dkg9;
                final int integer26 = arr10[integer17];
                final int integer27 = integer15;
                final int n7 = integer17;
                this.font.getClass();
                list12.add(this.createPartialLineSelection(string7, dkg11, integer26, integer27, n7 * 9, arr10[integer17]));
            }
        }
        return new DisplayCache(string2, c15, boolean11, arr10, (LineInfo[])list6.toArray((Object[])new LineInfo[0]), (Rect2i[])list7.toArray((Object[])new Rect2i[0]));
    }
    
    private static int findLineFromPos(final int[] arr, final int integer) {
        final int integer2 = Arrays.binarySearch(arr, integer);
        if (integer2 < 0) {
            return -(integer2 + 2);
        }
        return integer2;
    }
    
    private Rect2i createPartialLineSelection(final String string, final StringSplitter dkg, final int integer3, final int integer4, final int integer5, final int integer6) {
        final String string2 = string.substring(integer6, integer3);
        final String string3 = string.substring(integer6, integer4);
        final Pos2i c10 = new Pos2i((int)dkg.stringWidth(string2), integer5);
        final int integer7 = (int)dkg.stringWidth(string3);
        this.font.getClass();
        final Pos2i c11 = new Pos2i(integer7, integer5 + 9);
        return this.createSelection(c10, c11);
    }
    
    private Rect2i createSelection(final Pos2i c1, final Pos2i c2) {
        final Pos2i c3 = this.convertLocalToScreen(c1);
        final Pos2i c4 = this.convertLocalToScreen(c2);
        final int integer6 = Math.min(c3.x, c4.x);
        final int integer7 = Math.max(c3.x, c4.x);
        final int integer8 = Math.min(c3.y, c4.y);
        final int integer9 = Math.max(c3.y, c4.y);
        return new Rect2i(integer6, integer8, integer7 - integer6, integer9 - integer8);
    }
    
    static {
        EDIT_TITLE_LABEL = new TranslatableComponent("book.editTitle");
        FINALIZE_WARNING_LABEL = new TranslatableComponent("book.finalizeWarning");
        BLACK_CURSOR = FormattedCharSequence.forward("_", Style.EMPTY.withColor(ChatFormatting.BLACK));
        GRAY_CURSOR = FormattedCharSequence.forward("_", Style.EMPTY.withColor(ChatFormatting.GRAY));
    }
    
    static class Pos2i {
        public final int x;
        public final int y;
        
        Pos2i(final int integer1, final int integer2) {
            this.x = integer1;
            this.y = integer2;
        }
    }
    
    static class LineInfo {
        private final Style style;
        private final String contents;
        private final Component asComponent;
        private final int x;
        private final int y;
        
        public LineInfo(final Style ob, final String string, final int integer3, final int integer4) {
            this.style = ob;
            this.contents = string;
            this.x = integer3;
            this.y = integer4;
            this.asComponent = new TextComponent(string).setStyle(ob);
        }
    }
    
    static class DisplayCache {
        private static final DisplayCache EMPTY;
        private final String fullText;
        private final Pos2i cursor;
        private final boolean cursorAtEnd;
        private final int[] lineStarts;
        private final LineInfo[] lines;
        private final Rect2i[] selection;
        
        public DisplayCache(final String string, final Pos2i c, final boolean boolean3, final int[] arr, final LineInfo[] arr, final Rect2i[] arr) {
            this.fullText = string;
            this.cursor = c;
            this.cursorAtEnd = boolean3;
            this.lineStarts = arr;
            this.lines = arr;
            this.selection = arr;
        }
        
        public int getIndexAtPosition(final Font dkr, final Pos2i c) {
            final int y = c.y;
            dkr.getClass();
            final int integer4 = y / 9;
            if (integer4 < 0) {
                return 0;
            }
            if (integer4 >= this.lines.length) {
                return this.fullText.length();
            }
            final LineInfo b5 = this.lines[integer4];
            return this.lineStarts[integer4] + dkr.getSplitter().plainIndexAtWidth(b5.contents, c.x, b5.style);
        }
        
        public int changeLine(final int integer1, final int integer2) {
            final int integer3 = findLineFromPos(this.lineStarts, integer1);
            final int integer4 = integer3 + integer2;
            int integer7;
            if (0 <= integer4 && integer4 < this.lineStarts.length) {
                final int integer5 = integer1 - this.lineStarts[integer3];
                final int integer6 = this.lines[integer4].contents.length();
                integer7 = this.lineStarts[integer4] + Math.min(integer5, integer6);
            }
            else {
                integer7 = integer1;
            }
            return integer7;
        }
        
        public int findLineStart(final int integer) {
            final int integer2 = findLineFromPos(this.lineStarts, integer);
            return this.lineStarts[integer2];
        }
        
        public int findLineEnd(final int integer) {
            final int integer2 = findLineFromPos(this.lineStarts, integer);
            return this.lineStarts[integer2] + this.lines[integer2].contents.length();
        }
        
        static {
            EMPTY = new DisplayCache("", new Pos2i(0, 0), true, new int[] { 0 }, new LineInfo[] { new LineInfo(Style.EMPTY, "", 0, 0) }, new Rect2i[0]);
        }
    }
}
