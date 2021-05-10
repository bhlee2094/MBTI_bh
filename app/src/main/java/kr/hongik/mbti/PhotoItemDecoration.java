package kr.hongik.mbti;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PhotoItemDecoration extends RecyclerView.ItemDecoration {
    private final int size10;

    public PhotoItemDecoration(Context context){
        size10 = dpToPx(context,10);
        int size5 = dpToPx(context, 5);
    }

    private int dpToPx(Context context, int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount();

            outRect.top = size10;
            outRect.bottom = size10;


        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        int spanIndex = lp.getSpanIndex();

            outRect.left = size10;
            outRect.right = size10;
    }
}
