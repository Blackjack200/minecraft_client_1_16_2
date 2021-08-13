package net.minecraft.world.level.chunk;

import java.util.Map;
import it.unimi.dsi.fastutil.longs.LongSet;
import javax.annotation.Nullable;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

public interface FeatureAccess {
    @Nullable
    StructureStart<?> getStartForFeature(final StructureFeature<?> ckx);
    
    void setStartForFeature(final StructureFeature<?> ckx, final StructureStart<?> crs);
    
    LongSet getReferencesForFeature(final StructureFeature<?> ckx);
    
    void addReferenceForFeature(final StructureFeature<?> ckx, final long long2);
    
    Map<StructureFeature<?>, LongSet> getAllReferences();
    
    void setAllReferences(final Map<StructureFeature<?>, LongSet> map);
}
