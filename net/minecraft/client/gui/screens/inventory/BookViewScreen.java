package net.minecraft.client.gui.screens.inventory;

import net.minecraft.ChatFormatting;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.ListTag;
import com.google.common.collect.ImmutableList;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.TextComponent;
import java.util.Collections;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.screens.Screen;

public class BookViewScreen extends Screen {
    public static final BookAccess EMPTY_ACCESS;
    public static final ResourceLocation BOOK_LOCATION;
    private BookAccess bookAccess;
    private int currentPage;
    private List<FormattedCharSequence> cachedPageComponents;
    private int cachedPage;
    private Component pageMsg;
    private PageButton forwardButton;
    private PageButton backButton;
    private final boolean playTurnSound;
    
    public BookViewScreen(final BookAccess a) {
        this(a, true);
    }
    
    public BookViewScreen() {
        this(BookViewScreen.EMPTY_ACCESS, false);
    }
    
    private BookViewScreen(final BookAccess a, final boolean boolean2) {
        super(NarratorChatListener.NO_TITLE);
        this.cachedPageComponents = (List<FormattedCharSequence>)Collections.emptyList();
        this.cachedPage = -1;
        this.pageMsg = TextComponent.EMPTY;
        this.bookAccess = a;
        this.playTurnSound = boolean2;
    }
    
    public void setBookAccess(final BookAccess a) {
        this.bookAccess = a;
        this.currentPage = Mth.clamp(this.currentPage, 0, a.getPageCount());
        this.updateButtonVisibility();
        this.cachedPage = -1;
    }
    
    public boolean setPage(final int integer) {
        final int integer2 = Mth.clamp(integer, 0, this.bookAccess.getPageCount() - 1);
        if (integer2 != this.currentPage) {
            this.currentPage = integer2;
            this.updateButtonVisibility();
            this.cachedPage = -1;
            return true;
        }
        return false;
    }
    
    protected boolean forcePage(final int integer) {
        return this.setPage(integer);
    }
    
    @Override
    protected void init() {
        this.createMenuControls();
        this.createPageControlButtons();
    }
    
    protected void createMenuControls() {
        this.<Button>addButton(new Button(this.width / 2 - 100, 196, 200, 20, CommonComponents.GUI_DONE, dlg -> this.minecraft.setScreen(null)));
    }
    
    protected void createPageControlButtons() {
        final int integer2 = (this.width - 192) / 2;
        final int integer3 = 2;
        this.forwardButton = this.<PageButton>addButton(new PageButton(integer2 + 116, 159, true, dlg -> this.pageForward(), this.playTurnSound));
        this.backButton = this.<PageButton>addButton(new PageButton(integer2 + 43, 159, false, dlg -> this.pageBack(), this.playTurnSound));
        this.updateButtonVisibility();
    }
    
    private int getNumPages() {
        return this.bookAccess.getPageCount();
    }
    
    protected void pageBack() {
        if (this.currentPage > 0) {
            --this.currentPage;
        }
        this.updateButtonVisibility();
    }
    
    protected void pageForward() {
        if (this.currentPage < this.getNumPages() - 1) {
            ++this.currentPage;
        }
        this.updateButtonVisibility();
    }
    
    private void updateButtonVisibility() {
        this.forwardButton.visible = (this.currentPage < this.getNumPages() - 1);
        this.backButton.visible = (this.currentPage > 0);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (super.keyPressed(integer1, integer2, integer3)) {
            return true;
        }
        switch (integer1) {
            case 266: {
                this.backButton.onPress();
                return true;
            }
            case 267: {
                this.forwardButton.onPress();
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(BookViewScreen.BOOK_LOCATION);
        final int integer4 = (this.width - 192) / 2;
        final int integer5 = 2;
        this.blit(dfj, integer4, 2, 0, 0, 192, 192);
        if (this.cachedPage != this.currentPage) {
            final FormattedText nu8 = this.bookAccess.getPage(this.currentPage);
            this.cachedPageComponents = this.font.split(nu8, 114);
            this.pageMsg = new TranslatableComponent("book.pageIndicator", new Object[] { this.currentPage + 1, Math.max(this.getNumPages(), 1) });
        }
        this.cachedPage = this.currentPage;
        final int integer6 = this.font.width(this.pageMsg);
        this.font.draw(dfj, this.pageMsg, (float)(integer4 - integer6 + 192 - 44), 18.0f, 0);
        final int n = 128;
        this.font.getClass();
        for (int integer7 = Math.min(n / 9, this.cachedPageComponents.size()), integer8 = 0; integer8 < integer7; ++integer8) {
            final FormattedCharSequence aex11 = (FormattedCharSequence)this.cachedPageComponents.get(integer8);
            final Font font = this.font;
            final FormattedCharSequence aex12 = aex11;
            final float float5 = (float)(integer4 + 36);
            final int n2 = 32;
            final int n3 = integer8;
            this.font.getClass();
            font.draw(dfj, aex12, float5, (float)(n2 + n3 * 9), 0);
        }
        final Style ob10 = this.getClickedComponentStyleAt(integer2, integer3);
        if (ob10 != null) {
            this.renderComponentHoverEffect(dfj, ob10, integer2, integer3);
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (integer == 0) {
            final Style ob7 = this.getClickedComponentStyleAt(double1, double2);
            if (ob7 != null && this.handleComponentClicked(ob7)) {
                return true;
            }
        }
        return super.mouseClicked(double1, double2, integer);
    }
    
    @Override
    public boolean handleComponentClicked(final Style ob) {
        final ClickEvent np3 = ob.getClickEvent();
        if (np3 == null) {
            return false;
        }
        if (np3.getAction() == ClickEvent.Action.CHANGE_PAGE) {
            final String string4 = np3.getValue();
            try {
                final int integer5 = Integer.parseInt(string4) - 1;
                return this.forcePage(integer5);
            }
            catch (Exception ex) {
                return false;
            }
        }
        final boolean boolean4 = super.handleComponentClicked(ob);
        if (boolean4 && np3.getAction() == ClickEvent.Action.RUN_COMMAND) {
            this.minecraft.setScreen(null);
        }
        return boolean4;
    }
    
    @Nullable
    public Style getClickedComponentStyleAt(final double double1, final double double2) {
        if (this.cachedPageComponents.isEmpty()) {
            return null;
        }
        final int integer6 = Mth.floor(double1 - (this.width - 192) / 2 - 36.0);
        final int integer7 = Mth.floor(double2 - 2.0 - 30.0);
        if (integer6 < 0 || integer7 < 0) {
            return null;
        }
        final int n = 128;
        this.font.getClass();
        final int integer8 = Math.min(n / 9, this.cachedPageComponents.size());
        if (integer6 <= 114) {
            final int n2 = integer7;
            this.minecraft.font.getClass();
            if (n2 < 9 * integer8 + integer8) {
                final int n3 = integer7;
                this.minecraft.font.getClass();
                final int integer9 = n3 / 9;
                if (integer9 >= 0 && integer9 < this.cachedPageComponents.size()) {
                    final FormattedCharSequence aex10 = (FormattedCharSequence)this.cachedPageComponents.get(integer9);
                    return this.minecraft.font.getSplitter().componentStyleAtWidth(aex10, integer6);
                }
                return null;
            }
        }
        return null;
    }
    
    public static List<String> convertPages(final CompoundTag md) {
        final ListTag mj2 = md.getList("pages", 8).copy();
        final ImmutableList.Builder<String> builder3 = (ImmutableList.Builder<String>)ImmutableList.builder();
        for (int integer4 = 0; integer4 < mj2.size(); ++integer4) {
            builder3.add(mj2.getString(integer4));
        }
        return (List<String>)builder3.build();
    }
    
    static {
        EMPTY_ACCESS = new BookAccess() {
            public int getPageCount() {
                return 0;
            }
            
            public FormattedText getPageRaw(final int integer) {
                return FormattedText.EMPTY;
            }
        };
        BOOK_LOCATION = new ResourceLocation("textures/gui/book.png");
    }
    
    public interface BookAccess {
        int getPageCount();
        
        FormattedText getPageRaw(final int integer);
        
        default FormattedText getPage(final int integer) {
            if (integer >= 0 && integer < this.getPageCount()) {
                return this.getPageRaw(integer);
            }
            return FormattedText.EMPTY;
        }
        
        default BookAccess fromItem(final ItemStack bly) {
            final Item blu2 = bly.getItem();
            if (blu2 == Items.WRITTEN_BOOK) {
                return new WrittenBookAccess(bly);
            }
            if (blu2 == Items.WRITABLE_BOOK) {
                return new WritableBookAccess(bly);
            }
            return BookViewScreen.EMPTY_ACCESS;
        }
    }
    
    public static class WrittenBookAccess implements BookAccess {
        private final List<String> pages;
        
        public WrittenBookAccess(final ItemStack bly) {
            this.pages = readPages(bly);
        }
        
        private static List<String> readPages(final ItemStack bly) {
            final CompoundTag md2 = bly.getTag();
            if (md2 != null && WrittenBookItem.makeSureTagIsValid(md2)) {
                return BookViewScreen.convertPages(md2);
            }
            return (List<String>)ImmutableList.of(Component.Serializer.toJson(new TranslatableComponent("book.invalid.tag").withStyle(ChatFormatting.DARK_RED)));
        }
        
        public int getPageCount() {
            return this.pages.size();
        }
        
        public FormattedText getPageRaw(final int integer) {
            final String string3 = (String)this.pages.get(integer);
            try {
                final FormattedText nu4 = Component.Serializer.fromJson(string3);
                if (nu4 != null) {
                    return nu4;
                }
            }
            catch (Exception ex) {}
            return FormattedText.of(string3);
        }
    }
    
    public static class WritableBookAccess implements BookAccess {
        private final List<String> pages;
        
        public WritableBookAccess(final ItemStack bly) {
            this.pages = readPages(bly);
        }
        
        private static List<String> readPages(final ItemStack bly) {
            final CompoundTag md2 = bly.getTag();
            return (List<String>)((md2 != null) ? BookViewScreen.convertPages(md2) : ImmutableList.of());
        }
        
        public int getPageCount() {
            return this.pages.size();
        }
        
        public FormattedText getPageRaw(final int integer) {
            return FormattedText.of((String)this.pages.get(integer));
        }
    }
}
