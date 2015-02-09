package net.lomeli.lomlib.util.entity;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class SimpleEggInfo {
    public final int primaryColor;
    public final int secondaryColor;
    public final Class<? extends Entity> entityClass;
    public final String unlocalizedName;
    
    public SimpleEggInfo(Class<? extends Entity> entityClass, int primaryColor, int secondaryColor, String unlocalized) {
        this.entityClass = entityClass;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.unlocalizedName = unlocalized;
    }
    
    public Entity createEntity(World world) {
        Entity entity = null;
        try {
            if (entityClass != null)
                entity = (Entity) entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {world});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }
}
