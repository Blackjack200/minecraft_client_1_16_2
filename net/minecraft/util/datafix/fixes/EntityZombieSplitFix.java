package net.minecraft.util.datafix.fixes;

import java.util.Objects;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class EntityZombieSplitFix extends SimpleEntityRenameFix {
    public EntityZombieSplitFix(final Schema schema, final boolean boolean2) {
        super("EntityZombieSplitFix", schema, boolean2);
    }
    
    @Override
    protected Pair<String, Dynamic<?>> getNewNameAndTag(final String string, Dynamic<?> dynamic) {
        if (Objects.equals("Zombie", string)) {
            String string2 = "Zombie";
            final int integer5 = dynamic.get("ZombieType").asInt(0);
            switch (integer5) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5: {
                    string2 = "ZombieVillager";
                    dynamic = dynamic.set("Profession", dynamic.createInt(integer5 - 1));
                    break;
                }
                case 6: {
                    string2 = "Husk";
                    break;
                }
            }
            dynamic = dynamic.remove("ZombieType");
            return (Pair<String, Dynamic<?>>)Pair.of(string2, dynamic);
        }
        return (Pair<String, Dynamic<?>>)Pair.of(string, dynamic);
    }
}
