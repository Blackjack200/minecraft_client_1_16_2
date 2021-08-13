package net.minecraft.world;

public enum InteractionResult {
    SUCCESS, 
    CONSUME, 
    PASS, 
    FAIL;
    
    public boolean consumesAction() {
        return this == InteractionResult.SUCCESS || this == InteractionResult.CONSUME;
    }
    
    public boolean shouldSwing() {
        return this == InteractionResult.SUCCESS;
    }
    
    public static InteractionResult sidedSuccess(final boolean boolean1) {
        return boolean1 ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
    }
}
