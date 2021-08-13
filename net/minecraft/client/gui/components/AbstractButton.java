package net.minecraft.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public abstract class AbstractButton extends AbstractWidget {
    public AbstractButton(final int integer1, final int integer2, final int integer3, final int integer4, final Component nr) {
        super(integer1, integer2, integer3, integer4, nr);
    }
    
    public abstract void onPress();
    
    @Override
    public void onClick(final double double1, final double double2) {
        this.onPress();
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (!this.active || !this.visible) {
            return false;
        }
        if (integer1 == 257 || integer1 == 32 || integer1 == 335) {
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            this.onPress();
            return true;
        }
        return false;
    }
}
