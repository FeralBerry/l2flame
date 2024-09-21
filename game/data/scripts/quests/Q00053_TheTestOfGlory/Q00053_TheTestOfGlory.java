package quests.Q00053_TheTestOfGlory;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.enums.Race;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.util.Util;

public class Q00053_TheTestOfGlory extends Quest {
    private static final int QUEST_ID = 53;
    private static final int[] NPC = {
        30502, // Провидец Умос Глудин
    };
    private static final int minLevel = 35;
    private static final int[] QUEST_ITEMS = {
            3218, // Голова Орка Тамлин (переделеать)
            3222, // Голова Вултуса
            3221, // Голова Пашики
            3224, // Голова Владыки Энку
    };
    private static final int[] MONSTERS = {
            20448, // охотничий
            20446, // охотничий
            27081, // квестовый Вултуса
            27080, // квестовый Пашики
            27082, // квестовый Владыки Энку
            20130
    };
    private static final int[][] REWARDS = {
            {3203, 1}, // Знак Чести
            {57, 120000}, // Адена
    };
    private static final int KILL_COUNT_1 = 99;
    private static final int KILL_COUNT_2 = 9;
    public Q00053_TheTestOfGlory(){
        super(QUEST_ID);
        addStartNpc(NPC[0]);
        addTalkId(NPC);
        addKillId(MONSTERS);
        registerQuestItems(QUEST_ITEMS);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(player.getRace() == Race.ORC){
            if(player.getLevel() < minLevel){
                return "00053-01.htm";
            }
            final QuestState qs = getQuestState(player,true);
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            if(event.equalsIgnoreCase("00053-01.htm")) {
                if(npc.getId() == NPC[0]){
                    if (qs.isCreated()) {
                        qs.startQuest();
                        if (qs.isStarted()) {
                            qs.setCond(1);
                            htmltext = "00053-02.htm";
                        }
                    }
                    if(qs.isCond(3)){
                        qs.setCond(4);
                        htmltext = "00053-04.htm";
                    }
                    if(qs.isCond(5)){
                        for (int[] reward : REWARDS) {
                            giveItems(player, reward[0], reward[1]);
                        }
                        qs.exitQuest(false, true);
                        htmltext = "00053-05.htm";
                    }
                }
            }
        } else {
            htmltext = "00053-01.htm";
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
                if (npcId == MONSTERS[0] || npcId == MONSTERS[1] || npcId == MONSTERS[5]){
                    giveItems(killer,QUEST_ITEMS[0],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[0]) > KILL_COUNT_1){
                        addSpawn(MONSTERS[2], killer.getClientX(), killer.getClientY(), killer.getClientZ(), 0, true, 300000);
                        takeItems(killer, QUEST_ITEMS[0], KILL_COUNT_1 - 89);
                    }
                }
                if (npcId == MONSTERS[2]){
                    takeItems(killer, QUEST_ITEMS[0], KILL_COUNT_1 + 1);
                    giveItems(killer,QUEST_ITEMS[1],1);
                    qs.setCond(2);
                    showHtmlFile(killer,"00052-03.htm");
                }
            }
            if (qs.isCond(2)) {
                if (npcId == MONSTERS[0] || npcId == MONSTERS[1] || npcId == MONSTERS[5]){
                    giveItems(killer,QUEST_ITEMS[0],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[0]) > KILL_COUNT_1){
                        addSpawn(MONSTERS[3], killer.getClientX(), killer.getClientY(), killer.getClientZ(), 0, true, 300000);
                        takeItems(killer, QUEST_ITEMS[0], KILL_COUNT_1 - 89);
                    }
                }
                if (npcId == MONSTERS[3]){
                    giveItems(killer,QUEST_ITEMS[2],1);
                    qs.setCond(3);
                }
            }
            if (qs.isCond(4)) {
                if (npcId == MONSTERS[0] || npcId == MONSTERS[1] || npcId == MONSTERS[5]){
                    giveItems(killer,QUEST_ITEMS[0],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[0]) > KILL_COUNT_2){
                        addSpawn(MONSTERS[4], killer.getClientX(), killer.getClientY(), killer.getClientZ(), 0, true, 300000);
                        takeItems(killer, QUEST_ITEMS[0], KILL_COUNT_2);
                    }
                }
                if (npcId == MONSTERS[4]){
                    giveItems(killer,QUEST_ITEMS[3],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[3]) > KILL_COUNT_2){
                        qs.setCond(5);
                    }
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}
