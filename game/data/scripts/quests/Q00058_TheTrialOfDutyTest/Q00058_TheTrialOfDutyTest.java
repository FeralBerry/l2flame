package quests.Q00058_TheTrialOfDutyTest;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;

public class Q00058_TheTrialOfDutyTest extends Quest {
    private static final int QUEST_ID = 58;
    private static final int[] NPC = {
            34505,
            32497
    };
    private static final int minLevel = 39;
    public Q00058_TheTrialOfDutyTest(){
        super(QUEST_ID);
        addStartNpc(NPC[0]);
        addTalkId(NPC);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(player.getLevel() < minLevel){
            return "00058-01.htm";
        }
        if(
                player.getActiveClass() == 4 ||
                player.getActiveClass() == 19 ||
                player.getActiveClass() == 32 ||
                player.getActiveClass() == 213

        ){
            if(event.equalsIgnoreCase("00058-01.htm")) {
                final QuestState qs = getQuestState(player,true);
                if(NPC[0] == npc.getId()){
                    if(qs.isCompleted()){
                        return getAlreadyCompletedMsg(player);
                    }
                    if (qs.isCreated()) {
                        qs.startQuest();
                        if (qs.isStarted()) {
                            qs.setCond(1);
                            htmltext = "00058-02.htm";
                        }
                    }
                }
                if(NPC[0] == npc.getId()){
                    htmltext = "00058-03.htm";
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
}
