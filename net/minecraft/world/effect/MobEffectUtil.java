package net.minecraft.world.effect;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.StringUtil;
import net.minecraft.util.Mth;

public final class MobEffectUtil {
    public static String formatDuration(final MobEffectInstance apr, final float float2) {
        if (apr.isNoCounter()) {
            return "**:**";
        }
        final int integer3 = Mth.floor(apr.getDuration() * float2);
        return StringUtil.formatTickDuration(integer3);
    }
    
    public static boolean hasDigSpeed(final LivingEntity aqj) {
        return aqj.hasEffect(MobEffects.DIG_SPEED) || aqj.hasEffect(MobEffects.CONDUIT_POWER);
    }
    
    public static int getDigSpeedAmplification(final LivingEntity aqj) {
        int integer2 = 0;
        int integer3 = 0;
        if (aqj.hasEffect(MobEffects.DIG_SPEED)) {
            integer2 = aqj.getEffect(MobEffects.DIG_SPEED).getAmplifier();
        }
        if (aqj.hasEffect(MobEffects.CONDUIT_POWER)) {
            integer3 = aqj.getEffect(MobEffects.CONDUIT_POWER).getAmplifier();
        }
        return Math.max(integer2, integer3);
    }
    
    public static boolean hasWaterBreathing(final LivingEntity aqj) {
        return aqj.hasEffect(MobEffects.WATER_BREATHING) || aqj.hasEffect(MobEffects.CONDUIT_POWER);
    }
}
