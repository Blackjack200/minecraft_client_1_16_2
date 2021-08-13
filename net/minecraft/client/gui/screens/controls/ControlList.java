package net.minecraft.client.gui.screens.controls;

import net.minecraft.client.gui.components.AbstractSelectionList;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.client.gui.components.Button;
import java.util.Collections;
import net.minecraft.client.gui.components.events.GuiEventListener;
import java.util.List;
import net.minecraft.client.gui.Font;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;

public class ControlList extends ContainerObjectSelectionList<Entry> {
    private final ControlsScreen controlsScreen;
    private int maxNameWidth;
    
    public ControlList(final ControlsScreen dpi, final Minecraft djw) {
        super(djw, dpi.width + 45, dpi.height, 43, dpi.height - 32, 20);
        this.controlsScreen = dpi;
        final KeyMapping[] arr4 = (KeyMapping[])ArrayUtils.clone((Object[])djw.options.keyMappings);
        Arrays.sort((Object[])arr4);
        String string5 = null;
        for (final KeyMapping djt9 : arr4) {
            final String string6 = djt9.getCategory();
            if (!string6.equals(string5)) {
                string5 = string6;
                ((AbstractSelectionList<CategoryEntry>)this).addEntry(new CategoryEntry(new TranslatableComponent(string6)));
            }
            final Component nr11 = new TranslatableComponent(djt9.getName());
            final int integer12 = djw.font.width(nr11);
            if (integer12 > this.maxNameWidth) {
                this.maxNameWidth = integer12;
            }
            ((AbstractSelectionList<KeyEntry>)this).addEntry(new KeyEntry(djt9, nr11));
        }
    }
    
    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 15;
    }
    
    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 32;
    }
    
    public abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
    }
    
    public class CategoryEntry extends Entry {
        private final Component name;
        private final int width;
        
        public CategoryEntry(final Component nr) {
            this.name = nr;
            this.width = ControlList.this.minecraft.font.width(this.name);
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            final Font font = ControlList.this.minecraft.font;
            final Component name = this.name;
            final float float11 = (float)(ControlList.this.minecraft.screen.width / 2 - this.width / 2);
            final int n = integer3 + integer6;
            ControlList.this.minecraft.font.getClass();
            font.draw(dfj, name, float11, (float)(n - 9 - 1), 16777215);
        }
        
        @Override
        public boolean changeFocus(final boolean boolean1) {
            return false;
        }
        
        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }
    }
    
    public class KeyEntry extends Entry {
        private final KeyMapping key;
        private final Component name;
        private final Button changeButton;
        private final Button resetButton;
        
        private KeyEntry(final KeyMapping djt, final Component nr) {
            this.key = djt;
            this.name = nr;
            this.changeButton = new Button(0, 0, 75, 20, nr, dlg -> ControlList.this.controlsScreen.selectedKey = djt) {
                @Override
                protected MutableComponent createNarrationMessage() {
                    if (djt.isUnbound()) {
                        return new TranslatableComponent("narrator.controls.unbound", new Object[] { nr });
                    }
                    return new TranslatableComponent("narrator.controls.bound", new Object[] { nr, super.createNarrationMessage() });
                }
            };
            this.resetButton = new Button(0, 0, 50, 20, new TranslatableComponent("controls.reset"), dlg -> {
                ControlList.this.minecraft.options.setKey(djt, djt.getDefaultKey());
                KeyMapping.resetMapping();
            }) {
                @Override
                protected MutableComponent createNarrationMessage() {
                    return new TranslatableComponent("narrator.controls.reset", new Object[] { nr });
                }
            };
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            final boolean boolean10 = ControlList.this.controlsScreen.selectedKey == this.key;
            final Font font = ControlList.this.minecraft.font;
            final Component name = this.name;
            final float float11 = (float)(integer4 + 90 - ControlList.this.maxNameWidth);
            final int n = integer3 + integer6 / 2;
            ControlList.this.minecraft.font.getClass();
            font.draw(dfj, name, float11, (float)(n - 9 / 2), 16777215);
            this.resetButton.x = integer4 + 190;
            this.resetButton.y = integer3;
            this.resetButton.active = !this.key.isDefault();
            this.resetButton.render(dfj, integer7, integer8, float10);
            this.changeButton.x = integer4 + 105;
            this.changeButton.y = integer3;
            this.changeButton.setMessage(this.key.getTranslatedKeyMessage());
            boolean boolean11 = false;
            if (!this.key.isUnbound()) {
                for (final KeyMapping djt17 : ControlList.this.minecraft.options.keyMappings) {
                    if (djt17 != this.key && this.key.same(djt17)) {
                        boolean11 = true;
                        break;
                    }
                }
            }
            if (boolean10) {
                this.changeButton.setMessage(new TextComponent("> ").append(this.changeButton.getMessage().copy().withStyle(ChatFormatting.YELLOW)).append(" <").withStyle(ChatFormatting.YELLOW));
            }
            else if (boolean11) {
                this.changeButton.setMessage(this.changeButton.getMessage().copy().withStyle(ChatFormatting.RED));
            }
            this.changeButton.render(dfj, integer7, integer8, float10);
        }
        
        @Override
        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.changeButton, this.resetButton);
        }
        
        @Override
        public boolean mouseClicked(final double double1, final double double2, final int integer) {
            return this.changeButton.mouseClicked(double1, double2, integer) || this.resetButton.mouseClicked(double1, double2, integer);
        }
        
        @Override
        public boolean mouseReleased(final double double1, final double double2, final int integer) {
            return this.changeButton.mouseReleased(double1, double2, integer) || this.resetButton.mouseReleased(double1, double2, integer);
        }
    }
}
