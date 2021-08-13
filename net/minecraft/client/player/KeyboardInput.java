package net.minecraft.client.player;

import net.minecraft.client.Options;

public class KeyboardInput extends Input {
    private final Options options;
    
    public KeyboardInput(final Options dka) {
        this.options = dka;
    }
    
    @Override
    public void tick(final boolean boolean1) {
        this.up = this.options.keyUp.isDown();
        this.down = this.options.keyDown.isDown();
        this.left = this.options.keyLeft.isDown();
        this.right = this.options.keyRight.isDown();
        this.forwardImpulse = ((this.up == this.down) ? 0.0f : (this.up ? 1.0f : -1.0f));
        this.leftImpulse = ((this.left == this.right) ? 0.0f : (this.left ? 1.0f : -1.0f));
        this.jumping = this.options.keyJump.isDown();
        this.shiftKeyDown = this.options.keyShift.isDown();
        if (boolean1) {
            this.leftImpulse *= (float)0.3;
            this.forwardImpulse *= (float)0.3;
        }
    }
}
