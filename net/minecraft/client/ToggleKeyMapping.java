package net.minecraft.client;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.function.BooleanSupplier;

public class ToggleKeyMapping extends KeyMapping {
    private final BooleanSupplier needsToggle;
    
    public ToggleKeyMapping(final String string1, final int integer, final String string3, final BooleanSupplier booleanSupplier) {
        super(string1, InputConstants.Type.KEYSYM, integer, string3);
        this.needsToggle = booleanSupplier;
    }
    
    @Override
    public void setDown(final boolean boolean1) {
        if (this.needsToggle.getAsBoolean()) {
            if (boolean1) {
                super.setDown(!this.isDown());
            }
        }
        else {
            super.setDown(boolean1);
        }
    }
}
