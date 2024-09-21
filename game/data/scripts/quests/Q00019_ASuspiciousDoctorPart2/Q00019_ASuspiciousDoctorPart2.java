package quests.Q00019_ASuspiciousDoctorPart2;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.util.Util;

public class Q00019_ASuspiciousDoctorPart2 extends Quest {
    private static final int QUEST_ID = 19;
    private static final int ORPHEUS = 30971; // id NPC
    private static final int minLevel = 29;
    private static final int[] MONSTERS = {
            18611, // дух воды
            20158, // медуза
            20173, // троль
            21191 // клерик гигант
    };
    private static final int[][] REWARDS = {
            {57, 50000},
            {955, 1}, // Свиток: Модифицировать Оружие Ранга D
            {956, 1} // Свиток: Модифицировать Доспех Ранга D
    };
    private static final int FIRST_ITEM_COUNT = 69;
    private static final int SECOND_ITEM_COUNT = 34;
    private static final int THIRD_ITEM_COUNT = 51;
    private static final int LAST_ITEM_COUNT = 75;
    private static final int MEDUZE_ACID = 1453; //яд медуз
    public Q00019_ASuspiciousDoctorPart2(){
        super(QUEST_ID);
        addStartNpc(ORPHEUS);
        addTalkId(ORPHEUS);
        addKillId(MONSTERS);
        registerQuestItems(MEDUZE_ACID);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(player.getQuestState("Q00018_ASuspiciousDoctor") != null && player.getQuestState("Q00018_ASuspiciousDoctor").isCompleted()){
            final QuestState qs = getQuestState(player,true);
            if(player.getLevel() < minLevel){
                return "00019-02.htm";
            }
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            qs.startQuest();
            qs.setCond(1);
            htmltext = "00019-03.htm";
        } else {
            htmltext = "00019-01.htm";
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player)
    {
        final QuestState qs = getQuestState(player, false);
        String htmltext = getNoQuestMsg(player);
        if(player.getQuestState("Q00018_ASuspiciousDoctor") != null && player.getQuestState("Q00018_ASuspiciousDoctor").isCompleted()) {
            if (npc.getId() == ORPHEUS && qs.isCond(5)) {
                htmltext = "00019-08.htm";
                for (int[] reward : REWARDS) {
                    giveItems(player, reward[0], reward[1]);
                }
                qs.exitQuest(false, true);
            }
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon) {
        final QuestState qs = getQuestState(killer, false);
        if (qs == null) {
            return null;
        }
        int npcId = npc.getId();
        if (Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false))
        {
            if(qs.isCond(1)){
                if (
                        npcId == MONSTERS[0]
                                || npcId == MONSTERS[1]
                                || npcId == MONSTERS[2]
                                || npcId == MONSTERS[3]
                ){

                    giveItems(killer,MEDUZE_ACID,1);
                    if(getQuestItemsCount(killer,MEDUZE_ACID) == FIRST_ITEM_COUNT){
                        takeItems(killer, MEDUZE_ACID, FIRST_ITEM_COUNT);
                        qs.setCond(2);
                        showHtmlFile(killer,"00019-04.htm");
                    }
                }
            }
            if(qs.isCond(2)){
                if (
                        npcId == MONSTERS[0]
                                || npcId == MONSTERS[1]
                                || npcId == MONSTERS[2]
                                || npcId == MONSTERS[3]
                ){
                    giveItems(killer,MEDUZE_ACID,1);
                    if(getQuestItemsCount(killer,MEDUZE_ACID) == SECOND_ITEM_COUNT){
                        takeItems(killer, MEDUZE_ACID, SECOND_ITEM_COUNT);
                        showHtmlFile(killer,"00019-05.htm");
                        qs.setCond(3);
                    }
                }
            }
            if(qs.isCond(3)){
                if (
                        npcId == MONSTERS[0]
                                || npcId == MONSTERS[1]
                                || npcId == MONSTERS[2]
                                || npcId == MONSTERS[3]
                ){
                    giveItems(killer,MEDUZE_ACID,1);
                    if(getQuestItemsCount(killer,MEDUZE_ACID) == THIRD_ITEM_COUNT){
                        takeItems(killer, MEDUZE_ACID, THIRD_ITEM_COUNT);
                        showHtmlFile(killer,"00019-06.htm");
                        qs.setCond(4);
                    }
                }
            }
            if(qs.isCond(4)){
                if (
                        npcId == MONSTERS[0]
                                || npcId == MONSTERS[1]
                                || npcId == MONSTERS[2]
                                || npcId == MONSTERS[3]
                ){
                    giveItems(killer,MEDUZE_ACID,1);
                    if(getQuestItemsCount(killer,MEDUZE_ACID) == LAST_ITEM_COUNT){
                        showHtmlFile(killer,"00019-07.htm");
                        qs.setCond(5);
                    }
                }
            }
            if(!qs.isCond(5)) {
                addSpawn(npcId, killer.getClientX(), killer.getClientY(), killer.getClientZ(), 0, true, 60000);
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}
