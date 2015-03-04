package com.autecho.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.util.DisplayMetrics;

/**
 * Created by Santosh on 3/2/15.
 */
public class MyShapeDrawable extends ShapeDrawable {
    private Paint mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint opaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint_fill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint_eye_fill = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float d;
    @SuppressWarnings("unused")
    private DisplayMetrics metrics;

    public MyShapeDrawable(Shape s) {
        super(s);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(Color.rgb(0, 105, 0));
    }
    public void getInflection(int a){
        d = (float) ((a/2.5)+50);
    }

    private Shader makeLinear() {
        //return new LinearGradient(0, 0, 50, 50, new int[] { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF }, null, Shader.TileMode.MIRROR);
        //new int[] { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF }, null,
        return new RadialGradient(50, 50, 40, new int[] { 0xFFFFEC8B, 0xFFFFD700}, new float[] {0.1f,0.9f}, Shader.TileMode.MIRROR);
        //RadialGradient (20, 20, 25, new int[] { 0xFFFF0000, 0xFF00FF00, 0xFF0000FF }, null, Shader.TileMode tile)
    }

    public void setContext(Context c){
        Resources resources = c.getResources();
        metrics = resources.getDisplayMetrics();
    }

    @Override
    protected void onDraw(Shape shape, Canvas canvas, Paint fillpaint) {
        mpaint.setShader(makeLinear());
        paint.setColor(Color.rgb(140,98,57));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        opaint.setColor(Color.rgb(140,98,57));
        opaint.setStyle(Paint.Style.STROKE);
        opaint.setStrokeWidth(3);
        opaint.setShadowLayer(2f, -1, 1, Color.GRAY);
        paint_eye_fill.setColor(android.graphics.Color.rgb(0,0,0));
        paint_eye_fill.setAntiAlias(true);
        paint_fill.setColor(android.graphics.Color.rgb(255,255,255));
        paint_fill.setAntiAlias(true);

        canvas.drawCircle(50, 50, 40, opaint);
        canvas.drawCircle(50, 50, 40, mpaint);


        if(d<74){
            Path eyepathleft = new Path();

            eyepathleft.moveTo(27,30);
            eyepathleft.quadTo(35,22,43,30);
            eyepathleft.quadTo(35,38,27,30);
            canvas.drawPath(eyepathleft, paint_fill);

            eyepathleft.moveTo(27,30);
            eyepathleft.quadTo(35,22,43,30);
            eyepathleft.quadTo(35,38,27,30);
            canvas.drawPath(eyepathleft, paint);

            Path eyepathright = new Path();

            eyepathright.moveTo(57,30);
            eyepathright.quadTo(65,22,73,30);
            eyepathright.quadTo(65,38,57,30);
            canvas.drawPath(eyepathright, paint_fill);

            eyepathright.moveTo(57,30);
            eyepathright.quadTo(65,22,73,30);
            eyepathright.quadTo(65,38,57,30);
            canvas.drawPath(eyepathright, paint);

            canvas.drawCircle(35, 30, 2, paint_eye_fill);
            canvas.drawCircle(65, 30, 2, paint_eye_fill);

            Path path = new Path();
            path.moveTo(35,60);
            path.quadTo(50,d,65,60);
            canvas.drawPath(path, paint);
        }
        else{

            float e = d-74;
            e=(float) (e/4);
            Path eyepathleft = new Path();

            eyepathleft.moveTo(27,30);
            eyepathleft.quadTo(35,22-e,43,30);
            eyepathleft.quadTo(35,38+e,27,30);
            canvas.drawPath(eyepathleft, paint_fill);

            eyepathleft.moveTo(27,30);
            eyepathleft.quadTo(35,22-e,43,30);
            eyepathleft.quadTo(35,38+e,27,30);
            canvas.drawPath(eyepathleft, paint);

            Path eyepathright = new Path();

            eyepathright.moveTo(57,30);
            eyepathright.quadTo(65,22-e,73,30);
            eyepathright.quadTo(65,38+e,57,30);
            canvas.drawPath(eyepathright, paint_fill);

            eyepathright.moveTo(57,30);
            eyepathright.quadTo(65,22-e,73,30);
            eyepathright.quadTo(65,38+e,57,30);
            canvas.drawPath(eyepathright, paint);

            canvas.drawCircle(35, 30, 2, paint_eye_fill);
            canvas.drawCircle(65, 30, 2, paint_eye_fill);

            Path path = new Path();
            path.moveTo(35,60);
            path.quadTo(50,73,65,60);
            path.quadTo(50,d,35,60);
            canvas.drawPath(path, paint);
            path.moveTo(35,60);
            path.quadTo(50,73,65,60);
            path.quadTo(50,d,35,60);
            canvas.drawPath(path, paint_fill);
        }
    }
}
