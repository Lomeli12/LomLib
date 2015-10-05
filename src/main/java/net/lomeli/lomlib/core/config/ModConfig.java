package net.lomeli.lomlib.core.config;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.lomlib.core.config.annotations.*;
import net.lomeli.lomlib.util.ObfUtil;

/**
 * Totally not copying iChun's connfic annotation system, nope, not at all.
 */
public class ModConfig {
    protected Configuration config;
    protected String modid;
    protected Class<?>[] libClasses;
    protected List<String> categories;

    public ModConfig(String modid, Configuration config, Class<?>... libClasess) {
        this.config = config;
        this.modid = modid;
        this.libClasses = libClasess;
        this.categories = Lists.newArrayList();
    }

    public ModConfig(String modid, File configFile, Class<?>... libClasess) {
        this(modid, new Configuration(configFile), libClasess);
    }

    public Configuration getConfig() {
        return config;
    }

    public void loadConfig() {
        if (libClasses != null && libClasses.length > 0 && config != null) {
            for (int i = 0; i < libClasses.length; i++)
                readFields(libClasses[i]);

            if (config.hasChanged())
                config.save();
        }
    }

    private void readFields(Class<?> clazz) {
        Field[] fields = clazz.getFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                field.setAccessible(true);
                if (!Modifier.isTransient(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                    if (field.isAnnotationPresent(ConfigInt.class) && field.getType() == int.class) {
                        ConfigInt fieldConfig = field.getAnnotation(ConfigInt.class);
                        int value = config.getInt(!Strings.isNullOrEmpty(fieldConfig.nameOverride()) ? fieldConfig.nameOverride() : field.getName(), fieldConfig.category(), fieldConfig.defaultValue(), fieldConfig.minValue(), fieldConfig.maxValue(), fieldConfig.comment());
                        ObfUtil.setFieldValue(clazz, null, value, field.getName());
                        if (!categories.contains(fieldConfig.category()))
                            categories.add(fieldConfig.category());
                        continue;
                    } else if (field.isAnnotationPresent(ConfigFloat.class) && field.getType() == float.class) {
                        ConfigFloat fieldConfig = field.getAnnotation(ConfigFloat.class);
                        float value = config.getFloat(!Strings.isNullOrEmpty(fieldConfig.nameOverride()) ? fieldConfig.nameOverride() : field.getName(), fieldConfig.category(), fieldConfig.defaultValue(), fieldConfig.minValue(), fieldConfig.maxValue(), fieldConfig.comment());
                        ObfUtil.setFieldValue(clazz, null, value, field.getName());
                        if (!categories.contains(fieldConfig.category()))
                            categories.add(fieldConfig.category());
                        continue;
                    } else if (field.isAnnotationPresent(ConfigBoolean.class) && field.getType() == boolean.class) {
                        ConfigBoolean fieldConfig = field.getAnnotation(ConfigBoolean.class);
                        boolean value = config.getBoolean(!Strings.isNullOrEmpty(fieldConfig.nameOverride()) ? fieldConfig.nameOverride() : field.getName(), fieldConfig.category(), fieldConfig.defaultValue(), fieldConfig.comment());
                        ObfUtil.setFieldValue(clazz, null, value, field.getName());
                        if (!categories.contains(fieldConfig.category()))
                            categories.add(fieldConfig.category());
                        continue;
                    } else if (field.isAnnotationPresent(ConfigString.class) && field.getType() == String.class) {
                        ConfigString fieldConfig = field.getAnnotation(ConfigString.class);
                        String value = config.getString(!Strings.isNullOrEmpty(fieldConfig.nameOverride()) ? fieldConfig.nameOverride() : field.getName(), fieldConfig.category(), fieldConfig.defaultValue(), fieldConfig.comment());
                        ObfUtil.setFieldValue(clazz, null, value, field.getName());
                        if (!categories.contains(fieldConfig.category()))
                            categories.add(fieldConfig.category());
                        continue;
                    }  else if (field.isAnnotationPresent(ConfigStringArray.class) && field.getType() == String[].class) {
                        ConfigStringArray fieldConfig = field.getAnnotation(ConfigStringArray.class);
                        String[] value = config.getStringList(!Strings.isNullOrEmpty(fieldConfig.nameOverride()) ? fieldConfig.nameOverride() : field.getName(), fieldConfig.category(), fieldConfig.defaultValue(), fieldConfig.comment());
                        ObfUtil.setFieldValue(clazz, null, value, field.getName());
                        if (!categories.contains(fieldConfig.category()))
                            categories.add(fieldConfig.category());
                        continue;
                    }
                }
            }
        }
    }

    public List<IConfigElement> getConfigElements() {
        List<IConfigElement> configElements = Lists.newArrayList();
        if (categories.size() > 0) {
            for (String category : categories) {
                configElements.add(new ConfigElement(config.getCategory(category)));
            }
        }
        return configElements;
    }

    public String getTitle() {
        return config.toString();
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.modID.equalsIgnoreCase(modid))
            loadConfig();
    }
}
