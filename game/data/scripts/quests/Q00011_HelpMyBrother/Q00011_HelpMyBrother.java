package quests.Q00011_HelpMyBrother;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.NpcLogListHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.network.NpcStringId;

import java.util.HashSet;
import java.util.Set;

public class Q00011_HelpMyBrother extends Quest {
    private static final int QUEST_ID = 11;
    private static final int KUNAI = 30559;
    private static final int CENTURION = 31036;
    private static final int MIN_LEVEL = 10;
    private static final String KILL_COUNT_VAR = "KillCount";
    private static final int REQUEST_COUNT = 25;
    private static final int MONSTER1 = 20015;
    private static final int MONSTER2 = 20425;
    private static final int MONSTER3 = 20516;
    private static final int MONSTER4 = 20368;
    public Q00011_HelpMyBrother(){
        super(QUEST_ID);
        addStartNpc(KUNAI);
        addTalkId(CENTURION,KUNAI);
        addKillId(MONSTER1,MONSTER2,MONSTER3,MONSTER4);
    }
    public String onAdvEvent(String event, Npc npc, Player player) {
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        if(qs.getInt(KILL_COUNT_VAR) < REQUEST_COUNT){
            htmltext = "00011-06.htm";
        }
        if(event.equalsIgnoreCase("00011-01.htm")){
            if (npc.getId() == KUNAI && player.getLevel() >= MIN_LEVEL) {
                qs.startQuest();
                htmltext = "00011-02.htm";
            } else {
                htmltext = "00011-01.htm";
            }
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon){
        final QuestState qs = getQuestState(killer, false);
        int killCount;
        if (qs != null) {
            switch (npc.getId()) {
                case MONSTER1:
                case MONSTER2:
                case MONSTER3:
                case MONSTER4: {
                    if (qs.isCond(2)) {
                        killCount = qs.getInt(KILL_COUNT_VAR) + 1;
                        if (killCount < REQUEST_COUNT) {
                            qs.set(KILL_COUNT_VAR, killCount);
                            sendNpcLogList(killer);
                        } else {
                            qs.setCond(3, true);
                        }
                    }
                }
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
        switch (npc.getId()) {
            case CENTURION:{
                if(qs.isCond(1)){
                    qs.setCond(2);
                    htmltext = "00011-03.htm";
                }
                if(qs.isCond(3)){
                    htmltext = "00011-04.htm";
                }
            }
            case KUNAI:{
                if (qs.isCond(3)) {
                    giveItems(player, 57, 6000);
                    qs.unset(KILL_COUNT_VAR);
                    qs.exitQuest(true, true);
                    htmltext = "00011-05.htm";
                }
            }
        }
        return htmltext;
    }
    public Set<NpcLogListHolder> getNpcLogList(Player player){
        final QuestState qs = getQuestState(player, false);
        if (qs != null)
        {
            if (qs.isCond(2))
            {
                final Set<NpcLogListHolder> holder = new HashSet<>();
                holder.add(new NpcLogListHolder(NpcStringId.UNDEAD.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
}
