package BattleShip;

import javafx.scene.Parent;
//Ship class, used for setting variables of a ship such as health, position, ship size, and orientation
public class Ship extends Parent {

    public int type;
    public boolean vertical = true;
    public int x, y;
    public int health;

    public Ship(int type, boolean vertical) {
        this.type = type;
        this.vertical = vertical;
        health = type;
    }

    public void hit() { health--; }

    public boolean isAlive() {
        return health > 0;
    }
}
