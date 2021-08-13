package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.multiplayer.ClientLevel;

public class EnchantmentTableParticle extends TextureSheetParticle {
    private final double xStart;
    private final double yStart;
    private final double zStart;
    
    private EnchantmentTableParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(dwl, double2, double3, double4);
        this.xd = double5;
        this.yd = double6;
        this.zd = double7;
        this.xStart = double2;
        this.yStart = double3;
        this.zStart = double4;
        this.xo = double2 + double5;
        this.yo = double3 + double6;
        this.zo = double4 + double7;
        this.x = this.xo;
        this.y = this.yo;
        this.z = this.zo;
        this.quadSize = 0.1f * (this.random.nextFloat() * 0.5f + 0.2f);
        final float float15 = this.random.nextFloat() * 0.6f + 0.4f;
        this.rCol = 0.9f * float15;
        this.gCol = 0.9f * float15;
        this.bCol = float15;
        this.hasPhysics = false;
        this.lifetime = (int)(Math.random() * 10.0) + 30;
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
        float float2 = this.age / (float)this.lifetime;
        float2 = 1.0f - float2;
        float float3 = 1.0f - float2;
        float3 *= float3;
        float3 *= float3;
        this.x = this.xStart + this.xd * float2;
        this.y = this.yStart + this.yd * float2 - float3 * 1.2f;
        this.z = this.zStart + this.zd * float2;
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public Provider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final EnchantmentTableParticle dxj16 = new EnchantmentTableParticle(dwl, double3, double4, double5, double6, double7, double8, null);
            dxj16.pickSprite(this.sprite);
            return dxj16;
        }
    }
    
    public static class NautilusProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public NautilusProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final EnchantmentTableParticle dxj16 = new EnchantmentTableParticle(dwl, double3, double4, double5, double6, double7, double8, null);
            dxj16.pickSprite(this.sprite);
            return dxj16;
        }
    }
}
