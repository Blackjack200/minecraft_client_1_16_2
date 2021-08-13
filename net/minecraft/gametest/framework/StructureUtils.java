package net.minecraft.gametest.framework;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.state.properties.Property;
import java.util.Set;
import net.minecraft.commands.arguments.blocks.BlockInput;
import java.util.Collections;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import java.io.BufferedReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.io.IOException;
import net.minecraft.nbt.TagParser;
import java.io.Reader;
import org.apache.commons.io.IOUtils;
import java.nio.file.Files;
import net.minecraft.nbt.CompoundTag;
import java.nio.file.Path;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import java.nio.file.Paths;
import net.minecraft.world.level.block.state.BlockState;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.level.block.Rotation;

public class StructureUtils {
    public static String testStructuresDir;
    
    public static Rotation getRotationForRotationSteps(final int integer) {
        switch (integer) {
            case 0: {
                return Rotation.NONE;
            }
            case 1: {
                return Rotation.CLOCKWISE_90;
            }
            case 2: {
                return Rotation.CLOCKWISE_180;
            }
            case 3: {
                return Rotation.COUNTERCLOCKWISE_90;
            }
            default: {
                throw new IllegalArgumentException(new StringBuilder().append("rotationSteps must be a value from 0-3. Got value ").append(integer).toString());
            }
        }
    }
    
    public static AABB getStructureBounds(final StructureBlockEntity cdg) {
        final BlockPos fx2 = cdg.getBlockPos();
        final BlockPos fx3 = fx2.offset(cdg.getStructureSize().offset(-1, -1, -1));
        final BlockPos fx4 = StructureTemplate.transform(fx3, Mirror.NONE, cdg.getRotation(), fx2);
        return new AABB(fx2, fx4);
    }
    
    public static BoundingBox getStructureBoundingBox(final StructureBlockEntity cdg) {
        final BlockPos fx2 = cdg.getBlockPos();
        final BlockPos fx3 = fx2.offset(cdg.getStructureSize().offset(-1, -1, -1));
        final BlockPos fx4 = StructureTemplate.transform(fx3, Mirror.NONE, cdg.getRotation(), fx2);
        return new BoundingBox(fx2, fx4);
    }
    
    public static void addCommandBlockAndButtonToStartTest(final BlockPos fx1, final BlockPos fx2, final Rotation bzj, final ServerLevel aag) {
        final BlockPos fx3 = StructureTemplate.transform(fx1.offset(fx2), Mirror.NONE, bzj, fx1);
        aag.setBlockAndUpdate(fx3, Blocks.COMMAND_BLOCK.defaultBlockState());
        final CommandBlockEntity ccl6 = (CommandBlockEntity)aag.getBlockEntity(fx3);
        ccl6.getCommandBlock().setCommand("test runthis");
        final BlockPos fx4 = StructureTemplate.transform(fx3.offset(0, 0, -1), Mirror.NONE, bzj, fx3);
        aag.setBlockAndUpdate(fx4, Blocks.STONE_BUTTON.defaultBlockState().rotate(bzj));
    }
    
    public static void createNewEmptyStructureBlock(final String string, final BlockPos fx2, final BlockPos fx3, final Rotation bzj, final ServerLevel aag) {
        final BoundingBox cqx6 = getStructureBoundingBox(fx2, fx3, bzj);
        clearSpaceForStructure(cqx6, fx2.getY(), aag);
        aag.setBlockAndUpdate(fx2, Blocks.STRUCTURE_BLOCK.defaultBlockState());
        final StructureBlockEntity cdg7 = (StructureBlockEntity)aag.getBlockEntity(fx2);
        cdg7.setIgnoreEntities(false);
        cdg7.setStructureName(new ResourceLocation(string));
        cdg7.setStructureSize(fx3);
        cdg7.setMode(StructureMode.SAVE);
        cdg7.setShowBoundingBox(true);
    }
    
    public static StructureBlockEntity spawnStructure(final String string, final BlockPos fx, final Rotation bzj, final int integer, final ServerLevel aag, final boolean boolean6) {
        final BlockPos fx2 = getStructureTemplate(string, aag).getSize();
        final BoundingBox cqx8 = getStructureBoundingBox(fx, fx2, bzj);
        BlockPos fx3;
        if (bzj == Rotation.NONE) {
            fx3 = fx;
        }
        else if (bzj == Rotation.CLOCKWISE_90) {
            fx3 = fx.offset(fx2.getZ() - 1, 0, 0);
        }
        else if (bzj == Rotation.CLOCKWISE_180) {
            fx3 = fx.offset(fx2.getX() - 1, 0, fx2.getZ() - 1);
        }
        else {
            if (bzj != Rotation.COUNTERCLOCKWISE_90) {
                throw new IllegalArgumentException(new StringBuilder().append("Invalid rotation: ").append(bzj).toString());
            }
            fx3 = fx.offset(0, 0, fx2.getX() - 1);
        }
        forceLoadChunks(fx, aag);
        clearSpaceForStructure(cqx8, fx.getY(), aag);
        final StructureBlockEntity cdg10 = createStructureBlock(string, fx3, bzj, aag, boolean6);
        aag.getBlockTicks().fetchTicksInArea(cqx8, true, false);
        aag.clearBlockEvents(cqx8);
        return cdg10;
    }
    
    private static void forceLoadChunks(final BlockPos fx, final ServerLevel aag) {
        final ChunkPos bra3 = new ChunkPos(fx);
        for (int integer4 = -1; integer4 < 4; ++integer4) {
            for (int integer5 = -1; integer5 < 4; ++integer5) {
                final int integer6 = bra3.x + integer4;
                final int integer7 = bra3.z + integer5;
                aag.setChunkForced(integer6, integer7, true);
            }
        }
    }
    
    public static void clearSpaceForStructure(final BoundingBox cqx, final int integer, final ServerLevel aag) {
        final BoundingBox cqx2 = new BoundingBox(cqx.x0 - 2, cqx.y0 - 3, cqx.z0 - 3, cqx.x1 + 3, cqx.y1 + 20, cqx.z1 + 3);
        BlockPos.betweenClosedStream(cqx2).forEach(fx -> clearBlock(integer, fx, aag));
        aag.getBlockTicks().fetchTicksInArea(cqx2, true, false);
        aag.clearBlockEvents(cqx2);
        final AABB dcf5 = new AABB(cqx2.x0, cqx2.y0, cqx2.z0, cqx2.x1, cqx2.y1, cqx2.z1);
        final List<Entity> list6 = aag.<Entity>getEntitiesOfClass((java.lang.Class<? extends Entity>)Entity.class, dcf5, (java.util.function.Predicate<? super Entity>)(apx -> !(apx instanceof Player)));
        list6.forEach(Entity::remove);
    }
    
    public static BoundingBox getStructureBoundingBox(final BlockPos fx1, final BlockPos fx2, final Rotation bzj) {
        final BlockPos fx3 = fx1.offset(fx2).offset(-1, -1, -1);
        final BlockPos fx4 = StructureTemplate.transform(fx3, Mirror.NONE, bzj, fx1);
        final BoundingBox cqx6 = BoundingBox.createProper(fx1.getX(), fx1.getY(), fx1.getZ(), fx4.getX(), fx4.getY(), fx4.getZ());
        final int integer7 = Math.min(cqx6.x0, cqx6.x1);
        final int integer8 = Math.min(cqx6.z0, cqx6.z1);
        final BlockPos fx5 = new BlockPos(fx1.getX() - integer7, 0, fx1.getZ() - integer8);
        cqx6.move(fx5);
        return cqx6;
    }
    
    public static Optional<BlockPos> findStructureBlockContainingPos(final BlockPos fx, final int integer, final ServerLevel aag) {
        return (Optional<BlockPos>)findStructureBlocks(fx, integer, aag).stream().filter(fx3 -> doesStructureContain(fx3, fx, aag)).findFirst();
    }
    
    @Nullable
    public static BlockPos findNearestStructureBlock(final BlockPos fx, final int integer, final ServerLevel aag) {
        final Comparator<BlockPos> comparator4 = (Comparator<BlockPos>)Comparator.comparingInt(fx2 -> fx2.distManhattan(fx));
        final Collection<BlockPos> collection5 = findStructureBlocks(fx, integer, aag);
        final Optional<BlockPos> optional6 = (Optional<BlockPos>)collection5.stream().min((Comparator)comparator4);
        return (BlockPos)optional6.orElse(null);
    }
    
    public static Collection<BlockPos> findStructureBlocks(final BlockPos fx, final int integer, final ServerLevel aag) {
        final Collection<BlockPos> collection4 = (Collection<BlockPos>)Lists.newArrayList();
        AABB dcf5 = new AABB(fx);
        dcf5 = dcf5.inflate(integer);
        for (int integer2 = (int)dcf5.minX; integer2 <= (int)dcf5.maxX; ++integer2) {
            for (int integer3 = (int)dcf5.minY; integer3 <= (int)dcf5.maxY; ++integer3) {
                for (int integer4 = (int)dcf5.minZ; integer4 <= (int)dcf5.maxZ; ++integer4) {
                    final BlockPos fx2 = new BlockPos(integer2, integer3, integer4);
                    final BlockState cee10 = aag.getBlockState(fx2);
                    if (cee10.is(Blocks.STRUCTURE_BLOCK)) {
                        collection4.add(fx2);
                    }
                }
            }
        }
        return collection4;
    }
    
    private static StructureTemplate getStructureTemplate(final String string, final ServerLevel aag) {
        final StructureManager cst3 = aag.getStructureManager();
        final StructureTemplate csy4 = cst3.get(new ResourceLocation(string));
        if (csy4 != null) {
            return csy4;
        }
        final String string2 = string + ".snbt";
        final Path path6 = Paths.get(StructureUtils.testStructuresDir, new String[] { string2 });
        final CompoundTag md7 = tryLoadStructure(path6);
        if (md7 == null) {
            throw new RuntimeException(new StringBuilder().append("Could not find structure file ").append(path6).append(", and the structure is not available in the world structures either.").toString());
        }
        return cst3.readStructure(md7);
    }
    
    private static StructureBlockEntity createStructureBlock(final String string, final BlockPos fx, final Rotation bzj, final ServerLevel aag, final boolean boolean5) {
        aag.setBlockAndUpdate(fx, Blocks.STRUCTURE_BLOCK.defaultBlockState());
        final StructureBlockEntity cdg6 = (StructureBlockEntity)aag.getBlockEntity(fx);
        cdg6.setMode(StructureMode.LOAD);
        cdg6.setRotation(bzj);
        cdg6.setIgnoreEntities(false);
        cdg6.setStructureName(new ResourceLocation(string));
        cdg6.loadStructure(aag, boolean5);
        if (cdg6.getStructureSize() != BlockPos.ZERO) {
            return cdg6;
        }
        final StructureTemplate csy7 = getStructureTemplate(string, aag);
        cdg6.loadStructure(aag, boolean5, csy7);
        if (cdg6.getStructureSize() == BlockPos.ZERO) {
            throw new RuntimeException("Failed to load structure " + string);
        }
        return cdg6;
    }
    
    @Nullable
    private static CompoundTag tryLoadStructure(final Path path) {
        try {
            final BufferedReader bufferedReader2 = Files.newBufferedReader(path);
            final String string3 = IOUtils.toString((Reader)bufferedReader2);
            return TagParser.parseTag(string3);
        }
        catch (IOException iOException2) {
            return null;
        }
        catch (CommandSyntaxException commandSyntaxException2) {
            throw new RuntimeException(new StringBuilder().append("Error while trying to load structure ").append(path).toString(), (Throwable)commandSyntaxException2);
        }
    }
    
    private static void clearBlock(final int integer, final BlockPos fx, final ServerLevel aag) {
        BlockState cee4 = null;
        final FlatLevelGeneratorSettings cpc5 = FlatLevelGeneratorSettings.getDefault(aag.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY));
        if (cpc5 instanceof FlatLevelGeneratorSettings) {
            final BlockState[] arr6 = cpc5.getLayers();
            if (fx.getY() < integer && fx.getY() <= arr6.length) {
                cee4 = arr6[fx.getY() - 1];
            }
        }
        else if (fx.getY() == integer - 1) {
            cee4 = aag.getBiome(fx).getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial();
        }
        else if (fx.getY() < integer - 1) {
            cee4 = aag.getBiome(fx).getGenerationSettings().getSurfaceBuilderConfig().getUnderMaterial();
        }
        if (cee4 == null) {
            cee4 = Blocks.AIR.defaultBlockState();
        }
        final BlockInput ef6 = new BlockInput(cee4, (Set<Property<?>>)Collections.emptySet(), null);
        ef6.place(aag, fx, 2);
        aag.blockUpdated(fx, cee4.getBlock());
    }
    
    private static boolean doesStructureContain(final BlockPos fx1, final BlockPos fx2, final ServerLevel aag) {
        final StructureBlockEntity cdg4 = (StructureBlockEntity)aag.getBlockEntity(fx1);
        final AABB dcf5 = getStructureBounds(cdg4).inflate(1.0);
        return dcf5.contains(Vec3.atCenterOf(fx2));
    }
    
    static {
        StructureUtils.testStructuresDir = "gameteststructures";
    }
}
