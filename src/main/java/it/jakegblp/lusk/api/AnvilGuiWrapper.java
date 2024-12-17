package it.jakegblp.lusk.api;

import it.jakegblp.lusk.Lusk;
import it.jakegblp.lusk.api.events.AnvilGuiClickEvent;
import it.jakegblp.lusk.api.events.AnvilGuiCloseEvent;
import it.jakegblp.lusk.api.events.AnvilGuiOpenEvent;
import net.wesjd.anvilgui.AnvilGUI;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class AnvilGuiWrapper {
    private static final Map<AnvilGuiWrapper, Set<UUID>> OPEN_GUIS = new HashMap<>();

    private final AnvilGUI.Builder builder;
    private AnvilGUI anvilGUI;
    private String title, text;
    private ItemStack left, right, output;
    private int[] interactableSlots = null;
    private boolean isClosing;
    private boolean preventsClose;

    public AnvilGuiWrapper() {
        this.builder = new AnvilGUI.Builder();
        builder.plugin(Lusk.getInstance());
        builder.onClick((slot, snapshot) -> List.of(AnvilGUI.ResponseAction.run(() ->
                Lusk.callEvent(new AnvilGuiClickEvent(this, snapshot, slot)))));
        builder.onClose(snapshot -> {
            isClosing = true;
            removeUUIDs(snapshot.getPlayer().getUniqueId());
            Lusk.callEvent(new AnvilGuiCloseEvent(this, snapshot));
        });
    }

    public AnvilGuiWrapper(AnvilGuiWrapper anvilGuiWrapper) {
        this.builder = new AnvilGUI.Builder();
        builder.plugin(Lusk.getInstance());
        setLeft(anvilGuiWrapper.getLeft());
        setRight(anvilGuiWrapper.getRight());
        setOutput(anvilGuiWrapper.getOutput());
        setTitle(anvilGuiWrapper.getTitle());
        setText(anvilGuiWrapper.getText());
        this.isClosing = this.preventsClose = false;
        this.anvilGUI = null;
        this.interactableSlots = anvilGuiWrapper.interactableSlots;
    }

    public AnvilGUI.Builder getBuilder() {
        return builder;
    }

    public void open(Player... players) {
        Set<UUID> playerSet = OPEN_GUIS.computeIfAbsent(this, k -> new HashSet<>());
        int length = players.length;
        for (int i = 0; i < length; i++) {
            Player player = players[i];
            if (i == length - 1) {
                anvilGUI = getBuilder().open(player);
            } else {
                getBuilder().open(player);
            }
            playerSet.add(player.getUniqueId());
            Lusk.callEvent(new AnvilGuiOpenEvent(this, player));
        }
    }

    public static Map<AnvilGuiWrapper, Set<UUID>> getOpenGuis() {
        return OPEN_GUIS;
    }

    public void closeAndOpen(Player... players) {
        Set<AnvilGuiWrapper> guisToRemove = OPEN_GUIS.entrySet().stream()
                .filter(entry -> Arrays.stream(players)
                        .anyMatch(player -> entry.getValue().contains(player.getUniqueId())))
                .distinct()
                .peek(entry -> entry.getKey().close())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        guisToRemove.forEach(OPEN_GUIS::remove);

        for (Player player : players) {
            open(player);
        }

    }

    public boolean isOpenTo(Player... players) {
        if (OPEN_GUIS.containsKey(this))
            return OPEN_GUIS.get(this).containsAll(Arrays.stream(players).map(Entity::getUniqueId).collect(Collectors.toSet()));
        return false;
    }

    public static boolean isViewingAnyAnvilGui(Player... players) {
        return Arrays.stream(players)
                .allMatch(player -> OPEN_GUIS
                        .values()
                        .stream()
                        .anyMatch(uuids -> uuids.contains(player.getUniqueId())));
    }

    public static AnvilGuiWrapper getOpenAnvilGui(Player player) {
        return OPEN_GUIS.entrySet().stream()
                .filter(entry -> entry.getValue().contains(player.getUniqueId()))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // todo: figure out per-player mechanics, not too relevant for now
    //public void closeAndOpen(Player player) {
    //    OPEN_GUIS.forEach((k, v) -> {
    //        v.remove(player.getUniqueId());
    //        k.close();
    //
    //    });
    //    open(player);
    //}

    public void setInteractableSlots(int... slots) {
        interactableSlots = Arrays.stream(slots).distinct().toArray();
        builder.interactableSlots(interactableSlots);
    }

    public void addInteractableSlots(int... slots) {
        setInteractableSlots(ArrayUtils.addAll(interactableSlots, slots));
    }

    public void removeInteractableSlots(int... slots) {
        setInteractableSlots(ArrayUtils.removeAll(interactableSlots, slots));
    }

    public void resetInteractableSlots() {
        setInteractableSlots();
    }

    public int @Nullable [] getInteractableSlots() {
        return interactableSlots;
    }

    private void removeUUIDs(UUID... uuids) {
        Set<UUID> playerSet = OPEN_GUIS.get(this);
        if (playerSet != null) {
            List.of(uuids).forEach(playerSet::remove);
            if (playerSet.isEmpty()) {
                OPEN_GUIS.remove(this);
            }
        }
    }

    private void internallyClose() {
        if (anvilGUI != null) {
            anvilGUI.closeInventory();
            isClosing = false;
            OPEN_GUIS.remove(this);
        }
    }

    /**
     * Closes the anvil gui.
     * If this method is used to forcibly close the gui when prevented, it'll do so one tick later.
     */
    public void close() {
        if (isClosing && isPreventsClose()) {
            Bukkit.getScheduler().runTaskLater(Lusk.getInstance(), this::internallyClose, 1);
            return;
        }
        internallyClose();
    }

    public void preventClose() {
        this.builder.preventClose();
        preventsClose = true;
    }

    public boolean isPreventsClose() {
        return preventsClose;
    }

    public AnvilGUI getAnvilGUI() {
        return anvilGUI;
    }

    public ItemStack getLeft() {
        return left;
    }

    public void setLeft(ItemStack left) {
        this.left = left;
        this.builder.itemLeft(left);
    }

    public ItemStack getRight() {
        return right;
    }

    public void setRight(ItemStack right) {
        this.right = right;
        this.builder.itemRight(right);
    }

    public ItemStack getOutput() {
        return output;
    }

    public void setOutput(ItemStack output) {
        this.output = output;
        this.builder.itemOutput(output);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.builder.text(text);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(@NotNull String title) {
        this.title = title;
        this.builder.title(title);
    }

    @Override
    public String toString() {
        return "anvil gui" + (getTitle() != null ? " named " + getTitle() : "") + (getText() != null ? " with text " + getText() : "");
    }
}
