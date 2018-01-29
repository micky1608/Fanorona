package sample;

public enum Direction {
    NORD(0,1),
    NORDEST(1,1),
    EST(1,0),
    SUDEST(1,-1),
    SUD(0,-1),
    SUDOUEST(-1,-1),
    OUEST(-1,0),
    NORDOUEST(-1,1);

    private int x;
    private int y;

    Direction(int x , int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
