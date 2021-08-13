package net.minecraft.advancements.critereon;

import com.google.common.collect.Sets;
import net.minecraft.advancements.CriterionTriggerInstance;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.level.storage.loot.LootContext;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.Entity;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerPlayer;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import java.util.Set;
import net.minecraft.server.PlayerAdvancements;
import java.util.Map;
import net.minecraft.advancements.CriterionTrigger;

public abstract class SimpleCriterionTrigger<T extends AbstractCriterionTriggerInstance> implements CriterionTrigger<T> {
    private final Map<PlayerAdvancements, Set<Listener<T>>> players;
    
    public SimpleCriterionTrigger() {
        this.players = (Map<PlayerAdvancements, Set<Listener<T>>>)Maps.newIdentityHashMap();
    }
    
    public final void addPlayerListener(final PlayerAdvancements vt, final Listener<T> a) {
        ((Set)this.players.computeIfAbsent(vt, vt -> Sets.newHashSet())).add(a);
    }
    
    public final void removePlayerListener(final PlayerAdvancements vt, final Listener<T> a) {
        final Set<Listener<T>> set4 = (Set<Listener<T>>)this.players.get(vt);
        if (set4 != null) {
            set4.remove(a);
            if (set4.isEmpty()) {
                this.players.remove(vt);
            }
        }
    }
    
    public final void removePlayerListeners(final PlayerAdvancements vt) {
        this.players.remove(vt);
    }
    
    protected abstract T createInstance(final JsonObject jsonObject, final EntityPredicate.Composite b, final DeserializationContext ax);
    
    public final T createInstance(final JsonObject jsonObject, final DeserializationContext ax) {
        final EntityPredicate.Composite b4 = EntityPredicate.Composite.fromJson(jsonObject, "player", ax);
        return this.createInstance(jsonObject, b4, ax);
    }
    
    protected void trigger(final ServerPlayer aah, final Predicate<T> predicate) {
        final PlayerAdvancements vt4 = aah.getAdvancements();
        final Set<Listener<T>> set5 = (Set<Listener<T>>)this.players.get(vt4);
        if (set5 == null || set5.isEmpty()) {
            return;
        }
        final LootContext cys6 = EntityPredicate.createContext(aah, aah);
        List<Listener<T>> list7 = null;
        for (final Listener<T> a9 : set5) {
            final T al10 = a9.getTriggerInstance();
            if (al10.getPlayerPredicate().matches(cys6) && predicate.test(al10)) {
                if (list7 == null) {
                    list7 = (List<Listener<T>>)Lists.newArrayList();
                }
                list7.add(a9);
            }
        }
        if (list7 != null) {
            for (final Listener<T> a9 : list7) {
                a9.run(vt4);
            }
        }
    }
}
