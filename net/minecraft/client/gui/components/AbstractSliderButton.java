package net.minecraft.client.gui.components;

import net.minecraft.client.sounds.SoundManager;
import net.minecraft.util.Mth;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;

public abstract class AbstractSliderButton extends AbstractWidget {
    protected double value;
    
    public AbstractSliderButton(final int integer1, final int integer2, final int integer3, final int integer4, final Component nr, final double double6) {
        super(integer1, integer2, integer3, integer4, nr);
        this.value = double6;
    }
    
    @Override
    protected int getYImage(final boolean boolean1) {
        return 0;
    }
    
    @Override
    protected MutableComponent createNarrationMessage() {
        return new TranslatableComponent("gui.narrate.slider", new Object[] { this.getMessage() });
    }
    
    @Override
    protected void renderBg(final PoseStack dfj, final Minecraft djw, final int integer3, final int integer4) {
        djw.getTextureManager().bind(AbstractSliderButton.WIDGETS_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final int integer5 = (this.isHovered() ? 2 : 1) * 20;
        this.blit(dfj, this.x + (int)(this.value * (this.width - 8)), this.y, 0, 46 + integer5, 4, 20);
        this.blit(dfj, this.x + (int)(this.value * (this.width - 8)) + 4, this.y, 196, 46 + integer5, 4, 20);
    }
    
    @Override
    public void onClick(final double double1, final double double2) {
        this.setValueFromMouse(double1);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        final boolean boolean5 = integer1 == 263;
        if (boolean5 || integer1 == 262) {
            final float float6 = boolean5 ? -1.0f : 1.0f;
            this.setValue(this.value + float6 / (this.width - 8));
        }
        return false;
    }
    
    private void setValueFromMouse(final double double1) {
        this.setValue((double1 - (this.x + 4)) / (this.width - 8));
    }
    
    private void setValue(final double double1) {
        final double double2 = this.value;
        this.value = Mth.clamp(double1, 0.0, 1.0);
        if (double2 != this.value) {
            this.applyValue();
        }
        this.updateMessage();
    }
    
    @Override
    protected void onDrag(final double double1, final double double2, final double double3, final double double4) {
        this.setValueFromMouse(double1);
        super.onDrag(double1, double2, double3, double4);
    }
    
    @Override
    public void playDownSound(final SoundManager enm) {
    }
    
    @Override
    public void onRelease(final double double1, final double double2) {
        super.playDownSound(Minecraft.getInstance().getSoundManager());
    }
    
    protected abstract void updateMessage();
    
    protected abstract void applyValue();
}
