package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import net.minecraft.client.multiplayer.ClientLevel;

public class PlayerCloudParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    
    private PlayerCloudParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final SpriteSet dyo) {
        super(dwl, double2, double3, double4, 0.0, 0.0, 0.0);
        this.sprites = dyo;
        final float float16 = 2.5f;
        this.xd *= 0.10000000149011612;
        this.yd *= 0.10000000149011612;
        this.zd *= 0.10000000149011612;
        this.xd += double5;
        this.yd += double6;
        this.zd += double7;
        final float float17 = 1.0f - (float)(Math.random() * 0.30000001192092896);
        this.rCol = float17;
        this.gCol = float17;
        this.bCol = float17;
        this.quadSize *= 1.875f;
        final int integer18 = (int)(8.0 / (Math.random() * 0.8 + 0.3));
        this.lifetime = (int)Math.max(integer18 * 2.5f, 1.0f);
        this.hasPhysics = false;
        this.setSpriteFromAge(dyo);
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
    
    @Override
    public float getQuadSize(final float float1) {
        return this.quadSize * Mth.clamp((this.age + float1) / this.lifetime * 32.0f, 0.0f, 1.0f);
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
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.9599999785423279;
        this.yd *= 0.9599999785423279;
        this.zd *= 0.9599999785423279;
        final Player bft2 = this.level.getNearestPlayer(this.x, this.y, this.z, 2.0, false);
        if (bft2 != null) {
            final double double3 = bft2.getY();
            if (this.y > double3) {
                this.y += (double3 - this.y) * 0.2;
                this.yd += (bft2.getDeltaMovement().y - this.yd) * 0.2;
                this.setPos(this.x, this.y, this.z);
            }
        }
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
            return new PlayerCloudParticle(dwl, double3, double4, double5, double6, double7, double8, this.sprites, null);
        }
    }
    
    public static class SneezeProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        
        public SneezeProvider(final SpriteSet dyo) {
            this.sprites = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final Particle dxy16 = new PlayerCloudParticle(dwl, double3, double4, double5, double6, double7, double8, this.sprites, null);
            dxy16.setColor(200.0f, 50.0f, 120.0f);
            dxy16.setAlpha(0.4f);
            return dxy16;
        }
    }
}
