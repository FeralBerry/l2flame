package quests.Q00013_ANewFriend;

import org.l2jmobius.Config;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.holders.NpcLogListHolder;
import org.l2jmobius.gameserver.model.quest.Quest;
import org.l2jmobius.gameserver.model.quest.QuestState;
import org.l2jmobius.gameserver.network.NpcStringId;
import org.l2jmobius.gameserver.util.Util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Q00013_ANewFriend extends Quest {
    private static final int QUEST_ID = 13;
    private static final int URUTU = 30587;
    private static final int SPARTOI_BONES = 1183;
    private static final int SPARKLE_PABBLE = 1287;
    private static final int TOPAZ_PIECE = 1205;
    private static final String KILL_COUNT_VAR1 = "KillCount";
    private static final String KILL_COUNT_VAR2 = "KillCount";
    private static final String KILL_COUNT_VAR3 = "KillCount";
    private static final int REQUEST_COUNT = 15;
    private static final int MIN_CHANCE = 1;
    private static final int MAX_CHANCE = 100;
    private static final int MIN_LEVEL = 15;
    private static final int DOOM_SOLDIER = 20455;
    private static final int STONE_GOLEM = 20521;
    private static final int BEAR = 20479;
    private static final int[] REWARDS = {
            2375,
            2515,
    };
    public Q00013_ANewFriend(){
        super(QUEST_ID);
        addStartNpc(URUTU);
        addKillId(DOOM_SOLDIER,STONE_GOLEM,BEAR);
        registerQuestItems(SPARTOI_BONES,SPARKLE_PABBLE,TOPAZ_PIECE);
    }
    public String onAdvEvent(String event, Npc npc, Player player) {
        final QuestState qs = getQuestState(player, true);
        String htmltext = getNoQuestMsg(player);
        if(qs.getInt(KILL_COUNT_VAR1) < REQUEST_COUNT || qs.getInt(KILL_COUNT_VAR2) < REQUEST_COUNT || qs.getInt(KILL_COUNT_VAR3) < REQUEST_COUNT){
            htmltext = "00013-04.htm";
        }
        if(event.equalsIgnoreCase("00013-01.htm")){
            if (npc.getId() == URUTU && player.getLevel() >= MIN_LEVEL) {
                qs.startQuest();
                htmltext = "00013-02.htm";
            } else {
                htmltext = "00013-01.htm";
            }
        }
        if (qs.isCond(2)) {
            giveItems(player, REWARDS[0], 1);
            giveItems(player, REWARDS[1], 100);
            qs.unset(KILL_COUNT_VAR1);
            qs.unset(KILL_COUNT_VAR2);
            qs.unset(KILL_COUNT_VAR3);
            qs.exitQuest(true, true);
            htmltext = "00013-03.htm";
        }
        return htmltext;
    }
    public String onKill(Npc npc, Player killer, boolean isSummon){
        final QuestState qs = getQuestState(killer, false);
        Random rn = new Random();
        int randomNum;
        int killCount1 = 0;
        int killCount2 = 0;
        int killCount3 = 0;
        if (qs.isCond(1) && Util.checkIfInRange(Config.ALT_PARTY_RANGE, npc, killer, false)){
            randomNum = rn.nextInt(MAX_CHANCE - MIN_CHANCE + 1) + MIN_CHANCE;
            if(npc.getId() == DOOM_SOLDIER && killCount1 < REQUEST_COUNT){
                if(randomNum > 40) {
                    killCount1 = qs.getInt(KILL_COUNT_VAR1) + 1;
                    giveItems(killer, SPARTOI_BONES, 1);
                    qs.set(KILL_COUNT_VAR1, killCount1);
                    sendNpcLogList(killer);
                }
            }
            if(npc.getId() == STONE_GOLEM && killCount2 < REQUEST_COUNT){
                if(randomNum > 40) {
                    killCount2 = qs.getInt(KILL_COUNT_VAR2) + 1;
                    giveItems(killer, SPARKLE_PABBLE, 1);
                    qs.set(KILL_COUNT_VAR2, killCount2);
                    sendNpcLogList(killer);
                }
            }
            if(npc.getId() == BEAR && killCount3 < REQUEST_COUNT){
                if(randomNum > 40) {
                    killCount3 = qs.getInt(KILL_COUNT_VAR3) + 1;
                    giveItems(killer, TOPAZ_PIECE, 1);
                    qs.set(KILL_COUNT_VAR3, killCount3);
                    sendNpcLogList(killer);
                }
            }
            if(killCount1 == REQUEST_COUNT && killCount2 == REQUEST_COUNT && killCount3 == REQUEST_COUNT){
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
    public Set<NpcLogListHolder> getNpcLogList(Player player){
        final QuestState qs = getQuestState(player, false);
        if (qs != null)
        {
            if (qs.isCond(1))
            {
                final Set<NpcLogListHolder> holder = new HashSet<>();
                holder.add(new NpcLogListHolder(NpcStringId.BEED.getId(), true, qs.getInt(KILL_COUNT_VAR1)));
                holder.add(new NpcLogListHolder(NpcStringId.BEED.getId(), true, qs.getInt(KILL_COUNT_VAR2)));
                holder.add(new NpcLogListHolder(NpcStringId.BEED.getId(), true, qs.getInt(KILL_COUNT_VAR3)));
                return holder;
            }
        }
        return super.getNpcLogList(player);
    }
}
