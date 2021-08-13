package net.minecraft.util.datafix.fixes;

import java.util.function.Function;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;
import com.mojang.datafixers.DataFix;

public class RenameBiomesFix extends DataFix {
    private final String name;
    private final Map<String, String> biomes;
    
    public RenameBiomesFix(final Schema schema, final boolean boolean2, final String string, final Map<String, String> map) {
        super(schema, boolean2);
        this.biomes = map;
        this.name = string;
    }
    
    protected TypeRewriteRule makeRule() {
        final Type<Pair<String, String>> type2 = (Type<Pair<String, String>>)DSL.named(References.BIOME.typeName(), (Type)NamespacedSchema.namespacedString());
        if (!Objects.equals(type2, this.getInputSchema().getType(References.BIOME))) {
            throw new IllegalStateException("Biome type is not what was expected.");
        }
        return this.fixTypeEverywhere(this.name, (Type)type2, dynamicOps -> pair -> pair.mapSecond(string -> (String)this.biomes.getOrDefault(string, string)));
    }
}
