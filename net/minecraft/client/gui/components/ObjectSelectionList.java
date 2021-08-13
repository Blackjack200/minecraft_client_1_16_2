package net.minecraft.client.gui.components;

import net.minecraft.client.Minecraft;

public abstract class ObjectSelectionList<E extends AbstractSelectionList.Entry<E>> extends AbstractSelectionList<E> {
    private boolean inFocus;
    
    public ObjectSelectionList(final Minecraft djw, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        super(djw, integer2, integer3, integer4, integer5, integer6);
    }
    
    @Override
    public boolean changeFocus(final boolean boolean1) {
        if (!this.inFocus && this.getItemCount() == 0) {
            return false;
        }
        this.inFocus = !this.inFocus;
        if (this.inFocus && this.getSelected() == null && this.getItemCount() > 0) {
            this.moveSelection(SelectionDirection.DOWN);
        }
        else if (this.inFocus && this.getSelected() != null) {
            this.refreshSelection();
        }
        return this.inFocus;
    }
    
    public abstract static class Entry<E extends Entry<E>> extends AbstractSelectionList.Entry<E> {
        public boolean changeFocus(final boolean boolean1) {
            return false;
        }
    }
}
