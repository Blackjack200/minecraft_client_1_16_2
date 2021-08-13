package net.minecraft.network.protocol.game;

import net.minecraft.network.PacketListener;

public interface ClientGamePacketListener extends PacketListener {
    void handleAddEntity(final ClientboundAddEntityPacket on);
    
    void handleAddExperienceOrb(final ClientboundAddExperienceOrbPacket oo);
    
    void handleAddMob(final ClientboundAddMobPacket op);
    
    void handleAddObjective(final ClientboundSetObjectivePacket rg);
    
    void handleAddPainting(final ClientboundAddPaintingPacket oq);
    
    void handleAddPlayer(final ClientboundAddPlayerPacket or);
    
    void handleAnimate(final ClientboundAnimatePacket os);
    
    void handleAwardStats(final ClientboundAwardStatsPacket ot);
    
    void handleAddOrRemoveRecipes(final ClientboundRecipePacket ql);
    
    void handleBlockDestruction(final ClientboundBlockDestructionPacket ov);
    
    void handleOpenSignEditor(final ClientboundOpenSignEditorPacket qe);
    
    void handleBlockEntityData(final ClientboundBlockEntityDataPacket ow);
    
    void handleBlockEvent(final ClientboundBlockEventPacket ox);
    
    void handleBlockUpdate(final ClientboundBlockUpdatePacket oy);
    
    void handleChat(final ClientboundChatPacket pb);
    
    void handleChunkBlocksUpdate(final ClientboundSectionBlocksUpdatePacket qr);
    
    void handleMapItemData(final ClientboundMapItemDataPacket py);
    
    void handleContainerAck(final ClientboundContainerAckPacket pe);
    
    void handleContainerClose(final ClientboundContainerClosePacket pf);
    
    void handleContainerContent(final ClientboundContainerSetContentPacket pg);
    
    void handleHorseScreenOpen(final ClientboundHorseScreenOpenPacket pr);
    
    void handleContainerSetData(final ClientboundContainerSetDataPacket ph);
    
    void handleContainerSetSlot(final ClientboundContainerSetSlotPacket pi);
    
    void handleCustomPayload(final ClientboundCustomPayloadPacket pk);
    
    void handleDisconnect(final ClientboundDisconnectPacket pm);
    
    void handleEntityEvent(final ClientboundEntityEventPacket pn);
    
    void handleEntityLinkPacket(final ClientboundSetEntityLinkPacket rb);
    
    void handleSetEntityPassengersPacket(final ClientboundSetPassengersPacket rh);
    
    void handleExplosion(final ClientboundExplodePacket po);
    
    void handleGameEvent(final ClientboundGameEventPacket pq);
    
    void handleKeepAlive(final ClientboundKeepAlivePacket ps);
    
    void handleLevelChunk(final ClientboundLevelChunkPacket pt);
    
    void handleForgetLevelChunk(final ClientboundForgetLevelChunkPacket pp);
    
    void handleLevelEvent(final ClientboundLevelEventPacket pu);
    
    void handleLogin(final ClientboundLoginPacket px);
    
    void handleMoveEntity(final ClientboundMoveEntityPacket qa);
    
    void handleMovePlayer(final ClientboundPlayerPositionPacket qk);
    
    void handleParticleEvent(final ClientboundLevelParticlesPacket pv);
    
    void handlePlayerAbilities(final ClientboundPlayerAbilitiesPacket qg);
    
    void handlePlayerInfo(final ClientboundPlayerInfoPacket qi);
    
    void handleRemoveEntity(final ClientboundRemoveEntitiesPacket qm);
    
    void handleRemoveMobEffect(final ClientboundRemoveMobEffectPacket qn);
    
    void handleRespawn(final ClientboundRespawnPacket qp);
    
    void handleRotateMob(final ClientboundRotateHeadPacket qq);
    
    void handleSetCarriedItem(final ClientboundSetCarriedItemPacket qv);
    
    void handleSetDisplayObjective(final ClientboundSetDisplayObjectivePacket qz);
    
    void handleSetEntityData(final ClientboundSetEntityDataPacket ra);
    
    void handleSetEntityMotion(final ClientboundSetEntityMotionPacket rc);
    
    void handleSetEquipment(final ClientboundSetEquipmentPacket rd);
    
    void handleSetExperience(final ClientboundSetExperiencePacket re);
    
    void handleSetHealth(final ClientboundSetHealthPacket rf);
    
    void handleSetPlayerTeamPacket(final ClientboundSetPlayerTeamPacket ri);
    
    void handleSetScore(final ClientboundSetScorePacket rj);
    
    void handleSetSpawn(final ClientboundSetDefaultSpawnPositionPacket qy);
    
    void handleSetTime(final ClientboundSetTimePacket rk);
    
    void handleSoundEvent(final ClientboundSoundPacket rn);
    
    void handleSoundEntityEvent(final ClientboundSoundEntityPacket rm);
    
    void handleCustomSoundEvent(final ClientboundCustomSoundPacket pl);
    
    void handleTakeItemEntity(final ClientboundTakeItemEntityPacket rr);
    
    void handleTeleportEntity(final ClientboundTeleportEntityPacket rs);
    
    void handleUpdateAttributes(final ClientboundUpdateAttributesPacket ru);
    
    void handleUpdateMobEffect(final ClientboundUpdateMobEffectPacket rv);
    
    void handleUpdateTags(final ClientboundUpdateTagsPacket rx);
    
    void handlePlayerCombat(final ClientboundPlayerCombatPacket qh);
    
    void handleChangeDifficulty(final ClientboundChangeDifficultyPacket pa);
    
    void handleSetCamera(final ClientboundSetCameraPacket qu);
    
    void handleSetBorder(final ClientboundSetBorderPacket qt);
    
    void handleSetTitles(final ClientboundSetTitlesPacket rl);
    
    void handleTabListCustomisation(final ClientboundTabListPacket rp);
    
    void handleResourcePack(final ClientboundResourcePackPacket qo);
    
    void handleBossUpdate(final ClientboundBossEventPacket oz);
    
    void handleItemCooldown(final ClientboundCooldownPacket pj);
    
    void handleMoveVehicle(final ClientboundMoveVehiclePacket qb);
    
    void handleUpdateAdvancementsPacket(final ClientboundUpdateAdvancementsPacket rt);
    
    void handleSelectAdvancementsTab(final ClientboundSelectAdvancementsTabPacket qs);
    
    void handlePlaceRecipe(final ClientboundPlaceGhostRecipePacket qf);
    
    void handleCommands(final ClientboundCommandsPacket pd);
    
    void handleStopSoundEvent(final ClientboundStopSoundPacket ro);
    
    void handleCommandSuggestions(final ClientboundCommandSuggestionsPacket pc);
    
    void handleUpdateRecipes(final ClientboundUpdateRecipesPacket rw);
    
    void handleLookAt(final ClientboundPlayerLookAtPacket qj);
    
    void handleTagQueryPacket(final ClientboundTagQueryPacket rq);
    
    void handleLightUpdatePacked(final ClientboundLightUpdatePacket pw);
    
    void handleOpenBook(final ClientboundOpenBookPacket qc);
    
    void handleOpenScreen(final ClientboundOpenScreenPacket qd);
    
    void handleMerchantOffers(final ClientboundMerchantOffersPacket pz);
    
    void handleSetChunkCacheRadius(final ClientboundSetChunkCacheRadiusPacket qx);
    
    void handleSetChunkCacheCenter(final ClientboundSetChunkCacheCenterPacket qw);
    
    void handleBlockBreakAck(final ClientboundBlockBreakAckPacket ou);
}
