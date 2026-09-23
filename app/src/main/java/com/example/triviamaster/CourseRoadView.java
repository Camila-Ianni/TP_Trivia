package com.example.triviamaster;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
<<<<<<< HEAD
=======
import androidx.annotation.NonNull;
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1

public class CourseRoadView extends View {
    private final Path roadPath = new Path();
    private final Paint roadBorderPaint = createPaint();
    private final Paint roadCorePaint = createPaint();

    public CourseRoadView(Context context) {
        this(context, null);
    }

    public CourseRoadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseRoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        updateFloorTheme(1);
    }

    private static Paint createPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }

    public void updateFloorTheme(int floor) {
        switch (floor) {
            case 1:
                setBackgroundColor(Color.parseColor("#C8ECD7"));
                roadBorderPaint.setColor(Color.parseColor("#52B996"));
                roadCorePaint.setColor(Color.parseColor("#64C5A4"));
                break;
            case 2:
                setBackgroundColor(Color.parseColor("#FFEECB"));
                roadBorderPaint.setColor(Color.parseColor("#EEBA54"));
                roadCorePaint.setColor(Color.parseColor("#F7CE74"));
                break;
            case 3:
                setBackgroundColor(Color.parseColor("#FDE4CB"));
                roadBorderPaint.setColor(Color.parseColor("#DE904A"));
                roadCorePaint.setColor(Color.parseColor("#ECA664"));
                break;
            case 4:
                setBackgroundColor(Color.parseColor("#D4EEFC"));
                roadBorderPaint.setColor(Color.parseColor("#92D2F4"));
                roadCorePaint.setColor(Color.WHITE);
                break;
            default:
                break;
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        buildRoadPath(width, height);
    }

    private void buildRoadPath(int width, int height) {
        roadPath.reset();
        if (width <= 0 || height <= 0) return;

        float density = getResources().getDisplayMetrics().density;
        roadBorderPaint.setStrokeWidth(98f * density);
        roadCorePaint.setStrokeWidth(84f * density);

        float leftX = width * 0.28f;
        float rightX = width * 0.72f;
        float y1 = 110f * density;
        float y2 = 290f * density;
        float y3 = 480f * density;
        float y4 = 670f * density;
        float yEnd = Math.max(height, 780f * density);

        roadPath.moveTo(leftX, 0f);
        roadPath.lineTo(leftX, y1);
        roadPath.cubicTo(leftX, y1 + (y2 - y1) * .45f, rightX,
                y1 + (y2 - y1) * .55f, rightX, y2);
        roadPath.cubicTo(rightX, y2 + (y3 - y2) * .45f, leftX,
                y2 + (y3 - y2) * .55f, leftX, y3);
        roadPath.cubicTo(leftX, y3 + (y4 - y3) * .45f, rightX,
                y3 + (y4 - y3) * .55f, rightX, y4);
        roadPath.cubicTo(rightX, y4 + (yEnd - y4) * .4f, width * .5f,
                y4 + (yEnd - y4) * .7f, width * .5f, yEnd);
    }

    @Override
<<<<<<< HEAD
    protected void onDraw(Canvas canvas) {
=======
    protected void onDraw(@NonNull Canvas canvas) {
>>>>>>> a8a51715ed4a64226f920f547247ba6f61a4a5e1
        super.onDraw(canvas);
        canvas.drawPath(roadPath, roadBorderPaint);
        canvas.drawPath(roadPath, roadCorePaint);
    }
}
