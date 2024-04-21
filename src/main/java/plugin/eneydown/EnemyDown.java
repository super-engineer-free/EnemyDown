package plugin.eneydown;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.eneydown.command.EnemyDownCommand;

public  class EnemyDown extends JavaPlugin  {
    @Override
    public void onEnable() {
        EnemyDownCommand enemyDownCommand = new EnemyDownCommand(this);
        Bukkit.getPluginManager().registerEvents( enemyDownCommand,this);
        getCommand("enemyDown").setExecutor( enemyDownCommand);
    }
}