package it.jakegblp.lusk.nms.guardian;

import it.jakegblp.lusk.nms.core.async.ExecutionMode;
import it.jakegblp.lusk.nms.core.protocol.packets.client.*;
import it.jakegblp.lusk.nms.core.world.entity.AttributeSnapshot;
import it.jakegblp.lusk.nms.core.world.entity.metadata.EntityMetadata;
import it.jakegblp.lusk.nms.core.world.entity.metadata.MetadataKeys;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;


@Getter
public class GuardianBeam {

    public static final Map<String, GuardianBeam> beams = new HashMap<>();

    public static GuardianBeam getBeam(String id) {
        return beams.getOrDefault(id.toLowerCase(), null);
    }

    String id;
    Location from;
    Location to;
    int guardianID = ThreadLocalRandom.current().nextInt(999999, Integer.MAX_VALUE);
    UUID guardianUUID = UUID.randomUUID();
    int squidID = ThreadLocalRandom.current().nextInt(999999, Integer.MAX_VALUE);
    List<UUID> viewers = new ArrayList<>();
    boolean persistent;

    List<UUID> canSee = new ArrayList<>();

    int taskID;

    Plugin plugin;


    public GuardianBeam(Plugin plugin, String id, Location from, Location to, boolean persistent) {
        this.id = id.toLowerCase();

        if (beams.containsKey(this.id)) {
            plugin.getLogger().log(Level.WARNING, "a beam with id " + this.id + " already exists");
            return;
        }

        this.from = from;
        this.to = correctEndLocation(from, to);
        this.persistent = persistent;

        this.plugin = plugin;

        if(persistent)
            loop();
        beams.put(this.id, this);
    }


    public void addViewer(UUID uuid) {
        if (viewers.contains(uuid))
            return;

        viewers.add(uuid);
        runCheckAndShow(uuid);
    }

    public void removeViewer(UUID uuid) {
        viewers.remove(uuid);
        canSee.remove(uuid);

        final Player player = Bukkit.getPlayer(uuid);
        if (player == null)
            return;
        new RemoveEntitiesPacket(List.of(guardianID, squidID)).send(player);
    }


    public void loop() {
        this.taskID = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            boolean skip = true;
            for (UUID uuid : viewers) {
                if (Bukkit.getPlayer(uuid) != null) {
                    skip = false;
                    break;
                }
                else
                    canSee.remove(uuid);
            }

            if (skip) { //no point running checks if no user is online
                return;
            }

            for (UUID uuid : this.viewers) {
                final Player player = Bukkit.getPlayer(uuid);
                if (player == null) {
                    canSee.remove(uuid);
                    continue;
                }

                runCheckAndShow(uuid);
            }
        }, 20L, 20L).getTaskId();
    }

    public void runCheckAndShow(UUID uuid) {
        final Player player = Bukkit.getPlayer(uuid);
        if (player == null)
            return;

        if (player.getWorld() != this.from.getWorld()) {
            canSee.remove(uuid);
            return;
        }

        final Location playerLocation = player.getLocation();
        if (playerLocation.distanceSquared(from) > 22500 || playerLocation.distanceSquared(to) > 22500) { //150 blocks
            canSee.remove(uuid);
            return;
        }

        if (canSee.contains(uuid))
            return;

        canSee.add(uuid);

        display(player);
    }


    private void display(Player player) {

        new AddEntityPacket(guardianID, guardianUUID, from.getX(), from.getY(), from.getZ(), from.getYaw(), from.getPitch(), EntityType.GUARDIAN, 0, new Vector(0, 0, 0), from.getY())
                .send(player, ExecutionMode.ASYNCHRONOUS);
        new AddEntityPacket(squidID, UUID.randomUUID(), to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch(), EntityType.BAT, 0, new Vector(0, 0, 0), to.getY())
                .send(player, ExecutionMode.ASYNCHRONOUS);


        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            makeSmall(squidID).send(player);
            makeSmall(guardianID).send(player);
        }, 1L);
        guardianData().send(player, ExecutionMode.ASYNCHRONOUS);
        squidData().send(player, ExecutionMode.ASYNCHRONOUS);
    }


    private EntityMetadataPacket guardianData() {
        final EntityMetadata entityMetadata = new EntityMetadata();
        entityMetadata.set(MetadataKeys.GuardianKeys.TARGET_ENTITY_ID, squidID);
         entityMetadata.set(MetadataKeys.EntityKeys.INVISIBLE, true);
        entityMetadata.set(MetadataKeys.GuardianKeys.IS_RETRACTING_SPIKES, true);

        return new EntityMetadataPacket(guardianID, entityMetadata);
    }

    private EntityMetadataPacket squidData() {
        final EntityMetadata entityMetadata = new EntityMetadata();
         entityMetadata.set(MetadataKeys.EntityKeys.INVISIBLE, true);

        return new EntityMetadataPacket(squidID, entityMetadata);
    }

    private AttributePacket makeSmall(int entityID) { //todo dont use if below 1.21.5
        return new AttributePacket(entityID, List.of(new AttributeSnapshot(Attribute.SCALE, 0.01, List.of(new AttributeModifier(UUID.randomUUID(), "scale", 0.01, AttributeModifier.Operation.ADD_NUMBER)))));
    }


    public void destroy() {
        final RemoveEntitiesPacket removeEntitiesPacket = new RemoveEntitiesPacket(List.of(guardianID, squidID));

        for (UUID uuid : this.viewers) {
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null)
                continue;

            removeEntitiesPacket.send(player, ExecutionMode.ASYNCHRONOUS);
        }
        Bukkit.getScheduler().cancelTask(this.taskID);
        beams.remove(this.id);
    }

    private Location correctEndLocation(Location start, Location end) {
        Vector guardianVector = start.toVector();
        Vector batVector = end.toVector();
        Vector direction = guardianVector.subtract(batVector).normalize();

        Vector newBatVector = batVector.add(direction);
        return newBatVector.toLocation(end.getWorld());
    }

}
