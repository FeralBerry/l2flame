package quests.Q00016_HelpHuntROA;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.model.quest.State;
import org.l2jmobius.gameserver.util.Util;

import java.util.Random;

public class Q00016_HelpHuntROA extends Quest {
    private static final int QUEST_ID = 16;
    private static final int PARINA = 30391; // id NPC
    private static final int minLevel = 20;
    private static final int questItem_1 = 1259; // коленная кость
    private static final int questItem_2 = 1260; // сердце безумия
    private static final int ZOMBIE_1 = 24382; // Отвратительный Зомби 20
    private static final int ZOMBIE_2 = 24383; // Отвратительный Лорд Зомби 20
    private static final int ZOMBIE_3 = 20458; // Воин Зомби 22
    private static final int ZOMBIE_4 = 20198; // Вурдалак Берсерк 29
    private static final int ZOMBIE_5 = 20160; // Потрошитель 28
    private static final int ANIMAL_1 = 24384; // Кракос Летучая Мышь 20
    private static final int ANIMAL_2 = 20140; // Гигантская Пиявка 24
    private static final int ANIMAL_3 = 20225; // Гигантская Туманная Пиявка 25
    private static final int DEMONS_1 = 21167; // Ведьма Лит 24
    private static final int DEMONS_2 = 21169; // Страж Лит 30
    private static final int DEMONS_3 = 21168; // Воин Лит 27
    private static final int DEMONS_4 = 24385; // Вампир 20
    private static final int GHOST_1 = 20543; // Призрак 20
    private static final int GHOST_2 = 20329; // Призрак Страж 21
    private static final int GHOST_3 = 20171; // Спектр 26
    private static final int GHOST_4 = 20227; // Туманный Потрошитель 27
    private static final int GHOST_5 = 20615; // Призрачный Мечник 29
    private static final int GHOST_6 = 21136; // Ужас Смертельной Ловушки 28
    private static final int GHOST_7 = 22007; // Призрак Солдата 29
    private static final int SKELET_1 = 20051; // Скелет Лучник 20
    private static final int SKELET_2 = 20054; // Скелет Руин 21
    private static final int SKELET_3 = 20060; // Яростный Скелет 22
    private static final int SKELET_4 = 21133; // Скелет Стрелок Смертельной Ловушки 25
    private static final int SKELET_5 = 21234; // Стражник Могил 25
    private static final int SKELET_6 = 21134; // Скелет Смертельной Ловушки 26
    private static final int SKELET_7 = 24386; // Скелетон Разведчик 27
    private static final int SKELET_8 = 24387; // Скелетон Лучник 27
    private static final int SKELET_9 = 24388; // Скелетон Воин 27
    private static final int SKELET_10 = 20519; // Скелет Пикинер 27
    private static final int SKELET_11 = 20191; // Скелет Налетчик 30
    private static final int SKELET_12 = 20190; // Скелет Мародер 29
    private static final int SKELET_13 = 20254; // Скелет с Секирой 28

    private static final int MAX_CHANCE = 100;
    private static final int MIN_CHANCE = 1;

    public Q00016_HelpHuntROA(){
        super(QUEST_ID);
        addStartNpc(PARINA);
        addTalkId(PARINA);
        addKillId(
                    ZOMBIE_1,
                    ZOMBIE_2,
                    ZOMBIE_3,
                    ZOMBIE_4,
                    ZOMBIE_5,
                    ANIMAL_1,
                    ANIMAL_2,
                    ANIMAL_3,
                    DEMONS_1,
                    DEMONS_2,
                    DEMONS_3,
                    DEMONS_4,
                    GHOST_1,
                    GHOST_2,
                    GHOST_3,
                    GHOST_4,
                    GHOST_5,
                    GHOST_6,
                    GHOST_7,
                    SKELET_1,
                    SKELET_2,
                    SKELET_3,
                    SKELET_4,
                    SKELET_5,
                    SKELET_6,
                    SKELET_7,
                    SKELET_8,
                    SKELET_9,
                    SKELET_10,
                    SKELET_11,
                    SKELET_12,
                    SKELET_13
                );
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player) {
        String htmltext = null;
        if(player.getLevel() < minLevel){
            return "00016-02.htm";
        }
        final QuestState qs = getQuestState(player, true);
        if (event.equalsIgnoreCase("00016-01.htm")) {
            qs.startQuest();
            qs.isCond(1);
            htmltext = event;
        }
        return htmltext;
    }
    @Override
    public String onTalk(Npc npc, Player player)
    {
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        switch (qs.getState()) {
            case State.CREATED: {
                if (npc.getId() == PARINA && player.getLevel() >= minLevel) {
                    htmltext = "00016-01.htm";
                } else {
                    htmltext = "00016-02.htm";
                }
                break;
            }
            case State.STARTED: {
                htmltext = "00016-03.htm";
                break;
            }
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon) {
        final QuestState qs = getQuestState(killer, false);
        if (qs == null) {
            return null;
        }
        Random rn = new Random();
        int randomNum;
        if (qs.isCond(1) && Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false))
        {
            randomNum = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
            int npcId = npc.getId();
            if (
                    npcId == SKELET_1
                    || npcId == SKELET_2
                    || npcId == SKELET_3
                    || npcId == SKELET_4
                    || npcId == SKELET_5
                    || npcId == SKELET_6
                    || npcId == SKELET_7
                    || npcId == SKELET_8
                    || npcId == SKELET_9
                    || npcId == SKELET_10
                    || npcId == SKELET_11
                    || npcId == SKELET_12
                    || npcId == SKELET_13
            ){
                if(randomNum > 20) {
                    giveItems(killer, questItem_1, 1);
                }
            }
            if(
                    npcId == ANIMAL_1
                    || npcId == ANIMAL_2
                    || npcId == ANIMAL_3
                    || npcId == ZOMBIE_1
                    || npcId == ZOMBIE_2
                    || npcId == ZOMBIE_3
                    || npcId == ZOMBIE_4
                    || npcId == ZOMBIE_5
            ){
                if(randomNum > 20) {
                    giveItems(killer, questItem_2, 1);
                }
            }
            if(
                    npcId == GHOST_1
                            || npcId == GHOST_2
                            || npcId == GHOST_3
                            || npcId == GHOST_4
                            || npcId == GHOST_5
                            || npcId == GHOST_6
                            || npcId == GHOST_7
            ){
                if(randomNum < 50) {
                    giveItems(killer, questItem_1, 1);
                }
                if(randomNum >= 50) {
                    giveItems(killer, questItem_2, 1);
                }
            }
            if(
                    npcId == DEMONS_1
                            || npcId == DEMONS_2
                            || npcId == DEMONS_3
            ){
                if(randomNum > 70) {
                    giveItems(killer, questItem_1, 3);
                    giveItems(killer, questItem_2, 3);
                }
                if(randomNum > 30 && randomNum <= 70) {
                    giveItems(killer, questItem_1, 2);
                    giveItems(killer, questItem_2, 2);
                }
                if(randomNum <= 30) {
                    giveItems(killer, questItem_1, 1);
                    giveItems(killer, questItem_2, 1);
                }
            }
            if(
                    npcId == DEMONS_4
            ){
                giveItems(killer, questItem_1, 1);
                giveItems(killer, questItem_2, 1);
            }
        }
        return super.onKill(npc, killer, isSummon);
    }
}
