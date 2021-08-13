package net.minecraft.client.gui.components;

import javax.annotation.Nullable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.Minecraft;

public abstract class ContainerObjectSelectionList<E extends Entry<E>> extends AbstractSelectionList<E> {
    public ContainerObjectSelectionList(final Minecraft djw, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        super(djw, integer2, integer3, integer4, integer5, integer6);
    }
    
    @Override
    public boolean changeFocus(final boolean boolean1) {
        final boolean boolean2 = super.changeFocus(boolean1);
        if (boolean2) {
            this.ensureVisible(this.getFocused());
        }
        return boolean2;
    }
    
    @Override
    protected boolean isSelectedItem(final int integer) {
        return false;
    }
    
    public abstract static class Entry<E extends Entry<E>> extends AbstractSelectionList.Entry<E> implements ContainerEventHandler {
        @Nullable
        private GuiEventListener focused;
        private boolean dragging;
        
        @Override
        public boolean isDragging() {
            return this.dragging;
        }
        
        @Override
        public void setDragging(final boolean boolean1) {
            this.dragging = boolean1;
        }
        
        @Override
        public void setFocused(@Nullable final GuiEventListener dmf) {
            this.focused = dmf;
        }
        
        @Nullable
        @Override
        public GuiEventListener getFocused() {
            return this.focused;
        }
    }
}
