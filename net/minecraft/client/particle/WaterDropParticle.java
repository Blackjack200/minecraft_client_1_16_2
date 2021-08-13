package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.client.multiplayer.ClientLevel;

public class WaterDropParticle extends TextureSheetParticle {
    protected WaterDropParticle(final ClientLevel dwl, final double double2, final double double3, final double double4) {
        super(dwl, double2, double3, double4, 0.0, 0.0, 0.0);
        this.xd *= 0.30000001192092896;
        this.yd = Math.random() * 0.20000000298023224 + 0.10000000149011612;
        this.zd *= 0.30000001192092896;
        this.setSize(0.01f, 0.01f);
        this.gravity = 0.06f;
        this.lifetime = (int)(8.0 / (Math.random() * 0.8 + 0.2));
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
        if (this.lifetime-- <= 0) {
            this.remove();
            return;
        }
        this.yd -= this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.9800000190734863;
        this.yd *= 0.9800000190734863;
        this.zd *= 0.9800000190734863;
        if (this.onGround) {
            if (Math.random() < 0.5) {
                this.remove();
            }
            this.xd *= 0.699999988079071;
            this.zd *= 0.699999988079071;
        }
        final BlockPos fx2 = new BlockPos(this.x, this.y, this.z);
        final double double3 = Math.max(this.level.getBlockState(fx2).getCollisionShape(this.level, fx2).max(Direction.Axis.Y, this.x - fx2.getX(), this.z - fx2.getZ()), (double)this.level.getFluidState(fx2).getHeight(this.level, fx2));
        if (double3 > 0.0 && this.y < fx2.getY() + double3) {
            this.remove();
        }
    }
    
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public Provider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final WaterDropParticle dyy16 = new WaterDropParticle(dwl, double3, double4, double5);
            dyy16.pickSprite(this.sprite);
            return dyy16;
        }
    }
}
