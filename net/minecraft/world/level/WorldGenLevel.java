package net.minecraft.world.level;

import net.minecraft.world.level.levelgen.structure.StructureStart;
import java.util.stream.Stream;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.core.SectionPos;

public interface WorldGenLevel extends ServerLevelAccessor {
    long getSeed();
    
    Stream<? extends StructureStart<?>> startsForFeature(final SectionPos gp, final StructureFeature<?> ckx);
}
