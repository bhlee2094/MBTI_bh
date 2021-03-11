package kr.hongik.mbti;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.ViewHolder> {

    ArrayList<Chat> localDataSet;
    String otherUserNum;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.tvChat);
        }

    }

    public chatAdapter(ArrayList<Chat> dataSet, String otherUserNum) {
        localDataSet = dataSet;
        this.otherUserNum= otherUserNum;
    }

    @Override
    public int getItemViewType(int position){
        if(localDataSet.get(position).userNum.equals(otherUserNum)){
            return 1;
        }else {
            return 2;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);
        if(viewType == 1){
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.right_row_item, viewGroup, false);
        }

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.setText(localDataSet.get(position).getText());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
