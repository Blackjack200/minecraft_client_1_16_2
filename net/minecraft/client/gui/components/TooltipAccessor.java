package net.minecraft.client.gui.components;

import net.minecraft.util.FormattedCharSequence;
import java.util.List;
import java.util.Optional;

public interface TooltipAccessor {
    Optional<List<FormattedCharSequence>> getTooltip();
}
