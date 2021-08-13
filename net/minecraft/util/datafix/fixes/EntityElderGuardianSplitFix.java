package net.minecraft.util.datafix.fixes;

import java.util.Objects;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class EntityElderGuardianSplitFix extends SimpleEntityRenameFix {
    public EntityElderGuardianSplitFix(final Schema schema, final boolean boolean2) {
        super("EntityElderGuardianSplitFix", schema, boolean2);
    }
    
    @Override
    protected Pair<String, Dynamic<?>> getNewNameAndTag(final String string, final Dynamic<?> dynamic) {
        return (Pair<String, Dynamic<?>>)Pair.of((Objects.equals(string, "Guardian") && dynamic.get("Elder").asBoolean(false)) ? "ElderGuardian" : string, dynamic);
    }
}
