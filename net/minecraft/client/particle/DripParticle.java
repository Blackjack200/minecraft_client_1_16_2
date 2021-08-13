package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.material.Fluid;

public class DripParticle extends TextureSheetParticle {
    private final Fluid type;
    protected boolean isGlowing;
    
    private DripParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final Fluid cut) {
        super(dwl, double2, double3, double4);
        this.setSize(0.01f, 0.01f);
        this.gravity = 0.06f;
        this.type = cut;
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }
    
    public int getLightColor(final float float1) {
        if (this.isGlowing) {
            return 240;
        }
        return super.getLightColor(float1);
    }
    
    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.preMoveUpdate();
        if (this.removed) {
            return;
        }
        this.yd -= this.gravity;
        this.move(this.xd, this.yd, this.zd);
        this.postMoveUpdate();
        if (this.removed) {
            return;
        }
        this.xd *= 0.9800000190734863;
        this.yd *= 0.9800000190734863;
        this.zd *= 0.9800000190734863;
        final BlockPos fx2 = new BlockPos(this.x, this.y, this.z);
        final FluidState cuu3 = this.level.getFluidState(fx2);
        if (cuu3.getType() == this.type && this.y < fx2.getY() + cuu3.getHeight(this.level, fx2)) {
            this.remove();
        }
    }
    
    protected void preMoveUpdate() {
        if (this.lifetime-- <= 0) {
            this.remove();
        }
    }
    
    protected void postMoveUpdate() {
    }
    
    static class DripHangParticle extends DripParticle {
        private final ParticleOptions fallingParticle;
        
        private DripHangParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final Fluid cut, final ParticleOptions hf) {
            super(dwl, double2, double3, double4, cut, null);
            this.fallingParticle = hf;
            this.gravity *= 0.02f;
            this.lifetime = 40;
        }
        
        @Override
        protected void preMoveUpdate() {
            if (this.lifetime-- <= 0) {
                this.remove();
                this.level.addParticle(this.fallingParticle, this.x, this.y, this.z, this.xd, this.yd, this.zd);
            }
        }
        
        @Override
        protected void postMoveUpdate() {
            this.xd *= 0.02;
            this.yd *= 0.02;
            this.zd *= 0.02;
        }
    }
    
    static class CoolingDripHangParticle extends DripHangParticle {
        private CoolingDripHangParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final Fluid cut, final ParticleOptions hf) {
            super(dwl, double2, double3, double4, cut, hf);
        }
        
        @Override
        protected void preMoveUpdate() {
            this.rCol = 1.0f;
            this.gCol = 16.0f / (40 - this.lifetime + 16);
            this.bCol = 4.0f / (40 - this.lifetime + 8);
            super.preMoveUpdate();
        }
    }
    
    static class FallAndLandParticle extends FallingParticle {
        protected final ParticleOptions landParticle;
        
        private FallAndLandParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final Fluid cut, final ParticleOptions hf) {
            super(dwl, double2, double3, double4, cut);
            this.landParticle = hf;
        }
        
        @Override
        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
                this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
            }
        }
    }
    
    static class HoneyFallAndLandParticle extends FallAndLandParticle {
        private HoneyFallAndLandParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final Fluid cut, final ParticleOptions hf) {
            super(dwl, double2, double3, double4, cut, hf);
        }
        
        @Override
        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
                this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
                this.level.playLocalSound(this.x + 0.5, this.y, this.z + 0.5, SoundEvents.BEEHIVE_DRIP, SoundSource.BLOCKS, 0.3f + this.level.random.nextFloat() * 2.0f / 3.0f, 1.0f, false);
            }
        }
    }
    
    static class FallingParticle extends DripParticle {
        private FallingParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final Fluid cut) {
            super(dwl, double2, double3, double4, cut, null);
            this.lifetime = (int)(64.0 / (Math.random() * 0.8 + 0.2));
        }
        
        @Override
        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
            }
        }
    }
    
    static class DripLandParticle extends DripParticle {
        private DripLandParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final Fluid cut) {
            super(dwl, double2, double3, double4, cut, null);
            this.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        }
    }
    
    public static class WaterHangProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;
        
        public WaterHangProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final DripParticle dxh16 = new DripHangParticle(dwl, double3, double4, double5, (Fluid)Fluids.WATER, (ParticleOptions)ParticleTypes.FALLING_WATER);
            dxh16.setColor(0.2f, 0.3f, 1.0f);
            dxh16.pickSprite(this.sprite);
            return dxh16;
        }
    }
    
    public static class WaterFallProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;
        
        public WaterFallProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final DripParticle dxh16 = new FallAndLandParticle(dwl, double3, double4, double5, (Fluid)Fluids.WATER, (ParticleOptions)ParticleTypes.SPLASH);
            dxh16.setColor(0.2f, 0.3f, 1.0f);
            dxh16.pickSprite(this.sprite);
            return dxh16;
        }
    }
    
    public static class LavaHangProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;
        
        public LavaHangProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final CoolingDripHangParticle a16 = new CoolingDripHangParticle(dwl, double3, double4, double5, (Fluid)Fluids.LAVA, (ParticleOptions)ParticleTypes.FALLING_LAVA);
            a16.pickSprite(this.sprite);
            return a16;
        }
    }
    
    public static class LavaFallProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;
        
        public LavaFallProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final DripParticle dxh16 = new FallAndLandParticle(dwl, double3, double4, double5, (Fluid)Fluids.LAVA, (ParticleOptions)ParticleTypes.LANDING_LAVA);
            dxh16.setColor(1.0f, 0.2857143f, 0.083333336f);
            dxh16.pickSprite(this.sprite);
            return dxh16;
        }
    }
    
    public static class LavaLandProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;
        
        public LavaLandProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final DripParticle dxh16 = new DripLandParticle(dwl, double3, double4, double5, (Fluid)Fluids.LAVA);
            dxh16.setColor(1.0f, 0.2857143f, 0.083333336f);
            dxh16.pickSprite(this.sprite);
            return dxh16;
        }
    }
    
    public static class HoneyHangProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;
        
        public HoneyHangProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final DripHangParticle dripHangParticle;
            final DripHangParticle b16 = dripHangParticle = new DripHangParticle(dwl, double3, double4, double5, Fluids.EMPTY, (ParticleOptions)ParticleTypes.FALLING_HONEY);
            dripHangParticle.gravity *= 0.01f;
            b16.lifetime = 100;
            b16.setColor(0.622f, 0.508f, 0.082f);
            b16.pickSprite(this.sprite);
            return b16;
        }
    }
    
    public static class HoneyFallProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;
        
        public HoneyFallProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final DripParticle dxh16 = new HoneyFallAndLandParticle(dwl, double3, double4, double5, Fluids.EMPTY, (ParticleOptions)ParticleTypes.LANDING_HONEY);
            dxh16.gravity = 0.01f;
            dxh16.setColor(0.582f, 0.448f, 0.082f);
            dxh16.pickSprite(this.sprite);
            return dxh16;
        }
    }
    
    public static class HoneyLandProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;
        
        public HoneyLandProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final DripParticle dxh16 = new DripLandParticle(dwl, double3, double4, double5, Fluids.EMPTY);
            dxh16.lifetime = (int)(128.0 / (Math.random() * 0.8 + 0.2));
            dxh16.setColor(0.522f, 0.408f, 0.082f);
            dxh16.pickSprite(this.sprite);
            return dxh16;
        }
    }
    
    public static class NectarFallProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;
        
        public NectarFallProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final DripParticle dxh16 = new FallingParticle(dwl, double3, double4, double5, Fluids.EMPTY);
            dxh16.lifetime = (int)(16.0 / (Math.random() * 0.8 + 0.2));
            dxh16.gravity = 0.007f;
            dxh16.setColor(0.92f, 0.782f, 0.72f);
            dxh16.pickSprite(this.sprite);
            return dxh16;
        }
    }
    
    public static class ObsidianTearHangProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;
        
        public ObsidianTearHangProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final DripHangParticle b16 = new DripHangParticle(dwl, double3, double4, double5, Fluids.EMPTY, (ParticleOptions)ParticleTypes.FALLING_OBSIDIAN_TEAR);
            b16.isGlowing = true;
            final DripHangParticle dripHangParticle = b16;
            dripHangParticle.gravity *= 0.01f;
            b16.lifetime = 100;
            b16.setColor(0.51171875f, 0.03125f, 0.890625f);
            b16.pickSprite(this.sprite);
            return b16;
        }
    }
    
    public static class ObsidianTearFallProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;
        
        public ObsidianTearFallProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final DripParticle dxh16 = new FallAndLandParticle(dwl, double3, double4, double5, Fluids.EMPTY, (ParticleOptions)ParticleTypes.LANDING_OBSIDIAN_TEAR);
            dxh16.isGlowing = true;
            dxh16.gravity = 0.01f;
            dxh16.setColor(0.51171875f, 0.03125f, 0.890625f);
            dxh16.pickSprite(this.sprite);
            return dxh16;
        }
    }
    
    public static class ObsidianTearLandProvider implements ParticleProvider<SimpleParticleType> {
        protected final SpriteSet sprite;
        
        public ObsidianTearLandProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final DripParticle dxh16 = new DripLandParticle(dwl, double3, double4, double5, Fluids.EMPTY);
            dxh16.isGlowing = true;
            dxh16.lifetime = (int)(28.0 / (Math.random() * 0.8 + 0.2));
            dxh16.setColor(0.51171875f, 0.03125f, 0.890625f);
            dxh16.pickSprite(this.sprite);
            return dxh16;
        }
    }
}
