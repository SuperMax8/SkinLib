package fr.supermax_8.skinlib;

import lombok.Getter;
import org.mineskin.data.Skin;
import org.mineskin.data.TextureInfo;
import org.mineskin.data.Variant;
import org.mineskin.data.Visibility;

import java.util.HashMap;

@Getter
public class SkinsCache {

    private final HashMap<String, SkinCache> cache = new HashMap<>();

    public record SkinCache(String imgHash,
                            String uuid,
                            String name,
                            Variant variant,
                            Visibility visibility,
                            TextureInfo texture,
                            int views,
                            boolean duplicate) implements Skin {
    }

}