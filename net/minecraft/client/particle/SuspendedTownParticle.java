package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.multiplayer.ClientLevel;

public class SuspendedTownParticle extends TextureSheetParticle {
    private SuspendedTownParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7) {
        super(dwl, double2, double3, double4, double5, double6, double7);
        final float float15 = this.random.nextFloat() * 0.1f + 0.2f;
        this.rCol = float15;
        this.gCol = float15;
        this.bCol = float15;
        this.setSize(0.02f, 0.02f);
        this.quadSize *= this.random.nextFloat() * 0.6f + 0.5f;
        this.xd *= 0.019999999552965164;
        this.yd *= 0.019999999552965164;
        this.zd *= 0.019999999552965164;
        this.lifetime = (int)(20.0 / (Math.random() * 0.8 + 0.2));
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
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
            return;
        }
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.99;
        this.yd *= 0.99;
        this.zd *= 0.99;
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public Provider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final SuspendedTownParticle dyr16 = new SuspendedTownParticle(dwl, double3, double4, double5, double6, double7, double8, null);
            dyr16.pickSprite(this.sprite);
            return dyr16;
        }
    }
    
    public static class HappyVillagerProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public HappyVillagerProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final SuspendedTownParticle dyr16 = new SuspendedTownParticle(dwl, double3, double4, double5, double6, double7, double8, null);
            dyr16.pickSprite(this.sprite);
            dyr16.setColor(1.0f, 1.0f, 1.0f);
            return dyr16;
        }
    }
    
    public static class ComposterFillProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public ComposterFillProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final SuspendedTownParticle dyr16 = new SuspendedTownParticle(dwl, double3, double4, double5, double6, double7, double8, null);
            dyr16.pickSprite(this.sprite);
            dyr16.setColor(1.0f, 1.0f, 1.0f);
            dyr16.setLifetime(3 + dwl.getRandom().nextInt(5));
            return dyr16;
        }
    }
    
    public static class DolphinSpeedProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public DolphinSpeedProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final SuspendedTownParticle dyr16 = new SuspendedTownParticle(dwl, double3, double4, double5, double6, double7, double8, null);
            dyr16.setColor(0.3f, 0.5f, 1.0f);
            dyr16.pickSprite(this.sprite);
            dyr16.setAlpha(1.0f - dwl.random.nextFloat() * 0.7f);
            dyr16.setLifetime(dyr16.getLifetime() / 2);
            return dyr16;
        }
    }
}
