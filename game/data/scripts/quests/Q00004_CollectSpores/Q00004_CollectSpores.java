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
package quests.Q00004_CollectSpores;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.NpcLogListHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.util.Util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Collect Spores (313)
 * @author ivantotov
 */
public class Q00004_CollectSpores extends Quest {
    private static final int QUEST_ID = 4;
    private static final int MAX_CHANCE = 100;
    private static final int MIN_CHANCE = 1;
    private static final String KILL_COUNT_VAR = "KillCount";
    // NPC
    private static final int PAPUMA = 30561;
    // Item
    private static final int SPORE_SAC = 1118;
    // Misc
    private static final int MIN_LEVEL = 6;
    private static final int REQUIRED_SAC_COUNT = 10;
    // Monster
    private static final int MOUNTAIN_FUNGUS = 20365;

    public Q00004_CollectSpores()
    {
        super(QUEST_ID);
        addStartNpc(PAPUMA);
        addTalkId(PAPUMA);
        addKillId(MOUNTAIN_FUNGUS);
        registerQuestItems(SPORE_SAC);
    }

    @Override
    public String onAdvEvent(String event, Npc npc, Player player)
    {
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        switch (qs.getState()) {
            case State.CREATED: {
                if (npc.getId() == PAPUMA && player.getLevel() >= MIN_LEVEL) {
                    htmltext = "00004-01.htm";
                } else {
                    htmltext = "00004-02.htm";
                }
                break;
            }
        }
        if(event.equalsIgnoreCase("00004-03.htm")){
            htmltext = event;
        } else if(event.equalsIgnoreCase("00004-04.htm")){
            htmltext = event;
        } else if (event.equalsIgnoreCase("00004-05.htm")) {
            qs.startQuest();
            qs.isCond(1);
            htmltext = event;
        }
        return htmltext;
    }

    @Override
    public String onKill(Npc npc, Player killer, boolean isSummon)
    {
        final QuestState qs = getQuestState(killer, false);
        if(qs == null) {
            return null;
        }
        Random rn = new Random();
        int randomNum;
        int killCount = 0;
        if (qs.isCond(1) && Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false))
        {
            randomNum = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
            if(randomNum > 40) {
                killCount = qs.getInt(KILL_COUNT_VAR) + 1;
                giveItems(killer, SPORE_SAC, 1);
                qs.set(KILL_COUNT_VAR, killCount);
                sendNpcLogList(killer);
            }
            if(killCount == REQUIRED_SAC_COUNT){
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
        switch (qs.getState()) {
            case State.CREATED: {
                if (npc.getId() == PAPUMA && player.getLevel() >= MIN_LEVEL) {
                    htmltext = "00004-01.htm";
                } else {
                    htmltext = "00004-02.htm";
                }
                break;
            }
            case State.STARTED: {
                switch (qs.getCond()) {
                    case 1: {
                        if (getQuestItemsCount(player, SPORE_SAC) < REQUIRED_SAC_COUNT)
                        {
                            htmltext = "00004-06.html";
                        }
                        break;
                    }
                    case 2:
                    {
                        if (getQuestItemsCount(player, SPORE_SAC) >= REQUIRED_SAC_COUNT)
                        {
                            giveAdena(player, 3500, true);
                            qs.exitQuest(true, true);
                            htmltext = "00004-07.html";
                        }
                        break;
                    }
                }
                break;
            }
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
                holder.add(new NpcLogListHolder(NpcStringId.SPORE_SACS.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
}