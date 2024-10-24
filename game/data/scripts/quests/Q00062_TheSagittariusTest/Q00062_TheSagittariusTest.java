package quests.Q00062_TheSagittariusTest;

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

public class Q00062_TheSagittariusTest extends Quest {
    private static final int QUEST_ID = 62;
    private static final int[] NPC = {
            34505,
            30617,
    };
    private static final int minLevel = 39;
    private static final int[] QUEST_ITEMS = {
        8828
    };
    private static final int[] MONSTERS = {
            20500,
            20499,
            20765,
            20580
    };
    private static final int[][] REWARDS = {
            {3293, 1}, // Знак стрелка // хавк, Сыр, ФР, арба
            {57, 120000}, // Адена
    };
    private static final int KILL_COUNT_1 = 49;
    private static final String KILL_COUNT_VAR = "kill_count";
    public Q00062_TheSagittariusTest(){
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
                player.getActiveClass() == 7 ||
                        player.getActiveClass() == 22 ||
                        player.getActiveClass() == 35 ||
                        player.getActiveClass() == 126
        ){
            if(player.getLevel() < minLevel){
                return "00062-01.htm";
            }
            final QuestState qs = getQuestState(player,true);
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            if(npc.getId() == NPC[0]) {
                if (qs.isCreated()) {
                    qs.startQuest();
                    if (qs.isStarted()) {
                        qs.setCond(1);
                        giveItems(player,QUEST_ITEMS[0],1);
                        giveItems(player,1341,1000);
                        htmltext = "00062-02.htm";
                    }
                }
            }
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player)
    {
        final QuestState qs = getQuestState(player, false);
        String htmltext = getNoQuestMsg(player);
        if(npc.getId() == NPC[1] && qs.isCond(2)){
            htmltext = "00062-04.htm";
            qs.setCond(3);
        }
        if(npc.getId() == NPC[1] && qs.isCond(4)){
            for (int[] reward : REWARDS) {
                giveItems(player, reward[0], reward[1]);
            }
            qs.exitQuest(false, true);
            htmltext = "00062-05.htm";
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon) {
        final QuestState qs = getQuestState(killer, false);
        if (qs == null) {
            return null;
        }
        final int killCount = qs.getInt(KILL_COUNT_VAR) + 1;
        if(Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false)){
            if(killer.getActiveWeaponItem().getId() == QUEST_ITEMS[0]){
                if(qs.isCond(1) && (npc.getId() == MONSTERS[0] || npc.getId() == MONSTERS[1])) {
                    if(killCount < KILL_COUNT_1){
                        qs.set(KILL_COUNT_VAR,killCount);
                    } else {
                        qs.setCond(2);
                        qs.set(KILL_COUNT_VAR,0);
                        showHtmlFile(killer,"00062-03.htm");
                    }
                }
                if(qs.isCond(3) && (npc.getId() == MONSTERS[2] || npc.getId() == MONSTERS[3])) {
                    if(killCount < KILL_COUNT_1){
                        qs.set(KILL_COUNT_VAR,killCount);
                    } else {
                        qs.setCond(4);
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
            if (qs.isCond(3))
            {
                final Set<NpcLogListHolder> holder = new HashSet<>();
                holder.add(new NpcLogListHolder(NpcStringId.BLOOD_OF_KHIGHT_LIZARDMAN.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
}
