package quests.Q00057_TheChampionTest;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.util.Util;

public class Q00057_TheChampionTest extends Quest {
    private static final int QUEST_ID = 57;
    private static final int[] NPC = {
        34505
    };
    private static final int minLevel = 39;
    private static final int[] QUEST_ITEMS = {
            752, // амулет орка
            762, // Осколок Серой Кости
            1617, // Шкура медведя
    };
    private static final int[] MONSTERS = {
            20096, // орк
            20763, // лизард
            20932 // медведь хату
    };
    private static final int[][] REWARDS = {
            {3276, 1}, // Знак чемпиона // гладиатор, дестр, тир, копейщик, берс, физ ейтерия
            {57, 120000}, // Адена
    };
    private static final int KILL_COUNT_1 = 19;
    private static final int KILL_COUNT_2 = 49;
    private static final int KILL_COUNT_3 = 49;
    public Q00057_TheChampionTest(){
        super(QUEST_ID);
        addStartNpc(NPC[0]);
        addTalkId(NPC);
        addKillId(MONSTERS);
        registerQuestItems(QUEST_ITEMS);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(player.getLevel() < minLevel){
            return "00057-01.htm";
        }
        if(player.isMageClass()){
            return htmltext;
        }
        if(
                player.getActiveClass() == 1 ||
                player.getActiveClass() == 125 ||
                player.getActiveClass() == 45 ||
                player.getActiveClass() == 47 ||
                player.getActiveClass() == 184
        ){
            final QuestState qs = getQuestState(player,true);
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            htmltext = "00057-02.htm";
            if(event.equalsIgnoreCase("00057-01.htm")) {
                if(npc.getId() == NPC[0]){
                    if (qs.isCreated()) {
                        qs.startQuest();
                        if (qs.isStarted()) {
                            qs.setCond(1);
                            htmltext = "00057-02.htm";
                        }
                    }
                    if (qs.isCond(4)){
                        for (int[] reward : REWARDS) {
                            giveItems(player, reward[0], reward[1]);
                        }
                        qs.exitQuest(false, true);
                        htmltext = "00057-06.htm";
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
                        qs.setCond(2);
                        showHtmlFile(killer,"00057-03.htm");
                    }
                }
            }
            if (qs.isCond(2)) {
                if (npcId == MONSTERS[1]){
                    giveItems(killer,QUEST_ITEMS[1],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[1]) > KILL_COUNT_2){
                        qs.setCond(3);
                        showHtmlFile(killer,"00057-04.htm");
                    }
                }
            }
            if (qs.isCond(3)) {
                if (npcId == MONSTERS[2]){
                    giveItems(killer,QUEST_ITEMS[2],1);
                    if(getQuestItemsCount(killer,QUEST_ITEMS[2]) > KILL_COUNT_3){
                        qs.setCond(4);
                        showHtmlFile(killer,"00057-05.htm");
                    }
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}
