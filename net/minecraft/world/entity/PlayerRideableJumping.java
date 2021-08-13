package net.minecraft.world.entity;

public interface PlayerRideableJumping {
    void onPlayerJump(final int integer);
    
    boolean canJump();
    
    void handleStartJump(final int integer);
    
    void handleStopJump();
}
