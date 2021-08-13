package net.minecraft.client.gui.components;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.client.Options;

public abstract class AbstractOptionSliderButton extends AbstractSliderButton {
    protected final Options options;
    
    protected AbstractOptionSliderButton(final Options dka, final int integer2, final int integer3, final int integer4, final int integer5, final double double6) {
        super(integer2, integer3, integer4, integer5, TextComponent.EMPTY, double6);
        this.options = dka;
    }
}
