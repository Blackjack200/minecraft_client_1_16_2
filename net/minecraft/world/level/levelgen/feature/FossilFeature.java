package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class FossilFeature extends Feature<NoneFeatureConfiguration> {
    private static final ResourceLocation SPINE_1;
    private static final ResourceLocation SPINE_2;
    private static final ResourceLocation SPINE_3;
    private static final ResourceLocation SPINE_4;
    private static final ResourceLocation SPINE_1_COAL;
    private static final ResourceLocation SPINE_2_COAL;
    private static final ResourceLocation SPINE_3_COAL;
    private static final ResourceLocation SPINE_4_COAL;
    private static final ResourceLocation SKULL_1;
    private static final ResourceLocation SKULL_2;
    private static final ResourceLocation SKULL_3;
    private static final ResourceLocation SKULL_4;
    private static final ResourceLocation SKULL_1_COAL;
    private static final ResourceLocation SKULL_2_COAL;
    private static final ResourceLocation SKULL_3_COAL;
    private static final ResourceLocation SKULL_4_COAL;
    private static final ResourceLocation[] fossils;
    private static final ResourceLocation[] fossilsCoal;
    
    public FossilFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        final Rotation bzj7 = Rotation.getRandom(random);
        final int integer8 = random.nextInt(FossilFeature.fossils.length);
        final StructureManager cst9 = bso.getLevel().getServer().getStructureManager();
        final StructureTemplate csy10 = cst9.getOrCreate(FossilFeature.fossils[integer8]);
        final StructureTemplate csy11 = cst9.getOrCreate(FossilFeature.fossilsCoal[integer8]);
        final ChunkPos bra12 = new ChunkPos(fx);
        final BoundingBox cqx13 = new BoundingBox(bra12.getMinBlockX(), 0, bra12.getMinBlockZ(), bra12.getMaxBlockX(), 256, bra12.getMaxBlockZ());
        final StructurePlaceSettings csu14 = new StructurePlaceSettings().setRotation(bzj7).setBoundingBox(cqx13).setRandom(random).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        final BlockPos fx2 = csy10.getSize(bzj7);
        final int integer9 = random.nextInt(16 - fx2.getX());
        final int integer10 = random.nextInt(16 - fx2.getZ());
        int integer11 = 256;
        for (int integer12 = 0; integer12 < fx2.getX(); ++integer12) {
            for (int integer13 = 0; integer13 < fx2.getZ(); ++integer13) {
                integer11 = Math.min(integer11, bso.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, fx.getX() + integer12 + integer9, fx.getZ() + integer13 + integer10));
            }
        }
        int integer12 = Math.max(integer11 - 15 - random.nextInt(10), 10);
        final BlockPos fx3 = csy10.getZeroPositionWithTransform(fx.offset(integer9, integer12, integer10), Mirror.NONE, bzj7);
        final BlockRotProcessor csd21 = new BlockRotProcessor(0.9f);
        csu14.clearProcessors().addProcessor(csd21);
        csy10.placeInWorld(bso, fx3, fx3, csu14, random, 4);
        csu14.popProcessor(csd21);
        final BlockRotProcessor csd22 = new BlockRotProcessor(0.1f);
        csu14.clearProcessors().addProcessor(csd22);
        csy11.placeInWorld(bso, fx3, fx3, csu14, random, 4);
        return true;
    }
    
    static {
        SPINE_1 = new ResourceLocation("fossil/spine_1");
        SPINE_2 = new ResourceLocation("fossil/spine_2");
        SPINE_3 = new ResourceLocation("fossil/spine_3");
        SPINE_4 = new ResourceLocation("fossil/spine_4");
        SPINE_1_COAL = new ResourceLocation("fossil/spine_1_coal");
        SPINE_2_COAL = new ResourceLocation("fossil/spine_2_coal");
        SPINE_3_COAL = new ResourceLocation("fossil/spine_3_coal");
        SPINE_4_COAL = new ResourceLocation("fossil/spine_4_coal");
        SKULL_1 = new ResourceLocation("fossil/skull_1");
        SKULL_2 = new ResourceLocation("fossil/skull_2");
        SKULL_3 = new ResourceLocation("fossil/skull_3");
        SKULL_4 = new ResourceLocation("fossil/skull_4");
        SKULL_1_COAL = new ResourceLocation("fossil/skull_1_coal");
        SKULL_2_COAL = new ResourceLocation("fossil/skull_2_coal");
        SKULL_3_COAL = new ResourceLocation("fossil/skull_3_coal");
        SKULL_4_COAL = new ResourceLocation("fossil/skull_4_coal");
        fossils = new ResourceLocation[] { FossilFeature.SPINE_1, FossilFeature.SPINE_2, FossilFeature.SPINE_3, FossilFeature.SPINE_4, FossilFeature.SKULL_1, FossilFeature.SKULL_2, FossilFeature.SKULL_3, FossilFeature.SKULL_4 };
        fossilsCoal = new ResourceLocation[] { FossilFeature.SPINE_1_COAL, FossilFeature.SPINE_2_COAL, FossilFeature.SPINE_3_COAL, FossilFeature.SPINE_4_COAL, FossilFeature.SKULL_1_COAL, FossilFeature.SKULL_2_COAL, FossilFeature.SKULL_3_COAL, FossilFeature.SKULL_4_COAL };
    }
}
