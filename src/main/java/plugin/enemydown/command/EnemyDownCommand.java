package plugin.enemydown.command;

import com.sun.tools.javac.Main;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SplittableRandom;
import java.util.concurrent.CompletionStage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import plugin.enemydown.Enemydown;
import plugin.enemydown.deta.PlayerScore;

/**
 * 制限時間内にランダムで出現する敵を倒して、スコアを獲得するゲームを起動するコマンドです。
 * スコアは敵によって変わり、倒せた敵の合計によってスコアが変動します。
 * 結果は
 */

public class EnemyDownCommand extends BaseCommand implements Listener, org.bukkit.event.Listener {

  public static final int GAME_TIME = 20;
  private final Enemydown main;
  private final List<PlayerScore> playerScoreList = new ArrayList<>();
  private List<Entity> spawnEntityList = new ArrayList<>();

  public EnemyDownCommand(Enemydown main) {
    this.main = main;
  }

  @Override
  public boolean onExecutePlayerCommand(Player player) {
    PlayerScore nowPlayerScore = getPlayerSore(player);

    initPlayerStatus(player);

    gamePlay(player, nowPlayerScore);
    return true;
  }

  @Override
  public boolean onExecuteNPCCommand(CommandSender sender) {
    return false;
  }

  @EventHandler
  public void onEnemyDeath(EntityDeathEvent e) {
    LivingEntity enemy = e.getEntity();
    Player player = enemy.getKiller();

    if (Objects.isNull(player) || spawnEntityList.stream()
        .noneMatch(entity -> entity.equals(enemy))) {
      return;
    }

    playerScoreList.stream()
        .filter(p -> p.getPlayerName().equals(player.getName()))
        .findFirst()
        .ifPresent(p -> {
          int point = switch (enemy.getType()) {
            case ZOMBIE -> 10;
            case SKELETON, WITCH -> 20;
            default -> 0;
          };

          p.setScore(p.getScore() + point);
          player.sendMessage("敵を倒した! 現在のスコアは" + p.getScore() + "点!");
        });
  }

  /**
   * 現在実行しているプレイヤーのスコア情報を習得する。
   *
   * @param player 　コマンドを実行したプレイヤー
   * @return 現在実行しているプレイヤーのスコア情報
   */
  private PlayerScore getPlayerSore(Player player) {
    PlayerScore playerScore = new PlayerScore(player.getName());
    if (playerScoreList.isEmpty()) {
      playerScore = addNewPlayer(player);
    } else {
      playerScore = playerScoreList.stream()
          .findFirst()
          .map(ps -> ps.getPlayerName().equals(player.getName())
              ? ps
              : addNewPlayer(player)).orElse(playerScore);
    }
    playerScore.setGameTime(GAME_TIME);
    playerScore.setScore(0);
    return playerScore;
  }


  /**
   * @param player &#064;return新規プレイヤー
   */

  private PlayerScore addNewPlayer(Player player) {
    PlayerScore newPlayer = new PlayerScore(player.getName());
    playerScoreList.add(newPlayer);
    return newPlayer;
  }

  /**
   * ゲームを始める前にプレイヤーの状態を設定する。 体力と空腹度を最大にして、装備はネザライト一式になる。
   *
   * @param player 　コマンドを実行したプレイヤー
   */

  private void initPlayerStatus(Player player) {
    player.setHealth(20);
    player.setFoodLevel(20);

    PlayerInventory inventory = player.getInventory();
    inventory.setHelmet(new ItemStack(Material.NETHERITE_HELMET));
    inventory.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
    inventory.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
    inventory.setBoots(new ItemStack(Material.NETHERITE_BOOTS));
    inventory.setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD));
  }

  /**
   * ゲームを実行します。規定の時間内に敵を倒すとスコアが加算されます。合計スコアを時間過後に表示します。
   *
   * @param player         　コマンドを実行したプレイヤー
   * @param nowPlayerScore 　プレイヤースコア情報
   */
  private void gamePlay(Player player, PlayerScore nowPlayerScore) {
    Bukkit.getScheduler().runTaskTimer(main, Runnable -> {
      if (nowPlayerScore.getGameTime() <= 0) {
        Runnable.cancel();

        player.sendTitle("ゲームが終了しました。",
            nowPlayerScore.getPlayerName() + "合計 " + nowPlayerScore.getScore() + "点!",
            0, 60, 0);

        spawnEntityList.forEach(Entity::remove);
        spawnEntityList = new ArrayList<>();
        return;
      }
      Entity spawnEntity = player.getWorld().spawnEntity(getEnemySpawnLocation(player), getEnemy());
      spawnEntityList.add(spawnEntity);
      nowPlayerScore.setGameTime(nowPlayerScore.getGameTime() - 5);
    }, 0, 5 * 20);
  }


  /**
   * 敵の出現場所を取得します。 出現エリアはX軸とZ軸は自分の位置からプラス、ランダムで-10~9の値が設定されます。 Y軸はプレイヤーと同じ位置になります。
   *
   * @param player コマンドを実行したプレイヤー &#064;return　敵の出現場所
   */
  private Location getEnemySpawnLocation(Player player) {
    Location playerlocation = player.getLocation();
    int randomX = new SplittableRandom().nextInt(20) - 10;
    int randomZ = new SplittableRandom().nextInt(20) - 10;

    double x = playerlocation.getX() + randomX;
    double y = playerlocation.getY();
    double z = playerlocation.getZ() + randomZ;

    return new Location(player.getWorld(), x, y, z);
  }

  /**
   * ランダムで敵を抽選して、その結果の敵を取得します。 &#064;return　敵
   */
  private static EntityType getEnemy() {
    List<EntityType> entityList = List.of(EntityType.ZOMBIE, EntityType.SKELETON, EntityType.WITCH);
    return entityList.get(new SplittableRandom().nextInt(entityList.size()));
  }

  @Override
  public void onOpen(WebSocket webSocket) {
    Listener.super.onOpen(webSocket);
  }
}