/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers;

import handlers.actionhandlers.*;
import handlers.actionshifthandlers.*;
import handlers.admincommandhandlers.*;
import handlers.bypasshandlers.*;
import handlers.chathandlers.*;
import handlers.communityboard.*;
import handlers.itemhandlers.*;
import handlers.playeractions.*;
import handlers.punishmenthandlers.BanHandler;
import handlers.punishmenthandlers.ChatBanHandler;
import handlers.punishmenthandlers.JailHandler;
import handlers.targethandlers.*;
import handlers.targethandlers.affectobject.*;
import handlers.targethandlers.affectscope.*;
import handlers.usercommandhandlers.*;
import handlers.voicedcommandhandlers.*;
import org.l2jmobius.Config;
import org.l2jmobius.gameserver.handler.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Master handler.
 * @author UnAfraid
 */
public class MasterHandler
{
	private static final Logger LOGGER = Logger.getLogger(MasterHandler.class.getName());
	
	private static final IHandler<?, ?>[] LOAD_INSTANCES =
	{
		ActionHandler.getInstance(),
		ActionShiftHandler.getInstance(),
		AdminCommandHandler.getInstance(),
		BypassHandler.getInstance(),
		ChatHandler.getInstance(),
		CommunityBoardHandler.getInstance(),
		ItemHandler.getInstance(),
		PunishmentHandler.getInstance(),
		UserCommandHandler.getInstance(),
		VoicedCommandHandler.getInstance(),
		TargetHandler.getInstance(),
		AffectObjectHandler.getInstance(),
		AffectScopeHandler.getInstance(),
		PlayerActionHandler.getInstance()
	};
	
	private static final Class<?>[][] HANDLERS =
	{
		{
			// Action Handlers
			ArtefactAction.class,
			DecoyAction.class,
			DoorAction.class,
			ItemAction.class,
			NpcAction.class,
			PlayerAction.class,
			PetAction.class,
			StaticObjectAction.class,
			SummonAction.class,
			TrapAction.class,
		},
		{
			// Action Shift Handlers
			DoorActionShift.class,
			ItemActionShift.class,
			NpcActionShift.class,
			PlayerActionShift.class,
			StaticObjectActionShift.class,
			SummonActionShift.class,
		},
		{
			// Admin Command Handlers
			AdminAdmin.class,
			AdminAnnouncements.class,
			AdminBBS.class,
			AdminBuffs.class,
			AdminCamera.class,
			AdminChangeAccessLevel.class,
			AdminClan.class,
			AdminClanHall.class,
			AdminCastle.class,
			AdminPcCondOverride.class,
			AdminCreateItem.class,
			AdminCursedWeapons.class,
			AdminDelete.class,
			AdminDestroyItems.class,
			AdminDisconnect.class,
			AdminDoorControl.class,
			AdminEditChar.class,
			AdminEffects.class,
			AdminElement.class,
			AdminEnchant.class,
			AdminEvents.class,
			AdminExpSp.class,
			AdminFakePlayers.class,
			AdminFence.class,
			AdminFightCalculator.class,
			AdminFortSiege.class,
			AdminGeodata.class,
			AdminGm.class,
			AdminGmChat.class,
			AdminGmSpeed.class,
			AdminGraciaSeeds.class,
			AdminGrandBoss.class,
			AdminHeal.class,
			AdminHide.class,
			AdminHtml.class,
			AdminHwid.class,
			AdminInstance.class,
			AdminInstanceZone.class,
			AdminInvul.class,
			AdminKick.class,
			AdminKill.class,
			AdminLevel.class,
			AdminLogin.class,
			AdminManor.class,
			AdminMenu.class,
			AdminMessages.class,
			AdminMissingHtmls.class,
			AdminMobGroup.class,
			AdminOlympiad.class,
			AdminOnline.class,
			AdminPathNode.class,
			AdminPcCafePoints.class,
			AdminPetition.class,
			AdminPForge.class,
			AdminPledge.class,
			AdminZones.class,
			AdminPremium.class,
			AdminPrimePoints.class,
			AdminPunishment.class,
			AdminQuest.class,
			AdminReload.class,
			AdminRepairChar.class,
			AdminRes.class,
			AdminRide.class,
			AdminScan.class,
			AdminServerInfo.class,
			AdminShop.class,
			AdminShowQuests.class,
			AdminShutdown.class,
			AdminSkill.class,
			AdminSpawn.class,
			AdminSummon.class,
			AdminSuperHaste.class,
			AdminTarget.class,
			AdminTargetSay.class,
			AdminTeleport.class,
			AdminTest.class,
			AdminTransform.class,
			AdminVitality.class,
			AdminZone.class,
		},
		{
			// Bypass Handlers
			Augment.class,
			Buy.class,
			ChangePlayerName.class,
			ChatLink.class,
			ClanWarehouse.class,
			EnsoulWindow.class,
			FindPvP.class,
			Freight.class,
			ItemAuctionLink.class,
			Link.class,
			Multisell.class,
			NpcViewMod.class,
			Observation.class,
			QuestLink.class,
			PlayerHelp.class,
			PrivateWarehouse.class,
			ReleaseAttribute.class,
			SkillList.class,
			TerritoryStatus.class,
			TutorialClose.class,
			UpgradeEquipment.class,
			VoiceCommand.class,
			Wear.class,
			SupportMagic.class,
		},
		{
			// Chat Handlers
			ChatGeneral.class,
			ChatAlliance.class,
			ChatClan.class,
			ChatHeroVoice.class,
			ChatParty.class,
			ChatPartyMatchRoom.class,
			ChatPartyRoomAll.class,
			ChatPartyRoomCommander.class,
			ChatPetition.class,
			ChatShout.class,
			ChatWhisper.class,
			ChatTrade.class,
			ChatWorld.class,
		},
		{
			// Community Board
			ClanBoard.class,
			FavoriteBoard.class,
			FriendsBoard.class,
			HomeBoard.class,
			HomepageBoard.class,
			MailBoard.class,
			MemoBoard.class,
			RegionBoard.class,
			DropSearchBoard.class,
		},
		{
			// Item Handlers
			Appearance.class,
			BeastSoulShot.class,
			BeastSpiritShot.class,
			BlessedSoulShots.class,
			BlessedSpiritShot.class,
			Book.class,
			Bypass.class,
			Calculator.class,
			ChangeAttributeCrystal.class,
			CharmOfCourage.class,
			CreateHomunculus.class,
			Elixir.class,
			EnchantAttribute.class,
			EnchantScrolls.class,
			EventItem.class,
			ExtractableItems.class,
			FatedSupportBox.class,
			FishShots.class,
			Harvester.class,
			HeroBook.class,
			HomunculusHourglass.class,
			ItemSkills.class,
			ItemSkillsTemplate.class,
			Maps.class,
			MercTicket.class,
			NicknameColor.class,
			PaulinasSupportBox.class,
			PetFood.class,
			Recipes.class,
			RollingDice.class,
			Seed.class,
			SoulShots.class,
			SpecialXMas.class,
			SpiritShot.class,
			SummonItems.class,
			TeleportCube.class,
		},
		{
			// Punishment Handlers
			BanHandler.class,
			ChatBanHandler.class,
			JailHandler.class,
		},
		{
			// User Command Handlers
			BloodyCoinCommand.class,
			ClanPenalty.class,
			ClanWarsList.class,
			Dismount.class,
			ExperienceGain.class,
			Unstuck.class,
			InstanceZone.class,
			Loc.class,
			Mount.class,
			PartyInfo.class,
			Time.class,
			OlympiadStat.class,
			ChannelLeave.class,
			ChannelDelete.class,
			ChannelInfo.class,
			MyBirthday.class,
			SiegeStatus.class,
		},
		{
			// TODO: Add configuration options for this voiced commands:
			// CastleVCmd.class,
			// SetVCmd.class,
			Config.BANKING_SYSTEM_ENABLED ? Banking.class : null,
			Config.CHAT_ADMIN ? ChatAdmin.class : null,
			Config.MULTILANG_ENABLE && Config.MULTILANG_VOICED_ALLOW ? Lang.class : null,
			Config.ALLOW_CHANGE_PASSWORD ? ChangePassword.class : null,
			Config.ENABLE_OFFLINE_PLAY_COMMAND ? OfflinePlay.class : null,
			Config.ENABLE_OFFLINE_COMMAND && (Config.OFFLINE_TRADE_ENABLE || Config.OFFLINE_CRAFT_ENABLE) ? Offline.class : null,
			Config.ENABLE_ONLINE_COMMAND ? Online.class : null,
			Config.PREMIUM_SYSTEM_ENABLED ? Premium.class : null,
			Config.AUTO_POTIONS_ENABLED ? AutoPotion.class : null,
		},
		{
			// Target Handlers
			AdvanceBase.class,
			Artillery.class,
			DoorTreasure.class,
			Enemy.class,
			EnemyNot.class,
			EnemyOnly.class,
			FortressFlagpole.class,
			Ground.class,
			HolyThing.class,
			Item.class,
			MyMentor.class,
			MyParty.class,
			None.class,
			NpcBody.class,
			Others.class,
			OwnerPet.class,
			PcBody.class,
			Self.class,
			Summon.class,
			Target.class,
			WyvernTarget.class,
		},
		{
			// Affect Objects
			All.class,
			Clan.class,
			Friend.class,
			FriendPc.class,
			HiddenPlace.class,
			Invisible.class,
			NotFriend.class,
			NotFriendPc.class,
			ObjectDeadNpcBody.class,
			UndeadRealEnemy.class,
			WyvernObject.class,
		},
		{
			// Affect Scopes
			BalakasScope.class,
			DeadParty.class,
			DeadPartyPledge.class,
			DeadPledge.class,
			DeadUnion.class,
			Fan.class,
			FanPB.class,
			Party.class,
			PartyPledge.class,
			Pledge.class,
			PointBlank.class,
			Range.class,
			RangeSortByHp.class,
			RingRange.class,
			Single.class,
			Square.class,
			SquarePB.class,
			StaticObjectScope.class,
			SummonExceptMaster.class,
		},
		{
			AirshipAction.class,
			BotReport.class,
			InstanceZoneInfo.class,
			PetAttack.class,
			PetHold.class,
			PetMove.class,
			PetSkillUse.class,
			PetStop.class,
			PrivateStore.class,
			Ride.class,
			RunWalk.class,
			ServitorAttack.class,
			ServitorHold.class,
			ServitorMode.class,
			ServitorMove.class,
			ServitorSkillUse.class,
			ServitorStop.class,
			SitStand.class,
			SocialAction.class,
			TacticalSignTarget.class,
			TacticalSignUse.class,
			TeleportBookmark.class,
			UnsummonPet.class,
			UnsummonServitor.class,
			WaitingAction.class
		}
	};
	
	public static void main(String[] args)
	{
		LOGGER.info("Loading Handlers...");
		
		final Map<IHandler<?, ?>, Method> registerHandlerMethods = new HashMap<>();
		for (IHandler<?, ?> loadInstance : LOAD_INSTANCES)
		{
			registerHandlerMethods.put(loadInstance, null);
			for (Method method : loadInstance.getClass().getMethods())
			{
				if (method.getName().equals("registerHandler") && !method.isBridge())
				{
					registerHandlerMethods.put(loadInstance, method);
				}
			}
		}
		
		registerHandlerMethods.entrySet().stream().filter(e -> e.getValue() == null).forEach(e -> LOGGER.warning("Failed loading handlers of: " + e.getKey().getClass().getSimpleName() + " seems registerHandler function does not exist."));
		
		for (Class<?>[] classes : HANDLERS)
		{
			for (Class<?> c : classes)
			{
				if (c == null)
				{
					continue; // Disabled handler
				}
				
				try
				{
					final Object handler = c.getDeclaredConstructor().newInstance();
					for (Entry<IHandler<?, ?>, Method> entry : registerHandlerMethods.entrySet())
					{
						if ((entry.getValue() != null) && entry.getValue().getParameterTypes()[0].isInstance(handler))
						{
							entry.getValue().invoke(entry.getKey(), handler);
						}
					}
				}
				catch (Exception e)
				{
					LOGGER.log(Level.WARNING, "Failed loading handler: " + c.getSimpleName(), e);
				}
			}
		}
		
		for (IHandler<?, ?> loadInstance : LOAD_INSTANCES)
		{
			LOGGER.info(loadInstance.getClass().getSimpleName() + ": Loaded " + loadInstance.size() + " handlers.");
		}
		
		LOGGER.info("Handlers Loaded...");
	}
}
