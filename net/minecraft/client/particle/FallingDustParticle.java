package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.Level;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.client.multiplayer.ClientLevel;

public class FallingDustParticle extends TextureSheetParticle {
    private final float rotSpeed;
    private final SpriteSet sprites;
    
    private FallingDustParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final float float5, final float float6, final float float7, final SpriteSet dyo) {
        super(dwl, double2, double3, double4);
        this.sprites = dyo;
        this.rCol = float5;
        this.gCol = float6;
        this.bCol = float7;
        final float float8 = 0.9f;
        this.quadSize *= 0.67499995f;
        final int integer14 = (int)(32.0 / (Math.random() * 0.8 + 0.2));
        this.lifetime = (int)Math.max(integer14 * 0.9f, 1.0f);
        this.setSpriteFromAge(dyo);
        this.rotSpeed = ((float)Math.random() - 0.5f) * 0.1f;
        this.roll = (float)Math.random() * 6.2831855f;
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
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
        this.oRoll = this.roll;
        this.roll += 3.1415927f * this.rotSpeed * 2.0f;
        if (this.onGround) {
            final float n = 0.0f;
            this.roll = n;
            this.oRoll = n;
        }
        this.move(this.xd, this.yd, this.zd);
        this.yd -= 0.003000000026077032;
        this.yd = Math.max(this.yd, -0.14000000059604645);
    }
    
    public static class Provider implements ParticleProvider<BlockParticleOption> {
        private final SpriteSet sprite;
        
        public Provider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        @Nullable
        public Particle createParticle(final BlockParticleOption hc, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final BlockState cee16 = hc.getState();
            if (!cee16.isAir() && cee16.getRenderShape() == RenderShape.INVISIBLE) {
                return null;
            }
            final BlockPos fx17 = new BlockPos(double3, double4, double5);
            int integer18 = Minecraft.getInstance().getBlockColors().getColor(cee16, dwl, fx17);
            if (cee16.getBlock() instanceof FallingBlock) {
                integer18 = ((FallingBlock)cee16.getBlock()).getDustColor(cee16, dwl, fx17);
            }
            final float float19 = (integer18 >> 16 & 0xFF) / 255.0f;
            final float float20 = (integer18 >> 8 & 0xFF) / 255.0f;
            final float float21 = (integer18 & 0xFF) / 255.0f;
            return new FallingDustParticle(dwl, double3, double4, double5, float19, float20, float21, this.sprite, null);
        }
    }
}
