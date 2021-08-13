package net.minecraft.world.item;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.Map;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.network.chat.Component;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Iterator;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.sounds.SoundSource;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class BlockItem extends Item {
    @Deprecated
    private final Block block;
    
    public BlockItem(final Block bul, final Properties a) {
        super(a);
        this.block = bul;
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final InteractionResult aor3 = this.place(new BlockPlaceContext(bnx));
        if (!aor3.consumesAction() && this.isEdible()) {
            return this.use(bnx.getLevel(), bnx.getPlayer(), bnx.getHand()).getResult();
        }
        return aor3;
    }
    
    public InteractionResult place(final BlockPlaceContext bnv) {
        if (!bnv.canPlace()) {
            return InteractionResult.FAIL;
        }
        final BlockPlaceContext bnv2 = this.updatePlacementContext(bnv);
        if (bnv2 == null) {
            return InteractionResult.FAIL;
        }
        final BlockState cee4 = this.getPlacementState(bnv2);
        if (cee4 == null) {
            return InteractionResult.FAIL;
        }
        if (!this.placeBlock(bnv2, cee4)) {
            return InteractionResult.FAIL;
        }
        final BlockPos fx5 = bnv2.getClickedPos();
        final Level bru6 = bnv2.getLevel();
        final Player bft7 = bnv2.getPlayer();
        final ItemStack bly8 = bnv2.getItemInHand();
        BlockState cee5 = bru6.getBlockState(fx5);
        final Block bul10 = cee5.getBlock();
        if (bul10 == cee4.getBlock()) {
            cee5 = this.updateBlockStateFromTag(fx5, bru6, bly8, cee5);
            this.updateCustomBlockEntityTag(fx5, bru6, bft7, bly8, cee5);
            bul10.setPlacedBy(bru6, fx5, cee5, bft7, bly8);
            if (bft7 instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)bft7, fx5, bly8);
            }
        }
        final SoundType cab11 = cee5.getSoundType();
        bru6.playSound(bft7, fx5, this.getPlaceSound(cee5), SoundSource.BLOCKS, (cab11.getVolume() + 1.0f) / 2.0f, cab11.getPitch() * 0.8f);
        if (bft7 == null || !bft7.abilities.instabuild) {
            bly8.shrink(1);
        }
        return InteractionResult.sidedSuccess(bru6.isClientSide);
    }
    
    protected SoundEvent getPlaceSound(final BlockState cee) {
        return cee.getSoundType().getPlaceSound();
    }
    
    @Nullable
    public BlockPlaceContext updatePlacementContext(final BlockPlaceContext bnv) {
        return bnv;
    }
    
    protected boolean updateCustomBlockEntityTag(final BlockPos fx, final Level bru, @Nullable final Player bft, final ItemStack bly, final BlockState cee) {
        return updateCustomBlockEntityTag(bru, bft, fx, bly);
    }
    
    @Nullable
    protected BlockState getPlacementState(final BlockPlaceContext bnv) {
        final BlockState cee3 = this.getBlock().getStateForPlacement(bnv);
        return (cee3 != null && this.canPlace(bnv, cee3)) ? cee3 : null;
    }
    
    private BlockState updateBlockStateFromTag(final BlockPos fx, final Level bru, final ItemStack bly, final BlockState cee) {
        BlockState cee2 = cee;
        final CompoundTag md7 = bly.getTag();
        if (md7 != null) {
            final CompoundTag md8 = md7.getCompound("BlockStateTag");
            final StateDefinition<Block, BlockState> cef9 = cee2.getBlock().getStateDefinition();
            for (final String string11 : md8.getAllKeys()) {
                final Property<?> cfg12 = cef9.getProperty(string11);
                if (cfg12 != null) {
                    final String string12 = md8.get(string11).getAsString();
                    cee2 = BlockItem.updateState(cee2, cfg12, string12);
                }
            }
        }
        if (cee2 != cee) {
            bru.setBlock(fx, cee2, 2);
        }
        return cee2;
    }
    
    private static <T extends Comparable<T>> BlockState updateState(final BlockState cee, final Property<T> cfg, final String string) {
        return (BlockState)cfg.getValue(string).map(comparable -> ((StateHolder<O, BlockState>)cee).<Comparable, Comparable>setValue((Property<Comparable>)cfg, comparable)).orElse(cee);
    }
    
    protected boolean canPlace(final BlockPlaceContext bnv, final BlockState cee) {
        final Player bft4 = bnv.getPlayer();
        final CollisionContext dcp5 = (bft4 == null) ? CollisionContext.empty() : CollisionContext.of(bft4);
        return (!this.mustSurvive() || cee.canSurvive(bnv.getLevel(), bnv.getClickedPos())) && bnv.getLevel().isUnobstructed(cee, bnv.getClickedPos(), dcp5);
    }
    
    protected boolean mustSurvive() {
        return true;
    }
    
    protected boolean placeBlock(final BlockPlaceContext bnv, final BlockState cee) {
        return bnv.getLevel().setBlock(bnv.getClickedPos(), cee, 11);
    }
    
    public static boolean updateCustomBlockEntityTag(final Level bru, @Nullable final Player bft, final BlockPos fx, final ItemStack bly) {
        final MinecraftServer minecraftServer5 = bru.getServer();
        if (minecraftServer5 == null) {
            return false;
        }
        final CompoundTag md6 = bly.getTagElement("BlockEntityTag");
        if (md6 != null) {
            final BlockEntity ccg7 = bru.getBlockEntity(fx);
            if (ccg7 != null) {
                if (!bru.isClientSide && ccg7.onlyOpCanSetNbt() && (bft == null || !bft.canUseGameMasterBlocks())) {
                    return false;
                }
                final CompoundTag md7 = ccg7.save(new CompoundTag());
                final CompoundTag md8 = md7.copy();
                md7.merge(md6);
                md7.putInt("x", fx.getX());
                md7.putInt("y", fx.getY());
                md7.putInt("z", fx.getZ());
                if (!md7.equals(md8)) {
                    ccg7.load(bru.getBlockState(fx), md7);
                    ccg7.setChanged();
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public String getDescriptionId() {
        return this.getBlock().getDescriptionId();
    }
    
    @Override
    public void fillItemCategory(final CreativeModeTab bkp, final NonNullList<ItemStack> gj) {
        if (this.allowdedIn(bkp)) {
            this.getBlock().fillItemCategory(bkp, gj);
        }
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        super.appendHoverText(bly, bru, list, bni);
        this.getBlock().appendHoverText(bly, bru, list, bni);
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public void registerBlocks(final Map<Block, Item> map, final Item blu) {
        map.put(this.getBlock(), blu);
    }
}
