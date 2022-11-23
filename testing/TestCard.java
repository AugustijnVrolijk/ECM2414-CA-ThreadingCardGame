import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

public class TestCard {

    Card testCard1;
    Card testCard2;

    @Before
    public void setUp() {
         testCard1 = new Card(1);
         testCard2 = new Card(256000000);
    }

    @Test
    public void testCard() {
        assert (testCard1.getCardNumber() == 1);
        assert (Objects.equals(testCard2.toString(), "256000000"));
    }

}
