package sample;

import org.junit.Assert;
import org.junit.Test;

public class TestUnitaire {

    @Test
    public void testEqualsCouleur() {
        Colors white = Colors.WHITE;
        Colors black = Colors.BLACK;
        Assert.assertEquals(true , white.equals(white));
        Assert.assertEquals(true , black.equals(black));
        Assert.assertEquals(false , white.equals(black));
        Assert.assertEquals(false , black.equals(white));
    }



}
