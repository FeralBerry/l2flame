package quests.Q00015_HelpingLeopoldWithMahums;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;
import org.l2jmobius.gameserver.util.Util;

import java.util.Random;

public class Q00015_HelpingLeopoldWithMahums extends Quest {
    private static final int QUEST_ID = 15;
    private static final int LEPOLD = 30435; // id NPC
    private static final int RED = 1359;
    private static final int BLUE = 1360;
    private static final int BLACK = 1361;
    private static final int minLevel = 18;
    private static final int MAX_CHANCE = 100;
    private static final int MIN_CHANCE = 1;
    private static final int GRIFON = 20142;
    private static final int GNOLL_RECRUIT = 20437;
    private static final int GNOLL_LOOKOUT = 20053;
    private static final int GNOLL_GUARDIAN = 20058;
    private static final int GNOLL_RANGER = 20224;
    private static final int GNOLL_DESERTER= 20548;
    private static final int GNOLL_SUPPLIER= 20436;
    private static final int GNOLL_LONER= 20061;
    private static final int GNOLL= 20163;
    private static final int GNOLL_LOOKOUT_2 = 20065;
    private static final int GNOLL_RANGER_2 = 20063;
    private static final int GNOLL_CAPITAN = 20066;
    private static final int GNOLL_LEGIONER = 20073;
    private static final int GNOLL_GENERAL = 20438;
    private static final int GNOLL_RANGER_3 = 20164;
    private static final int GNOLL_BEASTMEN = 20209;
    private static final int GNOLL_COMANDER = 20076;
    private static final int GNOLL_CHEMPION = 20165;
    private static final int GNOLL_RAIDER = 20208;
    private static final int GNOLL_SERGANT = 20210;
    private static final int GNOLL_OFFICER = 20439;
    private static final int GNOLL_PARTIZAN = 20207;
    private static final int GNOLL_COMANDER_L = 20755;
    private static final int GNOLL_LEADER = 20211;
    private static final int GNOLL_WARRIOR = 21011;
    private static final int GNOLL_LORD = 20549;
    private static final int GNOLL_RANDGER_AVANGARD = 21013;
    private static final int GNOLL_LEADER_AVANGARD = 21015;
    private static final int GNOLL_BLOOD = 20212;
    public Q00015_HelpingLeopoldWithMahums(){
        super(QUEST_ID);
        addStartNpc(LEPOLD);
        addTalkId(LEPOLD);
        addKillId(
                GRIFON,
                GNOLL_RECRUIT,
                GNOLL_LOOKOUT,
                GNOLL_GUARDIAN,
                GNOLL_RANGER,
                GNOLL_DESERTER,
                GNOLL_SUPPLIER,
                GNOLL_LONER,
                GNOLL,
                GNOLL_LOOKOUT_2,
                GNOLL_RANGER_2,
                GNOLL_CAPITAN,
                GNOLL_LEGIONER,
                GNOLL_GENERAL,
                GNOLL_RANGER_3,
                GNOLL_BEASTMEN,
                GNOLL_COMANDER,
                GNOLL_CHEMPION,
                GNOLL_RAIDER,
                GNOLL_SERGANT,
                GNOLL_OFFICER,
                GNOLL_PARTIZAN,
                GNOLL_COMANDER_L,
                GNOLL_LEADER,
                GNOLL_WARRIOR,
                GNOLL_LORD,
                GNOLL_RANDGER_AVANGARD,
                GNOLL_LEADER_AVANGARD,
                GNOLL_BLOOD
                );
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = null;
        if(player.getLevel() < minLevel){
            return "00015-02.htm";
        }
        final QuestState qs = getQuestState(player, true);
        if (event.equalsIgnoreCase("00015-01.htm")) {
            qs.startQuest();
            qs.isCond(1);
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
                    htmltext = "00015-01.htm";
                } else {
                    htmltext = "00015-02.htm";
                }
                break;
            }
            case State.STARTED: {
                htmltext = "00015-03.htm";
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
                case GRIFON:
                case GNOLL_RECRUIT:
                case GNOLL_LOOKOUT:
                case GNOLL_GUARDIAN:
                case GNOLL_RANGER:
                case GNOLL_DESERTER:
                case GNOLL_SUPPLIER:
                case GNOLL_LONER:
                case GNOLL:
                case GNOLL_LOOKOUT_2:
                case GNOLL_RANGER_2:
                case GNOLL_CAPITAN:
                case GNOLL_LEGIONER:
                case GNOLL_GENERAL:
                case GNOLL_RANGER_3:
                case GNOLL_BEASTMEN:
                case GNOLL_COMANDER:
                case GNOLL_CHEMPION:
                case GNOLL_RAIDER:
                case GNOLL_SERGANT:
                case GNOLL_OFFICER:
                case GNOLL_PARTIZAN:
                case GNOLL_COMANDER_L:
                case GNOLL_LEADER:
                case GNOLL_WARRIOR:
                case GNOLL_LORD:
                case GNOLL_RANDGER_AVANGARD:
                case GNOLL_LEADER_AVANGARD:
                case GNOLL_BLOOD:
                {
                    if(randomNum > 20 && randomNum < 50) {
                        giveItems(killer, RED, 1);
                    }
                    if(randomNum >= 50 && randomNum < 80) {
                        giveItems(killer, BLUE, 1);
                    }
                    if(randomNum >= 80) {
                        giveItems(killer, BLACK, 1);
                    }
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}
