package ca326.com.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CanvasView extends View {

    public Paint mPaint;
    public int width;
    public  int height;
    public int colour;

    private Bitmap mBitmap;
    private Canvas mCanvas;

    public Path mPath;
    public static boolean settings_onion_skin = true;

    Context context;
    AttributeSet attrbs;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    public boolean shouldShowOnionSkin = true;

    public static List<Pair <Path, Paint>> newPaths = new ArrayList<Pair<Path, Paint>>();
    public static List<Pair <Path, Paint>> onionPaths = new ArrayList<Pair<Path, Paint>>();

    public CanvasView(Context c, AttributeSet attrbs) {
        super(c, attrbs);
        this.context=c;
        this.attrbs = attrbs;

        // Set up Paint object
        mPaint = new Paint();
        setUpPaint(Color.BLACK, mPaint, 8);
        this.colour = Color.BLACK;
    }

    // Another Constructor
    public CanvasView(Context c) {
        super(c);
        this.context=c;

        // Set up Paint object
        mPaint = new Paint();
        // setUpPaint(Color.BLACK, mPaint, 8);
        this.colour = Color.BLACK;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 0; i < newPaths.size(); i++) {
            canvas.drawPath(newPaths.get(i).first, newPaths.get(i).second);
        }
        if (shouldShowOnionSkin && settings_onion_skin)
            for(int i = 0; i < onionPaths.size(); i++) {
                canvas.drawPath(onionPaths.get(i).first, onionPaths.get(i).second);
            }
    }

    public void adjustPenSize(int value) {
        setUpPaint(colour, new Paint(), value);
    }

    public void setUpPaint(int color, Paint mPaint, int value) {
        // Set up the paint object with a different selected colour.
        // Have to redo all the set-up steps.
        this.mPaint = mPaint;
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setColor(color);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setStrokeWidth(value); // 8 - 10 good default
    }

    public Path get_new_Path() {
        this.mPath = new Path();
        Log.i("TEST LENGTH: ", mPath + "");
        return mPath;
    }

    private void touch_start(float x, float y) {
        Path ix = this.get_new_Path();
        this.newPaths.add(new Pair(ix,this.mPaint));

        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        System.out.println(this.newPaths);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Action start pushing
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                // Moving around screen
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // No longer pressing here
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    public void clearCanvas() {
        System.out.println("Canvas Cleared!");
        this.newPaths.clear();
        invalidate();

        // Restart with the same colour
        this.mPath = new Path();
        this.newPaths.add(new Pair(mPath, mPaint));
    }

    public void undoAction() {
        System.out.println("Action Undid");
        if (this.newPaths.size() > 0) {
            this.newPaths.remove(this.newPaths.size() - 1);

            // Assign paint object to previous paint obj
            // First check if there does exist a path previously with an IF statement,
            if (this.newPaths.size() != 0) {
                this.mPath = this.newPaths.get(this.newPaths.size() - 1).first;
            } else {
                this.mPath = new Path();
                this.newPaths.add(new Pair(mPath, mPaint));
            }

            // Restart with the same colour
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        this.width = w;      // don't forget these
        this.height = h;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }
}
