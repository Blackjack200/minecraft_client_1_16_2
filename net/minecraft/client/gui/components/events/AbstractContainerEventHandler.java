package net.minecraft.client.gui.components.events;

import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiComponent;

public abstract class AbstractContainerEventHandler extends GuiComponent implements ContainerEventHandler {
    @Nullable
    private GuiEventListener focused;
    private boolean isDragging;
    
    @Override
    public final boolean isDragging() {
        return this.isDragging;
    }
    
    @Override
    public final void setDragging(final boolean boolean1) {
        this.isDragging = boolean1;
    }
    
    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return this.focused;
    }
    
    @Override
    public void setFocused(@Nullable final GuiEventListener dmf) {
        this.focused = dmf;
    }
}
