package fr.supermax_8.skinlib;

import com.google.gson.Gson;
import fr.supermax_8.skinlib.utils.ImageUtils;
import org.mineskin.MineskinClient;
import org.mineskin.data.Skin;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class SkinLibConfig {

    private static final File skinsDirectory = new File(SkinLib.getInstance().getDataFolder(), "skins");
    private static SkinsCache cache;

    public static void load() {
        SkinManager.getSkins().clear();

        int pngCount = 0;
        if (!skinsDirectory.exists()) {
            skinsDirectory.mkdirs();
            return;
        }
        File cacheFile = new File(SkinLib.getInstance().getDataFolder(), "cache.json");
        if (cacheFile.exists()) {
            try (FileReader reader = new FileReader(cacheFile)) {
                SkinLib.log("Loading skin cache...");
                cache = new Gson().fromJson(reader, SkinsCache.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else cache = new SkinsCache();

        MineskinClient client = new MineskinClient("MyUserAgent");
        for (File f : getFilesRecursivly(skinsDirectory)) {
            String fileName = f.getName();
            if (!fileName.endsWith(".png")) {
                SkinLib.log("§4" + fileName + " is not a png file !");
                continue;
            }
            try {
                String hash = ImageUtils.generateImageHash(f);
                String name = fileName.replace(".png", "");
                if (cache.getCache().containsKey(name) && cache.getCache().get(name).imgHash().equals(hash)) {
                    SkinManager.addSkin(name, cache.getCache().get(name).skin());
                } else {
                    Skin skin = client.generateUpload(f).get();
                    cache.getCache().put(name, new SkinsCache.SkinCache(hash, skin));
                    SkinManager.addSkin(name, skin);
                    System.out.println("New skin upload generated ! " + name);
                }
                pngCount++;
            } catch (Exception e) {
                SkinLib.log("§4Problem with file " + fileName + " !");
                e.printStackTrace();
            }
        }
        try (FileWriter writer = new FileWriter(cacheFile)) {
            writer.write(new Gson().toJson(cache));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SkinLib.log("Cache size: " + cache.getCache().size());
        SkinLib.log(pngCount + " skins has been registred !");
    }

    public static List<File> getFilesRecursivly(File directory) {
        LinkedList<File> files = new LinkedList<>();
        for (File f : directory.listFiles()) {
            if (f.isDirectory()) files.addAll(getFilesRecursivly(f));
            else files.add(f);
        }
        return files;
    }


}