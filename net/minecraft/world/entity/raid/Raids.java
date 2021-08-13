package net.minecraft.world.entity.raid;

import net.minecraft.core.Vec3i;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nullable;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.phys.Vec3;
import java.util.stream.Collectors;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.level.GameRules;
import com.google.common.collect.Maps;
import net.minecraft.server.level.ServerLevel;
import java.util.Map;
import net.minecraft.world.level.saveddata.SavedData;

public class Raids extends SavedData {
    private final Map<Integer, Raid> raidMap;
    private final ServerLevel level;
    private int nextAvailableID;
    private int tick;
    
    public Raids(final ServerLevel aag) {
        super(getFileId(aag.dimensionType()));
        this.raidMap = (Map<Integer, Raid>)Maps.newHashMap();
        this.level = aag;
        this.nextAvailableID = 1;
        this.setDirty();
    }
    
    public Raid get(final int integer) {
        return (Raid)this.raidMap.get(integer);
    }
    
    public void tick() {
        ++this.tick;
        final Iterator<Raid> iterator2 = (Iterator<Raid>)this.raidMap.values().iterator();
        while (iterator2.hasNext()) {
            final Raid bgy3 = (Raid)iterator2.next();
            if (this.level.getGameRules().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
                bgy3.stop();
            }
            if (bgy3.isStopped()) {
                iterator2.remove();
                this.setDirty();
            }
            else {
                bgy3.tick();
            }
        }
        if (this.tick % 200 == 0) {
            this.setDirty();
        }
        DebugPackets.sendRaids(this.level, (Collection<Raid>)this.raidMap.values());
    }
    
    public static boolean canJoinRaid(final Raider bgz, final Raid bgy) {
        return bgz != null && bgy != null && bgy.getLevel() != null && bgz.isAlive() && bgz.canJoinRaid() && bgz.getNoActionTime() <= 2400 && bgz.level.dimensionType() == bgy.getLevel().dimensionType();
    }
    
    @Nullable
    public Raid createOrExtendRaid(final ServerPlayer aah) {
        if (aah.isSpectator()) {
            return null;
        }
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DISABLE_RAIDS)) {
            return null;
        }
        final DimensionType cha3 = aah.level.dimensionType();
        if (!cha3.hasRaids()) {
            return null;
        }
        final BlockPos fx4 = aah.blockPosition();
        final List<PoiRecord> list6 = (List<PoiRecord>)this.level.getPoiManager().getInRange(PoiType.ALL, fx4, 64, PoiManager.Occupancy.IS_OCCUPIED).collect(Collectors.toList());
        int integer7 = 0;
        Vec3 dck8 = Vec3.ZERO;
        for (final PoiRecord azm10 : list6) {
            final BlockPos fx5 = azm10.getPos();
            dck8 = dck8.add(fx5.getX(), fx5.getY(), fx5.getZ());
            ++integer7;
        }
        BlockPos fx6;
        if (integer7 > 0) {
            dck8 = dck8.scale(1.0 / integer7);
            fx6 = new BlockPos(dck8);
        }
        else {
            fx6 = fx4;
        }
        final Raid bgy9 = this.getOrCreateRaid(aah.getLevel(), fx6);
        boolean boolean10 = false;
        if (!bgy9.isStarted()) {
            if (!this.raidMap.containsKey(bgy9.getId())) {
                this.raidMap.put(bgy9.getId(), bgy9);
            }
            boolean10 = true;
        }
        else if (bgy9.getBadOmenLevel() < bgy9.getMaxBadOmenLevel()) {
            boolean10 = true;
        }
        else {
            aah.removeEffect(MobEffects.BAD_OMEN);
            aah.connection.send(new ClientboundEntityEventPacket(aah, (byte)43));
        }
        if (boolean10) {
            bgy9.absorbBadOmen(aah);
            aah.connection.send(new ClientboundEntityEventPacket(aah, (byte)43));
            if (!bgy9.hasFirstWaveSpawned()) {
                aah.awardStat(Stats.RAID_TRIGGER);
                CriteriaTriggers.BAD_OMEN.trigger(aah);
            }
        }
        this.setDirty();
        return bgy9;
    }
    
    private Raid getOrCreateRaid(final ServerLevel aag, final BlockPos fx) {
        final Raid bgy4 = aag.getRaidAt(fx);
        return (bgy4 != null) ? bgy4 : new Raid(this.getUniqueId(), aag, fx);
    }
    
    @Override
    public void load(final CompoundTag md) {
        this.nextAvailableID = md.getInt("NextAvailableID");
        this.tick = md.getInt("Tick");
        final ListTag mj3 = md.getList("Raids", 10);
        for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
            final CompoundTag md2 = mj3.getCompound(integer4);
            final Raid bgy6 = new Raid(this.level, md2);
            this.raidMap.put(bgy6.getId(), bgy6);
        }
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        md.putInt("NextAvailableID", this.nextAvailableID);
        md.putInt("Tick", this.tick);
        final ListTag mj3 = new ListTag();
        for (final Raid bgy5 : this.raidMap.values()) {
            final CompoundTag md2 = new CompoundTag();
            bgy5.save(md2);
            mj3.add(md2);
        }
        md.put("Raids", (Tag)mj3);
        return md;
    }
    
    public static String getFileId(final DimensionType cha) {
        return "raids" + cha.getFileSuffix();
    }
    
    private int getUniqueId() {
        return ++this.nextAvailableID;
    }
    
    @Nullable
    public Raid getNearbyRaid(final BlockPos fx, final int integer) {
        Raid bgy4 = null;
        double double5 = integer;
        for (final Raid bgy5 : this.raidMap.values()) {
            final double double6 = bgy5.getCenter().distSqr(fx);
            if (!bgy5.isActive()) {
                continue;
            }
            if (double6 >= double5) {
                continue;
            }
            bgy4 = bgy5;
            double5 = double6;
        }
        return bgy4;
    }
}
