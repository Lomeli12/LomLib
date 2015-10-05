package net.lomeli.lomlib.core.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SidedPacket {
    boolean acceptedServerSide() default true;

    boolean acceptedClientSide() default true;
}
