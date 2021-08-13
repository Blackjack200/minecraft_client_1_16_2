package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.client.multiplayer.ClientLevel;

public class SquidInkParticle extends SimpleAnimatedParticle {
    private SquidInkParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final SpriteSet dyo) {
        super(dwl, double2, double3, double4, dyo, 0.0f);
        this.quadSize = 0.5f;
        this.setAlpha(1.0f);
        this.setColor(0.0f, 0.0f, 0.0f);
        this.lifetime = (int)(this.quadSize * 12.0f / (Math.random() * 0.800000011920929 + 0.20000000298023224));
        this.setSpriteFromAge(dyo);
        this.hasPhysics = false;
        this.xd = double5;
        this.yd = double6;
        this.zd = double7;
        this.setBaseAirFriction(0.0f);
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
        this.setSpriteFromAge(this.sprites);
        if (this.age > this.lifetime / 2) {
            this.setAlpha(1.0f - (this.age - (float)(this.lifetime / 2)) / this.lifetime);
        }
        this.move(this.xd, this.yd, this.zd);
        if (this.level.getBlockState(new BlockPos(this.x, this.y, this.z)).isAir()) {
            this.yd -= 0.00800000037997961;
        }
        this.xd *= 0.9200000166893005;
        this.yd *= 0.9200000166893005;
        this.zd *= 0.9200000166893005;
        if (this.onGround) {
            this.xd *= 0.699999988079071;
            this.zd *= 0.699999988079071;
        }
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        
        public Provider(final SpriteSet dyo) {
            this.sprites = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            return new SquidInkParticle(dwl, double3, double4, double5, double6, double7, double8, this.sprites, null);
        }
    }
}
