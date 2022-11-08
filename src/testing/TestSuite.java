package testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({TestCard.class, TestCardGame.class})
public class TestSuite {
    // empty , as the class is just a holder
    // for the annotations above
}
