package net.minecraft.data.structures;

import org.apache.logging.log4j.LogManager;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.nbt.CompoundTag;
import org.apache.logging.log4j.Logger;

public class StructureUpdater implements SnbtToNbt.Filter {
    private static final Logger LOGGER;
    
    public CompoundTag apply(final String string, final CompoundTag md) {
        if (string.startsWith("data/minecraft/structures/")) {
            return updateStructure(string, patchVersion(md));
        }
        return md;
    }
    
    private static CompoundTag patchVersion(final CompoundTag md) {
        if (!md.contains("DataVersion", 99)) {
            md.putInt("DataVersion", 500);
        }
        return md;
    }
    
    private static CompoundTag updateStructure(final String string, final CompoundTag md) {
        final StructureTemplate csy3 = new StructureTemplate();
        final int integer4 = md.getInt("DataVersion");
        final int integer5 = 2532;
        if (integer4 < 2532) {
            StructureUpdater.LOGGER.warn(new StringBuilder().append("SNBT Too old, do not forget to update: ").append(integer4).append(" < ").append(2532).append(": ").append(string).toString());
        }
        final CompoundTag md2 = NbtUtils.update(DataFixers.getDataFixer(), DataFixTypes.STRUCTURE, md, integer4);
        csy3.load(md2);
        return csy3.save(new CompoundTag());
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
