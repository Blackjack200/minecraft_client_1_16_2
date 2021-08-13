package net.minecraft.client.gui.screens.packs;

import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackCompatibility;
import com.google.common.collect.ImmutableList;
import java.util.stream.Stream;
import java.util.Collection;
import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.server.packs.repository.Pack;
import java.util.List;
import net.minecraft.server.packs.repository.PackRepository;

public class PackSelectionModel {
    private final PackRepository repository;
    private final List<Pack> selected;
    private final List<Pack> unselected;
    private final Function<Pack, ResourceLocation> iconGetter;
    private final Runnable onListChanged;
    private final Consumer<PackRepository> output;
    
    public PackSelectionModel(final Runnable runnable, final Function<Pack, ResourceLocation> function, final PackRepository abu, final Consumer<PackRepository> consumer) {
        this.onListChanged = runnable;
        this.iconGetter = function;
        this.repository = abu;
        Collections.reverse((List)(this.selected = (List<Pack>)Lists.newArrayList((Iterable)abu.getSelectedPacks())));
        (this.unselected = (List<Pack>)Lists.newArrayList((Iterable)abu.getAvailablePacks())).removeAll((Collection)this.selected);
        this.output = consumer;
    }
    
    public Stream<Entry> getUnselected() {
        return (Stream<Entry>)this.unselected.stream().map(abs -> new UnselectedPackEntry(abs));
    }
    
    public Stream<Entry> getSelected() {
        return (Stream<Entry>)this.selected.stream().map(abs -> new SelectedPackEntry(abs));
    }
    
    public void commit() {
        this.repository.setSelected((Collection<String>)Lists.reverse((List)this.selected).stream().map(Pack::getId).collect(ImmutableList.toImmutableList()));
        this.output.accept(this.repository);
    }
    
    public void findNewPacks() {
        this.repository.reload();
        this.selected.retainAll((Collection)this.repository.getAvailablePacks());
        this.unselected.clear();
        this.unselected.addAll((Collection)this.repository.getAvailablePacks());
        this.unselected.removeAll((Collection)this.selected);
    }
    
    public interface Entry {
        ResourceLocation getIconTexture();
        
        PackCompatibility getCompatibility();
        
        Component getTitle();
        
        Component getDescription();
        
        PackSource getPackSource();
        
        default Component getExtendedDescription() {
            return this.getPackSource().decorate(this.getDescription());
        }
        
        boolean isFixedPosition();
        
        boolean isRequired();
        
        void select();
        
        void unselect();
        
        void moveUp();
        
        void moveDown();
        
        boolean isSelected();
        
        default boolean canSelect() {
            return !this.isSelected();
        }
        
        default boolean canUnselect() {
            return this.isSelected() && !this.isRequired();
        }
        
        boolean canMoveUp();
        
        boolean canMoveDown();
    }
    
    abstract class EntryBase implements Entry {
        private final Pack pack;
        
        public EntryBase(final Pack abs) {
            this.pack = abs;
        }
        
        protected abstract List<Pack> getSelfList();
        
        protected abstract List<Pack> getOtherList();
        
        public ResourceLocation getIconTexture() {
            return (ResourceLocation)PackSelectionModel.this.iconGetter.apply(this.pack);
        }
        
        public PackCompatibility getCompatibility() {
            return this.pack.getCompatibility();
        }
        
        public Component getTitle() {
            return this.pack.getTitle();
        }
        
        public Component getDescription() {
            return this.pack.getDescription();
        }
        
        public PackSource getPackSource() {
            return this.pack.getPackSource();
        }
        
        public boolean isFixedPosition() {
            return this.pack.isFixedPosition();
        }
        
        public boolean isRequired() {
            return this.pack.isRequired();
        }
        
        protected void toggleSelection() {
            this.getSelfList().remove(this.pack);
            this.pack.getDefaultPosition().<Pack>insert(this.getOtherList(), this.pack, (java.util.function.Function<Pack, Pack>)Function.identity(), true);
            PackSelectionModel.this.onListChanged.run();
        }
        
        protected void move(final int integer) {
            final List<Pack> list3 = this.getSelfList();
            final int integer2 = list3.indexOf(this.pack);
            list3.remove(integer2);
            list3.add(integer2 + integer, this.pack);
            PackSelectionModel.this.onListChanged.run();
        }
        
        public boolean canMoveUp() {
            final List<Pack> list2 = this.getSelfList();
            final int integer3 = list2.indexOf(this.pack);
            return integer3 > 0 && !((Pack)list2.get(integer3 - 1)).isFixedPosition();
        }
        
        public void moveUp() {
            this.move(-1);
        }
        
        public boolean canMoveDown() {
            final List<Pack> list2 = this.getSelfList();
            final int integer3 = list2.indexOf(this.pack);
            return integer3 >= 0 && integer3 < list2.size() - 1 && !((Pack)list2.get(integer3 + 1)).isFixedPosition();
        }
        
        public void moveDown() {
            this.move(1);
        }
    }
    
    class SelectedPackEntry extends EntryBase {
        public SelectedPackEntry(final Pack abs) {
            super(abs);
        }
        
        @Override
        protected List<Pack> getSelfList() {
            return PackSelectionModel.this.selected;
        }
        
        @Override
        protected List<Pack> getOtherList() {
            return PackSelectionModel.this.unselected;
        }
        
        public boolean isSelected() {
            return true;
        }
        
        public void select() {
        }
        
        public void unselect() {
            this.toggleSelection();
        }
    }
    
    class UnselectedPackEntry extends EntryBase {
        public UnselectedPackEntry(final Pack abs) {
            super(abs);
        }
        
        @Override
        protected List<Pack> getSelfList() {
            return PackSelectionModel.this.unselected;
        }
        
        @Override
        protected List<Pack> getOtherList() {
            return PackSelectionModel.this.selected;
        }
        
        public boolean isSelected() {
            return false;
        }
        
        public void select() {
            this.toggleSelection();
        }
        
        public void unselect() {
        }
    }
}
