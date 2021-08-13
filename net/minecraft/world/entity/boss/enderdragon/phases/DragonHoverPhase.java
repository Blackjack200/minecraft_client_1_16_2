package net.minecraft.world.entity.boss.enderdragon.phases;

import javax.annotation.Nullable;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.phys.Vec3;

public class DragonHoverPhase extends AbstractDragonPhaseInstance {
    private Vec3 targetLocation;
    
    public DragonHoverPhase(final EnderDragon bbo) {
        super(bbo);
    }
    
    @Override
    public void doServerTick() {
        if (this.targetLocation == null) {
            this.targetLocation = this.dragon.position();
        }
    }
    
    @Override
    public boolean isSitting() {
        return true;
    }
    
    @Override
    public void begin() {
        this.targetLocation = null;
    }
    
    @Override
    public float getFlySpeed() {
        return 1.0f;
    }
    
    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }
    
    public EnderDragonPhase<DragonHoverPhase> getPhase() {
        return EnderDragonPhase.HOVERING;
    }
}
