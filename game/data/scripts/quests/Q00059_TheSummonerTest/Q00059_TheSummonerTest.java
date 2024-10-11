package quests.Q00059_TheSummonerTest;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.util.Util;

public class Q00059_TheSummonerTest extends Quest {
    private static final int QUEST_ID = 59;
    private static final int[] NPC = {
        34505,
        30593
    };
    private static final int minLevel = 39;
    private static final int[] QUEST_ITEMS = {
            3342, // Камень Крови
            3121, // Ягода Мандрагоры
            3130, // Серый Порошок
            3205 // Осколок Манашен
    };
    private static final int[] MONSTERS = {
            20552, // Пленная Душа
            27103, // Единорог квестовый
            27104, // Сумеречный Турен квестовый
            27105 // кот Мими квестовый
    };
    private static final int[][] REWARDS = {
            {3336, 1}, // Знак призывателя // Некр, конь, кот, тень
            {57, 120000}, // Адена
    };
    private static final int KILL_COUNT_1 = 29;
    public Q00059_TheSummonerTest(){
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
                player.getActiveClass() == 11 ||
                        player.getActiveClass() == 26 ||
                        player.getActiveClass() == 39
        ){
            final QuestState qs = getQuestState(player,true);
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            if(player.getLevel() < minLevel){
                return "00059-01.htm";
            } else {
                if(npc.getId() == NPC[0]) {
                    if (qs.isCreated()) {
                        qs.startQuest();
                        if (qs.isStarted()) {
                            giveItems(player,QUEST_ITEMS[1],1);
                            giveItems(player,QUEST_ITEMS[2],1);
                            giveItems(player,QUEST_ITEMS[3],1);
                            qs.setCond(1);
                            htmltext = "00059-02.htm";
                        }
                    }
                }
                if(npc.getId() == NPC[1]){
                    if (qs.isCond(4)){
                        for (int[] reward : REWARDS) {
                            giveItems(player, reward[0], reward[1]);
                        }
                        qs.exitQuest(false, true);
                        htmltext = "00059-03.htm";
                    }
                }
            }
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
            if (qs.isCond(1)) {
                if (npcId == MONSTERS[0]){
                    giveItems(killer,QUEST_ITEMS[0],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[0]) > KILL_COUNT_1){
                        takeItems(killer,QUEST_ITEMS[0],KILL_COUNT_1 - 10);
                        addSpawn(MONSTERS[1], killer.getClientX(), killer.getClientY(), killer.getClientZ(), 0, true, 600000);
                    }
                }
                if (npcId == MONSTERS[1]){
                    takeItems(killer,QUEST_ITEMS[0],KILL_COUNT_1);
                    takeItems(killer,QUEST_ITEMS[1],1);
                    qs.setCond(2);
                }
            }
            if (qs.isCond(2)) {
                if (npcId == MONSTERS[0]){
                    giveItems(killer,QUEST_ITEMS[0],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[0]) > KILL_COUNT_1){
                        takeItems(killer,QUEST_ITEMS[0],KILL_COUNT_1 - 10);
                        addSpawn(MONSTERS[2], killer.getClientX(), killer.getClientY(), killer.getClientZ(), 0, true, 600000);
                    }
                }
                if (npcId == MONSTERS[2]){
                    takeItems(killer,QUEST_ITEMS[0],KILL_COUNT_1);
                    takeItems(killer,QUEST_ITEMS[2],1);
                    qs.setCond(3);
                }
            }
            if (qs.isCond(3)) {
                if (npcId == MONSTERS[0]){
                    giveItems(killer,QUEST_ITEMS[0],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[0]) > KILL_COUNT_1){
                        takeItems(killer,QUEST_ITEMS[0],KILL_COUNT_1 - 10);
                        addSpawn(MONSTERS[3], killer.getClientX(), killer.getClientY(), killer.getClientZ(), 0, true, 600000);
                    }
                }
                if (npcId == MONSTERS[3]){
                    takeItems(killer,QUEST_ITEMS[0],KILL_COUNT_1);
                    takeItems(killer,QUEST_ITEMS[3],1);
                    qs.setCond(4);
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}
