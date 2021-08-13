package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.material.FluidState;
import java.util.Iterator;
import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.tags.Tag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import com.google.common.collect.Lists;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.feature.configurations.OceanRuinConfiguration;
import java.util.List;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.Util;
import java.util.Random;
import net.minecraft.resources.ResourceLocation;

public class OceanRuinPieces {
    private static final ResourceLocation[] WARM_RUINS;
    private static final ResourceLocation[] RUINS_BRICK;
    private static final ResourceLocation[] RUINS_CRACKED;
    private static final ResourceLocation[] RUINS_MOSSY;
    private static final ResourceLocation[] BIG_RUINS_BRICK;
    private static final ResourceLocation[] BIG_RUINS_MOSSY;
    private static final ResourceLocation[] BIG_RUINS_CRACKED;
    private static final ResourceLocation[] BIG_WARM_RUINS;
    
    private static ResourceLocation getSmallWarmRuin(final Random random) {
        return Util.<ResourceLocation>getRandom(OceanRuinPieces.WARM_RUINS, random);
    }
    
    private static ResourceLocation getBigWarmRuin(final Random random) {
        return Util.<ResourceLocation>getRandom(OceanRuinPieces.BIG_WARM_RUINS, random);
    }
    
    public static void addPieces(final StructureManager cst, final BlockPos fx, final Rotation bzj, final List<StructurePiece> list, final Random random, final OceanRuinConfiguration cmf) {
        final boolean boolean7 = random.nextFloat() <= cmf.largeProbability;
        final float float8 = boolean7 ? 0.9f : 0.8f;
        addPiece(cst, fx, bzj, list, random, cmf, boolean7, float8);
        if (boolean7 && random.nextFloat() <= cmf.clusterProbability) {
            addClusterRuins(cst, random, bzj, fx, cmf, list);
        }
    }
    
    private static void addClusterRuins(final StructureManager cst, final Random random, final Rotation bzj, final BlockPos fx, final OceanRuinConfiguration cmf, final List<StructurePiece> list) {
        final int integer7 = fx.getX();
        final int integer8 = fx.getZ();
        final BlockPos fx2 = StructureTemplate.transform(new BlockPos(15, 0, 15), Mirror.NONE, bzj, BlockPos.ZERO).offset(integer7, 0, integer8);
        final BoundingBox cqx10 = BoundingBox.createProper(integer7, 0, integer8, fx2.getX(), 0, fx2.getZ());
        final BlockPos fx3 = new BlockPos(Math.min(integer7, fx2.getX()), 0, Math.min(integer8, fx2.getZ()));
        final List<BlockPos> list2 = allPositions(random, fx3.getX(), fx3.getZ());
        for (int integer9 = Mth.nextInt(random, 4, 8), integer10 = 0; integer10 < integer9; ++integer10) {
            if (!list2.isEmpty()) {
                final int integer11 = random.nextInt(list2.size());
                final BlockPos fx4 = (BlockPos)list2.remove(integer11);
                final int integer12 = fx4.getX();
                final int integer13 = fx4.getZ();
                final Rotation bzj2 = Rotation.getRandom(random);
                final BlockPos fx5 = StructureTemplate.transform(new BlockPos(5, 0, 6), Mirror.NONE, bzj2, BlockPos.ZERO).offset(integer12, 0, integer13);
                final BoundingBox cqx11 = BoundingBox.createProper(integer12, 0, integer13, fx5.getX(), 0, fx5.getZ());
                if (!cqx11.intersects(cqx10)) {
                    addPiece(cst, fx4, bzj2, list, random, cmf, false, 0.8f);
                }
            }
        }
    }
    
    private static List<BlockPos> allPositions(final Random random, final int integer2, final int integer3) {
        final List<BlockPos> list4 = (List<BlockPos>)Lists.newArrayList();
        list4.add(new BlockPos(integer2 - 16 + Mth.nextInt(random, 1, 8), 90, integer3 + 16 + Mth.nextInt(random, 1, 7)));
        list4.add(new BlockPos(integer2 - 16 + Mth.nextInt(random, 1, 8), 90, integer3 + Mth.nextInt(random, 1, 7)));
        list4.add(new BlockPos(integer2 - 16 + Mth.nextInt(random, 1, 8), 90, integer3 - 16 + Mth.nextInt(random, 4, 8)));
        list4.add(new BlockPos(integer2 + Mth.nextInt(random, 1, 7), 90, integer3 + 16 + Mth.nextInt(random, 1, 7)));
        list4.add(new BlockPos(integer2 + Mth.nextInt(random, 1, 7), 90, integer3 - 16 + Mth.nextInt(random, 4, 6)));
        list4.add(new BlockPos(integer2 + 16 + Mth.nextInt(random, 1, 7), 90, integer3 + 16 + Mth.nextInt(random, 3, 8)));
        list4.add(new BlockPos(integer2 + 16 + Mth.nextInt(random, 1, 7), 90, integer3 + Mth.nextInt(random, 1, 7)));
        list4.add(new BlockPos(integer2 + 16 + Mth.nextInt(random, 1, 7), 90, integer3 - 16 + Mth.nextInt(random, 4, 8)));
        return list4;
    }
    
    private static void addPiece(final StructureManager cst, final BlockPos fx, final Rotation bzj, final List<StructurePiece> list, final Random random, final OceanRuinConfiguration cmf, final boolean boolean7, final float float8) {
        if (cmf.biomeTemp == OceanRuinFeature.Type.WARM) {
            final ResourceLocation vk9 = boolean7 ? getBigWarmRuin(random) : getSmallWarmRuin(random);
            list.add(new OceanRuinPiece(cst, vk9, fx, bzj, float8, cmf.biomeTemp, boolean7));
        }
        else if (cmf.biomeTemp == OceanRuinFeature.Type.COLD) {
            final ResourceLocation[] arr9 = boolean7 ? OceanRuinPieces.BIG_RUINS_BRICK : OceanRuinPieces.RUINS_BRICK;
            final ResourceLocation[] arr10 = boolean7 ? OceanRuinPieces.BIG_RUINS_CRACKED : OceanRuinPieces.RUINS_CRACKED;
            final ResourceLocation[] arr11 = boolean7 ? OceanRuinPieces.BIG_RUINS_MOSSY : OceanRuinPieces.RUINS_MOSSY;
            final int integer12 = random.nextInt(arr9.length);
            list.add(new OceanRuinPiece(cst, arr9[integer12], fx, bzj, float8, cmf.biomeTemp, boolean7));
            list.add(new OceanRuinPiece(cst, arr10[integer12], fx, bzj, 0.7f, cmf.biomeTemp, boolean7));
            list.add(new OceanRuinPiece(cst, arr11[integer12], fx, bzj, 0.5f, cmf.biomeTemp, boolean7));
        }
    }
    
    static {
        WARM_RUINS = new ResourceLocation[] { new ResourceLocation("underwater_ruin/warm_1"), new ResourceLocation("underwater_ruin/warm_2"), new ResourceLocation("underwater_ruin/warm_3"), new ResourceLocation("underwater_ruin/warm_4"), new ResourceLocation("underwater_ruin/warm_5"), new ResourceLocation("underwater_ruin/warm_6"), new ResourceLocation("underwater_ruin/warm_7"), new ResourceLocation("underwater_ruin/warm_8") };
        RUINS_BRICK = new ResourceLocation[] { new ResourceLocation("underwater_ruin/brick_1"), new ResourceLocation("underwater_ruin/brick_2"), new ResourceLocation("underwater_ruin/brick_3"), new ResourceLocation("underwater_ruin/brick_4"), new ResourceLocation("underwater_ruin/brick_5"), new ResourceLocation("underwater_ruin/brick_6"), new ResourceLocation("underwater_ruin/brick_7"), new ResourceLocation("underwater_ruin/brick_8") };
        RUINS_CRACKED = new ResourceLocation[] { new ResourceLocation("underwater_ruin/cracked_1"), new ResourceLocation("underwater_ruin/cracked_2"), new ResourceLocation("underwater_ruin/cracked_3"), new ResourceLocation("underwater_ruin/cracked_4"), new ResourceLocation("underwater_ruin/cracked_5"), new ResourceLocation("underwater_ruin/cracked_6"), new ResourceLocation("underwater_ruin/cracked_7"), new ResourceLocation("underwater_ruin/cracked_8") };
        RUINS_MOSSY = new ResourceLocation[] { new ResourceLocation("underwater_ruin/mossy_1"), new ResourceLocation("underwater_ruin/mossy_2"), new ResourceLocation("underwater_ruin/mossy_3"), new ResourceLocation("underwater_ruin/mossy_4"), new ResourceLocation("underwater_ruin/mossy_5"), new ResourceLocation("underwater_ruin/mossy_6"), new ResourceLocation("underwater_ruin/mossy_7"), new ResourceLocation("underwater_ruin/mossy_8") };
        BIG_RUINS_BRICK = new ResourceLocation[] { new ResourceLocation("underwater_ruin/big_brick_1"), new ResourceLocation("underwater_ruin/big_brick_2"), new ResourceLocation("underwater_ruin/big_brick_3"), new ResourceLocation("underwater_ruin/big_brick_8") };
        BIG_RUINS_MOSSY = new ResourceLocation[] { new ResourceLocation("underwater_ruin/big_mossy_1"), new ResourceLocation("underwater_ruin/big_mossy_2"), new ResourceLocation("underwater_ruin/big_mossy_3"), new ResourceLocation("underwater_ruin/big_mossy_8") };
        BIG_RUINS_CRACKED = new ResourceLocation[] { new ResourceLocation("underwater_ruin/big_cracked_1"), new ResourceLocation("underwater_ruin/big_cracked_2"), new ResourceLocation("underwater_ruin/big_cracked_3"), new ResourceLocation("underwater_ruin/big_cracked_8") };
        BIG_WARM_RUINS = new ResourceLocation[] { new ResourceLocation("underwater_ruin/big_warm_4"), new ResourceLocation("underwater_ruin/big_warm_5"), new ResourceLocation("underwater_ruin/big_warm_6"), new ResourceLocation("underwater_ruin/big_warm_7") };
    }
    
    public static class OceanRuinPiece extends TemplateStructurePiece {
        private final OceanRuinFeature.Type biomeType;
        private final float integrity;
        private final ResourceLocation templateLocation;
        private final Rotation rotation;
        private final boolean isLarge;
        
        public OceanRuinPiece(final StructureManager cst, final ResourceLocation vk, final BlockPos fx, final Rotation bzj, final float float5, final OceanRuinFeature.Type b, final boolean boolean7) {
            super(StructurePieceType.OCEAN_RUIN, 0);
            this.templateLocation = vk;
            this.templatePosition = fx;
            this.rotation = bzj;
            this.integrity = float5;
            this.biomeType = b;
            this.isLarge = boolean7;
            this.loadTemplate(cst);
        }
        
        public OceanRuinPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.OCEAN_RUIN, md);
            this.templateLocation = new ResourceLocation(md.getString("Template"));
            this.rotation = Rotation.valueOf(md.getString("Rot"));
            this.integrity = md.getFloat("Integrity");
            this.biomeType = OceanRuinFeature.Type.valueOf(md.getString("BiomeType"));
            this.isLarge = md.getBoolean("IsLarge");
            this.loadTemplate(cst);
        }
        
        private void loadTemplate(final StructureManager cst) {
            final StructureTemplate csy3 = cst.getOrCreate(this.templateLocation);
            final StructurePlaceSettings csu4 = new StructurePlaceSettings().setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
            this.setup(csy3, this.templatePosition, csu4);
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putString("Template", this.templateLocation.toString());
            md.putString("Rot", this.rotation.name());
            md.putFloat("Integrity", this.integrity);
            md.putString("BiomeType", this.biomeType.toString());
            md.putBoolean("IsLarge", this.isLarge);
        }
        
        @Override
        protected void handleDataMarker(final String string, final BlockPos fx, final ServerLevelAccessor bsh, final Random random, final BoundingBox cqx) {
            if ("chest".equals(string)) {
                bsh.setBlock(fx, ((StateHolder<O, BlockState>)Blocks.CHEST.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)ChestBlock.WATERLOGGED, bsh.getFluidState(fx).is(FluidTags.WATER)), 2);
                final BlockEntity ccg7 = bsh.getBlockEntity(fx);
                if (ccg7 instanceof ChestBlockEntity) {
                    ((ChestBlockEntity)ccg7).setLootTable(this.isLarge ? BuiltInLootTables.UNDERWATER_RUIN_BIG : BuiltInLootTables.UNDERWATER_RUIN_SMALL, random.nextLong());
                }
            }
            else if ("drowned".equals(string)) {
                final Drowned bdb7 = EntityType.DROWNED.create(bsh.getLevel());
                bdb7.setPersistenceRequired();
                bdb7.moveTo(fx, 0.0f, 0.0f);
                bdb7.finalizeSpawn(bsh, bsh.getCurrentDifficultyAt(fx), MobSpawnType.STRUCTURE, null, null);
                bsh.addFreshEntityWithPassengers(bdb7);
                if (fx.getY() > bsh.getSeaLevel()) {
                    bsh.setBlock(fx, Blocks.AIR.defaultBlockState(), 2);
                }
                else {
                    bsh.setBlock(fx, Blocks.WATER.defaultBlockState(), 2);
                }
            }
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            this.placeSettings.clearProcessors().addProcessor(new BlockRotProcessor(this.integrity)).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
            final int integer9 = bso.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.templatePosition.getX(), this.templatePosition.getZ());
            this.templatePosition = new BlockPos(this.templatePosition.getX(), integer9, this.templatePosition.getZ());
            final BlockPos fx2 = StructureTemplate.transform(new BlockPos(this.template.getSize().getX() - 1, 0, this.template.getSize().getZ() - 1), Mirror.NONE, this.rotation, BlockPos.ZERO).offset(this.templatePosition);
            this.templatePosition = new BlockPos(this.templatePosition.getX(), this.getHeight(this.templatePosition, bso, fx2), this.templatePosition.getZ());
            return super.postProcess(bso, bsk, cfv, random, cqx, bra, fx);
        }
        
        private int getHeight(final BlockPos fx1, final BlockGetter bqz, final BlockPos fx3) {
            int integer5 = fx1.getY();
            int integer6 = 512;
            final int integer7 = integer5 - 1;
            int integer8 = 0;
            for (final BlockPos fx4 : BlockPos.betweenClosed(fx1, fx3)) {
                final int integer9 = fx4.getX();
                final int integer10 = fx4.getZ();
                int integer11 = fx1.getY() - 1;
                final BlockPos.MutableBlockPos a14 = new BlockPos.MutableBlockPos(integer9, integer11, integer10);
                BlockState cee15 = bqz.getBlockState(a14);
                for (FluidState cuu16 = bqz.getFluidState(a14); (cee15.isAir() || cuu16.is(FluidTags.WATER) || cee15.getBlock().is(BlockTags.ICE)) && integer11 > 1; cee15 = bqz.getBlockState(a14), cuu16 = bqz.getFluidState(a14)) {
                    --integer11;
                    a14.set(integer9, integer11, integer10);
                }
                integer6 = Math.min(integer6, integer11);
                if (integer11 < integer7 - 2) {
                    ++integer8;
                }
            }
            final int integer12 = Math.abs(fx1.getX() - fx3.getX());
            if (integer7 - integer6 > 2 && integer8 > integer12 - 2) {
                integer5 = integer6 + 1;
            }
            return integer5;
        }
    }
}
