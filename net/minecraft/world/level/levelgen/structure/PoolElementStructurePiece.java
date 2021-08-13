package net.minecraft.world.level.levelgen.structure;

import org.apache.logging.log4j.LogManager;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.level.ChunkPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import java.util.Iterator;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.levelgen.feature.structures.EmptyPoolElement;
import com.mojang.serialization.DynamicOps;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.CompoundTag;
import com.google.common.collect.Lists;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.feature.structures.JigsawJunction;
import java.util.List;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import org.apache.logging.log4j.Logger;

public class PoolElementStructurePiece extends StructurePiece {
    private static final Logger LOGGER;
    protected final StructurePoolElement element;
    protected BlockPos position;
    private final int groundLevelDelta;
    protected final Rotation rotation;
    private final List<JigsawJunction> junctions;
    private final StructureManager structureManager;
    
    public PoolElementStructurePiece(final StructureManager cst, final StructurePoolElement cof, final BlockPos fx, final int integer, final Rotation bzj, final BoundingBox cqx) {
        super(StructurePieceType.JIGSAW, 0);
        this.junctions = (List<JigsawJunction>)Lists.newArrayList();
        this.structureManager = cst;
        this.element = cof;
        this.position = fx;
        this.groundLevelDelta = integer;
        this.rotation = bzj;
        this.boundingBox = cqx;
    }
    
    public PoolElementStructurePiece(final StructureManager cst, final CompoundTag md) {
        super(StructurePieceType.JIGSAW, md);
        this.junctions = (List<JigsawJunction>)Lists.newArrayList();
        this.structureManager = cst;
        this.position = new BlockPos(md.getInt("PosX"), md.getInt("PosY"), md.getInt("PosZ"));
        this.groundLevelDelta = md.getInt("ground_level_delta");
        this.element = (StructurePoolElement)StructurePoolElement.CODEC.parse((DynamicOps)NbtOps.INSTANCE, md.getCompound("pool_element")).resultOrPartial(PoolElementStructurePiece.LOGGER::error).orElse(EmptyPoolElement.INSTANCE);
        this.rotation = Rotation.valueOf(md.getString("rotation"));
        this.boundingBox = this.element.getBoundingBox(cst, this.position, this.rotation);
        final ListTag mj4 = md.getList("junctions", 10);
        this.junctions.clear();
        mj4.forEach(mt -> this.junctions.add(JigsawJunction.deserialize((com.mojang.serialization.Dynamic<Object>)new Dynamic((DynamicOps)NbtOps.INSTANCE, (Object)mt))));
    }
    
    @Override
    protected void addAdditionalSaveData(final CompoundTag md) {
        md.putInt("PosX", this.position.getX());
        md.putInt("PosY", this.position.getY());
        md.putInt("PosZ", this.position.getZ());
        md.putInt("ground_level_delta", this.groundLevelDelta);
        StructurePoolElement.CODEC.encodeStart((DynamicOps)NbtOps.INSTANCE, this.element).resultOrPartial(PoolElementStructurePiece.LOGGER::error).ifPresent(mt -> md.put("pool_element", mt));
        md.putString("rotation", this.rotation.name());
        final ListTag mj3 = new ListTag();
        for (final JigsawJunction coa5 : this.junctions) {
            mj3.add(coa5.serialize((com.mojang.serialization.DynamicOps<Object>)NbtOps.INSTANCE).getValue());
        }
        md.put("junctions", (Tag)mj3);
    }
    
    @Override
    public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
        return this.place(bso, bsk, cfv, random, cqx, fx, false);
    }
    
    public boolean place(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final BlockPos fx, final boolean boolean7) {
        return this.element.place(this.structureManager, bso, bsk, cfv, this.position, fx, this.rotation, cqx, random, boolean7);
    }
    
    @Override
    public void move(final int integer1, final int integer2, final int integer3) {
        super.move(integer1, integer2, integer3);
        this.position = this.position.offset(integer1, integer2, integer3);
    }
    
    @Override
    public Rotation getRotation() {
        return this.rotation;
    }
    
    public String toString() {
        return String.format("<%s | %s | %s | %s>", new Object[] { this.getClass().getSimpleName(), this.position, this.rotation, this.element });
    }
    
    public StructurePoolElement getElement() {
        return this.element;
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public int getGroundLevelDelta() {
        return this.groundLevelDelta;
    }
    
    public void addJunction(final JigsawJunction coa) {
        this.junctions.add(coa);
    }
    
    public List<JigsawJunction> getJunctions() {
        return this.junctions;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
