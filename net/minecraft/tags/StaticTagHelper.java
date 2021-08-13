package net.minecraft.tags;

import javax.annotation.Nullable;
import com.google.common.collect.Sets;
import java.util.Collection;
import com.google.common.collect.ImmutableSet;
import java.util.stream.Collectors;
import java.util.Set;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.Lists;
import java.util.function.Function;
import java.util.List;

public class StaticTagHelper<T> {
    private TagCollection<T> source;
    private final List<Wrapper<T>> wrappers;
    private final Function<TagContainer, TagCollection<T>> collectionGetter;
    
    public StaticTagHelper(final Function<TagContainer, TagCollection<T>> function) {
        this.source = TagCollection.<T>empty();
        this.wrappers = (List<Wrapper<T>>)Lists.newArrayList();
        this.collectionGetter = function;
    }
    
    public Tag.Named<T> bind(final String string) {
        final Wrapper<T> a3 = new Wrapper<T>(new ResourceLocation(string));
        this.wrappers.add(a3);
        return a3;
    }
    
    public void resetToEmpty() {
        this.source = TagCollection.<T>empty();
        final Tag<T> aej2 = SetTag.empty();
        this.wrappers.forEach(a -> a.rebind(vk -> aej2));
    }
    
    public void reset(final TagContainer ael) {
        final TagCollection<T> aek3 = (TagCollection<T>)this.collectionGetter.apply(ael);
        this.source = aek3;
        this.wrappers.forEach(a -> a.rebind(aek3::getTag));
    }
    
    public TagCollection<T> getAllTags() {
        return this.source;
    }
    
    public List<? extends Tag.Named<T>> getWrappers() {
        return this.wrappers;
    }
    
    public Set<ResourceLocation> getMissingTags(final TagContainer ael) {
        final TagCollection<T> aek3 = (TagCollection<T>)this.collectionGetter.apply(ael);
        final Set<ResourceLocation> set4 = (Set<ResourceLocation>)this.wrappers.stream().map(Wrapper::getName).collect(Collectors.toSet());
        final ImmutableSet<ResourceLocation> immutableSet5 = (ImmutableSet<ResourceLocation>)ImmutableSet.copyOf((Collection)aek3.getAvailableTags());
        return (Set<ResourceLocation>)Sets.difference((Set)set4, (Set)immutableSet5);
    }
    
    static class Wrapper<T> implements Tag.Named<T> {
        @Nullable
        private Tag<T> tag;
        protected final ResourceLocation name;
        
        private Wrapper(final ResourceLocation vk) {
            this.name = vk;
        }
        
        public ResourceLocation getName() {
            return this.name;
        }
        
        private Tag<T> resolve() {
            if (this.tag == null) {
                throw new IllegalStateException(new StringBuilder().append("Tag ").append(this.name).append(" used before it was bound").toString());
            }
            return this.tag;
        }
        
        void rebind(final Function<ResourceLocation, Tag<T>> function) {
            this.tag = (Tag<T>)function.apply(this.name);
        }
        
        public boolean contains(final T object) {
            return this.resolve().contains(object);
        }
        
        public List<T> getValues() {
            return this.resolve().getValues();
        }
    }
}
