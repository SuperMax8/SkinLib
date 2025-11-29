package fr.supermax_8.skinlib;

import com.google.gson.Gson;
import fr.supermax_8.skinlib.utils.ImageUtils;
import lombok.Getter;
import org.mineskin.JsoupRequestHandler;
import org.mineskin.MineSkinClient;
import org.mineskin.QueueOptions;
import org.mineskin.data.Skin;
import org.mineskin.data.Visibility;
import org.mineskin.request.GenerateRequest;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SkinLibConfig {

    private static final File skinsDirectory = new File(SkinLib.getInstance().getDataFolder(), "skins");
    private static SkinsCache cache;
    private static File cacheFile = new File(SkinLib.getInstance().getDataFolder(), "cache.json");
    @Getter
    public static boolean loading = false;
    public static String apiKey;
    private static MineSkinClient client;

    private static MineSkinClient getMineSkinClient() {
        if (client == null)
            client = MineSkinClient.builder()
                    .requestHandler(JsoupRequestHandler::new)
                    .userAgent("SkinLibKlumApp/v1.0") // TODO: update this with your own user agent
                    .apiKey(apiKey) // TODO: update this with your own API key (https://account.mineskin.org/keys)
                    /*
                    // Uncomment this if you're on a paid plan with higher concurrency limits
                    .generateQueueOptions(new QueueOptions(Executors.newSingleThreadScheduledExecutor(), 200, 5))
                    */
                    // Use this to automatically adjust the queue settings based on your allowance
                    .generateQueueOptions(QueueOptions.createAutoGenerate())
                    .build();
        return client;
    }

    public static synchronized void load() {
        loading = true;
        SkinManager.getSkins().clear();

        SkinLib.getInstance().saveDefaultConfig();
        apiKey = SkinLib.getInstance().getConfig().getString("mineskin-apikey");
        if (apiKey == null || apiKey.isEmpty()) {
            SkinLib.log("§cCan't init mineskin skins api key is empty, setup it in the config!");
            return;
        }
        int pngCount = 0;
        if (!skinsDirectory.exists()) {
            skinsDirectory.mkdirs();
            return;
        }
        getMineSkinClient();
        if (cacheFile.exists()) {
            try (FileReader reader = new FileReader(cacheFile)) {
                SkinLib.log("Loading skin cache...");
                cache = new Gson().fromJson(reader, SkinsCache.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else cache = new SkinsCache();

        SkinLib.log("Loading skins...");
        int cacheHit = 0;
        int cacheMiss = 0;
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
                    SkinManager.addSkin(name, cache.getCache().get(name));
                    cacheHit++;
                } else {
                    GenerateRequest request = GenerateRequest.upload(f)
                            .name(f.getName())
                            .visibility(Visibility.PUBLIC);
                    Skin skin = getMineSkinClient().generate().submitAndWait(request).get().getSkin();
                    SkinCache skinCache = new SkinCache(
                            hash,
                            skin.uuid(),
                            skin.name(),
                            skin.variant(),
                            skin.visibility(),
                            skin.texture(),
                            skin.views(),
                            skin.duplicate());
                    cache.getCache().put(name, skinCache);
                    SkinManager.addSkin(name, skinCache);
                    saveCache();
                    SkinLib.log("New skin upload generated ! " + name);
                    cacheMiss++;
                }
                pngCount++;
            } catch (Exception e) {
                SkinLib.log("§4Issue with file §c" + fileName + " because: " + e.getMessage());
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
        for (File f : Objects.requireNonNull(directory.listFiles())) {
            if (f.isDirectory()) files.addAll(getFilesRecursivly(f));
            else files.add(f);
        }
        return files;
    }


}