package net.minecraft.client.renderer.debug;

import javax.annotation.Nullable;
import net.minecraft.world.level.pathfinder.Path;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import java.util.Objects;
import net.minecraft.util.Mth;
import net.minecraft.client.Camera;
import net.minecraft.core.Position;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.network.protocol.game.DebugEntityNameGenerator;
import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import net.minecraft.core.Vec3i;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import com.google.common.collect.Maps;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class BeeDebugRenderer implements DebugRenderer.SimpleDebugRenderer {
    private final Minecraft minecraft;
    private final Map<BlockPos, HiveInfo> hives;
    private final Map<UUID, BeeInfo> beeInfosPerEntity;
    private UUID lastLookedAtUuid;
    
    public BeeDebugRenderer(final Minecraft djw) {
        this.hives = (Map<BlockPos, HiveInfo>)Maps.newHashMap();
        this.beeInfosPerEntity = (Map<UUID, BeeInfo>)Maps.newHashMap();
        this.minecraft = djw;
    }
    
    public void clear() {
        this.hives.clear();
        this.beeInfosPerEntity.clear();
        this.lastLookedAtUuid = null;
    }
    
    public void addOrUpdateHiveInfo(final HiveInfo b) {
        this.hives.put(b.pos, b);
    }
    
    public void addOrUpdateBeeInfo(final BeeInfo a) {
        this.beeInfosPerEntity.put(a.uuid, a);
    }
    
    public void render(final PoseStack dfj, final MultiBufferSource dzy, final double double3, final double double4, final double double5) {
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();
        this.clearRemovedHives();
        this.clearRemovedBees();
        this.doRender();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.popMatrix();
        if (!this.minecraft.player.isSpectator()) {
            this.updateLastLookedAtUuid();
        }
    }
    
    private void clearRemovedBees() {
        this.beeInfosPerEntity.entrySet().removeIf(entry -> this.minecraft.level.getEntity(((BeeInfo)entry.getValue()).id) == null);
    }
    
    private void clearRemovedHives() {
        final long long2 = this.minecraft.level.getGameTime() - 20L;
        this.hives.entrySet().removeIf(entry -> ((HiveInfo)entry.getValue()).lastSeen < long2);
    }
    
    private void doRender() {
        final BlockPos fx2 = this.getCamera().getBlockPosition();
        this.beeInfosPerEntity.values().forEach(a -> {
            if (this.isPlayerCloseEnoughToMob(a)) {
                this.renderBeeInfo(a);
            }
        });
        this.renderFlowerInfos();
        for (final BlockPos fx3 : this.hives.keySet()) {
            if (fx2.closerThan(fx3, 30.0)) {
                highlightHive(fx3);
            }
        }
        final Map<BlockPos, Set<UUID>> map3 = this.createHiveBlacklistMap();
        this.hives.values().forEach(b -> {
            if (fx2.closerThan(b.pos, 30.0)) {
                final Set<UUID> set5 = (Set<UUID>)map3.get(b.pos);
                this.renderHiveInfo(b, (Collection<UUID>)((set5 == null) ? Sets.newHashSet() : set5));
            }
        });
        this.getGhostHives().forEach((fx2, list) -> {
            if (fx2.closerThan(fx2, 30.0)) {
                this.renderGhostHive(fx2, (List<String>)list);
            }
        });
    }
    
    private Map<BlockPos, Set<UUID>> createHiveBlacklistMap() {
        final Map<BlockPos, Set<UUID>> map2 = (Map<BlockPos, Set<UUID>>)Maps.newHashMap();
        this.beeInfosPerEntity.values().forEach(a -> a.blacklistedHives.forEach(fx -> ((Set)map2.computeIfAbsent(fx, fx -> Sets.newHashSet())).add(a.getUuid())));
        return map2;
    }
    
    private void renderFlowerInfos() {
        final Map<BlockPos, Set<UUID>> map2 = (Map<BlockPos, Set<UUID>>)Maps.newHashMap();
        this.beeInfosPerEntity.values().stream().filter(BeeInfo::hasFlower).forEach(a -> ((Set)map2.computeIfAbsent(a.flowerPos, fx -> Sets.newHashSet())).add(a.getUuid()));
        map2.entrySet().forEach(entry -> {
            final BlockPos fx2 = (BlockPos)entry.getKey();
            final Set<UUID> set3 = (Set<UUID>)entry.getValue();
            final Set<String> set4 = (Set<String>)set3.stream().map(DebugEntityNameGenerator::getEntityName).collect(Collectors.toSet());
            int integer5 = 1;
            renderTextOverPos(set4.toString(), fx2, integer5++, -256);
            renderTextOverPos("Flower", fx2, integer5++, -1);
            final float float6 = 0.05f;
            renderTransparentFilledBox(fx2, 0.05f, 0.8f, 0.8f, 0.0f, 0.3f);
        });
    }
    
    private static String getBeeUuidsAsString(final Collection<UUID> collection) {
        if (collection.isEmpty()) {
            return "-";
        }
        if (collection.size() > 3) {
            return new StringBuilder().append("").append(collection.size()).append(" bees").toString();
        }
        return ((Set)collection.stream().map(DebugEntityNameGenerator::getEntityName).collect(Collectors.toSet())).toString();
    }
    
    private static void highlightHive(final BlockPos fx) {
        final float float2 = 0.05f;
        renderTransparentFilledBox(fx, 0.05f, 0.2f, 0.2f, 1.0f, 0.3f);
    }
    
    private void renderGhostHive(final BlockPos fx, final List<String> list) {
        final float float4 = 0.05f;
        renderTransparentFilledBox(fx, 0.05f, 0.2f, 0.2f, 1.0f, 0.3f);
        renderTextOverPos(new StringBuilder().append("").append(list).toString(), fx, 0, -256);
        renderTextOverPos("Ghost Hive", fx, 1, -65536);
    }
    
    private static void renderTransparentFilledBox(final BlockPos fx, final float float2, final float float3, final float float4, final float float5, final float float6) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        DebugRenderer.renderFilledBox(fx, float2, float3, float4, float5, float6);
    }
    
    private void renderHiveInfo(final HiveInfo b, final Collection<UUID> collection) {
        int integer4 = 0;
        if (!collection.isEmpty()) {
            renderTextOverHive("Blacklisted by " + getBeeUuidsAsString(collection), b, integer4++, -65536);
        }
        renderTextOverHive("Out: " + getBeeUuidsAsString(this.getHiveMembers(b.pos)), b, integer4++, -3355444);
        if (b.occupantCount == 0) {
            renderTextOverHive("In: -", b, integer4++, -256);
        }
        else if (b.occupantCount == 1) {
            renderTextOverHive("In: 1 bee", b, integer4++, -256);
        }
        else {
            renderTextOverHive(new StringBuilder().append("In: ").append(b.occupantCount).append(" bees").toString(), b, integer4++, -256);
        }
        renderTextOverHive(new StringBuilder().append("Honey: ").append(b.honeyLevel).toString(), b, integer4++, -23296);
        renderTextOverHive(b.hiveType + (b.sedated ? " (sedated)" : ""), b, integer4++, -1);
    }
    
    private void renderPath(final BeeInfo a) {
        if (a.path != null) {
            PathfindingRenderer.renderPath(a.path, 0.5f, false, false, this.getCamera().getPosition().x(), this.getCamera().getPosition().y(), this.getCamera().getPosition().z());
        }
    }
    
    private void renderBeeInfo(final BeeInfo a) {
        final boolean boolean3 = this.isBeeSelected(a);
        int integer4 = 0;
        renderTextOverMob(a.pos, integer4++, a.toString(), -1, 0.03f);
        if (a.hivePos == null) {
            renderTextOverMob(a.pos, integer4++, "No hive", -98404, 0.02f);
        }
        else {
            renderTextOverMob(a.pos, integer4++, "Hive: " + this.getPosDescription(a, a.hivePos), -256, 0.02f);
        }
        if (a.flowerPos == null) {
            renderTextOverMob(a.pos, integer4++, "No flower", -98404, 0.02f);
        }
        else {
            renderTextOverMob(a.pos, integer4++, "Flower: " + this.getPosDescription(a, a.flowerPos), -256, 0.02f);
        }
        for (final String string6 : a.goals) {
            renderTextOverMob(a.pos, integer4++, string6, -16711936, 0.02f);
        }
        if (boolean3) {
            this.renderPath(a);
        }
        if (a.travelTicks > 0) {
            final int integer5 = (a.travelTicks < 600) ? -3355444 : -23296;
            renderTextOverMob(a.pos, integer4++, new StringBuilder().append("Travelling: ").append(a.travelTicks).append(" ticks").toString(), integer5, 0.02f);
        }
    }
    
    private static void renderTextOverHive(final String string, final HiveInfo b, final int integer3, final int integer4) {
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
    
    private Camera getCamera() {
        return this.minecraft.gameRenderer.getMainCamera();
    }
    
    private String getPosDescription(final BeeInfo a, final BlockPos fx) {
        final float float4 = Mth.sqrt(fx.distSqr(a.pos.x(), a.pos.y(), a.pos.z(), true));
        final double double5 = Math.round(float4 * 10.0f) / 10.0;
        return fx.toShortString() + " (dist " + double5 + ")";
    }
    
    private boolean isBeeSelected(final BeeInfo a) {
        return Objects.equals(this.lastLookedAtUuid, a.uuid);
    }
    
    private boolean isPlayerCloseEnoughToMob(final BeeInfo a) {
        final Player bft3 = this.minecraft.player;
        final BlockPos fx4 = new BlockPos(bft3.getX(), a.pos.y(), bft3.getZ());
        final BlockPos fx5 = new BlockPos(a.pos);
        return fx4.closerThan(fx5, 30.0);
    }
    
    private Collection<UUID> getHiveMembers(final BlockPos fx) {
        return (Collection<UUID>)this.beeInfosPerEntity.values().stream().filter(a -> a.hasHive(fx)).map(BeeInfo::getUuid).collect(Collectors.toSet());
    }
    
    private Map<BlockPos, List<String>> getGhostHives() {
        final Map<BlockPos, List<String>> map2 = (Map<BlockPos, List<String>>)Maps.newHashMap();
        for (final BeeInfo a4 : this.beeInfosPerEntity.values()) {
            if (a4.hivePos != null && !this.hives.containsKey(a4.hivePos)) {
                ((List)map2.computeIfAbsent(a4.hivePos, fx -> Lists.newArrayList())).add(a4.getName());
            }
        }
        return map2;
    }
    
    private void updateLastLookedAtUuid() {
        DebugRenderer.getTargetedEntity(this.minecraft.getCameraEntity(), 8).ifPresent(apx -> this.lastLookedAtUuid = apx.getUUID());
    }
    
    public static class HiveInfo {
        public final BlockPos pos;
        public final String hiveType;
        public final int occupantCount;
        public final int honeyLevel;
        public final boolean sedated;
        public final long lastSeen;
        
        public HiveInfo(final BlockPos fx, final String string, final int integer3, final int integer4, final boolean boolean5, final long long6) {
            this.pos = fx;
            this.hiveType = string;
            this.occupantCount = integer3;
            this.honeyLevel = integer4;
            this.sedated = boolean5;
            this.lastSeen = long6;
        }
    }
    
    public static class BeeInfo {
        public final UUID uuid;
        public final int id;
        public final Position pos;
        @Nullable
        public final Path path;
        @Nullable
        public final BlockPos hivePos;
        @Nullable
        public final BlockPos flowerPos;
        public final int travelTicks;
        public final List<String> goals;
        public final Set<BlockPos> blacklistedHives;
        
        public BeeInfo(final UUID uUID, final int integer2, final Position gk, final Path cxa, final BlockPos fx5, final BlockPos fx6, final int integer7) {
            this.goals = (List<String>)Lists.newArrayList();
            this.blacklistedHives = (Set<BlockPos>)Sets.newHashSet();
            this.uuid = uUID;
            this.id = integer2;
            this.pos = gk;
            this.path = cxa;
            this.hivePos = fx5;
            this.flowerPos = fx6;
            this.travelTicks = integer7;
        }
        
        public boolean hasHive(final BlockPos fx) {
            return this.hivePos != null && this.hivePos.equals(fx);
        }
        
        public UUID getUuid() {
            return this.uuid;
        }
        
        public String getName() {
            return DebugEntityNameGenerator.getEntityName(this.uuid);
        }
        
        public String toString() {
            return this.getName();
        }
        
        public boolean hasFlower() {
            return this.flowerPos != null;
        }
    }
}
