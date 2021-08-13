package net.minecraft.client.renderer.item;

import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;

public interface ItemPropertyFunction {
    float call(final ItemStack bly, @Nullable final ClientLevel dwl, @Nullable final LivingEntity aqj);
}
