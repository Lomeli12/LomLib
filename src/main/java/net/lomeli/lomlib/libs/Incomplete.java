package net.lomeli.lomlib.libs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method declaration is incomplete. While the method can still
 * be used, it is advised NOT to use it due to possible bugs or crashes that may
 * result.
 *
 * @author Lomeli12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Incomplete {

    MethodState currentState() default MethodState.NEW;

    public static enum MethodState {
        NEW, BUGGY, NEAR_COMPELETION
    }
}