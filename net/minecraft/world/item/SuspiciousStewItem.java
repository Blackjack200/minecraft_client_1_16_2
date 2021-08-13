package net.minecraft.world.item;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;

public class SuspiciousStewItem extends Item {
    public SuspiciousStewItem(final Properties a) {
        super(a);
    }
    
    public static void saveMobEffect(final ItemStack bly, final MobEffect app, final int integer) {
        final CompoundTag md4 = bly.getOrCreateTag();
        final ListTag mj5 = md4.getList("Effects", 9);
        final CompoundTag md5 = new CompoundTag();
        md5.putByte("EffectId", (byte)MobEffect.getId(app));
        md5.putInt("EffectDuration", integer);
        mj5.add(md5);
        md4.put("Effects", (Tag)mj5);
    }
    
    @Override
    public ItemStack finishUsingItem(final ItemStack bly, final Level bru, final LivingEntity aqj) {
        final ItemStack bly2 = super.finishUsingItem(bly, bru, aqj);
        final CompoundTag md6 = bly.getTag();
        if (md6 != null && md6.contains("Effects", 9)) {
            final ListTag mj7 = md6.getList("Effects", 10);
            for (int integer8 = 0; integer8 < mj7.size(); ++integer8) {
                int integer9 = 160;
                final CompoundTag md7 = mj7.getCompound(integer8);
                if (md7.contains("EffectDuration", 3)) {
                    integer9 = md7.getInt("EffectDuration");
                }
                final MobEffect app11 = MobEffect.byId(md7.getByte("EffectId"));
                if (app11 != null) {
                    aqj.addEffect(new MobEffectInstance(app11, integer9));
                }
            }
        }
        if (aqj instanceof Player && ((Player)aqj).abilities.instabuild) {
            return bly2;
        }
        return new ItemStack(Items.BOWL);
    }
}
