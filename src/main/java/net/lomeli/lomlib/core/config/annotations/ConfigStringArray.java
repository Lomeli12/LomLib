package net.lomeli.lomlib.core.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.common.config.Configuration;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigStringArray {
    String[] defaultValue();
    String nameOverride() default "";
    String comment() default "";
    String category() default Configuration.CATEGORY_GENERAL;
}
