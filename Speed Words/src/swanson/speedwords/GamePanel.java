/*
File name: GamePanel.java
Short description:
IST 261 Assignment:
@author jcswa
@version 1.01 Jan 8, 2021
 */
package swanson.speedwords;

import common.methods.FileIO;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;
    private static final int START_X = WIDTH / 2 - 7 * LetterTile.SIZE / 2;
    private static final int START_Y = HEIGHT / 2 - LetterTile.SIZE / 2;
    private static final String FILE_NAME = "./enable1_7.txt";
    private SpeedWords speedWords;
    private ArrayList<TileSet> tileSets = new ArrayList<>();
    private ArrayList<String> sevenLetterWords = new ArrayList<>();
    private Random rand = new Random();
    private TileSet movingTiles;
    private int mouseX;
    private int mouseY;
    private Dictionary dictionary = new Dictionary();
    private ArrayList<String> formedWords = new ArrayList<>();
    private boolean outOfTime = false;

// Constructors
    public GamePanel(SpeedWords speedWords) {
        this.speedWords = speedWords;

        sevenLetterWords = FileIO.readTextFile(this, FILE_NAME);
        restart();

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                dragged(x, y);
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int mouseButton = e.getButton();
                boolean leftClicked = mouseButton == MouseEvent.BUTTON1;
                clicked(x, y, leftClicked);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                released();
            }
        });
    }

    private void dragged(int x, int y) {
        if (movingTiles != null) {
            int changeX = x - mouseX;
            int changeY = y - mouseY;
            System.out.println("ChangeX: " + changeX);
            System.out.println("ChangeY: " + changeY);
            movingTiles.changeXY(changeX, changeY);
            mouseX = x;
            mouseY = y;
            repaint();
        }
    }

    private void released() {
        // if tile released on other tile connect tiles
        if (movingTiles != null) {
            boolean addedToTiles = false;
            for (int i = 0; i < tileSets.size() && addedToTiles == false; i++) {
                TileSet tileSet = tileSets.get(i);
                addedToTiles = tileSet.insertTiles(movingTiles);
                if (addedToTiles == true) {
                    movingTiles = null;
                    checkWord(tileSet);
                }
            }
        }
        //if tile released in empty slot add tile to tileSets
        if (movingTiles != null) {
            String str = movingTiles.toString();
            int x = movingTiles.getX();
            int y = movingTiles.getY();

            TileSet newTileSet = new TileSet(str, x, y);
            tileSets.add(0, newTileSet);
            movingTiles = null;
            checkWord(newTileSet);
        }
        repaint();
    }

    private void clicked(int x, int y, boolean leftClicked) {

        if (movingTiles == null && outOfTime==false) {
            mouseX = x;
            mouseY = y;
            for (int i = 0; i < tileSets.size() && movingTiles == null; i++) {
                TileSet tileSet = tileSets.get(i);
                if (tileSet.contains(x, y)) {
                    if (leftClicked) {
                        movingTiles = tileSet.removeAndReturn1TileAt(x, y);
                        if (tileSet.getNumberOfTiles() == 0) {
                            tileSets.remove(i);
                        } else {
                            checkWord(tileSet);
                        }
                    } else {
                        movingTiles = tileSet;
                        tileSets.remove(i);
                    }
                }
            }
            repaint();
        }
    }

    private void checkWord(TileSet tileSet) {
        String s = tileSet.toString();
        boolean isAWord = dictionary.isAWord(s);
        boolean foundBefore = formedWords.contains(s);
        if (isAWord && !foundBefore) {
            tileSet.setValid(true);
            int points = tileSet.getPoints();
            speedWords.addToScore(points);
            //if this is the first word found add it to the list
            if (formedWords.isEmpty()) {
                formedWords.add(s);
            } 
            else {
                //otherwise, insert word before first word it's less than
                boolean added = false;
                for (int i = 0; !added && i < formedWords.size(); i++) {
                    String formedWord = formedWords.get(i);
                    if (s.compareTo(formedWord) < 0) {
                        formedWords.add(i, s);
                        added = true;
                    }  
                }
                //if the word is not less than any of the words, add it to end of list
                if(!added){
                    formedWords.add(s);
                }
            }
            speedWords.setWordList(formedWords);
        } 
        else {
            tileSet.setValid(false);
        }
    }
    public void setOutOfTime(boolean outOfTime){
        this.outOfTime = outOfTime;
    }
    public void restart() {
        tileSets.clear();
        formedWords.clear();
        int range = sevenLetterWords.size();
        int choose = rand.nextInt(range);
        String str = sevenLetterWords.get(choose);
        TileSet tileSet = new TileSet(str, START_X, START_Y);
        tileSets.add(tileSet);
        checkWord(tileSet);
        outOfTime = false;
        movingTiles = null;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        // draws the background
        g.setColor(speedWords.getTAN());
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // draw all current tile sets
        for (int i = tileSets.size() - 1; i >= 0; i--) {
            TileSet tileSet = tileSets.get(i);
            tileSet.draw(g);
        }

        // draw the moving tiles
        if (movingTiles != null) {
            movingTiles.draw(g);
        }

    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dimension = new Dimension(WIDTH, HEIGHT);
        return dimension;
    }

}
