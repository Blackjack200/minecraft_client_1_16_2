package net.minecraft.world.level.block.entity;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.Objects;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.Util;
import java.util.Random;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.ResourceLocationException;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Vec3i;
import java.util.Iterator;
import com.google.common.collect.Lists;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.StructureBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class StructureBlockEntity extends BlockEntity {
    private ResourceLocation structureName;
    private String author;
    private String metaData;
    private BlockPos structurePos;
    private BlockPos structureSize;
    private Mirror mirror;
    private Rotation rotation;
    private StructureMode mode;
    private boolean ignoreEntities;
    private boolean powered;
    private boolean showAir;
    private boolean showBoundingBox;
    private float integrity;
    private long seed;
    
    public StructureBlockEntity() {
        super(BlockEntityType.STRUCTURE_BLOCK);
        this.author = "";
        this.metaData = "";
        this.structurePos = new BlockPos(0, 1, 0);
        this.structureSize = BlockPos.ZERO;
        this.mirror = Mirror.NONE;
        this.rotation = Rotation.NONE;
        this.mode = StructureMode.DATA;
        this.ignoreEntities = true;
        this.showBoundingBox = true;
        this.integrity = 1.0f;
    }
    
    @Override
    public double getViewDistance() {
        return 96.0;
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        md.putString("name", this.getStructureName());
        md.putString("author", this.author);
        md.putString("metadata", this.metaData);
        md.putInt("posX", this.structurePos.getX());
        md.putInt("posY", this.structurePos.getY());
        md.putInt("posZ", this.structurePos.getZ());
        md.putInt("sizeX", this.structureSize.getX());
        md.putInt("sizeY", this.structureSize.getY());
        md.putInt("sizeZ", this.structureSize.getZ());
        md.putString("rotation", this.rotation.toString());
        md.putString("mirror", this.mirror.toString());
        md.putString("mode", this.mode.toString());
        md.putBoolean("ignoreEntities", this.ignoreEntities);
        md.putBoolean("powered", this.powered);
        md.putBoolean("showair", this.showAir);
        md.putBoolean("showboundingbox", this.showBoundingBox);
        md.putFloat("integrity", this.integrity);
        md.putLong("seed", this.seed);
        return md;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        this.setStructureName(md.getString("name"));
        this.author = md.getString("author");
        this.metaData = md.getString("metadata");
        final int integer4 = Mth.clamp(md.getInt("posX"), -48, 48);
        final int integer5 = Mth.clamp(md.getInt("posY"), -48, 48);
        final int integer6 = Mth.clamp(md.getInt("posZ"), -48, 48);
        this.structurePos = new BlockPos(integer4, integer5, integer6);
        final int integer7 = Mth.clamp(md.getInt("sizeX"), 0, 48);
        final int integer8 = Mth.clamp(md.getInt("sizeY"), 0, 48);
        final int integer9 = Mth.clamp(md.getInt("sizeZ"), 0, 48);
        this.structureSize = new BlockPos(integer7, integer8, integer9);
        try {
            this.rotation = Rotation.valueOf(md.getString("rotation"));
        }
        catch (IllegalArgumentException illegalArgumentException10) {
            this.rotation = Rotation.NONE;
        }
        try {
            this.mirror = Mirror.valueOf(md.getString("mirror"));
        }
        catch (IllegalArgumentException illegalArgumentException10) {
            this.mirror = Mirror.NONE;
        }
        try {
            this.mode = StructureMode.valueOf(md.getString("mode"));
        }
        catch (IllegalArgumentException illegalArgumentException10) {
            this.mode = StructureMode.DATA;
        }
        this.ignoreEntities = md.getBoolean("ignoreEntities");
        this.powered = md.getBoolean("powered");
        this.showAir = md.getBoolean("showair");
        this.showBoundingBox = md.getBoolean("showboundingbox");
        if (md.contains("integrity")) {
            this.integrity = md.getFloat("integrity");
        }
        else {
            this.integrity = 1.0f;
        }
        this.seed = md.getLong("seed");
        this.updateBlockState();
    }
    
    private void updateBlockState() {
        if (this.level == null) {
            return;
        }
        final BlockPos fx2 = this.getBlockPos();
        final BlockState cee3 = this.level.getBlockState(fx2);
        if (cee3.is(Blocks.STRUCTURE_BLOCK)) {
            this.level.setBlock(fx2, ((StateHolder<O, BlockState>)cee3).<StructureMode, StructureMode>setValue(StructureBlock.MODE, this.mode), 2);
        }
    }
    
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 7, this.getUpdateTag());
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }
    
    public boolean usedBy(final Player bft) {
        if (!bft.canUseGameMasterBlocks()) {
            return false;
        }
        if (bft.getCommandSenderWorld().isClientSide) {
            bft.openStructureBlock(this);
        }
        return true;
    }
    
    public String getStructureName() {
        return (this.structureName == null) ? "" : this.structureName.toString();
    }
    
    public String getStructurePath() {
        return (this.structureName == null) ? "" : this.structureName.getPath();
    }
    
    public boolean hasStructureName() {
        return this.structureName != null;
    }
    
    public void setStructureName(@Nullable final String string) {
        this.setStructureName(StringUtil.isNullOrEmpty(string) ? null : ResourceLocation.tryParse(string));
    }
    
    public void setStructureName(@Nullable final ResourceLocation vk) {
        this.structureName = vk;
    }
    
    public void createdBy(final LivingEntity aqj) {
        this.author = aqj.getName().getString();
    }
    
    public BlockPos getStructurePos() {
        return this.structurePos;
    }
    
    public void setStructurePos(final BlockPos fx) {
        this.structurePos = fx;
    }
    
    public BlockPos getStructureSize() {
        return this.structureSize;
    }
    
    public void setStructureSize(final BlockPos fx) {
        this.structureSize = fx;
    }
    
    public Mirror getMirror() {
        return this.mirror;
    }
    
    public void setMirror(final Mirror byd) {
        this.mirror = byd;
    }
    
    public Rotation getRotation() {
        return this.rotation;
    }
    
    public void setRotation(final Rotation bzj) {
        this.rotation = bzj;
    }
    
    public String getMetaData() {
        return this.metaData;
    }
    
    public void setMetaData(final String string) {
        this.metaData = string;
    }
    
    public StructureMode getMode() {
        return this.mode;
    }
    
    public void setMode(final StructureMode cfl) {
        this.mode = cfl;
        final BlockState cee3 = this.level.getBlockState(this.getBlockPos());
        if (cee3.is(Blocks.STRUCTURE_BLOCK)) {
            this.level.setBlock(this.getBlockPos(), ((StateHolder<O, BlockState>)cee3).<StructureMode, StructureMode>setValue(StructureBlock.MODE, cfl), 2);
        }
    }
    
    public void nextMode() {
        switch (this.getMode()) {
            case SAVE: {
                this.setMode(StructureMode.LOAD);
                break;
            }
            case LOAD: {
                this.setMode(StructureMode.CORNER);
                break;
            }
            case CORNER: {
                this.setMode(StructureMode.DATA);
                break;
            }
            case DATA: {
                this.setMode(StructureMode.SAVE);
                break;
            }
        }
    }
    
    public boolean isIgnoreEntities() {
        return this.ignoreEntities;
    }
    
    public void setIgnoreEntities(final boolean boolean1) {
        this.ignoreEntities = boolean1;
    }
    
    public float getIntegrity() {
        return this.integrity;
    }
    
    public void setIntegrity(final float float1) {
        this.integrity = float1;
    }
    
    public long getSeed() {
        return this.seed;
    }
    
    public void setSeed(final long long1) {
        this.seed = long1;
    }
    
    public boolean detectSize() {
        if (this.mode != StructureMode.SAVE) {
            return false;
        }
        final BlockPos fx2 = this.getBlockPos();
        final int integer3 = 80;
        final BlockPos fx3 = new BlockPos(fx2.getX() - 80, 0, fx2.getZ() - 80);
        final BlockPos fx4 = new BlockPos(fx2.getX() + 80, 255, fx2.getZ() + 80);
        final List<StructureBlockEntity> list6 = this.getNearbyCornerBlocks(fx3, fx4);
        final List<StructureBlockEntity> list7 = this.filterRelatedCornerBlocks(list6);
        if (list7.size() < 1) {
            return false;
        }
        final BoundingBox cqx8 = this.calculateEnclosingBoundingBox(fx2, list7);
        if (cqx8.x1 - cqx8.x0 > 1 && cqx8.y1 - cqx8.y0 > 1 && cqx8.z1 - cqx8.z0 > 1) {
            this.structurePos = new BlockPos(cqx8.x0 - fx2.getX() + 1, cqx8.y0 - fx2.getY() + 1, cqx8.z0 - fx2.getZ() + 1);
            this.structureSize = new BlockPos(cqx8.x1 - cqx8.x0 - 1, cqx8.y1 - cqx8.y0 - 1, cqx8.z1 - cqx8.z0 - 1);
            this.setChanged();
            final BlockState cee9 = this.level.getBlockState(fx2);
            this.level.sendBlockUpdated(fx2, cee9, cee9, 3);
            return true;
        }
        return false;
    }
    
    private List<StructureBlockEntity> filterRelatedCornerBlocks(final List<StructureBlockEntity> list) {
        final Predicate<StructureBlockEntity> predicate3 = (Predicate<StructureBlockEntity>)(cdg -> cdg.mode == StructureMode.CORNER && Objects.equals(this.structureName, cdg.structureName));
        return (List<StructureBlockEntity>)list.stream().filter((Predicate)predicate3).collect(Collectors.toList());
    }
    
    private List<StructureBlockEntity> getNearbyCornerBlocks(final BlockPos fx1, final BlockPos fx2) {
        final List<StructureBlockEntity> list4 = (List<StructureBlockEntity>)Lists.newArrayList();
        for (final BlockPos fx3 : BlockPos.betweenClosed(fx1, fx2)) {
            final BlockState cee7 = this.level.getBlockState(fx3);
            if (!cee7.is(Blocks.STRUCTURE_BLOCK)) {
                continue;
            }
            final BlockEntity ccg8 = this.level.getBlockEntity(fx3);
            if (ccg8 == null || !(ccg8 instanceof StructureBlockEntity)) {
                continue;
            }
            list4.add(ccg8);
        }
        return list4;
    }
    
    private BoundingBox calculateEnclosingBoundingBox(final BlockPos fx, final List<StructureBlockEntity> list) {
        BoundingBox cqx4;
        if (list.size() > 1) {
            final BlockPos fx2 = ((StructureBlockEntity)list.get(0)).getBlockPos();
            cqx4 = new BoundingBox(fx2, fx2);
        }
        else {
            cqx4 = new BoundingBox(fx, fx);
        }
        for (final StructureBlockEntity cdg6 : list) {
            final BlockPos fx3 = cdg6.getBlockPos();
            if (fx3.getX() < cqx4.x0) {
                cqx4.x0 = fx3.getX();
            }
            else if (fx3.getX() > cqx4.x1) {
                cqx4.x1 = fx3.getX();
            }
            if (fx3.getY() < cqx4.y0) {
                cqx4.y0 = fx3.getY();
            }
            else if (fx3.getY() > cqx4.y1) {
                cqx4.y1 = fx3.getY();
            }
            if (fx3.getZ() < cqx4.z0) {
                cqx4.z0 = fx3.getZ();
            }
            else {
                if (fx3.getZ() <= cqx4.z1) {
                    continue;
                }
                cqx4.z1 = fx3.getZ();
            }
        }
        return cqx4;
    }
    
    public boolean saveStructure() {
        return this.saveStructure(true);
    }
    
    public boolean saveStructure(final boolean boolean1) {
        if (this.mode != StructureMode.SAVE || this.level.isClientSide || this.structureName == null) {
            return false;
        }
        final BlockPos fx3 = this.getBlockPos().offset(this.structurePos);
        final ServerLevel aag4 = (ServerLevel)this.level;
        final StructureManager cst5 = aag4.getStructureManager();
        StructureTemplate csy6;
        try {
            csy6 = cst5.getOrCreate(this.structureName);
        }
        catch (ResourceLocationException v7) {
            return false;
        }
        csy6.fillFromWorld(this.level, fx3, this.structureSize, !this.ignoreEntities, Blocks.STRUCTURE_VOID);
        csy6.setAuthor(this.author);
        if (boolean1) {
            try {
                return cst5.save(this.structureName);
            }
            catch (ResourceLocationException v7) {
                return false;
            }
        }
        return true;
    }
    
    public boolean loadStructure(final ServerLevel aag) {
        return this.loadStructure(aag, true);
    }
    
    private static Random createRandom(final long long1) {
        if (long1 == 0L) {
            return new Random(Util.getMillis());
        }
        return new Random(long1);
    }
    
    public boolean loadStructure(final ServerLevel aag, final boolean boolean2) {
        if (this.mode != StructureMode.LOAD || this.structureName == null) {
            return false;
        }
        final StructureManager cst4 = aag.getStructureManager();
        StructureTemplate csy5;
        try {
            csy5 = cst4.get(this.structureName);
        }
        catch (ResourceLocationException v6) {
            return false;
        }
        return csy5 != null && this.loadStructure(aag, boolean2, csy5);
    }
    
    public boolean loadStructure(final ServerLevel aag, final boolean boolean2, final StructureTemplate csy) {
        final BlockPos fx5 = this.getBlockPos();
        if (!StringUtil.isNullOrEmpty(csy.getAuthor())) {
            this.author = csy.getAuthor();
        }
        final BlockPos fx6 = csy.getSize();
        final boolean boolean3 = this.structureSize.equals(fx6);
        if (!boolean3) {
            this.structureSize = fx6;
            this.setChanged();
            final BlockState cee8 = aag.getBlockState(fx5);
            aag.sendBlockUpdated(fx5, cee8, cee8, 3);
        }
        if (!boolean2 || boolean3) {
            final StructurePlaceSettings csu8 = new StructurePlaceSettings().setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities).setChunkPos(null);
            if (this.integrity < 1.0f) {
                csu8.clearProcessors().addProcessor(new BlockRotProcessor(Mth.clamp(this.integrity, 0.0f, 1.0f))).setRandom(createRandom(this.seed));
            }
            final BlockPos fx7 = fx5.offset(this.structurePos);
            csy.placeInWorldChunk(aag, fx7, csu8, createRandom(this.seed));
            return true;
        }
        return false;
    }
    
    public void unloadStructure() {
        if (this.structureName == null) {
            return;
        }
        final ServerLevel aag2 = (ServerLevel)this.level;
        final StructureManager cst3 = aag2.getStructureManager();
        cst3.remove(this.structureName);
    }
    
    public boolean isStructureLoadable() {
        if (this.mode != StructureMode.LOAD || this.level.isClientSide || this.structureName == null) {
            return false;
        }
        final ServerLevel aag2 = (ServerLevel)this.level;
        final StructureManager cst3 = aag2.getStructureManager();
        try {
            return cst3.get(this.structureName) != null;
        }
        catch (ResourceLocationException v4) {
            return false;
        }
    }
    
    public boolean isPowered() {
        return this.powered;
    }
    
    public void setPowered(final boolean boolean1) {
        this.powered = boolean1;
    }
    
    public boolean getShowAir() {
        return this.showAir;
    }
    
    public void setShowAir(final boolean boolean1) {
        this.showAir = boolean1;
    }
    
    public boolean getShowBoundingBox() {
        return this.showBoundingBox;
    }
    
    public void setShowBoundingBox(final boolean boolean1) {
        this.showBoundingBox = boolean1;
    }
    
    public enum UpdateType {
        UPDATE_DATA, 
        SAVE_AREA, 
        LOAD_AREA, 
        SCAN_AREA;
    }
}
