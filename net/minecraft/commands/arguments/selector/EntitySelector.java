package net.minecraft.commands.arguments.selector;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.server.level.ServerLevel;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collections;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.entity.EntityType;
import java.util.UUID;
import java.util.List;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.function.Function;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.world.entity.Entity;
import java.util.function.Predicate;

public class EntitySelector {
    private final int maxResults;
    private final boolean includesEntities;
    private final boolean worldLimited;
    private final Predicate<Entity> predicate;
    private final MinMaxBounds.Floats range;
    private final Function<Vec3, Vec3> position;
    @Nullable
    private final AABB aabb;
    private final BiConsumer<Vec3, List<? extends Entity>> order;
    private final boolean currentEntity;
    @Nullable
    private final String playerName;
    @Nullable
    private final UUID entityUUID;
    @Nullable
    private final EntityType<?> type;
    private final boolean usesSelector;
    
    public EntitySelector(final int integer, final boolean boolean2, final boolean boolean3, final Predicate<Entity> predicate, final MinMaxBounds.Floats c, final Function<Vec3, Vec3> function, @Nullable final AABB dcf, final BiConsumer<Vec3, List<? extends Entity>> biConsumer, final boolean boolean9, @Nullable final String string, @Nullable final UUID uUID, @Nullable final EntityType<?> aqb, final boolean boolean13) {
        this.maxResults = integer;
        this.includesEntities = boolean2;
        this.worldLimited = boolean3;
        this.predicate = predicate;
        this.range = c;
        this.position = function;
        this.aabb = dcf;
        this.order = biConsumer;
        this.currentEntity = boolean9;
        this.playerName = string;
        this.entityUUID = uUID;
        this.type = aqb;
        this.usesSelector = boolean13;
    }
    
    public int getMaxResults() {
        return this.maxResults;
    }
    
    public boolean includesEntities() {
        return this.includesEntities;
    }
    
    public boolean isSelfSelector() {
        return this.currentEntity;
    }
    
    public boolean isWorldLimited() {
        return this.worldLimited;
    }
    
    private void checkPermissions(final CommandSourceStack db) throws CommandSyntaxException {
        if (this.usesSelector && !db.hasPermission(2)) {
            throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();
        }
    }
    
    public Entity findSingleEntity(final CommandSourceStack db) throws CommandSyntaxException {
        this.checkPermissions(db);
        final List<? extends Entity> list3 = this.findEntities(db);
        if (list3.isEmpty()) {
            throw EntityArgument.NO_ENTITIES_FOUND.create();
        }
        if (list3.size() > 1) {
            throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
        }
        return (Entity)list3.get(0);
    }
    
    public List<? extends Entity> findEntities(final CommandSourceStack db) throws CommandSyntaxException {
        this.checkPermissions(db);
        if (!this.includesEntities) {
            return this.findPlayers(db);
        }
        if (this.playerName != null) {
            final ServerPlayer aah3 = db.getServer().getPlayerList().getPlayerByName(this.playerName);
            if (aah3 == null) {
                return Collections.emptyList();
            }
            return Lists.newArrayList((Object[])new ServerPlayer[] { aah3 });
        }
        else {
            if (this.entityUUID != null) {
                for (final ServerLevel aag4 : db.getServer().getAllLevels()) {
                    final Entity apx5 = aag4.getEntity(this.entityUUID);
                    if (apx5 != null) {
                        return Lists.newArrayList((Object[])new Entity[] { apx5 });
                    }
                }
                return Collections.emptyList();
            }
            final Vec3 dck3 = (Vec3)this.position.apply(db.getPosition());
            final Predicate<Entity> predicate4 = this.getPredicate(dck3);
            if (!this.currentEntity) {
                final List<Entity> list5 = (List<Entity>)Lists.newArrayList();
                if (this.isWorldLimited()) {
                    this.addEntities(list5, db.getLevel(), dck3, predicate4);
                }
                else {
                    for (final ServerLevel aag5 : db.getServer().getAllLevels()) {
                        this.addEntities(list5, aag5, dck3, predicate4);
                    }
                }
                return this.sortAndLimit(dck3, (java.util.List<? extends Entity>)list5);
            }
            if (db.getEntity() != null && predicate4.test(db.getEntity())) {
                return Lists.newArrayList((Object[])new Entity[] { db.getEntity() });
            }
            return Collections.emptyList();
        }
    }
    
    private void addEntities(final List<Entity> list, final ServerLevel aag, final Vec3 dck, final Predicate<Entity> predicate) {
        if (this.aabb != null) {
            list.addAll((Collection)aag.getEntities(this.type, this.aabb.move(dck), predicate));
        }
        else {
            list.addAll((Collection)aag.getEntities(this.type, predicate));
        }
    }
    
    public ServerPlayer findSinglePlayer(final CommandSourceStack db) throws CommandSyntaxException {
        this.checkPermissions(db);
        final List<ServerPlayer> list3 = this.findPlayers(db);
        if (list3.size() != 1) {
            throw EntityArgument.NO_PLAYERS_FOUND.create();
        }
        return (ServerPlayer)list3.get(0);
    }
    
    public List<ServerPlayer> findPlayers(final CommandSourceStack db) throws CommandSyntaxException {
        this.checkPermissions(db);
        if (this.playerName != null) {
            final ServerPlayer aah3 = db.getServer().getPlayerList().getPlayerByName(this.playerName);
            if (aah3 == null) {
                return (List<ServerPlayer>)Collections.emptyList();
            }
            return (List<ServerPlayer>)Lists.newArrayList((Object[])new ServerPlayer[] { aah3 });
        }
        else if (this.entityUUID != null) {
            final ServerPlayer aah3 = db.getServer().getPlayerList().getPlayer(this.entityUUID);
            if (aah3 == null) {
                return (List<ServerPlayer>)Collections.emptyList();
            }
            return (List<ServerPlayer>)Lists.newArrayList((Object[])new ServerPlayer[] { aah3 });
        }
        else {
            final Vec3 dck3 = (Vec3)this.position.apply(db.getPosition());
            final Predicate<Entity> predicate4 = this.getPredicate(dck3);
            if (this.currentEntity) {
                if (db.getEntity() instanceof ServerPlayer) {
                    final ServerPlayer aah4 = (ServerPlayer)db.getEntity();
                    if (predicate4.test(aah4)) {
                        return (List<ServerPlayer>)Lists.newArrayList((Object[])new ServerPlayer[] { aah4 });
                    }
                }
                return (List<ServerPlayer>)Collections.emptyList();
            }
            List<ServerPlayer> list5;
            if (this.isWorldLimited()) {
                list5 = db.getLevel().getPlayers(predicate4::test);
            }
            else {
                list5 = (List<ServerPlayer>)Lists.newArrayList();
                for (final ServerPlayer aah5 : db.getServer().getPlayerList().getPlayers()) {
                    if (predicate4.test(aah5)) {
                        list5.add(aah5);
                    }
                }
            }
            return this.<ServerPlayer>sortAndLimit(dck3, list5);
        }
    }
    
    private Predicate<Entity> getPredicate(final Vec3 dck) {
        Predicate<Entity> predicate3 = this.predicate;
        if (this.aabb != null) {
            final AABB dcf4 = this.aabb.move(dck);
            predicate3 = (Predicate<Entity>)predicate3.and(apx -> dcf4.intersects(apx.getBoundingBox()));
        }
        if (!this.range.isAny()) {
            predicate3 = (Predicate<Entity>)predicate3.and(apx -> this.range.matchesSqr(apx.distanceToSqr(dck)));
        }
        return predicate3;
    }
    
    private <T extends Entity> List<T> sortAndLimit(final Vec3 dck, final List<T> list) {
        if (list.size() > 1) {
            this.order.accept(dck, list);
        }
        return (List<T>)list.subList(0, Math.min(this.maxResults, list.size()));
    }
    
    public static MutableComponent joinNames(final List<? extends Entity> list) {
        return ComponentUtils.formatList((java.util.Collection<Object>)list, (java.util.function.Function<Object, Component>)Entity::getDisplayName);
    }
}
