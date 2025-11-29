package fr.supermax_8.skinlib;

import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

public class SkinManager {

    @Getter
    private static final ConcurrentHashMap<String, SkinCache> skins = new ConcurrentHashMap<>();


    public static void addSkin(String str, SkinCache skin) {
        skins.put(str, skin);
    }

    public static void removeSkin(String str) {
        skins.remove(str);
    }

}