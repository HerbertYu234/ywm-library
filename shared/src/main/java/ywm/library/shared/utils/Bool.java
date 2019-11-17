package ywm.library.shared.utils;

import com.wolf.lang.helper.Strings;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


public class Bool<T> {

    private T t;

    Bool(T t) {
        this.t = t;
    }

    public static <T> Bool<T> with(T t) {
        return new <T>Bool(t);
    }

    public Action<T> when(Predicate<T> predicate) {
        return new <T>Action(predicate.test(t), this);
    }

    public Action<T> whenNotBlank(Function<T, CharSequence> fun) {
        return new <T>Action(Strings.isNoneBlank(fun.apply(t)), this);
    }

    public Action<T> whenBlank(Function<T, CharSequence> fun) {
        return new <T>Action(Strings.isBlank(fun.apply(t)), this);
    }

    public Action<T> whenNonNull(Function<T, Object> fun) {
        return new <T>Action(Objects.nonNull(fun.apply(t)), this);
    }

    public Action<T> whenNull(Function<T, Object> fun) {
        return new <T>Action(Objects.isNull(fun.apply(t)), this);
    }

    public Action<T> whenCollsNotEmpty(Function<T, Collection> fun) {
        return new <T>Action(CollectionUtils.isNotEmpty(fun.apply(t)), this);
    }

    public Action<T> whenCollsEmpty(Function<T, Collection> fun) {
        return new <T>Action(CollectionUtils.isEmpty(fun.apply(t)), this);
    }

    /**
     * 正数 >0
     */
    public Action<T> whenPositiveNum(Function<T, Double> fun) {
        return new <T>Action(Objects.nonNull(fun.apply(t)) && fun.apply(t) > 0, this);
    }


    public static class Action<T> {

        private boolean satisfy;

        private Bool<T> bool;

        Action(boolean satisfy, Bool<T> bool) {
            this.satisfy = satisfy;
            this.bool = bool;
        }

        public Bool<T> andThen(Consumer<T> consumer) {
            if (satisfy) {
                consumer.accept(bool.t);
            }
            return bool;
        }

    }

}
