package net.minecraft.world.level.levelgen.feature.structures;

import java.util.Optional;
import net.minecraft.core.Direction;
import java.util.Iterator;
import java.util.Collection;
import com.google.common.collect.Lists;
import net.minecraft.core.Vec3i;
import java.util.Objects;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import com.google.common.collect.Queues;
import java.util.Deque;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.WritableRegistry;
import org.apache.commons.lang3.mutable.MutableObject;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import java.util.Random;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.core.RegistryAccess;
import org.apache.logging.log4j.Logger;

public class JigsawPlacement {
    private static final Logger LOGGER;
    
    public static void addPieces(final RegistryAccess gn, final JigsawConfiguration clz, final PieceFactory a, final ChunkGenerator cfv, final StructureManager cst, final BlockPos fx, final List<? super PoolElementStructurePiece> list, final Random random, final boolean boolean9, final boolean boolean10) {
        StructureFeature.bootstrap();
        final WritableRegistry<StructureTemplatePool> gs11 = gn.<StructureTemplatePool>registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        final Rotation bzj12 = Rotation.getRandom(random);
        final StructureTemplatePool coh13 = (StructureTemplatePool)clz.startPool().get();
        final StructurePoolElement cof14 = coh13.getRandomTemplate(random);
        final PoolElementStructurePiece crl15 = a.create(cst, cof14, fx, cof14.getGroundLevelDelta(), bzj12, cof14.getBoundingBox(cst, fx, bzj12));
        final BoundingBox cqx16 = crl15.getBoundingBox();
        final int integer17 = (cqx16.x1 + cqx16.x0) / 2;
        final int integer18 = (cqx16.z1 + cqx16.z0) / 2;
        int integer19;
        if (boolean10) {
            integer19 = fx.getY() + cfv.getFirstFreeHeight(integer17, integer18, Heightmap.Types.WORLD_SURFACE_WG);
        }
        else {
            integer19 = fx.getY();
        }
        final int integer20 = cqx16.y0 + crl15.getGroundLevelDelta();
        crl15.move(0, integer19 - integer20, 0);
        list.add(crl15);
        if (clz.maxDepth() <= 0) {
            return;
        }
        final int integer21 = 80;
        final AABB dcf22 = new AABB(integer17 - 80, integer19 - 80, integer18 - 80, integer17 + 80 + 1, integer19 + 80 + 1, integer18 + 80 + 1);
        final Placer c23 = new Placer((Registry)gs11, clz.maxDepth(), a, cfv, cst, (List)list, random);
        c23.placing.addLast(new PieceState(crl15, new MutableObject((Object)Shapes.join(Shapes.create(dcf22), Shapes.create(AABB.of(cqx16)), BooleanOp.ONLY_FIRST)), integer19 + 80, 0));
        while (!c23.placing.isEmpty()) {
            final PieceState b24 = (PieceState)c23.placing.removeFirst();
            c23.tryPlacingChildren(b24.piece, b24.free, b24.boundsTop, b24.depth, boolean9);
        }
    }
    
    public static void addPieces(final RegistryAccess gn, final PoolElementStructurePiece crl, final int integer, final PieceFactory a, final ChunkGenerator cfv, final StructureManager cst, final List<? super PoolElementStructurePiece> list, final Random random) {
        final WritableRegistry<StructureTemplatePool> gs9 = gn.<StructureTemplatePool>registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        final Placer c10 = new Placer((Registry)gs9, integer, a, cfv, cst, (List)list, random);
        c10.placing.addLast(new PieceState(crl, new MutableObject((Object)Shapes.INFINITY), 0, 0));
        while (!c10.placing.isEmpty()) {
            final PieceState b11 = (PieceState)c10.placing.removeFirst();
            c10.tryPlacingChildren(b11.piece, b11.free, b11.boundsTop, b11.depth, false);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    static final class PieceState {
        private final PoolElementStructurePiece piece;
        private final MutableObject<VoxelShape> free;
        private final int boundsTop;
        private final int depth;
        
        private PieceState(final PoolElementStructurePiece crl, final MutableObject<VoxelShape> mutableObject, final int integer3, final int integer4) {
            this.piece = crl;
            this.free = mutableObject;
            this.boundsTop = integer3;
            this.depth = integer4;
        }
    }
    
    static final class Placer {
        private final Registry<StructureTemplatePool> pools;
        private final int maxDepth;
        private final PieceFactory factory;
        private final ChunkGenerator chunkGenerator;
        private final StructureManager structureManager;
        private final List<? super PoolElementStructurePiece> pieces;
        private final Random random;
        private final Deque<PieceState> placing;
        
        private Placer(final Registry<StructureTemplatePool> gm, final int integer, final PieceFactory a, final ChunkGenerator cfv, final StructureManager cst, final List<? super PoolElementStructurePiece> list, final Random random) {
            this.placing = (Deque<PieceState>)Queues.newArrayDeque();
            this.pools = gm;
            this.maxDepth = integer;
            this.factory = a;
            this.chunkGenerator = cfv;
            this.structureManager = cst;
            this.pieces = list;
            this.random = random;
        }
        
        private void tryPlacingChildren(final PoolElementStructurePiece crl, final MutableObject<VoxelShape> mutableObject, final int integer3, final int integer4, final boolean boolean5) {
            final StructurePoolElement cof7 = crl.getElement();
            final BlockPos fx8 = crl.getPosition();
            final Rotation bzj9 = crl.getRotation();
            final StructureTemplatePool.Projection a10 = cof7.getProjection();
            final boolean boolean6 = a10 == StructureTemplatePool.Projection.RIGID;
            final MutableObject<VoxelShape> mutableObject2 = (MutableObject<VoxelShape>)new MutableObject();
            final BoundingBox cqx13 = crl.getBoundingBox();
            final int integer5 = cqx13.y0;
            for (final StructureTemplate.StructureBlockInfo c16 : cof7.getShuffledJigsawBlocks(this.structureManager, fx8, bzj9, this.random)) {
                final Direction gc17 = JigsawBlock.getFrontFacing(c16.state);
                final BlockPos fx9 = c16.pos;
                final BlockPos fx10 = fx9.relative(gc17);
                final int integer6 = fx9.getY() - integer5;
                int integer7 = -1;
                final ResourceLocation vk22 = new ResourceLocation(c16.nbt.getString("pool"));
                final Optional<StructureTemplatePool> optional23 = this.pools.getOptional(vk22);
                if (!optional23.isPresent() || (((StructureTemplatePool)optional23.get()).size() == 0 && !Objects.equals(vk22, Pools.EMPTY.location()))) {
                    JigsawPlacement.LOGGER.warn("Empty or none existent pool: {}", vk22);
                }
                else {
                    final ResourceLocation vk23 = ((StructureTemplatePool)optional23.get()).getFallback();
                    final Optional<StructureTemplatePool> optional24 = this.pools.getOptional(vk23);
                    if (!optional24.isPresent() || (((StructureTemplatePool)optional24.get()).size() == 0 && !Objects.equals(vk23, Pools.EMPTY.location()))) {
                        JigsawPlacement.LOGGER.warn("Empty or none existent fallback pool: {}", vk23);
                    }
                    else {
                        final boolean boolean7 = cqx13.isInside(fx10);
                        MutableObject<VoxelShape> mutableObject3;
                        int integer8;
                        if (boolean7) {
                            mutableObject3 = mutableObject2;
                            integer8 = integer5;
                            if (mutableObject2.getValue() == null) {
                                mutableObject2.setValue(Shapes.create(AABB.of(cqx13)));
                            }
                        }
                        else {
                            mutableObject3 = mutableObject;
                            integer8 = integer3;
                        }
                        final List<StructurePoolElement> list29 = (List<StructurePoolElement>)Lists.newArrayList();
                        if (integer4 != this.maxDepth) {
                            list29.addAll((Collection)((StructureTemplatePool)optional23.get()).getShuffledTemplates(this.random));
                        }
                        list29.addAll((Collection)((StructureTemplatePool)optional24.get()).getShuffledTemplates(this.random));
                    Label_1174:
                        for (final StructurePoolElement cof8 : list29) {
                            if (cof8 == EmptyPoolElement.INSTANCE) {
                                break;
                            }
                            for (final Rotation bzj10 : Rotation.getShuffled(this.random)) {
                                final List<StructureTemplate.StructureBlockInfo> list30 = cof8.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, bzj10, this.random);
                                final BoundingBox cqx14 = cof8.getBoundingBox(this.structureManager, BlockPos.ZERO, bzj10);
                                int integer9;
                                if (!boolean5 || cqx14.getYSpan() > 16) {
                                    integer9 = 0;
                                }
                                else {
                                    integer9 = list30.stream().mapToInt(c -> {
                                        if (!cqx14.isInside(c.pos.relative(JigsawBlock.getFrontFacing(c.state)))) {
                                            return 0;
                                        }
                                        final ResourceLocation vk4 = new ResourceLocation(c.nbt.getString("pool"));
                                        final Optional<StructureTemplatePool> optional5 = this.pools.getOptional(vk4);
                                        final Optional<StructureTemplatePool> optional6 = (Optional<StructureTemplatePool>)optional5.flatMap(coh -> this.pools.getOptional(coh.getFallback()));
                                        final int integer7 = (int)optional5.map(coh -> coh.getMaxSize(this.structureManager)).orElse(0);
                                        final int integer8 = (int)optional6.map(coh -> coh.getMaxSize(this.structureManager)).orElse(0);
                                        return Math.max(integer7, integer8);
                                    }).max().orElse(0);
                                }
                                for (final StructureTemplate.StructureBlockInfo c17 : list30) {
                                    if (!JigsawBlock.canAttach(c16, c17)) {
                                        continue;
                                    }
                                    final BlockPos fx11 = c17.pos;
                                    final BlockPos fx12 = new BlockPos(fx10.getX() - fx11.getX(), fx10.getY() - fx11.getY(), fx10.getZ() - fx11.getZ());
                                    final BoundingBox cqx15 = cof8.getBoundingBox(this.structureManager, fx12, bzj10);
                                    final int integer10 = cqx15.y0;
                                    final StructureTemplatePool.Projection a11 = cof8.getProjection();
                                    final boolean boolean8 = a11 == StructureTemplatePool.Projection.RIGID;
                                    final int integer11 = fx11.getY();
                                    final int integer12 = integer6 - integer11 + JigsawBlock.getFrontFacing(c16.state).getStepY();
                                    int integer13;
                                    if (boolean6 && boolean8) {
                                        integer13 = integer5 + integer12;
                                    }
                                    else {
                                        if (integer7 == -1) {
                                            integer7 = this.chunkGenerator.getFirstFreeHeight(fx9.getX(), fx9.getZ(), Heightmap.Types.WORLD_SURFACE_WG);
                                        }
                                        integer13 = integer7 - integer11;
                                    }
                                    final int integer14 = integer13 - integer10;
                                    final BoundingBox cqx16 = cqx15.moved(0, integer14, 0);
                                    final BlockPos fx13 = fx12.offset(0, integer14, 0);
                                    if (integer9 > 0) {
                                        final int integer15 = Math.max(integer9 + 1, cqx16.y1 - cqx16.y0);
                                        cqx16.y1 = cqx16.y0 + integer15;
                                    }
                                    if (Shapes.joinIsNotEmpty((VoxelShape)mutableObject3.getValue(), Shapes.create(AABB.of(cqx16).deflate(0.25)), BooleanOp.ONLY_SECOND)) {
                                        continue;
                                    }
                                    mutableObject3.setValue(Shapes.joinUnoptimized((VoxelShape)mutableObject3.getValue(), Shapes.create(AABB.of(cqx16)), BooleanOp.ONLY_FIRST));
                                    final int integer15 = crl.getGroundLevelDelta();
                                    int integer16;
                                    if (boolean8) {
                                        integer16 = integer15 - integer12;
                                    }
                                    else {
                                        integer16 = cof8.getGroundLevelDelta();
                                    }
                                    final PoolElementStructurePiece crl2 = this.factory.create(this.structureManager, cof8, fx13, integer16, bzj10, cqx16);
                                    int integer17;
                                    if (boolean6) {
                                        integer17 = integer5 + integer6;
                                    }
                                    else if (boolean8) {
                                        integer17 = integer13 + integer11;
                                    }
                                    else {
                                        if (integer7 == -1) {
                                            integer7 = this.chunkGenerator.getFirstFreeHeight(fx9.getX(), fx9.getZ(), Heightmap.Types.WORLD_SURFACE_WG);
                                        }
                                        integer17 = integer7 + integer12 / 2;
                                    }
                                    crl.addJunction(new JigsawJunction(fx10.getX(), integer17 - integer6 + integer15, fx10.getZ(), integer12, a11));
                                    crl2.addJunction(new JigsawJunction(fx9.getX(), integer17 - integer11 + integer16, fx9.getZ(), -integer12, a10));
                                    this.pieces.add(crl2);
                                    if (integer4 + 1 <= this.maxDepth) {
                                        this.placing.addLast(new PieceState(crl2, (MutableObject)mutableObject3, integer8, integer4 + 1));
                                        break Label_1174;
                                    }
                                    break Label_1174;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    public interface PieceFactory {
        PoolElementStructurePiece create(final StructureManager cst, final StructurePoolElement cof, final BlockPos fx, final int integer, final Rotation bzj, final BoundingBox cqx);
    }
}
