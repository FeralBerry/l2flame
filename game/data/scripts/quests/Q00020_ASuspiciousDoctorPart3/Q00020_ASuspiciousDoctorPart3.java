package quests.Q00020_ASuspiciousDoctorPart3;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.util.Util;

public class Q00020_ASuspiciousDoctorPart3 extends Quest {
    private static final int QUEST_ID = 20;
    private static final int ORPHEUS = 30971; // id NPC
    private static final int minLevel = 30;
    private static final int FIRE = 3416; // Вечный Огонь
    private static final int WATER = 3160; // Живая Вода
    private static final int DEMON = 27117;
    private static final int[] MONSTERS = {
            18611, // дух воды
            18609, // Папион (Дух воды)
            18614, // Дух Огня
            18610, // Хиллас (Дух воды)
            18612, // Водяной призрак
            18616, // Фирак
            27117
    };
    private static final long KILL_COUNT_WATER = 120;
    private static final long KILL_COUNT_FIRE = 80;
    private static final long KILL_COUNT_WATER_REMOVE = 20;
    private static final long KILL_COUNT_FIRE_REMOVE = 10;
    private static final int[][] REWARDS = {
            {129, 1}, // Sword of Revolution
            {224, 1}, // Maingauche
            {1660, 1}, // Cursed Maingauche
            {279, 1}, // Reinforced Longbow
            {158, 1}, // Tarbar
            {7881, 1}, // Lesser Giant's Sword
            {7896, 1}, // Lesser Giant's Hammer
            {172, 1}, // Heavy Bone Club
            {294, 1}, // War Pick
            {88, 1}, // Morning Star
            {93, 1}, // Winged Spear
            {261, 1}, // Bich'Hwa
            {90, 1}, // Goat Head Staff
            {7886, 1}, // Sword of Magic Fog
            {7890, 1}, // Priest Mace
            {318, 1} // Crucifix of Blood
    };
    public Q00020_ASuspiciousDoctorPart3(){
        super(QUEST_ID);
        addStartNpc(ORPHEUS);
        addTalkId(ORPHEUS);
        addKillId(MONSTERS);
        registerQuestItems(FIRE,WATER);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = getNoQuestMsg(player);
        if(player.getQuestState("Q00019_ASuspiciousDoctorPart2") != null && player.getQuestState("Q00019_ASuspiciousDoctorPart2").isCompleted()){
            final QuestState qs = getQuestState(player,true);
            if(player.getLevel() < minLevel){
                return "00020-02.htm";
            }
            if(qs.isCompleted()){
                return getAlreadyCompletedMsg(player);
            }
            qs.startQuest();
            qs.setCond(1);
            htmltext = "00020-03.htm";
        } else {
            htmltext = "00020-01.htm";
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player)
    {
        final QuestState qs = getQuestState(player, false);
        String htmltext = getNoQuestMsg(player);
        if(player.getQuestState("Q00019_ASuspiciousDoctorPart2") != null && player.getQuestState("Q00019_ASuspiciousDoctorPart2").isCompleted()) {
            if (npc.getId() == ORPHEUS && qs.isCond(2)) {
                htmltext = "00020-05.htm";
                int randomNum = (int) (Math.random() * (REWARDS.length + 1));
                giveItems(player, REWARDS[randomNum][0], REWARDS[randomNum][1]);
                qs.exitQuest(false, true);
            }
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon) {
        final QuestState qs = getQuestState(killer, false);
        if (qs == null) {
            return null;
        }
        int npcId = npc.getId();
        if (Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false)) {
            if (qs.isCond(1)) {
                
                if (npcId == DEMON){
                    qs.setCond(2);
                    showHtmlFile(killer,"00020-04.htm");
                }
                if (
                        npcId == MONSTERS[0]
                                || npcId == MONSTERS[1]
                                || npcId == MONSTERS[3]
                                || npcId == MONSTERS[4]
                ){

                    giveItems(killer,WATER,1);
                }
                if (
                        npcId == MONSTERS[2]
                                || npcId == MONSTERS[5]
                ){

                    giveItems(killer,FIRE,1);
                }
                if(getQuestItemsCount(killer,WATER) > KILL_COUNT_WATER && getQuestItemsCount(killer,FIRE) > KILL_COUNT_FIRE){
                    addSpawn(DEMON, killer.getClientX(), killer.getClientY(), killer.getClientZ(), 0, true, 300000);
                    takeItems(killer, WATER, KILL_COUNT_WATER_REMOVE);
                    takeItems(killer, FIRE, KILL_COUNT_FIRE_REMOVE);
                }
            }
        }

        return super.onKill(npc, killer, isSummon);
    }
}
