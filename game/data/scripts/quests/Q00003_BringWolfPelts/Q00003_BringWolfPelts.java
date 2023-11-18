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
package quests.Q00003_BringWolfPelts;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.NpcLogListHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;
import org.l2jmobius.gameserver.network.NpcStringId;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Bring Wolf Pelts (258)
 * @author xban1x
 */
public class Q00003_BringWolfPelts extends Quest
{
	private static final int QUEST_ID = 3;
	private static final String KILL_COUNT_VAR = "KillCount";
	// Npc
	private static final int TIKU = 30582;
	private static final int PAPURION = 30580;
	// Item
	private static final int WOLF_PELT = 702;
	// Monsters
	private static final int[] MONSTERS = new int[]
	{
		20475, // Волк Кхаши 4 лвл
		20477, // Лесной Волк Кхаши 6 лвл
	};
	// Rewards
	private static final Map<Integer, Integer> REWARDS = new HashMap<>();
	static
	{
		REWARDS.put(390, 1); // Cotton Shirt
		REWARDS.put(29, 6); // Leather Pants
		REWARDS.put(2222, 9); // Leather Shirt
		REWARDS.put(1119, 13); // Short Leather Gloves
		REWARDS.put(426, 16); // Tunic
	}
	// Misc
	private static final int MIN_LEVEL = 3;
	private static final int WOLF_PELT_COUNT = 40;
	
	public Q00003_BringWolfPelts()
	{
		super(QUEST_ID);
		addStartNpc(TIKU,PAPURION);
		addTalkId(TIKU,PAPURION);
		addKillId(MONSTERS);
		registerQuestItems(WOLF_PELT);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		switch (qs.getState()) {
			case State.CREATED: {
				if (npc.getId() == TIKU || npc.getId() == PAPURION && player.getLevel() >= MIN_LEVEL) {
					htmltext = "00003-02.htm";
				} else {
					htmltext = "00003-01.htm";
				}
				break;
			}
			case State.STARTED: {
				if(qs.isCond(1)){
					htmltext = "00003-04.html";
				}
				if(qs.isCond(2)){
					if (getQuestItemsCount(player, WOLF_PELT) >= WOLF_PELT_COUNT)
					{
						final int chance = getRandom(16);
						for (Map.Entry<Integer, Integer> reward : REWARDS.entrySet())
						{
							if (chance < reward.getValue())
							{
								giveItems(player, reward.getKey(), 1);
								break;
							}
						}
						qs.unset(KILL_COUNT_VAR);
						qs.exitQuest(true, true);
						htmltext = "00003-05.html";
					}
				}
				break;
			}
		}
		if (event.equalsIgnoreCase("00003-03.html"))
		{
			qs.startQuest();
			htmltext = event;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(Npc npc, Player killer, boolean isSummon)
	{
		final QuestState qs = getQuestState(killer, false);
		final int killCount = qs.getInt(KILL_COUNT_VAR) + 1;
		if (qs.isCond(1))
		{
			giveItems(killer, WOLF_PELT, 1);
			qs.set(KILL_COUNT_VAR, killCount);
			sendNpcLogList(killer);
			if (getQuestItemsCount(killer, WOLF_PELT) >= WOLF_PELT_COUNT)
			{
				qs.setCond(2, true);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		final QuestState qs = getQuestState(player, true);
		String htmltext = getNoQuestMsg(player);
		System.out.println(qs);
		if (qs == null)
		{
			return null;
		}
		return htmltext;
	}
	@Override
	public Set<NpcLogListHolder> getNpcLogList(Player player)
	{
		final QuestState qs = getQuestState(player, false);
		if (qs != null)
		{
			if (qs.isCond(1))
			{
				final Set<NpcLogListHolder> holder = new HashSet<>();
				holder.add(new NpcLogListHolder(NpcStringId.WOLF_SKINS.getId(), true, qs.getInt(KILL_COUNT_VAR)));
				return holder;
			}
		}
		return super.getNpcLogList(player);
	}
}
