package net.minecraft.client.renderer.debug;

import com.google.common.collect.Sets;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.entity.Entity;
import com.google.common.collect.Iterables;
import java.util.Collection;
import net.minecraft.world.entity.player.Player;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
import net.minecraft.core.Position;
import com.google.common.collect.Lists;
import java.util.Set;
import java.util.List;
import java.util.Iterator;
import net.minecraft.core.Vec3i;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import java.util.Map;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class BrainDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
    private static final Logger LOGGER;
    private final Minecraft minecraft;
    private final Map<BlockPos, PoiInfo> pois;
    private final Map<UUID, BrainDump> brainDumpsPerEntity;
    @Nullable
    private UUID lastLookedAtUuid;
    
    public BrainDebugRenderer(final Minecraft djw) {
        this.pois = (Map<BlockPos, PoiInfo>)Maps.newHashMap();
        this.brainDumpsPerEntity = (Map<UUID, BrainDump>)Maps.newHashMap();
        this.minecraft = djw;
    }
    
    public void clear() {
        this.pois.clear();
        this.brainDumpsPerEntity.clear();
        this.lastLookedAtUuid = null;
    }
    
    public void addPoi(final PoiInfo b) {
        this.pois.put(b.pos, b);
    }
    
    public void removePoi(final BlockPos fx) {
        this.pois.remove(fx);
    }
    
    public void setFreeTicketCount(final BlockPos fx, final int integer) {
        final PoiInfo b4 = (PoiInfo)this.pois.get(fx);
        if (b4 == null) {
            BrainDebugRenderer.LOGGER.warn(new StringBuilder().append("Strange, setFreeTicketCount was called for an unknown POI: ").append(fx).toString());
            return;
        }
        b4.freeTicketCount = integer;
    }
    
    public void addOrUpdateBrainDump(final BrainDump a) {
        this.brainDumpsPerEntity.put(a.uuid, a);
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        this.clearRemovedEntities();
        this.doRender(double3, double4, double5);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
        if (!this.minecraft.player.isSpectator()) {
            this.updateLastLookedAtUuid();
        }
    }
    
    private void clearRemovedEntities() {
        this.brainDumpsPerEntity.entrySet().removeIf(entry -> {
            final Entity apx3 = this.minecraft.level.getEntity(((BrainDump)entry.getValue()).id);
            return apx3 == null || apx3.removed;
        });
    }
    
    private void doRender(final double double1, final double double2, final double double3) {
        final BlockPos fx8 = new BlockPos(double1, double2, double3);
        this.brainDumpsPerEntity.values().forEach(a -> {
            if (this.isPlayerCloseEnoughToMob(a)) {
                this.renderBrainInfo(a, double1, double2, double3);
            }
        });
        for (final BlockPos fx9 : this.pois.keySet()) {
            if (fx8.closerThan(fx9, 30.0)) {
                highlightPoi(fx9);
            }
        }
        this.pois.values().forEach(b -> {
            if (fx8.closerThan(b.pos, 30.0)) {
                this.renderPoiInfo(b);
            }
        });
        this.getGhostPois().forEach((fx2, list) -> {
            if (fx8.closerThan(fx2, 30.0)) {
                this.renderGhostPoi(fx2, (List<String>)list);
            }
        });
    }
    
    private static void highlightPoi(final BlockPos fx) {
        final float float2 = 0.05f;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        DebugRenderer.renderFilledBox(fx, 0.05f, 0.2f, 0.2f, 1.0f, 0.3f);
    }
    
    private void renderGhostPoi(final BlockPos fx, final List<String> list) {
        final float float4 = 0.05f;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        DebugRenderer.renderFilledBox(fx, 0.05f, 0.2f, 0.2f, 1.0f, 0.3f);
        renderTextOverPos(new StringBuilder().append("").append(list).toString(), fx, 0, -256);
        renderTextOverPos("Ghost POI", fx, 1, -65536);
    }
    
    private void renderPoiInfo(final PoiInfo b) {
        int integer3 = 0;
        final Set<String> set4 = this.getTicketHolderNames(b);
        if (set4.size() < 4) {
            renderTextOverPoi(new StringBuilder().append("Owners: ").append(set4).toString(), b, integer3, -256);
        }
        else {
            renderTextOverPoi(new StringBuilder().append("").append(set4.size()).append(" ticket holders").toString(), b, integer3, -256);
        }
        ++integer3;
        final Set<String> set5 = this.getPotentialTicketHolderNames(b);
        if (set5.size() < 4) {
            renderTextOverPoi(new StringBuilder().append("Candidates: ").append(set5).toString(), b, integer3, -23296);
        }
        else {
            renderTextOverPoi(new StringBuilder().append("").append(set5.size()).append(" potential owners").toString(), b, integer3, -23296);
        }
        ++integer3;
        renderTextOverPoi(new StringBuilder().append("Free tickets: ").append(b.freeTicketCount).toString(), b, integer3, -256);
        ++integer3;
        renderTextOverPoi(b.type, b, integer3, -1);
    }
    
    private void renderPath(final BrainDump a, final double double2, final double double3, final double double4) {
        if (a.path != null) {
            PathfindingRenderer.renderPath(a.path, 0.5f, false, false, double2, double3, double4);
        }
    }
    
    private void renderBrainInfo(final BrainDump a, final double double2, final double double3, final double double4) {
        final boolean boolean9 = this.isMobSelected(a);
        int integer10 = 0;
        renderTextOverMob(a.pos, integer10, a.name, -1, 0.03f);
        ++integer10;
        if (boolean9) {
            renderTextOverMob(a.pos, integer10, a.profession + " " + a.xp + " xp", -1, 0.02f);
            ++integer10;
        }
        if (boolean9) {
            final int integer11 = (a.health < a.maxHealth) ? -23296 : -1;
            renderTextOverMob(a.pos, integer10, "health: " + String.format("%.1f", new Object[] { a.health }) + " / " + String.format("%.1f", new Object[] { a.maxHealth }), integer11, 0.02f);
            ++integer10;
        }
        if (boolean9 && !a.inventory.equals("")) {
            renderTextOverMob(a.pos, integer10, a.inventory, -98404, 0.02f);
            ++integer10;
        }
        if (boolean9) {
            for (final String string12 : a.behaviors) {
                renderTextOverMob(a.pos, integer10, string12, -16711681, 0.02f);
                ++integer10;
            }
        }
        if (boolean9) {
            for (final String string12 : a.activities) {
                renderTextOverMob(a.pos, integer10, string12, -16711936, 0.02f);
                ++integer10;
            }
        }
        if (a.wantsGolem) {
            renderTextOverMob(a.pos, integer10, "Wants Golem", -23296, 0.02f);
            ++integer10;
        }
        if (boolean9) {
            for (final String string12 : a.gossips) {
                if (string12.startsWith(a.name)) {
                    renderTextOverMob(a.pos, integer10, string12, -1, 0.02f);
                }
                else {
                    renderTextOverMob(a.pos, integer10, string12, -23296, 0.02f);
                }
                ++integer10;
            }
        }
        if (boolean9) {
            for (final String string12 : Lists.reverse((List)a.memories)) {
                renderTextOverMob(a.pos, integer10, string12, -3355444, 0.02f);
                ++integer10;
            }
        }
        if (boolean9) {
            this.renderPath(a, double2, double3, double4);
        }
    }
    
    private static void renderTextOverPoi(final String string, final PoiInfo b, final int integer3, final int integer4) {
        final BlockPos fx5 = b.pos;
        renderTextOverPos(string, fx5, integer3, integer4);
    }
    
    private static void renderTextOverPos(final String string, final BlockPos fx, final int integer3, final int integer4) {
        final double double5 = 1.3;
        final double double6 = 0.2;
        final double double7 = fx.getX() + 0.5;
        final double double8 = fx.getY() + 1.3 + integer3 * 0.2;
        final double double9 = fx.getZ() + 0.5;
        DebugRenderer.renderFloatingText(string, double7, double8, double9, integer4, 0.02f, true, 0.0f, true);
    }
    
    private static void renderTextOverMob(final Position gk, final int integer2, final String string, final int integer4, final float float5) {
        final double double6 = 2.4;
        final double double7 = 0.25;
        final BlockPos fx10 = new BlockPos(gk);
        final double double8 = fx10.getX() + 0.5;
        final double double9 = gk.y() + 2.4 + integer2 * 0.25;
        final double double10 = fx10.getZ() + 0.5;
        final float float6 = 0.5f;
        DebugRenderer.renderFloatingText(string, double8, double9, double10, integer4, float5, false, 0.5f, true);
    }
    
    private Set<String> getTicketHolderNames(final PoiInfo b) {
        return (Set<String>)this.getTicketHolders(b.pos).stream().map(DebugEntityNameGenerator::getEntityName).collect(Collectors.toSet());
    }
    
    private Set<String> getPotentialTicketHolderNames(final PoiInfo b) {
        return (Set<String>)this.getPotentialTicketHolders(b.pos).stream().map(DebugEntityNameGenerator::getEntityName).collect(Collectors.toSet());
    }
    
    private boolean isMobSelected(final BrainDump a) {
        return Objects.equals(this.lastLookedAtUuid, a.uuid);
    }
    
    private boolean isPlayerCloseEnoughToMob(final BrainDump a) {
        final Player bft3 = this.minecraft.player;
        final BlockPos fx4 = new BlockPos(bft3.getX(), a.pos.y(), bft3.getZ());
        final BlockPos fx5 = new BlockPos(a.pos);
        return fx4.closerThan(fx5, 30.0);
    }
    
    private Collection<UUID> getTicketHolders(final BlockPos fx) {
        return (Collection<UUID>)this.brainDumpsPerEntity.values().stream().filter(a -> a.hasPoi(fx)).map(BrainDump::getUuid).collect(Collectors.toSet());
    }
    
    private Collection<UUID> getPotentialTicketHolders(final BlockPos fx) {
        return (Collection<UUID>)this.brainDumpsPerEntity.values().stream().filter(a -> a.hasPotentialPoi(fx)).map(BrainDump::getUuid).collect(Collectors.toSet());
    }
    
    private Map<BlockPos, List<String>> getGhostPois() {
        final Map<BlockPos, List<String>> map2 = (Map<BlockPos, List<String>>)Maps.newHashMap();
        for (final BrainDump a4 : this.brainDumpsPerEntity.values()) {
            for (final BlockPos fx6 : Iterables.concat((Iterable)a4.pois, (Iterable)a4.potentialPois)) {
                if (!this.pois.containsKey(fx6)) {
                    ((List)map2.computeIfAbsent(fx6, fx -> Lists.newArrayList())).add(a4.name);
                }
            }
        }
        return map2;
    }
    
    private void updateLastLookedAtUuid() {
        DebugRenderer.getTargetedEntity(this.minecraft.getCameraEntity(), 8).ifPresent(apx -> this.lastLookedAtUuid = apx.getUUID());
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class PoiInfo {
        public final BlockPos pos;
        public String type;
        public int freeTicketCount;
        
        public PoiInfo(final BlockPos fx, final String string, final int integer) {
            this.pos = fx;
            this.type = string;
            this.freeTicketCount = integer;
        }
    }
    
    public static class BrainDump {
        public final UUID uuid;
        public final int id;
        public final String name;
        public final String profession;
        public final int xp;
        public final float health;
        public final float maxHealth;
        public final Position pos;
        public final String inventory;
        public final Path path;
        public final boolean wantsGolem;
        public final List<String> activities;
        public final List<String> behaviors;
        public final List<String> memories;
        public final List<String> gossips;
        public final Set<BlockPos> pois;
        public final Set<BlockPos> potentialPois;
        
        public BrainDump(final UUID uUID, final int integer2, final String string3, final String string4, final int integer5, final float float6, final float float7, final Position gk, final String string9, @Nullable final Path cxa, final boolean boolean11) {
            this.activities = (List<String>)Lists.newArrayList();
            this.behaviors = (List<String>)Lists.newArrayList();
            this.memories = (List<String>)Lists.newArrayList();
            this.gossips = (List<String>)Lists.newArrayList();
            this.pois = (Set<BlockPos>)Sets.newHashSet();
            this.potentialPois = (Set<BlockPos>)Sets.newHashSet();
            this.uuid = uUID;
            this.id = integer2;
            this.name = string3;
            this.profession = string4;
            this.xp = integer5;
            this.health = float6;
            this.maxHealth = float7;
            this.pos = gk;
            this.inventory = string9;
            this.path = cxa;
            this.wantsGolem = boolean11;
        }
        
        private boolean hasPoi(final BlockPos fx) {
            return this.pois.stream().anyMatch(fx::equals);
        }
        
        private boolean hasPotentialPoi(final BlockPos fx) {
            return this.potentialPois.contains(fx);
        }
        
        public UUID getUuid() {
            return this.uuid;
        }
    }
}
