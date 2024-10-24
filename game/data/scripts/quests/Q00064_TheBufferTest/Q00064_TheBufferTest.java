package quests.Q00064_TheBufferTest;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.NpcLogListHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.util.Util;

import java.util.HashSet;
import java.util.Set;

public class Q00064_TheBufferTest extends Quest {
    private static final int QUEST_ID = 64;
    private static final int[] NPC = {
            34505,
            30608 // переделать текст
    };
    private static final int minLevel = 39;
    private static final int[] QUEST_ITEMS = {

    };
    private static final int[] MONSTERS = {
        20989,
        20990,
        20494
    };
    private static final int[][] REWARDS = {
            {2821, 1}, // Знак помощника // ПП, БД, СВС, крафт, варк
            {57, 120000}, // Адена
    };
    private static final int KILL_COUNT_1 = 99;
    private static final int KILL_COUNT_2 = 24;
    private static final String KILL_COUNT_VAR = "kill_count";
    public Q00064_TheBufferTest(){
        super(QUEST_ID);
        addStartNpc(NPC[0]);
        addTalkId(NPC);
        addKillId(MONSTERS);
        registerQuestItems(QUEST_ITEMS);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(
                player.getActiveClass() == 15 ||
                        player.getActiveClass() == 19 ||
                        player.getActiveClass() == 32 ||
                        player.getActiveClass() == 50 ||
                        player.getActiveClass() == 56
        ){
            if(player.getLevel() < minLevel){
                return "00064-01.htm";
            }
            final QuestState qs = getQuestState(player,true);
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            if(event.equalsIgnoreCase("00064-01.htm")) {
                if (npc.getId() == NPC[0]) {
                    if (qs.isCreated()) {
                        qs.startQuest();
                        if (qs.isStarted()) {
                            qs.setCond(1);
                            htmltext = "00064-02.htm";
                        }
                    }
                }
            }
        } else {
            htmltext = "00064-01.htm";
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player)
    {
        final QuestState qs = getQuestState(player, false);
        String htmltext = getNoQuestMsg(player);
        if(qs.isCond(1) && npc.getId() == NPC[1]){
            htmltext = "00064-03.htm";
            qs.setCond(2);
        }
        if(qs.isCond(3) && npc.getId() == NPC[1]){
            htmltext = "00064-04.htm";
            qs.setCond(4);
        }
        if(qs.isCond(5) && npc.getId() == NPC[1]){
            htmltext = "00064-05.htm";
            for (int[] reward : REWARDS) {
                giveItems(player, reward[0], reward[1]);
            }
            qs.exitQuest(false, true);
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon) {
        final QuestState qs = getQuestState(killer, false);
        if (qs == null) {
            return null;
        }
        final int killCount = qs.getInt(KILL_COUNT_VAR) + 1;
        if (Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false)) {
            if (qs.isCond(2)) {
                if (npc.getId() == MONSTERS[0] || npc.getId() == MONSTERS[1]){
                    if(killCount < KILL_COUNT_1){
                        qs.set(KILL_COUNT_VAR,killCount);
                    } else {
                        qs.set(KILL_COUNT_VAR,0);
                        qs.setCond(3);
                    }
                }
            }
            if (qs.isCond(4)) {
                if (npc.getId() == MONSTERS[2]){
                    if(killCount < KILL_COUNT_2){
                        qs.set(KILL_COUNT_VAR,killCount);
                    } else {
                        qs.set(KILL_COUNT_VAR,0);
                        qs.setCond(5);
                    }
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
    @Override
    public Set<NpcLogListHolder> getNpcLogList(Player player)
    {
        final QuestState qs = getQuestState(player, false);
        if (qs != null)
        {
            if (qs.isCond(2))
            {
                final Set<NpcLogListHolder> holder = new HashSet<>();
                holder.add(new NpcLogListHolder(NpcStringId.SLIME_OF_SWAMP_FLIES.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
            if (qs.isCond(4))
            {
                final Set<NpcLogListHolder> holder = new HashSet<>();
                holder.add(new NpcLogListHolder(NpcStringId.WOLF_SCHOOLS.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
}
