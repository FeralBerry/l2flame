package quests.Q00014_HelpingLeopoldWithLizards;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;
import org.l2jmobius.gameserver.util.Util;

import java.util.Random;

public class Q00014_HelpingLeopoldWithLizards extends Quest {
    private static final int QUEST_ID = 14;
    private static final int LEPOLD = 30435; // id NPC
    private static final int THE_HEART_OF_STONE = 813;
    private static final int DOLL = 1034;
    private static final int A_BAG_OF_POISON = 1077;
    private static final int FELIM_LIZARDMEN = 20008;
    private static final int FELIM_LIZARDMEN_SCOUT = 20010;
    private static final int FELIM_LIZARDMEN_WARRIOR = 20014;
    private static final int LANGK_LIZARDMEN = 20030;
    private static final int LANGK_LIZARDMEN_SCOUT = 20027;
    private static final int LANGK_LIZARDMEN_WARRIOR = 20024;
    private static final int VIRUD_LIZARDMEN = 20293;
    private static final int VIRUD_LIZARDMEN_WARRIOR = 20295;
    private static final int VIRUD_LIZARDMEN_SCOUT = 20296;
    private static final int VIRUD_LIZARDMEN_SHAMAN_27 = 20297;
    private static final int VIRUD_LIZARDMEN_SHAMAN_28 = 20298;
    private static final int VIRUD_LIZARDMEN_MATRIARCH = 20294;
    private static final int LIZARDMEN = 20152;
    private static final int minLevel = 18;
    private static final int MAX_CHANCE = 100;
    private static final int MIN_CHANCE = 1;
    public Q00014_HelpingLeopoldWithLizards(){
        super(QUEST_ID);
        addStartNpc(LEPOLD);
        addTalkId(LEPOLD);
        addKillId(FELIM_LIZARDMEN,
                FELIM_LIZARDMEN_SCOUT,
                VIRUD_LIZARDMEN,
                VIRUD_LIZARDMEN_WARRIOR,
                VIRUD_LIZARDMEN_SCOUT,
                VIRUD_LIZARDMEN_SHAMAN_27,
                VIRUD_LIZARDMEN_SHAMAN_28,
                VIRUD_LIZARDMEN_MATRIARCH,
                LIZARDMEN,
                LANGK_LIZARDMEN,
                LANGK_LIZARDMEN_SCOUT,
                LANGK_LIZARDMEN_WARRIOR);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = null;
        if (player.getLevel() < minLevel) {
            return "00014-02.htm";
        }
        final QuestState qs = getQuestState(player, true);
        if (event.equals("00014-01.htm")) {
            qs.startQuest();
            qs.setCond(1, true);
            htmltext = event;
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player)
    {
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        switch (qs.getState()) {
            case State.CREATED: {
                if (npc.getId() == LEPOLD && player.getLevel() >= minLevel) {
                    htmltext = "00014-01.htm";
                } else {
                    htmltext = "00014-02.htm";
                }
                break;
            }
            case State.STARTED: {
                htmltext = "00014-03.htm";
                break;
            }
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon) {
        final QuestState qs = getQuestState(killer, false);
        if (qs == null) {
            return null;
        }
        Random rn = new Random();
        int randomNum;
        if (qs.isCond(1) && Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false))
        {
            randomNum = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
            switch (npc.getId()) {
                case FELIM_LIZARDMEN_SCOUT:
                case FELIM_LIZARDMEN:
                case FELIM_LIZARDMEN_WARRIOR:
                case LANGK_LIZARDMEN_SCOUT:
                case LANGK_LIZARDMEN_WARRIOR:
                case VIRUD_LIZARDMEN:
                case VIRUD_LIZARDMEN_WARRIOR:
                case VIRUD_LIZARDMEN_SCOUT:
                case VIRUD_LIZARDMEN_SHAMAN_27:
                case VIRUD_LIZARDMEN_SHAMAN_28:
                case VIRUD_LIZARDMEN_MATRIARCH:
                case LIZARDMEN:
                case LANGK_LIZARDMEN: {
                    if(randomNum > 20 && randomNum < 50) {
                        giveItems(killer, THE_HEART_OF_STONE, 1);
                    }
                    if(randomNum >= 50 && randomNum < 80) {
                        giveItems(killer, DOLL, 1);
                    }
                    if(randomNum >= 80) {
                        giveItems(killer, A_BAG_OF_POISON, 1);
                    }
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}
