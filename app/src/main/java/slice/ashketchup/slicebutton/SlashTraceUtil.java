package slice.ashketchup.slicebutton;

import android.graphics.PointF;

public class SlashTraceUtil {
    static long FRAME_RATE = 1000/60;
    static float GRAVITY = 0.95f;
    static float density=1.0f;
    static float ROTATIONAL_VELOCITY = 2.1f;
    static float SEPARATION_VELOCITY = 4f;
    static float MAX_ROTATION=130f;
    static PointF getVelocities(float xf,float yf,float xt,float yt){
        PointF p=new PointF();
        float magnitude= getMagnitude(xf,yf,xt,yt);
        p.y = (yt-yf)/magnitude;
        p.x = (xt-xf)/magnitude;
        return p;
    }
    static float removeDensityDependence(float v){
        return density*(v);
    }
    static float getMagnitude(float xf,float yf,float xt,float yt){
        float mag = ((yt-yf)*(yt-yf)+(xt-xf)*(xt-xf));
        return (long)Math.sqrt(mag);
    }
    static void setPixelDensity(float dens){
        density=dens/3;
        SEPARATION_VELOCITY*= density;
        GRAVITY*= density;
    }
}
