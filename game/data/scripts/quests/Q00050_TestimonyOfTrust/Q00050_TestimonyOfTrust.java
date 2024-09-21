package quests.Q00050_TestimonyOfTrust;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.enums.Race;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.util.Util;

public class Q00050_TestimonyOfTrust extends Quest {
    private static final int QUEST_ID = 50;
    private static final int[] NPC = {
            30629, // Рукал - старт
            30345, // Рамониэль - светлый эльф (Глудио)
            30378, // Эстелла - тёмный эльф (Глудин)
            31439, // Роген - гном (Глудин)
            30509 // Доки - орк (Дион)
    };
    private static final int RUKAL = 30629; // id NPC
    private static final int minLevel = 35;
    private static final int[] QUEST_ITEMS = {
            2735, // письмо эльфам
            2736, // письмо тёмным эльфам
            2738, // письмо оркам
            2737, // письмо гномов
            2747, // Семя Зелени
            2756, // Паразит Лота
            2753, // Медовая Роса
            2637 // Слеза Признания
    };
    private static final int[] MONSTERS = {
            20549, // Лорд гнолов
            18616, // Фирак
            20130, // Орк отшельник
            20079 // Муравей (проставить спавн) и всего муравейника
    };
    private static final long KILL_COUNT_ELF = 49;
    private static final long KILL_COUNT_DARK_ELF = 49;
    private static final long KILL_COUNT_ORK = 29;
    private static final long KILL_COUNT_DWARF = 39;
    private static final int[][] REWARDS = {
            {3140, 1}, // Знак Жизни
            {57, 120000}, // Адена
    };
    public Q00050_TestimonyOfTrust(){
        super(QUEST_ID);
        addStartNpc(RUKAL);
        addTalkId(NPC);
        addKillId(MONSTERS);
        registerQuestItems(QUEST_ITEMS);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(player.getRace() == Race.HUMAN){
            if(player.getLevel() < minLevel){
                return "00050-01.htm";
            }
            final QuestState qs = getQuestState(player,true);
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            if(event.equalsIgnoreCase("00050-01.htm")){
                if(qs.isCreated()){
                    qs.startQuest();
                    if(qs.isStarted()){
                        qs.setCond(1);
                        htmltext = "00050-02.htm";
                    }
                }
                if(qs.isStarted()){
                    if (qs.isCond(13)){
                        for (int[] reward : REWARDS) {
                            giveItems(player, reward[0], reward[1]);
                        }
                        htmltext = "00050-11.htm";
                        qs.exitQuest(false, true);
                    }
                    else {
                        htmltext = "00050-02.htm";
                    }
                }
            }
            if(event.equalsIgnoreCase("00050-03.htm")) {
                if (qs.isCond(1)) {
                    htmltext = "00050-03.htm";
                    qs.setCond(2);
                }if (qs.isCond(2)) {
                    htmltext = "00050-03.htm";
                }
                if (qs.isCond(3)) {
                    htmltext = "00050-04.htm";
                    takeItems(player,QUEST_ITEMS[4],KILL_COUNT_ELF + 1);
                    giveItems(player,QUEST_ITEMS[0],1);
                    qs.setCond(4);
                }
            }
            if(event.equalsIgnoreCase("00050-05.htm")) {
                if (qs.isCond(4)) {
                    htmltext = "00050-05.htm";
                    qs.setCond(5);
                }
                if (qs.isCond(5)) {
                    htmltext = "00050-05.htm";
                }
                if (qs.isCond(6)) {
                    htmltext = "00050-06.htm";
                    takeItems(player,QUEST_ITEMS[5],KILL_COUNT_DARK_ELF + 1);
                    giveItems(player,QUEST_ITEMS[1],1);
                    qs.setCond(7);
                }
            }
            if(event.equalsIgnoreCase("00050-07.htm")) {
                if (qs.isCond(7)) {
                    htmltext = "00050-07.htm";
                    qs.setCond(8);
                }
                if (qs.isCond(8)) {
                    htmltext = "00050-07.htm";
                }
                if (qs.isCond(9)) {
                    htmltext = "00050-08.htm";
                    takeItems(player,QUEST_ITEMS[6],KILL_COUNT_ORK + 1);
                    giveItems(player,QUEST_ITEMS[2],1);
                    qs.setCond(10);
                }
            }
            if(event.equalsIgnoreCase("00050-09.htm")) {
                if (qs.isCond(10)) {
                    htmltext = "00050-09.htm";
                    qs.setCond(11);
                }
                if (qs.isCond(11)) {
                    htmltext = "00050-09.htm";
                }
                if (qs.isCond(12)) {
                    htmltext = "00050-10.htm";
                    takeItems(player,QUEST_ITEMS[7],KILL_COUNT_DWARF + 1);
                    giveItems(player,QUEST_ITEMS[3],1);
                    qs.setCond(13);
                }
            }

        } else {
            htmltext = "00050-01.htm";
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player)
    {
        final QuestState qs = getQuestState(player, false);
        String htmltext = getNoQuestMsg(player);
        if(qs.isStarted() && npc.getId() == NPC[0]){
            htmltext = "00050-02.htm";
        }
        if (npc.getId() == NPC[1] && qs.isCond(1)) {
            htmltext = "00050-03.htm";
        }
        if (npc.getId() == NPC[2] && qs.isCond(4)) {
            htmltext = "00050-05.htm";
        }
        if (npc.getId() == NPC[3] && qs.isCond(7)) {
            htmltext = "00050-07.htm";
        }
        if (npc.getId() == NPC[4] && qs.isCond(10)) {
            htmltext = "00050-09.htm";
        }
        return htmltext;
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
                    giveItems(killer,QUEST_ITEMS[4],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[4]) > KILL_COUNT_ELF){
                        qs.setCond(3);
                    }
                }
            }
            if (qs.isCond(5)){
                if (npcId == MONSTERS[1]){
                    giveItems(killer,QUEST_ITEMS[5],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[5]) > KILL_COUNT_DARK_ELF){
                        qs.setCond(6);
                    }
                }
            }
            if (qs.isCond(8)){
                if (npcId == MONSTERS[2]){
                    giveItems(killer,QUEST_ITEMS[6],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[6]) > KILL_COUNT_ORK){
                        qs.setCond(9);
                    }
                }
            }
            if (qs.isCond(11)){
                if (npcId == MONSTERS[3]){
                    giveItems(killer,QUEST_ITEMS[7],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[7]) > KILL_COUNT_DWARF){
                        qs.setCond(12);
                    }
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}
