package net.minecraft.gametest.framework;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;

public class GameTestAssertPosException extends GameTestAssertException {
    private final BlockPos absolutePos;
    private final BlockPos relativePos;
    private final long tick;
    
    public String getMessage() {
        final String string2 = new StringBuilder().append("").append(this.absolutePos.getX()).append(",").append(this.absolutePos.getY()).append(",").append(this.absolutePos.getZ()).append(" (relative: ").append(this.relativePos.getX()).append(",").append(this.relativePos.getY()).append(",").append(this.relativePos.getZ()).append(")").toString();
        return super.getMessage() + " at " + string2 + " (t=" + this.tick + ")";
    }
    
    @Nullable
    public String getMessageToShowAtBlock() {
        return super.getMessage() + " here";
    }
    
    @Nullable
    public BlockPos getAbsolutePos() {
        return this.absolutePos;
    }
}
