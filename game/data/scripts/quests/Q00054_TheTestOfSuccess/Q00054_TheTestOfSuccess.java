package quests.Q00054_TheTestOfSuccess;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.enums.Race;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.util.Util;

public class Q00054_TheTestOfSuccess extends Quest {
    private static final int QUEST_ID = 54;
    private static final int[] NPC = {
            30499, // Талой кузнец Глудин
            30676, // Начальник складе Круп Орен
            30511, // Гесто Начальник склада Гиран
            30897 // Роман кузнец Хейн
    };
    private static final int minLevel = 35;
    private static final int[] QUEST_ITEMS = {
            3246, // Лицензия Гильдии
            3256, // Взнос Крупа
            3266, // Темно-красный Мох
            3253, // Взнос Гесто
            3252, // Взнос Романа

            3273, // Панцирь Серого муравья
            3275, // Лапа муравья
            3274, // Ядовитая железа муравья
    };
    private static final int[] MONSTERS = {
            20202, // поедатель мертвечины Руины отчаяния
            20226, // Серый Муравей
            20079, // Муравей
		    20080, // Муравей Главарь
    };
    private static final int[][] REWARDS = {
            {3238, 1}, // Знак Успеха
            {57, 120000}, // Адена
    };
    private static final int KILL_COUNT_1 = 49;
    private static final int KILL_COUNT_2 = 20;
    private static final int KILL_COUNT_3 = 20;
    private static final int KILL_COUNT_4 = 20;
    public Q00054_TheTestOfSuccess(){
        super(QUEST_ID);
        addStartNpc(NPC[0]);
        addTalkId(NPC);
        addKillId(MONSTERS);
        registerQuestItems(QUEST_ITEMS);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(player.getRace() == Race.DWARF){
            if(player.getLevel() < minLevel){
                return "00054-01.htm";
            }
            final QuestState qs = getQuestState(player,true);
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            if(event.equalsIgnoreCase("00054-01.htm")) {
                if(npc.getId() == NPC[0]){
                    if (qs.isCreated()) {
                        qs.startQuest();
                        if (qs.isStarted()) {
                            giveItems(player,QUEST_ITEMS[0],1);
                            qs.setCond(1);
                            htmltext = "00054-02.htm";
                        }
                    }
                    if (qs.isCond(5)){
                        qs.setCond(6);
                        htmltext = "00052-06.htm";
                    }
                    if (qs.isCond(7)){
                        for (int[] reward : REWARDS) {
                            giveItems(player, reward[0], reward[1]);
                        }
                        qs.exitQuest(false, true);
                        htmltext = "00052-07.htm";
                    }
                }
                if(npc.getId() == NPC[1]){
                    if (qs.isCond(1)){
                        giveItems(player,QUEST_ITEMS[1],1);
                        qs.setCond(2);
                        htmltext = "00052-03.htm";
                    }
                }
                if(npc.getId() == NPC[2]){
                    if (qs.isCond(3)){
                        takeItems(player,QUEST_ITEMS[2],KILL_COUNT_1 +1);
                        giveItems(player,QUEST_ITEMS[3],1);
                        qs.setCond(4);
                        htmltext = "00052-04.htm";
                    }
                }
                if(npc.getId() == NPC[3]){
                    if (qs.isCond(4)){
                        giveItems(player,QUEST_ITEMS[4],1);
                        qs.setCond(5);
                        htmltext = "00052-05.htm";
                    }
                }
            }
        } else {
            htmltext = "00054-01.htm";
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
                    giveItems(killer,QUEST_ITEMS[2],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[2]) > KILL_COUNT_1){
                        qs.setCond(3);
                    }
                }
            }
            if (qs.isCond(6)) {
                if(getQuestItemsCount(killer,QUEST_ITEMS[5]) == KILL_COUNT_2
                        && getQuestItemsCount(killer,QUEST_ITEMS[6]) == KILL_COUNT_3
                        && getQuestItemsCount(killer,QUEST_ITEMS[7]) == KILL_COUNT_4){
                    qs.setCond(7);
                }
                if (npcId == MONSTERS[1]){
                    if(getQuestItemsCount(killer,QUEST_ITEMS[5]) < KILL_COUNT_2){
                        giveItems(killer,QUEST_ITEMS[5],1);
                    }
                }
                if (npcId == MONSTERS[2]){
                    if(getQuestItemsCount(killer,QUEST_ITEMS[6]) < KILL_COUNT_3){
                        giveItems(killer,QUEST_ITEMS[6],1);
                    }
                }
                if (npcId == MONSTERS[3]){
                    if(getQuestItemsCount(killer,QUEST_ITEMS[7]) < KILL_COUNT_4){
                        giveItems(killer,QUEST_ITEMS[7],1);
                    }
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}
