package net.lomeli.lomlib.client.render;

public class IconConnectedReverse extends IconConnected {

    public IconConnectedReverse(IconConnected connected){
        super(connected.icons[0], connected.icons[1], connected.icons[2], connected.icons[3], connected.icons[4]);
    }
    
    @Override
    public float getMinV() {
        return super.getMaxV();
    }

    @Override
    public float getMaxV() {
        return super.getMinV();
    }
}
