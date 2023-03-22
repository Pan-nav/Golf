package gg.panav.golf.commands;

import gg.panav.golf.Golf;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class ClubCMD implements CommandExecutor {

    Golf main = Golf.instance;

    List<String> clubsList = main.getGolfClubs();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (!(sender instanceof Player player)){return true;}
        if (!(args.length > 0)){return true;}

        String clubName = args[0];
        NamespacedKey key = Golf.key;

        if (clubsList.contains(clubName)){

            ItemStack club = new ItemStack(Material.valueOf(main.getConfig().getString("Golf-Clubs." + clubName + ".item")));
            double power = main.getConfig().getDouble("Golf-Clubs." + clubName + ".strength");
            ItemMeta meta = club.getItemMeta();
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(key, PersistentDataType.DOUBLE, power * 10);
            club.setItemMeta(meta);
            player.getInventory().addItem(club);
        } else {
            player.sendMessage(ChatColor.AQUA + "The following Club does not exist.");
            return true;
        }

        return true;
    }
}
