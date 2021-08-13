package net.minecraft.client.renderer.block.model;

import net.minecraft.core.Direction;
import com.mojang.math.Vector3f;

public class BlockElementRotation {
    public final Vector3f origin;
    public final Direction.Axis axis;
    public final float angle;
    public final boolean rescale;
    
    public BlockElementRotation(final Vector3f g, final Direction.Axis a, final float float3, final boolean boolean4) {
        this.origin = g;
        this.axis = a;
        this.angle = float3;
        this.rescale = boolean4;
    }
}
