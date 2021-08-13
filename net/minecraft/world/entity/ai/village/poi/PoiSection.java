package net.minecraft.world.entity.ai.village.poi;

import org.apache.logging.log4j.LogManager;
import java.util.Collection;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.google.common.collect.Sets;
import java.util.function.BiConsumer;
import java.util.Optional;
import net.minecraft.core.SectionPos;
import org.apache.logging.log4j.util.Supplier;
import net.minecraft.core.BlockPos;
import java.util.stream.Stream;
import java.util.function.Predicate;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import java.util.List;
import com.google.common.collect.ImmutableList;
import java.util.function.Consumer;
import net.minecraft.Util;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import java.util.Set;
import java.util.Map;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import org.apache.logging.log4j.Logger;

public class PoiSection {
    private static final Logger LOGGER;
    private final Short2ObjectMap<PoiRecord> records;
    private final Map<PoiType, Set<PoiRecord>> byType;
    private final Runnable setDirty;
    private boolean isValid;
    
    public static Codec<PoiSection> codec(final Runnable runnable) {
        return (Codec<PoiSection>)RecordCodecBuilder.create(instance -> instance.group((App)RecordCodecBuilder.point(runnable), (App)Codec.BOOL.optionalFieldOf("Valid", false).forGetter(azn -> azn.isValid), (App)PoiRecord.codec(runnable).listOf().fieldOf("Records").forGetter(azn -> ImmutableList.copyOf((Collection)azn.records.values()))).apply((Applicative)instance, PoiSection::new)).orElseGet((Consumer)Util.prefix("Failed to read POI section: ", (Consumer<String>)PoiSection.LOGGER::error), () -> new PoiSection(runnable, false, (List<PoiRecord>)ImmutableList.of()));
    }
    
    public PoiSection(final Runnable runnable) {
        this(runnable, true, (List<PoiRecord>)ImmutableList.of());
    }
    
    private PoiSection(final Runnable runnable, final boolean boolean2, final List<PoiRecord> list) {
        this.records = (Short2ObjectMap<PoiRecord>)new Short2ObjectOpenHashMap();
        this.byType = (Map<PoiType, Set<PoiRecord>>)Maps.newHashMap();
        this.setDirty = runnable;
        this.isValid = boolean2;
        list.forEach(this::add);
    }
    
    public Stream<PoiRecord> getRecords(final Predicate<PoiType> predicate, final PoiManager.Occupancy b) {
        return (Stream<PoiRecord>)this.byType.entrySet().stream().filter(entry -> predicate.test(entry.getKey())).flatMap(entry -> ((Set)entry.getValue()).stream()).filter((Predicate)b.getTest());
    }
    
    public void add(final BlockPos fx, final PoiType azo) {
        if (this.add(new PoiRecord(fx, azo, this.setDirty))) {
            PoiSection.LOGGER.debug("Added POI of type {} @ {}", new Supplier[] { () -> azo, () -> fx });
            this.setDirty.run();
        }
    }
    
    private boolean add(final PoiRecord azm) {
        final BlockPos fx3 = azm.getPos();
        final PoiType azo4 = azm.getPoiType();
        final short short5 = SectionPos.sectionRelativePos(fx3);
        final PoiRecord azm2 = (PoiRecord)this.records.get(short5);
        if (azm2 == null) {
            this.records.put(short5, azm);
            ((Set)this.byType.computeIfAbsent(azo4, azo -> Sets.newHashSet())).add(azm);
            return true;
        }
        if (azo4.equals(azm2.getPoiType())) {
            return false;
        }
        throw Util.<IllegalStateException>pauseInIde(new IllegalStateException(new StringBuilder().append("POI data mismatch: already registered at ").append(fx3).toString()));
    }
    
    public void remove(final BlockPos fx) {
        final PoiRecord azm3 = (PoiRecord)this.records.remove(SectionPos.sectionRelativePos(fx));
        if (azm3 == null) {
            PoiSection.LOGGER.error(new StringBuilder().append("POI data mismatch: never registered at ").append(fx).toString());
            return;
        }
        ((Set)this.byType.get(azm3.getPoiType())).remove(azm3);
        PoiSection.LOGGER.debug("Removed POI of type {} @ {}", new Supplier[] { azm3::getPoiType, azm3::getPos });
        this.setDirty.run();
    }
    
    public boolean release(final BlockPos fx) {
        final PoiRecord azm3 = (PoiRecord)this.records.get(SectionPos.sectionRelativePos(fx));
        if (azm3 == null) {
            throw Util.<IllegalStateException>pauseInIde(new IllegalStateException(new StringBuilder().append("POI never registered at ").append(fx).toString()));
        }
        final boolean boolean4 = azm3.releaseTicket();
        this.setDirty.run();
        return boolean4;
    }
    
    public boolean exists(final BlockPos fx, final Predicate<PoiType> predicate) {
        final short short4 = SectionPos.sectionRelativePos(fx);
        final PoiRecord azm5 = (PoiRecord)this.records.get(short4);
        return azm5 != null && predicate.test(azm5.getPoiType());
    }
    
    public Optional<PoiType> getType(final BlockPos fx) {
        final short short3 = SectionPos.sectionRelativePos(fx);
        final PoiRecord azm4 = (PoiRecord)this.records.get(short3);
        return (Optional<PoiType>)((azm4 != null) ? Optional.of(azm4.getPoiType()) : Optional.empty());
    }
    
    public void refresh(final Consumer<BiConsumer<BlockPos, PoiType>> consumer) {
        if (!this.isValid) {
            final Short2ObjectMap<PoiRecord> short2ObjectMap3 = (Short2ObjectMap<PoiRecord>)new Short2ObjectOpenHashMap((Short2ObjectMap)this.records);
            this.clear();
            consumer.accept(((fx, azo) -> {
                final short short5 = SectionPos.sectionRelativePos(fx);
                final PoiRecord azm6 = (PoiRecord)short2ObjectMap3.computeIfAbsent(short5, integer -> new PoiRecord(fx, azo, this.setDirty));
                this.add(azm6);
            }));
            this.isValid = true;
            this.setDirty.run();
        }
    }
    
    private void clear() {
        this.records.clear();
        this.byType.clear();
    }
    
    boolean isValid() {
        return this.isValid;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
