/*
File name: SpeedWordsTimerPanel.java
Short description:
IST 261 Assignment:
@author jcswa
@version 1.01 Jan 8, 2021
 */
package swanson.speedwords;

import java.awt.Font;
import swanson.mytimer.TimerPanel;

public class SpeedWordsTimerPanel extends TimerPanel {
// Instance Variables -- define your private data

    private static final long serialVersionUID = 1L;
    private static final Font FONT = new Font(Font.DIALOG, Font.BOLD, 24);
    private SpeedWords speedWords;


    public SpeedWordsTimerPanel(SpeedWords speedWords, int time) {
        super(time, FONT);
        this.speedWords = speedWords;
    }
    
    @Override
    protected void timesUp(){
        speedWords.outOfTime();
    }
   

}
