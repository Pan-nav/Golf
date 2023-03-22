package gg.panav.golf.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetBall implements CommandExecutor {
    private ArmorStand stand;
    private Location ballLoc;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)){
            return true;
        }

        ballLoc = player.getLocation();

        stand = (ArmorStand) player.getWorld().spawnEntity(ballLoc, EntityType.ARMOR_STAND);
        stand.setInvisible(true);
        stand.setInvulnerable(true);
        stand.setSmall(true);
        stand.setHelmet(new ItemStack(Material.SNOWBALL));
        stand.setCustomNameVisible(true);
        stand.setCustomName(String.valueOf(ChatColor.AQUA) + ChatColor.BOLD + "Golf Ball");

        return true;
    }

    public ArmorStand getBall(){ return stand;}

    public void resetBall(){
        getBall().remove();
        ballLoc = null;
    }
}
