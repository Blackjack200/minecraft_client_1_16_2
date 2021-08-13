package net.minecraft.gametest.framework;

public interface GameTestListener {
    void testStructureLoaded(final GameTestInfo lf);
    
    void testFailed(final GameTestInfo lf);
}
