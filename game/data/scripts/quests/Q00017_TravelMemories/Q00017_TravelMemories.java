package quests.Q00017_TravelMemories;

import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;

import java.util.Random;

public class Q00017_TravelMemories extends Quest {
    private static final int QUEST_ID = 17;
    private static final int NETI = 30425; // id NPC
    private static final int LEVIAN = 30037; // id NPC
    private static final int SAMED = 30434; // id NPC
    private static final int JACOB = 30073; // id NPC
    private static final int minLevel = 20;
    private static final int MAX_CHANCE = 100;
    private static final int MIN_CHANCE = 1;

    public Q00017_TravelMemories(){
        super(QUEST_ID);
        addStartNpc(NETI);
        addTalkId(NETI,LEVIAN,SAMED,JACOB);
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = null;
        if(player.getLevel() < minLevel){
            return "00017-02.htm";
        }
        final QuestState qs = getQuestState(player, true);
        if(qs.isCond(5)){
            htmltext = "00017-07.htm";
            Random rn = new Random();
            int randomNum = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
            if(randomNum > 15){
                int random = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
                if(random < 15) {
                    giveItems(player, 7684, 2); // Материя для Ушек Енота
                } else if (random < 30) {
                    giveItems(player, 7685, 2); // Лента для Глазной Повязки Разбойника
                } else if (random < 45){
                    giveItems(player, 7686, 2); // Часть Заколки Девы
                } else if (random < 60) {
                    giveItems(player, 7687, 2); // Материя для Ушек Кролика
                } else if (random < 75) {
                    giveItems(player, 7688, 2); // Материя для Кошачьих Ушек
                } else if (random < 90) {
                    giveItems(player, 7697, 2); // Часть Заколки Незабудки
                } else if (random < 100) {
                    giveItems(player, 7698, 2); // Часть Заколки Маргаритки
                }
            } else {
                int random = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
                if(random < 15) {
                    giveItems(player, 7689, 1); // Рецепт: Ушки Енота (100%)
                } else if (random < 30) {
                    giveItems(player, 7690, 1); // Рецепт: Глазная Повязка Разбойника (100%)
                } else if (random < 45){
                    giveItems(player, 7691, 1); // Рецепт: Заколка Девы (100%)
                } else if (random < 60) {
                    giveItems(player, 7692, 1); // Рецепт: Ушки Кролика (100%)
                } else if (random < 75) {
                    giveItems(player, 7693, 1); // Рецепт: Кошачьи Ушки (100%)
                } else if (random < 90) {
                    giveItems(player, 7699, 1); // Рецепт: Заколка Незабудка (100%)
                } else if (random < 100) {
                    giveItems(player, 7700, 1); // Рецепт: Заколка Маргаритка (100%)
                }
            }
            qs.exitQuest(true, true);
        } else {
            if (event.equalsIgnoreCase("00017-01.htm")) {
                qs.startQuest();
                qs.setCond(1);
                htmltext = event;
            }
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player)
    {
        final QuestState qs = getQuestState(player, false);
        String htmltext = getNoQuestMsg(player);
        if(npc.getId() == LEVIAN && qs.isCond(1)){
            htmltext = "00017-03.htm";
            qs.setCond(2);
        }
        if(npc.getId() == SAMED && qs.isCond(2)){
            htmltext = "00017-04.htm";
            qs.setCond(3);
        }
        if(npc.getId() == SAMED && qs.isCond(3)){
            htmltext = "00017-05.htm";
            qs.setCond(4);
        }
        if(npc.getId() == JACOB && qs.isCond(4)){
            htmltext = "00017-06.htm";
            qs.setCond(5);
        }
        return htmltext;
    }
}
