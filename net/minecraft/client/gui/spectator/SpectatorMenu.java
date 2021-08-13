package net.minecraft.client.gui.spectator;

import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.spectator.SpectatorGui;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.spectator.categories.SpectatorPage;
import com.google.common.collect.Lists;
import java.util.List;
import com.google.common.base.MoreObjects;
import net.minecraft.network.chat.Component;

public class SpectatorMenu {
    private static final SpectatorMenuItem CLOSE_ITEM;
    private static final SpectatorMenuItem SCROLL_LEFT;
    private static final SpectatorMenuItem SCROLL_RIGHT_ENABLED;
    private static final SpectatorMenuItem SCROLL_RIGHT_DISABLED;
    private static final Component CLOSE_MENU_TEXT;
    private static final Component PREVIOUS_PAGE_TEXT;
    private static final Component NEXT_PAGE_TEXT;
    public static final SpectatorMenuItem EMPTY_SLOT;
    private final SpectatorMenuListener listener;
    private SpectatorMenuCategory category;
    private int selectedSlot;
    private int page;
    
    public SpectatorMenu(final SpectatorMenuListener dsl) {
        this.selectedSlot = -1;
        this.category = new RootSpectatorMenuCategory();
        this.listener = dsl;
    }
    
    public SpectatorMenuItem getItem(final int integer) {
        final int integer2 = integer + this.page * 6;
        if (this.page > 0 && integer == 0) {
            return SpectatorMenu.SCROLL_LEFT;
        }
        if (integer == 7) {
            if (integer2 < this.category.getItems().size()) {
                return SpectatorMenu.SCROLL_RIGHT_ENABLED;
            }
            return SpectatorMenu.SCROLL_RIGHT_DISABLED;
        }
        else {
            if (integer == 8) {
                return SpectatorMenu.CLOSE_ITEM;
            }
            if (integer2 < 0 || integer2 >= this.category.getItems().size()) {
                return SpectatorMenu.EMPTY_SLOT;
            }
            return (SpectatorMenuItem)MoreObjects.firstNonNull(this.category.getItems().get(integer2), SpectatorMenu.EMPTY_SLOT);
        }
    }
    
    public List<SpectatorMenuItem> getItems() {
        final List<SpectatorMenuItem> list2 = (List<SpectatorMenuItem>)Lists.newArrayList();
        for (int integer3 = 0; integer3 <= 8; ++integer3) {
            list2.add(this.getItem(integer3));
        }
        return list2;
    }
    
    public SpectatorMenuItem getSelectedItem() {
        return this.getItem(this.selectedSlot);
    }
    
    public SpectatorMenuCategory getSelectedCategory() {
        return this.category;
    }
    
    public void selectSlot(final int integer) {
        final SpectatorMenuItem dsk3 = this.getItem(integer);
        if (dsk3 != SpectatorMenu.EMPTY_SLOT) {
            if (this.selectedSlot == integer && dsk3.isEnabled()) {
                dsk3.selectItem(this);
            }
            else {
                this.selectedSlot = integer;
            }
        }
    }
    
    public void exit() {
        this.listener.onSpectatorMenuClosed(this);
    }
    
    public int getSelectedSlot() {
        return this.selectedSlot;
    }
    
    public void selectCategory(final SpectatorMenuCategory dsj) {
        this.category = dsj;
        this.selectedSlot = -1;
        this.page = 0;
    }
    
    public SpectatorPage getCurrentPage() {
        return new SpectatorPage(this.category, this.getItems(), this.selectedSlot);
    }
    
    static {
        CLOSE_ITEM = new CloseSpectatorItem();
        SCROLL_LEFT = new ScrollMenuItem(-1, true);
        SCROLL_RIGHT_ENABLED = new ScrollMenuItem(1, true);
        SCROLL_RIGHT_DISABLED = new ScrollMenuItem(1, false);
        CLOSE_MENU_TEXT = new TranslatableComponent("spectatorMenu.close");
        PREVIOUS_PAGE_TEXT = new TranslatableComponent("spectatorMenu.previous_page");
        NEXT_PAGE_TEXT = new TranslatableComponent("spectatorMenu.next_page");
        EMPTY_SLOT = new SpectatorMenuItem() {
            public void selectItem(final SpectatorMenu dsi) {
            }
            
            public Component getName() {
                return TextComponent.EMPTY;
            }
            
            public void renderIcon(final PoseStack dfj, final float float2, final int integer) {
            }
            
            public boolean isEnabled() {
                return false;
            }
        };
    }
    
    static class CloseSpectatorItem implements SpectatorMenuItem {
        private CloseSpectatorItem() {
        }
        
        public void selectItem(final SpectatorMenu dsi) {
            dsi.exit();
        }
        
        public Component getName() {
            return SpectatorMenu.CLOSE_MENU_TEXT;
        }
        
        public void renderIcon(final PoseStack dfj, final float float2, final int integer) {
            Minecraft.getInstance().getTextureManager().bind(SpectatorGui.SPECTATOR_LOCATION);
            GuiComponent.blit(dfj, 0, 0, 128.0f, 0.0f, 16, 16, 256, 256);
        }
        
        public boolean isEnabled() {
            return true;
        }
    }
    
    static class ScrollMenuItem implements SpectatorMenuItem {
        private final int direction;
        private final boolean enabled;
        
        public ScrollMenuItem(final int integer, final boolean boolean2) {
            this.direction = integer;
            this.enabled = boolean2;
        }
        
        public void selectItem(final SpectatorMenu dsi) {
            dsi.page += this.direction;
        }
        
        public Component getName() {
            return (this.direction < 0) ? SpectatorMenu.PREVIOUS_PAGE_TEXT : SpectatorMenu.NEXT_PAGE_TEXT;
        }
        
        public void renderIcon(final PoseStack dfj, final float float2, final int integer) {
            Minecraft.getInstance().getTextureManager().bind(SpectatorGui.SPECTATOR_LOCATION);
            if (this.direction < 0) {
                GuiComponent.blit(dfj, 0, 0, 144.0f, 0.0f, 16, 16, 256, 256);
            }
            else {
                GuiComponent.blit(dfj, 0, 0, 160.0f, 0.0f, 16, 16, 256, 256);
            }
        }
        
        public boolean isEnabled() {
            return this.enabled;
        }
    }
}
