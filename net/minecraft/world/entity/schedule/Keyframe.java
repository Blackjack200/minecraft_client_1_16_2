package net.minecraft.world.entity.schedule;

public class Keyframe {
    private final int timeStamp;
    private final float value;
    
    public Keyframe(final int integer, final float float2) {
        this.timeStamp = integer;
        this.value = float2;
    }
    
    public int getTimeStamp() {
        return this.timeStamp;
    }
    
    public float getValue() {
        return this.value;
    }
}
