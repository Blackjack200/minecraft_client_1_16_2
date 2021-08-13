package net.minecraft.world.level;

import net.minecraft.server.level.ServerLevel;

public interface CustomSpawner {
    int tick(final ServerLevel aag, final boolean boolean2, final boolean boolean3);
}
