package quests.Q00012_CleaningTheLake;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.NpcLogListHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.util.Util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Q00012_CleaningTheLake extends Quest {
    private static final int QUEST_ID = 12;
    private static final int UNDINA_NPC = 30413;
    private static final int BEED = 1656;
    private static final String KILL_COUNT_VAR = "KillCount";
    private static final int REQUEST_COUNT = 50;
    private static final int MIN_CHANCE = 1;
    private static final int MAX_CHANCE = 100;
    private static final int MIN_LEVEL = 15;
    private static final int MAX_LEVEL = 28;
    private static final int LIREIN = 20036;
    private static final int LIREIN_ELDER = 20044;
    private static final int UNDINA = 20110;
    private static final int UNDINA_ELDER = 20113;
    private static final int[] REWARDS = {
            890,
            847,
            910
    };
    public Q00012_CleaningTheLake(){
        super(QUEST_ID);
        addStartNpc(UNDINA_NPC);
        addKillId(LIREIN,LIREIN_ELDER,UNDINA,UNDINA_ELDER);
        registerQuestItems(BEED);
    }
    public String onAdvEvent(String event, Npc npc, Player player) {
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        if(qs.getInt(KILL_COUNT_VAR) < REQUEST_COUNT){
            htmltext = "00012-04.htm";
        }
        if(event.equalsIgnoreCase("00012-01.htm")){
            if (npc.getId() == UNDINA_NPC && player.getLevel() >= MIN_LEVEL && player.getLevel() <= MAX_LEVEL) {
                qs.startQuest();
                htmltext = "00012-02.htm";
            } else {
                htmltext = "00012-01.htm";
            }
        }
        Random rn = new Random();
        int randomNum;
        if (qs.isCond(2)) {
            randomNum = rn.nextInt(REWARDS.length  + 1);
            giveItems(player, REWARDS[randomNum], 1);
            qs.unset(KILL_COUNT_VAR);
            qs.exitQuest(true, true);
            htmltext = "00012-03.htm";
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon){
        final QuestState qs = getQuestState(killer, false);
        Random rn = new Random();
        int randomNum;
        int killCount = 0;
        if (qs.isCond(1) && Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false))
        {
            randomNum = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
            if(randomNum > 40) {
                killCount = qs.getInt(KILL_COUNT_VAR) + 1;
                giveItems(killer, BEED, 1);
                qs.set(KILL_COUNT_VAR, killCount);
                sendNpcLogList(killer);
            }
            if(killCount == REQUEST_COUNT){
                qs.setCond(2, true);
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
        return htmltext;
    }
    public Set<NpcLogListHolder> getNpcLogList(Player player){
        final QuestState qs = getQuestState(player, false);
        if (qs != null)
        {
            if (qs.isCond(1))
            {
                final Set<NpcLogListHolder> holder = new HashSet<>();
                holder.add(new NpcLogListHolder(NpcStringId.BEED.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
}
