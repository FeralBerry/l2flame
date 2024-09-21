package quests.Q00051_TheTrialsOfLife;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.enums.Race;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.util.Util;

import java.util.Random;

public class Q00051_TheTrialsOfLife extends Quest {
    private static final int QUEST_ID = 51;
    private static final int[] NPC = {
            30327, // Мастер Сириус
            30417, // Сэр Клаус Васпер
            30314, // Ювелир Нестле
            30729, // Пьяница Борис
    };
    private static final int minLevel = 35;
    private static final int MAX_CHANCE = 100;
    private static final int MIN_CHANCE = 1;
    private static final int[] QUEST_ITEMS = {
            2635, // Слезы Рыцаря
            2638, // Обрывок Отчета
            2636, // Зеркало Души
            2642 // Урна с Прахом
    };
    private static final int[] MONSTERS = {
            20254, // Скелет с Секирой
            20615, // Призрачный Мечник

            20227, // Туманный Потрошитель
            20198, // Вурдалак Берсерк

            18609, // Папион (Дух воды)

            22010, // Призрак Генерала
            21213, // Посвященный Монах
		    20201 // Вурдалак
    };
    private static final int[][] REWARDS = {
            {3140, 1}, // Знак Жизни
            {57, 120000}, // Адена
    };
    private static final int KILL_COUNT_1 = 19;
    private static final int KILL_COUNT_2 = 19;
    private static final int KILL_COUNT_3 = 19;
    private static final int KILL_COUNT_4 = 29;

    public Q00051_TheTrialsOfLife(){
        super(QUEST_ID);
        addStartNpc(NPC[0]);
        addTalkId(NPC);
        addKillId(MONSTERS);
        registerQuestItems(QUEST_ITEMS);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(player.getRace() == Race.ELF){
            if(player.getLevel() < minLevel){
                return "00051-01.htm";
            }
            final QuestState qs = getQuestState(player,true);
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            if(event.equalsIgnoreCase("00051-01.htm")) {
                if(npc.getId() == NPC[0]){
                    if (qs.isCreated()) {
                        qs.startQuest();
                        if (qs.isStarted()) {
                            qs.setCond(1);
                            htmltext = "00051-02.htm";
                        }
                    }
                    if (qs.isCond(2)) {
                        takeItems(player, QUEST_ITEMS[0], KILL_COUNT_1 + 1);
                        qs.setCond(3);
                        htmltext = "00051-03.htm";
                    }
                }
                if(npc.getId() == NPC[1]){
                    if (qs.isCond(3)) {
                        qs.setCond(4);
                        htmltext = "00051-04.htm";
                    }
                    if (qs.isCond(5)) {
                        takeItems(player, QUEST_ITEMS[1], KILL_COUNT_2 + 1);
                        qs.setCond(6);
                        htmltext = "00051-05.htm";
                    }
                }
                if(npc.getId() == NPC[2]){
                    if (qs.isCond(6)) {
                        qs.setCond(7);
                        htmltext = "00051-06.htm";
                    }
                    if (qs.isCond(8)) {
                        takeItems(player, QUEST_ITEMS[2], KILL_COUNT_3 + 1);
                        qs.setCond(9);
                        htmltext = "00051-07.htm";
                    }
                }
                if(npc.getId() == NPC[3]){
                    if (qs.isCond(9)) {
                        qs.setCond(10);
                        htmltext = "00051-08.htm";
                    }
                    if (qs.isCond(11)) {
                        takeItems(player, QUEST_ITEMS[3], KILL_COUNT_4 + 1);
                        for (int[] reward : REWARDS) {
                            giveItems(player, reward[0], reward[1]);
                        }
                        qs.exitQuest(false, true);
                        htmltext = "00051-09.htm";
                    }
                }
            }
        } else {
            htmltext = "00051-01.htm";
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
        Random rn = new Random();
        int randomNum;
        if (Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false)) {
            if (qs.isCond(1)) {
                if (npcId == MONSTERS[0] || npcId == MONSTERS[1]){
                    randomNum = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
                    if(randomNum > 60) {
                        giveItems(killer, QUEST_ITEMS[0], 1);
                    }
                    if(getQuestItemsCount(killer,QUEST_ITEMS[0]) > KILL_COUNT_1){
                        qs.setCond(2);
                    }
                }
            }
            if (qs.isCond(4)) {
                if (npcId == MONSTERS[2] || npcId == MONSTERS[3]){
                    randomNum = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
                    if(randomNum > 60) {
                        giveItems(killer, QUEST_ITEMS[1], 1);
                    }
                    if(getQuestItemsCount(killer,QUEST_ITEMS[1]) > KILL_COUNT_2){
                        qs.setCond(5);
                    }
                }
            }
            if (qs.isCond(7)) {
                if (npcId == MONSTERS[4]){
                    randomNum = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
                    if(randomNum > 60) {
                        giveItems(killer, QUEST_ITEMS[2], 1);
                    }
                    if(getQuestItemsCount(killer,QUEST_ITEMS[2]) > KILL_COUNT_3){
                        qs.setCond(8);
                    }
                }
            }
            if (qs.isCond(10)) {
                if (npcId == MONSTERS[5] || npcId == MONSTERS[6] || npcId == MONSTERS[7]){
                    randomNum = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
                    if(randomNum > 60) {
                        giveItems(killer, QUEST_ITEMS[3], 1);
                    }
                    if(getQuestItemsCount(killer,QUEST_ITEMS[3]) > KILL_COUNT_4){
                        qs.setCond(11);
                    }
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}
