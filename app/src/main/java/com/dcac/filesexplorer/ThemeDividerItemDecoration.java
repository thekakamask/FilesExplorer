package com.dcac.filesexplorer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ThemeDividerItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint paint;


    public ThemeDividerItemDecoration(Context context, int attributeColor, int heightDp) {
        paint = new Paint();
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attributeColor, typedValue, true);
        paint.setColor(typedValue.data);
        final float scale = context.getResources().getDisplayMetrics().density;
        paint.setStrokeWidth(heightDp * scale);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + (int) paint.getStrokeWidth();

            canvas.drawLine(left, top, right, bottom, paint);
        }
    }
}
