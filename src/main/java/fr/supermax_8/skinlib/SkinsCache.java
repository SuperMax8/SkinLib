package fr.supermax_8.skinlib;

import lombok.Getter;
import org.mineskin.data.Skin;

import java.util.HashMap;

@Getter
public class SkinsCache {

    private final HashMap<String, SkinCache> cache = new HashMap<>();


    public record SkinCache(String imgHash, Skin skin) {
    }

}