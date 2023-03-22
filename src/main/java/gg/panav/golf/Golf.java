package gg.panav.golf;

import gg.panav.golf.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class Golf extends JavaPlugin {

    public static Golf instance;
    SetHole setHole;
    SetBall setBall;
    HitListener hitListener;

    public static NamespacedKey key;

    private MySQL database;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Database();
        setHole = new SetHole();
        setBall = new SetBall();
        hitListener = new HitListener();

        key = new NamespacedKey(this, "clubValue");

        //registering commands
        getCommand("sethole").setExecutor(setHole);
        getCommand("spawnball").setExecutor(setBall);
        getCommand("clubs").setExecutor(new ClubCMD());
        getCommand("clubs").setTabCompleter(new ClubComplete());
        getCommand("score").setExecutor(new ScoreCMD());

        //registering events
        Bukkit.getPluginManager().registerEvents(hitListener, this);


    }

    @Override
    public void onDisable(){
        database.disconnect();
        setHole.resetHole();
        setBall.resetBall();
    }

    //MySQL
    public void Database(){
        //database
        database = new MySQL();
        try {
            database.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //get the instance of SetHole class
    public SetHole getSetHole(){ return  setHole;}

    //get the instance of SetBall class
    public SetBall getSetBall(){ return setBall;}

    //get the list of clubs
    public List<String> getGolfClubs(){

        List<String> clubs = new ArrayList<>();

        ConfigurationSection clubsSection = getConfig().getConfigurationSection("Golf-Clubs");

        for (String club : clubsSection.getKeys(false)){
            clubs.add(club);
        }

        return clubs;
    }

    //get mysql class
    public MySQL getDatabase(){return database;}




}
