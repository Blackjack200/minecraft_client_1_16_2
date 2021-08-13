package net.minecraft.world.entity.ai.village.poi;

import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import net.minecraft.server.level.SectionTracker;
import net.minecraft.core.Vec3i;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.LevelReader;
import java.util.function.BiConsumer;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.world.level.chunk.LevelChunkSection;
import java.util.function.BooleanSupplier;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Random;
import java.util.Comparator;
import java.util.stream.IntStream;
import net.minecraft.world.level.ChunkPos;
import java.util.stream.Stream;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.SectionPos;
import net.minecraft.core.BlockPos;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.util.datafix.DataFixTypes;
import com.mojang.datafixers.DataFixer;
import java.io.File;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.world.level.chunk.storage.SectionStorage;

public class PoiManager extends SectionStorage<PoiSection> {
    private final DistanceTracker distanceTracker;
    private final LongSet loadedChunks;
    
    public PoiManager(final File file, final DataFixer dataFixer, final boolean boolean3) {
        super(file, PoiSection::codec, PoiSection::new, dataFixer, DataFixTypes.POI_CHUNK, boolean3);
        this.loadedChunks = (LongSet)new LongOpenHashSet();
        this.distanceTracker = new DistanceTracker();
    }
    
    public void add(final BlockPos fx, final PoiType azo) {
        this.getOrCreate(SectionPos.of(fx).asLong()).add(fx, azo);
    }
    
    public void remove(final BlockPos fx) {
        this.getOrCreate(SectionPos.of(fx).asLong()).remove(fx);
    }
    
    public long getCountInRange(final Predicate<PoiType> predicate, final BlockPos fx, final int integer, final Occupancy b) {
        return this.getInRange(predicate, fx, integer, b).count();
    }
    
    public boolean existsAtPosition(final PoiType azo, final BlockPos fx) {
        final Optional<PoiType> optional4 = this.getOrCreate(SectionPos.of(fx).asLong()).getType(fx);
        return optional4.isPresent() && ((PoiType)optional4.get()).equals(azo);
    }
    
    public Stream<PoiRecord> getInSquare(final Predicate<PoiType> predicate, final BlockPos fx, final int integer, final Occupancy b) {
        final int integer2 = Math.floorDiv(integer, 16) + 1;
        return (Stream<PoiRecord>)ChunkPos.rangeClosed(new ChunkPos(fx), integer2).flatMap(bra -> this.getInChunk(predicate, bra, b)).filter(azm -> {
            final BlockPos fx2 = azm.getPos();
            return Math.abs(fx2.getX() - fx.getX()) <= integer && Math.abs(fx2.getZ() - fx.getZ()) <= integer;
        });
    }
    
    public Stream<PoiRecord> getInRange(final Predicate<PoiType> predicate, final BlockPos fx, final int integer, final Occupancy b) {
        final int integer2 = integer * integer;
        return (Stream<PoiRecord>)this.getInSquare(predicate, fx, integer, b).filter(azm -> azm.getPos().distSqr(fx) <= integer2);
    }
    
    public Stream<PoiRecord> getInChunk(final Predicate<PoiType> predicate, final ChunkPos bra, final Occupancy b) {
        return (Stream<PoiRecord>)IntStream.range(0, 16).boxed().map(integer -> this.getOrLoad(SectionPos.of(bra, integer).asLong())).filter(Optional::isPresent).flatMap(optional -> ((PoiSection)optional.get()).getRecords(predicate, b));
    }
    
    public Stream<BlockPos> findAll(final Predicate<PoiType> predicate1, final Predicate<BlockPos> predicate2, final BlockPos fx, final int integer, final Occupancy b) {
        return (Stream<BlockPos>)this.getInRange(predicate1, fx, integer, b).map(PoiRecord::getPos).filter((Predicate)predicate2);
    }
    
    public Stream<BlockPos> findAllClosestFirst(final Predicate<PoiType> predicate1, final Predicate<BlockPos> predicate2, final BlockPos fx, final int integer, final Occupancy b) {
        return (Stream<BlockPos>)this.findAll(predicate1, predicate2, fx, integer, b).sorted(Comparator.comparingDouble(fx2 -> fx2.distSqr(fx)));
    }
    
    public Optional<BlockPos> find(final Predicate<PoiType> predicate1, final Predicate<BlockPos> predicate2, final BlockPos fx, final int integer, final Occupancy b) {
        return (Optional<BlockPos>)this.findAll(predicate1, predicate2, fx, integer, b).findFirst();
    }
    
    public Optional<BlockPos> findClosest(final Predicate<PoiType> predicate, final BlockPos fx, final int integer, final Occupancy b) {
        return (Optional<BlockPos>)this.getInRange(predicate, fx, integer, b).map(PoiRecord::getPos).min(Comparator.comparingDouble(fx2 -> fx2.distSqr(fx)));
    }
    
    public Optional<BlockPos> take(final Predicate<PoiType> predicate1, final Predicate<BlockPos> predicate2, final BlockPos fx, final int integer) {
        return (Optional<BlockPos>)this.getInRange(predicate1, fx, integer, Occupancy.HAS_SPACE).filter(azm -> predicate2.test(azm.getPos())).findFirst().map(azm -> {
            azm.acquireTicket();
            return azm.getPos();
        });
    }
    
    public Optional<BlockPos> getRandom(final Predicate<PoiType> predicate1, final Predicate<BlockPos> predicate2, final Occupancy b, final BlockPos fx, final int integer, final Random random) {
        final List<PoiRecord> list8 = (List<PoiRecord>)this.getInRange(predicate1, fx, integer, b).collect(Collectors.toList());
        Collections.shuffle((List)list8, random);
        return (Optional<BlockPos>)list8.stream().filter(azm -> predicate2.test(azm.getPos())).findFirst().map(PoiRecord::getPos);
    }
    
    public boolean release(final BlockPos fx) {
        return this.getOrCreate(SectionPos.of(fx).asLong()).release(fx);
    }
    
    public boolean exists(final BlockPos fx, final Predicate<PoiType> predicate) {
        return (boolean)this.getOrLoad(SectionPos.of(fx).asLong()).map(azn -> azn.exists(fx, predicate)).orElse(false);
    }
    
    public Optional<PoiType> getType(final BlockPos fx) {
        final PoiSection azn3 = this.getOrCreate(SectionPos.of(fx).asLong());
        return azn3.getType(fx);
    }
    
    public int sectionsToVillage(final SectionPos gp) {
        this.distanceTracker.runAllUpdates();
        return this.distanceTracker.getLevel(gp.asLong());
    }
    
    private boolean isVillageCenter(final long long1) {
        final Optional<PoiSection> optional4 = this.get(long1);
        return optional4 != null && (boolean)optional4.map(azn -> azn.getRecords(PoiType.ALL, Occupancy.IS_OCCUPIED).count() > 0L).orElse(false);
    }
    
    public void tick(final BooleanSupplier booleanSupplier) {
        super.tick(booleanSupplier);
        this.distanceTracker.runAllUpdates();
    }
    
    @Override
    protected void setDirty(final long long1) {
        super.setDirty(long1);
        this.distanceTracker.update(long1, this.distanceTracker.getLevelFromSource(long1), false);
    }
    
    @Override
    protected void onSectionLoad(final long long1) {
        this.distanceTracker.update(long1, this.distanceTracker.getLevelFromSource(long1), false);
    }
    
    public void checkConsistencyWithBlocks(final ChunkPos bra, final LevelChunkSection cgf) {
        final SectionPos gp4 = SectionPos.of(bra, cgf.bottomBlockY() >> 4);
        Util.<PoiSection>ifElse(this.getOrLoad(gp4.asLong()), (java.util.function.Consumer<PoiSection>)(azn -> azn.refresh((Consumer<BiConsumer<BlockPos, PoiType>>)(biConsumer -> {
            if (mayHavePoi(cgf)) {
                this.updateFromSection(cgf, gp4, (BiConsumer<BlockPos, PoiType>)biConsumer);
            }
        }))), () -> {
            if (mayHavePoi(cgf)) {
                final PoiSection azn4 = this.getOrCreate(gp4.asLong());
                this.updateFromSection(cgf, gp4, (BiConsumer<BlockPos, PoiType>)azn4::add);
            }
        });
    }
    
    private static boolean mayHavePoi(final LevelChunkSection cgf) {
        return cgf.maybeHas((Predicate<BlockState>)PoiType.ALL_STATES::contains);
    }
    
    private void updateFromSection(final LevelChunkSection cgf, final SectionPos gp, final BiConsumer<BlockPos, PoiType> biConsumer) {
        gp.blocksInside().forEach(fx -> {
            final BlockState cee4 = cgf.getBlockState(SectionPos.sectionRelative(fx.getX()), SectionPos.sectionRelative(fx.getY()), SectionPos.sectionRelative(fx.getZ()));
            PoiType.forState(cee4).ifPresent(azo -> biConsumer.accept(fx, azo));
        });
    }
    
    public void ensureLoadedAndValid(final LevelReader brw, final BlockPos fx, final int integer) {
        SectionPos.aroundChunk(new ChunkPos(fx), Math.floorDiv(integer, 16)).map(gp -> Pair.of(gp, this.getOrLoad(gp.asLong()))).filter(pair -> !(boolean)((Optional)pair.getSecond()).map(PoiSection::isValid).orElse(false)).map(pair -> ((SectionPos)pair.getFirst()).chunk()).filter(bra -> this.loadedChunks.add(bra.toLong())).forEach(bra -> brw.getChunk(bra.x, bra.z, ChunkStatus.EMPTY));
    }
    
    public enum Occupancy {
        HAS_SPACE(PoiRecord::hasSpace), 
        IS_OCCUPIED(PoiRecord::isOccupied), 
        ANY((azm -> true));
        
        private final Predicate<? super PoiRecord> test;
        
        private Occupancy(final Predicate<? super PoiRecord> predicate) {
            this.test = predicate;
        }
        
        public Predicate<? super PoiRecord> getTest() {
            return this.test;
        }
    }
    
    final class DistanceTracker extends SectionTracker {
        private final Long2ByteMap levels;
        
        protected DistanceTracker() {
            super(7, 16, 256);
            (this.levels = (Long2ByteMap)new Long2ByteOpenHashMap()).defaultReturnValue((byte)7);
        }
        
        @Override
        protected int getLevelFromSource(final long long1) {
            return PoiManager.this.isVillageCenter(long1) ? 0 : 7;
        }
        
        @Override
        protected int getLevel(final long long1) {
            return this.levels.get(long1);
        }
        
        @Override
        protected void setLevel(final long long1, final int integer) {
            if (integer > 6) {
                this.levels.remove(long1);
            }
            else {
                this.levels.put(long1, (byte)integer);
            }
        }
        
        public void runAllUpdates() {
            super.runUpdates(Integer.MAX_VALUE);
        }
    }
}
