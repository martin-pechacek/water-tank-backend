package watertank.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Fn {
    /**
     * Creates an object using given constructor and initializer.
     *
     * @param constructor Used to create an instance. Called exactly once
     * @param initializer Executed on newly created instance to initialize it before exposing.
     */
    public static <T> T create(Supplier<T> constructor, Consumer<? super T> initializer) {
        T o = constructor.get();
        initializer.accept(o);
        return o;
    }
}
