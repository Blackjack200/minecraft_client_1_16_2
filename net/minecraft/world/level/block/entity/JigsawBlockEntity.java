package net.minecraft.world.level.block.entity;

import java.util.Arrays;
import java.util.Optional;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.JigsawBlock;
import java.util.Iterator;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import java.util.Random;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import java.util.List;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.structures.SinglePoolElement;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class JigsawBlockEntity extends BlockEntity {
    private ResourceLocation name;
    private ResourceLocation target;
    private ResourceLocation pool;
    private JointType joint;
    private String finalState;
    
    public JigsawBlockEntity(final BlockEntityType<?> cch) {
        super(cch);
        this.name = new ResourceLocation("empty");
        this.target = new ResourceLocation("empty");
        this.pool = new ResourceLocation("empty");
        this.joint = JointType.ROLLABLE;
        this.finalState = "minecraft:air";
    }
    
    public JigsawBlockEntity() {
        this(BlockEntityType.JIGSAW);
    }
    
    public ResourceLocation getName() {
        return this.name;
    }
    
    public ResourceLocation getTarget() {
        return this.target;
    }
    
    public ResourceLocation getPool() {
        return this.pool;
    }
    
    public String getFinalState() {
        return this.finalState;
    }
    
    public JointType getJoint() {
        return this.joint;
    }
    
    public void setName(final ResourceLocation vk) {
        this.name = vk;
    }
    
    public void setTarget(final ResourceLocation vk) {
        this.target = vk;
    }
    
    public void setPool(final ResourceLocation vk) {
        this.pool = vk;
    }
    
    public void setFinalState(final String string) {
        this.finalState = string;
    }
    
    public void setJoint(final JointType a) {
        this.joint = a;
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        md.putString("name", this.name.toString());
        md.putString("target", this.target.toString());
        md.putString("pool", this.pool.toString());
        md.putString("final_state", this.finalState);
        md.putString("joint", this.joint.getSerializedName());
        return md;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        this.name = new ResourceLocation(md.getString("name"));
        this.target = new ResourceLocation(md.getString("target"));
        this.pool = new ResourceLocation(md.getString("pool"));
        this.finalState = md.getString("final_state");
        this.joint = (JointType)JointType.byName(md.getString("joint")).orElseGet(() -> JigsawBlock.getFrontFacing(cee).getAxis().isHorizontal() ? JointType.ALIGNED : JointType.ROLLABLE);
    }
    
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 12, this.getUpdateTag());
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }
    
    public void generate(final ServerLevel aag, final int integer, final boolean boolean3) {
        final ChunkGenerator cfv5 = aag.getChunkSource().getGenerator();
        final StructureManager cst6 = aag.getStructureManager();
        final StructureFeatureManager bsk7 = aag.structureFeatureManager();
        final Random random8 = aag.getRandom();
        final BlockPos fx9 = this.getBlockPos();
        final List<PoolElementStructurePiece> list10 = (List<PoolElementStructurePiece>)Lists.newArrayList();
        final StructureTemplate csy11 = new StructureTemplate();
        csy11.fillFromWorld(aag, fx9, new BlockPos(1, 1, 1), false, null);
        final StructurePoolElement cof12 = new SinglePoolElement(csy11);
        final PoolElementStructurePiece crl13 = new PoolElementStructurePiece(cst6, cof12, fx9, 1, Rotation.NONE, new BoundingBox(fx9, fx9));
        JigsawPlacement.addPieces(aag.registryAccess(), crl13, integer, PoolElementStructurePiece::new, cfv5, cst6, list10, random8);
        for (final PoolElementStructurePiece crl14 : list10) {
            crl14.place(aag, bsk7, cfv5, random8, BoundingBox.infinite(), fx9, boolean3);
        }
    }
    
    public enum JointType implements StringRepresentable {
        ROLLABLE("rollable"), 
        ALIGNED("aligned");
        
        private final String name;
        
        private JointType(final String string3) {
            this.name = string3;
        }
        
        public String getSerializedName() {
            return this.name;
        }
        
        public static Optional<JointType> byName(final String string) {
            return (Optional<JointType>)Arrays.stream((Object[])values()).filter(a -> a.getSerializedName().equals(string)).findFirst();
        }
    }
}
