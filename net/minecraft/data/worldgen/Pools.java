package net.minecraft.data.worldgen;

import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.resources.ResourceKey;

public class Pools {
    public static final ResourceKey<StructureTemplatePool> EMPTY;
    private static final StructureTemplatePool BUILTIN_EMPTY;
    
    public static StructureTemplatePool register(final StructureTemplatePool coh) {
        return BuiltinRegistries.<StructureTemplatePool, StructureTemplatePool>register(BuiltinRegistries.TEMPLATE_POOL, coh.getName(), coh);
    }
    
    public static StructureTemplatePool bootstrap() {
        BastionPieces.bootstrap();
        PillagerOutpostPools.bootstrap();
        VillagePools.bootstrap();
        return Pools.BUILTIN_EMPTY;
    }
    
    static {
        EMPTY = ResourceKey.<StructureTemplatePool>create(Registry.TEMPLATE_POOL_REGISTRY, new ResourceLocation("empty"));
        BUILTIN_EMPTY = register(new StructureTemplatePool(Pools.EMPTY.location(), Pools.EMPTY.location(), (List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>>)ImmutableList.of(), StructureTemplatePool.Projection.RIGID));
        bootstrap();
    }
}
