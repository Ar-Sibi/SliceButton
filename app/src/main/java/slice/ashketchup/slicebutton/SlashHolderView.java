package slice.ashketchup.slicebutton;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SlashHolderView extends View {
    SlashGroup slashGroup = new SlashGroup();
    FingerTracer fingerTracer = FingerTracer.newInstance();
    public SlashHolderView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                if(event.getY()<25)
                    return false;
                fingerTracer.setIsDown(true);
                slashGroup.reset();
                touchSender.send(event);
            }
                break;
            case MotionEvent.ACTION_MOVE : {
                float x=event.getX();
                float y=event.getY();
                float offsetx=Math.abs(event.getX()-touchSender.lastPointx);
                float offsety=Math.abs(event.getY()-touchSender.lastPointy);
                if(offsetx>offsety);
                    offsety=offsetx;
                PointF velocity=SlashTraceUtil.getVelocities(touchSender.lastPointx,touchSender.lastPointy,event.getX(),event.getY());
                int iterand=1;
                while (offsety>20){
                    event.setLocation(touchSender.lastPointx+velocity.x*10f*iterand,touchSender.lastPointy+velocity.y*10f*iterand);
                    touchSender.send(event);
                    offsety-=10;
                    iterand++;
                }
                event.setLocation(x,y);
                touchSender.send(event);
            }
                break;
            case MotionEvent.ACTION_UP : {
                fingerTracer.setIsDown(false);
                slashGroup.reset();
            }
        }
        touchSender.lastPointx=event.getX();
        touchSender.lastPointy=event.getY();
        return true;
    }
    void sendEvent(MotionEvent e){

    }
}
