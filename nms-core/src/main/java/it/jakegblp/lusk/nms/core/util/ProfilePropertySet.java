package it.jakegblp.lusk.nms.core.util;

import com.destroystokyo.paper.profile.ProfileProperty;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NullMarked
@NoArgsConstructor
public final class ProfilePropertySet implements Set<ProfileProperty> {

    @Delegate
    private final Set<ProfileProperty> delegate = new HashSet<>(16);

    public ProfilePropertySet(Collection<? extends ProfileProperty> initial) {
        delegate.addAll(initial);
    }

    public @Nullable ProfileProperty get(String name) {
        return delegate.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst().orElse(null);
    }
}
