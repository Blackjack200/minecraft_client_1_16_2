package net.minecraft.world.item;

import org.apache.logging.log4j.LogManager;
import net.minecraft.nbt.Tag;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import org.apache.logging.log4j.Logger;

public class CompassItem extends Item implements Vanishable {
    private static final Logger LOGGER;
    
    public CompassItem(final Properties a) {
        super(a);
    }
    
    public static boolean isLodestoneCompass(final ItemStack bly) {
        final CompoundTag md2 = bly.getTag();
        return md2 != null && (md2.contains("LodestoneDimension") || md2.contains("LodestonePos"));
    }
    
    @Override
    public boolean isFoil(final ItemStack bly) {
        return isLodestoneCompass(bly) || super.isFoil(bly);
    }
    
    public static Optional<ResourceKey<Level>> getLodestoneDimension(final CompoundTag md) {
        return (Optional<ResourceKey<Level>>)Level.RESOURCE_KEY_CODEC.parse((DynamicOps)NbtOps.INSTANCE, md.get("LodestoneDimension")).result();
    }
    
    @Override
    public void inventoryTick(final ItemStack bly, final Level bru, final Entity apx, final int integer, final boolean boolean5) {
        if (bru.isClientSide) {
            return;
        }
        if (isLodestoneCompass(bly)) {
            final CompoundTag md7 = bly.getOrCreateTag();
            if (md7.contains("LodestoneTracked") && !md7.getBoolean("LodestoneTracked")) {
                return;
            }
            final Optional<ResourceKey<Level>> optional8 = getLodestoneDimension(md7);
            if (optional8.isPresent() && optional8.get() == bru.dimension() && md7.contains("LodestonePos") && !((ServerLevel)bru).getPoiManager().existsAtPosition(PoiType.LODESTONE, NbtUtils.readBlockPos(md7.getCompound("LodestonePos")))) {
                md7.remove("LodestonePos");
            }
        }
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final BlockPos fx3 = bnx.getClickedPos();
        final Level bru4 = bnx.getLevel();
        if (bru4.getBlockState(fx3).is(Blocks.LODESTONE)) {
            bru4.playSound(null, fx3, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0f, 1.0f);
            final Player bft5 = bnx.getPlayer();
            final ItemStack bly6 = bnx.getItemInHand();
            final boolean boolean7 = !bft5.abilities.instabuild && bly6.getCount() == 1;
            if (boolean7) {
                this.addLodestoneTags(bru4.dimension(), fx3, bly6.getOrCreateTag());
            }
            else {
                final ItemStack bly7 = new ItemStack(Items.COMPASS, 1);
                final CompoundTag md9 = bly6.hasTag() ? bly6.getTag().copy() : new CompoundTag();
                bly7.setTag(md9);
                if (!bft5.abilities.instabuild) {
                    bly6.shrink(1);
                }
                this.addLodestoneTags(bru4.dimension(), fx3, md9);
                if (!bft5.inventory.add(bly7)) {
                    bft5.drop(bly7, false);
                }
            }
            return InteractionResult.sidedSuccess(bru4.isClientSide);
        }
        return super.useOn(bnx);
    }
    
    private void addLodestoneTags(final ResourceKey<Level> vj, final BlockPos fx, final CompoundTag md) {
        md.put("LodestonePos", (Tag)NbtUtils.writeBlockPos(fx));
        Level.RESOURCE_KEY_CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, vj).resultOrPartial(CompassItem.LOGGER::error).ifPresent(mt -> md.put("LodestoneDimension", mt));
        md.putBoolean("LodestoneTracked", true);
    }
    
    @Override
    public String getDescriptionId(final ItemStack bly) {
        return isLodestoneCompass(bly) ? "item.minecraft.lodestone_compass" : super.getDescriptionId(bly);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
