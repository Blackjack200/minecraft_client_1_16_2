package net.minecraft.client.particle;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.client.multiplayer.ClientLevel;

public class LavaParticle extends TextureSheetParticle {
    private LavaParticle(final ClientLevel dwl, final double double2, final double double3, final double double4) {
        super(dwl, double2, double3, double4, 0.0, 0.0, 0.0);
        this.xd *= 0.800000011920929;
        this.yd *= 0.800000011920929;
        this.zd *= 0.800000011920929;
        this.yd = this.random.nextFloat() * 0.4f + 0.05f;
        this.quadSize *= this.random.nextFloat() * 2.0f + 0.2f;
        this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
    
    public int getLightColor(final float float1) {
        final int integer3 = super.getLightColor(float1);
        final int integer4 = 240;
        final int integer5 = integer3 >> 16 & 0xFF;
        return 0xF0 | integer5 << 16;
    }
    
    @Override
    public float getQuadSize(final float float1) {
        final float float2 = (this.age + float1) / this.lifetime;
        return this.quadSize * (1.0f - float2 * float2);
    }
    
    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        final float float2 = this.age / (float)this.lifetime;
        if (this.random.nextFloat() > float2) {
            this.level.addParticle(ParticleTypes.SMOKE, this.x, this.y, this.z, this.xd, this.yd, this.zd);
        }
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.yd -= 0.03;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.9990000128746033;
        this.yd *= 0.9990000128746033;
        this.zd *= 0.9990000128746033;
        if (this.onGround) {
            this.xd *= 0.699999988079071;
            this.zd *= 0.699999988079071;
        }
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public Provider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final LavaParticle dxu16 = new LavaParticle(dwl, double3, double4, double5, null);
            dxu16.pickSprite(this.sprite);
            return dxu16;
        }
    }
}
