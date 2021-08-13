package net.minecraft.client.gui.components;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;

public class VolumeSlider extends AbstractOptionSliderButton {
    private final SoundSource source;
    
    public VolumeSlider(final Minecraft djw, final int integer2, final int integer3, final SoundSource adp, final int integer5) {
        super(djw.options, integer2, integer3, integer5, 20, djw.options.getSoundSourceVolume(adp));
        this.source = adp;
        this.updateMessage();
    }
    
    @Override
    protected void updateMessage() {
        final Component nr2 = ((float)this.value == this.getYImage(false)) ? CommonComponents.OPTION_OFF : new TextComponent(new StringBuilder().append((int)(this.value * 100.0)).append("%").toString());
        this.setMessage(new TranslatableComponent("soundCategory." + this.source.getName()).append(": ").append(nr2));
    }
    
    @Override
    protected void applyValue() {
        this.options.setSoundCategoryVolume(this.source, (float)this.value);
        this.options.save();
    }
}
