package ca326.com.activities;

/**
 * Created by Shane on 18/02/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CanvasViewNonEditable extends View {

    public int width;
    public  int height;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    Context context;
    AttributeSet attrbs;

    public static List<Pair<Path, Paint>> newPaths = new ArrayList<Pair<Path, Paint>>();

    public CanvasViewNonEditable(Context c, AttributeSet attrbs) {
        super(c, attrbs);
        this.context=c;
        this.attrbs = attrbs;
    }

    public CanvasViewNonEditable(Context context) {
        super(context);
        this.context = context;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 0; i < newPaths.size(); i++) {
            canvas.drawPath(newPaths.get(i).first, newPaths.get(i).second);
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

