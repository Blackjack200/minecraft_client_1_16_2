package net.minecraft.world.entity.boss.enderdragon.phases;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.AreaEffectCloud;

public class DragonSittingFlamingPhase extends AbstractDragonSittingPhase {
    private int flameTicks;
    private int flameCount;
    private AreaEffectCloud flame;
    
    public DragonSittingFlamingPhase(final EnderDragon bbo) {
        super(bbo);
    }
    
    @Override
    public void doClientTick() {
        ++this.flameTicks;
        if (this.flameTicks % 2 == 0 && this.flameTicks < 10) {
            final Vec3 dck2 = this.dragon.getHeadLookVector(1.0f).normalize();
            dck2.yRot(-0.7853982f);
            final double double3 = this.dragon.head.getX();
            final double double4 = this.dragon.head.getY(0.5);
            final double double5 = this.dragon.head.getZ();
            for (int integer9 = 0; integer9 < 8; ++integer9) {
                final double double6 = double3 + this.dragon.getRandom().nextGaussian() / 2.0;
                final double double7 = double4 + this.dragon.getRandom().nextGaussian() / 2.0;
                final double double8 = double5 + this.dragon.getRandom().nextGaussian() / 2.0;
                for (int integer10 = 0; integer10 < 6; ++integer10) {
                    this.dragon.level.addParticle(ParticleTypes.DRAGON_BREATH, double6, double7, double8, -dck2.x * 0.07999999821186066 * integer10, -dck2.y * 0.6000000238418579, -dck2.z * 0.07999999821186066 * integer10);
                }
                dck2.yRot(0.19634955f);
            }
        }
    }
    
    @Override
    public void doServerTick() {
        ++this.flameTicks;
        if (this.flameTicks >= 200) {
            if (this.flameCount >= 4) {
                this.dragon.getPhaseManager().setPhase(EnderDragonPhase.TAKEOFF);
            }
            else {
                this.dragon.getPhaseManager().setPhase(EnderDragonPhase.SITTING_SCANNING);
            }
        }
        else if (this.flameTicks == 10) {
            final Vec3 dck2 = new Vec3(this.dragon.head.getX() - this.dragon.getX(), 0.0, this.dragon.head.getZ() - this.dragon.getZ()).normalize();
            final float float3 = 5.0f;
            final double double4 = this.dragon.head.getX() + dck2.x * 5.0 / 2.0;
            final double double5 = this.dragon.head.getZ() + dck2.z * 5.0 / 2.0;
            double double7;
            final double double6 = double7 = this.dragon.head.getY(0.5);
            final BlockPos.MutableBlockPos a12 = new BlockPos.MutableBlockPos(double4, double7, double5);
            while (this.dragon.level.isEmptyBlock(a12)) {
                --double7;
                if (double7 < 0.0) {
                    double7 = double6;
                    break;
                }
                a12.set(double4, double7, double5);
            }
            double7 = Mth.floor(double7) + 1;
            (this.flame = new AreaEffectCloud(this.dragon.level, double4, double7, double5)).setOwner(this.dragon);
            this.flame.setRadius(5.0f);
            this.flame.setDuration(200);
            this.flame.setParticle(ParticleTypes.DRAGON_BREATH);
            this.flame.addEffect(new MobEffectInstance(MobEffects.HARM));
            this.dragon.level.addFreshEntity(this.flame);
        }
    }
    
    @Override
    public void begin() {
        this.flameTicks = 0;
        ++this.flameCount;
    }
    
    @Override
    public void end() {
        if (this.flame != null) {
            this.flame.remove();
            this.flame = null;
        }
    }
    
    public EnderDragonPhase<DragonSittingFlamingPhase> getPhase() {
        return EnderDragonPhase.SITTING_FLAMING;
    }
    
    public void resetFlameCount() {
        this.flameCount = 0;
    }
}
