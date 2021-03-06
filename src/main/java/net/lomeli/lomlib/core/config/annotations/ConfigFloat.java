package net.lomeli.lomlib.core.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.common.config.Configuration;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigFloat {
    float defaultValue();
    float maxValue() default Float.MAX_VALUE;
    float minValue() default Float.MIN_VALUE;
    String nameOverride() default "";
    String comment() default "";
    String category() default Configuration.CATEGORY_GENERAL;
    String categoryComment() default "";
}
