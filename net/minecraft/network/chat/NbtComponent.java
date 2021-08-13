package net.minecraft.network.chat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import java.util.Objects;
import java.util.List;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.arguments.selector.EntitySelector;
import org.apache.logging.log4j.LogManager;
import com.google.common.base.Joiner;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import java.util.stream.Stream;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.NbtPathArgument;
import org.apache.logging.log4j.Logger;

public abstract class NbtComponent extends BaseComponent implements ContextAwareComponent {
    private static final Logger LOGGER;
    protected final boolean interpreting;
    protected final String nbtPathPattern;
    @Nullable
    protected final NbtPathArgument.NbtPath compiledNbtPath;
    
    @Nullable
    private static NbtPathArgument.NbtPath compileNbtPath(final String string) {
        try {
            return new NbtPathArgument().parse(new StringReader(string));
        }
        catch (CommandSyntaxException commandSyntaxException2) {
            return null;
        }
    }
    
    public NbtComponent(final String string, final boolean boolean2) {
        this(string, compileNbtPath(string), boolean2);
    }
    
    protected NbtComponent(final String string, @Nullable final NbtPathArgument.NbtPath h, final boolean boolean3) {
        this.nbtPathPattern = string;
        this.compiledNbtPath = h;
        this.interpreting = boolean3;
    }
    
    protected abstract Stream<CompoundTag> getData(final CommandSourceStack db) throws CommandSyntaxException;
    
    public String getNbtPath() {
        return this.nbtPathPattern;
    }
    
    public boolean isInterpreting() {
        return this.interpreting;
    }
    
    @Override
    public MutableComponent resolve(@Nullable final CommandSourceStack db, @Nullable final Entity apx, final int integer) throws CommandSyntaxException {
        if (db == null || this.compiledNbtPath == null) {
            return new TextComponent("");
        }
        final Stream<String> stream5 = (Stream<String>)this.getData(db).flatMap(md -> {
            try {
                return this.compiledNbtPath.get(md).stream();
            }
            catch (CommandSyntaxException commandSyntaxException3) {
                return Stream.empty();
            }
        }).map(Tag::getAsString);
        if (this.interpreting) {
            return (MutableComponent)stream5.flatMap(string -> {
                try {
                    final MutableComponent nx5 = Component.Serializer.fromJson(string);
                    return Stream.of(ComponentUtils.updateForEntity(db, nx5, apx, integer));
                }
                catch (Exception exception5) {
                    NbtComponent.LOGGER.warn("Failed to parse component: " + string, (Throwable)exception5);
                    return Stream.of((Object[])new MutableComponent[0]);
                }
            }).reduce((nx1, nx2) -> nx1.append(", ").append(nx2)).orElse(new TextComponent(""));
        }
        return new TextComponent(Joiner.on(", ").join(stream5.iterator()));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class EntityNbtComponent extends NbtComponent {
        private final String selectorPattern;
        @Nullable
        private final EntitySelector compiledSelector;
        
        public EntityNbtComponent(final String string1, final boolean boolean2, final String string3) {
            super(string1, boolean2);
            this.selectorPattern = string3;
            this.compiledSelector = compileSelector(string3);
        }
        
        @Nullable
        private static EntitySelector compileSelector(final String string) {
            try {
                final EntitySelectorParser fd2 = new EntitySelectorParser(new StringReader(string));
                return fd2.parse();
            }
            catch (CommandSyntaxException commandSyntaxException2) {
                return null;
            }
        }
        
        private EntityNbtComponent(final String string1, @Nullable final NbtPathArgument.NbtPath h, final boolean boolean3, final String string4, @Nullable final EntitySelector fc) {
            super(string1, h, boolean3);
            this.selectorPattern = string4;
            this.compiledSelector = fc;
        }
        
        public String getSelector() {
            return this.selectorPattern;
        }
        
        @Override
        public EntityNbtComponent plainCopy() {
            return new EntityNbtComponent(this.nbtPathPattern, this.compiledNbtPath, this.interpreting, this.selectorPattern, this.compiledSelector);
        }
        
        @Override
        protected Stream<CompoundTag> getData(final CommandSourceStack db) throws CommandSyntaxException {
            if (this.compiledSelector != null) {
                final List<? extends Entity> list3 = this.compiledSelector.findEntities(db);
                return (Stream<CompoundTag>)list3.stream().map(NbtPredicate::getEntityTagToCompare);
            }
            return (Stream<CompoundTag>)Stream.empty();
        }
        
        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object instanceof EntityNbtComponent) {
                final EntityNbtComponent b3 = (EntityNbtComponent)object;
                return Objects.equals(this.selectorPattern, b3.selectorPattern) && Objects.equals(this.nbtPathPattern, b3.nbtPathPattern) && super.equals(object);
            }
            return false;
        }
        
        @Override
        public String toString() {
            return "EntityNbtComponent{selector='" + this.selectorPattern + '\'' + "path='" + this.nbtPathPattern + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
        }
    }
    
    public static class BlockNbtComponent extends NbtComponent {
        private final String posPattern;
        @Nullable
        private final Coordinates compiledPos;
        
        public BlockNbtComponent(final String string1, final boolean boolean2, final String string3) {
            super(string1, boolean2);
            this.posPattern = string3;
            this.compiledPos = this.compilePos(this.posPattern);
        }
        
        @Nullable
        private Coordinates compilePos(final String string) {
            try {
                return BlockPosArgument.blockPos().parse(new StringReader(string));
            }
            catch (CommandSyntaxException commandSyntaxException3) {
                return null;
            }
        }
        
        private BlockNbtComponent(final String string1, @Nullable final NbtPathArgument.NbtPath h, final boolean boolean3, final String string4, @Nullable final Coordinates em) {
            super(string1, h, boolean3);
            this.posPattern = string4;
            this.compiledPos = em;
        }
        
        @Nullable
        public String getPos() {
            return this.posPattern;
        }
        
        @Override
        public BlockNbtComponent plainCopy() {
            return new BlockNbtComponent(this.nbtPathPattern, this.compiledNbtPath, this.interpreting, this.posPattern, this.compiledPos);
        }
        
        @Override
        protected Stream<CompoundTag> getData(final CommandSourceStack db) {
            if (this.compiledPos != null) {
                final ServerLevel aag3 = db.getLevel();
                final BlockPos fx4 = this.compiledPos.getBlockPos(db);
                if (aag3.isLoaded(fx4)) {
                    final BlockEntity ccg5 = aag3.getBlockEntity(fx4);
                    if (ccg5 != null) {
                        return (Stream<CompoundTag>)Stream.of(ccg5.save(new CompoundTag()));
                    }
                }
            }
            return (Stream<CompoundTag>)Stream.empty();
        }
        
        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object instanceof BlockNbtComponent) {
                final BlockNbtComponent a3 = (BlockNbtComponent)object;
                return Objects.equals(this.posPattern, a3.posPattern) && Objects.equals(this.nbtPathPattern, a3.nbtPathPattern) && super.equals(object);
            }
            return false;
        }
        
        @Override
        public String toString() {
            return "BlockPosArgument{pos='" + this.posPattern + '\'' + "path='" + this.nbtPathPattern + '\'' + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
        }
    }
    
    public static class StorageNbtComponent extends NbtComponent {
        private final ResourceLocation id;
        
        public StorageNbtComponent(final String string, final boolean boolean2, final ResourceLocation vk) {
            super(string, boolean2);
            this.id = vk;
        }
        
        public StorageNbtComponent(final String string, @Nullable final NbtPathArgument.NbtPath h, final boolean boolean3, final ResourceLocation vk) {
            super(string, h, boolean3);
            this.id = vk;
        }
        
        public ResourceLocation getId() {
            return this.id;
        }
        
        @Override
        public StorageNbtComponent plainCopy() {
            return new StorageNbtComponent(this.nbtPathPattern, this.compiledNbtPath, this.interpreting, this.id);
        }
        
        @Override
        protected Stream<CompoundTag> getData(final CommandSourceStack db) {
            final CompoundTag md3 = db.getServer().getCommandStorage().get(this.id);
            return (Stream<CompoundTag>)Stream.of(md3);
        }
        
        @Override
        public boolean equals(final Object object) {
            if (this == object) {
                return true;
            }
            if (object instanceof StorageNbtComponent) {
                final StorageNbtComponent c3 = (StorageNbtComponent)object;
                return Objects.equals(this.id, c3.id) && Objects.equals(this.nbtPathPattern, c3.nbtPathPattern) && super.equals(object);
            }
            return false;
        }
        
        @Override
        public String toString() {
            return new StringBuilder().append("StorageNbtComponent{id='").append(this.id).append('\'').append("path='").append(this.nbtPathPattern).append('\'').append(", siblings=").append(this.siblings).append(", style=").append(this.getStyle()).append('}').toString();
        }
    }
}
