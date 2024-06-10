package fr.supermax_8.skinlib;

import fr.supermax_8.skinlib.commands.SkinLibCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkinLib extends JavaPlugin {

    private static SkinLib instance;

    @Override
    public void onEnable() {
        instance = this;
        SkinLibConfig.load();
        getCommand("skinlib").setExecutor(new SkinLibCommand());
    }

    @Override
    public void onDisable() {
    }


    public static SkinLib getInstance() {
        return instance;
    }


    public static void log(String s) {
        System.out.println(s);
    }

}