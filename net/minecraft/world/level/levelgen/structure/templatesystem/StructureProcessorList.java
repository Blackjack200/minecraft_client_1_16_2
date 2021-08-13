package net.minecraft.world.level.levelgen.structure.templatesystem;

import java.util.List;

public class StructureProcessorList {
    private final List<StructureProcessor> list;
    
    public StructureProcessorList(final List<StructureProcessor> list) {
        this.list = list;
    }
    
    public List<StructureProcessor> list() {
        return this.list;
    }
    
    public String toString() {
        return new StringBuilder().append("ProcessorList[").append(this.list).append("]").toString();
    }
}
