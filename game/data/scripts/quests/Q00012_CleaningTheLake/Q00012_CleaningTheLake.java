package quests.Q00012_CleaningTheLake;

import org.l2jmobius.gameserver.model.quest.Quest;

public class Q00012_CleaningTheLake extends Quest {
    private static final int QUEST_ID = 12;
    private static final int UNDINA_NPC = 30413;
    private static final int BEED = 1656;
    private static final int[] MONSTERS = new int[]
            {
                    20036,
                    20113,
                    20110,
                    20044,
            };
    public Q00012_CleaningTheLake(){
        super(QUEST_ID);
        addStartNpc(UNDINA_NPC);
        addKillId(MONSTERS);
    }
}
