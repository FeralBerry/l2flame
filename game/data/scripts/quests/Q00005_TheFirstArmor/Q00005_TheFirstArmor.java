package quests.Q00005_TheFirstArmor;

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

public class Q00005_TheFirstArmor extends Quest {
    private static final int QUEST_ID = 5;
    private static final int MIN_LEVEL = 8;
    private static final String KILL_COUNT_VAR = "KillCount";
    private static final int MAX_CHANCE = 100;
    private static final int MIN_CHANCE = 1;
    private static final int SUMARI = 30564;
    private static final int IMP = 20311;
    private static final int ELDER_IMP = 20312;
    private static final int MARUKU = 20363;
    private static final int IMP_SHACKLES = 1368;
    private static final int REQUIRED_IMP_SHACKLES_COUNT = 100;
    private static final int[] MAGE_SET = {
            429,
            464,
            43
    };
    private static final int[] FIGHTER_SET = {
            23,
            2386,
            43
    };
    public Q00005_TheFirstArmor()
    {
        super(QUEST_ID);
        addStartNpc(SUMARI);
        addTalkId(SUMARI);
        addKillId(IMP,ELDER_IMP,MARUKU);
        registerQuestItems(IMP_SHACKLES);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player){
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        switch (qs.getState()) {
            case State.CREATED: {
                if (npc.getId() == SUMARI && player.getLevel() >= MIN_LEVEL) {
                    htmltext = "00005-01.htm";
                } else {
                    htmltext = "00005-02.htm";
                }
                break;
            }
        }
        if (event.equalsIgnoreCase("00005-03.htm")) {
            qs.startQuest();
            qs.isCond(1);
            htmltext = event;
        }
        return htmltext;
    }
    @Override
    public String onKill(Npc npc, Player killer, boolean isSummon){
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
                giveItems(killer, IMP_SHACKLES, 1);
                qs.set(KILL_COUNT_VAR, killCount);
                sendNpcLogList(killer);
            }
            if(killCount == REQUIRED_IMP_SHACKLES_COUNT){
                qs.setCond(2, true);
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
    @Override
    public String onTalk(Npc npc, Player player){
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        switch (qs.getState()) {
            case State.CREATED: {
                if (npc.getId() == SUMARI && player.getLevel() >= MIN_LEVEL) {
                    htmltext = "00005-01.htm";
                } else {
                    htmltext = "00005-02.htm";
                }
                break;
            }
            case State.STARTED: {
                switch (qs.getCond()) {
                    case 1: {
                        if (getQuestItemsCount(player, IMP_SHACKLES) < REQUIRED_IMP_SHACKLES_COUNT)
                        {
                            htmltext = "00005-04.htm";
                        }
                        break;
                    }
                    case 2:
                    {
                        if (getQuestItemsCount(player, IMP_SHACKLES) >= REQUIRED_IMP_SHACKLES_COUNT)
                        {
                            if (player.isMageClass())
                            {
                                for (int i = 0; i < MAGE_SET.length; i++){
                                    giveItems(player, MAGE_SET[i], 1);
                                }
                            }
                            else
                            {
                                for (int i = 0; i < FIGHTER_SET.length; i++){
                                    giveItems(player, FIGHTER_SET[i], 1);
                                }
                            }
                            qs.exitQuest(false, true);
                            htmltext = "00005-05.htm";
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
                holder.add(new NpcLogListHolder(NpcStringId.IMP_SHACKLES.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
}
