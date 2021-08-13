package net.minecraft.world.level.levelgen.structure.templatesystem;

import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.Util;
import java.util.Collection;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import javax.annotation.Nullable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;

public class StructurePlaceSettings {
    private Mirror mirror;
    private Rotation rotation;
    private BlockPos rotationPivot;
    private boolean ignoreEntities;
    @Nullable
    private ChunkPos chunkPos;
    @Nullable
    private BoundingBox boundingBox;
    private boolean keepLiquids;
    @Nullable
    private Random random;
    @Nullable
    private int palette;
    private final List<StructureProcessor> processors;
    private boolean knownShape;
    private boolean finalizeEntities;
    
    public StructurePlaceSettings() {
        this.mirror = Mirror.NONE;
        this.rotation = Rotation.NONE;
        this.rotationPivot = BlockPos.ZERO;
        this.keepLiquids = true;
        this.processors = (List<StructureProcessor>)Lists.newArrayList();
    }
    
    public StructurePlaceSettings copy() {
        final StructurePlaceSettings csu2 = new StructurePlaceSettings();
        csu2.mirror = this.mirror;
        csu2.rotation = this.rotation;
        csu2.rotationPivot = this.rotationPivot;
        csu2.ignoreEntities = this.ignoreEntities;
        csu2.chunkPos = this.chunkPos;
        csu2.boundingBox = this.boundingBox;
        csu2.keepLiquids = this.keepLiquids;
        csu2.random = this.random;
        csu2.palette = this.palette;
        csu2.processors.addAll((Collection)this.processors);
        csu2.knownShape = this.knownShape;
        csu2.finalizeEntities = this.finalizeEntities;
        return csu2;
    }
    
    public StructurePlaceSettings setMirror(final Mirror byd) {
        this.mirror = byd;
        return this;
    }
    
    public StructurePlaceSettings setRotation(final Rotation bzj) {
        this.rotation = bzj;
        return this;
    }
    
    public StructurePlaceSettings setRotationPivot(final BlockPos fx) {
        this.rotationPivot = fx;
        return this;
    }
    
    public StructurePlaceSettings setIgnoreEntities(final boolean boolean1) {
        this.ignoreEntities = boolean1;
        return this;
    }
    
    public StructurePlaceSettings setChunkPos(final ChunkPos bra) {
        this.chunkPos = bra;
        return this;
    }
    
    public StructurePlaceSettings setBoundingBox(final BoundingBox cqx) {
        this.boundingBox = cqx;
        return this;
    }
    
    public StructurePlaceSettings setRandom(@Nullable final Random random) {
        this.random = random;
        return this;
    }
    
    public StructurePlaceSettings setKnownShape(final boolean boolean1) {
        this.knownShape = boolean1;
        return this;
    }
    
    public StructurePlaceSettings clearProcessors() {
        this.processors.clear();
        return this;
    }
    
    public StructurePlaceSettings addProcessor(final StructureProcessor csv) {
        this.processors.add(csv);
        return this;
    }
    
    public StructurePlaceSettings popProcessor(final StructureProcessor csv) {
        this.processors.remove(csv);
        return this;
    }
    
    public Mirror getMirror() {
        return this.mirror;
    }
    
    public Rotation getRotation() {
        return this.rotation;
    }
    
    public BlockPos getRotationPivot() {
        return this.rotationPivot;
    }
    
    public Random getRandom(@Nullable final BlockPos fx) {
        if (this.random != null) {
            return this.random;
        }
        if (fx == null) {
            return new Random(Util.getMillis());
        }
        return new Random(Mth.getSeed(fx));
    }
    
    public boolean isIgnoreEntities() {
        return this.ignoreEntities;
    }
    
    @Nullable
    public BoundingBox getBoundingBox() {
        if (this.boundingBox == null && this.chunkPos != null) {
            this.updateBoundingBoxFromChunkPos();
        }
        return this.boundingBox;
    }
    
    public boolean getKnownShape() {
        return this.knownShape;
    }
    
    public List<StructureProcessor> getProcessors() {
        return this.processors;
    }
    
    void updateBoundingBoxFromChunkPos() {
        if (this.chunkPos != null) {
            this.boundingBox = this.calculateBoundingBox(this.chunkPos);
        }
    }
    
    public boolean shouldKeepLiquids() {
        return this.keepLiquids;
    }
    
    public StructureTemplate.Palette getRandomPalette(final List<StructureTemplate.Palette> list, @Nullable final BlockPos fx) {
        final int integer4 = list.size();
        if (integer4 == 0) {
            throw new IllegalStateException("No palettes");
        }
        return (StructureTemplate.Palette)list.get(this.getRandom(fx).nextInt(integer4));
    }
    
    @Nullable
    private BoundingBox calculateBoundingBox(@Nullable final ChunkPos bra) {
        if (bra == null) {
            return this.boundingBox;
        }
        final int integer3 = bra.x * 16;
        final int integer4 = bra.z * 16;
        return new BoundingBox(integer3, 0, integer4, integer3 + 16 - 1, 255, integer4 + 16 - 1);
    }
    
    public StructurePlaceSettings setFinalizeEntities(final boolean boolean1) {
        this.finalizeEntities = boolean1;
        return this;
    }
    
    public boolean shouldFinalizeEntities() {
        return this.finalizeEntities;
    }
}
