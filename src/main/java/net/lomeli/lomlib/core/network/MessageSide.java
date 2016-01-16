package net.lomeli.lomlib.core.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.fml.relauncher.Side;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MessageSide {
    boolean serverSide() default true;

    boolean clientSide() default true;
}
