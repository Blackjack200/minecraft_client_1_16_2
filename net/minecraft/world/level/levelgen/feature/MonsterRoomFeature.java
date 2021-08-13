package net.minecraft.world.level.levelgen.feature;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.Util;
import net.minecraft.world.level.block.entity.BlockEntity;
import java.util.Iterator;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.EntityType;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MonsterRoomFeature extends Feature<NoneFeatureConfiguration> {
    private static final Logger LOGGER;
    private static final EntityType<?>[] MOBS;
    private static final BlockState AIR;
    
    public MonsterRoomFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        final int integer7 = 3;
        final int integer8 = random.nextInt(2) + 2;
        final int integer9 = -integer8 - 1;
        final int integer10 = integer8 + 1;
        final int integer11 = -1;
        final int integer12 = 4;
        final int integer13 = random.nextInt(2) + 2;
        final int integer14 = -integer13 - 1;
        final int integer15 = integer13 + 1;
        int integer16 = 0;
        for (int integer17 = integer9; integer17 <= integer10; ++integer17) {
            for (int integer18 = -1; integer18 <= 4; ++integer18) {
                for (int integer19 = integer14; integer19 <= integer15; ++integer19) {
                    final BlockPos fx2 = fx.offset(integer17, integer18, integer19);
                    final Material cux21 = bso.getBlockState(fx2).getMaterial();
                    final boolean boolean22 = cux21.isSolid();
                    if (integer18 == -1 && !boolean22) {
                        return false;
                    }
                    if (integer18 == 4 && !boolean22) {
                        return false;
                    }
                    if ((integer17 == integer9 || integer17 == integer10 || integer19 == integer14 || integer19 == integer15) && integer18 == 0 && bso.isEmptyBlock(fx2) && bso.isEmptyBlock(fx2.above())) {
                        ++integer16;
                    }
                }
            }
        }
        if (integer16 < 1 || integer16 > 5) {
            return false;
        }
        for (int integer17 = integer9; integer17 <= integer10; ++integer17) {
            for (int integer18 = 3; integer18 >= -1; --integer18) {
                for (int integer19 = integer14; integer19 <= integer15; ++integer19) {
                    final BlockPos fx2 = fx.offset(integer17, integer18, integer19);
                    final BlockState cee21 = bso.getBlockState(fx2);
                    if (integer17 == integer9 || integer18 == -1 || integer19 == integer14 || integer17 == integer10 || integer18 == 4 || integer19 == integer15) {
                        if (fx2.getY() >= 0 && !bso.getBlockState(fx2.below()).getMaterial().isSolid()) {
                            bso.setBlock(fx2, MonsterRoomFeature.AIR, 2);
                        }
                        else if (cee21.getMaterial().isSolid() && !cee21.is(Blocks.CHEST)) {
                            if (integer18 == -1 && random.nextInt(4) != 0) {
                                bso.setBlock(fx2, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 2);
                            }
                            else {
                                bso.setBlock(fx2, Blocks.COBBLESTONE.defaultBlockState(), 2);
                            }
                        }
                    }
                    else if (!cee21.is(Blocks.CHEST) && !cee21.is(Blocks.SPAWNER)) {
                        bso.setBlock(fx2, MonsterRoomFeature.AIR, 2);
                    }
                }
            }
        }
        for (int integer17 = 0; integer17 < 2; ++integer17) {
            for (int integer18 = 0; integer18 < 3; ++integer18) {
                final int integer19 = fx.getX() + random.nextInt(integer8 * 2 + 1) - integer8;
                final int integer20 = fx.getY();
                final int integer21 = fx.getZ() + random.nextInt(integer13 * 2 + 1) - integer13;
                final BlockPos fx3 = new BlockPos(integer19, integer20, integer21);
                if (bso.isEmptyBlock(fx3)) {
                    int integer22 = 0;
                    for (final Direction gc25 : Direction.Plane.HORIZONTAL) {
                        if (bso.getBlockState(fx3.relative(gc25)).getMaterial().isSolid()) {
                            ++integer22;
                        }
                    }
                    if (integer22 == 1) {
                        bso.setBlock(fx3, StructurePiece.reorient(bso, fx3, Blocks.CHEST.defaultBlockState()), 2);
                        RandomizableContainerBlockEntity.setLootTable(bso, random, fx3, BuiltInLootTables.SIMPLE_DUNGEON);
                        break;
                    }
                }
            }
        }
        bso.setBlock(fx, Blocks.SPAWNER.defaultBlockState(), 2);
        final BlockEntity ccg17 = bso.getBlockEntity(fx);
        if (ccg17 instanceof SpawnerBlockEntity) {
            ((SpawnerBlockEntity)ccg17).getSpawner().setEntityId(this.randomEntityId(random));
        }
        else {
            MonsterRoomFeature.LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})", fx.getX(), fx.getY(), fx.getZ());
        }
        return true;
    }
    
    private EntityType<?> randomEntityId(final Random random) {
        return Util.<EntityType<?>>getRandom(MonsterRoomFeature.MOBS, random);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        MOBS = new EntityType[] { EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER };
        AIR = Blocks.CAVE_AIR.defaultBlockState();
    }
}
