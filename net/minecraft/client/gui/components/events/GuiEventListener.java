package net.minecraft.client.gui.components.events;

public interface GuiEventListener {
    default void mouseMoved(final double double1, final double double2) {
    }
    
    default boolean mouseClicked(final double double1, final double double2, final int integer) {
        return false;
    }
    
    default boolean mouseReleased(final double double1, final double double2, final int integer) {
        return false;
    }
    
    default boolean mouseDragged(final double double1, final double double2, final int integer, final double double4, final double double5) {
        return false;
    }
    
    default boolean mouseScrolled(final double double1, final double double2, final double double3) {
        return false;
    }
    
    default boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        return false;
    }
    
    default boolean keyReleased(final int integer1, final int integer2, final int integer3) {
        return false;
    }
    
    default boolean charTyped(final char character, final int integer) {
        return false;
    }
    
    default boolean changeFocus(final boolean boolean1) {
        return false;
    }
    
    default boolean isMouseOver(final double double1, final double double2) {
        return false;
    }
}
