package net.minecraft.client.resources.metadata.animation;

import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Map;

public class VillagerMetaDataSection {
    public static final VillagerMetadataSectionSerializer SERIALIZER;
    private final Hat hat;
    
    public VillagerMetaDataSection(final Hat a) {
        this.hat = a;
    }
    
    public Hat getHat() {
        return this.hat;
    }
    
    static {
        SERIALIZER = new VillagerMetadataSectionSerializer();
    }
    
    public enum Hat {
        NONE("none"), 
        PARTIAL("partial"), 
        FULL("full");
        
        private static final Map<String, Hat> BY_NAME;
        private final String name;
        
        private Hat(final String string3) {
            this.name = string3;
        }
        
        public String getName() {
            return this.name;
        }
        
        public static Hat getByName(final String string) {
            return (Hat)Hat.BY_NAME.getOrDefault(string, Hat.NONE);
        }
        
        static {
            BY_NAME = (Map)Arrays.stream((Object[])values()).collect(Collectors.toMap(Hat::getName, a -> a));
        }
    }
}
