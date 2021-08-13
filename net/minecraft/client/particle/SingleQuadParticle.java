package net.minecraft.client.particle;

import net.minecraft.world.phys.Vec3;
import com.mojang.math.Vector3f;
import com.mojang.math.Quaternion;
import net.minecraft.util.Mth;
import net.minecraft.client.Camera;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.multiplayer.ClientLevel;

public abstract class SingleQuadParticle extends Particle {
    protected float quadSize;
    
    protected SingleQuadParticle(final ClientLevel dwl, final double double2, final double double3, final double double4) {
        super(dwl, double2, double3, double4);
        this.quadSize = 0.1f * (this.random.nextFloat() * 0.5f + 0.5f) * 2.0f;
    }
    
    protected SingleQuadParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(dwl, double2, double3, double4, double5, double6, double7);
        this.quadSize = 0.1f * (this.random.nextFloat() * 0.5f + 0.5f) * 2.0f;
    }
    
    @Override
    public void render(final VertexConsumer dfn, final Camera djh, final float float3) {
        final Vec3 dck5 = djh.getPosition();
        final float float4 = (float)(Mth.lerp(float3, this.xo, this.x) - dck5.x());
        final float float5 = (float)(Mth.lerp(float3, this.yo, this.y) - dck5.y());
        final float float6 = (float)(Mth.lerp(float3, this.zo, this.z) - dck5.z());
        Quaternion d9;
        if (this.roll == 0.0f) {
            d9 = djh.rotation();
        }
        else {
            d9 = new Quaternion(djh.rotation());
            final float float7 = Mth.lerp(float3, this.oRoll, this.roll);
            d9.mul(Vector3f.ZP.rotation(float7));
        }
        final Vector3f g10 = new Vector3f(-1.0f, -1.0f, 0.0f);
        g10.transform(d9);
        final Vector3f[] arr11 = { new Vector3f(-1.0f, -1.0f, 0.0f), new Vector3f(-1.0f, 1.0f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(1.0f, -1.0f, 0.0f) };
        final float float8 = this.getQuadSize(float3);
        for (int integer13 = 0; integer13 < 4; ++integer13) {
            final Vector3f g11 = arr11[integer13];
            g11.transform(d9);
            g11.mul(float8);
            g11.add(float4, float5, float6);
        }
        final float float9 = this.getU0();
        final float float10 = this.getU1();
        final float float11 = this.getV0();
        final float float12 = this.getV1();
        final int integer14 = this.getLightColor(float3);
        dfn.vertex(arr11[0].x(), arr11[0].y(), arr11[0].z()).uv(float10, float12).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(integer14).endVertex();
        dfn.vertex(arr11[1].x(), arr11[1].y(), arr11[1].z()).uv(float10, float11).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(integer14).endVertex();
        dfn.vertex(arr11[2].x(), arr11[2].y(), arr11[2].z()).uv(float9, float11).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(integer14).endVertex();
        dfn.vertex(arr11[3].x(), arr11[3].y(), arr11[3].z()).uv(float9, float12).color(this.rCol, this.gCol, this.bCol, this.alpha).uv2(integer14).endVertex();
    }
    
    public float getQuadSize(final float float1) {
        return this.quadSize;
    }
    
    @Override
    public Particle scale(final float float1) {
        this.quadSize *= float1;
        return super.scale(float1);
    }
    
    protected abstract float getU0();
    
    protected abstract float getU1();
    
    protected abstract float getV0();
    
    protected abstract float getV1();
}
