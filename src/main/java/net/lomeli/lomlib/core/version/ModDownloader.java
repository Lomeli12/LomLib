package net.lomeli.lomlib.core.version;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

import net.minecraft.util.StatCollector;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.util.ResourceUtil;

public class ModDownloader extends Thread {
    private static final File modsfolder = ResourceUtil.getModsFolder();
    private final File modFile;
    public final String modname, url;
    private final long size;

    public ModDownloader(String modName, String url, long size) {
        this.modname = modName;
        this.url = url;
        this.size = size;
        this.modFile = new File(modsfolder, FilenameUtils.getName(url));
        this.setName("Mod Downloader");
        this.setDaemon(true);
    }

    @Override
    public void run() {
        try {
            downloadFile(new URL(url), modFile, size);
            LomLib.proxy.messageClient(String.format(StatCollector.translateToLocal("message.lomlib.updateFinish"), modname));
            LomLib.logger.logInfo(String.format(StatCollector.translateToLocal("message.lomlib.updateFinish"), modname));
        } catch (Exception e) {
            LomLib.logger.logError("Could not download update!");
            LomLib.logger.logException(e);
        }
    }

    public boolean downloadFile(URL par1URL, File par2File, long size) throws IOException {
        if (par2File.exists()) {
            if (par2File.length() == size)
                return false;
        } else if (!par2File.getParentFile().exists())
            par2File.getParentFile().mkdirs();
        byte[] var5 = new byte[4096];
        URLConnection con = par1URL.openConnection();
        con.setConnectTimeout(15000);
        con.setReadTimeout(15000);
        DataInputStream var6 = new DataInputStream(con.getInputStream());
        DataOutputStream var7 = new DataOutputStream(new FileOutputStream(par2File));
        while (true) {
            int var9;
            if ((var9 = var6.read(var5)) < 0) {
                var6.close();
                var7.close();
                return true;
            }
            var7.write(var5, 0, var9);
        }
    }
}
