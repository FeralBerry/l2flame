package quests.Q00009_ZombieHunting;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.NpcLogListHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;
import org.l2jmobius.gameserver.network.NpcStringId;

import java.util.HashSet;
import java.util.Set;

public class Q00009_ZombieHunting extends Quest {
    private static final int QUEST_ID = 9;
    private static final int ZOMBIE_HEAD = 973;
    private static final int ELF = 30157;
    private static final int LORD_ZOMBIE = 25375;
    private static final String KILL_COUNT_VAR = "KillCount";
    private static final int MIN_LEVEL = 15;
    private static final int MAX_LEVEL = 28;
    public Q00009_ZombieHunting(){
        super(QUEST_ID);
        addStartNpc(ELF);
        addKillId(LORD_ZOMBIE);
        registerQuestItems(ZOMBIE_HEAD);
    }
    public String onAdvEvent(String event, Npc npc, Player player){
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        switch (qs.getState()) {
            case State.CREATED: {
                if (npc.getId() == ELF && player.getLevel() >= MIN_LEVEL && player.getLevel() <= MAX_LEVEL ) {
                    qs.startQuest();
                    htmltext = "00009-02.htm";
                } else {
                    htmltext = "00009-01.htm";
                }
                break;
            }
            case State.STARTED: {
                if(qs.isCond(2)){
                    if (getQuestItemsCount(player, ZOMBIE_HEAD) >= 1)
                    {
                        giveItems(player, 57,30000);
                        qs.unset(KILL_COUNT_VAR);
                        qs.exitQuest(true, true);
                        htmltext = "00009-03.htm";
                    }
                }
                break;
            }
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon){
        final QuestState qs = getQuestState(killer, false);
        if (qs == null)
        {
            return null;
        }
        final int killCount = qs.getInt(KILL_COUNT_VAR) + 1;
        if (qs.isCond(1))
        {
            giveItems(killer, ZOMBIE_HEAD, 1);
            qs.set(KILL_COUNT_VAR, killCount);
            sendNpcLogList(killer);
            if (getQuestItemsCount(killer, ZOMBIE_HEAD) >= 1)
            {
                qs.setCond(2, true);
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
    public String onTalk(Npc npc, Player player) {
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        if (qs == null)
        {
            return null;
        }
        return htmltext;
    }
    public Set<NpcLogListHolder> getNpcLogList(Player player)
    {
        final QuestState qs = getQuestState(player, false);
        if (qs != null)
        {
            if (qs.isCond(1))
            {
                final Set<NpcLogListHolder> holder = new HashSet<>();
                holder.add(new NpcLogListHolder(NpcStringId.ZOMBIE_HEAD.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
}
