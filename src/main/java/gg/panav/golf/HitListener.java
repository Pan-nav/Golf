package gg.panav.golf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;


import java.sql.SQLException;
import java.util.Objects;

import static gg.panav.golf.MySQL.database;


public class HitListener implements Listener {

    Golf main = Golf.instance;

    Player Currentplayer;

    @EventHandler
    public void onHit(PlayerInteractAtEntityEvent  e){
        if (!Objects.equals(e.getRightClicked().getCustomName(), String.valueOf(ChatColor.AQUA) + ChatColor.BOLD + "Golf Ball")) return;
        Player player = e.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (!itemStack.hasItemMeta()) return;

        if(itemStack.getItemMeta().getPersistentDataContainer().has(Golf.key, PersistentDataType.DOUBLE)) {
            e.getRightClicked().setVelocity(player.getLocation().getDirection()
                .multiply(itemStack.getItemMeta().getPersistentDataContainer().get(Golf.key, PersistentDataType.DOUBLE)));
        }

        Currentplayer = player;
        startCheck(e.getRightClicked());
    }


    public void startCheck(Entity ball) {
        new BukkitRunnable() {
            @Override
            public void run() {


                    Location holeLoc = main.getSetHole().getHoleLoc();
                    Location ballLoc = ball.getLocation();

                    if (holeLoc == null)return;
                    if (!(Math.round(holeLoc.distance(ballLoc)) <= 3)) return;

                    if (Currentplayer != null) {
                        try {
                            database.updatePlayerScore(Currentplayer);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        Bukkit.getScheduler().runTask(main,  ()->{
                            main.getSetHole().resetHole();
                            main.getSetBall().resetBall();
                        });

                        cancel();
                    }
                }

            }.runTaskTimerAsynchronously(main, 0, 20);
        };
    }

