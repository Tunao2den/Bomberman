package server;

public class PlayerData {
    public boolean logged;
    public boolean alive;
    public int x;
    public int y; //current coordinate
    public int numberOfBombs;

    PlayerData(int x, int y) {
        this.x = x;
        this.y = y;
        this.logged = false;
        this.alive = false;
        this.numberOfBombs = 1; // for 2 bombs, each bomb needs to be handled in a separate thread.
    }
}
