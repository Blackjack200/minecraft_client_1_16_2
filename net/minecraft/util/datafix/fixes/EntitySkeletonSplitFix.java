package net.minecraft.util.datafix.fixes;

import java.util.Objects;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class EntitySkeletonSplitFix extends SimpleEntityRenameFix {
    public EntitySkeletonSplitFix(final Schema schema, final boolean boolean2) {
        super("EntitySkeletonSplitFix", schema, boolean2);
    }
    
    @Override
    protected Pair<String, Dynamic<?>> getNewNameAndTag(String string, final Dynamic<?> dynamic) {
        if (Objects.equals(string, "Skeleton")) {
            final int integer4 = dynamic.get("SkeletonType").asInt(0);
            if (integer4 == 1) {
                string = "WitherSkeleton";
            }
            else if (integer4 == 2) {
                string = "Stray";
            }
        }
        return (Pair<String, Dynamic<?>>)Pair.of(string, dynamic);
    }
}
