package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;
import java.util.Random;

public class EntityZombieVillagerTypeFix extends NamedEntityFix {
    private static final Random RANDOM;
    
    public EntityZombieVillagerTypeFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "EntityZombieVillagerTypeFix", References.ENTITY, "Zombie");
    }
    
    public Dynamic<?> fixTag(Dynamic<?> dynamic) {
        if (dynamic.get("IsVillager").asBoolean(false)) {
            if (!dynamic.get("ZombieType").result().isPresent()) {
                int integer3 = this.getVillagerProfession(dynamic.get("VillagerProfession").asInt(-1));
                if (integer3 == -1) {
                    integer3 = this.getVillagerProfession(EntityZombieVillagerTypeFix.RANDOM.nextInt(6));
                }
                dynamic = dynamic.set("ZombieType", dynamic.createInt(integer3));
            }
            dynamic = dynamic.remove("IsVillager");
        }
        return dynamic;
    }
    
    private int getVillagerProfession(final int integer) {
        if (integer < 0 || integer >= 6) {
            return -1;
        }
        return integer;
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.update(DSL.remainderFinder(), this::fixTag);
    }
    
    static {
        RANDOM = new Random();
    }
}
