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
package handlers.bypasshandlers;

import org.l2jmobius.gameserver.handler.IBypassHandler;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.skill.SkillCaster;

public class SupportMagic implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"supportmagicservitor",
		"supportmagic"
	};
	
	// Buffs
	private static final SkillHolder HASTE_1 = new SkillHolder(4327, 1);
	private static final SkillHolder HASTE_2 = new SkillHolder(5632, 1);
	private static final SkillHolder[] FIGHTER_BUFFS =
	{
		new SkillHolder(4322, 1), // Wind Walk
		new SkillHolder(4323, 1), // Shield
		new SkillHolder(5637, 1), // Magic Barrier
		new SkillHolder(4324, 1), // Bless the Body
		new SkillHolder(4325, 1), // Vampiric Rage
		new SkillHolder(4326, 1), // Regeneration
	};
	private static final SkillHolder[] MAGE_BUFFS =
	{
		new SkillHolder(4322, 1), // Wind Walk
		new SkillHolder(4323, 1), // Shield
		new SkillHolder(5637, 1), // Magic Barrier
		new SkillHolder(4328, 1), // Bless the Soul
		new SkillHolder(4329, 1), // Acumen
		new SkillHolder(4330, 1), // Concentration
		new SkillHolder(4331, 1), // Empower
	};
	private static final SkillHolder[] SUMMON_BUFFS =
	{
		new SkillHolder(4322, 1), // Wind Walk
		new SkillHolder(4323, 1), // Shield
		new SkillHolder(5637, 1), // Magic Barrier
		new SkillHolder(4324, 1), // Bless the Body
		new SkillHolder(4325, 1), // Vampiric Rage
		new SkillHolder(4326, 1), // Regeneration
		new SkillHolder(4328, 1), // Bless the Soul
		new SkillHolder(4329, 1), // Acumen
		new SkillHolder(4330, 1), // Concentration
		new SkillHolder(4331, 1), // Empower
	};
	
	// Levels
	private static final int LOWEST_LEVEL = 6;
	private static final int HIGHEST_LEVEL = 41;
	private static final int HASTE_LEVEL_2 = 20;
	
	@Override
	public boolean useBypass(String command, Player player, Creature target)
	{
		if (!target.isNpc() || player.isCursedWeaponEquipped())
		{
			return false;
		}
		
		if (command.equalsIgnoreCase(COMMANDS[0]))
		{
			makeSupportMagic(player, (Npc) target, true);
		}
		else if (command.equalsIgnoreCase(COMMANDS[1]))
		{
			makeSupportMagic(player, (Npc) target, false);
		}
		return true;
	}
	
	private void makeSupportMagic(Player player, Npc npc, boolean isSummon)
	{
		final int level = player.getLevel();
		if (level > HIGHEST_LEVEL)
		{
			npc.showChatWindow(player, "data/html/default/SupportMagicHighLevel.htm");
			return;
		}
		else if (level < LOWEST_LEVEL)
		{
			npc.showChatWindow(player, "data/html/default/SupportMagicLowLevel.htm");
			return;
		}
		if(level < HIGHEST_LEVEL && level > LOWEST_LEVEL){
			if (isSummon)
			{
				npc.setTarget(player.getPet());
				for (SkillHolder holder : SUMMON_BUFFS) {
					SkillCaster.triggerCast(npc, player, holder.getSkill());
				}

				if (level >= HASTE_LEVEL_2)
				{
					SkillCaster.triggerCast(npc, player, HASTE_2.getSkill());
				}
				else
				{
					SkillCaster.triggerCast(npc, player, HASTE_1.getSkill());
				}
			}
			else
			{
				npc.setTarget(player);
				if (player.isMageClass())
				{
					for (SkillHolder holder : MAGE_BUFFS)
					{
						SkillCaster.triggerCast(npc, player, holder.getSkill());
					}
				}
				else
				{
					for (SkillHolder holder : FIGHTER_BUFFS)
					{
						SkillCaster.triggerCast(npc, player, holder.getSkill());
					}

					if (level >= HASTE_LEVEL_2)
					{
						npc.setTarget(player);
						SkillCaster.triggerCast(npc, player, HASTE_2.getSkill());
					}
					else
					{
						SkillCaster.triggerCast(npc, player, HASTE_1.getSkill());
					}
				}
			}
		}
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}