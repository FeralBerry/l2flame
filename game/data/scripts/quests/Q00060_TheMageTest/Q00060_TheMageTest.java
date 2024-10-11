package quests.Q00060_TheMageTest;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.item.instance.Item;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.util.Util;
public class Q00060_TheMageTest extends Quest {
    private static final int QUEST_ID = 60;
    private static final int[] NPC = {
        34505
    };
    private static final int minLevel = 39;
    private static final int[] QUEST_ITEMS = {
            817,// Магическая жидкость
            973, // голова зомби
            827, // звёздная пыль
            826, // Серебристые споры
            971, // Ловец душ
            820, // Слеза души
            1475, // Чёрный Камень Души
            1476 // Испорченный камень души
    };
    private static final int[] MONSTERS = {
            22045, // Темный Труп
            21644, // Аспид
            20764, // Шаман ящеров
            20581, // Шаман Ящеров Лито
            20501, // Провидец Орков Турек
            20546 // Тетрарх Орк Турек
    };
    private static final int[][] REWARDS = {
            {2840, 1}, // Знак мага // сорк, СС, СХ, кам маг, маг ейтерия
            {57, 120000}, // Адена
    };
    public Q00060_TheMageTest(){
        super(QUEST_ID);
        addStartNpc(NPC[0]);
        addTalkId(NPC);
        addKillId(MONSTERS);
        registerQuestItems(QUEST_ITEMS);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(
                player.getActiveClass() == 11 ||
                        player.getActiveClass() == 26 ||
                        player.getActiveClass() == 39 ||
                        player.getActiveClass() == 125 ||
                        player.getActiveClass() == 126 ||
                        player.getActiveClass() == 185
        ){
            if(player.getLevel() < minLevel){
                return "00060-01.htm";
            }
            final QuestState qs = getQuestState(player,true);
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            if(npc.getId() == NPC[0]) {
                if (qs.isCreated()) {
                    qs.startQuest();
                    if (qs.isStarted()) {
                        qs.setCond(1);
                        htmltext = "00060-02.htm";
                    }
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
    public String onKill(Npc npc, Player killer, boolean isSummon) {
        final QuestState qs = getQuestState(killer, false);
        if (qs == null) {
            return null;
        }
        int npcId = npc.getId();
        if (Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false)) {
            if (qs.isCond(1)) {
                if (npcId == MONSTERS[0]){
                    giveItems(killer,QUEST_ITEMS[1],1);
                }
                if (npcId == MONSTERS[1]){
                    giveItems(killer,QUEST_ITEMS[2],1);
                }
                if (npcId == MONSTERS[2] || npcId == MONSTERS[3]){
                    giveItems(killer,QUEST_ITEMS[3],1);
                }
                if (npcId == MONSTERS[4] || npcId == MONSTERS[5]){
                    giveItems(killer,QUEST_ITEMS[4],1);
                }
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
    @org.l2jmobius.gameserver.model.events.annotations.RegisterEvent(org.l2jmobius.gameserver.model.events.EventType.ON_ITEM_USE)
    @org.l2jmobius.gameserver.model.events.annotations.RegisterType(org.l2jmobius.gameserver.model.events.ListenerRegisterType.ITEM)
    @org.l2jmobius.gameserver.model.events.annotations.Id(817)
    public void onItemUse(org.l2jmobius.gameserver.model.events.impl.item.OnItemUse event){
        final Player player = event.getPlayer();
        long countItem = 0;
        if(!player.getQuestState("Q00060_TheMageTest").isCompleted()){
            if (event.getItem().getId() == QUEST_ITEMS[0])
            {
                takeItems(player,QUEST_ITEMS[0],player.getInventory().getItemByItemId(QUEST_ITEMS[0]).getCount());
                for (int i = 1; i < QUEST_ITEMS.length; i++) {
                    Item item = player.getInventory().getItemByItemId(QUEST_ITEMS[i]);
                    if(item != null){
                        countItem = item.getCount();
                        if(countItem > 0){
                            takeItems(player,QUEST_ITEMS[i],countItem);
                        }
                    }
                }
                for (int[] reward : REWARDS) {
                    giveItems(player, reward[0], reward[1]);
                }
                player.getQuestState("Q00060_TheMageTest").exitQuest(false, true);
            }
        }
    }
}
