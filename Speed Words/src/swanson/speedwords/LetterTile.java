/*
File name: LetterTile.java
Short description:
IST 261 Assignment:
@author jcswa
@version 1.01 Jan 9, 2021
 */
package swanson.speedwords;

import common.methods.FileIO;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;


public class LetterTile extends JPanel {

    private static final long serialVersionUID = 1L;
    static final int SIZE = 40;
    private static final String IMAGE_NAME = "./WoodTile.jpg";
    private static final Font BIG_FONT = new Font(Font.DIALOG, Font.BOLD, 30);
    private static final Font SMALL_FONT = new Font(Font.DIALOG, Font.BOLD, 11);
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int[] LETTER_POINTS = 
    {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
    
    private static BufferedImage image;
    private String letter;
    private FontMetrics smallFMetric = getFontMetrics(SMALL_FONT);
    private FontMetrics bigFMetric = getFontMetrics(BIG_FONT);
    private int points;
    
    
// Constructors
    public LetterTile(String letter) {
        this.letter = letter;
        
        if(image == null){
            image = FileIO.readImageFile(this, IMAGE_NAME);    
        }
        // grabbing letter from ALPHABET
        int index = ALPHABET.indexOf(letter);
        points = LETTER_POINTS[index];
    }
    public void draw(Graphics g, int x, int y){
        // draw wood panel onto tile from jpg file
        if(image == null){
            g.setColor(Color.white);
            g.fillRect(x, y, SIZE, SIZE);
        }
        else{
            g.drawImage(image, x, y, SIZE, SIZE, null);
        }
        // draw the border of tile
        g.setColor(Color.black);
        g.drawRect(x, y, SIZE-1, SIZE-1);
        
        // draw large letter in center of tile
        g.setFont(BIG_FONT);
        int letterWidth = bigFMetric.stringWidth(letter);
        // getting x coordinate for letter draw
        int letterX = ((SIZE - letterWidth) / 2) + x;
        // get y coordinate for letter
        int letterY = ((SIZE * 3) / 4) + y;
        g.drawString(letter, letterX, letterY);
        
        // draw small letters on center of tile
        g.setFont(SMALL_FONT);
        String pointsString = "" + points;
        int pointsWidth = smallFMetric.stringWidth(pointsString);
        // X coordinate 2 pixels from right tile edge
        int pointsX = ((SIZE - pointsWidth) - 2) + x;
        // Y coordinate 
        int pointsY = ((SIZE * 36) / 40) + y;
        g.drawString(pointsString, pointsX, pointsY);
    }
    
    
    @Override
    public Dimension getPreferredSize(){
        Dimension dim = new Dimension(SIZE, SIZE);
        return dim;
    }

    public String getLetter() {
        return letter;
    }

    public int getPoints() {
        return points;
    }
    
}
