package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.client.multiplayer.ClientLevel;

public class FlameParticle extends RisingParticle {
    private FlameParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(dwl, double2, double3, double4, double5, double6, double7);
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
        final float float2 = (this.age + float1) / this.lifetime;
        return this.quadSize * (1.0f - float2 * float2 * 0.5f);
    }
    
    public int getLightColor(final float float1) {
        float float2 = (this.age + float1) / this.lifetime;
        float2 = Mth.clamp(float2, 0.0f, 1.0f);
        final int integer4 = super.getLightColor(float1);
        int integer5 = integer4 & 0xFF;
        final int integer6 = integer4 >> 16 & 0xFF;
        integer5 += (int)(float2 * 15.0f * 16.0f);
        if (integer5 > 240) {
            integer5 = 240;
        }
        return integer5 | integer6 << 16;
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public Provider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final FlameParticle dxo16 = new FlameParticle(dwl, double3, double4, double5, double6, double7, double8, null);
            dxo16.pickSprite(this.sprite);
            return dxo16;
        }
    }
}
