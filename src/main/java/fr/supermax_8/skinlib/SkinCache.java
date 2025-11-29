package fr.supermax_8.skinlib;

import org.mineskin.data.TextureInfo;
import org.mineskin.data.Variant;
import org.mineskin.data.Visibility;

public record SkinCache(String imgHash,
                        String uuid,
                        String name,
                        Variant variant,
                        Visibility visibility,
                        TextureInfo texture,
                        int views,
                        boolean duplicate) {
}