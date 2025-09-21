package fr.supermax_8.skinlib;

import com.google.gson.Gson;
import fr.supermax_8.skinlib.utils.ImageUtils;
import lombok.Getter;
import org.mineskin.MineskinClient;
import org.mineskin.data.Skin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SkinLibConfig {

    private static final File skinsDirectory = new File(SkinLib.getInstance().getDataFolder(), "skins");
    private static SkinsCache cache;
    private static File cacheFile = new File(SkinLib.getInstance().getDataFolder(), "cache.json");
    @Getter
    public static boolean loading = false;

    public static synchronized void load() {
        loading = true;
        SkinManager.getSkins().clear();

        int pngCount = 0;
        if (!skinsDirectory.exists()) {
            skinsDirectory.mkdirs();
            return;
        }
        if (cacheFile.exists()) {
            try (FileReader reader = new FileReader(cacheFile)) {
                SkinLib.log("Loading skin cache...");
                cache = new Gson().fromJson(reader, SkinsCache.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else cache = new SkinsCache();

        int cacheHit = 0;
        int cacheMiss = 0;
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
                    cacheHit++;
                } else {
                    Skin skin = client.generateUpload(f).get();
                    cache.getCache().put(name, new SkinsCache.SkinCache(hash, skin));
                    SkinManager.addSkin(name, skin);
                    saveCache();
                    SkinLib.log("New skin upload generated ! " + name);
                    cacheMiss++;
                }
                pngCount++;
            } catch (Exception e) {
                SkinLib.log("§4Problem with file §c" + fileName + "because: " + e.getMessage());
                e.printStackTrace();
            }
        }

        saveCache();
        SkinLib.log("Cache size: " + cache.getCache().size());
        SkinLib.log(pngCount + " skins has been registred ! SkinLib is fully loaded! §fCacheHit: §6" + cacheHit + " §fCacheMiss: §6" + cacheMiss);
        loading = false;
    }

    private static void saveCache() {
        try (FileWriter writer = new FileWriter(cacheFile)) {
            writer.write(new Gson().toJson(cache));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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