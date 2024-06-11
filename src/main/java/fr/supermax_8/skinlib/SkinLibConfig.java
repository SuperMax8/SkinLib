package fr.supermax_8.skinlib;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.mineskin.MineskinClient;
import org.mineskin.SkinOptions;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class SkinLibConfig {

    private static final File skinsDirectory = new File(SkinLib.getInstance().getDataFolder(), "skins");
    private static SkinCache cache;

    public static void load() {
        SkinManager.getSkins().clear();

        int pngCount = 0;
        if (!skinsDirectory.exists()) {
            skinsDirectory.mkdirs();
            return;
        }
        File cacheFile = new File(SkinLib.getInstance().getDataFolder(), "cache.json");
        if (cacheFile.exists()) {
            try (FileReader reader = new FileReader(cacheFile)){
                cache = new Gson().fromJson(reader, SkinCache.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else cache = new SkinCache();

        MineskinClient client = new MineskinClient("MyUserAgent");
        for (File f : getFilesRecursivly(skinsDirectory)) {
            String fileName = f.getName();
            if (!fileName.endsWith(".png")) {
                SkinLib.log("§4" + fileName + " is not a png file !");
                continue;
            }
            try {
                String name = fileName.replace(".png", "");
                if (cache.getCache().containsKey(fileName)) {
                    SkinManager.addSkin(name, new Skin(cache.getCache().get(fileName)));
                    continue;
                }
                client.generateUpload(f).thenAccept(skin -> {
                    cache.getCache().put(fileName, skin.data.texture.url);
                    SkinManager.addSkin(name, new Skin(skin.data.texture.url));
                });
                pngCount++;
            } catch (Exception e) {
                SkinLib.log("§4Problem with file " + fileName + " !");
                e.printStackTrace();
            }
        }
        try (FileWriter writer = new FileWriter(cacheFile)){
            writer.write(new Gson().toJson(cache));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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