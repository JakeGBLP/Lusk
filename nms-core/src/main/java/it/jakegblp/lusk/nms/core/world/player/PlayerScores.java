package it.jakegblp.lusk.nms.core.world.player;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import javax.annotation.Nullable;

public class PlayerScores {
    private final Reference2ObjectOpenHashMap<Objective, Score> scores = new Reference2ObjectOpenHashMap<>(16, 0.5F);

    @Nullable
    public Score get(Objective objective) {
        return this.scores.get(objective);
    }

    public boolean remove(Objective objective) {
        return this.scores.remove(objective) != null;
    }

    public boolean hasScores() {
        return !this.scores.isEmpty();
    }

    public Object2IntMap<Objective> listScores() {
        Object2IntMap<Objective> object2IntMap = new Object2IntOpenHashMap<>();
        this.scores.forEach((objective, score) -> object2IntMap.put(objective, score.getScore()));
        return object2IntMap;
    }

    void setScore(Objective objective, Score score) {
        this.scores.put(objective, score);
    }

}
