package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;

public interface ServerGamePacketListener extends PacketListener {
    void handleAnimate(final ServerboundSwingPacket tq);
    
    void handleChat(final ServerboundChatPacket se);
    
    void handleClientCommand(final ServerboundClientCommandPacket sf);
    
    void handleClientInformation(final ServerboundClientInformationPacket sg);
    
    void handleContainerAck(final ServerboundContainerAckPacket si);
    
    void handleContainerButtonClick(final ServerboundContainerButtonClickPacket sj);
    
    void handleContainerClick(final ServerboundContainerClickPacket sk);
    
    void handlePlaceRecipe(final ServerboundPlaceRecipePacket sx);
    
    void handleContainerClose(final ServerboundContainerClosePacket sl);
    
    void handleCustomPayload(final ServerboundCustomPayloadPacket sm);
    
    void handleInteract(final ServerboundInteractPacket sp);
    
    void handleKeepAlive(final ServerboundKeepAlivePacket sr);
    
    void handleMovePlayer(final ServerboundMovePlayerPacket st);
    
    void handlePlayerAbilities(final ServerboundPlayerAbilitiesPacket sy);
    
    void handlePlayerAction(final ServerboundPlayerActionPacket sz);
    
    void handlePlayerCommand(final ServerboundPlayerCommandPacket ta);
    
    void handlePlayerInput(final ServerboundPlayerInputPacket tb);
    
    void handleSetCarriedItem(final ServerboundSetCarriedItemPacket tj);
    
    void handleSetCreativeModeSlot(final ServerboundSetCreativeModeSlotPacket tm);
    
    void handleSignUpdate(final ServerboundSignUpdatePacket tp);
    
    void handleUseItemOn(final ServerboundUseItemOnPacket ts);
    
    void handleUseItem(final ServerboundUseItemPacket tt);
    
    void handleTeleportToEntityPacket(final ServerboundTeleportToEntityPacket tr);
    
    void handleResourcePackResponse(final ServerboundResourcePackPacket tf);
    
    void handlePaddleBoat(final ServerboundPaddleBoatPacket sv);
    
    void handleMoveVehicle(final ServerboundMoveVehiclePacket su);
    
    void handleAcceptTeleportPacket(final ServerboundAcceptTeleportationPacket sb);
    
    void handleRecipeBookSeenRecipePacket(final ServerboundRecipeBookSeenRecipePacket td);
    
    void handleRecipeBookChangeSettingsPacket(final ServerboundRecipeBookChangeSettingsPacket tc);
    
    void handleSeenAdvancements(final ServerboundSeenAdvancementsPacket tg);
    
    void handleCustomCommandSuggestions(final ServerboundCommandSuggestionPacket sh);
    
    void handleSetCommandBlock(final ServerboundSetCommandBlockPacket tk);
    
    void handleSetCommandMinecart(final ServerboundSetCommandMinecartPacket tl);
    
    void handlePickItem(final ServerboundPickItemPacket sw);
    
    void handleRenameItem(final ServerboundRenameItemPacket te);
    
    void handleSetBeaconPacket(final ServerboundSetBeaconPacket ti);
    
    void handleSetStructureBlock(final ServerboundSetStructureBlockPacket to);
    
    void handleSelectTrade(final ServerboundSelectTradePacket th);
    
    void handleEditBook(final ServerboundEditBookPacket sn);
    
    void handleEntityTagQuery(final ServerboundEntityTagQuery so);
    
    void handleBlockEntityTagQuery(final ServerboundBlockEntityTagQuery sc);
    
    void handleSetJigsawBlock(final ServerboundSetJigsawBlockPacket tn);
    
    void handleJigsawGenerate(final ServerboundJigsawGeneratePacket sq);
    
    void handleChangeDifficulty(final ServerboundChangeDifficultyPacket sd);
    
    void handleLockDifficulty(final ServerboundLockDifficultyPacket ss);
}
