package net.minecraft.client.gui.components.events;

import java.util.function.Supplier;
import java.util.function.BooleanSupplier;
import java.util.ListIterator;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Optional;
import java.util.List;

public interface ContainerEventHandler extends GuiEventListener {
    List<? extends GuiEventListener> children();
    
    default Optional<GuiEventListener> getChildAt(final double double1, final double double2) {
        for (final GuiEventListener dmf7 : this.children()) {
            if (dmf7.isMouseOver(double1, double2)) {
                return (Optional<GuiEventListener>)Optional.of(dmf7);
            }
        }
        return (Optional<GuiEventListener>)Optional.empty();
    }
    
    default boolean mouseClicked(final double double1, final double double2, final int integer) {
        for (final GuiEventListener dmf8 : this.children()) {
            if (dmf8.mouseClicked(double1, double2, integer)) {
                this.setFocused(dmf8);
                if (integer == 0) {
                    this.setDragging(true);
                }
                return true;
            }
        }
        return false;
    }
    
    default boolean mouseReleased(final double double1, final double double2, final int integer) {
        this.setDragging(false);
        return this.getChildAt(double1, double2).filter(dmf -> dmf.mouseReleased(double1, double2, integer)).isPresent();
    }
    
    default boolean mouseDragged(final double double1, final double double2, final int integer, final double double4, final double double5) {
        return this.getFocused() != null && this.isDragging() && integer == 0 && this.getFocused().mouseDragged(double1, double2, integer, double4, double5);
    }
    
    boolean isDragging();
    
    void setDragging(final boolean boolean1);
    
    default boolean mouseScrolled(final double double1, final double double2, final double double3) {
        return this.getChildAt(double1, double2).filter(dmf -> dmf.mouseScrolled(double1, double2, double3)).isPresent();
    }
    
    default boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        return this.getFocused() != null && this.getFocused().keyPressed(integer1, integer2, integer3);
    }
    
    default boolean keyReleased(final int integer1, final int integer2, final int integer3) {
        return this.getFocused() != null && this.getFocused().keyReleased(integer1, integer2, integer3);
    }
    
    default boolean charTyped(final char character, final int integer) {
        return this.getFocused() != null && this.getFocused().charTyped(character, integer);
    }
    
    @Nullable
    GuiEventListener getFocused();
    
    void setFocused(@Nullable final GuiEventListener dmf);
    
    default void setInitialFocus(@Nullable final GuiEventListener dmf) {
        this.setFocused(dmf);
        dmf.changeFocus(true);
    }
    
    default void magicalSpecialHackyFocus(@Nullable final GuiEventListener dmf) {
        this.setFocused(dmf);
    }
    
    default boolean changeFocus(final boolean boolean1) {
        final GuiEventListener dmf3 = this.getFocused();
        final boolean boolean2 = dmf3 != null;
        if (boolean2 && dmf3.changeFocus(boolean1)) {
            return true;
        }
        final List<? extends GuiEventListener> list5 = this.children();
        final int integer7 = list5.indexOf(dmf3);
        int integer8;
        if (boolean2 && integer7 >= 0) {
            integer8 = integer7 + (boolean1 ? 1 : 0);
        }
        else if (boolean1) {
            integer8 = 0;
        }
        else {
            integer8 = list5.size();
        }
        final ListIterator<? extends GuiEventListener> listIterator8 = list5.listIterator(integer8);
        final BooleanSupplier booleanSupplier9 = boolean1 ? listIterator8::hasNext : listIterator8::hasPrevious;
        final Supplier<? extends GuiEventListener> supplier10 = (boolean1 ? listIterator8::next : listIterator8::previous);
        while (booleanSupplier9.getAsBoolean()) {
            final GuiEventListener dmf4 = (GuiEventListener)supplier10.get();
            if (dmf4.changeFocus(boolean1)) {
                this.setFocused(dmf4);
                return true;
            }
        }
        this.setFocused(null);
        return false;
    }
}
