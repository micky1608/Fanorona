package sample;


import org.junit.Assert;
import org.junit.Test;

public class Tests {

    @Test
    public void testCloneBoard() {
        Board board = new Board();
        Board clone = board.clone();
        System.out.println(board + "\n\n" + clone);

        //Assert.assertEquals(true , board.equals(clone));
        //Assert.assertEquals(true , board.getNodes().equals(clone.getNodes()));
        //Assert.assertEquals(false , board == clone);
        //Assert.assertEquals(false , board.getNodes() == clone.getNodes());
    }
}
