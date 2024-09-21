package quests.Q00018_ASuspiciousDoctor;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.util.Util;

public class Q00018_ASuspiciousDoctor extends Quest {
    private static final int QUEST_ID = 18;
    private static final int ORPHEUS = 30971; // id NPC
    private static final int BOX = 30960; // id NPC
    private static final int minLevel = 27;
    private static final int[] MONSTERS = {
            21235, // Смотритель Могил 27
            21189, // Офицер смотритель 27
            21211, // Смотритель Подземелья 27
            21141 // Змей Катакомб 28
    };
    private static final int[][] REWARDS = {
            {57, 50000},
            {955, 1}, // Свиток: Модифицировать Оружие Ранга D
            {956, 1} // Свиток: Модифицировать Доспех Ранга D
    };
    private static final int FIRST_ITEM = 739;
    private static final int SECOND = 1364; // исправить
    private static final int KEY = 4324;
    private static final int GRINIS_LETTER = 693;
    private static final int FIRST_ITEM_COUNT = 150;
    private static final int SECOND_ITEM_COUNT = 70;
    public Q00018_ASuspiciousDoctor(){
        super(QUEST_ID);
        addStartNpc(ORPHEUS);
        addTalkId(ORPHEUS,BOX);
        addKillId(MONSTERS);
        registerQuestItems(FIRST_ITEM,GRINIS_LETTER,SECOND,KEY);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = null;
        final QuestState qs = getQuestState(player, true);
        if(player.getLevel() < minLevel){
            return "00018-02.htm";
        }
        if(qs.isCompleted()){
            return getAlreadyCompletedMsg(player);
        }
        if(event.equalsIgnoreCase("00018-01.htm")){
            qs.startQuest();
            qs.setCond(1);
            htmltext = event;
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player)
    {
        final QuestState qs = getQuestState(player, false);
        String htmltext = getNoQuestMsg(player);
        if(npc.getId() == BOX && qs.isCond(3)){
            qs.setCond(4);
            takeItems(player, KEY, 1);
            htmltext = "00018-05.htm";
        }
        if(npc.getId() == ORPHEUS && qs.isCond(4)){
            htmltext = "00018-06.htm";
            for (int[] reward : REWARDS) {
                giveItems(player, reward[0], reward[1]);
            }
            qs.exitQuest(false, true);
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon) {
        final QuestState qs = getQuestState(killer, false);
        if (qs == null) {
            return null;
        }
        int npcId = npc.getId();
        if (qs.isCond(1) && Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false))
        {
            if (
                    npcId == MONSTERS[0]
                            || npcId == MONSTERS[1]
                            || npcId == MONSTERS[2]
            ){
                giveItems(killer,FIRST_ITEM,1);
                if(getQuestItemsCount(killer,FIRST_ITEM) == FIRST_ITEM_COUNT){
                    takeItems(killer, FIRST_ITEM, FIRST_ITEM_COUNT);
                    showHtmlFile(killer,"00018-03.htm");
                    qs.setCond(2);
                    giveItems(killer,GRINIS_LETTER,1);
                }
            }
        }
        if (qs.isCond(2) && Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false))
        {
            if (
                    npcId == MONSTERS[3]
            ){
                giveItems(killer,SECOND,1);
                if(getQuestItemsCount(killer,SECOND) == SECOND_ITEM_COUNT){
                    takeItems(killer, SECOND, SECOND_ITEM_COUNT);
                    showHtmlFile(killer,"00018-04.htm");
                    qs.setCond(3);
                    giveItems(killer,KEY,1);
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}
