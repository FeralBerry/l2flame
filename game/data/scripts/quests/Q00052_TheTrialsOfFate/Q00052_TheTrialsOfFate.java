package quests.Q00052_TheTrialsOfFate;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.enums.Race;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.util.Util;

public class Q00052_TheTrialsOfFate extends Quest {
    private static final int QUEST_ID = 52;
    private static final int[] NPC = {
            30654, // Сер Кайль Ночной Ястреб
            30407, // Мезелла
            30410, // Человек-ящер из Пустошей
            30313 // Аша Глудин
    };
    private static final int minLevel = 35;
    private static final int[] QUEST_ITEMS = {
            3181, // Кровь Тирана
            3178, // Кровь медузы
            2629, // Глаз наблюдателя
    };
    private static final int[] MONSTERS = {
            21015, // Главарь Авангарда Гнолов
            20158, // Медуза
            21106 // Проклятый смотритель
    };
    private static final int[][] REWARDS = {
            {3172, 1}, // Знак Судьбы
            {57, 120000}, // Адена
    };
    private static final int KILL_COUNT_1 = 29;
    private static final int KILL_COUNT_2 = 29;
    private static final int KILL_COUNT_3 = 49;
    public Q00052_TheTrialsOfFate(){
        super(QUEST_ID);
        addStartNpc(NPC[0]);
        addTalkId(NPC);
        addKillId(MONSTERS);
        registerQuestItems(QUEST_ITEMS);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(player.getRace() == Race.DARK_ELF){
            if(player.getLevel() < minLevel){
                return "00052-01.htm";
            }
            final QuestState qs = getQuestState(player,true);
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            if(event.equalsIgnoreCase("00052-01.htm")) {
                if(npc.getId() == NPC[0]){
                    if (qs.isCreated()) {
                        qs.startQuest();
                        if (qs.isStarted()) {
                            qs.setCond(1);
                            htmltext = "00052-02.htm";
                        }
                    }
                    if (qs.isCond(10)){
                        for (int[] reward : REWARDS) {
                            giveItems(player, reward[0], reward[1]);
                        }
                        qs.exitQuest(false, true);
                        htmltext = "00052-09.htm";
                    }
                }
                if(npc.getId() == NPC[1]){
                    if (qs.isCond(1)){
                        qs.setCond(2);
                        htmltext = "00052-03.htm";
                    }
                    if (qs.isCond(3)){
                        takeItems(player, QUEST_ITEMS[0], KILL_COUNT_1 + 1);
                        qs.setCond(4);
                        htmltext = "00052-04.htm";
                    }
                }
                if(npc.getId() == NPC[2]){
                    if (qs.isCond(4)){
                        qs.setCond(5);
                        htmltext = "00052-05.htm";
                    }
                    if (qs.isCond(6)){
                        takeItems(player, QUEST_ITEMS[1], KILL_COUNT_2 + 1);
                        qs.setCond(7);
                        htmltext = "00052-06.htm";
                    }
                }
                if(npc.getId() == NPC[3]){
                    if (qs.isCond(7)){
                        qs.setCond(8);
                        htmltext = "00052-07.htm";
                    }
                    if (qs.isCond(9)){
                        takeItems(player, QUEST_ITEMS[2], KILL_COUNT_3 + 1);
                        qs.setCond(10);
                        htmltext = "00052-08.htm";
                    }
                }
            }
        } else {
            htmltext = "00052-01.htm";
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
            if (qs.isCond(2)) {
                if (npcId == MONSTERS[0]){
                    giveItems(killer,QUEST_ITEMS[0],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[0]) > KILL_COUNT_1){
                        qs.setCond(3);
                    }
                }
            }
            if (qs.isCond(5)) {
                if (npcId == MONSTERS[1]){
                    giveItems(killer,QUEST_ITEMS[1],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[1]) > KILL_COUNT_2){
                        qs.setCond(6);
                    }
                }
            }
            if (qs.isCond(8)) {
                if (npcId == MONSTERS[2]){
                    giveItems(killer,QUEST_ITEMS[2],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[2]) > KILL_COUNT_3){
                        qs.setCond(9);
                    }
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}
