package quests.Q00056_TheTestOfWisdom;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.enums.Race;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.util.Util;

public class Q00056_TheTestOfWisdom extends Quest {
    private static final int QUEST_ID = 56;
    private static final int[] NPC = {

    };
    private static final int minLevel = 35;
    private static final int[] QUEST_ITEMS = {

    };
    private static final int[] MONSTERS = {

    };
    private static final int[][] REWARDS = {
            {2674, 1}, // Знак мудрости
            {57, 120000}, // Адена
    };
    private static final int KILL_COUNT_1 = 49;
    private static final int KILL_COUNT_2 = 29;
    private static final int KILL_COUNT_3 = 29;
    public Q00056_TheTestOfWisdom(){
        super(QUEST_ID);
        addStartNpc(NPC[0]);
        addTalkId(NPC);
        addKillId(MONSTERS);
        registerQuestItems(QUEST_ITEMS);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(player.getRace() == Race.KAMAEL){
            if(player.getLevel() < minLevel){
                return "00056-01.htm";
            }
            final QuestState qs = getQuestState(player,true);
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            if(event.equalsIgnoreCase("00056-01.htm")) {
                if(npc.getId() == NPC[0]){
                    if (qs.isCreated()) {
                        qs.startQuest();
                        if (qs.isStarted()) {
                            qs.setCond(1);
                            htmltext = "00056-02.htm";
                        }
                    }
                    /*if (qs.isCond(10)){
                        for (int[] reward : REWARDS) {
                            giveItems(player, reward[0], reward[1]);
                        }
                        qs.exitQuest(false, true);
                        htmltext = "00052-10.htm";
                    }*/
                }
                /*if(npc.getId() == NPC[1]){
                    if (qs.isCond(1)){
                        qs.setCond(2);
                        htmltext = "00052-03.htm";
                    }
                    if (qs.isCond(3)){
                        takeItems(player, QUEST_ITEMS[0], KILL_COUNT_1 + 1);
                        qs.setCond(4);
                        htmltext = "00052-05.htm";
                    }
                }*/
            }
        } else {
            htmltext = "00056-01.htm";
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player)
    {
        return getNoQuestMsg(player);
    }
    public String onKill(Npc npc, Player killer, boolean isSummon) {
        final QuestState qs = getQuestState(killer, false);
        if (qs == null) {
            return null;
        }
        int npcId = npc.getId();
        if (Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false)) {
            /*if (qs.isCond(2)) {
                if (npcId == MONSTERS[0]){
                    giveItems(killer,QUEST_ITEMS[0],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[0]) > KILL_COUNT_1){
                        qs.setCond(3);
                        showHtmlFile(killer,"00052-04.htm");
                    }
                }
            }*/
        }
        return super.onKill(npc, killer, isSummon);
    }
}
