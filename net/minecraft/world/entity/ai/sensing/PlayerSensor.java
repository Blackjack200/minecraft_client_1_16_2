package net.minecraft.world.entity.ai.sensing;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.Brain;
import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.function.Predicate;
import net.minecraft.world.entity.EntitySelector;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import java.util.Set;
import net.minecraft.world.entity.LivingEntity;

public class PlayerSensor extends Sensor<LivingEntity> {
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
    }
    
    @Override
    protected void doTick(final ServerLevel aag, final LivingEntity aqj) {
        final List<Player> list4 = (List<Player>)aag.players().stream().filter((Predicate)EntitySelector.NO_SPECTATORS).filter(aah -> aqj.closerThan(aah, 16.0)).sorted(Comparator.comparingDouble(aqj::distanceToSqr)).collect(Collectors.toList());
        final Brain<?> arc5 = aqj.getBrain();
        arc5.<List<Player>>setMemory(MemoryModuleType.NEAREST_PLAYERS, list4);
        final List<Player> list5 = (List<Player>)list4.stream().filter(bft -> Sensor.isEntityTargetable(aqj, bft)).collect(Collectors.toList());
        arc5.<Player>setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, list5.isEmpty() ? null : ((Player)list5.get(0)));
        final Optional<Player> optional7 = (Optional<Player>)list5.stream().filter((Predicate)EntitySelector.ATTACK_ALLOWED).findFirst();
        arc5.<Player>setMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, optional7);
    }
}
