package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class InteractGoal extends LookAtPlayerGoal {
    public InteractGoal(final Mob aqk, final Class<? extends LivingEntity> class2, final float float3, final float float4) {
        super(aqk, class2, float3, float4);
        this.setFlags((EnumSet<Flag>)EnumSet.of((Enum)Flag.LOOK, (Enum)Flag.MOVE));
    }
}
