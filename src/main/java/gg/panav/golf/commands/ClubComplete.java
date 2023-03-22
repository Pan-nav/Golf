package gg.panav.golf.commands;

import gg.panav.golf.Golf;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class ClubComplete implements TabCompleter {

    Golf main = Golf.instance;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> results = main.getGolfClubs();


        return results;
    }
}
