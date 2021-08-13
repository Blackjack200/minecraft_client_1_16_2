package net.minecraft.client.particle;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.client.Camera;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.FireworkRocketItem;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.ListTag;

public class FireworkParticles {
    public static class Starter extends NoRenderParticle {
        private int life;
        private final ParticleEngine engine;
        private ListTag explosions;
        private boolean twinkleDelay;
        
        public Starter(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final ParticleEngine dya, @Nullable final CompoundTag md) {
            super(dwl, double2, double3, double4);
            this.xd = double5;
            this.yd = double6;
            this.zd = double7;
            this.engine = dya;
            this.lifetime = 8;
            if (md != null) {
                this.explosions = md.getList("Explosions", 10);
                if (this.explosions.isEmpty()) {
                    this.explosions = null;
                }
                else {
                    this.lifetime = this.explosions.size() * 2 - 1;
                    for (int integer17 = 0; integer17 < this.explosions.size(); ++integer17) {
                        final CompoundTag md2 = this.explosions.getCompound(integer17);
                        if (md2.getBoolean("Flicker")) {
                            this.twinkleDelay = true;
                            this.lifetime += 15;
                            break;
                        }
                    }
                }
            }
        }
        
        @Override
        public void tick() {
            if (this.life == 0 && this.explosions != null) {
                final boolean boolean2 = this.isFarAwayFromCamera();
                boolean boolean3 = false;
                if (this.explosions.size() >= 3) {
                    boolean3 = true;
                }
                else {
                    for (int integer4 = 0; integer4 < this.explosions.size(); ++integer4) {
                        final CompoundTag md5 = this.explosions.getCompound(integer4);
                        if (FireworkRocketItem.Shape.byId(md5.getByte("Type")) == FireworkRocketItem.Shape.LARGE_BALL) {
                            boolean3 = true;
                            break;
                        }
                    }
                }
                SoundEvent adn4;
                if (boolean3) {
                    adn4 = (boolean2 ? SoundEvents.FIREWORK_ROCKET_LARGE_BLAST_FAR : SoundEvents.FIREWORK_ROCKET_LARGE_BLAST);
                }
                else {
                    adn4 = (boolean2 ? SoundEvents.FIREWORK_ROCKET_BLAST_FAR : SoundEvents.FIREWORK_ROCKET_BLAST);
                }
                this.level.playLocalSound(this.x, this.y, this.z, adn4, SoundSource.AMBIENT, 20.0f, 0.95f + this.random.nextFloat() * 0.1f, true);
            }
            if (this.life % 2 == 0 && this.explosions != null && this.life / 2 < this.explosions.size()) {
                final int integer5 = this.life / 2;
                final CompoundTag md6 = this.explosions.getCompound(integer5);
                final FireworkRocketItem.Shape a4 = FireworkRocketItem.Shape.byId(md6.getByte("Type"));
                final boolean boolean4 = md6.getBoolean("Trail");
                final boolean boolean5 = md6.getBoolean("Flicker");
                int[] arr7 = md6.getIntArray("Colors");
                final int[] arr8 = md6.getIntArray("FadeColors");
                if (arr7.length == 0) {
                    arr7 = new int[] { DyeColor.BLACK.getFireworkColor() };
                }
                switch (a4) {
                    default: {
                        this.createParticleBall(0.25, 2, arr7, arr8, boolean4, boolean5);
                        break;
                    }
                    case LARGE_BALL: {
                        this.createParticleBall(0.5, 4, arr7, arr8, boolean4, boolean5);
                        break;
                    }
                    case STAR: {
                        this.createParticleShape(0.5, new double[][] { { 0.0, 1.0 }, { 0.3455, 0.309 }, { 0.9511, 0.309 }, { 0.3795918367346939, -0.12653061224489795 }, { 0.6122448979591837, -0.8040816326530612 }, { 0.0, -0.35918367346938773 } }, arr7, arr8, boolean4, boolean5, false);
                        break;
                    }
                    case CREEPER: {
                        this.createParticleShape(0.5, new double[][] { { 0.0, 0.2 }, { 0.2, 0.2 }, { 0.2, 0.6 }, { 0.6, 0.6 }, { 0.6, 0.2 }, { 0.2, 0.2 }, { 0.2, 0.0 }, { 0.4, 0.0 }, { 0.4, -0.6 }, { 0.2, -0.6 }, { 0.2, -0.4 }, { 0.0, -0.4 } }, arr7, arr8, boolean4, boolean5, true);
                        break;
                    }
                    case BURST: {
                        this.createParticleBurst(arr7, arr8, boolean4, boolean5);
                        break;
                    }
                }
                final int integer6 = arr7[0];
                final float float10 = ((integer6 & 0xFF0000) >> 16) / 255.0f;
                final float float11 = ((integer6 & 0xFF00) >> 8) / 255.0f;
                final float float12 = ((integer6 & 0xFF) >> 0) / 255.0f;
                final Particle dxy13 = this.engine.createParticle(ParticleTypes.FLASH, this.x, this.y, this.z, 0.0, 0.0, 0.0);
                dxy13.setColor(float10, float11, float12);
            }
            ++this.life;
            if (this.life > this.lifetime) {
                if (this.twinkleDelay) {
                    final boolean boolean2 = this.isFarAwayFromCamera();
                    final SoundEvent adn5 = boolean2 ? SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR : SoundEvents.FIREWORK_ROCKET_TWINKLE;
                    this.level.playLocalSound(this.x, this.y, this.z, adn5, SoundSource.AMBIENT, 20.0f, 0.9f + this.random.nextFloat() * 0.15f, true);
                }
                this.remove();
            }
        }
        
        private boolean isFarAwayFromCamera() {
            final Minecraft djw2 = Minecraft.getInstance();
            return djw2.gameRenderer.getMainCamera().getPosition().distanceToSqr(this.x, this.y, this.z) >= 256.0;
        }
        
        private void createParticle(final double double1, final double double2, final double double3, final double double4, final double double5, final double double6, final int[] arr7, final int[] arr8, final boolean boolean9, final boolean boolean10) {
            final SparkParticle c18 = (SparkParticle)this.engine.createParticle(ParticleTypes.FIREWORK, double1, double2, double3, double4, double5, double6);
            c18.setTrail(boolean9);
            c18.setFlicker(boolean10);
            c18.setAlpha(0.99f);
            final int integer19 = this.random.nextInt(arr7.length);
            c18.setColor(arr7[integer19]);
            if (arr8.length > 0) {
                c18.setFadeColor(Util.getRandom(arr8, this.random));
            }
        }
        
        private void createParticleBall(final double double1, final int integer, final int[] arr3, final int[] arr4, final boolean boolean5, final boolean boolean6) {
            final double double2 = this.x;
            final double double3 = this.y;
            final double double4 = this.z;
            for (int integer2 = -integer; integer2 <= integer; ++integer2) {
                for (int integer3 = -integer; integer3 <= integer; ++integer3) {
                    for (int integer4 = -integer; integer4 <= integer; ++integer4) {
                        final double double5 = integer3 + (this.random.nextDouble() - this.random.nextDouble()) * 0.5;
                        final double double6 = integer2 + (this.random.nextDouble() - this.random.nextDouble()) * 0.5;
                        final double double7 = integer4 + (this.random.nextDouble() - this.random.nextDouble()) * 0.5;
                        final double double8 = Mth.sqrt(double5 * double5 + double6 * double6 + double7 * double7) / double1 + this.random.nextGaussian() * 0.05;
                        this.createParticle(double2, double3, double4, double5 / double8, double6 / double8, double7 / double8, arr3, arr4, boolean5, boolean6);
                        if (integer2 != -integer && integer2 != integer && integer3 != -integer && integer3 != integer) {
                            integer4 += integer * 2 - 1;
                        }
                    }
                }
            }
        }
        
        private void createParticleShape(final double double1, final double[][] arr, final int[] arr3, final int[] arr4, final boolean boolean5, final boolean boolean6, final boolean boolean7) {
            final double double2 = arr[0][0];
            final double double3 = arr[0][1];
            this.createParticle(this.x, this.y, this.z, double2 * double1, double3 * double1, 0.0, arr3, arr4, boolean5, boolean6);
            final float float14 = this.random.nextFloat() * 3.1415927f;
            final double double4 = boolean7 ? 0.034 : 0.34;
            for (int integer17 = 0; integer17 < 3; ++integer17) {
                final double double5 = float14 + integer17 * 3.1415927f * double4;
                double double6 = double2;
                double double7 = double3;
                for (int integer18 = 1; integer18 < arr.length; ++integer18) {
                    final double double8 = arr[integer18][0];
                    final double double9 = arr[integer18][1];
                    for (double double10 = 0.25; double10 <= 1.0; double10 += 0.25) {
                        double double11 = Mth.lerp(double10, double6, double8) * double1;
                        final double double12 = Mth.lerp(double10, double7, double9) * double1;
                        final double double13 = double11 * Math.sin(double5);
                        double11 *= Math.cos(double5);
                        for (double double14 = -1.0; double14 <= 1.0; double14 += 2.0) {
                            this.createParticle(this.x, this.y, this.z, double11 * double14, double12, double13 * double14, arr3, arr4, boolean5, boolean6);
                        }
                    }
                    double6 = double8;
                    double7 = double9;
                }
            }
        }
        
        private void createParticleBurst(final int[] arr1, final int[] arr2, final boolean boolean3, final boolean boolean4) {
            final double double6 = this.random.nextGaussian() * 0.05;
            final double double7 = this.random.nextGaussian() * 0.05;
            for (int integer10 = 0; integer10 < 70; ++integer10) {
                final double double8 = this.xd * 0.5 + this.random.nextGaussian() * 0.15 + double6;
                final double double9 = this.zd * 0.5 + this.random.nextGaussian() * 0.15 + double7;
                final double double10 = this.yd * 0.5 + this.random.nextDouble() * 0.5;
                this.createParticle(this.x, this.y, this.z, double8, double10, double9, arr1, arr2, boolean3, boolean4);
            }
        }
    }
    
    static class SparkParticle extends SimpleAnimatedParticle {
        private boolean trail;
        private boolean flicker;
        private final ParticleEngine engine;
        private float fadeR;
        private float fadeG;
        private float fadeB;
        private boolean hasFade;
        
        private SparkParticle(final ClientLevel dwl, final double double2, final double double3, final double double4, final double double5, final double double6, final double double7, final ParticleEngine dya, final SpriteSet dyo) {
            super(dwl, double2, double3, double4, dyo, -0.004f);
            this.xd = double5;
            this.yd = double6;
            this.zd = double7;
            this.engine = dya;
            this.quadSize *= 0.75f;
            this.lifetime = 48 + this.random.nextInt(12);
            this.setSpriteFromAge(dyo);
        }
        
        public void setTrail(final boolean boolean1) {
            this.trail = boolean1;
        }
        
        public void setFlicker(final boolean boolean1) {
            this.flicker = boolean1;
        }
        
        @Override
        public void render(final VertexConsumer dfn, final Camera djh, final float float3) {
            if (!this.flicker || this.age < this.lifetime / 3 || (this.age + this.lifetime) / 3 % 2 == 0) {
                super.render(dfn, djh, float3);
            }
        }
        
        @Override
        public void tick() {
            super.tick();
            if (this.trail && this.age < this.lifetime / 2 && (this.age + this.lifetime) % 2 == 0) {
                final SparkParticle c2 = new SparkParticle(this.level, this.x, this.y, this.z, 0.0, 0.0, 0.0, this.engine, this.sprites);
                c2.setAlpha(0.99f);
                c2.setColor(this.rCol, this.gCol, this.bCol);
                c2.age = c2.lifetime / 2;
                if (this.hasFade) {
                    c2.hasFade = true;
                    c2.fadeR = this.fadeR;
                    c2.fadeG = this.fadeG;
                    c2.fadeB = this.fadeB;
                }
                c2.flicker = this.flicker;
                this.engine.add(c2);
            }
        }
    }
    
    public static class OverlayParticle extends TextureSheetParticle {
        private OverlayParticle(final ClientLevel dwl, final double double2, final double double3, final double double4) {
            super(dwl, double2, double3, double4);
            this.lifetime = 4;
        }
        
        @Override
        public ParticleRenderType getRenderType() {
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }
        
        @Override
        public void render(final VertexConsumer dfn, final Camera djh, final float float3) {
            this.setAlpha(0.6f - (this.age + float3 - 1.0f) * 0.25f * 0.5f);
            super.render(dfn, djh, float3);
        }
        
        @Override
        public float getQuadSize(final float float1) {
            return 7.1f * Mth.sin((this.age + float1 - 1.0f) * 0.25f * 3.1415927f);
        }
    }
    
    public static class FlashProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;
        
        public FlashProvider(final SpriteSet dyo) {
            this.sprite = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final OverlayParticle b16 = new OverlayParticle(dwl, double3, double4, double5);
            b16.pickSprite(this.sprite);
            return b16;
        }
    }
    
    public static class SparkProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        
        public SparkProvider(final SpriteSet dyo) {
            this.sprites = dyo;
        }
        
        public Particle createParticle(final SimpleParticleType hi, final ClientLevel dwl, final double double3, final double double4, final double double5, final double double6, final double double7, final double double8) {
            final SparkParticle c16 = new SparkParticle(dwl, double3, double4, double5, double6, double7, double8, Minecraft.getInstance().particleEngine, this.sprites);
            c16.setAlpha(0.99f);
            return c16;
        }
    }
}
