package plugin.eneydown;

import java.net.http.WebSocket;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.eneydown.command.EnemyDownCommand;

public  class EnemyDown extends JavaPlugin  {
    @Override
    public void onEnable() {
        EnemyDownCommand enemyDownCommand = new EnemyDownCommand(this);
        Bukkit.getPluginManager().registerEvents( enemyDownCommand,  this);
        getCommand("enemyDown").setExecutor( enemyDownCommand);
    }
    }
