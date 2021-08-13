package net.minecraft.world.level.saveddata.maps;

import java.util.Collection;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.item.MapItem;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.BlockGetter;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.player.Player;
import java.util.Map;
import java.util.List;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.saveddata.SavedData;

public class MapItemSavedData extends SavedData {
    private static final Logger LOGGER;
    public int x;
    public int z;
    public ResourceKey<Level> dimension;
    public boolean trackingPosition;
    public boolean unlimitedTracking;
    public byte scale;
    public byte[] colors;
    public boolean locked;
    public final List<HoldingPlayer> carriedBy;
    private final Map<Player, HoldingPlayer> carriedByPlayers;
    private final Map<String, MapBanner> bannerMarkers;
    public final Map<String, MapDecoration> decorations;
    private final Map<String, MapFrame> frameMarkers;
    
    public MapItemSavedData(final String string) {
        super(string);
        this.colors = new byte[16384];
        this.carriedBy = (List<HoldingPlayer>)Lists.newArrayList();
        this.carriedByPlayers = (Map<Player, HoldingPlayer>)Maps.newHashMap();
        this.bannerMarkers = (Map<String, MapBanner>)Maps.newHashMap();
        this.decorations = (Map<String, MapDecoration>)Maps.newLinkedHashMap();
        this.frameMarkers = (Map<String, MapFrame>)Maps.newHashMap();
    }
    
    public void setProperties(final int integer1, final int integer2, final int integer3, final boolean boolean4, final boolean boolean5, final ResourceKey<Level> vj) {
        this.scale = (byte)integer3;
        this.setOrigin(integer1, integer2, this.scale);
        this.dimension = vj;
        this.trackingPosition = boolean4;
        this.unlimitedTracking = boolean5;
        this.setDirty();
    }
    
    public void setOrigin(final double double1, final double double2, final int integer) {
        final int integer2 = 128 * (1 << integer);
        final int integer3 = Mth.floor((double1 + 64.0) / integer2);
        final int integer4 = Mth.floor((double2 + 64.0) / integer2);
        this.x = integer3 * integer2 + integer2 / 2 - 64;
        this.z = integer4 * integer2 + integer2 / 2 - 64;
    }
    
    @Override
    public void load(final CompoundTag md) {
        this.dimension = (ResourceKey<Level>)DimensionType.parseLegacy(new Dynamic((DynamicOps)NbtOps.INSTANCE, md.get("dimension"))).resultOrPartial(MapItemSavedData.LOGGER::error).orElseThrow(() -> new IllegalArgumentException(new StringBuilder().append("Invalid map dimension: ").append(md.get("dimension")).toString()));
        this.x = md.getInt("xCenter");
        this.z = md.getInt("zCenter");
        this.scale = (byte)Mth.clamp(md.getByte("scale"), 0, 4);
        this.trackingPosition = (!md.contains("trackingPosition", 1) || md.getBoolean("trackingPosition"));
        this.unlimitedTracking = md.getBoolean("unlimitedTracking");
        this.locked = md.getBoolean("locked");
        this.colors = md.getByteArray("colors");
        if (this.colors.length != 16384) {
            this.colors = new byte[16384];
        }
        final ListTag mj3 = md.getList("banners", 10);
        for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
            final MapBanner cxq5 = MapBanner.load(mj3.getCompound(integer4));
            this.bannerMarkers.put(cxq5.getId(), cxq5);
            this.addDecoration(cxq5.getDecoration(), null, cxq5.getId(), cxq5.getPos().getX(), cxq5.getPos().getZ(), 180.0, cxq5.getName());
        }
        final ListTag mj4 = md.getList("frames", 10);
        for (int integer5 = 0; integer5 < mj4.size(); ++integer5) {
            final MapFrame cxs6 = MapFrame.load(mj4.getCompound(integer5));
            this.frameMarkers.put(cxs6.getId(), cxs6);
            this.addDecoration(MapDecoration.Type.FRAME, null, new StringBuilder().append("frame-").append(cxs6.getEntityId()).toString(), cxs6.getPos().getX(), cxs6.getPos().getZ(), cxs6.getRotation(), null);
        }
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        ResourceLocation.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, this.dimension.location()).resultOrPartial(MapItemSavedData.LOGGER::error).ifPresent(mt -> md.put("dimension", mt));
        md.putInt("xCenter", this.x);
        md.putInt("zCenter", this.z);
        md.putByte("scale", this.scale);
        md.putByteArray("colors", this.colors);
        md.putBoolean("trackingPosition", this.trackingPosition);
        md.putBoolean("unlimitedTracking", this.unlimitedTracking);
        md.putBoolean("locked", this.locked);
        final ListTag mj3 = new ListTag();
        for (final MapBanner cxq5 : this.bannerMarkers.values()) {
            mj3.add(cxq5.save());
        }
        md.put("banners", (Tag)mj3);
        final ListTag mj4 = new ListTag();
        for (final MapFrame cxs6 : this.frameMarkers.values()) {
            mj4.add(cxs6.save());
        }
        md.put("frames", (Tag)mj4);
        return md;
    }
    
    public void lockData(final MapItemSavedData cxu) {
        this.locked = true;
        this.x = cxu.x;
        this.z = cxu.z;
        this.bannerMarkers.putAll((Map)cxu.bannerMarkers);
        this.decorations.putAll((Map)cxu.decorations);
        System.arraycopy(cxu.colors, 0, this.colors, 0, cxu.colors.length);
        this.setDirty();
    }
    
    public void tickCarriedBy(final Player bft, final ItemStack bly) {
        if (!this.carriedByPlayers.containsKey(bft)) {
            final HoldingPlayer a4 = new HoldingPlayer(bft);
            this.carriedByPlayers.put(bft, a4);
            this.carriedBy.add(a4);
        }
        if (!bft.inventory.contains(bly)) {
            this.decorations.remove(bft.getName().getString());
        }
        for (int integer4 = 0; integer4 < this.carriedBy.size(); ++integer4) {
            final HoldingPlayer a5 = (HoldingPlayer)this.carriedBy.get(integer4);
            final String string6 = a5.player.getName().getString();
            if (a5.player.removed || (!a5.player.inventory.contains(bly) && !bly.isFramed())) {
                this.carriedByPlayers.remove(a5.player);
                this.carriedBy.remove(a5);
                this.decorations.remove(string6);
            }
            else if (!bly.isFramed() && a5.player.level.dimension() == this.dimension && this.trackingPosition) {
                this.addDecoration(MapDecoration.Type.PLAYER, a5.player.level, string6, a5.player.getX(), a5.player.getZ(), a5.player.yRot, null);
            }
        }
        if (bly.isFramed() && this.trackingPosition) {
            final ItemFrame bcm4 = bly.getFrame();
            final BlockPos fx5 = bcm4.getPos();
            final MapFrame cxs6 = (MapFrame)this.frameMarkers.get(MapFrame.frameId(fx5));
            if (cxs6 != null && bcm4.getId() != cxs6.getEntityId() && this.frameMarkers.containsKey(cxs6.getId())) {
                this.decorations.remove(new StringBuilder().append("frame-").append(cxs6.getEntityId()).toString());
            }
            final MapFrame cxs7 = new MapFrame(fx5, bcm4.getDirection().get2DDataValue() * 90, bcm4.getId());
            this.addDecoration(MapDecoration.Type.FRAME, bft.level, new StringBuilder().append("frame-").append(bcm4.getId()).toString(), fx5.getX(), fx5.getZ(), bcm4.getDirection().get2DDataValue() * 90, null);
            this.frameMarkers.put(cxs7.getId(), cxs7);
        }
        final CompoundTag md4 = bly.getTag();
        if (md4 != null && md4.contains("Decorations", 9)) {
            final ListTag mj5 = md4.getList("Decorations", 10);
            for (int integer5 = 0; integer5 < mj5.size(); ++integer5) {
                final CompoundTag md5 = mj5.getCompound(integer5);
                if (!this.decorations.containsKey(md5.getString("id"))) {
                    this.addDecoration(MapDecoration.Type.byIcon(md5.getByte("type")), bft.level, md5.getString("id"), md5.getDouble("x"), md5.getDouble("z"), md5.getDouble("rot"), null);
                }
            }
        }
    }
    
    public static void addTargetDecoration(final ItemStack bly, final BlockPos fx, final String string, final MapDecoration.Type a) {
        ListTag mj5;
        if (bly.hasTag() && bly.getTag().contains("Decorations", 9)) {
            mj5 = bly.getTag().getList("Decorations", 10);
        }
        else {
            mj5 = new ListTag();
            bly.addTagElement("Decorations", (Tag)mj5);
        }
        final CompoundTag md6 = new CompoundTag();
        md6.putByte("type", a.getIcon());
        md6.putString("id", string);
        md6.putDouble("x", (double)fx.getX());
        md6.putDouble("z", (double)fx.getZ());
        md6.putDouble("rot", 180.0);
        mj5.add(md6);
        if (a.hasMapColor()) {
            final CompoundTag md7 = bly.getOrCreateTagElement("display");
            md7.putInt("MapColor", a.getMapColor());
        }
    }
    
    private void addDecoration(MapDecoration.Type a, @Nullable final LevelAccessor brv, final String string, final double double4, final double double5, double double6, @Nullable final Component nr) {
        final int integer12 = 1 << this.scale;
        final float float13 = (float)(double4 - this.x) / integer12;
        final float float14 = (float)(double5 - this.z) / integer12;
        byte byte15 = (byte)(float13 * 2.0f + 0.5);
        byte byte16 = (byte)(float14 * 2.0f + 0.5);
        final int integer13 = 63;
        byte byte17;
        if (float13 >= -63.0f && float14 >= -63.0f && float13 <= 63.0f && float14 <= 63.0f) {
            double6 += ((double6 < 0.0) ? -8.0 : 8.0);
            byte17 = (byte)(double6 * 16.0 / 360.0);
            if (this.dimension == Level.NETHER && brv != null) {
                final int integer14 = (int)(brv.getLevelData().getDayTime() / 10L);
                byte17 = (byte)(integer14 * integer14 * 34187121 + integer14 * 121 >> 15 & 0xF);
            }
        }
        else {
            if (a != MapDecoration.Type.PLAYER) {
                this.decorations.remove(string);
                return;
            }
            final int integer14 = 320;
            if (Math.abs(float13) < 320.0f && Math.abs(float14) < 320.0f) {
                a = MapDecoration.Type.PLAYER_OFF_MAP;
            }
            else {
                if (!this.unlimitedTracking) {
                    this.decorations.remove(string);
                    return;
                }
                a = MapDecoration.Type.PLAYER_OFF_LIMITS;
            }
            byte17 = 0;
            if (float13 <= -63.0f) {
                byte15 = -128;
            }
            if (float14 <= -63.0f) {
                byte16 = -128;
            }
            if (float13 >= 63.0f) {
                byte15 = 127;
            }
            if (float14 >= 63.0f) {
                byte16 = 127;
            }
        }
        this.decorations.put(string, new MapDecoration(a, byte15, byte16, byte17, nr));
    }
    
    @Nullable
    public Packet<?> getUpdatePacket(final ItemStack bly, final BlockGetter bqz, final Player bft) {
        final HoldingPlayer a5 = (HoldingPlayer)this.carriedByPlayers.get(bft);
        if (a5 == null) {
            return null;
        }
        return a5.nextUpdatePacket(bly);
    }
    
    public void setDirty(final int integer1, final int integer2) {
        this.setDirty();
        for (final HoldingPlayer a5 : this.carriedBy) {
            a5.markDirty(integer1, integer2);
        }
    }
    
    public HoldingPlayer getHoldingPlayer(final Player bft) {
        HoldingPlayer a3 = (HoldingPlayer)this.carriedByPlayers.get(bft);
        if (a3 == null) {
            a3 = new HoldingPlayer(bft);
            this.carriedByPlayers.put(bft, a3);
            this.carriedBy.add(a3);
        }
        return a3;
    }
    
    public void toggleBanner(final LevelAccessor brv, final BlockPos fx) {
        final double double4 = fx.getX() + 0.5;
        final double double5 = fx.getZ() + 0.5;
        final int integer8 = 1 << this.scale;
        final double double6 = (double4 - this.x) / integer8;
        final double double7 = (double5 - this.z) / integer8;
        final int integer9 = 63;
        boolean boolean14 = false;
        if (double6 >= -63.0 && double7 >= -63.0 && double6 <= 63.0 && double7 <= 63.0) {
            final MapBanner cxq15 = MapBanner.fromWorld(brv, fx);
            if (cxq15 == null) {
                return;
            }
            boolean boolean15 = true;
            if (this.bannerMarkers.containsKey(cxq15.getId()) && ((MapBanner)this.bannerMarkers.get(cxq15.getId())).equals(cxq15)) {
                this.bannerMarkers.remove(cxq15.getId());
                this.decorations.remove(cxq15.getId());
                boolean15 = false;
                boolean14 = true;
            }
            if (boolean15) {
                this.bannerMarkers.put(cxq15.getId(), cxq15);
                this.addDecoration(cxq15.getDecoration(), brv, cxq15.getId(), double4, double5, 180.0, cxq15.getName());
                boolean14 = true;
            }
            if (boolean14) {
                this.setDirty();
            }
        }
    }
    
    public void checkBanners(final BlockGetter bqz, final int integer2, final int integer3) {
        final Iterator<MapBanner> iterator5 = (Iterator<MapBanner>)this.bannerMarkers.values().iterator();
        while (iterator5.hasNext()) {
            final MapBanner cxq6 = (MapBanner)iterator5.next();
            if (cxq6.getPos().getX() == integer2 && cxq6.getPos().getZ() == integer3) {
                final MapBanner cxq7 = MapBanner.fromWorld(bqz, cxq6.getPos());
                if (cxq6.equals(cxq7)) {
                    continue;
                }
                iterator5.remove();
                this.decorations.remove(cxq6.getId());
            }
        }
    }
    
    public void removedFromFrame(final BlockPos fx, final int integer) {
        this.decorations.remove(new StringBuilder().append("frame-").append(integer).toString());
        this.frameMarkers.remove(MapFrame.frameId(fx));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public class HoldingPlayer {
        public final Player player;
        private boolean dirtyData;
        private int minDirtyX;
        private int minDirtyY;
        private int maxDirtyX;
        private int maxDirtyY;
        private int tick;
        public int step;
        
        public HoldingPlayer(final Player bft) {
            this.dirtyData = true;
            this.maxDirtyX = 127;
            this.maxDirtyY = 127;
            this.player = bft;
        }
        
        @Nullable
        public Packet<?> nextUpdatePacket(final ItemStack bly) {
            if (this.dirtyData) {
                this.dirtyData = false;
                return new ClientboundMapItemDataPacket(MapItem.getMapId(bly), MapItemSavedData.this.scale, MapItemSavedData.this.trackingPosition, MapItemSavedData.this.locked, (Collection<MapDecoration>)MapItemSavedData.this.decorations.values(), MapItemSavedData.this.colors, this.minDirtyX, this.minDirtyY, this.maxDirtyX + 1 - this.minDirtyX, this.maxDirtyY + 1 - this.minDirtyY);
            }
            if (this.tick++ % 5 == 0) {
                return new ClientboundMapItemDataPacket(MapItem.getMapId(bly), MapItemSavedData.this.scale, MapItemSavedData.this.trackingPosition, MapItemSavedData.this.locked, (Collection<MapDecoration>)MapItemSavedData.this.decorations.values(), MapItemSavedData.this.colors, 0, 0, 0, 0);
            }
            return null;
        }
        
        public void markDirty(final int integer1, final int integer2) {
            if (this.dirtyData) {
                this.minDirtyX = Math.min(this.minDirtyX, integer1);
                this.minDirtyY = Math.min(this.minDirtyY, integer2);
                this.maxDirtyX = Math.max(this.maxDirtyX, integer1);
                this.maxDirtyY = Math.max(this.maxDirtyY, integer2);
            }
            else {
                this.dirtyData = true;
                this.minDirtyX = integer1;
                this.minDirtyY = integer2;
                this.maxDirtyX = integer1;
                this.maxDirtyY = integer2;
            }
        }
    }
}
