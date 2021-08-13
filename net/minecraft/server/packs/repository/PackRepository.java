package net.minecraft.server.packs.repository;

import net.minecraft.server.packs.PackResources;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.function.Function;
import com.google.common.base.Functions;
import java.util.stream.Collectors;
import java.util.Iterator;
import java.util.function.Consumer;
import com.google.common.collect.Maps;
import java.util.Collection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PackRepository implements AutoCloseable {
    private final Set<RepositorySource> sources;
    private Map<String, Pack> available;
    private List<Pack> selected;
    private final Pack.PackConstructor constructor;
    
    public PackRepository(final Pack.PackConstructor a, final RepositorySource... arr) {
        this.available = (Map<String, Pack>)ImmutableMap.of();
        this.selected = (List<Pack>)ImmutableList.of();
        this.constructor = a;
        this.sources = (Set<RepositorySource>)ImmutableSet.copyOf((Object[])arr);
    }
    
    public PackRepository(final RepositorySource... arr) {
        this(Pack::new, arr);
    }
    
    public void reload() {
        final List<String> list2 = (List<String>)this.selected.stream().map(Pack::getId).collect(ImmutableList.toImmutableList());
        this.close();
        this.available = this.discoverAvailable();
        this.selected = this.rebuildSelected((Collection<String>)list2);
    }
    
    private Map<String, Pack> discoverAvailable() {
        final Map<String, Pack> map2 = (Map<String, Pack>)Maps.newTreeMap();
        for (final RepositorySource abw4 : this.sources) {
            abw4.loadPacks((Consumer<Pack>)(abs -> {
                final Pack pack = (Pack)map2.put(abs.getId(), abs);
            }), this.constructor);
        }
        return (Map<String, Pack>)ImmutableMap.copyOf((Map)map2);
    }
    
    public void setSelected(final Collection<String> collection) {
        this.selected = this.rebuildSelected(collection);
    }
    
    private List<Pack> rebuildSelected(final Collection<String> collection) {
        final List<Pack> list3 = (List<Pack>)this.getAvailablePacks(collection).collect(Collectors.toList());
        for (final Pack abs5 : this.available.values()) {
            if (abs5.isRequired() && !list3.contains(abs5)) {
                abs5.getDefaultPosition().<Pack>insert(list3, abs5, (java.util.function.Function<Pack, Pack>)Functions.identity(), false);
            }
        }
        return (List<Pack>)ImmutableList.copyOf((Collection)list3);
    }
    
    private Stream<Pack> getAvailablePacks(final Collection<String> collection) {
        return (Stream<Pack>)collection.stream().map(this.available::get).filter(Objects::nonNull);
    }
    
    public Collection<String> getAvailableIds() {
        return (Collection<String>)this.available.keySet();
    }
    
    public Collection<Pack> getAvailablePacks() {
        return (Collection<Pack>)this.available.values();
    }
    
    public Collection<String> getSelectedIds() {
        return (Collection<String>)this.selected.stream().map(Pack::getId).collect(ImmutableSet.toImmutableSet());
    }
    
    public Collection<Pack> getSelectedPacks() {
        return (Collection<Pack>)this.selected;
    }
    
    @Nullable
    public Pack getPack(final String string) {
        return (Pack)this.available.get(string);
    }
    
    public void close() {
        this.available.values().forEach(Pack::close);
    }
    
    public boolean isAvailable(final String string) {
        return this.available.containsKey(string);
    }
    
    public List<PackResources> openAllSelected() {
        return (List<PackResources>)this.selected.stream().map(Pack::open).collect(ImmutableList.toImmutableList());
    }
}
