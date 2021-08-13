package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.stream.Collectors;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.core.IdMapper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Mob;
import net.minecraft.nbt.IntTag;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.EntityType;
import java.util.Optional;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.LevelAccessor;
import java.util.Random;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.Collections;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.phys.Vec3;
import java.util.function.Predicate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Entity;
import java.util.Collection;
import java.util.Comparator;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Vec3i;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import java.util.List;

public class StructureTemplate {
    private final List<Palette> palettes;
    private final List<StructureEntityInfo> entityInfoList;
    private BlockPos size;
    private String author;
    
    public StructureTemplate() {
        this.palettes = (List<Palette>)Lists.newArrayList();
        this.entityInfoList = (List<StructureEntityInfo>)Lists.newArrayList();
        this.size = BlockPos.ZERO;
        this.author = "?";
    }
    
    public BlockPos getSize() {
        return this.size;
    }
    
    public void setAuthor(final String string) {
        this.author = string;
    }
    
    public String getAuthor() {
        return this.author;
    }
    
    public void fillFromWorld(final Level bru, final BlockPos fx2, final BlockPos fx3, final boolean boolean4, @Nullable final Block bul) {
        if (fx3.getX() < 1 || fx3.getY() < 1 || fx3.getZ() < 1) {
            return;
        }
        final BlockPos fx4 = fx2.offset(fx3).offset(-1, -1, -1);
        final List<StructureBlockInfo> list8 = (List<StructureBlockInfo>)Lists.newArrayList();
        final List<StructureBlockInfo> list9 = (List<StructureBlockInfo>)Lists.newArrayList();
        final List<StructureBlockInfo> list10 = (List<StructureBlockInfo>)Lists.newArrayList();
        final BlockPos fx5 = new BlockPos(Math.min(fx2.getX(), fx4.getX()), Math.min(fx2.getY(), fx4.getY()), Math.min(fx2.getZ(), fx4.getZ()));
        final BlockPos fx6 = new BlockPos(Math.max(fx2.getX(), fx4.getX()), Math.max(fx2.getY(), fx4.getY()), Math.max(fx2.getZ(), fx4.getZ()));
        this.size = fx3;
        for (final BlockPos fx7 : BlockPos.betweenClosed(fx5, fx6)) {
            final BlockPos fx8 = fx7.subtract(fx5);
            final BlockState cee16 = bru.getBlockState(fx7);
            if (bul != null && bul == cee16.getBlock()) {
                continue;
            }
            final BlockEntity ccg17 = bru.getBlockEntity(fx7);
            StructureBlockInfo c18;
            if (ccg17 != null) {
                final CompoundTag md19 = ccg17.save(new CompoundTag());
                md19.remove("x");
                md19.remove("y");
                md19.remove("z");
                c18 = new StructureBlockInfo(fx8, cee16, md19.copy());
            }
            else {
                c18 = new StructureBlockInfo(fx8, cee16, null);
            }
            addToLists(c18, list8, list9, list10);
        }
        final List<StructureBlockInfo> list11 = buildInfoList(list8, list9, list10);
        this.palettes.clear();
        this.palettes.add(new Palette((List)list11));
        if (boolean4) {
            this.fillEntityList(bru, fx5, fx6.offset(1, 1, 1));
        }
        else {
            this.entityInfoList.clear();
        }
    }
    
    private static void addToLists(final StructureBlockInfo c, final List<StructureBlockInfo> list2, final List<StructureBlockInfo> list3, final List<StructureBlockInfo> list4) {
        if (c.nbt != null) {
            list3.add(c);
        }
        else if (!c.state.getBlock().hasDynamicShape() && c.state.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)) {
            list2.add(c);
        }
        else {
            list4.add(c);
        }
    }
    
    private static List<StructureBlockInfo> buildInfoList(final List<StructureBlockInfo> list1, final List<StructureBlockInfo> list2, final List<StructureBlockInfo> list3) {
        final Comparator<StructureBlockInfo> comparator4 = (Comparator<StructureBlockInfo>)Comparator.comparingInt(c -> c.pos.getY()).thenComparingInt(c -> c.pos.getX()).thenComparingInt(c -> c.pos.getZ());
        list1.sort((Comparator)comparator4);
        list3.sort((Comparator)comparator4);
        list2.sort((Comparator)comparator4);
        final List<StructureBlockInfo> list4 = (List<StructureBlockInfo>)Lists.newArrayList();
        list4.addAll((Collection)list1);
        list4.addAll((Collection)list3);
        list4.addAll((Collection)list2);
        return list4;
    }
    
    private void fillEntityList(final Level bru, final BlockPos fx2, final BlockPos fx3) {
        final List<Entity> list5 = bru.<Entity>getEntitiesOfClass((java.lang.Class<? extends Entity>)Entity.class, new AABB(fx2, fx3), (java.util.function.Predicate<? super Entity>)(apx -> !(apx instanceof Player)));
        this.entityInfoList.clear();
        for (final Entity apx7 : list5) {
            final Vec3 dck8 = new Vec3(apx7.getX() - fx2.getX(), apx7.getY() - fx2.getY(), apx7.getZ() - fx2.getZ());
            final CompoundTag md9 = new CompoundTag();
            apx7.save(md9);
            BlockPos fx4;
            if (apx7 instanceof Painting) {
                fx4 = ((Painting)apx7).getPos().subtract(fx2);
            }
            else {
                fx4 = new BlockPos(dck8);
            }
            this.entityInfoList.add(new StructureEntityInfo(dck8, fx4, md9.copy()));
        }
    }
    
    public List<StructureBlockInfo> filterBlocks(final BlockPos fx, final StructurePlaceSettings csu, final Block bul) {
        return this.filterBlocks(fx, csu, bul, true);
    }
    
    public List<StructureBlockInfo> filterBlocks(final BlockPos fx, final StructurePlaceSettings csu, final Block bul, final boolean boolean4) {
        final List<StructureBlockInfo> list6 = (List<StructureBlockInfo>)Lists.newArrayList();
        final BoundingBox cqx7 = csu.getBoundingBox();
        if (this.palettes.isEmpty()) {
            return (List<StructureBlockInfo>)Collections.emptyList();
        }
        for (final StructureBlockInfo c9 : csu.getRandomPalette(this.palettes, fx).blocks(bul)) {
            final BlockPos fx2 = boolean4 ? calculateRelativePosition(csu, c9.pos).offset(fx) : c9.pos;
            if (cqx7 != null && !cqx7.isInside(fx2)) {
                continue;
            }
            list6.add(new StructureBlockInfo(fx2, c9.state.rotate(csu.getRotation()), c9.nbt));
        }
        return list6;
    }
    
    public BlockPos calculateConnectedPosition(final StructurePlaceSettings csu1, final BlockPos fx2, final StructurePlaceSettings csu3, final BlockPos fx4) {
        final BlockPos fx5 = calculateRelativePosition(csu1, fx2);
        final BlockPos fx6 = calculateRelativePosition(csu3, fx4);
        return fx5.subtract(fx6);
    }
    
    public static BlockPos calculateRelativePosition(final StructurePlaceSettings csu, final BlockPos fx) {
        return transform(fx, csu.getMirror(), csu.getRotation(), csu.getRotationPivot());
    }
    
    public void placeInWorldChunk(final ServerLevelAccessor bsh, final BlockPos fx, final StructurePlaceSettings csu, final Random random) {
        csu.updateBoundingBoxFromChunkPos();
        this.placeInWorld(bsh, fx, csu, random);
    }
    
    public void placeInWorld(final ServerLevelAccessor bsh, final BlockPos fx, final StructurePlaceSettings csu, final Random random) {
        this.placeInWorld(bsh, fx, fx, csu, random, 2);
    }
    
    public boolean placeInWorld(final ServerLevelAccessor bsh, final BlockPos fx2, final BlockPos fx3, final StructurePlaceSettings csu, final Random random, final int integer) {
        if (this.palettes.isEmpty()) {
            return false;
        }
        final List<StructureBlockInfo> list8 = csu.getRandomPalette(this.palettes, fx2).blocks();
        if ((list8.isEmpty() && (csu.isIgnoreEntities() || this.entityInfoList.isEmpty())) || this.size.getX() < 1 || this.size.getY() < 1 || this.size.getZ() < 1) {
            return false;
        }
        final BoundingBox cqx9 = csu.getBoundingBox();
        final List<BlockPos> list9 = (List<BlockPos>)Lists.newArrayListWithCapacity(csu.shouldKeepLiquids() ? list8.size() : 0);
        final List<Pair<BlockPos, CompoundTag>> list10 = (List<Pair<BlockPos, CompoundTag>>)Lists.newArrayListWithCapacity(list8.size());
        int integer2 = Integer.MAX_VALUE;
        int integer3 = Integer.MAX_VALUE;
        int integer4 = Integer.MAX_VALUE;
        int integer5 = Integer.MIN_VALUE;
        int integer6 = Integer.MIN_VALUE;
        int integer7 = Integer.MIN_VALUE;
        final List<StructureBlockInfo> list11 = processBlockInfos(bsh, fx2, fx3, csu, list8);
        for (final StructureBlockInfo c20 : list11) {
            final BlockPos fx4 = c20.pos;
            if (cqx9 != null && !cqx9.isInside(fx4)) {
                continue;
            }
            final FluidState cuu22 = csu.shouldKeepLiquids() ? bsh.getFluidState(fx4) : null;
            final BlockState cee23 = c20.state.mirror(csu.getMirror()).rotate(csu.getRotation());
            if (c20.nbt != null) {
                final BlockEntity ccg24 = bsh.getBlockEntity(fx4);
                Clearable.tryClear(ccg24);
                bsh.setBlock(fx4, Blocks.BARRIER.defaultBlockState(), 20);
            }
            if (!bsh.setBlock(fx4, cee23, integer)) {
                continue;
            }
            integer2 = Math.min(integer2, fx4.getX());
            integer3 = Math.min(integer3, fx4.getY());
            integer4 = Math.min(integer4, fx4.getZ());
            integer5 = Math.max(integer5, fx4.getX());
            integer6 = Math.max(integer6, fx4.getY());
            integer7 = Math.max(integer7, fx4.getZ());
            list10.add(Pair.of((Object)fx4, (Object)c20.nbt));
            if (c20.nbt != null) {
                final BlockEntity ccg24 = bsh.getBlockEntity(fx4);
                if (ccg24 != null) {
                    c20.nbt.putInt("x", fx4.getX());
                    c20.nbt.putInt("y", fx4.getY());
                    c20.nbt.putInt("z", fx4.getZ());
                    if (ccg24 instanceof RandomizableContainerBlockEntity) {
                        c20.nbt.putLong("LootTableSeed", random.nextLong());
                    }
                    ccg24.load(c20.state, c20.nbt);
                    ccg24.mirror(csu.getMirror());
                    ccg24.rotate(csu.getRotation());
                }
            }
            if (cuu22 == null || !(cee23.getBlock() instanceof LiquidBlockContainer)) {
                continue;
            }
            ((LiquidBlockContainer)cee23.getBlock()).placeLiquid(bsh, fx4, cee23, cuu22);
            if (cuu22.isSource()) {
                continue;
            }
            list9.add(fx4);
        }
        boolean boolean19 = true;
        final Direction[] arr20 = { Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
        while (boolean19 && !list9.isEmpty()) {
            boolean19 = false;
            final Iterator<BlockPos> iterator21 = (Iterator<BlockPos>)list9.iterator();
            while (iterator21.hasNext()) {
                BlockPos fx6;
                final BlockPos fx5 = fx6 = (BlockPos)iterator21.next();
                FluidState cuu23 = bsh.getFluidState(fx6);
                for (int integer8 = 0; integer8 < arr20.length && !cuu23.isSource(); ++integer8) {
                    final BlockPos fx7 = fx6.relative(arr20[integer8]);
                    final FluidState cuu24 = bsh.getFluidState(fx7);
                    if (cuu24.getHeight(bsh, fx7) > cuu23.getHeight(bsh, fx6) || (cuu24.isSource() && !cuu23.isSource())) {
                        cuu23 = cuu24;
                        fx6 = fx7;
                    }
                }
                if (cuu23.isSource()) {
                    final BlockState cee24 = bsh.getBlockState(fx5);
                    final Block bul26 = cee24.getBlock();
                    if (!(bul26 instanceof LiquidBlockContainer)) {
                        continue;
                    }
                    ((LiquidBlockContainer)bul26).placeLiquid(bsh, fx5, cee24, cuu23);
                    boolean19 = true;
                    iterator21.remove();
                }
            }
        }
        if (integer2 <= integer5) {
            if (!csu.getKnownShape()) {
                final DiscreteVoxelShape dct21 = new BitSetDiscreteVoxelShape(integer5 - integer2 + 1, integer6 - integer3 + 1, integer7 - integer4 + 1);
                final int integer9 = integer2;
                final int integer10 = integer3;
                final int integer11 = integer4;
                for (final Pair<BlockPos, CompoundTag> pair26 : list10) {
                    final BlockPos fx8 = (BlockPos)pair26.getFirst();
                    dct21.setFull(fx8.getX() - integer9, fx8.getY() - integer10, fx8.getZ() - integer11, true, true);
                }
                updateShapeAtEdge(bsh, integer, dct21, integer9, integer10, integer11);
            }
            for (final Pair<BlockPos, CompoundTag> pair27 : list10) {
                final BlockPos fx6 = (BlockPos)pair27.getFirst();
                if (!csu.getKnownShape()) {
                    final BlockState cee25 = bsh.getBlockState(fx6);
                    final BlockState cee24 = Block.updateFromNeighbourShapes(cee25, bsh, fx6);
                    if (cee25 != cee24) {
                        bsh.setBlock(fx6, cee24, (integer & 0xFFFFFFFE) | 0x10);
                    }
                    bsh.blockUpdated(fx6, cee24.getBlock());
                }
                if (pair27.getSecond() != null) {
                    final BlockEntity ccg24 = bsh.getBlockEntity(fx6);
                    if (ccg24 == null) {
                        continue;
                    }
                    ccg24.setChanged();
                }
            }
        }
        if (!csu.isIgnoreEntities()) {
            this.placeEntities(bsh, fx2, csu.getMirror(), csu.getRotation(), csu.getRotationPivot(), cqx9, csu.shouldFinalizeEntities());
        }
        return true;
    }
    
    public static void updateShapeAtEdge(final LevelAccessor brv, final int integer2, final DiscreteVoxelShape dct, final int integer4, final int integer5, final int integer6) {
        final BlockPos fx10;
        final BlockPos fx11;
        final BlockState cee12;
        final BlockState cee13;
        final BlockState cee14;
        final BlockState cee15;
        dct.forAllFaces((gc, integer7, integer8, integer9) -> {
            fx10 = new BlockPos(integer4 + integer7, integer5 + integer8, integer6 + integer9);
            fx11 = fx10.relative(gc);
            cee12 = brv.getBlockState(fx10);
            cee13 = brv.getBlockState(fx11);
            cee14 = cee12.updateShape(gc, cee13, brv, fx10, fx11);
            if (cee12 != cee14) {
                brv.setBlock(fx10, cee14, integer2 & 0xFFFFFFFE);
            }
            cee15 = cee13.updateShape(gc.getOpposite(), cee14, brv, fx11, fx10);
            if (cee13 != cee15) {
                brv.setBlock(fx11, cee15, integer2 & 0xFFFFFFFE);
            }
        });
    }
    
    public static List<StructureBlockInfo> processBlockInfos(final LevelAccessor brv, final BlockPos fx2, final BlockPos fx3, final StructurePlaceSettings csu, final List<StructureBlockInfo> list) {
        final List<StructureBlockInfo> list2 = (List<StructureBlockInfo>)Lists.newArrayList();
        for (final StructureBlockInfo c8 : list) {
            final BlockPos fx4 = calculateRelativePosition(csu, c8.pos).offset(fx2);
            StructureBlockInfo c9 = new StructureBlockInfo(fx4, c8.state, (c8.nbt != null) ? c8.nbt.copy() : null);
            for (Iterator<StructureProcessor> iterator11 = (Iterator<StructureProcessor>)csu.getProcessors().iterator(); c9 != null && iterator11.hasNext(); c9 = ((StructureProcessor)iterator11.next()).processBlock(brv, fx2, fx3, c8, c9, csu)) {}
            if (c9 != null) {
                list2.add(c9);
            }
        }
        return list2;
    }
    
    private void placeEntities(final ServerLevelAccessor bsh, final BlockPos fx2, final Mirror byd, final Rotation bzj, final BlockPos fx5, @Nullable final BoundingBox cqx, final boolean boolean7) {
        for (final StructureEntityInfo d10 : this.entityInfoList) {
            final BlockPos fx6 = transform(d10.blockPos, byd, bzj, fx5).offset(fx2);
            if (cqx != null && !cqx.isInside(fx6)) {
                continue;
            }
            final CompoundTag md12 = d10.nbt.copy();
            final Vec3 dck13 = transform(d10.pos, byd, bzj, fx5);
            final Vec3 dck14 = dck13.add(fx2.getX(), fx2.getY(), fx2.getZ());
            final ListTag mj15 = new ListTag();
            mj15.add(DoubleTag.valueOf(dck14.x));
            mj15.add(DoubleTag.valueOf(dck14.y));
            mj15.add(DoubleTag.valueOf(dck14.z));
            md12.put("Pos", (Tag)mj15);
            md12.remove("UUID");
            createEntityIgnoreException(bsh, md12).ifPresent(apx -> {
                float float8 = apx.mirror(byd);
                float8 += apx.yRot - apx.rotate(bzj);
                apx.moveTo(dck14.x, dck14.y, dck14.z, float8, apx.xRot);
                if (boolean7 && apx instanceof Mob) {
                    ((Mob)apx).finalizeSpawn(bsh, bsh.getCurrentDifficultyAt(new BlockPos(dck14)), MobSpawnType.STRUCTURE, null, md12);
                }
                bsh.addFreshEntityWithPassengers(apx);
            });
        }
    }
    
    private static Optional<Entity> createEntityIgnoreException(final ServerLevelAccessor bsh, final CompoundTag md) {
        try {
            return EntityType.create(md, bsh.getLevel());
        }
        catch (Exception exception3) {
            return (Optional<Entity>)Optional.empty();
        }
    }
    
    public BlockPos getSize(final Rotation bzj) {
        switch (bzj) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90: {
                return new BlockPos(this.size.getZ(), this.size.getY(), this.size.getX());
            }
            default: {
                return this.size;
            }
        }
    }
    
    public static BlockPos transform(final BlockPos fx1, final Mirror byd, final Rotation bzj, final BlockPos fx4) {
        int integer5 = fx1.getX();
        final int integer6 = fx1.getY();
        int integer7 = fx1.getZ();
        boolean boolean8 = true;
        switch (byd) {
            case LEFT_RIGHT: {
                integer7 = -integer7;
                break;
            }
            case FRONT_BACK: {
                integer5 = -integer5;
                break;
            }
            default: {
                boolean8 = false;
                break;
            }
        }
        final int integer8 = fx4.getX();
        final int integer9 = fx4.getZ();
        switch (bzj) {
            case CLOCKWISE_180: {
                return new BlockPos(integer8 + integer8 - integer5, integer6, integer9 + integer9 - integer7);
            }
            case COUNTERCLOCKWISE_90: {
                return new BlockPos(integer8 - integer9 + integer7, integer6, integer8 + integer9 - integer5);
            }
            case CLOCKWISE_90: {
                return new BlockPos(integer8 + integer9 - integer7, integer6, integer9 - integer8 + integer5);
            }
            default: {
                return boolean8 ? new BlockPos(integer5, integer6, integer7) : fx1;
            }
        }
    }
    
    public static Vec3 transform(final Vec3 dck, final Mirror byd, final Rotation bzj, final BlockPos fx) {
        double double5 = dck.x;
        final double double6 = dck.y;
        double double7 = dck.z;
        boolean boolean11 = true;
        switch (byd) {
            case LEFT_RIGHT: {
                double7 = 1.0 - double7;
                break;
            }
            case FRONT_BACK: {
                double5 = 1.0 - double5;
                break;
            }
            default: {
                boolean11 = false;
                break;
            }
        }
        final int integer12 = fx.getX();
        final int integer13 = fx.getZ();
        switch (bzj) {
            case CLOCKWISE_180: {
                return new Vec3(integer12 + integer12 + 1 - double5, double6, integer13 + integer13 + 1 - double7);
            }
            case COUNTERCLOCKWISE_90: {
                return new Vec3(integer12 - integer13 + double7, double6, integer12 + integer13 + 1 - double5);
            }
            case CLOCKWISE_90: {
                return new Vec3(integer12 + integer13 + 1 - double7, double6, integer13 - integer12 + double5);
            }
            default: {
                return boolean11 ? new Vec3(double5, double6, double7) : dck;
            }
        }
    }
    
    public BlockPos getZeroPositionWithTransform(final BlockPos fx, final Mirror byd, final Rotation bzj) {
        return getZeroPositionWithTransform(fx, byd, bzj, this.getSize().getX(), this.getSize().getZ());
    }
    
    public static BlockPos getZeroPositionWithTransform(final BlockPos fx, final Mirror byd, final Rotation bzj, int integer4, int integer5) {
        --integer4;
        --integer5;
        final int integer6 = (byd == Mirror.FRONT_BACK) ? integer4 : 0;
        final int integer7 = (byd == Mirror.LEFT_RIGHT) ? integer5 : 0;
        BlockPos fx2 = fx;
        switch (bzj) {
            case NONE: {
                fx2 = fx.offset(integer6, 0, integer7);
                break;
            }
            case CLOCKWISE_90: {
                fx2 = fx.offset(integer5 - integer7, 0, integer6);
                break;
            }
            case CLOCKWISE_180: {
                fx2 = fx.offset(integer4 - integer6, 0, integer5 - integer7);
                break;
            }
            case COUNTERCLOCKWISE_90: {
                fx2 = fx.offset(integer7, 0, integer4 - integer6);
                break;
            }
        }
        return fx2;
    }
    
    public BoundingBox getBoundingBox(final StructurePlaceSettings csu, final BlockPos fx) {
        return this.getBoundingBox(fx, csu.getRotation(), csu.getRotationPivot(), csu.getMirror());
    }
    
    public BoundingBox getBoundingBox(final BlockPos fx1, final Rotation bzj, final BlockPos fx3, final Mirror byd) {
        final BlockPos fx4 = this.getSize(bzj);
        final int integer7 = fx3.getX();
        final int integer8 = fx3.getZ();
        final int integer9 = fx4.getX() - 1;
        final int integer10 = fx4.getY() - 1;
        final int integer11 = fx4.getZ() - 1;
        BoundingBox cqx12 = new BoundingBox(0, 0, 0, 0, 0, 0);
        switch (bzj) {
            case NONE: {
                cqx12 = new BoundingBox(0, 0, 0, integer9, integer10, integer11);
                break;
            }
            case CLOCKWISE_180: {
                cqx12 = new BoundingBox(integer7 + integer7 - integer9, 0, integer8 + integer8 - integer11, integer7 + integer7, integer10, integer8 + integer8);
                break;
            }
            case COUNTERCLOCKWISE_90: {
                cqx12 = new BoundingBox(integer7 - integer8, 0, integer7 + integer8 - integer11, integer7 - integer8 + integer9, integer10, integer7 + integer8);
                break;
            }
            case CLOCKWISE_90: {
                cqx12 = new BoundingBox(integer7 + integer8 - integer9, 0, integer8 - integer7, integer7 + integer8, integer10, integer8 - integer7 + integer11);
                break;
            }
        }
        switch (byd) {
            case FRONT_BACK: {
                this.mirrorAABB(bzj, integer9, integer11, cqx12, Direction.WEST, Direction.EAST);
                break;
            }
            case LEFT_RIGHT: {
                this.mirrorAABB(bzj, integer11, integer9, cqx12, Direction.NORTH, Direction.SOUTH);
                break;
            }
        }
        cqx12.move(fx1.getX(), fx1.getY(), fx1.getZ());
        return cqx12;
    }
    
    private void mirrorAABB(final Rotation bzj, final int integer2, final int integer3, final BoundingBox cqx, final Direction gc5, final Direction gc6) {
        BlockPos fx8 = BlockPos.ZERO;
        if (bzj == Rotation.CLOCKWISE_90 || bzj == Rotation.COUNTERCLOCKWISE_90) {
            fx8 = fx8.relative(bzj.rotate(gc5), integer3);
        }
        else if (bzj == Rotation.CLOCKWISE_180) {
            fx8 = fx8.relative(gc6, integer2);
        }
        else {
            fx8 = fx8.relative(gc5, integer2);
        }
        cqx.move(fx8.getX(), 0, fx8.getZ());
    }
    
    public CompoundTag save(final CompoundTag md) {
        if (this.palettes.isEmpty()) {
            md.put("blocks", (Tag)new ListTag());
            md.put("palette", (Tag)new ListTag());
        }
        else {
            final List<SimplePalette> list3 = (List<SimplePalette>)Lists.newArrayList();
            final SimplePalette b4 = new SimplePalette();
            list3.add(b4);
            for (int integer5 = 1; integer5 < this.palettes.size(); ++integer5) {
                list3.add(new SimplePalette());
            }
            final ListTag mj5 = new ListTag();
            final List<StructureBlockInfo> list4 = ((Palette)this.palettes.get(0)).blocks();
            for (int integer6 = 0; integer6 < list4.size(); ++integer6) {
                final StructureBlockInfo c8 = (StructureBlockInfo)list4.get(integer6);
                final CompoundTag md2 = new CompoundTag();
                md2.put("pos", (Tag)this.newIntegerList(c8.pos.getX(), c8.pos.getY(), c8.pos.getZ()));
                final int integer7 = b4.idFor(c8.state);
                md2.putInt("state", integer7);
                if (c8.nbt != null) {
                    md2.put("nbt", (Tag)c8.nbt);
                }
                mj5.add(md2);
                for (int integer8 = 1; integer8 < this.palettes.size(); ++integer8) {
                    final SimplePalette b5 = (SimplePalette)list3.get(integer8);
                    b5.addMapping(((StructureBlockInfo)((Palette)this.palettes.get(integer8)).blocks().get(integer6)).state, integer7);
                }
            }
            md.put("blocks", (Tag)mj5);
            if (list3.size() == 1) {
                final ListTag mj6 = new ListTag();
                for (final BlockState cee9 : b4) {
                    mj6.add(NbtUtils.writeBlockState(cee9));
                }
                md.put("palette", (Tag)mj6);
            }
            else {
                final ListTag mj6 = new ListTag();
                for (final SimplePalette b6 : list3) {
                    final ListTag mj7 = new ListTag();
                    for (final BlockState cee10 : b6) {
                        mj7.add(NbtUtils.writeBlockState(cee10));
                    }
                    mj6.add(mj7);
                }
                md.put("palettes", (Tag)mj6);
            }
        }
        final ListTag mj8 = new ListTag();
        for (final StructureEntityInfo d5 : this.entityInfoList) {
            final CompoundTag md3 = new CompoundTag();
            md3.put("pos", (Tag)this.newDoubleList(d5.pos.x, d5.pos.y, d5.pos.z));
            md3.put("blockPos", (Tag)this.newIntegerList(d5.blockPos.getX(), d5.blockPos.getY(), d5.blockPos.getZ()));
            if (d5.nbt != null) {
                md3.put("nbt", (Tag)d5.nbt);
            }
            mj8.add(md3);
        }
        md.put("entities", (Tag)mj8);
        md.put("size", (Tag)this.newIntegerList(this.size.getX(), this.size.getY(), this.size.getZ()));
        md.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());
        return md;
    }
    
    public void load(final CompoundTag md) {
        this.palettes.clear();
        this.entityInfoList.clear();
        final ListTag mj3 = md.getList("size", 3);
        this.size = new BlockPos(mj3.getInt(0), mj3.getInt(1), mj3.getInt(2));
        final ListTag mj4 = md.getList("blocks", 10);
        if (md.contains("palettes", 9)) {
            final ListTag mj5 = md.getList("palettes", 9);
            for (int integer6 = 0; integer6 < mj5.size(); ++integer6) {
                this.loadPalette(mj5.getList(integer6), mj4);
            }
        }
        else {
            this.loadPalette(md.getList("palette", 10), mj4);
        }
        final ListTag mj5 = md.getList("entities", 10);
        for (int integer6 = 0; integer6 < mj5.size(); ++integer6) {
            final CompoundTag md2 = mj5.getCompound(integer6);
            final ListTag mj6 = md2.getList("pos", 6);
            final Vec3 dck9 = new Vec3(mj6.getDouble(0), mj6.getDouble(1), mj6.getDouble(2));
            final ListTag mj7 = md2.getList("blockPos", 3);
            final BlockPos fx11 = new BlockPos(mj7.getInt(0), mj7.getInt(1), mj7.getInt(2));
            if (md2.contains("nbt")) {
                final CompoundTag md3 = md2.getCompound("nbt");
                this.entityInfoList.add(new StructureEntityInfo(dck9, fx11, md3));
            }
        }
    }
    
    private void loadPalette(final ListTag mj1, final ListTag mj2) {
        final SimplePalette b4 = new SimplePalette();
        for (int integer5 = 0; integer5 < mj1.size(); ++integer5) {
            b4.addMapping(NbtUtils.readBlockState(mj1.getCompound(integer5)), integer5);
        }
        final List<StructureBlockInfo> list5 = (List<StructureBlockInfo>)Lists.newArrayList();
        final List<StructureBlockInfo> list6 = (List<StructureBlockInfo>)Lists.newArrayList();
        final List<StructureBlockInfo> list7 = (List<StructureBlockInfo>)Lists.newArrayList();
        for (int integer6 = 0; integer6 < mj2.size(); ++integer6) {
            final CompoundTag md9 = mj2.getCompound(integer6);
            final ListTag mj3 = md9.getList("pos", 3);
            final BlockPos fx11 = new BlockPos(mj3.getInt(0), mj3.getInt(1), mj3.getInt(2));
            final BlockState cee12 = b4.stateFor(md9.getInt("state"));
            CompoundTag md10;
            if (md9.contains("nbt")) {
                md10 = md9.getCompound("nbt");
            }
            else {
                md10 = null;
            }
            final StructureBlockInfo c14 = new StructureBlockInfo(fx11, cee12, md10);
            addToLists(c14, list5, list6, list7);
        }
        final List<StructureBlockInfo> list8 = buildInfoList(list5, list6, list7);
        this.palettes.add(new Palette((List)list8));
    }
    
    private ListTag newIntegerList(final int... arr) {
        final ListTag mj3 = new ListTag();
        for (final int integer7 : arr) {
            mj3.add(IntTag.valueOf(integer7));
        }
        return mj3;
    }
    
    private ListTag newDoubleList(final double... arr) {
        final ListTag mj3 = new ListTag();
        for (final double double7 : arr) {
            mj3.add(DoubleTag.valueOf(double7));
        }
        return mj3;
    }
    
    static class SimplePalette implements Iterable<BlockState> {
        public static final BlockState DEFAULT_BLOCK_STATE;
        private final IdMapper<BlockState> ids;
        private int lastId;
        
        private SimplePalette() {
            this.ids = new IdMapper<BlockState>(16);
        }
        
        public int idFor(final BlockState cee) {
            int integer3 = this.ids.getId(cee);
            if (integer3 == -1) {
                integer3 = this.lastId++;
                this.ids.addMapping(cee, integer3);
            }
            return integer3;
        }
        
        @Nullable
        public BlockState stateFor(final int integer) {
            final BlockState cee3 = this.ids.byId(integer);
            return (cee3 == null) ? SimplePalette.DEFAULT_BLOCK_STATE : cee3;
        }
        
        public Iterator<BlockState> iterator() {
            return this.ids.iterator();
        }
        
        public void addMapping(final BlockState cee, final int integer) {
            this.ids.addMapping(cee, integer);
        }
        
        static {
            DEFAULT_BLOCK_STATE = Blocks.AIR.defaultBlockState();
        }
    }
    
    public static class StructureBlockInfo {
        public final BlockPos pos;
        public final BlockState state;
        public final CompoundTag nbt;
        
        public StructureBlockInfo(final BlockPos fx, final BlockState cee, @Nullable final CompoundTag md) {
            this.pos = fx;
            this.state = cee;
            this.nbt = md;
        }
        
        public String toString() {
            return String.format("<StructureBlockInfo | %s | %s | %s>", new Object[] { this.pos, this.state, this.nbt });
        }
    }
    
    public static class StructureEntityInfo {
        public final Vec3 pos;
        public final BlockPos blockPos;
        public final CompoundTag nbt;
        
        public StructureEntityInfo(final Vec3 dck, final BlockPos fx, final CompoundTag md) {
            this.pos = dck;
            this.blockPos = fx;
            this.nbt = md;
        }
    }
    
    public static final class Palette {
        private final List<StructureBlockInfo> blocks;
        private final Map<Block, List<StructureBlockInfo>> cache;
        
        private Palette(final List<StructureBlockInfo> list) {
            this.cache = (Map<Block, List<StructureBlockInfo>>)Maps.newHashMap();
            this.blocks = list;
        }
        
        public List<StructureBlockInfo> blocks() {
            return this.blocks;
        }
        
        public List<StructureBlockInfo> blocks(final Block bul) {
            return (List<StructureBlockInfo>)this.cache.computeIfAbsent(bul, bul -> (List)this.blocks.stream().filter(c -> c.state.is(bul)).collect(Collectors.toList()));
        }
    }
}
