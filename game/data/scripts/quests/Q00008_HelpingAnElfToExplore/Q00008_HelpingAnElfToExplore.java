package quests.Q00008_HelpingAnElfToExplore;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.NpcLogListHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;
import org.l2jmobius.gameserver.network.NpcStringId;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Q00008_HelpingAnElfToExplore extends Quest {
    private static final int QUEST_ID = 8;
    private static final int ELF = 30157;
    private static final int SHADE_HORROR = 20033;
    private static final int CONTORTION_OF_LUNACY = 20041;
    private static final int SKELETON_LONGBOWMAN = 20542;
    private static final String KILL_COUNT_VAR = "KillCount";
    private static final int MIN_LEVEL = 15;
    private static final int MAX_LEVEL = 28;
    private static final int REQUEST_COUNT = 150;
    private static final int MAX_CHANCE = 100;
    private static final int MIN_CHANCE = 0;
    private static final int[] REWARDS = {
            123,
            101,
            156,
            166,
            167,
            168,
            178,
            220,
            221,
            258,
            274,
            291,
    };
    public Q00008_HelpingAnElfToExplore(){
        super(QUEST_ID);
        addStartNpc(ELF);
        addKillId(SHADE_HORROR,CONTORTION_OF_LUNACY,SKELETON_LONGBOWMAN);
    }
    public String onAdvEvent(String event, Npc npc, Player player) {
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        switch (qs.getState()) {
            case State.CREATED: {
                if (npc.getId() == ELF && player.getLevel() >= MIN_LEVEL && player.getLevel() <= MAX_LEVEL) {
                    qs.startQuest();
                    htmltext = "00008-02.htm";
                } else {
                    htmltext = "00008-01.htm";
                }
                break;
            }
        }
        if (event.equalsIgnoreCase("start"))
        {
            qs.startQuest();
            htmltext = null;
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon){
        final QuestState qs = getQuestState(killer, false);
        Random rn = new Random();
        int randomNum;
        int killCount;
        if (qs != null) {
            switch (npc.getId()) {
                case SHADE_HORROR:
                case CONTORTION_OF_LUNACY:
                case SKELETON_LONGBOWMAN: {
                    if (qs.isCond(2)) {
                        randomNum = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
                        if(randomNum > 70) {
                            killCount = qs.getInt(KILL_COUNT_VAR) + 1;
                            if (killCount < REQUEST_COUNT) {
                                qs.set(KILL_COUNT_VAR, killCount);
                                sendNpcLogList(killer);
                            } else {
                                qs.setCond(3, true);
                                qs.unset(KILL_COUNT_VAR);
                            }
                        }
                    }
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
    public String onTalk(Npc npc, Player player) {
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        if (qs == null)
        {
            return null;
        }
        Random rn = new Random();
        int randomNum;
        switch (qs.getState()) {
            case State.STARTED: {
                if (qs.isCond(3)) {
                    randomNum = rn.nextInt(REWARDS.length  + 1);
                    giveItems(player, REWARDS[randomNum], 1);
                    qs.unset(KILL_COUNT_VAR);
                    qs.exitQuest(true, true);
                    htmltext = "00008-03.htm";
                }
                break;
            }
        }
        return htmltext;
    }
    public Set<NpcLogListHolder> getNpcLogList(Player player){
        final QuestState qs = getQuestState(player, false);
        if (qs != null)
        {
            if (qs.isCond(1))
            {
                final Set<NpcLogListHolder> holder = new HashSet<>();
                holder.add(new NpcLogListHolder(NpcStringId.UNDEAD.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
}
