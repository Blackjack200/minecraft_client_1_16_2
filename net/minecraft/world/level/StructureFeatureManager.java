package net.minecraft.world.level;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import com.mojang.datafixers.DataFixUtils;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.world.level.chunk.FeatureAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import java.util.stream.Stream;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.levelgen.WorldGenSettings;

public class StructureFeatureManager {
    private final LevelAccessor level;
    private final WorldGenSettings worldGenSettings;
    
    public StructureFeatureManager(final LevelAccessor brv, final WorldGenSettings cht) {
        this.level = brv;
        this.worldGenSettings = cht;
    }
    
    public StructureFeatureManager forWorldGenRegion(final WorldGenRegion aam) {
        if (aam.getLevel() != this.level) {
            throw new IllegalStateException(new StringBuilder().append("Using invalid feature manager (source level: ").append(aam.getLevel()).append(", region: ").append(aam).toString());
        }
        return new StructureFeatureManager(aam, this.worldGenSettings);
    }
    
    public Stream<? extends StructureStart<?>> startsForFeature(final SectionPos gp, final StructureFeature<?> ckx) {
        return this.level.getChunk(gp.x(), gp.z(), ChunkStatus.STRUCTURE_REFERENCES).getReferencesForFeature(ckx).stream().map(long1 -> SectionPos.of(new ChunkPos(long1), 0)).map(gp -> this.getStartForFeature(gp, ckx, this.level.getChunk(gp.x(), gp.z(), ChunkStatus.STRUCTURE_STARTS))).filter(crs -> crs != null && crs.isValid());
    }
    
    @Nullable
    public StructureStart<?> getStartForFeature(final SectionPos gp, final StructureFeature<?> ckx, final FeatureAccess cga) {
        return cga.getStartForFeature(ckx);
    }
    
    public void setStartForFeature(final SectionPos gp, final StructureFeature<?> ckx, final StructureStart<?> crs, final FeatureAccess cga) {
        cga.setStartForFeature(ckx, crs);
    }
    
    public void addReferenceForFeature(final SectionPos gp, final StructureFeature<?> ckx, final long long3, final FeatureAccess cga) {
        cga.addReferenceForFeature(ckx, long3);
    }
    
    public boolean shouldGenerateFeatures() {
        return this.worldGenSettings.generateFeatures();
    }
    
    public StructureStart<?> getStructureAt(final BlockPos fx, final boolean boolean2, final StructureFeature<?> ckx) {
        return DataFixUtils.orElse(this.startsForFeature(SectionPos.of(fx), ckx).filter(crs -> crs.getBoundingBox().isInside(fx)).filter(crs -> !boolean2 || crs.getPieces().stream().anyMatch(crr -> crr.getBoundingBox().isInside(fx))).findFirst(), StructureStart.INVALID_START);
    }
}
