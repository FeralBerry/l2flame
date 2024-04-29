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
package ai.others.AdventurersGuide;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.SkillHolder;
import org.l2jmobius.gameserver.model.skill.Skill;
import org.l2jmobius.gameserver.model.skill.SkillCaster;

import ai.AbstractNpcAI;

/**
 * Adventurers Guide AI.
 * @author St3eT
 */
public class AdventurersGuide extends AbstractNpcAI
{
	// NPC
	private static final int[] ADVENTURERS_GUIDE =
	{
		32327,
		33950,
	};
	// Items
	private static final int ADENA = 57;
	private static final int GEMSTONE_R = 19440;
	// Skills
	private static final SkillHolder BLESS_PROTECTION = new SkillHolder(5182, 1); // Blessing of Protection
	private static final SkillHolder FANTASIA = new SkillHolder(4322, 1); // Легкая Походка Путешественника
	private static final SkillHolder[] GROUP_BUFFS =
	{
		new SkillHolder(4323, 1), // Щит Путешественника
		new SkillHolder(4324, 1), // Благословение Тела Путешественника
		new SkillHolder(4325, 1), // Гнев Вампира Путешественника
		new SkillHolder(4326, 1), // Регенерация Путешественника
		new SkillHolder(4327, 1), // Ускорение Путешественника
		new SkillHolder(4328, 1), // Благословение Души Путешественника
		new SkillHolder(4329, 1), // Проницательность Путешественника
		new SkillHolder(4330, 1), // Концентрация Путешественника
		new SkillHolder(4331, 1), // Воодушевление Путешественника
		new SkillHolder(4332, 1), // Ментальная Защита
	};
	private static final SkillHolder[] DONATE_BUFFS =
	{
		new SkillHolder(34243, 3), // Поэма Музыкантов - Путешественник
		new SkillHolder(32840, 1), // Гармония Фантазии - Путешественник
		new SkillHolder(34254, 1), // Исполнение Сонаты - Путешественник
	};
	// Misc
	private static final int MAX_LEVEL_BUFFS = 40;
	private static final int MIN_LEVEL_PROTECTION = 10;
	
	private AdventurersGuide()
	{
		addStartNpc(ADVENTURERS_GUIDE);
		addTalkId(ADVENTURERS_GUIDE);
		addFirstTalkId(ADVENTURERS_GUIDE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = null;
		switch (event)
		{
			case "guide-01.html":
			case "guide-02.html":
			case "guide-03.html":
			case "guide-04.html":
			case "guide-05.html":
			case "guide-06.html":
			case "guide-07.html":
			case "guide-08.html":
			{
				htmltext = event;
				break;
			}
			case "index":
			{
				htmltext = npc.getId() + ".html";
				break;
			}
			case "weakenBreath":
			{
				if (player.getShilensBreathDebuffLevel() < 3)
				{
					htmltext = "guide-noBreath.html";
					break;
				}
				player.setShilensBreathDebuffLevel(2);
				htmltext = "guide-cleanedBreath.html";
				break;
			}
			case "fantasia":
			{
				if (player.getLevel() > MAX_LEVEL_BUFFS)
				{
					return "guide-noBuffs.html";
				}
				for (SkillHolder holder : GROUP_BUFFS)
				{
					SkillCaster.triggerCast(npc, player, holder.getSkill());
				}
				htmltext = applyBuffs(npc, player, FANTASIA.getSkill());
				break;
			}
			case "fantasia_donate_adena":
			{
				if (getQuestItemsCount(player, ADENA) >= 3000000)
				{
					takeItems(player, ADENA, 3000000);
					for (SkillHolder holder : DONATE_BUFFS)
					{
						SkillCaster.triggerCast(npc, player, holder.getSkill());
					}
					htmltext = applyBuffs(npc, player, FANTASIA.getSkill());
				}
				else
				{
					htmltext = "guide-noItems.html";
				}
				break;
			}
			case "fantasia_donate_gemstones":
			{
				if (getQuestItemsCount(player, GEMSTONE_R) >= 5)
				{
					takeItems(player, GEMSTONE_R, 5);
					for (SkillHolder holder : DONATE_BUFFS)
					{
						SkillCaster.triggerCast(npc, player, holder.getSkill());
					}
					htmltext = applyBuffs(npc, player, FANTASIA.getSkill());
				}
				else
				{
					htmltext = "guide-noItems.html";
				}
				break;
			}
		}
		return htmltext;
	}
	
	private String applyBuffs(Npc npc, Player player, Skill skill)
	{
		for (SkillHolder holder : GROUP_BUFFS)
		{
			SkillCaster.triggerCast(npc, player, holder.getSkill());
		}
		SkillCaster.triggerCast(npc, player, skill);
		if ((player.getLevel() < MIN_LEVEL_PROTECTION) && (player.getLevel() <= 1))
		{
			SkillCaster.triggerCast(npc, player, BLESS_PROTECTION.getSkill());
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		new AdventurersGuide();
	}
}