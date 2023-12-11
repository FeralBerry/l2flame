package quests.Q00005_TheFirstArmor;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.NpcLogListHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.network.NpcStringId;

import java.util.HashSet;
import java.util.Set;

public class Q00005_TheFirstArmor extends Quest {
    private static final int QUEST_ID = 5;
    private static final int MIN_LEVEL = 8;
    private static final String KILL_COUNT_VAR = "KillCount";
    private static final int MAX_CHANCE = 100;
    private static final int MIN_CHANCE = 1;
    public Q00005_TheFirstArmor()
    {
        super(QUEST_ID);
        /*addStartNpc(PAPUMA);
        addTalkId(PAPUMA);
        addKillId(MOUNTAIN_FUNGUS);
        registerQuestItems(SPORE_SAC);*/
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player){

    }
    @Override
    public String onKill(Npc npc, Player killer, boolean isSummon){

    }
    @Override
    public String onTalk(Npc npc, Player player){

    }
    @Override
    public Set<NpcLogListHolder> getNpcLogList(Player player)
    {
        final QuestState qs = getQuestState(player, false);
        if (qs != null)
        {
            if (qs.isCond(1))
            {
                final Set<NpcLogListHolder> holder = new HashSet<>();
                holder.add(new NpcLogListHolder(NpcStringId.SPORE_SACS.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
}
