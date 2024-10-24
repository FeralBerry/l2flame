package quests.Q00063_TheHealerTest;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.NpcLogListHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.zone.ZoneType;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.util.Util;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Q00063_TheHealerTest extends Quest {
    private static final int QUEST_ID = 63;
    private static final int[] NPC = {
            34505,
            30022,
    };
    private static final int minLevel = 39;
    private static final int[] QUEST_ITEMS = {

    };
    private static final int[] MONSTERS = {
            21216, // Владыка святых земель
            20159 // Минотавр
    };
    private static final int[][] REWARDS = {
            {2820, 1}, // Знак лекаря // Биш, ЕЕ, ШЕ, инспектор, овер
            {57, 120000}, // Адена
    };
    private static final String KILL_COUNT_VAR = "kill_count";
    private static final int KILL_COUNT_1 = 99;
    private static final int SPAWN_ZONE = 10064;
    public Q00063_TheHealerTest(){
        super(QUEST_ID);
        addStartNpc(NPC[0]);
        addTalkId(NPC);
        addKillId(MONSTERS);
        registerQuestItems(QUEST_ITEMS);
        addEnterZoneId(SPAWN_ZONE);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(
                player.getActiveClass() == 15 ||
                        player.getActiveClass() == 29 ||
                        player.getActiveClass() == 42 ||
                        player.getActiveClass() == 50
        ){
            if(player.getLevel() < minLevel){
                return "00063-01.htm";
            }
            final QuestState qs = getQuestState(player,true);
            if(event.equalsIgnoreCase("00063-01.htm")){
                if(npc.getId() == NPC[0]) {
                    if (qs.isCreated()) {
                        qs.startQuest();
                        if (qs.isStarted()) {
                            qs.setCond(1);
                            htmltext = "00063-02.htm";
                        }
                    }
                }
            }
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player)
    {
        final QuestState qs = getQuestState(player, false);
        String htmltext = getNoQuestMsg(player);
        if(npc.getId() == NPC[1] && qs.isCond(1)){
            htmltext = "00063-03.htm";
            qs.setCond(2);
        }
        if(npc.getId() == NPC[1] && qs.isCond(3)){
            htmltext = "00063-04.htm";
            qs.setCond(4);
        }
        if(npc.getId() == NPC[1] && qs.isCond(5)){
            htmltext = "00063-05.htm";
            for (int[] reward : REWARDS) {
                giveItems(player, reward[0], reward[1]);
            }
            qs.exitQuest(false, true);
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon) {
        final QuestState qs = getQuestState(killer, false);
        if (qs == null) {
            return null;
        }
        final int killCount = qs.getInt(KILL_COUNT_VAR) + 1;
        if(Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false)){
            if(qs.isCond(2) && npc.getId() == MONSTERS[0]) {
                if(killCount < KILL_COUNT_1){
                    qs.set(KILL_COUNT_VAR,killCount);
                } else {
                    qs.set(KILL_COUNT_VAR,0);
                    qs.setCond(3);
                }
            }
            if(qs.isCond(4) && npc.getId() == MONSTERS[1]) {
                qs.setCond(5);
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
                holder.add(new NpcLogListHolder(NpcStringId.THE_LORD_OF_THE_HOLY_LANDS.getId(), true, qs.getInt(KILL_COUNT_VAR)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
    @Override
    public String onEnterZone(Creature creature, ZoneType zone)
    {
        long delay = 600000;
        int qs = creature.getActingPlayer().getQuestState("Q00063_TheHealerTest").getCond();
        Player player = creature.getActingPlayer();
        if(qs == 4 && player.getQuestZoneId() != 4){
            if (creature.isPlayer() && (zone.getId() == SPAWN_ZONE))
            {
                player.setQuestZoneId(4);
                TimerZone(player,delay);
                addSpawn(MONSTERS[1], player.getClientX(), player.getClientY(), player.getClientZ(), 0, true, delay);
            }
        }
        return super.onEnterZone(creature, zone);
    }
    private static void TimerZone(Player player, long delay){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                player.setQuestZoneId(0);
            }
        }, delay);
    }
}
