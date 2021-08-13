package net.minecraft.world.entity.ai.goal;

import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.PathfinderMob;

public class WaterAvoidingRandomStrollGoal extends RandomStrollGoal {
    protected final float probability;
    
    public WaterAvoidingRandomStrollGoal(final PathfinderMob aqr, final double double2) {
        this(aqr, double2, 0.001f);
    }
    
    public WaterAvoidingRandomStrollGoal(final PathfinderMob aqr, final double double2, final float float3) {
        super(aqr, double2);
        this.probability = float3;
    }
    
    @Nullable
    @Override
    protected Vec3 getPosition() {
        if (this.mob.isInWaterOrBubble()) {
            final Vec3 dck2 = RandomPos.getLandPos(this.mob, 15, 7);
            return (dck2 == null) ? super.getPosition() : dck2;
        }
        if (this.mob.getRandom().nextFloat() >= this.probability) {
            return RandomPos.getLandPos(this.mob, 10, 7);
        }
        return super.getPosition();
    }
}
