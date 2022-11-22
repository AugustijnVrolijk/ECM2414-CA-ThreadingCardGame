import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        TestCardGame.class,
        TestPlayer.class,
        TestCard.class
})

public class TestSuite {
}