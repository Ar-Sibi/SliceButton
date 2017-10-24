package slice.ashketchup.slicebutton;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;


public class SlashGroup {
    List<SlashButton> buttons= new ArrayList<>();
    List<PointF> points = new ArrayList<>();
    boolean slashed=false;
    SlashButton slashedButton;
    public void addButton(SlashButton button){
        buttons.add(button);
        button.setParent(this);
    }

    public void reset(){
        //TODO: Implement reset
    }

    public boolean listen(MotionEvent e){
            for (SlashButton b : buttons) {
                if (b.withinBounds(e)) {
                    //If one point is detected outside and an event is detected inside add point
                    if (points.size() == 0) {
                        points.add(new PointF(e.getX(), e.getY()));
                        return true;
                    }
                    if (points.size() == 1) {
                        points.add(new PointF(e.getX(), e.getY()));
                        slashedButton = b;
                    } else {

                    }
                    return true;
                }
            }
            // First point detected outside a button
            if (points.size() == 0) {
                points.add(new PointF(e.getX(), e.getY()));
            } else if (points.size() == 2) {
                // Third point detected once event detected outside image
                points.add(new PointF(e.getX(), e.getY()));
                slashedButton.normalOnSlash();
            } else {
                // Replacing point if button not encountered
                points.set(0,new PointF(e.getX(), e.getY()));
            }
            return true

    }
    public void drawGroup(Canvas c) {
        for(SlashButton b : buttons){
            b.drawButton(c);
        }
    }
}
