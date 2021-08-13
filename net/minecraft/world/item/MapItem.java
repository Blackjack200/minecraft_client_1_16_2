package net.minecraft.world.item;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.Component;
import java.util.List;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import com.google.common.collect.LinkedHashMultiset;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceKey;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

public class MapItem extends ComplexItem {
    public MapItem(final Properties a) {
        super(a);
    }
    
    public static ItemStack create(final Level bru, final int integer2, final int integer3, final byte byte4, final boolean boolean5, final boolean boolean6) {
        final ItemStack bly7 = new ItemStack(Items.FILLED_MAP);
        createAndStoreSavedData(bly7, bru, integer2, integer3, byte4, boolean5, boolean6, bru.dimension());
        return bly7;
    }
    
    @Nullable
    public static MapItemSavedData getSavedData(final ItemStack bly, final Level bru) {
        return bru.getMapData(makeKey(getMapId(bly)));
    }
    
    @Nullable
    public static MapItemSavedData getOrCreateSavedData(final ItemStack bly, final Level bru) {
        MapItemSavedData cxu3 = getSavedData(bly, bru);
        if (cxu3 == null && bru instanceof ServerLevel) {
            cxu3 = createAndStoreSavedData(bly, bru, bru.getLevelData().getXSpawn(), bru.getLevelData().getZSpawn(), 3, false, false, bru.dimension());
        }
        return cxu3;
    }
    
    public static int getMapId(final ItemStack bly) {
        final CompoundTag md2 = bly.getTag();
        return (md2 != null && md2.contains("map", 99)) ? md2.getInt("map") : 0;
    }
    
    private static MapItemSavedData createAndStoreSavedData(final ItemStack bly, final Level bru, final int integer3, final int integer4, final int integer5, final boolean boolean6, final boolean boolean7, final ResourceKey<Level> vj) {
        final int integer6 = bru.getFreeMapId();
        final MapItemSavedData cxu10 = new MapItemSavedData(makeKey(integer6));
        cxu10.setProperties(integer3, integer4, integer5, boolean6, boolean7, vj);
        bru.setMapData(cxu10);
        bly.getOrCreateTag().putInt("map", integer6);
        return cxu10;
    }
    
    public static String makeKey(final int integer) {
        return new StringBuilder().append("map_").append(integer).toString();
    }
    
    public void update(final Level bru, final Entity apx, final MapItemSavedData cxu) {
        if (bru.dimension() != cxu.dimension || !(apx instanceof Player)) {
            return;
        }
        final int integer5 = 1 << cxu.scale;
        final int integer6 = cxu.x;
        final int integer7 = cxu.z;
        final int integer8 = Mth.floor(apx.getX() - integer6) / integer5 + 64;
        final int integer9 = Mth.floor(apx.getZ() - integer7) / integer5 + 64;
        int integer10 = 128 / integer5;
        if (bru.dimensionType().hasCeiling()) {
            integer10 /= 2;
        }
        final MapItemSavedData.HoldingPlayer holdingPlayer;
        final MapItemSavedData.HoldingPlayer a11 = holdingPlayer = cxu.getHoldingPlayer((Player)apx);
        ++holdingPlayer.step;
        boolean boolean12 = false;
        for (int integer11 = integer8 - integer10 + 1; integer11 < integer8 + integer10; ++integer11) {
            if ((integer11 & 0xF) == (a11.step & 0xF) || boolean12) {
                boolean12 = false;
                double double14 = 0.0;
                for (int integer12 = integer9 - integer10 - 1; integer12 < integer9 + integer10; ++integer12) {
                    if (integer11 >= 0 && integer12 >= -1 && integer11 < 128) {
                        if (integer12 < 128) {
                            final int integer13 = integer11 - integer8;
                            final int integer14 = integer12 - integer9;
                            final boolean boolean13 = integer13 * integer13 + integer14 * integer14 > (integer10 - 2) * (integer10 - 2);
                            final int integer15 = (integer6 / integer5 + integer11 - 64) * integer5;
                            final int integer16 = (integer7 / integer5 + integer12 - 64) * integer5;
                            final Multiset<MaterialColor> multiset22 = (Multiset<MaterialColor>)LinkedHashMultiset.create();
                            final LevelChunk cge23 = bru.getChunkAt(new BlockPos(integer15, 0, integer16));
                            if (!cge23.isEmpty()) {
                                final ChunkPos bra24 = cge23.getPos();
                                final int integer17 = integer15 & 0xF;
                                final int integer18 = integer16 & 0xF;
                                int integer19 = 0;
                                double double15 = 0.0;
                                if (bru.dimensionType().hasCeiling()) {
                                    int integer20 = integer15 + integer16 * 231871;
                                    integer20 = integer20 * integer20 * 31287121 + integer20 * 11;
                                    if ((integer20 >> 20 & 0x1) == 0x0) {
                                        multiset22.add(Blocks.DIRT.defaultBlockState().getMapColor(bru, BlockPos.ZERO), 10);
                                    }
                                    else {
                                        multiset22.add(Blocks.STONE.defaultBlockState().getMapColor(bru, BlockPos.ZERO), 100);
                                    }
                                    double15 = 100.0;
                                }
                                else {
                                    final BlockPos.MutableBlockPos a12 = new BlockPos.MutableBlockPos();
                                    final BlockPos.MutableBlockPos a13 = new BlockPos.MutableBlockPos();
                                    for (int integer21 = 0; integer21 < integer5; ++integer21) {
                                        for (int integer22 = 0; integer22 < integer5; ++integer22) {
                                            int integer23 = cge23.getHeight(Heightmap.Types.WORLD_SURFACE, integer21 + integer17, integer22 + integer18) + 1;
                                            BlockState cee35;
                                            if (integer23 > 1) {
                                                do {
                                                    --integer23;
                                                    a12.set(bra24.getMinBlockX() + integer21 + integer17, integer23, bra24.getMinBlockZ() + integer22 + integer18);
                                                    cee35 = cge23.getBlockState(a12);
                                                } while (cee35.getMapColor(bru, a12) == MaterialColor.NONE && integer23 > 0);
                                                if (integer23 > 0 && !cee35.getFluidState().isEmpty()) {
                                                    int integer24 = integer23 - 1;
                                                    a13.set(a12);
                                                    BlockState cee36;
                                                    do {
                                                        a13.setY(integer24--);
                                                        cee36 = cge23.getBlockState(a13);
                                                        ++integer19;
                                                    } while (integer24 > 0 && !cee36.getFluidState().isEmpty());
                                                    cee35 = this.getCorrectStateForFluidBlock(bru, cee35, a12);
                                                }
                                            }
                                            else {
                                                cee35 = Blocks.BEDROCK.defaultBlockState();
                                            }
                                            cxu.checkBanners(bru, bra24.getMinBlockX() + integer21 + integer17, bra24.getMinBlockZ() + integer22 + integer18);
                                            double15 += integer23 / (double)(integer5 * integer5);
                                            multiset22.add(cee35.getMapColor(bru, a12));
                                        }
                                    }
                                }
                                integer19 /= integer5 * integer5;
                                double double16 = (double15 - double14) * 4.0 / (integer5 + 4) + ((integer11 + integer12 & 0x1) - 0.5) * 0.4;
                                int integer21 = 1;
                                if (double16 > 0.6) {
                                    integer21 = 2;
                                }
                                if (double16 < -0.6) {
                                    integer21 = 0;
                                }
                                final MaterialColor cuy33 = (MaterialColor)Iterables.getFirst((Iterable)Multisets.copyHighestCountFirst((Multiset)multiset22), MaterialColor.NONE);
                                if (cuy33 == MaterialColor.WATER) {
                                    double16 = integer19 * 0.1 + (integer11 + integer12 & 0x1) * 0.2;
                                    integer21 = 1;
                                    if (double16 < 0.5) {
                                        integer21 = 2;
                                    }
                                    if (double16 > 0.9) {
                                        integer21 = 0;
                                    }
                                }
                                double14 = double15;
                                if (integer12 >= 0) {
                                    if (integer13 * integer13 + integer14 * integer14 < integer10 * integer10) {
                                        if (!boolean13 || (integer11 + integer12 & 0x1) != 0x0) {
                                            final byte byte34 = cxu.colors[integer11 + integer12 * 128];
                                            final byte byte35 = (byte)(cuy33.id * 4 + integer21);
                                            if (byte34 != byte35) {
                                                cxu.colors[integer11 + integer12 * 128] = byte35;
                                                cxu.setDirty(integer11, integer12);
                                                boolean12 = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private BlockState getCorrectStateForFluidBlock(final Level bru, final BlockState cee, final BlockPos fx) {
        final FluidState cuu5 = cee.getFluidState();
        if (!cuu5.isEmpty() && !cee.isFaceSturdy(bru, fx, Direction.UP)) {
            return cuu5.createLegacyBlock();
        }
        return cee;
    }
    
    private static boolean isLand(final Biome[] arr, final int integer2, final int integer3, final int integer4) {
        return arr[integer3 * integer2 + integer4 * integer2 * 128 * integer2].getDepth() >= 0.0f;
    }
    
    public static void renderBiomePreviewMap(final ServerLevel aag, final ItemStack bly) {
        final MapItemSavedData cxu3 = getOrCreateSavedData(bly, aag);
        if (cxu3 == null) {
            return;
        }
        if (aag.dimension() != cxu3.dimension) {
            return;
        }
        final int integer4 = 1 << cxu3.scale;
        final int integer5 = cxu3.x;
        final int integer6 = cxu3.z;
        final Biome[] arr7 = new Biome[128 * integer4 * 128 * integer4];
        for (int integer7 = 0; integer7 < 128 * integer4; ++integer7) {
            for (int integer8 = 0; integer8 < 128 * integer4; ++integer8) {
                arr7[integer7 * 128 * integer4 + integer8] = aag.getBiome(new BlockPos((integer5 / integer4 - 64) * integer4 + integer8, 0, (integer6 / integer4 - 64) * integer4 + integer7));
            }
        }
        for (int integer7 = 0; integer7 < 128; ++integer7) {
            for (int integer8 = 0; integer8 < 128; ++integer8) {
                if (integer7 > 0 && integer8 > 0 && integer7 < 127 && integer8 < 127) {
                    final Biome bss10 = arr7[integer7 * integer4 + integer8 * integer4 * 128 * integer4];
                    int integer9 = 8;
                    if (isLand(arr7, integer4, integer7 - 1, integer8 - 1)) {
                        --integer9;
                    }
                    if (isLand(arr7, integer4, integer7 - 1, integer8 + 1)) {
                        --integer9;
                    }
                    if (isLand(arr7, integer4, integer7 - 1, integer8)) {
                        --integer9;
                    }
                    if (isLand(arr7, integer4, integer7 + 1, integer8 - 1)) {
                        --integer9;
                    }
                    if (isLand(arr7, integer4, integer7 + 1, integer8 + 1)) {
                        --integer9;
                    }
                    if (isLand(arr7, integer4, integer7 + 1, integer8)) {
                        --integer9;
                    }
                    if (isLand(arr7, integer4, integer7, integer8 - 1)) {
                        --integer9;
                    }
                    if (isLand(arr7, integer4, integer7, integer8 + 1)) {
                        --integer9;
                    }
                    int integer10 = 3;
                    MaterialColor cuy13 = MaterialColor.NONE;
                    if (bss10.getDepth() < 0.0f) {
                        cuy13 = MaterialColor.COLOR_ORANGE;
                        if (integer9 > 7 && integer8 % 2 == 0) {
                            integer10 = (integer7 + (int)(Mth.sin(integer8 + 0.0f) * 7.0f)) / 8 % 5;
                            if (integer10 == 3) {
                                integer10 = 1;
                            }
                            else if (integer10 == 4) {
                                integer10 = 0;
                            }
                        }
                        else if (integer9 > 7) {
                            cuy13 = MaterialColor.NONE;
                        }
                        else if (integer9 > 5) {
                            integer10 = 1;
                        }
                        else if (integer9 > 3) {
                            integer10 = 0;
                        }
                        else if (integer9 > 1) {
                            integer10 = 0;
                        }
                    }
                    else if (integer9 > 0) {
                        cuy13 = MaterialColor.COLOR_BROWN;
                        if (integer9 > 3) {
                            integer10 = 1;
                        }
                        else {
                            integer10 = 3;
                        }
                    }
                    if (cuy13 != MaterialColor.NONE) {
                        cxu3.colors[integer7 + integer8 * 128] = (byte)(cuy13.id * 4 + integer10);
                        cxu3.setDirty(integer7, integer8);
                    }
                }
            }
        }
    }
    
    @Override
    public void inventoryTick(final ItemStack bly, final Level bru, final Entity apx, final int integer, final boolean boolean5) {
        if (bru.isClientSide) {
            return;
        }
        final MapItemSavedData cxu7 = getOrCreateSavedData(bly, bru);
        if (cxu7 == null) {
            return;
        }
        if (apx instanceof Player) {
            final Player bft8 = (Player)apx;
            cxu7.tickCarriedBy(bft8, bly);
        }
        if (!cxu7.locked && (boolean5 || (apx instanceof Player && ((Player)apx).getOffhandItem() == bly))) {
            this.update(bru, apx, cxu7);
        }
    }
    
    @Nullable
    @Override
    public Packet<?> getUpdatePacket(final ItemStack bly, final Level bru, final Player bft) {
        return getOrCreateSavedData(bly, bru).getUpdatePacket(bly, bru, bft);
    }
    
    @Override
    public void onCraftedBy(final ItemStack bly, final Level bru, final Player bft) {
        final CompoundTag md5 = bly.getTag();
        if (md5 != null && md5.contains("map_scale_direction", 99)) {
            scaleMap(bly, bru, md5.getInt("map_scale_direction"));
            md5.remove("map_scale_direction");
        }
        else if (md5 != null && md5.contains("map_to_lock", 1) && md5.getBoolean("map_to_lock")) {
            lockMap(bru, bly);
            md5.remove("map_to_lock");
        }
    }
    
    protected static void scaleMap(final ItemStack bly, final Level bru, final int integer) {
        final MapItemSavedData cxu4 = getOrCreateSavedData(bly, bru);
        if (cxu4 != null) {
            createAndStoreSavedData(bly, bru, cxu4.x, cxu4.z, Mth.clamp(cxu4.scale + integer, 0, 4), cxu4.trackingPosition, cxu4.unlimitedTracking, cxu4.dimension);
        }
    }
    
    public static void lockMap(final Level bru, final ItemStack bly) {
        final MapItemSavedData cxu3 = getOrCreateSavedData(bly, bru);
        if (cxu3 != null) {
            final MapItemSavedData cxu4 = createAndStoreSavedData(bly, bru, 0, 0, cxu3.scale, cxu3.trackingPosition, cxu3.unlimitedTracking, cxu3.dimension);
            cxu4.lockData(cxu3);
        }
    }
    
    @Override
    public void appendHoverText(final ItemStack bly, @Nullable final Level bru, final List<Component> list, final TooltipFlag bni) {
        final MapItemSavedData cxu6 = (bru == null) ? null : getOrCreateSavedData(bly, bru);
        if (cxu6 != null && cxu6.locked) {
            list.add(new TranslatableComponent("filled_map.locked", new Object[] { getMapId(bly) }).withStyle(ChatFormatting.GRAY));
        }
        if (bni.isAdvanced()) {
            if (cxu6 != null) {
                list.add(new TranslatableComponent("filled_map.id", new Object[] { getMapId(bly) }).withStyle(ChatFormatting.GRAY));
                list.add(new TranslatableComponent("filled_map.scale", new Object[] { 1 << cxu6.scale }).withStyle(ChatFormatting.GRAY));
                list.add(new TranslatableComponent("filled_map.level", new Object[] { cxu6.scale, 4 }).withStyle(ChatFormatting.GRAY));
            }
            else {
                list.add(new TranslatableComponent("filled_map.unknown").withStyle(ChatFormatting.GRAY));
            }
        }
    }
    
    public static int getColor(final ItemStack bly) {
        final CompoundTag md2 = bly.getTagElement("display");
        if (md2 != null && md2.contains("MapColor", 99)) {
            final int integer3 = md2.getInt("MapColor");
            return 0xFF000000 | (integer3 & 0xFFFFFF);
        }
        return -12173266;
    }
    
    @Override
    public InteractionResult useOn(final UseOnContext bnx) {
        final BlockState cee3 = bnx.getLevel().getBlockState(bnx.getClickedPos());
        if (cee3.is(BlockTags.BANNERS)) {
            if (!bnx.getLevel().isClientSide) {
                final MapItemSavedData cxu4 = getOrCreateSavedData(bnx.getItemInHand(), bnx.getLevel());
                cxu4.toggleBanner(bnx.getLevel(), bnx.getClickedPos());
            }
            return InteractionResult.sidedSuccess(bnx.getLevel().isClientSide);
        }
        return super.useOn(bnx);
    }
}
