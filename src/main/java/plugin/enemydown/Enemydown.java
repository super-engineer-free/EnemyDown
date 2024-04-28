package plugin.enemydown;

import java.net.http.WebSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Enemy;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.enemydown.command.EnemyDownCommand;
import plugin.enemydown.command.EnemySpawnCommand;

public final class Enemydown extends JavaPlugin  {


    @Override
    public void onEnable() {
        EnemyDownCommand enemyDownCommand = new EnemyDownCommand(this);
        Bukkit.getPluginManager().registerEvents( enemyDownCommand, this);
        getCommand("enemyDown").setExecutor(enemyDownCommand);
        EnemySpawnCommand enemySpawnCommand = new EnemySpawnCommand();
        Bukkit.getPluginManager().registerEvents(enemySpawnCommand, this);
        getCommand("enemyspawn").setExecutor(enemySpawnCommand);

    }
}
