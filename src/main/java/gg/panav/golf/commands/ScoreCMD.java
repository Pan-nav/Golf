package gg.panav.golf.commands;

import gg.panav.golf.Golf;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class ScoreCMD implements CommandExecutor {

    Golf main = Golf.instance;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) return true;
        if (!(args.length > 0)) return true;
        if (Bukkit.getPlayer(args[0]) == null) return true;

        try {
            player.sendMessage(main.getDatabase().getScore(Bukkit.getPlayer(args[0])));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
