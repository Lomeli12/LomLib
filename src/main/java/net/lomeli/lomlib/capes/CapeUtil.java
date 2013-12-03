package net.lomeli.lomlib.capes;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.libs.LibraryStrings;
import net.lomeli.lomlib.util.XMLUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureObject;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class CapeUtil {
    private static CapeUtil instance = new CapeUtil();

    private HashMap<String, ResourceLocation> capeResources;
    private HashMap<String, ThreadDownloadImageData> capes;

    private CapeUtil() {
        capeResources = new HashMap<String, ResourceLocation>();
        capes = new HashMap<String, ThreadDownloadImageData>();
    }

    public static CapeUtil getInstance() {
        if(instance == null)
            instance = new CapeUtil();
        return instance;
    }

    public void readXML() {
        if(FMLCommonHandler.instance().getSide() != Side.CLIENT)
            return;
        try {
            URL xmlURL = new URL(LibraryStrings.CAPE_XML);
            InputStream xml = xmlURL.openStream();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xml);

            doc.getDocumentElement().normalize();

            NodeList list = doc.getChildNodes().item(0).getChildNodes();
            for(int i = 1; i <= list.getLength(); i++) {
                String nodeName = list.item(i).getNodeName();
                String capeURL = list.item(i).getTextContent();
                if(nodeName != "#text" && capeURL != "")
                    giveUserCape(nodeName, capeURL);
            }
        }catch(Exception e) {
        }

        TickRegistry.registerTickHandler(new CapesTickHandler(), Side.CLIENT);
    }

    public void giveUserCape(String user, String cape) {
        if(capeResources.get(user) == null)
            capeResources.put(user, new ResourceLocation("LomLib/" + user));
        if(capes.get(user) == null)
            capes.put(user, makeDownloadThread(capeResources.get(user), cape, null, new CapeBuffer()));
    }

    public void giveUserCape(String user) {
        giveUserCape(user, capeURL(user));
    }

    public ResourceLocation getUserResource(String user) {
        ResourceLocation resource = null;
        try {
            resource = capeResources.get(user);
        }catch(Exception e) {
        }
        return resource;
    }

    public ThreadDownloadImageData getUserCape(String user) {
        ThreadDownloadImageData image = null;
        try {
            image = capes.get(user);
        }catch(Exception e) {
        }
        return image;
    }

    public static String capeURL(String user) {
        String url;
        url = XMLUtil.getString(LibraryStrings.CAPE_XML, user);
        return url;
    }

    public static ThreadDownloadImageData makeDownloadThread(ResourceLocation par0ResourceLocation, String par1Str,
            ResourceLocation par2ResourceLocation, IImageBuffer par3IImageBuffer) {
        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();

        TextureObject object = new ThreadDownloadImageData(par1Str, par2ResourceLocation, par3IImageBuffer);
        // Binds ResourceLocation to this.
        texturemanager.loadTexture(par0ResourceLocation, object);

        return (ThreadDownloadImageData) object;
    }

}
