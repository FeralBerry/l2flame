package quests.Q00061_TheSeekerTest;

import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.zone.ZoneType;

import java.util.Timer;
import java.util.TimerTask;

public class Q00061_TheSeekerTest extends Quest {
    private static final int QUEST_ID = 61;
    private static final int[] NPC = {
            34505,
            30647, // спавн сундук
            30628 // финальный сундук
    };
    private static final int minLevel = 39;
    private static final int[] QUEST_ITEMS = {
            687, // Письмо с запиской
            692, // Письмо с запиской
            768 // Ключ от тайника
    };
    private static final int FIRST_BOX_ZONE = 10061;
    private static final int SECOND_BOX_ZONE = 10062;
    private static final int THIRD_BOX_ZONE = 10063;
    private static final int[][] REWARDS = {
            {2673, 1}, // Знак искателя // ТХ, БХ, АВ, ПВ
            {57, 120000}, // Адена
    };
    public Q00061_TheSeekerTest(){
        super(QUEST_ID);
        addStartNpc(NPC[0]);
        addTalkId(NPC);
        registerQuestItems(QUEST_ITEMS);
        addEnterZoneId(FIRST_BOX_ZONE,SECOND_BOX_ZONE,THIRD_BOX_ZONE);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(
                player.getActiveClass() == 7 ||
                        player.getActiveClass() == 22 ||
                        player.getActiveClass() == 35 ||
                        player.getActiveClass() == 54
        ){
            if(player.getLevel() < minLevel){
                return "00061-01.htm";
            }
            final QuestState qs = getQuestState(player,true);
            if(event.equalsIgnoreCase("00061-02.htm")){
                htmltext = "00061-02-1.htm";
            } else if (event.equalsIgnoreCase("00061-03.htm")) {
                htmltext = "00061-03-1.htm";
            } else if (event.equalsIgnoreCase("00061-04.htm")) {
                htmltext = "00061-04-1.htm";
            } else {
                if(npc.getId() == NPC[0]) {
                    if (qs.isCreated()) {
                        qs.startQuest();
                        if (qs.isStarted()) {
                            qs.setCond(1);
                            htmltext = "00061-02.htm";
                        }
                    }
                }
                if (npc.getId() == NPC[1]){
                    if(player.getQuestZoneId() == 1){
                        if (qs.isCond(1)){
                            qs.setCond(2);
                            player.setQuestZoneId(0);
                            giveAdena(player,122,true);
                            giveItems(player,QUEST_ITEMS[0],1);
                            htmltext = "00061-03.htm";
                            npc.deleteMe();
                        }
                    }
                    if(player.getQuestZoneId() == 2){
                        if (qs.isCond(2)){
                            qs.setCond(3);
                            giveAdena(player,50,true);
                            giveItems(player,QUEST_ITEMS[1],1);
                            player.setQuestZoneId(0);
                            htmltext = "00061-04.htm";
                            npc.deleteMe();
                        }
                    }
                    if(player.getQuestZoneId() == 3){
                        if (qs.isCond(3)){
                            qs.setCond(4);
                            giveAdena(player,171,true);
                            giveItems(player,QUEST_ITEMS[2],1);
                            player.setQuestZoneId(0);
                            htmltext = "00061-05.htm";
                            npc.deleteMe();
                        }
                    }
                }
                if(npc.getId() == NPC[2] && qs.isCond(4)){
                    for (int[] reward : REWARDS) {
                        giveItems(player, reward[0], reward[1]);
                    }
                    qs.exitQuest(false, true);
                    htmltext = "00061-06.htm";
                }
            }
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player)
    {
        return getNoQuestMsg(player);
    }
    @Override
    public String onEnterZone(Creature creature, ZoneType zone)
    {
        long delay = 60000;
        int qs = creature.getActingPlayer().getQuestState("Q00061_TheSeekerTest").getCond();
        Player player = creature.getActingPlayer();
        if(qs == 1 && player.getQuestZoneId() != 1){
            if (creature.isPlayer() && (zone.getId() == FIRST_BOX_ZONE))
            {
                player.setQuestZoneId(1);
                TimerZone(player,delay);
                addSpawn(NPC[1], player.getClientX(), player.getClientY(), player.getClientZ(), 0, true, delay);
            }
        }
        if(qs == 2 && player.getQuestZoneId() != 2){
            if (creature.isPlayer() && (zone.getId() == SECOND_BOX_ZONE))
            {
                player.setQuestZoneId(2);
                TimerZone(player,delay);
                addSpawn(NPC[1], player.getClientX(), player.getClientY(), player.getClientZ(), 0, true, delay);
            }
        }
        if(qs == 3 && player.getQuestZoneId() != 3){
            if (creature.isPlayer() && (zone.getId() == THIRD_BOX_ZONE))
            {
                player.setQuestZoneId(3);
                TimerZone(player,delay);
                addSpawn(NPC[1], player.getClientX(), player.getClientY(), player.getClientZ(), 0, true, delay);
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
