package slice.ashketchup.slicebutton;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class SlashHolderView extends View {
    SlashGroup slashGroup = new SlashGroup();
    FingerTracer fingerTracer = FingerTracer.newInstance();
    float lastpointx=0;
    float lastpointy=0;
    Thread drawer = new Thread(new Runnable(){
        @Override
        public void run() {
            while(!Thread.interrupted()) {
                try{
                    Thread.sleep(1000/60);
                }catch (InterruptedException e){

                }
                handler.sendEmptyMessage(0);
            }
        }
    });
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };
    public SlashHolderView(Context context) {
        super(context);
        init();
    }

    public SlashHolderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init(){
        drawer.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        slashGroup.drawGroup(canvas);
        fingerTracer.drawPoints(canvas);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                fingerTracer.setIsDown(true);
                slashGroup.reset();
                sendEvent(event);
            }
                break;
            case MotionEvent.ACTION_MOVE : {
                float x=event.getX();
                float y=event.getY();
                float offsetx=Math.abs(event.getX()-lastpointx);
                float offsety=Math.abs(event.getY()-lastpointy);
                if(offsetx>offsety);
                    offsety=offsetx;
                PointF velocity=SlashTraceUtil.getVelocities(lastpointx,lastpointy,event.getX(),event.getY());
                int iterand=1;
                while (offsety>20){
                    event.setLocation(lastpointx+velocity.x*10f*iterand,lastpointy+velocity.y*10f*iterand);
                    sendEvent(event);
                    offsety-=10;
                    iterand++;
                }
                event.setLocation(x,y);
                sendEvent(event);
            }
                break;
            case MotionEvent.ACTION_UP : {
                fingerTracer.setIsDown(false);
                slashGroup.reset();
            }
        }
        lastpointx=event.getX();
        lastpointy=event.getY();
        return true;
    }

    void sendEvent(MotionEvent e){
        fingerTracer.addPoints(e);
        slashGroup.listen(e);
    }
}
