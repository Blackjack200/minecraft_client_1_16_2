package net.minecraft.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class LockIconButton extends Button {
    private boolean locked;
    
    public LockIconButton(final int integer1, final int integer2, final OnPress a) {
        super(integer1, integer2, 20, 20, new TranslatableComponent("narrator.button.difficulty_lock"), a);
    }
    
    @Override
    protected MutableComponent createNarrationMessage() {
        return super.createNarrationMessage().append(". ").append(this.isLocked() ? new TranslatableComponent("narrator.button.difficulty_lock.locked") : new TranslatableComponent("narrator.button.difficulty_lock.unlocked"));
    }
    
    public boolean isLocked() {
        return this.locked;
    }
    
    public void setLocked(final boolean boolean1) {
        this.locked = boolean1;
    }
    
    @Override
    public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        Minecraft.getInstance().getTextureManager().bind(Button.WIDGETS_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        Icon a6;
        if (!this.active) {
            a6 = (this.locked ? Icon.LOCKED_DISABLED : Icon.UNLOCKED_DISABLED);
        }
        else if (this.isHovered()) {
            a6 = (this.locked ? Icon.LOCKED_HOVER : Icon.UNLOCKED_HOVER);
        }
        else {
            a6 = (this.locked ? Icon.LOCKED : Icon.UNLOCKED);
        }
        this.blit(dfj, this.x, this.y, a6.getX(), a6.getY(), this.width, this.height);
    }
    
    enum Icon {
        LOCKED(0, 146), 
        LOCKED_HOVER(0, 166), 
        LOCKED_DISABLED(0, 186), 
        UNLOCKED(20, 146), 
        UNLOCKED_HOVER(20, 166), 
        UNLOCKED_DISABLED(20, 186);
        
        private final int x;
        private final int y;
        
        private Icon(final int integer3, final int integer4) {
            this.x = integer3;
            this.y = integer4;
        }
        
        public int getX() {
            return this.x;
        }
        
        public int getY() {
            return this.y;
        }
    }
}
