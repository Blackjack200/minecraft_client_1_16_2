package net.minecraft.util.datafix.fixes;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import com.mojang.datafixers.DataFixUtils;
import java.util.function.Function;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.types.Type;
import java.util.Objects;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class ObjectiveDisplayNameFix extends DataFix {
    public ObjectiveDisplayNameFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    protected TypeRewriteRule makeRule() {
        final Type<Pair<String, Dynamic<?>>> type2 = (Type<Pair<String, Dynamic<?>>>)DSL.named(References.OBJECTIVE.typeName(), DSL.remainderType());
        if (!Objects.equals(type2, this.getInputSchema().getType(References.OBJECTIVE))) {
            throw new IllegalStateException("Objective type is not what was expected.");
        }
        return this.fixTypeEverywhere("ObjectiveDisplayNameFix", (Type)type2, dynamicOps -> pair -> pair.mapSecond(dynamic -> dynamic.update("DisplayName", dynamic2 -> (Dynamic)DataFixUtils.orElse(dynamic2.asString().map(string -> Component.Serializer.toJson(new TextComponent(string))).map(dynamic::createString).result(), dynamic2))));
    }
}
