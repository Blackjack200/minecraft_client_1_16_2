package net.minecraft.world.entity.ai.util;

import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.Mob;

public class GoalUtils {
    public static boolean hasGroundPathNavigation(final Mob aqk) {
        return aqk.getNavigation() instanceof GroundPathNavigation;
    }
}
