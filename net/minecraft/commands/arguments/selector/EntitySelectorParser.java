package net.minecraft.commands.arguments.selector;

import com.google.common.primitives.Doubles;
import java.util.Collections;
import net.minecraft.server.level.ServerPlayer;
import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.ImmutableStringReader;
import net.minecraft.util.Mth;
import java.util.function.ToDoubleFunction;
import java.util.function.Function;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.EntityType;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.advancements.critereon.WrappedMinMaxBounds;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.MinMaxBounds;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.function.BiFunction;
import net.minecraft.world.entity.Entity;
import java.util.List;
import net.minecraft.world.phys.Vec3;
import java.util.function.BiConsumer;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class EntitySelectorParser {
    public static final SimpleCommandExceptionType ERROR_INVALID_NAME_OR_UUID;
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_SELECTOR_TYPE;
    public static final SimpleCommandExceptionType ERROR_SELECTORS_NOT_ALLOWED;
    public static final SimpleCommandExceptionType ERROR_MISSING_SELECTOR_TYPE;
    public static final SimpleCommandExceptionType ERROR_EXPECTED_END_OF_OPTIONS;
    public static final DynamicCommandExceptionType ERROR_EXPECTED_OPTION_VALUE;
    public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_ARBITRARY;
    public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_NEAREST;
    public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_FURTHEST;
    public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_RANDOM;
    public static final BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> SUGGEST_NOTHING;
    private final StringReader reader;
    private final boolean allowSelectors;
    private int maxResults;
    private boolean includesEntities;
    private boolean worldLimited;
    private MinMaxBounds.Floats distance;
    private MinMaxBounds.Ints level;
    @Nullable
    private Double x;
    @Nullable
    private Double y;
    @Nullable
    private Double z;
    @Nullable
    private Double deltaX;
    @Nullable
    private Double deltaY;
    @Nullable
    private Double deltaZ;
    private WrappedMinMaxBounds rotX;
    private WrappedMinMaxBounds rotY;
    private Predicate<Entity> predicate;
    private BiConsumer<Vec3, List<? extends Entity>> order;
    private boolean currentEntity;
    @Nullable
    private String playerName;
    private int startPosition;
    @Nullable
    private UUID entityUUID;
    private BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> suggestions;
    private boolean hasNameEquals;
    private boolean hasNameNotEquals;
    private boolean isLimited;
    private boolean isSorted;
    private boolean hasGamemodeEquals;
    private boolean hasGamemodeNotEquals;
    private boolean hasTeamEquals;
    private boolean hasTeamNotEquals;
    @Nullable
    private EntityType<?> type;
    private boolean typeInverse;
    private boolean hasScores;
    private boolean hasAdvancements;
    private boolean usesSelectors;
    
    public EntitySelectorParser(final StringReader stringReader) {
        this(stringReader, true);
    }
    
    public EntitySelectorParser(final StringReader stringReader, final boolean boolean2) {
        this.distance = MinMaxBounds.Floats.ANY;
        this.level = MinMaxBounds.Ints.ANY;
        this.rotX = WrappedMinMaxBounds.ANY;
        this.rotY = WrappedMinMaxBounds.ANY;
        this.predicate = (Predicate<Entity>)(apx -> true);
        this.order = EntitySelectorParser.ORDER_ARBITRARY;
        this.suggestions = EntitySelectorParser.SUGGEST_NOTHING;
        this.reader = stringReader;
        this.allowSelectors = boolean2;
    }
    
    public EntitySelector getSelector() {
        AABB dcf2;
        if (this.deltaX != null || this.deltaY != null || this.deltaZ != null) {
            dcf2 = this.createAabb((this.deltaX == null) ? 0.0 : ((double)this.deltaX), (this.deltaY == null) ? 0.0 : ((double)this.deltaY), (this.deltaZ == null) ? 0.0 : ((double)this.deltaZ));
        }
        else if (this.distance.getMax() != null) {
            final float float3 = this.distance.getMax();
            dcf2 = new AABB(-float3, -float3, -float3, float3 + 1.0f, float3 + 1.0f, float3 + 1.0f);
        }
        else {
            dcf2 = null;
        }
        Function<Vec3, Vec3> function3;
        if (this.x == null && this.y == null && this.z == null) {
            function3 = (Function<Vec3, Vec3>)(dck -> dck);
        }
        else {
            function3 = (Function<Vec3, Vec3>)(dck -> new Vec3((this.x == null) ? dck.x : this.x, (this.y == null) ? dck.y : this.y, (this.z == null) ? dck.z : this.z));
        }
        return new EntitySelector(this.maxResults, this.includesEntities, this.worldLimited, this.predicate, this.distance, function3, dcf2, this.order, this.currentEntity, this.playerName, this.entityUUID, this.type, this.usesSelectors);
    }
    
    private AABB createAabb(final double double1, final double double2, final double double3) {
        final boolean boolean8 = double1 < 0.0;
        final boolean boolean9 = double2 < 0.0;
        final boolean boolean10 = double3 < 0.0;
        final double double4 = boolean8 ? double1 : 0.0;
        final double double5 = boolean9 ? double2 : 0.0;
        final double double6 = boolean10 ? double3 : 0.0;
        final double double7 = (boolean8 ? 0.0 : double1) + 1.0;
        final double double8 = (boolean9 ? 0.0 : double2) + 1.0;
        final double double9 = (boolean10 ? 0.0 : double3) + 1.0;
        return new AABB(double4, double5, double6, double7, double8, double9);
    }
    
    private void finalizePredicates() {
        if (this.rotX != WrappedMinMaxBounds.ANY) {
            this.predicate = (Predicate<Entity>)this.predicate.and((Predicate)this.createRotationPredicate(this.rotX, (ToDoubleFunction<Entity>)(apx -> apx.xRot)));
        }
        if (this.rotY != WrappedMinMaxBounds.ANY) {
            this.predicate = (Predicate<Entity>)this.predicate.and((Predicate)this.createRotationPredicate(this.rotY, (ToDoubleFunction<Entity>)(apx -> apx.yRot)));
        }
        if (!this.level.isAny()) {
            this.predicate = (Predicate<Entity>)this.predicate.and(apx -> apx instanceof ServerPlayer && this.level.matches(((ServerPlayer)apx).experienceLevel));
        }
    }
    
    private Predicate<Entity> createRotationPredicate(final WrappedMinMaxBounds cu, final ToDoubleFunction<Entity> toDoubleFunction) {
        final double double4 = Mth.wrapDegrees((cu.getMin() == null) ? 0.0f : ((float)cu.getMin()));
        final double double5 = Mth.wrapDegrees((cu.getMax() == null) ? 359.0f : ((float)cu.getMax()));
        return (Predicate<Entity>)(apx -> {
            final double double4 = Mth.wrapDegrees(toDoubleFunction.applyAsDouble(apx));
            if (double4 > double5) {
                return double4 >= double4 || double4 <= double5;
            }
            return double4 >= double4 && double4 <= double5;
        });
    }
    
    protected void parseSelector() throws CommandSyntaxException {
        this.usesSelectors = true;
        this.suggestions = (BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>>)this::suggestSelector;
        if (!this.reader.canRead()) {
            throw EntitySelectorParser.ERROR_MISSING_SELECTOR_TYPE.createWithContext((ImmutableStringReader)this.reader);
        }
        final int integer2 = this.reader.getCursor();
        final char character3 = this.reader.read();
        if (character3 == 'p') {
            this.maxResults = 1;
            this.includesEntities = false;
            this.order = EntitySelectorParser.ORDER_NEAREST;
            this.limitToType(EntityType.PLAYER);
        }
        else if (character3 == 'a') {
            this.maxResults = Integer.MAX_VALUE;
            this.includesEntities = false;
            this.order = EntitySelectorParser.ORDER_ARBITRARY;
            this.limitToType(EntityType.PLAYER);
        }
        else if (character3 == 'r') {
            this.maxResults = 1;
            this.includesEntities = false;
            this.order = EntitySelectorParser.ORDER_RANDOM;
            this.limitToType(EntityType.PLAYER);
        }
        else if (character3 == 's') {
            this.maxResults = 1;
            this.includesEntities = true;
            this.currentEntity = true;
        }
        else {
            if (character3 != 'e') {
                this.reader.setCursor(integer2);
                throw EntitySelectorParser.ERROR_UNKNOWN_SELECTOR_TYPE.createWithContext((ImmutableStringReader)this.reader, ('@' + String.valueOf(character3)));
            }
            this.maxResults = Integer.MAX_VALUE;
            this.includesEntities = true;
            this.order = EntitySelectorParser.ORDER_ARBITRARY;
            this.predicate = (Predicate<Entity>)Entity::isAlive;
        }
        this.suggestions = (BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>>)this::suggestOpenOptions;
        if (this.reader.canRead() && this.reader.peek() == '[') {
            this.reader.skip();
            this.suggestions = (BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>>)this::suggestOptionsKeyOrClose;
            this.parseOptions();
        }
    }
    
    protected void parseNameOrUUID() throws CommandSyntaxException {
        if (this.reader.canRead()) {
            this.suggestions = (BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>>)this::suggestName;
        }
        final int integer2 = this.reader.getCursor();
        final String string3 = this.reader.readString();
        try {
            this.entityUUID = UUID.fromString(string3);
            this.includesEntities = true;
        }
        catch (IllegalArgumentException illegalArgumentException4) {
            if (string3.isEmpty() || string3.length() > 16) {
                this.reader.setCursor(integer2);
                throw EntitySelectorParser.ERROR_INVALID_NAME_OR_UUID.createWithContext((ImmutableStringReader)this.reader);
            }
            this.includesEntities = false;
            this.playerName = string3;
        }
        this.maxResults = 1;
    }
    
    protected void parseOptions() throws CommandSyntaxException {
        this.suggestions = (BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>>)this::suggestOptionsKey;
        this.reader.skipWhitespace();
        while (this.reader.canRead() && this.reader.peek() != ']') {
            this.reader.skipWhitespace();
            final int integer2 = this.reader.getCursor();
            final String string3 = this.reader.readString();
            final EntitySelectorOptions.Modifier a4 = EntitySelectorOptions.get(this, string3, integer2);
            this.reader.skipWhitespace();
            if (!this.reader.canRead() || this.reader.peek() != '=') {
                this.reader.setCursor(integer2);
                throw EntitySelectorParser.ERROR_EXPECTED_OPTION_VALUE.createWithContext((ImmutableStringReader)this.reader, string3);
            }
            this.reader.skip();
            this.reader.skipWhitespace();
            this.suggestions = EntitySelectorParser.SUGGEST_NOTHING;
            a4.handle(this);
            this.reader.skipWhitespace();
            this.suggestions = (BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>>)this::suggestOptionsNextOrClose;
            if (!this.reader.canRead()) {
                continue;
            }
            if (this.reader.peek() == ',') {
                this.reader.skip();
                this.suggestions = (BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>>)this::suggestOptionsKey;
            }
            else {
                if (this.reader.peek() == ']') {
                    break;
                }
                throw EntitySelectorParser.ERROR_EXPECTED_END_OF_OPTIONS.createWithContext((ImmutableStringReader)this.reader);
            }
        }
        if (this.reader.canRead()) {
            this.reader.skip();
            this.suggestions = EntitySelectorParser.SUGGEST_NOTHING;
            return;
        }
        throw EntitySelectorParser.ERROR_EXPECTED_END_OF_OPTIONS.createWithContext((ImmutableStringReader)this.reader);
    }
    
    public boolean shouldInvertValue() {
        this.reader.skipWhitespace();
        if (this.reader.canRead() && this.reader.peek() == '!') {
            this.reader.skip();
            this.reader.skipWhitespace();
            return true;
        }
        return false;
    }
    
    public boolean isTag() {
        this.reader.skipWhitespace();
        if (this.reader.canRead() && this.reader.peek() == '#') {
            this.reader.skip();
            this.reader.skipWhitespace();
            return true;
        }
        return false;
    }
    
    public StringReader getReader() {
        return this.reader;
    }
    
    public void addPredicate(final Predicate<Entity> predicate) {
        this.predicate = (Predicate<Entity>)this.predicate.and((Predicate)predicate);
    }
    
    public void setWorldLimited() {
        this.worldLimited = true;
    }
    
    public MinMaxBounds.Floats getDistance() {
        return this.distance;
    }
    
    public void setDistance(final MinMaxBounds.Floats c) {
        this.distance = c;
    }
    
    public MinMaxBounds.Ints getLevel() {
        return this.level;
    }
    
    public void setLevel(final MinMaxBounds.Ints d) {
        this.level = d;
    }
    
    public WrappedMinMaxBounds getRotX() {
        return this.rotX;
    }
    
    public void setRotX(final WrappedMinMaxBounds cu) {
        this.rotX = cu;
    }
    
    public WrappedMinMaxBounds getRotY() {
        return this.rotY;
    }
    
    public void setRotY(final WrappedMinMaxBounds cu) {
        this.rotY = cu;
    }
    
    @Nullable
    public Double getX() {
        return this.x;
    }
    
    @Nullable
    public Double getY() {
        return this.y;
    }
    
    @Nullable
    public Double getZ() {
        return this.z;
    }
    
    public void setX(final double double1) {
        this.x = double1;
    }
    
    public void setY(final double double1) {
        this.y = double1;
    }
    
    public void setZ(final double double1) {
        this.z = double1;
    }
    
    public void setDeltaX(final double double1) {
        this.deltaX = double1;
    }
    
    public void setDeltaY(final double double1) {
        this.deltaY = double1;
    }
    
    public void setDeltaZ(final double double1) {
        this.deltaZ = double1;
    }
    
    @Nullable
    public Double getDeltaX() {
        return this.deltaX;
    }
    
    @Nullable
    public Double getDeltaY() {
        return this.deltaY;
    }
    
    @Nullable
    public Double getDeltaZ() {
        return this.deltaZ;
    }
    
    public void setMaxResults(final int integer) {
        this.maxResults = integer;
    }
    
    public void setIncludesEntities(final boolean boolean1) {
        this.includesEntities = boolean1;
    }
    
    public void setOrder(final BiConsumer<Vec3, List<? extends Entity>> biConsumer) {
        this.order = biConsumer;
    }
    
    public EntitySelector parse() throws CommandSyntaxException {
        this.startPosition = this.reader.getCursor();
        this.suggestions = (BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>>)this::suggestNameOrSelector;
        if (this.reader.canRead() && this.reader.peek() == '@') {
            if (!this.allowSelectors) {
                throw EntitySelectorParser.ERROR_SELECTORS_NOT_ALLOWED.createWithContext((ImmutableStringReader)this.reader);
            }
            this.reader.skip();
            this.parseSelector();
        }
        else {
            this.parseNameOrUUID();
        }
        this.finalizePredicates();
        return this.getSelector();
    }
    
    private static void fillSelectorSuggestions(final SuggestionsBuilder suggestionsBuilder) {
        suggestionsBuilder.suggest("@p", (Message)new TranslatableComponent("argument.entity.selector.nearestPlayer"));
        suggestionsBuilder.suggest("@a", (Message)new TranslatableComponent("argument.entity.selector.allPlayers"));
        suggestionsBuilder.suggest("@r", (Message)new TranslatableComponent("argument.entity.selector.randomPlayer"));
        suggestionsBuilder.suggest("@s", (Message)new TranslatableComponent("argument.entity.selector.self"));
        suggestionsBuilder.suggest("@e", (Message)new TranslatableComponent("argument.entity.selector.allEntities"));
    }
    
    private CompletableFuture<Suggestions> suggestNameOrSelector(final SuggestionsBuilder suggestionsBuilder, final Consumer<SuggestionsBuilder> consumer) {
        consumer.accept(suggestionsBuilder);
        if (this.allowSelectors) {
            fillSelectorSuggestions(suggestionsBuilder);
        }
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    private CompletableFuture<Suggestions> suggestName(final SuggestionsBuilder suggestionsBuilder, final Consumer<SuggestionsBuilder> consumer) {
        final SuggestionsBuilder suggestionsBuilder2 = suggestionsBuilder.createOffset(this.startPosition);
        consumer.accept(suggestionsBuilder2);
        return (CompletableFuture<Suggestions>)suggestionsBuilder.add(suggestionsBuilder2).buildFuture();
    }
    
    private CompletableFuture<Suggestions> suggestSelector(final SuggestionsBuilder suggestionsBuilder, final Consumer<SuggestionsBuilder> consumer) {
        final SuggestionsBuilder suggestionsBuilder2 = suggestionsBuilder.createOffset(suggestionsBuilder.getStart() - 1);
        fillSelectorSuggestions(suggestionsBuilder2);
        suggestionsBuilder.add(suggestionsBuilder2);
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    private CompletableFuture<Suggestions> suggestOpenOptions(final SuggestionsBuilder suggestionsBuilder, final Consumer<SuggestionsBuilder> consumer) {
        suggestionsBuilder.suggest(String.valueOf('['));
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    private CompletableFuture<Suggestions> suggestOptionsKeyOrClose(final SuggestionsBuilder suggestionsBuilder, final Consumer<SuggestionsBuilder> consumer) {
        suggestionsBuilder.suggest(String.valueOf(']'));
        EntitySelectorOptions.suggestNames(this, suggestionsBuilder);
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    private CompletableFuture<Suggestions> suggestOptionsKey(final SuggestionsBuilder suggestionsBuilder, final Consumer<SuggestionsBuilder> consumer) {
        EntitySelectorOptions.suggestNames(this, suggestionsBuilder);
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    private CompletableFuture<Suggestions> suggestOptionsNextOrClose(final SuggestionsBuilder suggestionsBuilder, final Consumer<SuggestionsBuilder> consumer) {
        suggestionsBuilder.suggest(String.valueOf(','));
        suggestionsBuilder.suggest(String.valueOf(']'));
        return (CompletableFuture<Suggestions>)suggestionsBuilder.buildFuture();
    }
    
    public boolean isCurrentEntity() {
        return this.currentEntity;
    }
    
    public void setSuggestions(final BiFunction<SuggestionsBuilder, Consumer<SuggestionsBuilder>, CompletableFuture<Suggestions>> biFunction) {
        this.suggestions = biFunction;
    }
    
    public CompletableFuture<Suggestions> fillSuggestions(final SuggestionsBuilder suggestionsBuilder, final Consumer<SuggestionsBuilder> consumer) {
        return (CompletableFuture<Suggestions>)this.suggestions.apply(suggestionsBuilder.createOffset(this.reader.getCursor()), consumer);
    }
    
    public boolean hasNameEquals() {
        return this.hasNameEquals;
    }
    
    public void setHasNameEquals(final boolean boolean1) {
        this.hasNameEquals = boolean1;
    }
    
    public boolean hasNameNotEquals() {
        return this.hasNameNotEquals;
    }
    
    public void setHasNameNotEquals(final boolean boolean1) {
        this.hasNameNotEquals = boolean1;
    }
    
    public boolean isLimited() {
        return this.isLimited;
    }
    
    public void setLimited(final boolean boolean1) {
        this.isLimited = boolean1;
    }
    
    public boolean isSorted() {
        return this.isSorted;
    }
    
    public void setSorted(final boolean boolean1) {
        this.isSorted = boolean1;
    }
    
    public boolean hasGamemodeEquals() {
        return this.hasGamemodeEquals;
    }
    
    public void setHasGamemodeEquals(final boolean boolean1) {
        this.hasGamemodeEquals = boolean1;
    }
    
    public boolean hasGamemodeNotEquals() {
        return this.hasGamemodeNotEquals;
    }
    
    public void setHasGamemodeNotEquals(final boolean boolean1) {
        this.hasGamemodeNotEquals = boolean1;
    }
    
    public boolean hasTeamEquals() {
        return this.hasTeamEquals;
    }
    
    public void setHasTeamEquals(final boolean boolean1) {
        this.hasTeamEquals = boolean1;
    }
    
    public void setHasTeamNotEquals(final boolean boolean1) {
        this.hasTeamNotEquals = boolean1;
    }
    
    public void limitToType(final EntityType<?> aqb) {
        this.type = aqb;
    }
    
    public void setTypeLimitedInversely() {
        this.typeInverse = true;
    }
    
    public boolean isTypeLimited() {
        return this.type != null;
    }
    
    public boolean isTypeLimitedInversely() {
        return this.typeInverse;
    }
    
    public boolean hasScores() {
        return this.hasScores;
    }
    
    public void setHasScores(final boolean boolean1) {
        this.hasScores = boolean1;
    }
    
    public boolean hasAdvancements() {
        return this.hasAdvancements;
    }
    
    public void setHasAdvancements(final boolean boolean1) {
        this.hasAdvancements = boolean1;
    }
    
    static {
        ERROR_INVALID_NAME_OR_UUID = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.invalid"));
        ERROR_UNKNOWN_SELECTOR_TYPE = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.entity.selector.unknown", new Object[] { object }));
        ERROR_SELECTORS_NOT_ALLOWED = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.selector.not_allowed"));
        ERROR_MISSING_SELECTOR_TYPE = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.selector.missing"));
        ERROR_EXPECTED_END_OF_OPTIONS = new SimpleCommandExceptionType((Message)new TranslatableComponent("argument.entity.options.unterminated"));
        ERROR_EXPECTED_OPTION_VALUE = new DynamicCommandExceptionType(object -> new TranslatableComponent("argument.entity.options.valueless", new Object[] { object }));
        ORDER_ARBITRARY = ((dck, list) -> {});
        ORDER_NEAREST = ((dck, list) -> list.sort((apx2, apx3) -> Doubles.compare(apx2.distanceToSqr(dck), apx3.distanceToSqr(dck))));
        ORDER_FURTHEST = ((dck, list) -> list.sort((apx2, apx3) -> Doubles.compare(apx3.distanceToSqr(dck), apx2.distanceToSqr(dck))));
        ORDER_RANDOM = ((dck, list) -> Collections.shuffle(list));
        SUGGEST_NOTHING = ((suggestionsBuilder, consumer) -> suggestionsBuilder.buildFuture());
    }
}
