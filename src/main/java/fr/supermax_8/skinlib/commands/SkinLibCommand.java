package fr.supermax_8.skinlib.commands;

import fr.supermax_8.skinlib.SkinLibAPI;
import fr.supermax_8.skinlib.SkinLibConfig;
import fr.supermax_8.skinlib.SkinManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

public class SkinLibCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!sender.hasPermission("skinlib.use")) return false;
        try {
            switch (args[0].toLowerCase()) {
                case "list":
                    sender.sendMessage("§7§lSkins List:");
                    for (String skinName : SkinManager.getSkins().keySet())
                        sender.sendMessage("§8- §f" + skinName);
                    break;
                case "addplayer":
                    break;
                case "addurl":
                    break;
                case "out":
                    String inputSkin = args[1];
                    System.out.println(inputSkin + ":");
                    System.out.println(SkinManager.getSkins().get(inputSkin).data.texture.url);
                    break;
                case "giveurl":
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§cYour not a player");
                        return false;
                    }
                    Player p1 = (Player) sender;
                    String b64 = args[1];
                    ItemStack itma = SkinLibAPI.itemFromUrl(b64);
                    p1.getInventory().addItem(itma);
                    p1.sendMessage("Give " + b64 + " !");
                    break;
                case "give":
                    if (!(sender instanceof Player)) {
                        sender.sendMessage("§cYour not a player");
                        return false;
                    }
                    Player p = (Player) sender;
                    String inputSkinId = args[1];
                    ItemStack itm = SkinLibAPI.createHead(inputSkinId, inputSkinId);
                    p.getInventory().addItem(itm);
                    p.sendMessage("Give " + inputSkinId + " !");
                    break;
                case "reload":
                    CompletableFuture.runAsync(SkinLibConfig::load);
                    break;
            }
        } catch (Exception e) {
            sendHelp(sender);
        }
        return false;
    }


    public void sendHelp(CommandSender sender) {
        sender.sendMessage(
                "§7SkinLib",
                "§f/skinlib list §7show the skin registred",
                "§f/skinlib addPlayer <PlayerName> §7add skin from player",
                "§f/skinlib addUrl <URL> §7add skin from image url"
        );
    }


}