package aronb.energyclicker;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CrankView extends View {

    public CrankView(Context context) {
        super(context);
        init(null);
    }
    public CrankView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    public CrankView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }
    public CrankView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    //Define the variables out of onDraw.
    private Rect crankRect;
    private Paint paintCrank;
    private Paint paintCircle;
    private double crankAngle;
    private int crankW;
    private int crankH;
    private int circlex;
    private int circley;


    private void init(@Nullable AttributeSet set){
        crankRect = new Rect();
        paintCircle = new Paint();
        paintCrank = new Paint();
        paintCrank.setColor(Color.GRAY);
        paintCircle.setColor(Color.RED);
        crankAngle = 0;
        crankH = 30;
    }

    @Override
    protected void onDraw(Canvas canvas){
        crankW =(int) Math.round(canvas.getWidth()/1.5);
        //Positions
        canvas.save();
        canvas.rotate((float)crankAngle, canvas.getWidth()/2, canvas.getHeight()/2);
        crankRect.left = canvas.getWidth()/2 - crankW/2;
        crankRect.top = canvas.getHeight()/2 - crankH/2;
        crankRect.right = crankRect.left + crankW;
        crankRect.bottom = crankRect.top + crankH;
        canvas.drawRect(crankRect, paintCrank);
        circlex = crankRect.right - 15;
        circley = crankRect.top + 15;
        canvas.drawCircle(circlex, circley, 10, paintCircle);
        canvas.restore();
    }
    double oldAng = 0;
    public double rotateCrank(float x, float y){
        int cx = crankRect.centerX();
        int cy = crankRect.centerY();
        double rads = Math.atan2(x - cx, y - cy);
        double diff = Math.abs(Math.abs(rads) - Math.abs(oldAng));
        oldAng = rads;
        crankAngle = (rads * (180/Math.PI) * -1) + 90;





        postInvalidate();
        return diff * 1000;



    }

}
