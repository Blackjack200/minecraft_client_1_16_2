package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.multiplayer.ClientLevel;

public class PortalParticle extends TextureSheetParticle {
    private final double xStart;
    private final double yStart;
    private final double zStart;
    
    protected PortalParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(dwl, double2, double3, double4);
        this.xd = double5;
        this.yd = double6;
        this.zd = double7;
        this.x = double2;
        this.y = double3;
        this.z = double4;
        this.xStart = this.x;
        this.yStart = this.y;
        this.zStart = this.z;
        this.quadSize = 0.1f * (this.random.nextFloat() * 0.2f + 0.5f);
        final float float15 = this.random.nextFloat() * 0.6f + 0.4f;
        this.rCol = float15 * 0.9f;
        this.gCol = float15 * 0.3f;
        this.bCol = float15;
        this.lifetime = (int)(Math.random() * 10.0) + 40;
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
    
    @Override
    public void move(final double double1, final double double2, final double double3) {
        this.setBoundingBox(this.getBoundingBox().move(double1, double2, double3));
        this.setLocationFromBoundingbox();
    }
    
    @Override
    public float getQuadSize(final float float1) {
        float float2 = (this.age + float1) / this.lifetime;
        float2 = 1.0f - float2;
        float2 *= float2;
        float2 = 1.0f - float2;
        return this.quadSize * float2;
    }
    
    public int getLightColor(final float float1) {
        final int integer3 = super.getLightColor(float1);
        float float2 = this.age / (float)this.lifetime;
        float2 *= float2;
        float2 *= float2;
        final int integer4 = integer3 & 0xFF;
        int integer5 = integer3 >> 16 & 0xFF;
        integer5 += (int)(float2 * 15.0f * 16.0f);
        if (integer5 > 240) {
            integer5 = 240;
        }
        return integer4 | integer5 << 16;
    }
    
    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        final float float3;
        float float2 = float3 = this.age / (float)this.lifetime;
        float2 = -float2 + float2 * float2 * 2.0f;
        float2 = 1.0f - float2;
        this.x = this.xStart + this.xd * float2;
        this.y = this.yStart + this.yd * float2 + (1.0f - float3);
        this.z = this.zStart + this.zd * float2;
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public Provider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final PortalParticle dye16 = new PortalParticle(dwl, double3, double4, double5, double6, double7, double8);
            dye16.pickSprite(this.sprite);
            return dye16;
        }
    }
}
