package net.minecraft.world.level.portal;

import net.minecraft.world.phys.Vec3;

public class PortalInfo {
    public final Vec3 pos;
    public final Vec3 speed;
    public final float yRot;
    public final float xRot;
    
    public PortalInfo(final Vec3 dck1, final Vec3 dck2, final float float3, final float float4) {
        this.pos = dck1;
        this.speed = dck2;
        this.yRot = float3;
        this.xRot = float4;
    }
}
