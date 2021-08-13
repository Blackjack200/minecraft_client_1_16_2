package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.client.multiplayer.ClientLevel;

public class WaterCurrentDownParticle extends TextureSheetParticle {
    private float angle;
    
    private WaterCurrentDownParticle(final ClientLevel dwl, final double double2, final double double3, final double double4) {
        super(dwl, double2, double3, double4);
        this.lifetime = (int)(Math.random() * 60.0) + 30;
        this.hasPhysics = false;
        this.xd = 0.0;
        this.yd = -0.05;
        this.zd = 0.0;
        this.setSize(0.02f, 0.02f);
        this.quadSize *= this.random.nextFloat() * 0.6f + 0.2f;
        this.gravity = 0.002f;
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
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
        final float float2 = 0.6f;
        this.xd += 0.6f * Mth.cos(this.angle);
        this.zd += 0.6f * Mth.sin(this.angle);
        this.xd *= 0.07;
        this.zd *= 0.07;
        this.move(this.xd, this.yd, this.zd);
        if (!this.level.getFluidState(new BlockPos(this.x, this.y, this.z)).is(FluidTags.WATER) || this.onGround) {
            this.remove();
        }
        this.angle += (float)0.08;
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public Provider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final WaterCurrentDownParticle dyx16 = new WaterCurrentDownParticle(dwl, double3, double4, double5, null);
            dyx16.pickSprite(this.sprite);
            return dyx16;
        }
    }
}
