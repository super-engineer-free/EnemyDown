package plugin.enemydown.mapper.data;

import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;

/**
 * プレイヤーのスコア情報を扱うオブジェクト。
 * DBに存在するテーブルと連動する。
 */
@Getter
@Setter
public class PlayerScore {

  private int id;
  private String playerName;
  private static int score;
  private String difficulty;
  private String registered_at;




  public int getId() {
    return id;
  }

  public String getPlayerName() {
    return playerName;
  }

  public static int getScore() {
    return score;
  }


  public String getDifficulty() {
    return difficulty;
  }

  public String getRegistered_at() {
    return registered_at;
  }

}