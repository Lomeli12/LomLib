package net.lomeli.lomlib.core.config;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.util.StatCollector;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.lomlib.core.config.annotations.*;
import net.lomeli.lomlib.util.ObfUtil;

/**
 * Totally not copying iChun's config annotation system, nope, not at all.
 */
public class ModConfig {
    protected Configuration config;
    protected String modid;
    protected List<Class> libClasses;
    protected HashMap<String, String> categories;

    public ModConfig(String modid, Configuration config, Object... objects) {
        this.config = config;
        this.modid = modid;
        this.libClasses = Lists.newArrayList();
        if (objects != null && objects.length > 0) {
            for (int i = 0; i < objects.length; i++) {
                Object obj = objects[i];
                if (obj instanceof Class)
                    this.libClasses.add((Class) obj);
                else
                    this.libClasses.add(obj.getClass());
            }
        }
        this.categories = Maps.newHashMap();
    }

    public ModConfig(String modid, File configFile, Object... objects) {
        this(modid, new Configuration(configFile), objects);
    }

    public Configuration getConfig() {
        return config;
    }

    public void loadConfig() {
        if (libClasses != null && libClasses.size() > 0 && config != null) {
            for (int i = 0; i < libClasses.size(); i++)
                readFields(libClasses.get(i));

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
                        if (!Strings.isNullOrEmpty(fieldConfig.categoryComment()))
                            config.addCustomCategoryComment(fieldConfig.category(), StatCollector.translateToLocal(fieldConfig.categoryComment()));
                        int value = config.getInt(!Strings.isNullOrEmpty(fieldConfig.nameOverride()) ? fieldConfig.nameOverride() : field.getName(), fieldConfig.category(), fieldConfig.defaultValue(), fieldConfig.minValue(), fieldConfig.maxValue(), StatCollector.translateToLocal(fieldConfig.comment()));
                        ObfUtil.setFieldValue(clazz, null, value, field.getName());
                        if (!categories.containsKey(fieldConfig.category()))
                            categories.put(fieldConfig.category(), fieldConfig.categoryComment());
                        continue;
                    } else if (field.isAnnotationPresent(ConfigFloat.class) && field.getType() == float.class) {
                        ConfigFloat fieldConfig = field.getAnnotation(ConfigFloat.class);
                        if (!Strings.isNullOrEmpty(fieldConfig.categoryComment()))
                            config.addCustomCategoryComment(fieldConfig.category(), StatCollector.translateToLocal(fieldConfig.categoryComment()));
                        float value = config.getFloat(!Strings.isNullOrEmpty(fieldConfig.nameOverride()) ? fieldConfig.nameOverride() : field.getName(), fieldConfig.category(), fieldConfig.defaultValue(), fieldConfig.minValue(), fieldConfig.maxValue(), StatCollector.translateToLocal(fieldConfig.comment()));
                        ObfUtil.setFieldValue(clazz, null, value, field.getName());
                        if (!categories.containsKey(fieldConfig.category()))
                            categories.put(fieldConfig.category(), fieldConfig.categoryComment());
                        continue;
                    } else if (field.isAnnotationPresent(ConfigBoolean.class) && field.getType() == boolean.class) {
                        ConfigBoolean fieldConfig = field.getAnnotation(ConfigBoolean.class);
                        if (!Strings.isNullOrEmpty(fieldConfig.categoryComment()))
                            config.addCustomCategoryComment(fieldConfig.category(), StatCollector.translateToLocal(fieldConfig.categoryComment()));
                        boolean value = config.getBoolean(!Strings.isNullOrEmpty(fieldConfig.nameOverride()) ? fieldConfig.nameOverride() : field.getName(), fieldConfig.category(), fieldConfig.defaultValue(), StatCollector.translateToLocal(fieldConfig.comment()));
                        ObfUtil.setFieldValue(clazz, null, value, field.getName());
                        if (!categories.containsKey(fieldConfig.category()))
                            categories.put(fieldConfig.category(), fieldConfig.categoryComment());
                        continue;
                    } else if (field.isAnnotationPresent(ConfigString.class) && field.getType() == String.class) {
                        ConfigString fieldConfig = field.getAnnotation(ConfigString.class);
                        if (!Strings.isNullOrEmpty(fieldConfig.categoryComment()))
                            config.addCustomCategoryComment(fieldConfig.category(), StatCollector.translateToLocal(fieldConfig.categoryComment()));
                        String value = config.getString(!Strings.isNullOrEmpty(fieldConfig.nameOverride()) ? fieldConfig.nameOverride() : field.getName(), fieldConfig.category(), fieldConfig.defaultValue(), StatCollector.translateToLocal(fieldConfig.comment()));
                        ObfUtil.setFieldValue(clazz, null, value, field.getName());
                        if (!categories.containsKey(fieldConfig.category()))
                            categories.put(fieldConfig.category(), fieldConfig.categoryComment());
                        continue;
                    } else if (field.isAnnotationPresent(ConfigStringArray.class) && field.getType() == String[].class) {
                        ConfigStringArray fieldConfig = field.getAnnotation(ConfigStringArray.class);
                        if (!Strings.isNullOrEmpty(fieldConfig.categoryComment()))
                            config.addCustomCategoryComment(fieldConfig.category(), StatCollector.translateToLocal(fieldConfig.categoryComment()));
                        String[] value = config.getStringList(!Strings.isNullOrEmpty(fieldConfig.nameOverride()) ? fieldConfig.nameOverride() : field.getName(), fieldConfig.category(), fieldConfig.defaultValue(), StatCollector.translateToLocal(fieldConfig.comment()));
                        ObfUtil.setFieldValue(clazz, null, value, field.getName());
                        if (!categories.containsKey(fieldConfig.category()))
                            categories.put(fieldConfig.category(), fieldConfig.categoryComment());
                        continue;
                    }
                }
            }
        }
    }

    public List<IConfigElement> getConfigElements() {
        List<IConfigElement> configElements = Lists.newArrayList();
        if (categories.size() > 0) {
            for (String catName : categories.keySet()) {
                configElements.add(new ConfigElement(config.getCategory(catName)));
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
