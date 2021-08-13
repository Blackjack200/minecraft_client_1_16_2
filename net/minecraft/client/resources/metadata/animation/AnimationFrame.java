package net.minecraft.client.resources.metadata.animation;

public class AnimationFrame {
    private final int index;
    private final int time;
    
    public AnimationFrame(final int integer) {
        this(integer, -1);
    }
    
    public AnimationFrame(final int integer1, final int integer2) {
        this.index = integer1;
        this.time = integer2;
    }
    
    public boolean isTimeUnknown() {
        return this.time == -1;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public int getIndex() {
        return this.index;
    }
}
