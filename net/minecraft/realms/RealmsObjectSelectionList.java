package net.minecraft.realms;

import net.minecraft.client.gui.components.AbstractSelectionList;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;

public abstract class RealmsObjectSelectionList<E extends Entry<E>> extends ObjectSelectionList<E> {
    protected RealmsObjectSelectionList(final int integer1, final int integer2, final int integer3, final int integer4, final int integer5) {
        super(Minecraft.getInstance(), integer1, integer2, integer3, integer4, integer5);
    }
    
    public void setSelectedItem(final int integer) {
        if (integer == -1) {
            this.setSelected(null);
        }
        else if (super.getItemCount() != 0) {
            this.setSelected(this.getEntry(integer));
        }
    }
    
    public void selectItem(final int integer) {
        this.setSelectedItem(integer);
    }
    
    public void itemClicked(final int integer1, final int integer2, final double double3, final double double4, final int integer5) {
    }
    
    public int getMaxPosition() {
        return 0;
    }
    
    public int getScrollbarPosition() {
        return this.getRowLeft() + this.getRowWidth();
    }
    
    @Override
    public int getRowWidth() {
        return (int)(this.width * 0.6);
    }
    
    public void replaceEntries(final Collection<E> collection) {
        super.replaceEntries(collection);
    }
    
    public int getItemCount() {
        return super.getItemCount();
    }
    
    public int getRowTop(final int integer) {
        return super.getRowTop(integer);
    }
    
    public int getRowLeft() {
        return super.getRowLeft();
    }
    
    public int addEntry(final E a) {
        return super.addEntry(a);
    }
    
    public void clear() {
        this.clearEntries();
    }
}
