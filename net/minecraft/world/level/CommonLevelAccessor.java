package net.minecraft.world.level;

import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceKey;
import java.util.Optional;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.stream.Stream;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;

public interface CommonLevelAccessor extends EntityGetter, LevelReader, LevelSimulatedRW {
    default Stream<VoxelShape> getEntityCollisions(@Nullable final Entity apx, final AABB dcf, final Predicate<Entity> predicate) {
        return super.getEntityCollisions(apx, dcf, predicate);
    }
    
    default boolean isUnobstructed(@Nullable final Entity apx, final VoxelShape dde) {
        return super.isUnobstructed(apx, dde);
    }
    
    default BlockPos getHeightmapPos(final Heightmap.Types a, final BlockPos fx) {
        return super.getHeightmapPos(a, fx);
    }
    
    RegistryAccess registryAccess();
    
    default Optional<ResourceKey<Biome>> getBiomeName(final BlockPos fx) {
        return this.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY).getResourceKey(this.getBiome(fx));
    }
}
