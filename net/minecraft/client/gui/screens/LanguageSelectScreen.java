package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.chat.NarratorChatListener;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.Option;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionButton;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.network.chat.Component;

public class LanguageSelectScreen extends OptionsSubScreen {
    private static final Component WARNING_LABEL;
    private LanguageSelectionList packSelectionList;
    private final LanguageManager languageManager;
    private OptionButton forceUnicodeButton;
    private Button doneButton;
    
    public LanguageSelectScreen(final Screen doq, final Options dka, final LanguageManager ekr) {
        super(doq, dka, new TranslatableComponent("options.language"));
        this.languageManager = ekr;
    }
    
    @Override
    protected void init() {
        this.packSelectionList = new LanguageSelectionList(this.minecraft);
        this.children.add(this.packSelectionList);
        this.forceUnicodeButton = this.<OptionButton>addButton(new OptionButton(this.width / 2 - 155, this.height - 38, 150, 20, Option.FORCE_UNICODE_FONT, Option.FORCE_UNICODE_FONT.getMessage(this.options), dlg -> {
            Option.FORCE_UNICODE_FONT.toggle(this.options);
            this.options.save();
            dlg.setMessage(Option.FORCE_UNICODE_FONT.getMessage(this.options));
            this.minecraft.resizeDisplay();
            return;
        }));
        final LanguageSelectionList.Entry a3;
        this.doneButton = this.<Button>addButton(new Button(this.width / 2 - 155 + 160, this.height - 38, 150, 20, CommonComponents.GUI_DONE, dlg -> {
            a3 = this.packSelectionList.getSelected();
            if (a3 != null && !a3.language.getCode().equals(this.languageManager.getSelected().getCode())) {
                this.languageManager.setSelected(a3.language);
                this.options.languageCode = a3.language.getCode();
                this.minecraft.reloadResourcePacks();
                this.doneButton.setMessage(CommonComponents.GUI_DONE);
                this.forceUnicodeButton.setMessage(Option.FORCE_UNICODE_FONT.getMessage(this.options));
                this.options.save();
            }
            this.minecraft.setScreen(this.lastScreen);
            return;
        }));
        super.init();
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.packSelectionList.render(dfj, integer2, integer3, float4);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 16, 16777215);
        GuiComponent.drawCenteredString(dfj, this.font, LanguageSelectScreen.WARNING_LABEL, this.width / 2, this.height - 56, 8421504);
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        WARNING_LABEL = new TextComponent("(").append(new TranslatableComponent("options.languageWarning")).append(")");
    }
    
    class LanguageSelectionList extends ObjectSelectionList<Entry> {
        public LanguageSelectionList(final Minecraft djw) {
            super(djw, LanguageSelectScreen.this.width, LanguageSelectScreen.this.height, 32, LanguageSelectScreen.this.height - 65 + 4, 18);
            for (final LanguageInfo ekq5 : LanguageSelectScreen.this.languageManager.getLanguages()) {
                final Entry a6 = new Entry(ekq5);
                this.addEntry(a6);
                if (LanguageSelectScreen.this.languageManager.getSelected().getCode().equals(ekq5.getCode())) {
                    this.setSelected(a6);
                }
            }
            if (this.getSelected() != null) {
                this.centerScrollOn(this.getSelected());
            }
        }
        
        @Override
        protected int getScrollbarPosition() {
            return super.getScrollbarPosition() + 20;
        }
        
        @Override
        public int getRowWidth() {
            return super.getRowWidth() + 50;
        }
        
        @Override
        public void setSelected(@Nullable final Entry a) {
            super.setSelected(a);
            if (a != null) {
                NarratorChatListener.INSTANCE.sayNow(new TranslatableComponent("narrator.select", new Object[] { a.language }).getString());
            }
        }
        
        @Override
        protected void renderBackground(final PoseStack dfj) {
            LanguageSelectScreen.this.renderBackground(dfj);
        }
        
        @Override
        protected boolean isFocused() {
            return LanguageSelectScreen.this.getFocused() == this;
        }
        
        public class Entry extends ObjectSelectionList.Entry<Entry> {
            private final LanguageInfo language;
            
            public Entry(final LanguageInfo ekq) {
                this.language = ekq;
            }
            
            @Override
            public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
                final String string12 = this.language.toString();
                LanguageSelectScreen.this.font.drawShadow(dfj, string12, (float)(LanguageSelectionList.this.width / 2 - LanguageSelectScreen.this.font.width(string12) / 2), (float)(integer3 + 1), 16777215, true);
            }
            
            public boolean mouseClicked(final double double1, final double double2, final int integer) {
                if (integer == 0) {
                    this.select();
                    return true;
                }
                return false;
            }
            
            private void select() {
                LanguageSelectionList.this.setSelected(this);
            }
        }
    }
}
