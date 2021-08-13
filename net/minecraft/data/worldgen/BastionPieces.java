package net.minecraft.data.worldgen;

import java.util.function.Function;
import java.util.List;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

public class BastionPieces {
    public static final StructureTemplatePool START;
    
    public static void bootstrap() {
        BastionHousingUnitsPools.bootstrap();
        BastionHoglinStablePools.bootstrap();
        BastionTreasureRoomPools.bootstrap();
        BastionBridgePools.bootstrap();
        BastionSharedPools.bootstrap();
    }
    
    static {
        START = Pools.register(new StructureTemplatePool(new ResourceLocation("bastion/starts"), new ResourceLocation("empty"), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(Pair.of((Object)StructurePoolElement.single("bastion/units/air_base", ProcessorLists.BASTION_GENERIC_DEGRADATION), (Object)1), Pair.of((Object)StructurePoolElement.single("bastion/hoglin_stable/air_base", ProcessorLists.BASTION_GENERIC_DEGRADATION), (Object)1), Pair.of((Object)StructurePoolElement.single("bastion/treasure/big_air_full", ProcessorLists.BASTION_GENERIC_DEGRADATION), (Object)1), Pair.of((Object)StructurePoolElement.single("bastion/bridge/starting_pieces/entrance_base", ProcessorLists.BASTION_GENERIC_DEGRADATION), (Object)1)), StructureTemplatePool.Projection.RIGID));
    }
}
