package net.minecraft.client.particle;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.BlockAndTintGetter;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TerrainParticle extends TextureSheetParticle {
    private final BlockState blockState;
    private BlockPos pos;
    private final float uo;
    private final float vo;
    
    public TerrainParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final BlockState cee) {
        super(dwl, double2, double3, double4, double5, double6, double7);
        this.blockState = cee;
        this.setSprite(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(cee));
        this.gravity = 1.0f;
        this.rCol = 0.6f;
        this.gCol = 0.6f;
        this.bCol = 0.6f;
        this.quadSize /= 2.0f;
        this.uo = this.random.nextFloat() * 3.0f;
        this.vo = this.random.nextFloat() * 3.0f;
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }
    
    public TerrainParticle init(final BlockPos fx) {
        this.pos = fx;
        if (this.blockState.is(Blocks.GRASS_BLOCK)) {
            return this;
        }
        this.multiplyColor(fx);
        return this;
    }
    
    public TerrainParticle init() {
        this.pos = new BlockPos(this.x, this.y, this.z);
        if (this.blockState.is(Blocks.GRASS_BLOCK)) {
            return this;
        }
        this.multiplyColor(this.pos);
        return this;
    }
    
    protected void multiplyColor(@Nullable final BlockPos fx) {
        final int integer3 = Minecraft.getInstance().getBlockColors().getColor(this.blockState, this.level, fx, 0);
        this.rCol *= (integer3 >> 16 & 0xFF) / 255.0f;
        this.gCol *= (integer3 >> 8 & 0xFF) / 255.0f;
        this.bCol *= (integer3 & 0xFF) / 255.0f;
    }
    
    @Override
    protected float getU0() {
        return this.sprite.getU((this.uo + 1.0f) / 4.0f * 16.0f);
    }
    
    @Override
    protected float getU1() {
        return this.sprite.getU(this.uo / 4.0f * 16.0f);
    }
    
    @Override
    protected float getV0() {
        return this.sprite.getV(this.vo / 4.0f * 16.0f);
    }
    
    @Override
    protected float getV1() {
        return this.sprite.getV((this.vo + 1.0f) / 4.0f * 16.0f);
    }
    
    public int getLightColor(final float float1) {
        final int integer3 = super.getLightColor(float1);
        int integer4 = 0;
        if (this.level.hasChunkAt(this.pos)) {
            integer4 = LevelRenderer.getLightColor(this.level, this.pos);
        }
        return (integer3 == 0) ? integer4 : integer3;
    }
    
    public static class Provider implements ParticleProvider<BlockParticleOption> {
        public Particle createParticle(final BlockParticleOption hc, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final BlockState cee16 = hc.getState();
            if (cee16.isAir() || cee16.is(Blocks.MOVING_PISTON)) {
                return null;
            }
            return new TerrainParticle(dwl, double3, double4, double5, double6, double7, double8, cee16).init();
        }
    }
}
