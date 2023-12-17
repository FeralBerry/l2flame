package quests.Q000010_TheFirstChanges;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.NpcLogListHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;
import org.l2jmobius.gameserver.network.NpcStringId;

import java.util.HashSet;
import java.util.Set;

public class Q00010_TheFirstChanges extends Quest {
    private static final int QUEST_ID = 10;
    private static final int HONEY_JAR = 1655;
    private static final int RED_SOIL = 1290;
    private static final int LEAF = 1235;
    private static final int KEKEY = 30565;
    private static final int TOMA = 30556;
    private static final int ROSELA = 30414;
    private static final int ANNIKA = 30418;
    private static final int LEVIAN = 30037;
    private static final int MIN_LEVEL = 18;
    private static final String KILL_COUNT_VAR = "KillCount";
    private static final int[] MONSTERS = new int[]
            {
                    20475, // Волк Кхаши 4 лвл
                    20477, // Лесной Волк Кхаши 6 лвл
            };

    public Q00010_TheFirstChanges(){
        super(QUEST_ID);
        addStartNpc(KEKEY);
        addTalkId(TOMA,ROSELA,ANNIKA,LEVIAN);
        addKillId(MONSTERS);
        //setQuestNameNpcStringId(NpcStringId.TRAINING_GETTING_TO_KNOW_THE_POSSIBILITIES);
        registerQuestItems(LEAF,RED_SOIL);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player)
    {
        final QuestState qs = getQuestState(player, true);
        String htmltext = null;
        switch (qs.getState()) {
            case State.CREATED: {
                if (npc.getId() == KEKEY && player.getLevel() >= MIN_LEVEL) {
                    htmltext = "00010-02.htm";
                } else {
                    htmltext = "00010-01.htm";
                }
                break;
            }
            case State.COMPLETED: {
                htmltext = getAlreadyCompletedMsg(player);
                break;
            }
        }
        if (event.equalsIgnoreCase("start")) {
            qs.startQuest();
            qs.setCond(1);
        }
        return htmltext;
    }

    @Override
    public String onKill(Npc npc, Player killer, boolean isSummon)
    {
        final QuestState qs = getQuestState(killer, false);
        if (qs == null)
        {
            return null;
        }
        final int killCount = qs.getInt(KILL_COUNT_VAR) + 1;
        if (qs.isCond(3))
        {
            giveItems(killer, RED_SOIL, 1);
            qs.set(KILL_COUNT_VAR, killCount);
            sendNpcLogList(killer);
            if (getQuestItemsCount(killer, RED_SOIL) >= 100)
            {
                qs.setCond(4, true);
            }
        }
        return super.onKill(npc, killer, isSummon);
    }

    @Override
    public String onTalk(Npc npc, Player player)
    {
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        if (qs == null)
        {
            return null;
        }
        switch (qs.getState()) {
            case State.STARTED: {
                switch (npc.getId()) {
                    case TOMA: {
                        if (qs.isCond(1)) {
                            qs.setCond(2);
                            htmltext = "00010-03.htm";
                        }
                        if (qs.isCond(6)) {
                            qs.setCond(7);
                            htmltext = "00010-07.htm";
                        }
                        break;
                    }
                    case ANNIKA: {
                        if (qs.isCond(2)) {
                            qs.setCond(3);
                            htmltext = "00010-04.htm";
                        }
                        if (qs.isCond(4)) {
                            qs.setCond(5);
                            htmltext = "00010-05.htm";
                        }
                        break;
                    }
                    case ROSELA: {
                        if (qs.isCond(5)) {
                            qs.setCond(6);
                            giveItems(player,LEAF,1);
                            htmltext = "00010-06.htm";
                        }
                        break;
                    }
                    case LEVIAN: {
                        if (qs.isCond(7)) {
                            giveItems(player,HONEY_JAR,1);
                            giveItems(player,57,130000);
                            htmltext = "00010-08.htm";
                        }
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
            if (qs.isCond(3))
            {
                final Set<NpcLogListHolder> holder = new HashSet<>();
                holder.add(new NpcLogListHolder(NpcStringId.RED_SOIL.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
}
