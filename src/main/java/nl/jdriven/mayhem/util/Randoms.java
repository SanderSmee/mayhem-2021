package nl.jdriven.mayhem.util;

import org.apache.commons.lang3.RandomUtils;

import java.util.List;
import java.util.Optional;

public final class Randoms {

    public static <T> T randomFrom(List<T> list) {
        if (list.isEmpty()) return null;

        var index = RandomUtils.nextInt(0, list.size());
        return list.get(index);
    }

    public static <T> Optional<T> optionalRandomFrom(List<T> list) {
        if (list.isEmpty()) return Optional.empty();

        var index = RandomUtils.nextInt(0, list.size());
        return Optional.of(list.get(index));
    }
}
