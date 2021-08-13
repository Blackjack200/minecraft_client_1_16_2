package net.minecraft.client.tutorial;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.Input;

public interface TutorialStepInstance {
    default void clear() {
    }
    
    default void tick() {
    }
    
    default void onInput(final Input dzc) {
    }
    
    default void onMouse(final double double1, final double double2) {
    }
    
    default void onLookAt(final ClientLevel dwl, final HitResult dci) {
    }
    
    default void onDestroyBlock(final ClientLevel dwl, final BlockPos fx, final BlockState cee, final float float4) {
    }
    
    default void onOpenInventory() {
    }
    
    default void onGetItem(final ItemStack bly) {
    }
}
