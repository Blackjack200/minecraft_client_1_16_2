package net.minecraft.world.level;

import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.WeighedRandom;

public class SpawnData extends WeighedRandom.WeighedRandomItem {
    private final CompoundTag tag;
    
    public SpawnData() {
        super(1);
        (this.tag = new CompoundTag()).putString("id", "minecraft:pig");
    }
    
    public SpawnData(final CompoundTag md) {
        this(md.contains("Weight", 99) ? md.getInt("Weight") : 1, md.getCompound("Entity"));
    }
    
    public SpawnData(final int integer, final CompoundTag md) {
        super(integer);
        this.tag = md;
        final ResourceLocation vk4 = ResourceLocation.tryParse(md.getString("id"));
        if (vk4 != null) {
            md.putString("id", vk4.toString());
        }
        else {
            md.putString("id", "minecraft:pig");
        }
    }
    
    public CompoundTag save() {
        final CompoundTag md2 = new CompoundTag();
        md2.put("Entity", (Tag)this.tag);
        md2.putInt("Weight", this.weight);
        return md2;
    }
    
    public CompoundTag getTag() {
        return this.tag;
    }
}
