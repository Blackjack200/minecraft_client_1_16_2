package net.minecraft.util.datafix.fixes;

import java.util.Objects;
import com.mojang.datafixers.schemas.Schema;

public class EntityTippedArrowFix extends SimplestEntityRenameFix {
    public EntityTippedArrowFix(final Schema schema, final boolean boolean2) {
        super("EntityTippedArrowFix", schema, boolean2);
    }
    
    @Override
    protected String rename(final String string) {
        return Objects.equals(string, "TippedArrow") ? "Arrow" : string;
    }
}
