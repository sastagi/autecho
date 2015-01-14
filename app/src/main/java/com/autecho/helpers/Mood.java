package com.autecho.helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.DisplayMetrics;
import android.view.View;

import static com.autecho.dcc.MakeEntry.smallsizeflag;

/**
 * Created by Santosh on 1/13/15.
 */
public class Mood extends View {
    //private final float x;
    private final float y;
    private final Paint mpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private DisplayMetrics metrics;
    private int cx,cy, r;
    private int lx,ly,rx,ry,mtx, mty, mbx, mby;

    public Mood(Context context, float x, float y) {
        super(context);
        //this.x = x;
        this.y = y;
        Resources resources = context.getResources();
        metrics = resources.getDisplayMetrics();
        if(smallsizeflag){
            cx = (int) (160 * (metrics.densityDpi / 160f));
            cy = (int) (90 * (metrics.densityDpi / 160f));
            r = (int) (75 * (metrics.densityDpi / 160f));
        }
        else{
            cx = (int) (160 * (metrics.densityDpi / 160f));
            cy = (int) (120 * (metrics.densityDpi / 160f));
            r = (int) (100 * (metrics.densityDpi / 160f));
        }
    }

    private Shader radialGradient() {
        //return new LinearGradient(0, 0, 50, 50, new int[] { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF }, null, Shader.TileMode.MIRROR);
        //new int[] { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF }, null,
        return new RadialGradient(cx, cy, r, new int[] { 0xFFFFEC8B, 0xFFFFD700}, new float[] {0.1f,0.9f}, Shader.TileMode.MIRROR);
        //RadialGradient (20, 20, 25, new int[] { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF }, null, Shader.TileMode tile)
    }

    //USE THE CODE BELOW TO OPTIMISE LATER
    /*private int converter(float toconvert){
    	int converted = (int) (toconvert * (metrics.densityDpi / 160f));
    	return converted;
    }*/

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint_stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint paint_fill = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint eyefillpaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint_stroke.setStyle(Paint.Style.STROKE);
        paint_stroke.setStrokeWidth(2);
        paint_stroke.setColor(android.graphics.Color.rgb(140,98,57));
        //paint.setStyle(Paint.Style.STROKE);
        //paint.setStyle(Style.FILL);
        paint_fill.setColor(android.graphics.Color.rgb(255,255,255));
        paint_fill.setAntiAlias(true);

        eyefillpaint.setStrokeWidth(2);
        eyefillpaint.setColor(android.graphics.Color.BLACK);

        Path path = new Path();

        canvas.drawCircle(cx, cy, r, paint_stroke);

        float d = y+125;

        if (d<=150){
            mpaint.setShader(radialGradient());
            canvas.drawCircle(cx, cy, r, mpaint);

            Path eyepathleft = new Path();
            Path eyepathright = new Path();

            if(smallsizeflag){
                lx = (int) (105 * (metrics.densityDpi / 160f));
                ly = (int) (60 * (metrics.densityDpi / 160f));
                rx = (int) (135 * (metrics.densityDpi / 160f));
                ry = (int) (60 * (metrics.densityDpi / 160f));
                mtx = (int) (120 * (metrics.densityDpi / 160f));
                mty = (int) (50 * (metrics.densityDpi / 160f));
                mbx = (int) (120 * (metrics.densityDpi / 160f));
                mby = (int) (70 * (metrics.densityDpi / 160f));
            }
            else{
                lx = (int) (100 * (metrics.densityDpi / 160f));
                ly = (int) (80 * (metrics.densityDpi / 160f));
                rx = (int) (140 * (metrics.densityDpi / 160f));
                ry = (int) (80 * (metrics.densityDpi / 160f));
                mtx = (int) (120 * (metrics.densityDpi / 160f));
                mty = (int) (65 * (metrics.densityDpi / 160f));
                mbx = (int) (120 * (metrics.densityDpi / 160f));
                mby = (int) (95 * (metrics.densityDpi / 160f));
            }

            eyepathleft.moveTo(lx,ly);
            eyepathleft.quadTo(mtx,mty,rx,ry);
            eyepathleft.quadTo(mbx,mby,lx,ly);
            canvas.drawPath(eyepathleft, paint_fill);

            eyepathleft.moveTo(lx,ly);
            eyepathleft.quadTo(mtx,mty,rx,ry);
            eyepathleft.quadTo(mbx,mby,lx,ly);
            canvas.drawPath(eyepathleft, paint_stroke);

            int r = (int) (5 * (metrics.densityDpi / 160f));

            canvas.drawCircle(mtx, ly, r, eyefillpaint);

            if(smallsizeflag){
                lx = (int) (185 * (metrics.densityDpi / 160f));
                ly = (int) (60 * (metrics.densityDpi / 160f));
                rx = (int) (215 * (metrics.densityDpi / 160f));
                ry = (int) (60 * (metrics.densityDpi / 160f));
                mtx = (int) (200 * (metrics.densityDpi / 160f));
                mty = (int) (50 * (metrics.densityDpi / 160f));
                mbx = (int) (200 * (metrics.densityDpi / 160f));
                mby = (int) (70 * (metrics.densityDpi / 160f));
            }
            else{
                lx = (int) (180 * (metrics.densityDpi / 160f));
                ly = (int) (80 * (metrics.densityDpi / 160f));
                rx = (int) (220 * (metrics.densityDpi / 160f));
                ry = (int) (80 * (metrics.densityDpi / 160f));
                mtx = (int) (200 * (metrics.densityDpi / 160f));
                mty = (int) (65 * (metrics.densityDpi / 160f));
                mbx = (int) (200 * (metrics.densityDpi / 160f));
                mby = (int) (95 * (metrics.densityDpi / 160f));
            }

            eyepathright.moveTo(lx,ly);
            eyepathright.quadTo(mtx,mty,rx,ry);
            eyepathright.quadTo(mbx,mby,lx,ly);
            canvas.drawPath(eyepathright, paint_fill);

            eyepathright.moveTo(lx,ly);
            eyepathright.quadTo(mtx,mty,rx,ry);
            eyepathright.quadTo(mbx,mby,lx,ly);
            canvas.drawPath(eyepathright, paint_stroke);

            r = (int) (5 * (metrics.densityDpi / 160f));

            canvas.drawCircle(mtx, ly, r, eyefillpaint);

            int lipy;

            if(smallsizeflag){
                d = d-35;
                lipy=115;
            }
            else{
                lipy=150;
            }

            lx = (int) (120 * (metrics.densityDpi / 160f));
            ly = (int) (lipy * (metrics.densityDpi / 160f));
            rx = (int) (160 * (metrics.densityDpi / 160f));
            ry = (int) (d * (metrics.densityDpi / 160f));
            mtx = (int) (200 * (metrics.densityDpi / 160f));
            mty = (int) (lipy * (metrics.densityDpi / 160f));

            path.moveTo(lx,ly);
            path.quadTo(rx,ry,mtx,mty);
            canvas.drawPath(path, paint_stroke);
        }

        if ((d>150)&(d<=185)){
            mpaint.setShader(radialGradient());
            canvas.drawCircle(cx, cy, r, mpaint);

            Path eyepathleft = new Path();
            Path eyepathright = new Path();

            if(smallsizeflag){
                lx = (int) (105 * (metrics.densityDpi / 160f));
                ly = (int) (60 * (metrics.densityDpi / 160f));
                rx = (int) (135 * (metrics.densityDpi / 160f));
                ry = (int) (60 * (metrics.densityDpi / 160f));
                mtx = (int) (120 * (metrics.densityDpi / 160f));
                mty = (int) (50 * (metrics.densityDpi / 160f));
                mbx = (int) (120 * (metrics.densityDpi / 160f));
                mby = (int) (70 * (metrics.densityDpi / 160f));
            }
            else{
                lx = (int) (100 * (metrics.densityDpi / 160f));
                ly = (int) (80 * (metrics.densityDpi / 160f));
                rx = (int) (140 * (metrics.densityDpi / 160f));
                ry = (int) (80 * (metrics.densityDpi / 160f));
                mtx = (int) (120 * (metrics.densityDpi / 160f));
                mty = (int) (65 * (metrics.densityDpi / 160f));
                mbx = (int) (120 * (metrics.densityDpi / 160f));
                mby = (int) (95 * (metrics.densityDpi / 160f));
            }

            eyepathleft.moveTo(lx,ly);
            eyepathleft.quadTo(mtx,mty,rx,ry);
            eyepathleft.quadTo(mbx,mby,lx,ly);
            canvas.drawPath(eyepathleft, paint_fill);

            eyepathleft.moveTo(lx,ly);
            eyepathleft.quadTo(mtx,mty,rx,ry);
            eyepathleft.quadTo(mbx,mby,lx,ly);
            canvas.drawPath(eyepathleft, paint_stroke);

            int r = (int) (5 * (metrics.densityDpi / 160f));

            canvas.drawCircle(mtx, ly, r, eyefillpaint);

            if(smallsizeflag){
                lx = (int) (185 * (metrics.densityDpi / 160f));
                ly = (int) (60 * (metrics.densityDpi / 160f));
                rx = (int) (215 * (metrics.densityDpi / 160f));
                ry = (int) (60 * (metrics.densityDpi / 160f));
                mtx = (int) (200 * (metrics.densityDpi / 160f));
                mty = (int) (50 * (metrics.densityDpi / 160f));
                mbx = (int) (200 * (metrics.densityDpi / 160f));
                mby = (int) (70 * (metrics.densityDpi / 160f));
            }
            else{
                lx = (int) (180 * (metrics.densityDpi / 160f));
                ly = (int) (80 * (metrics.densityDpi / 160f));
                rx = (int) (220 * (metrics.densityDpi / 160f));
                ry = (int) (80 * (metrics.densityDpi / 160f));
                mtx = (int) (200 * (metrics.densityDpi / 160f));
                mty = (int) (65 * (metrics.densityDpi / 160f));
                mbx = (int) (200 * (metrics.densityDpi / 160f));
                mby = (int) (95 * (metrics.densityDpi / 160f));
            }

            eyepathright.moveTo(lx,ly);
            eyepathright.quadTo(mtx,mty,rx,ry);
            eyepathright.quadTo(mbx,mby,lx,ly);
            canvas.drawPath(eyepathright, paint_fill);

            eyepathright.moveTo(lx,ly);
            eyepathright.quadTo(mtx,mty,rx,ry);
            eyepathright.quadTo(mbx,mby,lx,ly);
            canvas.drawPath(eyepathright, paint_stroke);

            r = (int) (5 * (metrics.densityDpi / 160f));

            canvas.drawCircle(mtx, ly, r, eyefillpaint);

            int lipy;

            if(smallsizeflag){
                d = d-35;
                lipy=115;
            }
            else{
                lipy=150;
            }

            lx = (int) (120 * (metrics.densityDpi / 160f));
            ly = (int) (lipy * (metrics.densityDpi / 160f));
            rx = (int) (160 * (metrics.densityDpi / 160f));
            ry = (int) (d * (metrics.densityDpi / 160f));
            mtx = (int) (200 * (metrics.densityDpi / 160f));
            mty = (int) (lipy * (metrics.densityDpi / 160f));

            path.moveTo(lx,ly);
            path.quadTo(rx,ry,mtx,mty);
            canvas.drawPath(path, paint_stroke);
        }


        if (d>185){
            mpaint.setShader(radialGradient());
            canvas.drawCircle(cx, cy, r, mpaint);

            Path eyepathleft = new Path();
            Path eyepathright = new Path();

            int ety,eby;

            float e = y - 60;
            e = e/4;
            if(smallsizeflag){
                ety = (int) (50-e);
                eby = (int) (70+e);
            }
            else{
                ety = (int) (65-e);
                eby = (int) (95+e);
            }
            e = (e * (metrics.densityDpi / 160f));
            if(smallsizeflag){
                lx = (int) (105 * (metrics.densityDpi / 160f));
                ly = (int) (60 * (metrics.densityDpi / 160f));
                rx = (int) (135 * (metrics.densityDpi / 160f));
                ry = (int) (60 * (metrics.densityDpi / 160f));
                mtx = (int) (120 * (metrics.densityDpi / 160f));
                mty = (int) (ety * (metrics.densityDpi / 160f));
                mbx = (int) (120 * (metrics.densityDpi / 160f));
                mby = (int) (eby * (metrics.densityDpi / 160f));
            }
            else{
                lx = (int) (100 * (metrics.densityDpi / 160f));
                ly = (int) (80 * (metrics.densityDpi / 160f));
                rx = (int) (140 * (metrics.densityDpi / 160f));
                ry = (int) (80 * (metrics.densityDpi / 160f));
                mtx = (int) (120 * (metrics.densityDpi / 160f));
                mty = (int) (ety * (metrics.densityDpi / 160f));
                mbx = (int) (120 * (metrics.densityDpi / 160f));
                mby = (int) (eby * (metrics.densityDpi / 160f));
            }

            eyepathleft.moveTo(lx,ly);
            eyepathleft.quadTo(mtx,mty,rx,ry);
            eyepathleft.quadTo(mbx,mby,lx,ly);
            canvas.drawPath(eyepathleft, paint_fill);

            eyepathleft.moveTo(lx,ly);
            eyepathleft.quadTo(mtx,mty,rx,ry);
            eyepathleft.quadTo(mbx,mby,lx,ly);
            canvas.drawPath(eyepathleft, paint_stroke);

            int r = (int) (5 * (metrics.densityDpi / 160f));

            canvas.drawCircle(mtx, ly, r, eyefillpaint);

            if(smallsizeflag){
                lx = (int) (185 * (metrics.densityDpi / 160f));
                ly = (int) (60 * (metrics.densityDpi / 160f));
                rx = (int) (215 * (metrics.densityDpi / 160f));
                ry = (int) (60 * (metrics.densityDpi / 160f));
                mtx = (int) (200 * (metrics.densityDpi / 160f));
                mty = (int) (ety * (metrics.densityDpi / 160f));
                mbx = (int) (200 * (metrics.densityDpi / 160f));
                mby = (int) (eby * (metrics.densityDpi / 160f));
            }
            else{
                lx = (int) (180 * (metrics.densityDpi / 160f));
                ly = (int) (80 * (metrics.densityDpi / 160f));
                rx = (int) (220 * (metrics.densityDpi / 160f));
                ry = (int) (80 * (metrics.densityDpi / 160f));
                mtx = (int) (200 * (metrics.densityDpi / 160f));
                mty = (int) (ety * (metrics.densityDpi / 160f));
                mbx = (int) (200 * (metrics.densityDpi / 160f));
                mby = (int) (eby * (metrics.densityDpi / 160f));
            }

            eyepathright.moveTo(lx,ly);
            eyepathright.quadTo(mtx,mty,rx,ry);
            eyepathright.quadTo(mbx,mby,lx,ly);
            canvas.drawPath(eyepathright, paint_fill);

            eyepathright.moveTo(lx,ly);
            eyepathright.quadTo(mtx,mty,rx,ry);
            eyepathright.quadTo(mbx,mby,lx,ly);
            canvas.drawPath(eyepathright, paint_stroke);

            r = (int) (5 * (metrics.densityDpi / 160f));

            canvas.drawCircle(mtx, ly, r, eyefillpaint);

            int upperlip, lipy;

            if(smallsizeflag){
                d = d-35;
                upperlip = (int) (150 * (metrics.densityDpi / 160f));
                lipy=115;
            }
            else{
                lipy=150;
                upperlip = (int) (185 * (metrics.densityDpi / 160f));
            }

            lx = (int) (120 * (metrics.densityDpi / 160f));
            ly = (int) (lipy * (metrics.densityDpi / 160f));
            mtx = (int) (160 * (metrics.densityDpi / 160f));
            mty = (int) (d * (metrics.densityDpi / 160f));
            rx = (int) (200 * (metrics.densityDpi / 160f));
            ry = (int) (lipy * (metrics.densityDpi / 160f));

            path.moveTo(lx,ly);
            path.quadTo(mtx,upperlip,rx,ry);
            path.quadTo(mtx,mty,lx,ly);
            canvas.drawPath(path, paint_stroke);

            path.moveTo(lx,ly);
            path.quadTo(mtx,upperlip,rx,ry);
            path.quadTo(mtx,mty,lx,ly);
            canvas.drawPath(path, paint_fill);
        }
    }
}
