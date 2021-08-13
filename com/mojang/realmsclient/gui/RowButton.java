package com.mojang.realmsclient.gui;

import net.minecraft.client.gui.components.ObjectSelectionList;
import java.util.Iterator;
import net.minecraft.realms.RealmsObjectSelectionList;
import java.util.List;
import com.mojang.blaze3d.vertex.PoseStack;

public abstract class RowButton {
    public final int width;
    public final int height;
    public final int xOffset;
    public final int yOffset;
    
    public RowButton(final int integer1, final int integer2, final int integer3, final int integer4) {
        this.width = integer1;
        this.height = integer2;
        this.xOffset = integer3;
        this.yOffset = integer4;
    }
    
    public void drawForRowAt(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        final int integer6 = integer2 + this.xOffset;
        final int integer7 = integer3 + this.yOffset;
        boolean boolean9 = false;
        if (integer4 >= integer6 && integer4 <= integer6 + this.width && integer5 >= integer7 && integer5 <= integer7 + this.height) {
            boolean9 = true;
        }
        this.draw(dfj, integer6, integer7, boolean9);
    }
    
    protected abstract void draw(final PoseStack dfj, final int integer2, final int integer3, final boolean boolean4);
    
    public int getRight() {
        return this.xOffset + this.width;
    }
    
    public int getBottom() {
        return this.yOffset + this.height;
    }
    
    public abstract void onClick(final int integer);
    
    public static void drawButtonsInRow(final PoseStack dfj, final List<RowButton> list, final RealmsObjectSelectionList<?> eof, final int integer4, final int integer5, final int integer6, final int integer7) {
        for (final RowButton dhk9 : list) {
            if (eof.getRowWidth() > dhk9.getRight()) {
                dhk9.drawForRowAt(dfj, integer4, integer5, integer6, integer7);
            }
        }
    }
    
    public static void rowButtonMouseClicked(final RealmsObjectSelectionList<?> eof, final ObjectSelectionList.Entry<?> a, final List<RowButton> list, final int integer, final double double5, final double double6) {
        if (integer == 0) {
            final int integer2 = eof.children().indexOf(a);
            if (integer2 > -1) {
                eof.selectItem(integer2);
                final int integer3 = eof.getRowLeft();
                final int integer4 = eof.getRowTop(integer2);
                final int integer5 = (int)(double5 - integer3);
                final int integer6 = (int)(double6 - integer4);
                for (final RowButton dhk15 : list) {
                    if (integer5 >= dhk15.xOffset && integer5 <= dhk15.getRight() && integer6 >= dhk15.yOffset && integer6 <= dhk15.getBottom()) {
                        dhk15.onClick(integer2);
                    }
                }
            }
        }
    }
}
