package quests.Q00001_Newbie;

import org.l2jmobius.gameserver.enums.Race;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.NpcLogListHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.network.serverpackets.ExShowScreenMessage;
import org.l2jmobius.gameserver.network.serverpackets.ExTutorialShowId;

import java.util.HashSet;
import java.util.Set;

public class Q00001_Newbie extends Quest {
    private static final int QUEST_ID = 1;
    private static final int SS_NG_NOVICE = 5789; // id соски воин
    private static final int BSS_NG_NOVICE = 5790; // id соски маг
    private static final int NEWBIE_GUIDE = 30575; // id NPC
    private static final int GREMLIN = 18342; // id NPC - для охоты
    private static final int YOUNG_KELTIR = 20531; // id NPC - для охоты
    private static final int PRAIRIE_KELTIR = 20535; // id NPC - для охоты
    private static final int ELDER_PRAIRIE_KELTIR = 20538; // id NPC - для охоты
    private static final String KILL_COUNT_VAR = "KillCount";
    public Q00001_Newbie(){
        super(QUEST_ID);
        addStartNpc(NEWBIE_GUIDE);
        addTalkId(NEWBIE_GUIDE);
        addKillId(GREMLIN, YOUNG_KELTIR, PRAIRIE_KELTIR, ELDER_PRAIRIE_KELTIR);
        setQuestNameNpcStringId(NpcStringId.TRAINING_START_HUNTING);
    }

    @Override
    public String onAdvEvent(String event, Npc npc, Player player){
        String htmltext = null;
        final QuestState qs = getQuestState(player, false);
        if (qs == null)
        {
            return htmltext;
        }
        switch (event) {
            case "30575-02.htm": {
                qs.startQuest();
                qs.setCond(1, true);
                giveItems(player, SS_NG_NOVICE, 300);
                giveItems(player, BSS_NG_NOVICE, 100);
                player.sendPacket(new ExTutorialShowId(9)); // Quest
                giveStoryBuffReward(npc, player);
                htmltext = event;
                break;
            }
            case "30575-05.htm": {
                if (qs.isCond(4))
                {
                    htmltext = event;
                    player.sendPacket(new ExTutorialShowId(102)); // Class Transfer
                }
                qs.setCond(5);
                break;
            }
            case "30575-06.htm": {
                if (qs.isCond(5))
                {
                    htmltext = event;
                    if (player.getRace() == Race.KAMAEL)
                    {
                        giveItems(player, SS_NG_NOVICE, 2000);
                        giveItems(player, BSS_NG_NOVICE, 1000);
                    }
                    else if (player.isMageClass())
                    {
                        giveItems(player, BSS_NG_NOVICE, 2000);
                    }
                    else
                    {
                        giveItems(player, SS_NG_NOVICE, 3000);
                    }
                    qs.exitQuest(false, true);
                }
                showOnScreenMsg(player, NpcStringId.TO_CONTINUE_YOUR_STUDIES_GO_TO_THE_VILLAGE, ExShowScreenMessage.TOP_CENTER, 10000);
                break;
            }
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player) {
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        switch (qs.getState()) {
            case State.CREATED: {
                if (npc.getId() == NEWBIE_GUIDE) {
                    htmltext = "30575-01.htm";
                }
                break;
            }
            case State.STARTED: {
                if (npc.getId() == NEWBIE_GUIDE) {
                    htmltext = "30575-03.htm";
                    if (qs.isCond(1)) {
                        if (!player.isSimulatingTalking()) {
                            player.sendPacket(new ExTutorialShowId(14)); // Soulshots and Spiritshots
                        }
                        qs.setCond(2);
                    } else if(qs.isCond(3)){
                        htmltext = "30575-04.htm";
                        qs.setCond(4);
                    }
                }
                break;
            }
            case State.COMPLETED: {
                htmltext = getAlreadyCompletedMsg(player);
                break;
            }
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon) {
        final QuestState qs = getQuestState(killer, false);
        if (qs != null) {
            switch (npc.getId()) {
                case GREMLIN:
                case YOUNG_KELTIR:
                case PRAIRIE_KELTIR:
                case ELDER_PRAIRIE_KELTIR: {
                    if (qs.isCond(2)) {
                        final int killCount = qs.getInt(KILL_COUNT_VAR) + 1;
                        if (killCount < 15) {
                            qs.set(KILL_COUNT_VAR, killCount);
                            sendNpcLogList(killer);
                        } else {
                            qs.setCond(3, true);
                            qs.unset(KILL_COUNT_VAR);
                            // сообщение на пол экрана меняется в компиле
                            showOnScreenMsg(killer, NpcStringId.CONGRATULATIOS_ON_COMPLETING_THE_HUNT_TO_COMPLETE_THE_QUEST_TALK_TO_THE_NOVICE, ExShowScreenMessage.TOP_CENTER, 10000);
                        }
                    }
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }

    @Override
    public Set<NpcLogListHolder> getNpcLogList(Player player)
    {
        final QuestState qs = getQuestState(player, false);
        if (qs != null)
        {
            if (qs.isCond(2))
            {
                final Set<NpcLogListHolder> holder = new HashSet<>();
                holder.add(new NpcLogListHolder(NpcStringId.TRAINING_START_HUNTING.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
}
