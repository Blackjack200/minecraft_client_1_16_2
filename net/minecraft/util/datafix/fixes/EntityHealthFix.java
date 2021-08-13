package net.minecraft.util.datafix.fixes;

import com.google.common.collect.Sets;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.TypeRewriteRule;
import java.util.Optional;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;
import java.util.Set;
import com.mojang.datafixers.DataFix;

public class EntityHealthFix extends DataFix {
    private static final Set<String> ENTITIES;
    
    public EntityHealthFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public Dynamic<?> fixTag(Dynamic<?> dynamic) {
        final Optional<Number> optional4 = (Optional<Number>)dynamic.get("HealF").asNumber().result();
        final Optional<Number> optional5 = (Optional<Number>)dynamic.get("Health").asNumber().result();
        float float3;
        if (optional4.isPresent()) {
            float3 = ((Number)optional4.get()).floatValue();
            dynamic = dynamic.remove("HealF");
        }
        else {
            if (!optional5.isPresent()) {
                return dynamic;
            }
            float3 = ((Number)optional5.get()).floatValue();
        }
        return dynamic.set("Health", dynamic.createFloat(float3));
    }
    
    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("EntityHealthFix", this.getInputSchema().getType(References.ENTITY), typed -> typed.update(DSL.remainderFinder(), this::fixTag));
    }
    
    static {
        ENTITIES = (Set)Sets.newHashSet((Object[])new String[] { "ArmorStand", "Bat", "Blaze", "CaveSpider", "Chicken", "Cow", "Creeper", "EnderDragon", "Enderman", "Endermite", "EntityHorse", "Ghast", "Giant", "Guardian", "LavaSlime", "MushroomCow", "Ozelot", "Pig", "PigZombie", "Rabbit", "Sheep", "Shulker", "Silverfish", "Skeleton", "Slime", "SnowMan", "Spider", "Squid", "Villager", "VillagerGolem", "Witch", "WitherBoss", "Wolf", "Zombie" });
    }
}
