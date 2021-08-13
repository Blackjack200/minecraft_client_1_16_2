package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import java.util.Random;

public class SwamplandHutPiece extends ScatteredFeaturePiece {
    private boolean spawnedWitch;
    private boolean spawnedCat;
    
    public SwamplandHutPiece(final Random random, final int integer2, final int integer3) {
        super(StructurePieceType.SWAMPLAND_HUT, random, integer2, 64, integer3, 7, 7, 9);
    }
    
    public SwamplandHutPiece(final StructureManager cst, final CompoundTag md) {
        super(StructurePieceType.SWAMPLAND_HUT, md);
        this.spawnedWitch = md.getBoolean("Witch");
        this.spawnedCat = md.getBoolean("Cat");
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        super.addAdditionalSaveData(md);
        md.putBoolean("Witch", this.spawnedWitch);
        md.putBoolean("Cat", this.spawnedCat);
    }
    
    @Override
    public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
        if (!this.updateAverageGroundHeight(bso, cqx, 0)) {
            return false;
        }
        this.generateBox(bso, cqx, 1, 1, 1, 5, 1, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        this.generateBox(bso, cqx, 1, 4, 2, 5, 4, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        this.generateBox(bso, cqx, 2, 1, 0, 4, 1, 0, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        this.generateBox(bso, cqx, 2, 2, 2, 3, 3, 2, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        this.generateBox(bso, cqx, 1, 2, 3, 1, 3, 6, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        this.generateBox(bso, cqx, 5, 2, 3, 5, 3, 6, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        this.generateBox(bso, cqx, 2, 2, 7, 4, 3, 7, Blocks.SPRUCE_PLANKS.defaultBlockState(), Blocks.SPRUCE_PLANKS.defaultBlockState(), false);
        this.generateBox(bso, cqx, 1, 0, 2, 1, 3, 2, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
        this.generateBox(bso, cqx, 5, 0, 2, 5, 3, 2, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
        this.generateBox(bso, cqx, 1, 0, 7, 1, 3, 7, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
        this.generateBox(bso, cqx, 5, 0, 7, 5, 3, 7, Blocks.OAK_LOG.defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), false);
        this.placeBlock(bso, Blocks.OAK_FENCE.defaultBlockState(), 2, 3, 2, cqx);
        this.placeBlock(bso, Blocks.OAK_FENCE.defaultBlockState(), 3, 3, 7, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 1, 3, 4, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 5, 3, 4, cqx);
        this.placeBlock(bso, Blocks.AIR.defaultBlockState(), 5, 3, 5, cqx);
        this.placeBlock(bso, Blocks.POTTED_RED_MUSHROOM.defaultBlockState(), 1, 3, 5, cqx);
        this.placeBlock(bso, Blocks.CRAFTING_TABLE.defaultBlockState(), 3, 2, 6, cqx);
        this.placeBlock(bso, Blocks.CAULDRON.defaultBlockState(), 4, 2, 6, cqx);
        this.placeBlock(bso, Blocks.OAK_FENCE.defaultBlockState(), 1, 2, 1, cqx);
        this.placeBlock(bso, Blocks.OAK_FENCE.defaultBlockState(), 5, 2, 1, cqx);
        final BlockState cee9 = ((StateHolder<O, BlockState>)Blocks.SPRUCE_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.NORTH);
        final BlockState cee10 = ((StateHolder<O, BlockState>)Blocks.SPRUCE_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.EAST);
        final BlockState cee11 = ((StateHolder<O, BlockState>)Blocks.SPRUCE_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.WEST);
        final BlockState cee12 = ((StateHolder<O, BlockState>)Blocks.SPRUCE_STAIRS.defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)StairBlock.FACING, Direction.SOUTH);
        this.generateBox(bso, cqx, 0, 4, 1, 6, 4, 1, cee9, cee9, false);
        this.generateBox(bso, cqx, 0, 4, 2, 0, 4, 7, cee10, cee10, false);
        this.generateBox(bso, cqx, 6, 4, 2, 6, 4, 7, cee11, cee11, false);
        this.generateBox(bso, cqx, 0, 4, 8, 6, 4, 8, cee12, cee12, false);
        this.placeBlock(bso, ((StateHolder<O, BlockState>)cee9).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, StairsShape.OUTER_RIGHT), 0, 4, 1, cqx);
        this.placeBlock(bso, ((StateHolder<O, BlockState>)cee9).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT), 6, 4, 1, cqx);
        this.placeBlock(bso, ((StateHolder<O, BlockState>)cee12).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, StairsShape.OUTER_LEFT), 0, 4, 8, cqx);
        this.placeBlock(bso, ((StateHolder<O, BlockState>)cee12).<StairsShape, StairsShape>setValue(StairBlock.SHAPE, StairsShape.OUTER_RIGHT), 6, 4, 8, cqx);
        for (int integer13 = 2; integer13 <= 7; integer13 += 5) {
            for (int integer14 = 1; integer14 <= 5; integer14 += 4) {
                this.fillColumnDown(bso, Blocks.OAK_LOG.defaultBlockState(), integer14, -1, integer13, cqx);
            }
        }
        if (!this.spawnedWitch) {
            final int integer13 = this.getWorldX(2, 5);
            final int integer14 = this.getWorldY(2);
            final int integer15 = this.getWorldZ(2, 5);
            if (cqx.isInside(new BlockPos(integer13, integer14, integer15))) {
                this.spawnedWitch = true;
                final Witch bed16 = EntityType.WITCH.create(bso.getLevel());
                bed16.setPersistenceRequired();
                bed16.moveTo(integer13 + 0.5, integer14, integer15 + 0.5, 0.0f, 0.0f);
                bed16.finalizeSpawn(bso, bso.getCurrentDifficultyAt(new BlockPos(integer13, integer14, integer15)), MobSpawnType.STRUCTURE, null, null);
                bso.addFreshEntityWithPassengers(bed16);
            }
        }
        this.spawnCat(bso, cqx);
        return true;
    }
    
    private void spawnCat(final ServerLevelAccessor bsh, final BoundingBox cqx) {
        if (!this.spawnedCat) {
            final int integer4 = this.getWorldX(2, 5);
            final int integer5 = this.getWorldY(2);
            final int integer6 = this.getWorldZ(2, 5);
            if (cqx.isInside(new BlockPos(integer4, integer5, integer6))) {
                this.spawnedCat = true;
                final Cat azy7 = EntityType.CAT.create(bsh.getLevel());
                azy7.setPersistenceRequired();
                azy7.moveTo(integer4 + 0.5, integer5, integer6 + 0.5, 0.0f, 0.0f);
                azy7.finalizeSpawn(bsh, bsh.getCurrentDifficultyAt(new BlockPos(integer4, integer5, integer6)), MobSpawnType.STRUCTURE, null, null);
                bsh.addFreshEntityWithPassengers(azy7);
            }
        }
    }
}
