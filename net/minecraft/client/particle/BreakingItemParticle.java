package net.minecraft.client.particle;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.client.multiplayer.ClientLevel;

public class BreakingItemParticle extends TextureSheetParticle {
    private final float uo;
    private final float vo;
    
    private BreakingItemParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final ItemStack bly) {
        this(dwl, double2, double3, double4, bly);
        this.xd *= 0.10000000149011612;
        this.yd *= 0.10000000149011612;
        this.zd *= 0.10000000149011612;
        this.xd += double5;
        this.yd += double6;
        this.zd += double7;
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }
    
    protected BreakingItemParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final ItemStack bly) {
        super(dwl, double2, double3, double4, 0.0, 0.0, 0.0);
        this.setSprite(Minecraft.getInstance().getItemRenderer().getModel(bly, dwl, null).getParticleIcon());
        this.gravity = 1.0f;
        this.quadSize /= 2.0f;
        this.uo = this.random.nextFloat() * 3.0f;
        this.vo = this.random.nextFloat() * 3.0f;
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
    
    public static class Provider implements ParticleProvider<ItemParticleOption> {
        public Particle createParticle(final ItemParticleOption he, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            return new BreakingItemParticle(dwl, double3, double4, double5, double6, double7, double8, he.getItem(), null);
        }
    }
    
    public static class SlimeProvider implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            return new BreakingItemParticle(dwl, double3, double4, double5, new ItemStack(Items.SLIME_BALL));
        }
    }
    
    public static class SnowballProvider implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            return new BreakingItemParticle(dwl, double3, double4, double5, new ItemStack(Items.SNOWBALL));
        }
    }
}
