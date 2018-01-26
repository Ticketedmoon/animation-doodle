package ca326.com.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CanvasView extends View {

    public Paint mPaint;
    public int width;
    public  int height;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    public Path mPath;
    Context context;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    public CanvasView(Context c, AttributeSet attrbs) {
        super(c, attrbs);

        // Other Stuff
        this.context=c;

        // Set up Paint object
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);

        // Path
        this.mPath = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,  mPaint);
    }

    public Path getPath() {
        return this.mPath;
    }

    private void touch_start(float x, float y) {
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
        this.mPath.reset();
        invalidate();
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
