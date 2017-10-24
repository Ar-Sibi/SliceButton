package slice.ashketchup.slicebutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

public class SlashButton {
    private static final int MODE_UNSET=0;
    private static final int MODE_NORMAL=1;
    private static final int MODE_SLASHED=2;
    float top;
    float left;
    float width;
    float height;
    Bitmap source;
    Bitmap drawnBitmap;
    Bitmap leftPiece;
    Bitmap rightPiece;
    float sampleheight;
    float samplewidth;
    Paint bitmapPainter;
    Context context;
    private int mode = MODE_UNSET;
    private float rotation=0;
    private float animationDepressionY=0;
    private float downwardVelocity=0;
    private float separationx=0;
    SlashGroup parent;
    SlashButton(Context c){
        context=c;
    }


    void setSource(Bitmap source,int samplewidth,int sampleheight){
        (source)=null;
        this.sampleheight=sampleheight;
        this.samplewidth=samplewidth;
        this.source=Bitmap.createScaledBitmap(source,(int)sampleheight,(int)sampleheight,false);
    }

    boolean withinBounds(MotionEvent e) {
        //TODO: Within Bounds for Rectangular and Circular Buttons
        return false;
    }

    void setSource(Bitmap source,float scaleX,float scaleY){
        (source)=null;
        if(scaleX>1||scaleY>1)
            throw new NumberFormatException("Scale cannot be greater than 1");
        else {
            this.sampleheight = source.getHeight()*scaleY;
            this.samplewidth = source.getWidth()*scaleX;
            this.source = Bitmap.createScaledBitmap(source, (int) sampleheight, (int) sampleheight, false);
        }
    }
    void setSource(Bitmap source){
        (source)=null;
        this.sampleheight=source.getHeight();
        this.samplewidth=source.getWidth();
        this.source=Bitmap.createScaledBitmap(source,(int)sampleheight,(int)sampleheight,false);
    }

    void setSize(){
        if(source==null){
            throw new IllegalStateException("Source is not set");
        }
        this.height=source.getHeight();
        this.width=source.getWidth();
        drawnBitmap=null;
        drawnBitmap =  Bitmap.createScaledBitmap(source,source.getWidth(),source.getHeight(),false);
        mode = MODE_NORMAL;
    }
    void setSize(int width,int height){
        this.height=height;
        this.width=width;
        drawnBitmap=null;
        if(source==null){
            throw new IllegalStateException("Source is not set");
        }
        drawnBitmap =  Bitmap.createScaledBitmap(source,width,height,false);
        mode = MODE_NORMAL;
    }

    void setParent(SlashGroup parent){
        this.parent = parent;
    }

    void slash(final float m1,final float m2,final float cutx,final float cuty){
        new Thread(new Runnable() {
            @Override
            public void run() {

                boolean slashStyleChecker=false;
                int[] pixels= new int[(int)sampleheight*(int)samplewidth];
                source.getPixels(pixels, 0, (int)samplewidth, 0, 0, (int)samplewidth, (int)sampleheight);
                int[]pixels2 = pixels.clone();
                int index = 0;
                for (float i=0 ; i < sampleheight  ; i++) {
                    for (float j=0 ; j < samplewidth ; j++) {
                        if (m2 * (top + (i*height/sampleheight)) - m1 * (left + width/samplewidth) > -m1 * cutx + m2 * cuty) {
                            if (i == 0 && j == 0) {
                                slashStyleChecker = true;
                            }
                            int pix = pixels[index];
                            pix = Color.argb(0, Color.red(pix), Color.green(pix), Color.blue(pix));
                            pixels[index] = pix;
                        } else {
                            int pix= pixels2[index];
                            pix = Color.argb(0, Color.red(pix), Color.green(pix), Color.blue(pix));
                            pixels2[index] = pix;
                        }
                        index++;
                    }
                }
                if (slashStyleChecker) {
                    leftPiece = Bitmap.createBitmap(pixels, (int)samplewidth, (int)sampleheight, Bitmap.Config.ARGB_8888);
                    rightPiece = Bitmap.createBitmap(pixels2, (int)samplewidth, (int)sampleheight, Bitmap.Config.ARGB_8888);
                } else {
                    rightPiece= Bitmap.createBitmap(pixels,(int) samplewidth,(int) sampleheight, Bitmap.Config.ARGB_8888);
                    leftPiece= Bitmap.createBitmap(pixels2,(int) samplewidth,(int) sampleheight, Bitmap.Config.ARGB_8888);
                }
                mode=MODE_SLASHED;    }
        }).start();
    }
    void drawButton(Canvas canvas){
        if(mode==MODE_NORMAL)
            canvas.drawBitmap(drawnBitmap,new Rect(0,0,(int)samplewidth,(int)sampleheight),new Rect((int)left,(int)top,(int)width,(int)height),bitmapPainter);
        if(mode==MODE_SLASHED){
            if (rotation > SlashTraceUtil.MAX_ROTATION) {
                separationx = 0f;
                animationDepressionY = 0f;
                rotation = 0f;
                leftPiece.recycle();
                rightPiece.recycle();
            } else {
                Matrix matrix = new Matrix();
                matrix.setTranslate(left + separationx, top + animationDepressionY);
                matrix.preScale(width/samplewidth,height/sampleheight);
                matrix.postRotate(rotation, left + separationx + width / 2, top+ animationDepressionY + height / 2);
                canvas.drawBitmap(leftPiece, matrix, bitmapPainter);
                matrix.reset();
                matrix.setTranslate(left - separationx, top + animationDepressionY);
                matrix.preScale(width/samplewidth,  height/sampleheight);
                matrix.postRotate(-rotation, left - separationx + width / 2, top + animationDepressionY + height / 2);
                //matrix.postScale(width/width,height/height);;
                canvas.drawBitmap(rightPiece, matrix, bitmapPainter);
                downwardVelocity += SlashTraceUtil.GRAVITY;
                animationDepressionY += downwardVelocity;
                rotation += SlashTraceUtil.ROTATIONAL_VELOCITY;
                separationx += SlashTraceUtil.SEPARATION_VELOCITY;
            }
        }
    }
}
