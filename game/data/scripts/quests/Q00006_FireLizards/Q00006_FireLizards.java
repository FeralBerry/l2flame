package quests.Q00006_FireLizards;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.NpcLogListHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.network.NpcStringId;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Q00006_FireLizards extends Quest {
    private static final int QUEST_ID = 6;
    private static final int SALAMANDRA_NPC = 30411;
    private static final int SALAMANDRA = 20109;
    private static final int ELDER_SALAMANDRA = 20112;
    private static final int ELITE_SALAMANDRA = 20114;
    private static final int ELDER_RED_SALAMANDRA = 20416;
    private static final String KILL_COUNT_VAR = "KillCount";
    private static final int MIN_LEVEL = 15;
    private static final int MAX_LEVEL = 28;
    private static final int REQUEST_COUNT = 50;
    private static final int MAX_CHANCE = 100;
    private static final int MIN_CHANCE = 1;
    private static final int[] REWARDS = {
            432,
            391,
            348,
            377,
            413,
            465,
            40,
            719,
            45,
    };
    public Q00006_FireLizards(){
        super(QUEST_ID);
        addStartNpc(SALAMANDRA_NPC);
        addKillId(SALAMANDRA,ELDER_SALAMANDRA,ELITE_SALAMANDRA,ELDER_RED_SALAMANDRA);
    }
    public String onAdvEvent(String event, Npc npc, Player player) {
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        if(qs.getInt(KILL_COUNT_VAR) < REQUEST_COUNT){
            htmltext = "00006-04.htm";
        }
        if(event.equalsIgnoreCase("00006-01.htm")){
            if (npc.getId() == SALAMANDRA_NPC && player.getLevel() >= MIN_LEVEL && player.getLevel() <= MAX_LEVEL) {
                qs.startQuest();
                htmltext = "00006-02.htm";
            } else {
                htmltext = "00006-01.htm";
            }
        }
        Random rn = new Random();
        int randomNum;
        if (qs.isCond(2)) {
            randomNum = rn.nextInt(REWARDS.length  + 1);
            giveItems(player, REWARDS[randomNum], 1);
            qs.unset(KILL_COUNT_VAR);
            qs.exitQuest(true, true);
            htmltext = "00006-03.htm";
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
                case SALAMANDRA:
                case ELDER_SALAMANDRA:
                case ELITE_SALAMANDRA:
                case ELDER_RED_SALAMANDRA: {
                    if (qs.isCond(1)) {
                        randomNum = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
                        if(randomNum > 50) {
                            killCount = qs.getInt(KILL_COUNT_VAR) + 1;
                            if (killCount < REQUEST_COUNT) {
                                qs.set(KILL_COUNT_VAR, killCount);
                                sendNpcLogList(killer);
                            } else {
                                qs.setCond(2, true);
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
        return htmltext;
    }
    public Set<NpcLogListHolder> getNpcLogList(Player player){
        final QuestState qs = getQuestState(player, false);
        if (qs != null)
        {
            if (qs.isCond(1))
            {
                final Set<NpcLogListHolder> holder = new HashSet<>();
                holder.add(new NpcLogListHolder(NpcStringId.FIRE_SALAMANDERS_BLOOD.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
}
