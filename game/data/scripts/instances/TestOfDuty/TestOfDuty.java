package instances.TestOfDuty;

import instances.AbstractInstance;
import org.l2jmobius.gameserver.instancemanager.InstanceManager;
import org.l2jmobius.gameserver.model.Location;
import org.l2jmobius.gameserver.model.Party;
import org.l2jmobius.gameserver.model.actor.Creature;
import org.l2jmobius.gameserver.model.actor.Npc;
import org.l2jmobius.gameserver.model.actor.Player;
import org.l2jmobius.gameserver.model.instancezone.Instance;

import java.util.Arrays;
import java.util.logging.Level;

public class TestOfDuty extends AbstractInstance {
    protected static final int[] BUFFS_WHITELIST =
            {
                    4322, 4323, 4324, 4325, 4326, 4327, 4328, 4329, 4330, 4331, 5632, 5637, 5950
            };
    private static final Location TELEPORT = new Location(-88429, -220629, -7903);
    private static final int[] MONSTERS = {
            20579, // Воитель Ящеров Лито 37
            20553, // Вепрь
            21034, // Огр 38
            20501, // Провидец Орков Турек 41
            20793, // Рогач
            27102 // Кот Пако (Босс)
    };
    private static final int KILL_COUNT = 10;
    private static final int QUEST_ITEM_ID = 695;
    private static final int BOSS_ID = 27102;
    private static final int[][] REWARDS = {
            {2633, 1}, // Знак долга // Паладин, ДА, ТК, ШК, МертвыйТанк
            {57, 120000}, // Адена
    };
    public TestOfDuty()
    {
        super(125);
        addStartNpc(34505);
        addKillId(MONSTERS);
    }
    private static boolean checkPartyConditions(Player player){
        final Party party = player.getParty();
        // player must be in party
        return party == null;
    }
    private void removeBuffs(Creature ch)
    {
        // Stop all buffs.
        ch.getEffectList().stopEffects(info -> (info != null) && !info.getSkill().isStayAfterDeath() && (Arrays.binarySearch(BUFFS_WHITELIST, info.getSkill().getId()) < 0), true, true);
    }
    private final synchronized void enterInstance(Player player, int index)
    {
        int templateId;
        try
        {
            templateId = 135;
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            LOGGER.warning("Problem with TestOdDuty: " + e.getMessage());
            return;
        }
        if(player.getQuestState("Q00058_TheTrialOfDutyTest").isCond(1) ||
        player.getQuestState("Q00058_TheTrialOfDutyTest").isCond(2) ||
        player.getQuestState("Q00058_TheTrialOfDutyTest").isCond(3) ||
        player.getQuestState("Q00058_TheTrialOfDutyTest").isCond(4) ||
        player.getQuestState("Q00058_TheTrialOfDutyTest").isCond(5) ||
        player.getQuestState("Q00058_TheTrialOfDutyTest").isCond(6)
        ){
            // check for existing instances for this player
            Instance world = player.getInstanceWorld();
            // player already in the instance
            if (world != null)
            {
                // but not in kamaloka
                if ((player.getInstanceId() == 0) || (world.getTemplateId() != templateId))
                {
                    return;
                }
                // check what instance still exist
                final Instance inst = InstanceManager.getInstance().getInstance(world.getId());
                if (inst != null)
                {
                    removeBuffs(player);
                    player.teleToLocation(TELEPORT, world);
                }
                return;
            }
            // Creating new instance
            if (!checkPartyConditions(player))
            {
                // месседж
                return;
            }

            // Creating instance
            world = InstanceManager.getInstance().createInstance(templateId, player);
            // set duration and empty destroy time
            world.setDuration(60 * 60000);
            // set index for easy access to the arrays
            world.setParameter("index", index);
            world.setStatus(0);
            // spawn npcs
            spawn(world,player);
            removeBuffs(player);
            player.teleToLocation(TELEPORT, world);
        }
    }
    private void spawn(Instance world,Player player)
    {
        Npc npc;
        if(player.getQuestState("Q00058_TheTrialOfDutyTest").isCond(1)){
            npc = addSpawn(MONSTERS[0],-88998, -220077, -7892, 0, false, 0, false, world.getId());
            npc.setRandomWalking(true);
        }
        if(player.getQuestState("Q00058_TheTrialOfDutyTest").isCond(2)){
            npc = addSpawn(MONSTERS[1],-88998, -220077, -7892, 0, false, 0, false, world.getId());
            npc.setRandomWalking(true);
        }
        if(player.getQuestState("Q00058_TheTrialOfDutyTest").isCond(3)){
            npc = addSpawn(MONSTERS[2],-88998, -220077, -7892, 0, false, 0, false, world.getId());
            npc.setRandomWalking(true);
        }
        if(player.getQuestState("Q00058_TheTrialOfDutyTest").isCond(4)){
            npc = addSpawn(MONSTERS[3],-88998, -220077, -7892, 0, false, 0, false, world.getId());
            npc.setRandomWalking(true);
        }
        if(player.getQuestState("Q00058_TheTrialOfDutyTest").isCond(5)){
            npc = addSpawn(MONSTERS[4],-88998, -220077, -7892, 0, false, 0, false, world.getId());
            npc.setRandomWalking(true);
        }
        if(player.getQuestState("Q00058_TheTrialOfDutyTest").isCond(6)){
            npc = addSpawn(BOSS_ID,-88998, -220077, -7892, 0, false, 0, false, world.getId());
            npc.setRandomWalking(true);
        }
    }
    @Override
    public String onAdvEvent(String event, Npc npc, Player player)
    {
        if (npc != null)
        {
            try
            {
                enterInstance(player, Integer.parseInt(event));
            }
            catch (Exception e)
            {
                LOGGER.log(Level.WARNING, "", e);
            }
        }
        return null;
    }
    @Override
    public String onKill(Npc npc, Player player, boolean isSummon)
    {
        final Instance world = npc.getInstanceWorld();
        if (world != null)
        {
            if(npc.getId() == BOSS_ID) {
                for (int[] reward : REWARDS) {
                    giveItems(player, reward[0], reward[1]);
                }
                world.finishInstance(0);
                player.teleToLocation(-14608,123920,-3115);
                player.getQuestState("Q00058_TheTrialOfDutyTest").exitQuest(false, true);
            }
            giveItems(player, QUEST_ITEM_ID, 1);
            if (getQuestItemsCount(player, QUEST_ITEM_ID) < KILL_COUNT) {
                addSpawn(MONSTERS[0], -88998, -220077, -7892, 0, false, 0, false, world.getId());
            }
            if (getQuestItemsCount(player, QUEST_ITEM_ID) == KILL_COUNT) {
                player.getQuestState("Q00058_TheTrialOfDutyTest").setCond(2);
                addSpawn(MONSTERS[1], -88998, -220077, -7892, 0, false, 0, false, world.getId());
            }
            if (getQuestItemsCount(player, QUEST_ITEM_ID) > KILL_COUNT && getQuestItemsCount(player, QUEST_ITEM_ID) < KILL_COUNT * 2) {
                addSpawn(MONSTERS[1], -88998, -220077, -7892, 0, false, 0, false, world.getId());
            }
            if (getQuestItemsCount(player, QUEST_ITEM_ID) == KILL_COUNT * 2) {
                player.getQuestState("Q00058_TheTrialOfDutyTest").setCond(3);
                addSpawn(MONSTERS[2], -88998, -220077, -7892, 0, false, 0, false, world.getId());
            }
            if (getQuestItemsCount(player, QUEST_ITEM_ID) > KILL_COUNT * 2 && getQuestItemsCount(player, QUEST_ITEM_ID) < KILL_COUNT * 3) {
                addSpawn(MONSTERS[2], -88998, -220077, -7892, 0, false, 0, false, world.getId());
            }
            if (getQuestItemsCount(player, QUEST_ITEM_ID) == KILL_COUNT * 3) {
                player.getQuestState("Q00058_TheTrialOfDutyTest").setCond(4);
                addSpawn(MONSTERS[3], -88998, -220077, -7892, 0, false, 0, false, world.getId());
            }
            if (getQuestItemsCount(player, QUEST_ITEM_ID) > KILL_COUNT * 3 && getQuestItemsCount(player, QUEST_ITEM_ID) < KILL_COUNT * 4) {
                addSpawn(MONSTERS[3], -88998, -220077, -7892, 0, false, 0, false, world.getId());
            }
            if (getQuestItemsCount(player, QUEST_ITEM_ID) == KILL_COUNT * 4) {
                player.getQuestState("Q00058_TheTrialOfDutyTest").setCond(5);
                addSpawn(MONSTERS[4], -88998, -220077, -7892, 0, false, 0, false, world.getId());
            }
            if (getQuestItemsCount(player, QUEST_ITEM_ID) > KILL_COUNT * 4 && getQuestItemsCount(player, QUEST_ITEM_ID) < KILL_COUNT * 5) {
                addSpawn(MONSTERS[4], -88998, -220077, -7892, 0, false, 0, false, world.getId());
            }
            if (getQuestItemsCount(player, QUEST_ITEM_ID) == KILL_COUNT * 5) {
                player.getQuestState("Q00058_TheTrialOfDutyTest").setCond(6);
                takeItems(player, QUEST_ITEM_ID, KILL_COUNT * 5);
                addSpawn(BOSS_ID, -88998, -220077, -7892, 0, false, 0, false, world.getId());
            }

        }

        return super.onKill(npc, player, isSummon);
    }
    public static void main(String[] args)
    {
        new TestOfDuty();
    }
}
