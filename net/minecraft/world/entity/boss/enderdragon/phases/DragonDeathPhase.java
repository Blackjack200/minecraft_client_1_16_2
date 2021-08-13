package net.minecraft.world.entity.boss.enderdragon.phases;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.feature.EndPodiumFeature;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.phys.Vec3;

public class DragonDeathPhase extends AbstractDragonPhaseInstance {
    private Vec3 targetLocation;
    private int time;
    
    public DragonDeathPhase(final EnderDragon bbo) {
        super(bbo);
    }
    
    @Override
    public void doClientTick() {
        if (this.time++ % 10 == 0) {
            final float float2 = (this.dragon.getRandom().nextFloat() - 0.5f) * 8.0f;
            final float float3 = (this.dragon.getRandom().nextFloat() - 0.5f) * 4.0f;
            final float float4 = (this.dragon.getRandom().nextFloat() - 0.5f) * 8.0f;
            this.dragon.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.dragon.getX() + float2, this.dragon.getY() + 2.0 + float3, this.dragon.getZ() + float4, 0.0, 0.0, 0.0);
        }
    }
    
    @Override
    public void doServerTick() {
        ++this.time;
        if (this.targetLocation == null) {
            final BlockPos fx2 = this.dragon.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, EndPodiumFeature.END_PODIUM_LOCATION);
            this.targetLocation = Vec3.atBottomCenterOf(fx2);
        }
        final double double2 = this.targetLocation.distanceToSqr(this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
        if (double2 < 100.0 || double2 > 22500.0 || this.dragon.horizontalCollision || this.dragon.verticalCollision) {
            this.dragon.setHealth(0.0f);
        }
        else {
            this.dragon.setHealth(1.0f);
        }
    }
    
    @Override
    public void begin() {
        this.targetLocation = null;
        this.time = 0;
    }
    
    @Override
    public float getFlySpeed() {
        return 3.0f;
    }
    
    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }
    
    public EnderDragonPhase<DragonDeathPhase> getPhase() {
        return EnderDragonPhase.DYING;
    }
}
