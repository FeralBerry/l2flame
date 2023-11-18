package quests.Q00002_Newbie2;

import org.l2jmobius.gameserver.enums.Race;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;
import org.l2jmobius.gameserver.network.NpcStringId;

public class Q00002_Newbie2 extends Quest {
    private static final int QUEST_ID = 2;
    private static final int NEWBIE_GUIDE = 30602; // id NPC
    private static final int PAPUMA = 30561; // id NPC бакалейщик
    private static final int USKA = 30560; // id NPC ювелир
    private static final int IMANTU = 30563; // id NPC склад
    private static final int KUNAI = 30559; // id NPC доспех
    private static final int JAKAL = 30558; // id NPC оружейник
    private static final int SUMARI = 30564; // id NPC кузнец
    // Rewards
    private static final int GALLINT_OAK_WAND = 748; // дубинка для маговq
    private static final int MACE = 5; // Дубина для гномов
    private static final int BROADSWORD = 3; // Меч для воинов
    private static final int IRON_GLOVES = 254; // Кастет для орков
    private static final int WARRIOR_SWORD = 9720; // рапира для камаелей
    private static final int HEALING_POTION = 1060; // малые бутылки хп
    public Q00002_Newbie2(){
        super(QUEST_ID);
        addStartNpc(NEWBIE_GUIDE);
        addTalkId(NEWBIE_GUIDE,PAPUMA,USKA,IMANTU,KUNAI,JAKAL,SUMARI);
        setQuestNameNpcStringId(NpcStringId.TRAINING_GETTING_TO_KNOW_THE_POSSIBILITIES);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player){
        final QuestState qs = getQuestState(player, true);
        String htmltext = null;
        switch (qs.getState()) {
            case State.CREATED: {
                if (npc.getId() == NEWBIE_GUIDE) {
                    htmltext = "00002-01.htm";
                }
                break;
            }
            case State.COMPLETED: {
                htmltext = getAlreadyCompletedMsg(player);
                break;
            }
        }
        if (event.equalsIgnoreCase("00002-02.htm")) {
            htmltext = event;
            qs.startQuest();
            qs.setCond(1);
        }
        if(event.equalsIgnoreCase("00002-09.htm")){
            if (qs.isCond(7)) {
                htmltext = event;
                if (player.getRace() == Race.KAMAEL){
                    giveItems(player, WARRIOR_SWORD, 1);
                } else if (player.isMageClass()) {
                    giveItems(player, GALLINT_OAK_WAND, 1);
                } else if(player.getRace() == Race.DWARF) {
                    giveItems(player, MACE, 1);
                } else if(player.getRace() == Race.ORC) {
                    giveItems(player,IRON_GLOVES,1);
                } else {
                    giveItems(player,BROADSWORD,1);
                }
                giveItems(player, HEALING_POTION, 50);
                qs.exitQuest(false, true);
            }
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player) {
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        if (qs == null)
        {
            return null;
        }
        switch (qs.getState()) {
            case State.STARTED: {
                switch (npc.getId()) {
                    case PAPUMA: {
                        if (qs.isCond(1)) {
                            qs.setCond(2);
                            htmltext = "00002-03.htm";
                        }
                        break;
                    }
                    case USKA: {
                        if (qs.isCond(2)) {
                            qs.setCond(3);
                            htmltext = "00002-04.htm";
                        }
                        break;
                    }
                    case IMANTU: {
                        if (qs.isCond(3)) {
                            qs.setCond(4);
                            htmltext = "00002-05.htm";
                        }
                        break;
                    }
                    case KUNAI: {
                        if (qs.isCond(4)) {
                            qs.setCond(5);
                            htmltext = "00002-06.htm";
                        }
                        break;
                    }
                    case JAKAL: {
                        if (qs.isCond(5)) {
                            qs.setCond(6);
                            htmltext = "00002-07.htm";
                        }
                        break;
                    }
                    case SUMARI: {
                        if (qs.isCond(6)) {
                            qs.setCond(7);
                            htmltext = "00002-08.htm";
                        }
                        break;
                    }
                }
                break;
            }
        }
        return htmltext;
    }
}
