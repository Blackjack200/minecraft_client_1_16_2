package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.levelgen.feature.configurations.MineshaftConfiguration;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import java.util.Iterator;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import java.util.Random;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.core.RegistryAccess;
import com.google.common.collect.Lists;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import java.util.List;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public abstract class StructureStart<C extends FeatureConfiguration> {
    public static final StructureStart<?> INVALID_START;
    private final StructureFeature<C> feature;
    protected final List<StructurePiece> pieces;
    protected BoundingBox boundingBox;
    private final int chunkX;
    private final int chunkZ;
    private int references;
    protected final WorldgenRandom random;
    
    public StructureStart(final StructureFeature<C> ckx, final int integer2, final int integer3, final BoundingBox cqx, final int integer5, final long long6) {
        this.pieces = (List<StructurePiece>)Lists.newArrayList();
        this.feature = ckx;
        this.chunkX = integer2;
        this.chunkZ = integer3;
        this.references = integer5;
        (this.random = new WorldgenRandom()).setLargeFeatureSeed(long6, integer2, integer3);
        this.boundingBox = cqx;
    }
    
    public abstract void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final C clx);
    
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }
    
    public List<StructurePiece> getPieces() {
        return this.pieces;
    }
    
    public void placeInChunk(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra) {
        synchronized (this.pieces) {
            if (this.pieces.isEmpty()) {
                return;
            }
            final BoundingBox cqx2 = ((StructurePiece)this.pieces.get(0)).boundingBox;
            final Vec3i gr10 = cqx2.getCenter();
            final BlockPos fx11 = new BlockPos(gr10.getX(), cqx2.y0, gr10.getZ());
            final Iterator<StructurePiece> iterator12 = (Iterator<StructurePiece>)this.pieces.iterator();
            while (iterator12.hasNext()) {
                final StructurePiece crr13 = (StructurePiece)iterator12.next();
                if (crr13.getBoundingBox().intersects(cqx) && !crr13.postProcess(bso, bsk, cfv, random, cqx, bra, fx11)) {
                    iterator12.remove();
                }
            }
            this.calculateBoundingBox();
        }
    }
    
    protected void calculateBoundingBox() {
        this.boundingBox = BoundingBox.getUnknownBox();
        for (final StructurePiece crr3 : this.pieces) {
            this.boundingBox.expand(crr3.getBoundingBox());
        }
    }
    
    public CompoundTag createTag(final int integer1, final int integer2) {
        final CompoundTag md4 = new CompoundTag();
        if (this.isValid()) {
            md4.putString("id", Registry.STRUCTURE_FEATURE.getKey(this.getFeature()).toString());
            md4.putInt("ChunkX", integer1);
            md4.putInt("ChunkZ", integer2);
            md4.putInt("references", this.references);
            md4.put("BB", (Tag)this.boundingBox.createTag());
            final ListTag mj5 = new ListTag();
            synchronized (this.pieces) {
                for (final StructurePiece crr8 : this.pieces) {
                    mj5.add(crr8.createTag());
                }
            }
            md4.put("Children", (Tag)mj5);
            return md4;
        }
        md4.putString("id", "INVALID");
        return md4;
    }
    
    protected void moveBelowSeaLevel(final int integer1, final Random random, final int integer3) {
        final int integer4 = integer1 - integer3;
        int integer5 = this.boundingBox.getYSpan() + 1;
        if (integer5 < integer4) {
            integer5 += random.nextInt(integer4 - integer5);
        }
        final int integer6 = integer5 - this.boundingBox.y1;
        this.boundingBox.move(0, integer6, 0);
        for (final StructurePiece crr9 : this.pieces) {
            crr9.move(0, integer6, 0);
        }
    }
    
    protected void moveInsideHeights(final Random random, final int integer2, final int integer3) {
        final int integer4 = integer3 - integer2 + 1 - this.boundingBox.getYSpan();
        int integer5;
        if (integer4 > 1) {
            integer5 = integer2 + random.nextInt(integer4);
        }
        else {
            integer5 = integer2;
        }
        final int integer6 = integer5 - this.boundingBox.y0;
        this.boundingBox.move(0, integer6, 0);
        for (final StructurePiece crr9 : this.pieces) {
            crr9.move(0, integer6, 0);
        }
    }
    
    public boolean isValid() {
        return !this.pieces.isEmpty();
    }
    
    public int getChunkX() {
        return this.chunkX;
    }
    
    public int getChunkZ() {
        return this.chunkZ;
    }
    
    public BlockPos getLocatePos() {
        return new BlockPos(this.chunkX << 4, 0, this.chunkZ << 4);
    }
    
    public boolean canBeReferenced() {
        return this.references < this.getMaxReferences();
    }
    
    public void addReference() {
        ++this.references;
    }
    
    public int getReferences() {
        return this.references;
    }
    
    protected int getMaxReferences() {
        return 1;
    }
    
    public StructureFeature<?> getFeature() {
        return this.feature;
    }
    
    static {
        INVALID_START = new StructureStart<MineshaftConfiguration>(StructureFeature.MINESHAFT, 0, 0, BoundingBox.getUnknownBox(), 0, 0L) {
            @Override
            public void generatePieces(final RegistryAccess gn, final ChunkGenerator cfv, final StructureManager cst, final int integer4, final int integer5, final Biome bss, final MineshaftConfiguration cmb) {
            }
        };
    }
}
