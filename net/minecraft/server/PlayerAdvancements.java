package net.minecraft.server;

import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.protocol.game.ClientboundSelectAdvancementsTabPacket;
import net.minecraft.network.protocol.Packet;
import java.util.Collection;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.Criterion;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.GameRules;
import java.io.Writer;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import com.google.common.base.Charsets;
import java.io.FileOutputStream;
import java.util.stream.Stream;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.Comparator;
import com.google.gson.JsonParseException;
import com.google.gson.JsonElement;
import net.minecraft.SharedConstants;
import net.minecraft.util.datafix.DataFixTypes;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Dynamic;
import com.google.gson.internal.Streams;
import com.mojang.serialization.JsonOps;
import java.io.Reader;
import com.google.gson.stream.JsonReader;
import java.io.StringReader;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.Iterator;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import java.util.Set;
import net.minecraft.advancements.Advancement;
import java.io.File;
import net.minecraft.server.players.PlayerList;
import com.mojang.datafixers.DataFixer;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class PlayerAdvancements {
    private static final Logger LOGGER;
    private static final Gson GSON;
    private static final TypeToken<Map<ResourceLocation, AdvancementProgress>> TYPE_TOKEN;
    private final DataFixer dataFixer;
    private final PlayerList playerList;
    private final File file;
    private final Map<Advancement, AdvancementProgress> advancements;
    private final Set<Advancement> visible;
    private final Set<Advancement> visibilityChanged;
    private final Set<Advancement> progressChanged;
    private ServerPlayer player;
    @Nullable
    private Advancement lastSelectedTab;
    private boolean isFirstPacket;
    
    public PlayerAdvancements(final DataFixer dataFixer, final PlayerList acs, final ServerAdvancementManager vv, final File file, final ServerPlayer aah) {
        this.advancements = (Map<Advancement, AdvancementProgress>)Maps.newLinkedHashMap();
        this.visible = (Set<Advancement>)Sets.newLinkedHashSet();
        this.visibilityChanged = (Set<Advancement>)Sets.newLinkedHashSet();
        this.progressChanged = (Set<Advancement>)Sets.newLinkedHashSet();
        this.isFirstPacket = true;
        this.dataFixer = dataFixer;
        this.playerList = acs;
        this.file = file;
        this.player = aah;
        this.load(vv);
    }
    
    public void setPlayer(final ServerPlayer aah) {
        this.player = aah;
    }
    
    public void stopListening() {
        for (final CriterionTrigger<?> af3 : CriteriaTriggers.all()) {
            af3.removePlayerListeners(this);
        }
    }
    
    public void reload(final ServerAdvancementManager vv) {
        this.stopListening();
        this.advancements.clear();
        this.visible.clear();
        this.visibilityChanged.clear();
        this.progressChanged.clear();
        this.isFirstPacket = true;
        this.lastSelectedTab = null;
        this.load(vv);
    }
    
    private void registerListeners(final ServerAdvancementManager vv) {
        for (final Advancement y4 : vv.getAllAdvancements()) {
            this.registerListeners(y4);
        }
    }
    
    private void ensureAllVisible() {
        final List<Advancement> list2 = (List<Advancement>)Lists.newArrayList();
        for (final Map.Entry<Advancement, AdvancementProgress> entry4 : this.advancements.entrySet()) {
            if (((AdvancementProgress)entry4.getValue()).isDone()) {
                list2.add(entry4.getKey());
                this.progressChanged.add(entry4.getKey());
            }
        }
        for (final Advancement y4 : list2) {
            this.ensureVisibility(y4);
        }
    }
    
    private void checkForAutomaticTriggers(final ServerAdvancementManager vv) {
        for (final Advancement y4 : vv.getAllAdvancements()) {
            if (y4.getCriteria().isEmpty()) {
                this.award(y4, "");
                y4.getRewards().grant(this.player);
            }
        }
    }
    
    private void load(final ServerAdvancementManager vv) {
        if (this.file.isFile()) {
            try (final JsonReader jsonReader3 = new JsonReader((Reader)new StringReader(Files.toString(this.file, StandardCharsets.UTF_8)))) {
                jsonReader3.setLenient(false);
                Dynamic<JsonElement> dynamic5 = (Dynamic<JsonElement>)new Dynamic((DynamicOps)JsonOps.INSTANCE, Streams.parse(jsonReader3));
                if (!dynamic5.get("DataVersion").asNumber().result().isPresent()) {
                    dynamic5 = (Dynamic<JsonElement>)dynamic5.set("DataVersion", dynamic5.createInt(1343));
                }
                dynamic5 = (Dynamic<JsonElement>)this.dataFixer.update(DataFixTypes.ADVANCEMENTS.getType(), (Dynamic)dynamic5, dynamic5.get("DataVersion").asInt(0), SharedConstants.getCurrentVersion().getWorldVersion());
                dynamic5 = (Dynamic<JsonElement>)dynamic5.remove("DataVersion");
                final Map<ResourceLocation, AdvancementProgress> map6 = (Map<ResourceLocation, AdvancementProgress>)PlayerAdvancements.GSON.getAdapter((TypeToken)PlayerAdvancements.TYPE_TOKEN).fromJsonTree((JsonElement)dynamic5.getValue());
                if (map6 == null) {
                    throw new JsonParseException("Found null for advancements");
                }
                final Stream<Map.Entry<ResourceLocation, AdvancementProgress>> stream7 = (Stream<Map.Entry<ResourceLocation, AdvancementProgress>>)map6.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue));
                for (final Map.Entry<ResourceLocation, AdvancementProgress> entry9 : (List)stream7.collect(Collectors.toList())) {
                    final Advancement y10 = vv.getAdvancement((ResourceLocation)entry9.getKey());
                    if (y10 == null) {
                        PlayerAdvancements.LOGGER.warn("Ignored advancement '{}' in progress file {} - it doesn't exist anymore?", entry9.getKey(), this.file);
                    }
                    else {
                        this.startProgress(y10, (AdvancementProgress)entry9.getValue());
                    }
                }
            }
            catch (JsonParseException jsonParseException3) {
                PlayerAdvancements.LOGGER.error("Couldn't parse player advancements in {}", this.file, jsonParseException3);
            }
            catch (IOException iOException3) {
                PlayerAdvancements.LOGGER.error("Couldn't access player advancements in {}", this.file, iOException3);
            }
        }
        this.checkForAutomaticTriggers(vv);
        this.ensureAllVisible();
        this.registerListeners(vv);
    }
    
    public void save() {
        final Map<ResourceLocation, AdvancementProgress> map2 = (Map<ResourceLocation, AdvancementProgress>)Maps.newHashMap();
        for (final Map.Entry<Advancement, AdvancementProgress> entry4 : this.advancements.entrySet()) {
            final AdvancementProgress aa5 = (AdvancementProgress)entry4.getValue();
            if (aa5.hasProgress()) {
                map2.put(((Advancement)entry4.getKey()).getId(), aa5);
            }
        }
        if (this.file.getParentFile() != null) {
            this.file.getParentFile().mkdirs();
        }
        final JsonElement jsonElement3 = PlayerAdvancements.GSON.toJsonTree(map2);
        jsonElement3.getAsJsonObject().addProperty("DataVersion", (Number)SharedConstants.getCurrentVersion().getWorldVersion());
        try (final OutputStream outputStream4 = (OutputStream)new FileOutputStream(this.file);
             final Writer writer6 = (Writer)new OutputStreamWriter(outputStream4, Charsets.UTF_8.newEncoder())) {
            PlayerAdvancements.GSON.toJson(jsonElement3, (Appendable)writer6);
        }
        catch (IOException iOException4) {
            PlayerAdvancements.LOGGER.error("Couldn't save player advancements to {}", this.file, iOException4);
        }
    }
    
    public boolean award(final Advancement y, final String string) {
        boolean boolean4 = false;
        final AdvancementProgress aa5 = this.getOrStartProgress(y);
        final boolean boolean5 = aa5.isDone();
        if (aa5.grantProgress(string)) {
            this.unregisterListeners(y);
            this.progressChanged.add(y);
            boolean4 = true;
            if (!boolean5 && aa5.isDone()) {
                y.getRewards().grant(this.player);
                if (y.getDisplay() != null && y.getDisplay().shouldAnnounceChat() && this.player.level.getGameRules().getBoolean(GameRules.RULE_ANNOUNCE_ADVANCEMENTS)) {
                    this.playerList.broadcastMessage(new TranslatableComponent("chat.type.advancement." + y.getDisplay().getFrame().getName(), new Object[] { this.player.getDisplayName(), y.getChatComponent() }), ChatType.SYSTEM, Util.NIL_UUID);
                }
            }
        }
        if (aa5.isDone()) {
            this.ensureVisibility(y);
        }
        return boolean4;
    }
    
    public boolean revoke(final Advancement y, final String string) {
        boolean boolean4 = false;
        final AdvancementProgress aa5 = this.getOrStartProgress(y);
        if (aa5.revokeProgress(string)) {
            this.registerListeners(y);
            this.progressChanged.add(y);
            boolean4 = true;
        }
        if (!aa5.hasProgress()) {
            this.ensureVisibility(y);
        }
        return boolean4;
    }
    
    private void registerListeners(final Advancement y) {
        final AdvancementProgress aa3 = this.getOrStartProgress(y);
        if (aa3.isDone()) {
            return;
        }
        for (final Map.Entry<String, Criterion> entry5 : y.getCriteria().entrySet()) {
            final CriterionProgress ae6 = aa3.getCriterion((String)entry5.getKey());
            if (ae6 != null) {
                if (ae6.isDone()) {
                    continue;
                }
                final CriterionTriggerInstance ag7 = ((Criterion)entry5.getValue()).getTrigger();
                if (ag7 == null) {
                    continue;
                }
                final CriterionTrigger<CriterionTriggerInstance> af8 = CriteriaTriggers.<CriterionTriggerInstance>getCriterion(ag7.getCriterion());
                if (af8 == null) {
                    continue;
                }
                af8.addPlayerListener(this, new CriterionTrigger.Listener<CriterionTriggerInstance>(ag7, y, (String)entry5.getKey()));
            }
        }
    }
    
    private void unregisterListeners(final Advancement y) {
        final AdvancementProgress aa3 = this.getOrStartProgress(y);
        for (final Map.Entry<String, Criterion> entry5 : y.getCriteria().entrySet()) {
            final CriterionProgress ae6 = aa3.getCriterion((String)entry5.getKey());
            if (ae6 != null) {
                if (!ae6.isDone() && !aa3.isDone()) {
                    continue;
                }
                final CriterionTriggerInstance ag7 = ((Criterion)entry5.getValue()).getTrigger();
                if (ag7 == null) {
                    continue;
                }
                final CriterionTrigger<CriterionTriggerInstance> af8 = CriteriaTriggers.<CriterionTriggerInstance>getCriterion(ag7.getCriterion());
                if (af8 == null) {
                    continue;
                }
                af8.removePlayerListener(this, new CriterionTrigger.Listener<CriterionTriggerInstance>(ag7, y, (String)entry5.getKey()));
            }
        }
    }
    
    public void flushDirty(final ServerPlayer aah) {
        if (this.isFirstPacket || !this.visibilityChanged.isEmpty() || !this.progressChanged.isEmpty()) {
            final Map<ResourceLocation, AdvancementProgress> map3 = (Map<ResourceLocation, AdvancementProgress>)Maps.newHashMap();
            final Set<Advancement> set4 = (Set<Advancement>)Sets.newLinkedHashSet();
            final Set<ResourceLocation> set5 = (Set<ResourceLocation>)Sets.newLinkedHashSet();
            for (final Advancement y7 : this.progressChanged) {
                if (this.visible.contains(y7)) {
                    map3.put(y7.getId(), this.advancements.get(y7));
                }
            }
            for (final Advancement y7 : this.visibilityChanged) {
                if (this.visible.contains(y7)) {
                    set4.add(y7);
                }
                else {
                    set5.add(y7.getId());
                }
            }
            if (this.isFirstPacket || !map3.isEmpty() || !set4.isEmpty() || !set5.isEmpty()) {
                aah.connection.send(new ClientboundUpdateAdvancementsPacket(this.isFirstPacket, (Collection<Advancement>)set4, set5, map3));
                this.visibilityChanged.clear();
                this.progressChanged.clear();
            }
        }
        this.isFirstPacket = false;
    }
    
    public void setSelectedTab(@Nullable final Advancement y) {
        final Advancement y2 = this.lastSelectedTab;
        if (y != null && y.getParent() == null && y.getDisplay() != null) {
            this.lastSelectedTab = y;
        }
        else {
            this.lastSelectedTab = null;
        }
        if (y2 != this.lastSelectedTab) {
            this.player.connection.send(new ClientboundSelectAdvancementsTabPacket((this.lastSelectedTab == null) ? null : this.lastSelectedTab.getId()));
        }
    }
    
    public AdvancementProgress getOrStartProgress(final Advancement y) {
        AdvancementProgress aa3 = (AdvancementProgress)this.advancements.get(y);
        if (aa3 == null) {
            aa3 = new AdvancementProgress();
            this.startProgress(y, aa3);
        }
        return aa3;
    }
    
    private void startProgress(final Advancement y, final AdvancementProgress aa) {
        aa.update(y.getCriteria(), y.getRequirements());
        this.advancements.put(y, aa);
    }
    
    private void ensureVisibility(final Advancement y) {
        final boolean boolean3 = this.shouldBeVisible(y);
        final boolean boolean4 = this.visible.contains(y);
        if (boolean3 && !boolean4) {
            this.visible.add(y);
            this.visibilityChanged.add(y);
            if (this.advancements.containsKey(y)) {
                this.progressChanged.add(y);
            }
        }
        else if (!boolean3 && boolean4) {
            this.visible.remove(y);
            this.visibilityChanged.add(y);
        }
        if (boolean3 != boolean4 && y.getParent() != null) {
            this.ensureVisibility(y.getParent());
        }
        for (final Advancement y2 : y.getChildren()) {
            this.ensureVisibility(y2);
        }
    }
    
    private boolean shouldBeVisible(Advancement y) {
        for (int integer3 = 0; y != null && integer3 <= 2; y = y.getParent(), ++integer3) {
            if (integer3 == 0 && this.hasCompletedChildrenOrSelf(y)) {
                return true;
            }
            if (y.getDisplay() == null) {
                return false;
            }
            final AdvancementProgress aa4 = this.getOrStartProgress(y);
            if (aa4.isDone()) {
                return true;
            }
            if (y.getDisplay().isHidden()) {
                return false;
            }
        }
        return false;
    }
    
    private boolean hasCompletedChildrenOrSelf(final Advancement y) {
        final AdvancementProgress aa3 = this.getOrStartProgress(y);
        if (aa3.isDone()) {
            return true;
        }
        for (final Advancement y2 : y.getChildren()) {
            if (this.hasCompletedChildrenOrSelf(y2)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = new GsonBuilder().registerTypeAdapter((Type)AdvancementProgress.class, new AdvancementProgress.Serializer()).registerTypeAdapter((Type)ResourceLocation.class, new ResourceLocation.Serializer()).setPrettyPrinting().create();
        TYPE_TOKEN = new TypeToken<Map<ResourceLocation, AdvancementProgress>>() {};
    }
}
