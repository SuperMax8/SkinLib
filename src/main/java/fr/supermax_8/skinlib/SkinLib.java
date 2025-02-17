package fr.supermax_8.skinlib;

import fr.supermax_8.skinlib.commands.SkinLibCommand;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;

public final class SkinLib extends JavaPlugin {

    @Getter
    private static SkinLib instance;

    @Override
    public void onEnable() {
        instance = this;
        CompletableFuture.runAsync(SkinLibConfig::load);
        getCommand("skinlib").setExecutor(new SkinLibCommand());
    }

    @Override
    public void onDisable() {
    }


    public static void log(String s) {
        System.out.println(s);
    }

}