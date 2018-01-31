package sample;

import org.junit.Assert;
import org.junit.Test;

public class TestUnitaire {

    @Test
    public void testEqualsCouleur() {
        Couleur blanc = Couleur.BLANC;
        Couleur noir = Couleur.NOIR;
        Assert.assertEquals(true , blanc.equals(blanc));
        Assert.assertEquals(true , noir.equals(noir));
        Assert.assertEquals(false , blanc.equals(noir));
        Assert.assertEquals(false , noir.equals(blanc));
    }



}
