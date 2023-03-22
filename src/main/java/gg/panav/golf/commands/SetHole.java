package gg.panav.golf.commands;

import gg.panav.golf.Golf;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class SetHole implements CommandExecutor {

    private final Golf main = Golf.instance;
    private Location holeLoc;
    private Material originalBlock;
    private int particleRing;


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        if (holeLoc != null){
            resetHole();
        }

        Location playerLoc = player.getLocation();
        holeLoc = playerLoc.add(0, -1, 0);
        originalBlock = holeLoc.getBlock().getType();
        holeLoc.getBlock().setType(Material.REINFORCED_DEEPSLATE);

        Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
            createHole();
        });
        return true;
    }

    public Location getHoleLoc() {
        return holeLoc;
    }


    private void createHole() {
        particleRing = new BukkitRunnable() {
            int angle = 0;
            Location centre = getHoleLoc().add(0, 1, 0);

            @Override
            public void run() {

                angle += 5;
                double radian = Math.toRadians(angle);

                double LocX = centre.getX() + Math.cos(radian);
                double LocZ = centre.getZ() + Math.sin(radian);

                centre.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, LocX, centre.getY(), LocZ, 10);

            }
        }.runTaskTimer(main, 0, 1).getTaskId();

    }

    public void resetHole(){
        if (holeLoc != null){holeLoc.add(0, -1, 0).getBlock().setType(originalBlock);}
        Bukkit.getScheduler().cancelTask(particleRing);
        holeLoc = null;
    }
}




