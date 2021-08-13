package net.minecraft.client.gui.screens.advancements;

public enum AdvancementWidgetType {
    OBTAINED(0), 
    UNOBTAINED(1);
    
    private final int y;
    
    private AdvancementWidgetType(final int integer3) {
        this.y = integer3;
    }
    
    public int getIndex() {
        return this.y;
    }
}
