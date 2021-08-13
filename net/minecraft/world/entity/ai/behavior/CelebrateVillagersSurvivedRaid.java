package net.minecraft.world.entity.ai.behavior;

import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.FireworkRocketItem;
import com.google.common.collect.Lists;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import java.util.Random;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.Util;
import net.minecraft.world.item.DyeColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import javax.annotation.Nullable;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.npc.Villager;

public class CelebrateVillagersSurvivedRaid extends Behavior<Villager> {
    @Nullable
    private Raid currentRaid;
    
    public CelebrateVillagersSurvivedRaid(final int integer1, final int integer2) {
        super((Map)ImmutableMap.of(), integer1, integer2);
    }
    
    @Override
    protected boolean checkExtraStartConditions(final ServerLevel aag, final Villager bfg) {
        final BlockPos fx4 = bfg.blockPosition();
        this.currentRaid = aag.getRaidAt(fx4);
        return this.currentRaid != null && this.currentRaid.isVictory() && MoveToSkySeeingSpot.hasNoBlocksAbove(aag, bfg, fx4);
    }
    
    @Override
    protected boolean canStillUse(final ServerLevel aag, final Villager bfg, final long long3) {
        return this.currentRaid != null && !this.currentRaid.isStopped();
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Villager bfg, final long long3) {
        this.currentRaid = null;
        bfg.getBrain().updateActivityFromSchedule(aag.getDayTime(), aag.getGameTime());
    }
    
    @Override
    protected void stop(final ServerLevel aag, final Villager bfg, final long long3) {
        final Random random6 = bfg.getRandom();
        if (random6.nextInt(100) == 0) {
            bfg.playCelebrateSound();
        }
        if (random6.nextInt(200) == 0 && MoveToSkySeeingSpot.hasNoBlocksAbove(aag, bfg, bfg.blockPosition())) {
            final DyeColor bku7 = Util.<DyeColor>getRandom(DyeColor.values(), random6);
            final int integer8 = random6.nextInt(3);
            final ItemStack bly9 = this.getFirework(bku7, integer8);
            final FireworkRocketEntity bge10 = new FireworkRocketEntity(bfg.level, bfg, bfg.getX(), bfg.getEyeY(), bfg.getZ(), bly9);
            bfg.level.addFreshEntity(bge10);
        }
    }
    
    private ItemStack getFirework(final DyeColor bku, final int integer) {
        final ItemStack bly4 = new ItemStack(Items.FIREWORK_ROCKET, 1);
        final ItemStack bly5 = new ItemStack(Items.FIREWORK_STAR);
        final CompoundTag md6 = bly5.getOrCreateTagElement("Explosion");
        final List<Integer> list7 = (List<Integer>)Lists.newArrayList();
        list7.add(bku.getFireworkColor());
        md6.putIntArray("Colors", list7);
        md6.putByte("Type", (byte)FireworkRocketItem.Shape.BURST.getId());
        final CompoundTag md7 = bly4.getOrCreateTagElement("Fireworks");
        final ListTag mj9 = new ListTag();
        final CompoundTag md8 = bly5.getTagElement("Explosion");
        if (md8 != null) {
            mj9.add(md8);
        }
        md7.putByte("Flight", (byte)integer);
        if (!mj9.isEmpty()) {
            md7.put("Explosions", (Tag)mj9);
        }
        return bly4;
    }
}
