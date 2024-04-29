package plugin.enemydown.command;

import com.google.errorprone.annotations.DoNotMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import plugin.enemydown.Enemydown;


class EnemyDownCommandTest {

  @Mock
  Enemydown enemydown;

  @Test
  void getDifficultyに渡す引数のargsの最初の文字列がeasyの時にeasyの文字列が返ること(){
    EnemyDownCommand sut = new EnemyDownCommand(enemydown);
    Player player = Mockito.mock(Player.class);


    String actual = sut.getDifficulty(player,new String[]{"easy"});
    Assertions.assertEquals("easy",actual);

  }
}