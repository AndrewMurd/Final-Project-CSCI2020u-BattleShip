package BattleShip;

import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

//Board class to initialize the board and add cells and the ships
public class Board {
    VBox playerGrid = new VBox();
    public int ships = 5;
    public Ship[] shipList = new Ship[5];

    BackgroundImage water = new BackgroundImage(new Image("images/water.jpg",400,400,false,true),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
    //board action if clicked
    public Board(EventHandler<? super MouseEvent> handler) {
        for (int y = 0; y < 10; y++) {
            HBox row = new HBox();
            for (int x = 0; x < 10; x++) {
                Cell c = new Cell(x, y, this, null);
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }
            playerGrid.setBackground(new Background(water)); //background water image
            playerGrid.getChildren().add(row);
        }
    }
    //board sent through socket
    public Board(int[][] gridLayout, boolean[][] vertical, EventHandler<? super MouseEvent> handler) {
        playerGrid.getChildren().clear();
        for (int y = 0; y < 5; y++){
            shipList[y] = new Ship(y + 1, true);
        }
        for (int y = 0; y < 10; y++){
            HBox row = new HBox();
            for (int x = 0; x < 10; x++){
                Ship s = new Ship(0, true);
                switch(gridLayout[x][y]) {
                    case 1:
                        shipList[0].vertical = vertical[x][y];
                        s = shipList[0];
                        break;
                    case 2:
                        shipList[1].vertical = vertical[x][y];
                        s = shipList[1];
                        break;
                    case 3:
                        shipList[2].vertical = vertical[x][y];
                        s = shipList[2];
                        break;
                    case 4:
                        shipList[3].vertical = vertical[x][y];
                        s = shipList[3];
                        break;
                    case 5:
                        shipList[4].vertical = vertical[x][y];
                        s = shipList[4];
                        break;
                    default:
                        s = null;
                }
                Cell c = new Cell(x, y, this, s);
                if (gridLayout[x][y] == 0) {
                    c.ship = null;
                }
                c.setOnMouseClicked(handler);
                row.getChildren().add(c);
            }
            playerGrid.setBackground(new Background(water));
            playerGrid.getChildren().add(row);
        }
    }
    //check if game is won
    public boolean checkWin(){
        return this.ships == 0;
    }
    //setting ships
    public boolean placeShip(Ship ship, int x, int y) {
        if (canPlaceShip(ship, x, y)) {
            int length = ship.type;
            ship.x = x;
            ship.y = y;

            if (ship.vertical) {
                for (int i = y; i < y + length; i++) {
                    Cell cell = getCell(x, i);
                    cell.ship = ship;
                    cell.setFill(Color.LIMEGREEN);
                    cell.setStroke(Color.GREEN);
                }
            }
            else {
                for (int i = x; i < x + length; i++) {
                    Cell cell = getCell(i, y);
                    cell.ship = ship;
                    cell.setFill(Color.LIMEGREEN);
                    cell.setStroke(Color.GREEN);
                }
            }
            return true;
        }
        return false;
    }

    public Cell getCell(int x, int y) {
        return (Cell)((HBox)playerGrid.getChildren().get(y)).getChildren().get(x);
    }
    //get cell neighbors, for placement
    private Cell[] getNeighbors(int x, int y) {
        Point2D[] points = new Point2D[] {
                new Point2D(x - 1, y),
                new Point2D(x + 1, y),
                new Point2D(x, y - 1),
                new Point2D(x, y + 1)
        };

        List<Cell> neighbors = new ArrayList<Cell>();

        for (Point2D p : points) {
            if (isValidPoint(p)) {
                neighbors.add(getCell((int)p.getX(), (int)p.getY()));
            }
        }
        return neighbors.toArray(new Cell[0]);
    }
    //check if you can place a ship based on surrounding ships
    private boolean canPlaceShip(Ship ship, int x, int y) {
        int length = ship.type;

        if (ship.vertical) {
            for (int i = y; i < y + length; i++) {
                if (!isValidPoint(x, i))
                    return false;

                Cell cell = getCell(x, i);
                if (cell.ship != null)
                    return false;

                for (Cell neighbor : getNeighbors(x, i)) {
                    if (!isValidPoint(x, i))
                        return false;

                    if (neighbor.ship != null)
                        return false;
                }
            }
        }
        else {
            for (int i = x; i < x + length; i++) {
                if (!isValidPoint(i, y))
                    return false;

                Cell cell = getCell(i, y);
                if (cell.ship != null)
                    return false;

                for (Cell neighbor : getNeighbors(i, y)) {
                    if (!isValidPoint(i, y))
                        return false;

                    if (neighbor.ship != null)
                        return false;
                }
            }
        }
        return true;
    }

    private boolean isValidPoint(Point2D point) {
        return isValidPoint(point.getX(), point.getY());
    }

    private boolean isValidPoint(double x, double y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }
}
