package net.minecraft.world.entity.npc;

import com.mojang.datafixers.kinds.Applicative;
import net.minecraft.core.Registry;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class VillagerData {
    private static final int[] NEXT_LEVEL_XP_THRESHOLDS;
    public static final Codec<VillagerData> CODEC;
    private final VillagerType type;
    private final VillagerProfession profession;
    private final int level;
    
    public VillagerData(final VillagerType bfl, final VillagerProfession bfj, final int integer) {
        this.type = bfl;
        this.profession = bfj;
        this.level = Math.max(1, integer);
    }
    
    public VillagerType getType() {
        return this.type;
    }
    
    public VillagerProfession getProfession() {
        return this.profession;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public VillagerData setType(final VillagerType bfl) {
        return new VillagerData(bfl, this.profession, this.level);
    }
    
    public VillagerData setProfession(final VillagerProfession bfj) {
        return new VillagerData(this.type, bfj, this.level);
    }
    
    public VillagerData setLevel(final int integer) {
        return new VillagerData(this.type, this.profession, integer);
    }
    
    public static int getMinXpPerLevel(final int integer) {
        return canLevelUp(integer) ? VillagerData.NEXT_LEVEL_XP_THRESHOLDS[integer - 1] : 0;
    }
    
    public static int getMaxXpPerLevel(final int integer) {
        return canLevelUp(integer) ? VillagerData.NEXT_LEVEL_XP_THRESHOLDS[integer] : 0;
    }
    
    public static boolean canLevelUp(final int integer) {
        return integer >= 1 && integer < 5;
    }
    
    static {
        NEXT_LEVEL_XP_THRESHOLDS = new int[] { 0, 10, 70, 150, 250 };
        CODEC = RecordCodecBuilder.create(instance -> instance.group((App)Registry.VILLAGER_TYPE.fieldOf("type").orElseGet(() -> VillagerType.PLAINS).forGetter(bfh -> bfh.type), (App)Registry.VILLAGER_PROFESSION.fieldOf("profession").orElseGet(() -> VillagerProfession.NONE).forGetter(bfh -> bfh.profession), (App)Codec.INT.fieldOf("level").orElse(1).forGetter(bfh -> bfh.level)).apply((Applicative)instance, VillagerData::new));
    }
}
