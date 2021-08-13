package net.minecraft.world.item;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.entity.EntityType;

public class FishBucketItem extends BucketItem {
    private final EntityType<?> type;
    
    public FishBucketItem(final EntityType<?> aqb, final Fluid cut, final Properties a) {
        super(cut, a);
        this.type = aqb;
    }
    
    @Override
    public void checkExtraContent(final Level bru, final ItemStack bly, final BlockPos fx) {
        if (bru instanceof ServerLevel) {
            this.spawn((ServerLevel)bru, bly, fx);
        }
    }
    
    @Override
    protected void playEmptySound(@Nullable final Player bft, final LevelAccessor brv, final BlockPos fx) {
        brv.playSound(bft, fx, SoundEvents.BUCKET_EMPTY_FISH, SoundSource.NEUTRAL, 1.0f, 1.0f);
    }
    
    private void spawn(final ServerLevel aag, final ItemStack bly, final BlockPos fx) {
        final Entity apx5 = this.type.spawn(aag, bly, null, fx, MobSpawnType.BUCKET, true, false);
        if (apx5 != null) {
            ((AbstractFish)apx5).setFromBucket(true);
        }
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        if (this.type == EntityType.TROPICAL_FISH) {
            final CompoundTag md6 = bly.getTag();
            if (md6 != null && md6.contains("BucketVariantTag", 3)) {
                final int integer7 = md6.getInt("BucketVariantTag");
                final ChatFormatting[] arr8 = { ChatFormatting.ITALIC, ChatFormatting.GRAY };
                final String string9 = new StringBuilder().append("color.minecraft.").append(TropicalFish.getBaseColor(integer7)).toString();
                final String string10 = new StringBuilder().append("color.minecraft.").append(TropicalFish.getPatternColor(integer7)).toString();
                for (int integer8 = 0; integer8 < TropicalFish.COMMON_VARIANTS.length; ++integer8) {
                    if (integer7 == TropicalFish.COMMON_VARIANTS[integer8]) {
                        list.add(new TranslatableComponent(TropicalFish.getPredefinedName(integer8)).withStyle(arr8));
                        return;
                    }
                }
                list.add(new TranslatableComponent(TropicalFish.getFishTypeName(integer7)).withStyle(arr8));
                final MutableComponent nx11 = new TranslatableComponent(string9);
                if (!string9.equals(string10)) {
                    nx11.append(", ").append(new TranslatableComponent(string10));
                }
                nx11.withStyle(arr8);
                list.add(nx11);
            }
        }
    }
}
