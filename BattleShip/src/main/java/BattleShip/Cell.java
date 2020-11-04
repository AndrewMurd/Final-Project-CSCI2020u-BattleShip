package BattleShip;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.File;
//Cell class mainly for sink and shoot functions, along with coloring a cell when it's clicked on
public class Cell extends Rectangle {
    public int x, y;
    public Ship ship;
    public boolean wasShot = false;
    private Board board;

    private String blast = "src/main/resources/blast.aiff";
    private Media sound = new Media(new File(blast).toURI().toString());
    public MediaPlayer mediaPlayer = new MediaPlayer(sound);

    public Cell(int x, int y, Board board, Ship ship) {
        super(30, 30);
        this.x = x;
        this.y = y;
        this.board = board;
        this.ship = ship;
        setFill(Color.TRANSPARENT);
        setStroke(Color.BLACK);
        setOpacity(0.7);
        mediaPlayer.setVolume(0.01);
    }

    public void shoot() {
        wasShot = true;
        if (ship != null && ship.type != 0) {
            mediaPlayer.setVolume((Main.volume)*2);
            mediaPlayer.play();
            setFill(Color.RED);
            ship.hit();
            if (!ship.isAlive()) {
                setShipSunkColor();
                this.board.ships--;
            }
        } else setFill(Color.BLACK);
    }

    public void setShipSunkColor(){
        //loop through board, set all ships which have 0 health to dark red color
        for(int y = 0; y < 10; y++){
            for(int x = 0; x < 10; x++){
                Cell c = board.getCell(x,y);
                if(c.ship != null)
                {
                    if(c.ship.health == 0){
                        c.setFill(Color.DARKRED);
                    }
                }
            }
        }
    }
}
