package net.minecraft.world.item;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.network.chat.ChatType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.Util;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import java.util.Collection;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.core.Registry;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DebugStickItem extends Item {
    public DebugStickItem(final Properties a) {
        super(a);
    }
    
    @Override
    public boolean isFoil(final ItemStack bly) {
        return true;
    }
    
    @Override
    public boolean canAttackBlock(final BlockState cee, final Level bru, final BlockPos fx, final Player bft) {
        if (!bru.isClientSide) {
            this.handleInteraction(bft, cee, bru, fx, false, bft.getItemInHand(InteractionHand.MAIN_HAND));
        }
        return false;
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final Player bft3 = bnx.getPlayer();
        final Level bru4 = bnx.getLevel();
        if (!bru4.isClientSide && bft3 != null) {
            final BlockPos fx5 = bnx.getClickedPos();
            this.handleInteraction(bft3, bru4.getBlockState(fx5), bru4, fx5, true, bnx.getItemInHand());
        }
        return InteractionResult.sidedSuccess(bru4.isClientSide);
    }
    
    private void handleInteraction(final Player bft, final BlockState cee, final LevelAccessor brv, final BlockPos fx, final boolean boolean5, final ItemStack bly) {
        if (!bft.canUseGameMasterBlocks()) {
            return;
        }
        final Block bul8 = cee.getBlock();
        final StateDefinition<Block, BlockState> cef9 = bul8.getStateDefinition();
        final Collection<Property<?>> collection10 = cef9.getProperties();
        final String string11 = Registry.BLOCK.getKey(bul8).toString();
        if (collection10.isEmpty()) {
            message(bft, new TranslatableComponent(this.getDescriptionId() + ".empty", new Object[] { string11 }));
            return;
        }
        final CompoundTag md12 = bly.getOrCreateTagElement("DebugProperty");
        final String string12 = md12.getString(string11);
        Property<?> cfg14 = cef9.getProperty(string12);
        if (boolean5) {
            if (cfg14 == null) {
                cfg14 = collection10.iterator().next();
            }
            final BlockState cee2 = DebugStickItem.cycleState(cee, cfg14, bft.isSecondaryUseActive());
            brv.setBlock(fx, cee2, 18);
            message(bft, new TranslatableComponent(this.getDescriptionId() + ".update", new Object[] { cfg14.getName(), DebugStickItem.getNameHelper(cee2, cfg14) }));
        }
        else {
            cfg14 = DebugStickItem.<Property<?>>getRelative((java.lang.Iterable<Property<?>>)collection10, cfg14, bft.isSecondaryUseActive());
            final String string13 = cfg14.getName();
            md12.putString(string11, string13);
            message(bft, new TranslatableComponent(this.getDescriptionId() + ".select", new Object[] { string13, DebugStickItem.getNameHelper(cee, cfg14) }));
        }
    }
    
    private static <T extends Comparable<T>> BlockState cycleState(final BlockState cee, final Property<T> cfg, final boolean boolean3) {
        return ((StateHolder<O, BlockState>)cee).<T, Comparable>setValue(cfg, (Comparable)DebugStickItem.<V>getRelative((java.lang.Iterable<V>)cfg.getPossibleValues(), (V)cee.<T>getValue((Property<T>)cfg), boolean3));
    }
    
    private static <T> T getRelative(final Iterable<T> iterable, @Nullable final T object, final boolean boolean3) {
        return boolean3 ? Util.<T>findPreviousInIterable(iterable, object) : Util.<T>findNextInIterable(iterable, object);
    }
    
    private static void message(final Player bft, final Component nr) {
        ((ServerPlayer)bft).sendMessage(nr, ChatType.GAME_INFO, Util.NIL_UUID);
    }
    
    private static <T extends Comparable<T>> String getNameHelper(final BlockState cee, final Property<T> cfg) {
        return cfg.getName(cee.<T>getValue(cfg));
    }
}
