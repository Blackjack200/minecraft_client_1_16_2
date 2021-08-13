package net.minecraft.network;

import com.google.common.collect.Maps;
import com.google.common.collect.Iterables;
import org.apache.logging.log4j.LogManager;
import com.google.common.collect.Lists;
import java.util.function.Consumer;
import net.minecraft.Util;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.function.Supplier;
import java.util.List;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import javax.annotation.Nullable;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.Packet;
import java.util.Map;

public enum ConnectionProtocol {
    public static final ConnectionProtocol HANDSHAKING;
    public static final ConnectionProtocol PLAY;
    public static final ConnectionProtocol STATUS;
    public static final ConnectionProtocol LOGIN;
    private static final ConnectionProtocol[] LOOKUP;
    private static final Map<Class<? extends Packet<?>>, ConnectionProtocol> PROTOCOL_BY_PACKET;
    private final int id;
    private final Map<PacketFlow, ? extends PacketSet<?>> flows;
    
    private static ProtocolBuilder protocol() {
        return new ProtocolBuilder();
    }
    
    private ConnectionProtocol(final int integer3, final ProtocolBuilder b) {
        this.id = integer3;
        this.flows = b.flows;
    }
    
    @Nullable
    public Integer getPacketId(final PacketFlow ok, final Packet<?> oj) {
        return ((PacketSet)this.flows.get(ok)).getId(oj.getClass());
    }
    
    @Nullable
    public Packet<?> createPacket(final PacketFlow ok, final int integer) {
        return ((PacketSet)this.flows.get(ok)).createPacket(integer);
    }
    
    public int getId() {
        return this.id;
    }
    
    @Nullable
    public static ConnectionProtocol getById(final int integer) {
        if (integer < -1 || integer > 2) {
            return null;
        }
        return ConnectionProtocol.LOOKUP[integer + 1];
    }
    
    public static ConnectionProtocol getProtocolForPacket(final Packet<?> oj) {
        return (ConnectionProtocol)ConnectionProtocol.PROTOCOL_BY_PACKET.get(oj.getClass());
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: ldc             "HANDSHAKING"
        //     6: iconst_0       
        //     7: iconst_m1      
        //     8: invokestatic    net/minecraft/network/ConnectionProtocol.protocol:()Lnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;
        //    11: getstatic       net/minecraft/network/protocol/PacketFlow.SERVERBOUND:Lnet/minecraft/network/protocol/PacketFlow;
        //    14: new             Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //    17: dup            
        //    18: aconst_null    
        //    19: invokespecial   net/minecraft/network/ConnectionProtocol$PacketSet.<init>:(Lnet/minecraft/network/ConnectionProtocol$1;)V
        //    22: ldc             Lnet/minecraft/network/protocol/handshake/ClientIntentionPacket;.class
        //    24: invokedynamic   BootstrapMethod #1, get:()Ljava/util/function/Supplier;
        //    29: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //    32: invokevirtual   net/minecraft/network/ConnectionProtocol$ProtocolBuilder.addFlow:(Lnet/minecraft/network/protocol/PacketFlow;Lnet/minecraft/network/ConnectionProtocol$PacketSet;)Lnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;
        //    35: invokespecial   net/minecraft/network/ConnectionProtocol.<init>:(Ljava/lang/String;IILnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;)V
        //    38: putstatic       net/minecraft/network/ConnectionProtocol.HANDSHAKING:Lnet/minecraft/network/ConnectionProtocol;
        //    41: new             Lnet/minecraft/network/ConnectionProtocol;
        //    44: dup            
        //    45: ldc             "PLAY"
        //    47: iconst_1       
        //    48: iconst_0       
        //    49: invokestatic    net/minecraft/network/ConnectionProtocol.protocol:()Lnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;
        //    52: getstatic       net/minecraft/network/protocol/PacketFlow.CLIENTBOUND:Lnet/minecraft/network/protocol/PacketFlow;
        //    55: new             Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //    58: dup            
        //    59: aconst_null    
        //    60: invokespecial   net/minecraft/network/ConnectionProtocol$PacketSet.<init>:(Lnet/minecraft/network/ConnectionProtocol$1;)V
        //    63: ldc             Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;.class
        //    65: invokedynamic   BootstrapMethod #2, get:()Ljava/util/function/Supplier;
        //    70: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //    73: ldc             Lnet/minecraft/network/protocol/game/ClientboundAddExperienceOrbPacket;.class
        //    75: invokedynamic   BootstrapMethod #3, get:()Ljava/util/function/Supplier;
        //    80: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //    83: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundAddMobPacket;.class
        //    86: invokedynamic   BootstrapMethod #4, get:()Ljava/util/function/Supplier;
        //    91: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //    94: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundAddPaintingPacket;.class
        //    97: invokedynamic   BootstrapMethod #5, get:()Ljava/util/function/Supplier;
        //   102: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   105: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundAddPlayerPacket;.class
        //   108: invokedynamic   BootstrapMethod #6, get:()Ljava/util/function/Supplier;
        //   113: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   116: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundAnimatePacket;.class
        //   119: invokedynamic   BootstrapMethod #7, get:()Ljava/util/function/Supplier;
        //   124: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   127: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundAwardStatsPacket;.class
        //   130: invokedynamic   BootstrapMethod #8, get:()Ljava/util/function/Supplier;
        //   135: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   138: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundBlockBreakAckPacket;.class
        //   141: invokedynamic   BootstrapMethod #9, get:()Ljava/util/function/Supplier;
        //   146: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   149: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundBlockDestructionPacket;.class
        //   152: invokedynamic   BootstrapMethod #10, get:()Ljava/util/function/Supplier;
        //   157: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   160: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundBlockEntityDataPacket;.class
        //   163: invokedynamic   BootstrapMethod #11, get:()Ljava/util/function/Supplier;
        //   168: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   171: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundBlockEventPacket;.class
        //   174: invokedynamic   BootstrapMethod #12, get:()Ljava/util/function/Supplier;
        //   179: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   182: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundBlockUpdatePacket;.class
        //   185: invokedynamic   BootstrapMethod #13, get:()Ljava/util/function/Supplier;
        //   190: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   193: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundBossEventPacket;.class
        //   196: invokedynamic   BootstrapMethod #14, get:()Ljava/util/function/Supplier;
        //   201: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   204: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundChangeDifficultyPacket;.class
        //   207: invokedynamic   BootstrapMethod #15, get:()Ljava/util/function/Supplier;
        //   212: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   215: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundChatPacket;.class
        //   218: invokedynamic   BootstrapMethod #16, get:()Ljava/util/function/Supplier;
        //   223: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   226: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundCommandSuggestionsPacket;.class
        //   229: invokedynamic   BootstrapMethod #17, get:()Ljava/util/function/Supplier;
        //   234: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   237: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundCommandsPacket;.class
        //   240: invokedynamic   BootstrapMethod #18, get:()Ljava/util/function/Supplier;
        //   245: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   248: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundContainerAckPacket;.class
        //   251: invokedynamic   BootstrapMethod #19, get:()Ljava/util/function/Supplier;
        //   256: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   259: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundContainerClosePacket;.class
        //   262: invokedynamic   BootstrapMethod #20, get:()Ljava/util/function/Supplier;
        //   267: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   270: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundContainerSetContentPacket;.class
        //   273: invokedynamic   BootstrapMethod #21, get:()Ljava/util/function/Supplier;
        //   278: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   281: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundContainerSetDataPacket;.class
        //   284: invokedynamic   BootstrapMethod #22, get:()Ljava/util/function/Supplier;
        //   289: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   292: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundContainerSetSlotPacket;.class
        //   295: invokedynamic   BootstrapMethod #23, get:()Ljava/util/function/Supplier;
        //   300: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   303: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundCooldownPacket;.class
        //   306: invokedynamic   BootstrapMethod #24, get:()Ljava/util/function/Supplier;
        //   311: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   314: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundCustomPayloadPacket;.class
        //   317: invokedynamic   BootstrapMethod #25, get:()Ljava/util/function/Supplier;
        //   322: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   325: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundCustomSoundPacket;.class
        //   328: invokedynamic   BootstrapMethod #26, get:()Ljava/util/function/Supplier;
        //   333: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   336: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundDisconnectPacket;.class
        //   339: invokedynamic   BootstrapMethod #27, get:()Ljava/util/function/Supplier;
        //   344: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   347: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundEntityEventPacket;.class
        //   350: invokedynamic   BootstrapMethod #28, get:()Ljava/util/function/Supplier;
        //   355: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   358: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundExplodePacket;.class
        //   361: invokedynamic   BootstrapMethod #29, get:()Ljava/util/function/Supplier;
        //   366: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   369: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;.class
        //   372: invokedynamic   BootstrapMethod #30, get:()Ljava/util/function/Supplier;
        //   377: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   380: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundGameEventPacket;.class
        //   383: invokedynamic   BootstrapMethod #31, get:()Ljava/util/function/Supplier;
        //   388: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   391: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundHorseScreenOpenPacket;.class
        //   394: invokedynamic   BootstrapMethod #32, get:()Ljava/util/function/Supplier;
        //   399: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   402: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundKeepAlivePacket;.class
        //   405: invokedynamic   BootstrapMethod #33, get:()Ljava/util/function/Supplier;
        //   410: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   413: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundLevelChunkPacket;.class
        //   416: invokedynamic   BootstrapMethod #34, get:()Ljava/util/function/Supplier;
        //   421: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   424: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundLevelEventPacket;.class
        //   427: invokedynamic   BootstrapMethod #35, get:()Ljava/util/function/Supplier;
        //   432: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   435: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundLevelParticlesPacket;.class
        //   438: invokedynamic   BootstrapMethod #36, get:()Ljava/util/function/Supplier;
        //   443: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   446: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundLightUpdatePacket;.class
        //   449: invokedynamic   BootstrapMethod #37, get:()Ljava/util/function/Supplier;
        //   454: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   457: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;.class
        //   460: invokedynamic   BootstrapMethod #38, get:()Ljava/util/function/Supplier;
        //   465: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   468: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundMapItemDataPacket;.class
        //   471: invokedynamic   BootstrapMethod #39, get:()Ljava/util/function/Supplier;
        //   476: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   479: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundMerchantOffersPacket;.class
        //   482: invokedynamic   BootstrapMethod #40, get:()Ljava/util/function/Supplier;
        //   487: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   490: ldc             Lnet/minecraft/network/protocol/game/ClientboundMoveEntityPacket$Pos;.class
        //   492: invokedynamic   BootstrapMethod #41, get:()Ljava/util/function/Supplier;
        //   497: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   500: ldc             Lnet/minecraft/network/protocol/game/ClientboundMoveEntityPacket$PosRot;.class
        //   502: invokedynamic   BootstrapMethod #42, get:()Ljava/util/function/Supplier;
        //   507: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   510: ldc             Lnet/minecraft/network/protocol/game/ClientboundMoveEntityPacket$Rot;.class
        //   512: invokedynamic   BootstrapMethod #43, get:()Ljava/util/function/Supplier;
        //   517: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   520: ldc             Lnet/minecraft/network/protocol/game/ClientboundMoveEntityPacket;.class
        //   522: invokedynamic   BootstrapMethod #44, get:()Ljava/util/function/Supplier;
        //   527: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   530: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundMoveVehiclePacket;.class
        //   533: invokedynamic   BootstrapMethod #45, get:()Ljava/util/function/Supplier;
        //   538: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   541: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundOpenBookPacket;.class
        //   544: invokedynamic   BootstrapMethod #46, get:()Ljava/util/function/Supplier;
        //   549: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   552: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundOpenScreenPacket;.class
        //   555: invokedynamic   BootstrapMethod #47, get:()Ljava/util/function/Supplier;
        //   560: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   563: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundOpenSignEditorPacket;.class
        //   566: invokedynamic   BootstrapMethod #48, get:()Ljava/util/function/Supplier;
        //   571: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   574: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundPlaceGhostRecipePacket;.class
        //   577: invokedynamic   BootstrapMethod #49, get:()Ljava/util/function/Supplier;
        //   582: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   585: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundPlayerAbilitiesPacket;.class
        //   588: invokedynamic   BootstrapMethod #50, get:()Ljava/util/function/Supplier;
        //   593: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   596: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundPlayerCombatPacket;.class
        //   599: invokedynamic   BootstrapMethod #51, get:()Ljava/util/function/Supplier;
        //   604: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   607: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundPlayerInfoPacket;.class
        //   610: invokedynamic   BootstrapMethod #52, get:()Ljava/util/function/Supplier;
        //   615: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   618: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundPlayerLookAtPacket;.class
        //   621: invokedynamic   BootstrapMethod #53, get:()Ljava/util/function/Supplier;
        //   626: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   629: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundPlayerPositionPacket;.class
        //   632: invokedynamic   BootstrapMethod #54, get:()Ljava/util/function/Supplier;
        //   637: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   640: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundRecipePacket;.class
        //   643: invokedynamic   BootstrapMethod #55, get:()Ljava/util/function/Supplier;
        //   648: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   651: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundRemoveEntitiesPacket;.class
        //   654: invokedynamic   BootstrapMethod #56, get:()Ljava/util/function/Supplier;
        //   659: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   662: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundRemoveMobEffectPacket;.class
        //   665: invokedynamic   BootstrapMethod #57, get:()Ljava/util/function/Supplier;
        //   670: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   673: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundResourcePackPacket;.class
        //   676: invokedynamic   BootstrapMethod #58, get:()Ljava/util/function/Supplier;
        //   681: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   684: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundRespawnPacket;.class
        //   687: invokedynamic   BootstrapMethod #59, get:()Ljava/util/function/Supplier;
        //   692: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   695: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundRotateHeadPacket;.class
        //   698: invokedynamic   BootstrapMethod #60, get:()Ljava/util/function/Supplier;
        //   703: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   706: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSectionBlocksUpdatePacket;.class
        //   709: invokedynamic   BootstrapMethod #61, get:()Ljava/util/function/Supplier;
        //   714: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   717: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSelectAdvancementsTabPacket;.class
        //   720: invokedynamic   BootstrapMethod #62, get:()Ljava/util/function/Supplier;
        //   725: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   728: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetBorderPacket;.class
        //   731: invokedynamic   BootstrapMethod #63, get:()Ljava/util/function/Supplier;
        //   736: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   739: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetCameraPacket;.class
        //   742: invokedynamic   BootstrapMethod #64, get:()Ljava/util/function/Supplier;
        //   747: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   750: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetCarriedItemPacket;.class
        //   753: invokedynamic   BootstrapMethod #65, get:()Ljava/util/function/Supplier;
        //   758: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   761: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetChunkCacheCenterPacket;.class
        //   764: invokedynamic   BootstrapMethod #66, get:()Ljava/util/function/Supplier;
        //   769: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   772: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetChunkCacheRadiusPacket;.class
        //   775: invokedynamic   BootstrapMethod #67, get:()Ljava/util/function/Supplier;
        //   780: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   783: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetDefaultSpawnPositionPacket;.class
        //   786: invokedynamic   BootstrapMethod #68, get:()Ljava/util/function/Supplier;
        //   791: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   794: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetDisplayObjectivePacket;.class
        //   797: invokedynamic   BootstrapMethod #69, get:()Ljava/util/function/Supplier;
        //   802: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   805: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetEntityDataPacket;.class
        //   808: invokedynamic   BootstrapMethod #70, get:()Ljava/util/function/Supplier;
        //   813: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   816: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetEntityLinkPacket;.class
        //   819: invokedynamic   BootstrapMethod #71, get:()Ljava/util/function/Supplier;
        //   824: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   827: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetEntityMotionPacket;.class
        //   830: invokedynamic   BootstrapMethod #72, get:()Ljava/util/function/Supplier;
        //   835: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   838: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetEquipmentPacket;.class
        //   841: invokedynamic   BootstrapMethod #73, get:()Ljava/util/function/Supplier;
        //   846: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   849: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetExperiencePacket;.class
        //   852: invokedynamic   BootstrapMethod #74, get:()Ljava/util/function/Supplier;
        //   857: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   860: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetHealthPacket;.class
        //   863: invokedynamic   BootstrapMethod #75, get:()Ljava/util/function/Supplier;
        //   868: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   871: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetObjectivePacket;.class
        //   874: invokedynamic   BootstrapMethod #76, get:()Ljava/util/function/Supplier;
        //   879: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   882: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetPassengersPacket;.class
        //   885: invokedynamic   BootstrapMethod #77, get:()Ljava/util/function/Supplier;
        //   890: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   893: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetPlayerTeamPacket;.class
        //   896: invokedynamic   BootstrapMethod #78, get:()Ljava/util/function/Supplier;
        //   901: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   904: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetScorePacket;.class
        //   907: invokedynamic   BootstrapMethod #79, get:()Ljava/util/function/Supplier;
        //   912: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   915: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;.class
        //   918: invokedynamic   BootstrapMethod #80, get:()Ljava/util/function/Supplier;
        //   923: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   926: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSetTitlesPacket;.class
        //   929: invokedynamic   BootstrapMethod #81, get:()Ljava/util/function/Supplier;
        //   934: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   937: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSoundEntityPacket;.class
        //   940: invokedynamic   BootstrapMethod #82, get:()Ljava/util/function/Supplier;
        //   945: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   948: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundSoundPacket;.class
        //   951: invokedynamic   BootstrapMethod #83, get:()Ljava/util/function/Supplier;
        //   956: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   959: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundStopSoundPacket;.class
        //   962: invokedynamic   BootstrapMethod #84, get:()Ljava/util/function/Supplier;
        //   967: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   970: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundTabListPacket;.class
        //   973: invokedynamic   BootstrapMethod #85, get:()Ljava/util/function/Supplier;
        //   978: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   981: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundTagQueryPacket;.class
        //   984: invokedynamic   BootstrapMethod #86, get:()Ljava/util/function/Supplier;
        //   989: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //   992: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundTakeItemEntityPacket;.class
        //   995: invokedynamic   BootstrapMethod #87, get:()Ljava/util/function/Supplier;
        //  1000: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1003: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundTeleportEntityPacket;.class
        //  1006: invokedynamic   BootstrapMethod #88, get:()Ljava/util/function/Supplier;
        //  1011: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1014: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundUpdateAdvancementsPacket;.class
        //  1017: invokedynamic   BootstrapMethod #89, get:()Ljava/util/function/Supplier;
        //  1022: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1025: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundUpdateAttributesPacket;.class
        //  1028: invokedynamic   BootstrapMethod #90, get:()Ljava/util/function/Supplier;
        //  1033: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1036: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundUpdateMobEffectPacket;.class
        //  1039: invokedynamic   BootstrapMethod #91, get:()Ljava/util/function/Supplier;
        //  1044: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1047: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundUpdateRecipesPacket;.class
        //  1050: invokedynamic   BootstrapMethod #92, get:()Ljava/util/function/Supplier;
        //  1055: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1058: ldc_w           Lnet/minecraft/network/protocol/game/ClientboundUpdateTagsPacket;.class
        //  1061: invokedynamic   BootstrapMethod #93, get:()Ljava/util/function/Supplier;
        //  1066: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1069: invokevirtual   net/minecraft/network/ConnectionProtocol$ProtocolBuilder.addFlow:(Lnet/minecraft/network/protocol/PacketFlow;Lnet/minecraft/network/ConnectionProtocol$PacketSet;)Lnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;
        //  1072: getstatic       net/minecraft/network/protocol/PacketFlow.SERVERBOUND:Lnet/minecraft/network/protocol/PacketFlow;
        //  1075: new             Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1078: dup            
        //  1079: aconst_null    
        //  1080: invokespecial   net/minecraft/network/ConnectionProtocol$PacketSet.<init>:(Lnet/minecraft/network/ConnectionProtocol$1;)V
        //  1083: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundAcceptTeleportationPacket;.class
        //  1086: invokedynamic   BootstrapMethod #94, get:()Ljava/util/function/Supplier;
        //  1091: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1094: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundBlockEntityTagQuery;.class
        //  1097: invokedynamic   BootstrapMethod #95, get:()Ljava/util/function/Supplier;
        //  1102: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1105: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundChangeDifficultyPacket;.class
        //  1108: invokedynamic   BootstrapMethod #96, get:()Ljava/util/function/Supplier;
        //  1113: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1116: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundChatPacket;.class
        //  1119: invokedynamic   BootstrapMethod #97, get:()Ljava/util/function/Supplier;
        //  1124: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1127: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundClientCommandPacket;.class
        //  1130: invokedynamic   BootstrapMethod #98, get:()Ljava/util/function/Supplier;
        //  1135: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1138: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundClientInformationPacket;.class
        //  1141: invokedynamic   BootstrapMethod #99, get:()Ljava/util/function/Supplier;
        //  1146: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1149: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundCommandSuggestionPacket;.class
        //  1152: invokedynamic   BootstrapMethod #100, get:()Ljava/util/function/Supplier;
        //  1157: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1160: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundContainerAckPacket;.class
        //  1163: invokedynamic   BootstrapMethod #101, get:()Ljava/util/function/Supplier;
        //  1168: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1171: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundContainerButtonClickPacket;.class
        //  1174: invokedynamic   BootstrapMethod #102, get:()Ljava/util/function/Supplier;
        //  1179: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1182: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundContainerClickPacket;.class
        //  1185: invokedynamic   BootstrapMethod #103, get:()Ljava/util/function/Supplier;
        //  1190: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1193: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundContainerClosePacket;.class
        //  1196: invokedynamic   BootstrapMethod #104, get:()Ljava/util/function/Supplier;
        //  1201: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1204: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundCustomPayloadPacket;.class
        //  1207: invokedynamic   BootstrapMethod #105, get:()Ljava/util/function/Supplier;
        //  1212: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1215: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundEditBookPacket;.class
        //  1218: invokedynamic   BootstrapMethod #106, get:()Ljava/util/function/Supplier;
        //  1223: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1226: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundEntityTagQuery;.class
        //  1229: invokedynamic   BootstrapMethod #107, get:()Ljava/util/function/Supplier;
        //  1234: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1237: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;.class
        //  1240: invokedynamic   BootstrapMethod #108, get:()Ljava/util/function/Supplier;
        //  1245: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1248: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundJigsawGeneratePacket;.class
        //  1251: invokedynamic   BootstrapMethod #109, get:()Ljava/util/function/Supplier;
        //  1256: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1259: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundKeepAlivePacket;.class
        //  1262: invokedynamic   BootstrapMethod #110, get:()Ljava/util/function/Supplier;
        //  1267: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1270: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundLockDifficultyPacket;.class
        //  1273: invokedynamic   BootstrapMethod #111, get:()Ljava/util/function/Supplier;
        //  1278: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1281: ldc             Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Pos;.class
        //  1283: invokedynamic   BootstrapMethod #112, get:()Ljava/util/function/Supplier;
        //  1288: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1291: ldc             Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$PosRot;.class
        //  1293: invokedynamic   BootstrapMethod #113, get:()Ljava/util/function/Supplier;
        //  1298: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1301: ldc             Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket$Rot;.class
        //  1303: invokedynamic   BootstrapMethod #114, get:()Ljava/util/function/Supplier;
        //  1308: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1311: ldc             Lnet/minecraft/network/protocol/game/ServerboundMovePlayerPacket;.class
        //  1313: invokedynamic   BootstrapMethod #115, get:()Ljava/util/function/Supplier;
        //  1318: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1321: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundMoveVehiclePacket;.class
        //  1324: invokedynamic   BootstrapMethod #116, get:()Ljava/util/function/Supplier;
        //  1329: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1332: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundPaddleBoatPacket;.class
        //  1335: invokedynamic   BootstrapMethod #117, get:()Ljava/util/function/Supplier;
        //  1340: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1343: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundPickItemPacket;.class
        //  1346: invokedynamic   BootstrapMethod #118, get:()Ljava/util/function/Supplier;
        //  1351: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1354: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundPlaceRecipePacket;.class
        //  1357: invokedynamic   BootstrapMethod #119, get:()Ljava/util/function/Supplier;
        //  1362: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1365: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundPlayerAbilitiesPacket;.class
        //  1368: invokedynamic   BootstrapMethod #120, get:()Ljava/util/function/Supplier;
        //  1373: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1376: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundPlayerActionPacket;.class
        //  1379: invokedynamic   BootstrapMethod #121, get:()Ljava/util/function/Supplier;
        //  1384: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1387: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundPlayerCommandPacket;.class
        //  1390: invokedynamic   BootstrapMethod #122, get:()Ljava/util/function/Supplier;
        //  1395: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1398: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundPlayerInputPacket;.class
        //  1401: invokedynamic   BootstrapMethod #123, get:()Ljava/util/function/Supplier;
        //  1406: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1409: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundRecipeBookChangeSettingsPacket;.class
        //  1412: invokedynamic   BootstrapMethod #124, get:()Ljava/util/function/Supplier;
        //  1417: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1420: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundRecipeBookSeenRecipePacket;.class
        //  1423: invokedynamic   BootstrapMethod #125, get:()Ljava/util/function/Supplier;
        //  1428: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1431: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundRenameItemPacket;.class
        //  1434: invokedynamic   BootstrapMethod #126, get:()Ljava/util/function/Supplier;
        //  1439: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1442: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundResourcePackPacket;.class
        //  1445: invokedynamic   BootstrapMethod #127, get:()Ljava/util/function/Supplier;
        //  1450: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1453: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundSeenAdvancementsPacket;.class
        //  1456: invokedynamic   BootstrapMethod #128, get:()Ljava/util/function/Supplier;
        //  1461: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1464: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundSelectTradePacket;.class
        //  1467: invokedynamic   BootstrapMethod #129, get:()Ljava/util/function/Supplier;
        //  1472: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1475: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundSetBeaconPacket;.class
        //  1478: invokedynamic   BootstrapMethod #130, get:()Ljava/util/function/Supplier;
        //  1483: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1486: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundSetCarriedItemPacket;.class
        //  1489: invokedynamic   BootstrapMethod #131, get:()Ljava/util/function/Supplier;
        //  1494: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1497: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundSetCommandBlockPacket;.class
        //  1500: invokedynamic   BootstrapMethod #132, get:()Ljava/util/function/Supplier;
        //  1505: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1508: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundSetCommandMinecartPacket;.class
        //  1511: invokedynamic   BootstrapMethod #133, get:()Ljava/util/function/Supplier;
        //  1516: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1519: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket;.class
        //  1522: invokedynamic   BootstrapMethod #134, get:()Ljava/util/function/Supplier;
        //  1527: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1530: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundSetJigsawBlockPacket;.class
        //  1533: invokedynamic   BootstrapMethod #135, get:()Ljava/util/function/Supplier;
        //  1538: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1541: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundSetStructureBlockPacket;.class
        //  1544: invokedynamic   BootstrapMethod #136, get:()Ljava/util/function/Supplier;
        //  1549: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1552: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundSignUpdatePacket;.class
        //  1555: invokedynamic   BootstrapMethod #137, get:()Ljava/util/function/Supplier;
        //  1560: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1563: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundSwingPacket;.class
        //  1566: invokedynamic   BootstrapMethod #138, get:()Ljava/util/function/Supplier;
        //  1571: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1574: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundTeleportToEntityPacket;.class
        //  1577: invokedynamic   BootstrapMethod #139, get:()Ljava/util/function/Supplier;
        //  1582: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1585: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundUseItemOnPacket;.class
        //  1588: invokedynamic   BootstrapMethod #140, get:()Ljava/util/function/Supplier;
        //  1593: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1596: ldc_w           Lnet/minecraft/network/protocol/game/ServerboundUseItemPacket;.class
        //  1599: invokedynamic   BootstrapMethod #141, get:()Ljava/util/function/Supplier;
        //  1604: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1607: invokevirtual   net/minecraft/network/ConnectionProtocol$ProtocolBuilder.addFlow:(Lnet/minecraft/network/protocol/PacketFlow;Lnet/minecraft/network/ConnectionProtocol$PacketSet;)Lnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;
        //  1610: invokespecial   net/minecraft/network/ConnectionProtocol.<init>:(Ljava/lang/String;IILnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;)V
        //  1613: putstatic       net/minecraft/network/ConnectionProtocol.PLAY:Lnet/minecraft/network/ConnectionProtocol;
        //  1616: new             Lnet/minecraft/network/ConnectionProtocol;
        //  1619: dup            
        //  1620: ldc_w           "STATUS"
        //  1623: iconst_2       
        //  1624: iconst_1       
        //  1625: invokestatic    net/minecraft/network/ConnectionProtocol.protocol:()Lnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;
        //  1628: getstatic       net/minecraft/network/protocol/PacketFlow.SERVERBOUND:Lnet/minecraft/network/protocol/PacketFlow;
        //  1631: new             Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1634: dup            
        //  1635: aconst_null    
        //  1636: invokespecial   net/minecraft/network/ConnectionProtocol$PacketSet.<init>:(Lnet/minecraft/network/ConnectionProtocol$1;)V
        //  1639: ldc_w           Lnet/minecraft/network/protocol/status/ServerboundStatusRequestPacket;.class
        //  1642: invokedynamic   BootstrapMethod #142, get:()Ljava/util/function/Supplier;
        //  1647: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1650: ldc_w           Lnet/minecraft/network/protocol/status/ServerboundPingRequestPacket;.class
        //  1653: invokedynamic   BootstrapMethod #143, get:()Ljava/util/function/Supplier;
        //  1658: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1661: invokevirtual   net/minecraft/network/ConnectionProtocol$ProtocolBuilder.addFlow:(Lnet/minecraft/network/protocol/PacketFlow;Lnet/minecraft/network/ConnectionProtocol$PacketSet;)Lnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;
        //  1664: getstatic       net/minecraft/network/protocol/PacketFlow.CLIENTBOUND:Lnet/minecraft/network/protocol/PacketFlow;
        //  1667: new             Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1670: dup            
        //  1671: aconst_null    
        //  1672: invokespecial   net/minecraft/network/ConnectionProtocol$PacketSet.<init>:(Lnet/minecraft/network/ConnectionProtocol$1;)V
        //  1675: ldc_w           Lnet/minecraft/network/protocol/status/ClientboundStatusResponsePacket;.class
        //  1678: invokedynamic   BootstrapMethod #144, get:()Ljava/util/function/Supplier;
        //  1683: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1686: ldc_w           Lnet/minecraft/network/protocol/status/ClientboundPongResponsePacket;.class
        //  1689: invokedynamic   BootstrapMethod #145, get:()Ljava/util/function/Supplier;
        //  1694: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1697: invokevirtual   net/minecraft/network/ConnectionProtocol$ProtocolBuilder.addFlow:(Lnet/minecraft/network/protocol/PacketFlow;Lnet/minecraft/network/ConnectionProtocol$PacketSet;)Lnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;
        //  1700: invokespecial   net/minecraft/network/ConnectionProtocol.<init>:(Ljava/lang/String;IILnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;)V
        //  1703: putstatic       net/minecraft/network/ConnectionProtocol.STATUS:Lnet/minecraft/network/ConnectionProtocol;
        //  1706: new             Lnet/minecraft/network/ConnectionProtocol;
        //  1709: dup            
        //  1710: ldc_w           "LOGIN"
        //  1713: iconst_3       
        //  1714: iconst_2       
        //  1715: invokestatic    net/minecraft/network/ConnectionProtocol.protocol:()Lnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;
        //  1718: getstatic       net/minecraft/network/protocol/PacketFlow.CLIENTBOUND:Lnet/minecraft/network/protocol/PacketFlow;
        //  1721: new             Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1724: dup            
        //  1725: aconst_null    
        //  1726: invokespecial   net/minecraft/network/ConnectionProtocol$PacketSet.<init>:(Lnet/minecraft/network/ConnectionProtocol$1;)V
        //  1729: ldc_w           Lnet/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket;.class
        //  1732: invokedynamic   BootstrapMethod #146, get:()Ljava/util/function/Supplier;
        //  1737: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1740: ldc_w           Lnet/minecraft/network/protocol/login/ClientboundHelloPacket;.class
        //  1743: invokedynamic   BootstrapMethod #147, get:()Ljava/util/function/Supplier;
        //  1748: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1751: ldc_w           Lnet/minecraft/network/protocol/login/ClientboundGameProfilePacket;.class
        //  1754: invokedynamic   BootstrapMethod #148, get:()Ljava/util/function/Supplier;
        //  1759: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1762: ldc_w           Lnet/minecraft/network/protocol/login/ClientboundLoginCompressionPacket;.class
        //  1765: invokedynamic   BootstrapMethod #149, get:()Ljava/util/function/Supplier;
        //  1770: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1773: ldc_w           Lnet/minecraft/network/protocol/login/ClientboundCustomQueryPacket;.class
        //  1776: invokedynamic   BootstrapMethod #150, get:()Ljava/util/function/Supplier;
        //  1781: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1784: invokevirtual   net/minecraft/network/ConnectionProtocol$ProtocolBuilder.addFlow:(Lnet/minecraft/network/protocol/PacketFlow;Lnet/minecraft/network/ConnectionProtocol$PacketSet;)Lnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;
        //  1787: getstatic       net/minecraft/network/protocol/PacketFlow.SERVERBOUND:Lnet/minecraft/network/protocol/PacketFlow;
        //  1790: new             Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1793: dup            
        //  1794: aconst_null    
        //  1795: invokespecial   net/minecraft/network/ConnectionProtocol$PacketSet.<init>:(Lnet/minecraft/network/ConnectionProtocol$1;)V
        //  1798: ldc_w           Lnet/minecraft/network/protocol/login/ServerboundHelloPacket;.class
        //  1801: invokedynamic   BootstrapMethod #151, get:()Ljava/util/function/Supplier;
        //  1806: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1809: ldc_w           Lnet/minecraft/network/protocol/login/ServerboundKeyPacket;.class
        //  1812: invokedynamic   BootstrapMethod #152, get:()Ljava/util/function/Supplier;
        //  1817: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1820: ldc_w           Lnet/minecraft/network/protocol/login/ServerboundCustomQueryPacket;.class
        //  1823: invokedynamic   BootstrapMethod #153, get:()Ljava/util/function/Supplier;
        //  1828: invokevirtual   net/minecraft/network/ConnectionProtocol$PacketSet.addPacket:(Ljava/lang/Class;Ljava/util/function/Supplier;)Lnet/minecraft/network/ConnectionProtocol$PacketSet;
        //  1831: invokevirtual   net/minecraft/network/ConnectionProtocol$ProtocolBuilder.addFlow:(Lnet/minecraft/network/protocol/PacketFlow;Lnet/minecraft/network/ConnectionProtocol$PacketSet;)Lnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;
        //  1834: invokespecial   net/minecraft/network/ConnectionProtocol.<init>:(Ljava/lang/String;IILnet/minecraft/network/ConnectionProtocol$ProtocolBuilder;)V
        //  1837: putstatic       net/minecraft/network/ConnectionProtocol.LOGIN:Lnet/minecraft/network/ConnectionProtocol;
        //  1840: iconst_4       
        //  1841: anewarray       Lnet/minecraft/network/ConnectionProtocol;
        //  1844: dup            
        //  1845: iconst_0       
        //  1846: getstatic       net/minecraft/network/ConnectionProtocol.HANDSHAKING:Lnet/minecraft/network/ConnectionProtocol;
        //  1849: aastore        
        //  1850: dup            
        //  1851: iconst_1       
        //  1852: getstatic       net/minecraft/network/ConnectionProtocol.PLAY:Lnet/minecraft/network/ConnectionProtocol;
        //  1855: aastore        
        //  1856: dup            
        //  1857: iconst_2       
        //  1858: getstatic       net/minecraft/network/ConnectionProtocol.STATUS:Lnet/minecraft/network/ConnectionProtocol;
        //  1861: aastore        
        //  1862: dup            
        //  1863: iconst_3       
        //  1864: getstatic       net/minecraft/network/ConnectionProtocol.LOGIN:Lnet/minecraft/network/ConnectionProtocol;
        //  1867: aastore        
        //  1868: putstatic       net/minecraft/network/ConnectionProtocol.$VALUES:[Lnet/minecraft/network/ConnectionProtocol;
        //  1871: iconst_4       
        //  1872: anewarray       Lnet/minecraft/network/ConnectionProtocol;
        //  1875: putstatic       net/minecraft/network/ConnectionProtocol.LOOKUP:[Lnet/minecraft/network/ConnectionProtocol;
        //  1878: invokestatic    com/google/common/collect/Maps.newHashMap:()Ljava/util/HashMap;
        //  1881: putstatic       net/minecraft/network/ConnectionProtocol.PROTOCOL_BY_PACKET:Ljava/util/Map;
        //  1884: invokestatic    net/minecraft/network/ConnectionProtocol.values:()[Lnet/minecraft/network/ConnectionProtocol;
        //  1887: astore_0       
        //  1888: aload_0        
        //  1889: arraylength    
        //  1890: istore_1       
        //  1891: iconst_0       
        //  1892: istore_2       
        //  1893: iload_2        
        //  1894: iload_1        
        //  1895: if_icmpge       1982
        //  1898: aload_0        
        //  1899: iload_2        
        //  1900: aaload         
        //  1901: astore_3       
        //  1902: aload_3        
        //  1903: invokevirtual   net/minecraft/network/ConnectionProtocol.getId:()I
        //  1906: istore          integer5
        //  1908: iload           integer5
        //  1910: iconst_m1      
        //  1911: if_icmplt       1920
        //  1914: iload           integer5
        //  1916: iconst_2       
        //  1917: if_icmple       1952
        //  1920: new             Ljava/lang/Error;
        //  1923: dup            
        //  1924: new             Ljava/lang/StringBuilder;
        //  1927: dup            
        //  1928: invokespecial   java/lang/StringBuilder.<init>:()V
        //  1931: ldc_w           "Invalid protocol ID "
        //  1934: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1937: iload           integer5
        //  1939: invokestatic    java/lang/Integer.toString:(I)Ljava/lang/String;
        //  1942: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1945: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //  1948: invokespecial   java/lang/Error.<init>:(Ljava/lang/String;)V
        //  1951: athrow         
        //  1952: getstatic       net/minecraft/network/ConnectionProtocol.LOOKUP:[Lnet/minecraft/network/ConnectionProtocol;
        //  1955: iload           integer5
        //  1957: iconst_m1      
        //  1958: isub           
        //  1959: aload_3        
        //  1960: aastore        
        //  1961: aload_3        
        //  1962: getfield        net/minecraft/network/ConnectionProtocol.flows:Ljava/util/Map;
        //  1965: aload_3        
        //  1966: invokedynamic   BootstrapMethod #154, accept:(Lnet/minecraft/network/ConnectionProtocol;)Ljava/util/function/BiConsumer;
        //  1971: invokeinterface java/util/Map.forEach:(Ljava/util/function/BiConsumer;)V
        //  1976: iinc            2, 1
        //  1979: goto            1893
        //  1982: return         
        //    StackMapTable: 00 04 FE 07 65 07 00 3A 01 01 FF 00 1A 00 05 00 00 00 00 01 00 00 FF 00 1F 00 05 07 00 3A 01 01 07 00 02 01 00 00 FF 00 1D 00 00 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.util.Vector.get(Vector.java:751)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
        //     at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
        //     at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1577)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:715)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3216)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visitParameterizedType(TypeAnalysis.java:3127)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.decompiler.ast.TypeAnalysis$AddMappingsForArgumentVisitor.visit(TypeAnalysis.java:3136)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2617)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:266)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:242)
        //     at java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:193)
        //     at java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1382)
        //     at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:482)
        //     at java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:291)
        //     at java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:731)
        //     at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:289)
        //     at java.util.concurrent.ForkJoinPool.helpComplete(ForkJoinPool.java:1870)
        //     at java.util.concurrent.ForkJoinPool.externalHelpComplete(ForkJoinPool.java:2467)
        //     at java.util.concurrent.ForkJoinTask.externalAwaitDone(ForkJoinTask.java:324)
        //     at java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:405)
        //     at java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:734)
        //     at java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:160)
        //     at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:174)
        //     at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
        //     at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:418)
        //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
        //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
        //     at java.lang.Thread.run(Thread.java:748)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    static class PacketSet<T extends PacketListener> {
        private final Object2IntMap<Class<? extends Packet<T>>> classToId;
        private final List<Supplier<? extends Packet<T>>> idToConstructor;
        
        private PacketSet() {
            this.classToId = Util.make((Object2IntMap)new Object2IntOpenHashMap(), (java.util.function.Consumer<Object2IntMap>)(object2IntOpenHashMap -> object2IntOpenHashMap.defaultReturnValue(-1)));
            this.idToConstructor = (List<Supplier<? extends Packet<T>>>)Lists.newArrayList();
        }
        
        public <P extends Packet<T>> PacketSet<T> addPacket(final Class<P> class1, final Supplier<P> supplier) {
            final int integer4 = this.idToConstructor.size();
            final int integer5 = this.classToId.put(class1, integer4);
            if (integer5 != -1) {
                final String string6 = new StringBuilder().append("Packet ").append(class1).append(" is already registered to ID ").append(integer5).toString();
                LogManager.getLogger().fatal(string6);
                throw new IllegalArgumentException(string6);
            }
            this.idToConstructor.add(supplier);
            return this;
        }
        
        @Nullable
        public Integer getId(final Class<?> class1) {
            final int integer3 = this.classToId.getInt(class1);
            return (integer3 == -1) ? null : Integer.valueOf(integer3);
        }
        
        @Nullable
        public Packet<?> createPacket(final int integer) {
            final Supplier<? extends Packet<T>> supplier3 = this.idToConstructor.get(integer);
            return ((supplier3 != null) ? ((Packet)supplier3.get()) : null);
        }
        
        public Iterable<Class<? extends Packet<?>>> getAllPackets() {
            return (Iterable<Class<? extends Packet<?>>>)Iterables.unmodifiableIterable((Iterable)this.classToId.keySet());
        }
    }
    
    static class ProtocolBuilder {
        private final Map<PacketFlow, PacketSet<?>> flows;
        
        private ProtocolBuilder() {
            this.flows = (Map<PacketFlow, PacketSet<?>>)Maps.newEnumMap((Class)PacketFlow.class);
        }
        
        public <T extends PacketListener> ProtocolBuilder addFlow(final PacketFlow ok, final PacketSet<T> a) {
            this.flows.put(ok, a);
            return this;
        }
    }
}
