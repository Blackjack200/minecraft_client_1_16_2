package com.mojang.blaze3d.audio;

import com.mojang.math.Vector3f;
import org.lwjgl.openal.AL10;
import net.minecraft.world.phys.Vec3;

public class Listener {
    private float gain;
    private Vec3 position;
    
    public Listener() {
        this.gain = 1.0f;
        this.position = Vec3.ZERO;
    }
    
    public void setListenerPosition(final Vec3 dck) {
        this.position = dck;
        AL10.alListener3f(4100, (float)dck.x, (float)dck.y, (float)dck.z);
    }
    
    public Vec3 getListenerPosition() {
        return this.position;
    }
    
    public void setListenerOrientation(final Vector3f g1, final Vector3f g2) {
        AL10.alListenerfv(4111, new float[] { g1.x(), g1.y(), g1.z(), g2.x(), g2.y(), g2.z() });
    }
    
    public void setGain(final float float1) {
        AL10.alListenerf(4106, float1);
        this.gain = float1;
    }
    
    public float getGain() {
        return this.gain;
    }
    
    public void reset() {
        this.setListenerPosition(Vec3.ZERO);
        this.setListenerOrientation(Vector3f.ZN, Vector3f.YP);
    }
}
