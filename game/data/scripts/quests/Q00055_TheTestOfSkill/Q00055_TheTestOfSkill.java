package quests.Q00055_TheTestOfSkill;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.enums.Race;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.util.Util;

public class Q00055_TheTestOfSkill extends Quest {
    private static final int QUEST_ID = 55;
    private static final int[] NPC = {
            30644, // Кеш (переделать тексты)
            30597, // Пиотур (переделать тексты) Орки
            33509, // Джена (переделать тексты) Руины страданий
            33302, // Ридонбек (переделать тексты) Крума
            33163 // Линкес (переделать тексты) Крума
    };
    private static final int minLevel = 35;
    private static final int[] QUEST_ITEMS = {
            2631
    };
    private static final int[] MONSTERS = {
            27200,
            27109,
            27112,
            27113
    };
    private static final int[][] REWARDS = {
            {2674, 1}, // Знак мудрости
            {57, 120000}, // Адена
    };
    private static final int KILL_COUNT_1 = 4;
    public Q00055_TheTestOfSkill(){
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
                return "00055-01.htm";
            }
            final QuestState qs = getQuestState(player,true);
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            if(event.equalsIgnoreCase("00055-01.htm")) {
                if(npc.getId() == NPC[0]){
                    if (qs.isCreated()) {
                        qs.startQuest();
                        if (qs.isStarted()) {
                            qs.setCond(1);
                            htmltext = "00055-02.htm";
                        }
                    }
                    if(getQuestItemsCount(player,QUEST_ITEMS[0]) >= KILL_COUNT_1){
                        if (qs.isCond(9)){
                            for (int[] reward : REWARDS) {
                                giveItems(player, reward[0], reward[1]);
                            }
                            qs.exitQuest(false, true);
                            htmltext = "00052-11.htm";
                        }
                    } else {
                        if(getQuestItemsCount(player,QUEST_ITEMS[0]) == 0){
                            qs.setCond(1);
                            htmltext = "00052-12.htm";
                        }
                        if(getQuestItemsCount(player,QUEST_ITEMS[0]) == 1){
                            qs.setCond(3);
                            htmltext = "00052-12.htm";
                        }
                        if(getQuestItemsCount(player,QUEST_ITEMS[0]) == 2){
                            qs.setCond(5);
                            htmltext = "00052-12.htm";
                        }
                        if(getQuestItemsCount(player,QUEST_ITEMS[0]) == 3){
                            qs.setCond(7);
                            htmltext = "00052-12.htm";
                        }
                    }
                }
                if(npc.getId() == NPC[1]){
                    if (qs.isCond(1)) {
                        if(getQuestItemsCount(player,QUEST_ITEMS[0]) == 0) {
                            htmltext = "00052-03.htm";
                            addSpawn(MONSTERS[0], player.getClientX(), player.getClientY(), player.getClientZ(), 0, true, 300000);
                        }
                    }
                    if (qs.isCond(2)) {
                        giveItems(player, QUEST_ITEMS[0], 1);
                        qs.setCond(3);
                        htmltext = "00052-04.htm";
                    }

                }
                if(npc.getId() == NPC[2]){
                    if (qs.isCond(3)){
                        htmltext = "00052-05.htm";
                        addSpawn(MONSTERS[1], player.getClientX(), player.getClientY(), player.getClientZ(), 0, true, 300000);
                    }
                    if (qs.isCond(4)){
                        giveItems(player,QUEST_ITEMS[0],1);
                        qs.setCond(5);
                        htmltext = "00052-06.htm";
                    }
                }
                if(npc.getId() == NPC[3]){
                    if (qs.isCond(5)){
                        htmltext = "00052-07.htm";
                        addSpawn(MONSTERS[2], player.getClientX(), player.getClientY(), player.getClientZ(), 0, true, 300000);
                    }
                    if (qs.isCond(6)){
                        giveItems(player,QUEST_ITEMS[0],1);
                        qs.setCond(7);
                        htmltext = "00052-08.htm";
                    }
                }
                if(npc.getId() == NPC[4]){
                    if (qs.isCond(7)){
                        htmltext = "00052-09.htm";
                        addSpawn(MONSTERS[3], player.getClientX(), player.getClientY(), player.getClientZ(), 0, true, 300000);
                    }
                    if (qs.isCond(8)){
                        giveItems(player,QUEST_ITEMS[0],1);
                        qs.setCond(9);
                        htmltext = "00052-10.htm";
                    }
                }
            }
        } else {
            htmltext = "00055-01.htm";
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
                    qs.setCond(2);
                }
            }
            if (qs.isCond(3)) {
                if (npcId == MONSTERS[1]){
                    qs.setCond(4);
                }
            }
            if (qs.isCond(5)) {
                if (npcId == MONSTERS[2]){
                    qs.setCond(6);
                }
            }
            if (qs.isCond(7)) {
                if (npcId == MONSTERS[3]){
                    qs.setCond(8);
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}

