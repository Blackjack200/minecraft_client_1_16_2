package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.schemas.Schema;

public class BeehivePoiRenameFix extends PoiTypeRename {
    public BeehivePoiRenameFix(final Schema schema) {
        super(schema, false);
    }
    
    @Override
    protected String rename(final String string) {
        return string.equals("minecraft:bee_hive") ? "minecraft:beehive" : string;
    }
}
