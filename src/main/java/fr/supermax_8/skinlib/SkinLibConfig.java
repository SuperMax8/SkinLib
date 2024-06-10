package fr.supermax_8.skinlib;

import org.mineskin.MineskinClient;
import org.mineskin.SkinOptions;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class SkinLibConfig {

    private static final File skinsDirectory = new File(SkinLib.getInstance().getDataFolder(), "skins");

    public static void load() {
        SkinManager.getSkins().clear();

        int pngCount = 0;
        if (!skinsDirectory.exists()) {
            skinsDirectory.mkdirs();
            return;
        }
        MineskinClient client = new MineskinClient("MyUserAgent");
        for (File f : getFilesRecursivly(skinsDirectory)) {
            String fileName = f.getName();
            if (!fileName.endsWith(".png")) {
                SkinLib.log("§4" + fileName + " is not a png file !");
                continue;
            }
            try {
                String name = fileName.replace(".png", "");
                client.generateUpload(f).thenAccept(skin -> {
                    SkinManager.addSkin(name, new Skin(skin.data.texture.url));
                });
                pngCount++;
            } catch (Exception e) {
                SkinLib.log("§4Problem with file " + fileName + " !");
                e.printStackTrace();
            }
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