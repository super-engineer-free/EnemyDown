package plugin.enemydown.mapper.data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * プレイヤーのスコア情報を扱うオブジェクト。
 * DBに存在するテーブルと連動する。
 */
@Getter
@Setter
@NoArgsConstructor
public class PlayerScore {

  private int id;
  private  String playerName;
  private static int score;
  private  String difficulty;
  private LocalDateTime registered_at;


public  PlayerScore(String playerName,int score,String difficulty) {
  this.playerName =playerName;
  PlayerScore.score =score;
  this.difficulty = difficulty;
}


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

  public LocalDateTime getRegistered_at() {
    return registered_at;
  }
}
