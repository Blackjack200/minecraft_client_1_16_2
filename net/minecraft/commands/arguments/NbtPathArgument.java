package net.minecraft.commands.arguments;

import org.apache.commons.lang3.mutable.MutableBoolean;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CollectionTag;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.Collections;
import java.util.Arrays;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Message;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import com.mojang.brigadier.arguments.ArgumentType;

public class NbtPathArgument implements ArgumentType<NbtPath> {
    private static final Collection<String> EXAMPLES;
    public static final SimpleCommandExceptionType ERROR_INVALID_NODE;
    public static final DynamicCommandExceptionType ERROR_NOTHING_FOUND;
    
    public static NbtPathArgument nbtPath() {
        return new NbtPathArgument();
    }
    
    public static NbtPath getPath(final CommandContext<CommandSourceStack> commandContext, final String string) {
        return (NbtPath)commandContext.getArgument(string, (Class)NbtPath.class);
    }
    
    public NbtPath parse(final StringReader stringReader) throws CommandSyntaxException {
        final List<Node> list3 = (List<Node>)Lists.newArrayList();
        final int integer4 = stringReader.getCursor();
        final Object2IntMap<Node> object2IntMap5 = (Object2IntMap<Node>)new Object2IntOpenHashMap();
        boolean boolean6 = true;
        while (stringReader.canRead() && stringReader.peek() != ' ') {
            final Node i7 = parseNode(stringReader, boolean6);
            list3.add(i7);
            object2IntMap5.put(i7, stringReader.getCursor() - integer4);
            boolean6 = false;
            if (stringReader.canRead()) {
                final char character8 = stringReader.peek();
                if (character8 == ' ' || character8 == '[' || character8 == '{') {
                    continue;
                }
                stringReader.expect('.');
            }
        }
        return new NbtPath(stringReader.getString().substring(integer4, stringReader.getCursor()), (Node[])list3.toArray((Object[])new Node[0]), object2IntMap5);
    }
    
    private static Node parseNode(final StringReader stringReader, final boolean boolean2) throws CommandSyntaxException {
        switch (stringReader.peek()) {
            case '{': {
                if (!boolean2) {
                    throw NbtPathArgument.ERROR_INVALID_NODE.createWithContext((ImmutableStringReader)stringReader);
                }
                final CompoundTag md3 = new TagParser(stringReader).readStruct();
                return new MatchRootObjectNode(md3);
            }
            case '[': {
                stringReader.skip();
                final int integer3 = stringReader.peek();
                if (integer3 == 123) {
                    final CompoundTag md4 = new TagParser(stringReader).readStruct();
                    stringReader.expect(']');
                    return new MatchElementNode(md4);
                }
                if (integer3 == 93) {
                    stringReader.skip();
                    return AllElementsNode.INSTANCE;
                }
                final int integer4 = stringReader.readInt();
                stringReader.expect(']');
                return new IndexedElementNode(integer4);
            }
            case '\"': {
                final String string3 = stringReader.readString();
                return readObjectNode(stringReader, string3);
            }
            default: {
                final String string3 = readUnquotedName(stringReader);
                return readObjectNode(stringReader, string3);
            }
        }
    }
    
    private static Node readObjectNode(final StringReader stringReader, final String string) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '{') {
            final CompoundTag md3 = new TagParser(stringReader).readStruct();
            return new MatchObjectNode(string, md3);
        }
        return new CompoundChildNode(string);
    }
    
    private static String readUnquotedName(final StringReader stringReader) throws CommandSyntaxException {
        final int integer2 = stringReader.getCursor();
        while (stringReader.canRead() && isAllowedInUnquotedName(stringReader.peek())) {
            stringReader.skip();
        }
        if (stringReader.getCursor() == integer2) {
            throw NbtPathArgument.ERROR_INVALID_NODE.createWithContext((ImmutableStringReader)stringReader);
        }
        return stringReader.getString().substring(integer2, stringReader.getCursor());
    }
    
    public Collection<String> getExamples() {
        return NbtPathArgument.EXAMPLES;
    }
    
    private static boolean isAllowedInUnquotedName(final char character) {
        return character != ' ' && character != '\"' && character != '[' && character != ']' && character != '.' && character != '{' && character != '}';
    }
    
    private static Predicate<Tag> createTagPredicate(final CompoundTag md) {
        return (Predicate<Tag>)(mt -> NbtUtils.compareNbt(md, mt, true));
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "foo", "foo.bar", "foo[0]", "[0]", "[]", "{foo=bar}" });
        ERROR_INVALID_NODE = new SimpleCommandExceptionType((Message)new TranslatableComponent("arguments.nbtpath.node.invalid"));
        ERROR_NOTHING_FOUND = new DynamicCommandExceptionType(object -> new TranslatableComponent("arguments.nbtpath.nothing_found", new Object[] { object }));
    }
    
    public static class NbtPath {
        private final String original;
        private final Object2IntMap<Node> nodeToOriginalPosition;
        private final Node[] nodes;
        
        public NbtPath(final String string, final Node[] arr, final Object2IntMap<Node> object2IntMap) {
            this.original = string;
            this.nodes = arr;
            this.nodeToOriginalPosition = object2IntMap;
        }
        
        public List<Tag> get(final Tag mt) throws CommandSyntaxException {
            List<Tag> list3 = (List<Tag>)Collections.singletonList(mt);
            for (final Node i7 : this.nodes) {
                list3 = i7.get(list3);
                if (list3.isEmpty()) {
                    throw this.createNotFoundException(i7);
                }
            }
            return list3;
        }
        
        public int countMatching(final Tag mt) {
            List<Tag> list3 = (List<Tag>)Collections.singletonList(mt);
            for (final Node i7 : this.nodes) {
                list3 = i7.get(list3);
                if (list3.isEmpty()) {
                    return 0;
                }
            }
            return list3.size();
        }
        
        private List<Tag> getOrCreateParents(final Tag mt) throws CommandSyntaxException {
            List<Tag> list3 = (List<Tag>)Collections.singletonList(mt);
            for (int integer4 = 0; integer4 < this.nodes.length - 1; ++integer4) {
                final Node i5 = this.nodes[integer4];
                final int integer5 = integer4 + 1;
                list3 = i5.getOrCreate(list3, (Supplier<Tag>)this.nodes[integer5]::createPreferredParentTag);
                if (list3.isEmpty()) {
                    throw this.createNotFoundException(i5);
                }
            }
            return list3;
        }
        
        public List<Tag> getOrCreate(final Tag mt, final Supplier<Tag> supplier) throws CommandSyntaxException {
            final List<Tag> list4 = this.getOrCreateParents(mt);
            final Node i5 = this.nodes[this.nodes.length - 1];
            return i5.getOrCreate(list4, supplier);
        }
        
        private static int apply(final List<Tag> list, final Function<Tag, Integer> function) {
            return (int)list.stream().map((Function)function).reduce(0, (integer1, integer2) -> integer1 + integer2);
        }
        
        public int set(final Tag mt, final Supplier<Tag> supplier) throws CommandSyntaxException {
            final List<Tag> list4 = this.getOrCreateParents(mt);
            final Node i5 = this.nodes[this.nodes.length - 1];
            return apply(list4, (Function<Tag, Integer>)(mt -> i5.setTag(mt, supplier)));
        }
        
        public int remove(final Tag mt) {
            List<Tag> list3 = (List<Tag>)Collections.singletonList(mt);
            for (int integer4 = 0; integer4 < this.nodes.length - 1; ++integer4) {
                list3 = this.nodes[integer4].get(list3);
            }
            final Node i4 = this.nodes[this.nodes.length - 1];
            return apply(list3, (Function<Tag, Integer>)i4::removeTag);
        }
        
        private CommandSyntaxException createNotFoundException(final Node i) {
            final int integer3 = this.nodeToOriginalPosition.getInt(i);
            return NbtPathArgument.ERROR_NOTHING_FOUND.create(this.original.substring(0, integer3));
        }
        
        public String toString() {
            return this.original;
        }
    }
    
    interface Node {
        void getTag(final Tag mt, final List<Tag> list);
        
        void getOrCreateTag(final Tag mt, final Supplier<Tag> supplier, final List<Tag> list);
        
        Tag createPreferredParentTag();
        
        int setTag(final Tag mt, final Supplier<Tag> supplier);
        
        int removeTag(final Tag mt);
        
        default List<Tag> get(final List<Tag> list) {
            return this.collect(list, (BiConsumer<Tag, List<Tag>>)this::getTag);
        }
        
        default List<Tag> getOrCreate(final List<Tag> list, final Supplier<Tag> supplier) {
            return this.collect(list, (BiConsumer<Tag, List<Tag>>)((mt, list) -> this.getOrCreateTag(mt, supplier, (List<Tag>)list)));
        }
        
        default List<Tag> collect(final List<Tag> list, final BiConsumer<Tag, List<Tag>> biConsumer) {
            final List<Tag> list2 = (List<Tag>)Lists.newArrayList();
            for (final Tag mt6 : list) {
                biConsumer.accept(mt6, list2);
            }
            return list2;
        }
    }
    
    static class CompoundChildNode implements Node {
        private final String name;
        
        public CompoundChildNode(final String string) {
            this.name = string;
        }
        
        public void getTag(final Tag mt, final List<Tag> list) {
            if (mt instanceof CompoundTag) {
                final Tag mt2 = ((CompoundTag)mt).get(this.name);
                if (mt2 != null) {
                    list.add(mt2);
                }
            }
        }
        
        public void getOrCreateTag(final Tag mt, final Supplier<Tag> supplier, final List<Tag> list) {
            if (mt instanceof CompoundTag) {
                final CompoundTag md5 = (CompoundTag)mt;
                Tag mt2;
                if (md5.contains(this.name)) {
                    mt2 = md5.get(this.name);
                }
                else {
                    mt2 = (Tag)supplier.get();
                    md5.put(this.name, mt2);
                }
                list.add(mt2);
            }
        }
        
        public Tag createPreferredParentTag() {
            return new CompoundTag();
        }
        
        public int setTag(final Tag mt, final Supplier<Tag> supplier) {
            if (mt instanceof CompoundTag) {
                final CompoundTag md4 = (CompoundTag)mt;
                final Tag mt2 = (Tag)supplier.get();
                final Tag mt3 = md4.put(this.name, mt2);
                if (!mt2.equals(mt3)) {
                    return 1;
                }
            }
            return 0;
        }
        
        public int removeTag(final Tag mt) {
            if (mt instanceof CompoundTag) {
                final CompoundTag md3 = (CompoundTag)mt;
                if (md3.contains(this.name)) {
                    md3.remove(this.name);
                    return 1;
                }
            }
            return 0;
        }
    }
    
    static class IndexedElementNode implements Node {
        private final int index;
        
        public IndexedElementNode(final int integer) {
            this.index = integer;
        }
        
        public void getTag(final Tag mt, final List<Tag> list) {
            if (mt instanceof CollectionTag) {
                final CollectionTag<?> mc4 = mt;
                final int integer5 = mc4.size();
                final int integer6 = (this.index < 0) ? (integer5 + this.index) : this.index;
                if (0 <= integer6 && integer6 < integer5) {
                    list.add(mc4.get(integer6));
                }
            }
        }
        
        public void getOrCreateTag(final Tag mt, final Supplier<Tag> supplier, final List<Tag> list) {
            this.getTag(mt, list);
        }
        
        public Tag createPreferredParentTag() {
            return new ListTag();
        }
        
        public int setTag(final Tag mt, final Supplier<Tag> supplier) {
            if (mt instanceof CollectionTag) {
                final CollectionTag<?> mc4 = mt;
                final int integer5 = mc4.size();
                final int integer6 = (this.index < 0) ? (integer5 + this.index) : this.index;
                if (0 <= integer6 && integer6 < integer5) {
                    final Tag mt2 = (Tag)mc4.get(integer6);
                    final Tag mt3 = (Tag)supplier.get();
                    if (!mt3.equals(mt2) && mc4.setTag(integer6, mt3)) {
                        return 1;
                    }
                }
            }
            return 0;
        }
        
        public int removeTag(final Tag mt) {
            if (mt instanceof CollectionTag) {
                final CollectionTag<?> mc3 = mt;
                final int integer4 = mc3.size();
                final int integer5 = (this.index < 0) ? (integer4 + this.index) : this.index;
                if (0 <= integer5 && integer5 < integer4) {
                    mc3.remove(integer5);
                    return 1;
                }
            }
            return 0;
        }
    }
    
    static class MatchElementNode implements Node {
        private final CompoundTag pattern;
        private final Predicate<Tag> predicate;
        
        public MatchElementNode(final CompoundTag md) {
            this.pattern = md;
            this.predicate = createTagPredicate(md);
        }
        
        public void getTag(final Tag mt, final List<Tag> list) {
            if (mt instanceof ListTag) {
                final ListTag mj4 = (ListTag)mt;
                mj4.stream().filter((Predicate)this.predicate).forEach(list::add);
            }
        }
        
        public void getOrCreateTag(final Tag mt, final Supplier<Tag> supplier, final List<Tag> list) {
            final MutableBoolean mutableBoolean5 = new MutableBoolean();
            if (mt instanceof ListTag) {
                final ListTag mj6 = (ListTag)mt;
                mj6.stream().filter((Predicate)this.predicate).forEach(mt -> {
                    list.add(mt);
                    mutableBoolean5.setTrue();
                });
                if (mutableBoolean5.isFalse()) {
                    final CompoundTag md7 = this.pattern.copy();
                    mj6.add(md7);
                    list.add(md7);
                }
            }
        }
        
        public Tag createPreferredParentTag() {
            return new ListTag();
        }
        
        public int setTag(final Tag mt, final Supplier<Tag> supplier) {
            int integer4 = 0;
            if (mt instanceof ListTag) {
                final ListTag mj5 = (ListTag)mt;
                final int integer5 = mj5.size();
                if (integer5 == 0) {
                    mj5.add(supplier.get());
                    ++integer4;
                }
                else {
                    for (int integer6 = 0; integer6 < integer5; ++integer6) {
                        final Tag mt2 = mj5.get(integer6);
                        if (this.predicate.test(mt2)) {
                            final Tag mt3 = (Tag)supplier.get();
                            if (!mt3.equals(mt2) && mj5.setTag(integer6, mt3)) {
                                ++integer4;
                            }
                        }
                    }
                }
            }
            return integer4;
        }
        
        public int removeTag(final Tag mt) {
            int integer3 = 0;
            if (mt instanceof ListTag) {
                final ListTag mj4 = (ListTag)mt;
                for (int integer4 = mj4.size() - 1; integer4 >= 0; --integer4) {
                    if (this.predicate.test(mj4.get(integer4))) {
                        mj4.remove(integer4);
                        ++integer3;
                    }
                }
            }
            return integer3;
        }
    }
    
    static class AllElementsNode implements Node {
        public static final AllElementsNode INSTANCE;
        
        private AllElementsNode() {
        }
        
        public void getTag(final Tag mt, final List<Tag> list) {
            if (mt instanceof CollectionTag) {
                list.addAll((Collection)mt);
            }
        }
        
        public void getOrCreateTag(final Tag mt, final Supplier<Tag> supplier, final List<Tag> list) {
            if (mt instanceof CollectionTag) {
                final CollectionTag<?> mc5 = mt;
                if (mc5.isEmpty()) {
                    final Tag mt2 = (Tag)supplier.get();
                    if (mc5.addTag(0, mt2)) {
                        list.add(mt2);
                    }
                }
                else {
                    list.addAll((Collection)mc5);
                }
            }
        }
        
        public Tag createPreferredParentTag() {
            return new ListTag();
        }
        
        public int setTag(final Tag mt, final Supplier<Tag> supplier) {
            if (!(mt instanceof CollectionTag)) {
                return 0;
            }
            final CollectionTag<?> mc4 = mt;
            final int integer5 = mc4.size();
            if (integer5 == 0) {
                mc4.addTag(0, (Tag)supplier.get());
                return 1;
            }
            final Tag mt2 = (Tag)supplier.get();
            final int integer6 = integer5 - (int)mc4.stream().filter(mt2::equals).count();
            if (integer6 == 0) {
                return 0;
            }
            mc4.clear();
            if (!mc4.addTag(0, mt2)) {
                return 0;
            }
            for (int integer7 = 1; integer7 < integer5; ++integer7) {
                mc4.addTag(integer7, (Tag)supplier.get());
            }
            return integer6;
        }
        
        public int removeTag(final Tag mt) {
            if (mt instanceof CollectionTag) {
                final CollectionTag<?> mc3 = mt;
                final int integer4 = mc3.size();
                if (integer4 > 0) {
                    mc3.clear();
                    return integer4;
                }
            }
            return 0;
        }
        
        static {
            INSTANCE = new AllElementsNode();
        }
    }
    
    static class MatchObjectNode implements Node {
        private final String name;
        private final CompoundTag pattern;
        private final Predicate<Tag> predicate;
        
        public MatchObjectNode(final String string, final CompoundTag md) {
            this.name = string;
            this.pattern = md;
            this.predicate = createTagPredicate(md);
        }
        
        public void getTag(final Tag mt, final List<Tag> list) {
            if (mt instanceof CompoundTag) {
                final Tag mt2 = ((CompoundTag)mt).get(this.name);
                if (this.predicate.test(mt2)) {
                    list.add(mt2);
                }
            }
        }
        
        public void getOrCreateTag(final Tag mt, final Supplier<Tag> supplier, final List<Tag> list) {
            if (mt instanceof CompoundTag) {
                final CompoundTag md5 = (CompoundTag)mt;
                Tag mt2 = md5.get(this.name);
                if (mt2 == null) {
                    mt2 = this.pattern.copy();
                    md5.put(this.name, mt2);
                    list.add(mt2);
                }
                else if (this.predicate.test(mt2)) {
                    list.add(mt2);
                }
            }
        }
        
        public Tag createPreferredParentTag() {
            return new CompoundTag();
        }
        
        public int setTag(final Tag mt, final Supplier<Tag> supplier) {
            if (mt instanceof CompoundTag) {
                final CompoundTag md4 = (CompoundTag)mt;
                final Tag mt2 = md4.get(this.name);
                if (this.predicate.test(mt2)) {
                    final Tag mt3 = (Tag)supplier.get();
                    if (!mt3.equals(mt2)) {
                        md4.put(this.name, mt3);
                        return 1;
                    }
                }
            }
            return 0;
        }
        
        public int removeTag(final Tag mt) {
            if (mt instanceof CompoundTag) {
                final CompoundTag md3 = (CompoundTag)mt;
                final Tag mt2 = md3.get(this.name);
                if (this.predicate.test(mt2)) {
                    md3.remove(this.name);
                    return 1;
                }
            }
            return 0;
        }
    }
    
    static class MatchRootObjectNode implements Node {
        private final Predicate<Tag> predicate;
        
        public MatchRootObjectNode(final CompoundTag md) {
            this.predicate = createTagPredicate(md);
        }
        
        public void getTag(final Tag mt, final List<Tag> list) {
            if (mt instanceof CompoundTag && this.predicate.test(mt)) {
                list.add(mt);
            }
        }
        
        public void getOrCreateTag(final Tag mt, final Supplier<Tag> supplier, final List<Tag> list) {
            this.getTag(mt, list);
        }
        
        public Tag createPreferredParentTag() {
            return new CompoundTag();
        }
        
        public int setTag(final Tag mt, final Supplier<Tag> supplier) {
            return 0;
        }
        
        public int removeTag(final Tag mt) {
            return 0;
        }
    }
}
