package fr.supermax_8.skinlib;

import java.util.concurrent.ConcurrentHashMap;

public class SkinManager {

    private static final ConcurrentHashMap<String, Skin> skins = new ConcurrentHashMap<>();


    public static void addSkin(String str, Skin skin) {
        skins.put(str, skin);
    }

    public static void removeSkin(String str) {
        skins.remove(str);
    }

    public static ConcurrentHashMap<String, Skin> getSkins() {
        return skins;
    }

}