package net.minecraft.network.chat;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.entity.Entity;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;

public interface ContextAwareComponent {
    MutableComponent resolve(@Nullable final CommandSourceStack db, @Nullable final Entity apx, final int integer) throws CommandSyntaxException;
}
