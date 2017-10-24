package slice.ashketchup.slicebutton;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import java.util.*;

class FingerTracer {

        private FingerTracer(){
        }

    public static FingerTracer newInstance() {
        return new FingerTracer();
    }

        Paint paint = new Paint();
        boolean down = false;
        List<PointF> points=new  ArrayList<PointF>();
        public void addPoints (MotionEvent e){
            points.add(0,new PointF(e.getX(),e.getY()));
            if(points.size()>80){
                points.remove(points.size()-1);
            }
        }
        public void setIsDown(boolean b){
            down=b;
        }
        public void setColor(){
            paint.setColor( Color.parseColor("#000000"));
        }
        public void drawPoints(Canvas c){
            if(points.size()>2){
                for(int i=1;i <=points.size()-1;i++){
                    paint.setStyle(Paint.Style.STROKE);
                    PointF p = points.get(i-1);
                    PointF p1 = points.get(i);
                    if(i<points.size()/4&&i!=0) {
                        paint.setStrokeWidth(16*(i+0.3f)/(points.size()/4));
                    }else
                        paint.setStrokeWidth(16*((-i+points.size()+0.3f))/(3*points.size()/4+1));
                    c.drawLine(p.x, p.y, p1.x, p1.y, paint);
                }
            }
            if(points.size()>20)
                points.remove(points.size()-1);
            if(points.size()>20)
                points.remove(points.size()-1);
            if(!down&& points.size()>0){
                remove();
            }
        }
        public void remove(){
            if(points.size()>3){
                points.remove(points.size()-1);
                points.remove(points.size()-1);
                points.remove(points.size()-1);
            }else{
                reset();
            }
        }
        public void reset(){
            points= new ArrayList<>();
        }
}