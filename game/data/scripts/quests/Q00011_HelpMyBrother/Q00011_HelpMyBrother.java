package quests.Q00011_HelpMyBrother;

import org.l2jmobius.gameserver.model.quest.Quest;

public class Q00011_HelpMyBrother extends Quest {
    private static final int QUEST_ID = 11;
    private static final int KUNAI = 30559;
    private static final int CENTURION = 31036;
    private static final int[] MONSTERS = new int[]
            {
                    20015,
                    20425,
                    20516,
                    20368,
            };
    public Q00011_HelpMyBrother(){
        super(QUEST_ID);
        addStartNpc(KUNAI);
        addTalkId(CENTURION);
        addKillId(MONSTERS);
    }
}
