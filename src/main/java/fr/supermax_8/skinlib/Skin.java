package fr.supermax_8.skinlib;

import org.bukkit.inventory.meta.SkullMeta;

public class Skin {

    private final String url;
    private final SkullMeta baseSkullMeta;


    public Skin(String url) {
        this.url = url;
        baseSkullMeta = (SkullMeta) SkinLibAPI.itemFromUrl(url).getItemMeta();
    }


    public String getUrl() {
        return url;
    }

    public SkullMeta getCloneSkullMeta() {
        return baseSkullMeta.clone();
    }

}