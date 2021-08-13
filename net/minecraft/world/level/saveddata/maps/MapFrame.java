package net.minecraft.world.level.saveddata.maps;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

public class MapFrame {
    private final BlockPos pos;
    private final int rotation;
    private final int entityId;
    
    public MapFrame(final BlockPos fx, final int integer2, final int integer3) {
        this.pos = fx;
        this.rotation = integer2;
        this.entityId = integer3;
    }
    
    public static MapFrame load(final CompoundTag md) {
        final BlockPos fx2 = NbtUtils.readBlockPos(md.getCompound("Pos"));
        final int integer3 = md.getInt("Rotation");
        final int integer4 = md.getInt("EntityId");
        return new MapFrame(fx2, integer3, integer4);
    }
    
    public CompoundTag save() {
        final CompoundTag md2 = new CompoundTag();
        md2.put("Pos", (Tag)NbtUtils.writeBlockPos(this.pos));
        md2.putInt("Rotation", this.rotation);
        md2.putInt("EntityId", this.entityId);
        return md2;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public int getRotation() {
        return this.rotation;
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public String getId() {
        return frameId(this.pos);
    }
    
    public static String frameId(final BlockPos fx) {
        return new StringBuilder().append("frame-").append(fx.getX()).append(",").append(fx.getY()).append(",").append(fx.getZ()).toString();
    }
}
